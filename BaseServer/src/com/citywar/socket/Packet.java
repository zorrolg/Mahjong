package com.citywar.socket;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * 游戏协议包类，描述具体游戏数据包结构
 * 
 */
public class Packet {

	// private static final Logger logger =
	// Logger.getLogger(Packet.class.getName());
	/**
	 * 数据类型长度
	 **/
	// public static final int BYTE_LEN = 1;
	// public static final int BOOLEN_LEN = 1;
	// public static final int CHAR_LEN = 1;
	// public static final int SHORT_LEN = 2;
	// public static final int INT_LEN = 4;
	// public static final int FLOAT_LEN = 4;
	// public static final int DATE_LEN = 7;
	// public static final int DOUBLE_LEN = 8;
	// public static final int LONG_LEN = 8;
	//
	public static final short HDR_SIZE = 20;
	public static final short HEADER = 0x71ab;
	// private short len;
	// private short checksum = 0;
	// private short code;
	// private int clientId;
	// private int param1;
	// private int param2;
	// private ByteBuffer buffer;

	private short code;
	private int clientId;
	private int param1;
	private int param2;

	private JSONArray list;
	private int index;

	public Packet(String strContent) {

		this.code = 0;
		this.index = 0;

		org.json.JSONObject result;

		try {
			result = new org.json.JSONObject(strContent);
			this.code = (short) result.getInt("code");
			this.list = result.getJSONArray("data");

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public Packet(int code) {
		this.code = (short) code;
		this.list = new JSONArray();
	}

	public Packet(short code) {
		this.code = code;
		this.list = new JSONArray();
	}

	public Packet(short code, int clientId) {
		this.code = code;
		this.clientId = clientId;
		this.list = new JSONArray();
	}

	public boolean isHasData() {
		return this.index < this.list.length();
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public void setCode(short code) {
		this.code = code;
	}

	public void setParam1(int param1) {
		this.param1 = param1;
	}

	public void setParam2(int param2) {
		this.param2 = param2;
	}

	public void position(int param2) {
		this.index = 0;
	}

	public void clearCheckSum() {
		return;
	}

	public short getChecksum() {
		return 0;
	}

	public int getClientId() {
		return clientId;
	}

	public short getCode() {
		return code;
	}

	public int getParam1() {
		return param1;
	}

	public int getParam2() {
		return param2;
	}

	public String getJsonDate() {
		org.json.JSONObject result = new org.json.JSONObject();

		try {

			result.put("code", this.code);
			result.put("data", this.list);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result.toString();
	}

	public boolean getBoolean() {

		String temp = "";
		boolean valve = false;
		try {
			temp = this.list.get(this.index).toString();
			valve = Boolean.valueOf(temp);
			this.index++;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return valve;
	}

	public byte getByte() {
		String temp = "";
		byte valve = 0;
		try {
			temp = this.list.get(this.index).toString();
			valve = Byte.valueOf(temp);
			this.index++;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return valve;
	}

	public byte[] getBytes() {
		String temp = "";
		byte[] valve = {};
		try {
			temp = this.list.get(this.index).toString();
			valve = temp.getBytes("UTF8");
			this.index++;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return valve;
	}

	public void getBytes(byte[] dst, int offset, int len) {

	}

	public int getChar() {
		String temp = "";
		int valve = 0;
		try {
			temp = this.list.get(this.index).toString();
			valve = Integer.valueOf(temp);
			this.index++;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return valve;

	}

	public Date getDate() {
		// int year = buffer.getShort();
		// int month = buffer.get();
		// int date = buffer.get();
		// int hourOfDay = buffer.get();
		// int minute = buffer.get();
		// int second = buffer.get();

		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(0, 0, 0, 0, 0, 0);
		return calendar.getTime();
	}

	public double getDouble() {
		String temp = "";
		double valve = 0;
		try {
			temp = this.list.get(this.index).toString();
			valve = Double.valueOf(temp);
			this.index++;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return valve;
	}

	public float getFloat() {
		String temp = "";
		float valve = 0;
		try {
			temp = this.list.get(this.index).toString();
			valve = Float.valueOf(temp);
			this.index++;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return valve;
	}

	public int getInt() {
		String temp = "";
		int valve = 0;
		try {
			temp = this.list.get(this.index).toString();
			valve = Integer.valueOf(temp);
			this.index++;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return valve;
	}

	public long getLong() {
		String temp = "";
		long valve = 0;
		try {
			temp = this.list.get(this.index).toString();
			valve = Long.valueOf(temp);
			this.index++;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return valve;
	}

	public short getShort() {
		String temp = "";
		short valve = 0;
		try {
			temp = this.list.get(this.index).toString();
			valve = Short.valueOf(temp);
			this.index++;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return valve;
	}

	public String getStr() {

		String valve = "";
		try {
			valve = this.list.get(this.index).toString();
			this.index++;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return valve;
	}

	public void putBoolean(boolean b) {
		String temp = String.valueOf(b);
		this.list.put(temp);
		this.index++;
	}

	public void putByte(byte b) {
		String temp = String.valueOf(b);
		this.list.put(temp);
		this.index++;
	}

	public void putByte(byte[] bytes) {
		String temp = String.valueOf(bytes);
		this.list.put(temp);
		this.index++;
	}

	public void putChar(char c) {
		String temp = String.valueOf(c);
		this.list.put(temp);
		this.index++;
	}

	public void putDate(Date date) {

	}

	public void putDouble(double value) {
		String temp = String.valueOf(value);
		this.list.put(temp);
		this.index++;
	}

	public void putFloat(float value) {
		String temp = String.valueOf(value);
		this.list.put(temp);
		this.index++;
	}

	public void putInt(int l) {
		String temp = String.valueOf(l);
		this.list.put(temp);
		this.index++;
	}

	public void putLong(Long i) {
		String temp = String.valueOf(i);
		this.list.put(temp);
		this.index++;
	}

	public void putShort(short s) {
		String temp = String.valueOf(s);
		this.list.put(temp);
		this.index++;
	}

	public void putStr(String str) {
		this.list.put(str);
		this.index++;
	}

	public int getLength() {
		return this.list.length();
	}

	public JSONArray getList() {
		return this.list;
	}

	public void insertClientId(int id) {
		// expand(INT_LEN);
		// // 创建新的数据+4
		// ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity());
		// newBuffer.putInt(id);
		// newBuffer.put(buffer.array(), 0, len);
		// buffer = newBuffer;
		// len = (short) buffer.position();
	}

	public void insertClientIdAtHead(int id) {
		// expand(INT_LEN);
		// // 创建新的数据+4
		// ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity());
		// newBuffer.put(buffer.array(), 0, HDR_SIZE);
		// newBuffer.putInt(id);
		// newBuffer.put(buffer.array(), HDR_SIZE + 4, len - HDR_SIZE);
		// buffer = newBuffer;
		// len = (short) buffer.position();
	}

	public void readHeader() {
		// buffer.position(0);
		// getShort(); // 跳过数据包分隔符
		// len = buffer.getShort();
		// checksum = buffer.getShort();
		// code = buffer.getShort();
		// clientId = buffer.getInt();
		// param1 = buffer.getInt();
		// param2 = buffer.getInt();
	}

	public void writeHeader() {
		// // 以实际包长限制当前位置，否则在原样转发数据包时包长度只会从当前position截断
		// // commented by sky
		// buffer.position(len);
		//
		// buffer.flip();
		// len = (short) buffer.limit();
		// buffer.putShort(HEADER); // 插入数据包分割符
		// buffer.putShort(len);
		//
		// // 先跳过校验和
		// buffer.position(6);
		// buffer.putShort(code);
		// buffer.putInt(clientId);
		// buffer.putInt(param1);
		// buffer.putInt(param2);
		//
		// if (checksum == 0)
		// // 计算校验和
		// checksum = calcChecksum();
		// // 写入校验和
		// buffer.position(4);
		// buffer.putShort(checksum);
		//
		// buffer.position(len);
	}

	public void writePacket(byte[] bytes, int offset, int length) {
		// expand(length);
		// buffer.position(0);
		// buffer.put(bytes, offset, length);
	}

	public void putPacket(Packet packet) {

	}

	public short calcChecksum() {
		// byte[] pak = buffer.array();
		// int val = 0x77;
		// int i = 6;
		// int size = len;
		// while (i < size)
		// {
		// val += (pak[i++] & 0xFF);
		// }
		// return (short) (val & 0x7F7F);
		return (short) 0;
	}

	public void flip() {
		// buffer.flip();
	}

	public short getLen() {
		return 0;
	}

	//
	//
	// public Packet()
	// {
	// // TODO peter 这里为什么需要默认构造函数
	// }
	//
	// public Packet(int capacity)
	// {
	// if (capacity < HDR_SIZE)
	// {
	// buffer = ByteBuffer.allocate(HDR_SIZE);
	// }
	// else
	// {
	// buffer = ByteBuffer.allocate(capacity);
	// }
	// }
	//
	// public Packet(short code)
	// {
	// this(1024);
	// this.code = code;
	// buffer.position(HDR_SIZE);
	// len = (short) buffer.position();
	// }
	//
	// public Packet(short code, int clientId)
	// {
	// this(1024);
	// this.code = code;
	// this.clientId = clientId;
	// buffer.position(HDR_SIZE);
	// len = (short) buffer.position();
	// }
	//
	// public Packet(short code, int clientid, int capacity)
	// {
	// this(capacity);
	// this.code = code;
	// this.clientId = clientid;
	// buffer.position(HDR_SIZE);
	// len = (short) buffer.position();
	// }
	//
	// public byte[] array()
	// {
	// if (buffer.hasArray())
	// {
	// return buffer.array();
	// }
	// return null;
	// }
	//
	// public int arrayOffset()
	// {
	// return buffer.arrayOffset();
	// }
	//
	// public short calcChecksum()
	// {
	// byte[] pak = buffer.array();
	// int val = 0x77;
	// int i = 6;
	// int size = len;
	// while (i < size)
	// {
	// val += (pak[i++] & 0xFF);
	// }
	// return (short) (val & 0x7F7F);
	// }
	//
	// public void clearCheckSum()
	// {
	// checksum = 0;
	// }
	//
	// /**
	// * 深拷贝，对当前数据包进行完全复制
	// */
	// public Packet clone()
	// {
	// Packet packet = new Packet(code);
	// packet.writePacket(buffer.array(), buffer.arrayOffset(), len);
	//
	// packet.len = len;
	// packet.clientId = clientId;
	// packet.param1 = param1;
	// packet.param2 = param2;
	// packet.checksum = 0;
	//
	// return packet;
	// }
	//
	// /**
	// * 存储边界检查，当存储空间不足时自动扩容当前容量的一倍
	// *
	// * @param expected
	// * 预期的存储长度
	// */
	// public void expand(int expected)
	// {
	// int pos = position();
	// if (pos + expected > buffer.capacity())
	// {
	// int newCapacity = Math.max(buffer.capacity() * 2, pos + expected);
	// ByteBuffer newBuf = ByteBuffer.allocate(newCapacity);
	// buffer.flip();
	// newBuf.put(buffer);
	//
	// buffer = newBuf;
	// }
	// }
	//
	// public void flip()
	// {
	// buffer.flip();
	// }
	//
	// public boolean getBoolean()
	// {
	// return buffer.get() > 0;
	// }
	//
	// public byte getByte()
	// {
	// return buffer.get();
	// }
	//
	// public byte[] getBytes()
	// {
	// int temp = len - position();
	// byte[] dst = new byte[temp];
	// buffer.get(dst, 0, temp);
	// return dst;
	// }
	//
	// public void getBytes(byte[] dst, int offset, int len)
	// {
	// buffer.get(dst, offset, len);
	// }
	//
	// public int getChar()
	// {
	// return buffer.getChar();
	// }
	//
	// public short getChecksum()
	// {
	// return checksum;
	// }
	//
	// public int getClientId()
	// {
	// return clientId;
	// }
	//
	// public short getCode()
	// {
	// return code;
	// }
	//
	// public Date getDate()
	// {
	// int year = buffer.getShort();
	// int month = buffer.get();
	// int date = buffer.get();
	// int hourOfDay = buffer.get();
	// int minute = buffer.get();
	// int second = buffer.get();
	// java.util.Calendar calendar = java.util.Calendar.getInstance();
	// calendar.set(year, month, date, hourOfDay, minute, second);
	// return calendar.getTime();
	// }
	//
	// public double getDouble()
	// {
	// double temp = buffer.getDouble();
	// return temp;
	// }
	//
	// public float getFloat()
	// {
	// float temp = buffer.getFloat();
	// return temp;
	// }
	//
	// public int getInt()
	// {
	// return buffer.getInt();
	// }
	//
	// public short getLen()
	// {
	// return len;
	// }
	//
	// public long getLong()
	// {
	// return buffer.getLong();
	// }
	//
	// public Packet getPacket()
	// {
	// short len = buffer.getShort();
	// byte[] data = new byte[len];
	// buffer.get(data);
	//
	// Packet packet = new Packet(len);
	// packet.writePacket(data, 0, len);
	// packet.readHeader();
	//
	// return packet;
	// }
	//
	// public int getParam1()
	// {
	// return param1;
	// }
	//
	// public int getParam2()
	// {
	// return param2;
	// }
	//
	// public short getShort()
	// {
	// return buffer.getShort();
	// }
	//
	// public String getStr()
	// {
	// int strLen = buffer.getShort();
	// byte[] bytes = new byte[strLen];
	// buffer.get(bytes, 0, strLen);
	//
	// try
	// {
	// return new String(bytes, "utf-8");
	// }
	// catch (UnsupportedEncodingException e)
	// {
	// logger.error("[ Packet : getStr ]", e);
	// return null;
	// }
	// }
	//
	// public int limit()
	// {
	// return buffer.limit();
	// }
	//
	// public int position()
	// {
	// return buffer.position();
	// }
	//
	// public void position(int newPosition)
	// {
	// buffer.position(newPosition);
	// }
	//
	// public void putBoolean(boolean b)
	// {
	// byte temp = (byte) (b == true ? 1 : 0);
	// expand(BOOLEN_LEN);
	// buffer.put(temp);
	// len = (short) buffer.position();
	// }
	//
	// public void putByte(byte b)
	// {
	// expand(BYTE_LEN);
	// buffer.put(b);
	// len = (short) buffer.position();
	// }
	//
	// public void putByte(byte[] bytes)
	// {
	// expand(bytes.length);
	// buffer.put(bytes);
	// len = (short) buffer.position();
	// }
	//
	// public void putChar(char c)
	// {
	// expand(CHAR_LEN);
	// buffer.putChar(c);
	// len = (short) buffer.position();
	// }
	//
	// public void putDate(Date date)
	// {
	// expand(DATE_LEN);
	// java.util.Calendar calendar = java.util.Calendar.getInstance();
	// calendar.setTime(date);
	// buffer.putShort((short) calendar.get(Calendar.YEAR));
	// buffer.put((byte) (calendar.get(Calendar.MONTH) + 1));
	// buffer.put((byte) calendar.get(Calendar.DATE));
	// buffer.put((byte) calendar.get(Calendar.HOUR_OF_DAY));
	// buffer.put((byte) calendar.get(Calendar.MINUTE));
	// buffer.put((byte) calendar.get(Calendar.SECOND));
	// len = (short) buffer.position();
	// }
	//
	// public void putDouble(double value)
	// {
	// expand(DOUBLE_LEN);
	// buffer.putDouble(value);
	// len = (short) buffer.position();
	// }
	//
	// public void putFloat(float value)
	// {
	// expand(FLOAT_LEN);
	// buffer.putFloat(value);
	// len = (short) buffer.position();
	// }
	//
	// public void putInt(int l)
	// {
	// expand(INT_LEN);
	// buffer.putInt(l);
	// len = (short) buffer.position();
	// }
	//
	// public void insertClientId(int id)
	// {
	// expand(INT_LEN);
	// // 创建新的数据+4
	// ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity());
	// newBuffer.putInt(id);
	// newBuffer.put(buffer.array(), 0, len);
	// buffer = newBuffer;
	// len = (short) buffer.position();
	// }
	//
	// public void insertClientIdAtHead(int id)
	// {
	// expand(INT_LEN);
	// // 创建新的数据+4
	// ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity());
	// newBuffer.put(buffer.array(), 0, HDR_SIZE);
	// newBuffer.putInt(id);
	// newBuffer.put(buffer.array(), HDR_SIZE + 4, len - HDR_SIZE);
	// buffer = newBuffer;
	// len = (short) buffer.position();
	// }
	//
	// public void putLong(Long i)
	// {
	// expand(LONG_LEN);
	// buffer.putLong(i);
	// len = (short) buffer.position();
	// }
	//
	// public void putPacket(Packet packet)
	// {
	// try
	// {
	// packet.writeHeader();
	// short len = packet.getLen();
	//
	// expand(len + SHORT_LEN);
	//
	// buffer.putShort(len);
	//
	// buffer.put(packet.toBytes());
	// }
	// catch (Exception e)
	// {
	// logger.error("[ Packet : getStr ]", e);
	// }
	//
	// len = (short) buffer.position();
	// }
	//
	// public void putShort(short s)
	// {
	// expand(SHORT_LEN);
	// buffer.putShort(s);
	// len = (short) buffer.position();
	// }
	//
	// public void putStr(String str)
	// {
	// try
	// {
	// if (str == null)
	// {
	// str = "";
	// }
	// byte[] bytes = str.getBytes("utf-8");
	//
	// expand(bytes.length + SHORT_LEN);
	//
	// buffer.putShort((short) bytes.length);
	// buffer.put(bytes);
	// }
	// catch (UnsupportedEncodingException e)
	// {
	// logger.error("[ Packet : getStr ]", e);
	// }
	// len = (short) buffer.position();
	// }
	//
	// public void readHeader()
	// {
	// buffer.position(0);
	// getShort(); // 跳过数据包分隔符
	// len = buffer.getShort();
	// checksum = buffer.getShort();
	// code = buffer.getShort();
	// clientId = buffer.getInt();
	// param1 = buffer.getInt();
	// param2 = buffer.getInt();
	// }
	//
	// public void setClientId(int clientId)
	// {
	// this.clientId = clientId;
	// }
	//
	// public void setCode(short code)
	// {
	// this.code = code;
	// }
	//
	// public void setParam1(int param1)
	// {
	// this.param1 = param1;
	// }
	//
	// public void setParam2(int param2)
	// {
	// this.param2 = param2;
	// }
	//
	// /**
	// * 将packet转换为byte数组，即读取整个数据包的字节序列（含包头部分）
	// *
	// * @return
	// */
	// public byte[] toBytes()
	// {
	// byte[] temp = new byte[len];
	// buffer.position(0);
	// buffer.get(temp);
	// buffer.position(len);
	//
	// return temp;
	// }
	//
	// public void toEnd()
	// {
	// buffer.position(buffer.limit());
	// }
	//
	// public void writeHeader()
	// {
	// // 以实际包长限制当前位置，否则在原样转发数据包时包长度只会从当前position截断
	// // commented by sky
	// buffer.position(len);
	//
	// buffer.flip();
	// len = (short) buffer.limit();
	// buffer.putShort(HEADER); // 插入数据包分割符
	// buffer.putShort(len);
	//
	// // 先跳过校验和
	// buffer.position(6);
	// buffer.putShort(code);
	// buffer.putInt(clientId);
	// buffer.putInt(param1);
	// buffer.putInt(param2);
	//
	// if (checksum == 0)
	// // 计算校验和
	// checksum = calcChecksum();
	// // 写入校验和
	// buffer.position(4);
	// buffer.putShort(checksum);
	//
	// buffer.position(len);
	// }
	//
	// public void writePacket(byte[] bytes, int offset, int length)
	// {
	// expand(length);
	// buffer.position(0);
	// buffer.put(bytes, offset, length);
	// }
	//
	// /**
	// * 是否还可以读数据
	// * @return
	// */
	// public boolean isHasData(){
	// if(position() < limit())
	// {
	// return true;
	// }
	// return false;
	// }
	// /**
	// * @author dansen
	// * @date 2011-6-23
	// */
	// public void clearContext()
	// {
	// buffer.position(HDR_SIZE);
	// len = HDR_SIZE;
	// checksum = 0;
	// }
	//
	// public static void main(String[] args)
	// {
	// Packet packet = new Packet((short) 100);
	// packet.putInt(1);
	// packet.putInt(2);
	// packet.putShort((short) 3);
	// packet.insertClientIdAtHead(4);
	// packet.position(HDR_SIZE);
	// //System.out.println(packet.getInt());
	// //System.out.println(packet.getInt());
	// //System.out.println(packet.getInt());
	// //System.out.println(packet.getShort());
	// //System.out.println(packet.getDouble());
	// }
	//
	// /**
	// * @param
	// * @return
	// * @exception
	// */
	// public void compress()
	// {
	// // 对HDR_SIZE后的数据进行压缩
	// byte[] temp = CompressUtil.compress(buffer.array(), HDR_SIZE, len
	// - HDR_SIZE);// CommonUtil.compress(buffer, HDR_SIZE, len -
	// // HDR_SIZE);
	// buffer.position(HDR_SIZE);
	// buffer.put(temp);
	// len = (short) (temp.length + HDR_SIZE);
	// }

}
