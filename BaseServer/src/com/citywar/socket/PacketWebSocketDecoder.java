/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.socket;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 严格模式数据包解析器，用于数据解密及数据包创建
 * 
 * @author sky
 * @date 2011-9-20
 * @version
 * 
 */
public class PacketWebSocketDecoder extends CumulativeProtocolDecoder {
	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(PacketWebSocketDecoder.class);

	/*
	 * 解码接收到的数据包
	 * 
	 * @see org.apache.mina.filter.codec.CumulativeProtocolDecoder#doDecode(org.
	 * apache .mina.core.session.IoSession,
	 * org.apache.mina.core.buffer.IoBuffer,
	 * org.apache.mina.filter.codec.ProtocolDecoderOutput)
	 */
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() < 4) {
			// 剩余不足4字节，不足以解析数据包头，暂不处理
			return false;
		}

		int size = in.remaining();

		byte[] buf = new byte[size];
		in.get(buf, 0, size);
		String key = new String(buf);

		Integer hasHandshake = getContext(session);
		if (hasHandshake == 0 && key.indexOf("Key") > 0) {

			key = key.substring(0, key.indexOf("==") + 2);
			key = key.substring(key.indexOf("Key") + 4, key.length()).trim();
			key += "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(key.getBytes("utf-8"), 0, key.length());
			byte[] sha1Hash = md.digest();
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			key = encoder.encode(sha1Hash);

			String newLine = (String) java.security.AccessController
					.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));
			String strResponse = "HTTP/1.1 101 Switching Protocols" + newLine;
			strResponse += "Upgrade: websocket" + newLine;
			strResponse += "Connection: Upgrade" + newLine;
			strResponse += "Sec-WebSocket-Accept: " + key;

			session.setAttribute(PacketWebSocketDecoder.class, hasHandshake + 1);
			session.write(IoBuffer.wrap(strResponse.getBytes()));
			return false;
		}

		in.position(0);
		byte[] first = new byte[1];
		// 这里会阻塞
		in.get(first, 0, 1);

		int b = first[0] & 0xFF;
		// 1为字符数据，8为关闭socket
		byte opCode = (byte) (b & 0x0F);

		if (opCode == 8) {
			session.close(true);
			return true;
		}
		b = in.get();
		int payloadLength = b & 0x7F;
		if (payloadLength == 126) {
			byte[] extended = new byte[2];
			in.get(extended, 0, 2);
			int shift = 0;
			payloadLength = 0;
			for (int i = extended.length - 1; i >= 0; i--) {
				payloadLength = payloadLength + ((extended[i] & 0xFF) << shift);
				shift += 8;
			}

		} else if (payloadLength == 127) {
			byte[] extended = new byte[8];
			in.get(extended, 0, 8);
			int shift = 0;
			payloadLength = 0;
			for (int i = extended.length - 1; i >= 0; i--) {
				payloadLength = payloadLength + ((extended[i] & 0xFF) << shift);
				shift += 8;
			}
		}

		// 掩码
		byte[] mask = new byte[4];
		in.get(mask, 0, 4);
		int readThisFragment = 1;
		ByteBuffer byteBuf = ByteBuffer.allocate(payloadLength + 10);
		byteBuf.put("echo: ".getBytes("UTF-8"));
		while (payloadLength > 0) {
			int masked = in.get();
			masked = masked ^ (mask[(int) ((readThisFragment - 1) % 4)] & 0xFF);
			byteBuf.put((byte) masked);
			payloadLength--;
			readThisFragment++;
		}
		byteBuf.flip();

		out.write(byteBuf);

		return true;
	}

	// 获取密钥上下文
	private Integer getContext(IoSession session) {
		Integer hasHandshake = (Integer) session.getAttribute(PacketWebSocketDecoder.class);

		if (hasHandshake == null) {
			hasHandshake = 0;
			session.setAttribute(PacketWebSocketDecoder.class, hasHandshake);
		}

		return hasHandshake;
	}

	// 解密整段数据
	@SuppressWarnings("unused")
	private byte[] decrypt(byte[] data, int[] decryptKey) throws Exception {
		if (data.length == 0)
			return data;

		if (decryptKey.length < 8)
			throw new Exception("The decryptKey must be 64bits length!");

		int length = data.length;
		int lastCipherByte;
		int plainText;
		int key;

		// 解密首字节
		lastCipherByte = data[0] & 0xff;
		data[0] ^= decryptKey[0];

		for (int index = 1; index < length; index++) {
			// 解密当前字节
			key = ((decryptKey[index & 0x7] + lastCipherByte) ^ index);
			plainText = (((data[index] & 0xff) - lastCipherByte) ^ key) & 0xff;

			// 更新变量值
			lastCipherByte = data[index] & 0xff;
			data[index] = (byte) plainText;
			decryptKey[index & 0x7] = key & 0xff;
		}

		return data;
	}
}
