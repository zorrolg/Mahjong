/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import com.citywar.define.ManagerPacketType;
import com.citywar.socket.BaseConnector;
import com.citywar.socket.Handler;
import com.citywar.socket.Packet;

/**
 * 客户端---管理者连接登录的入口
 * 
 * @author Dream
 * @date 2011-9-16
 * @version
 * 
 */
public class ClientConnetor extends BaseConnector
{
    public ClientConnetor(String host, int port) throws UnknownHostException,
            IOException
    {
        Handler handler = new ClientManagerPackethandle();
        connectTo(host, port);
        this.setHandler(handler);
    }

    /**
     * 用户登录
     */
    private void login()
    {
        Packet pkg = new Packet(ManagerPacketType.MANAGER_LOGIN);
        pkg.putStr(getKey());
        sendTCP(pkg);
    }

    /**
     * 发送命令
     * 
     * @param cmd
     */
    private void sendCmd(String cmd)
    {
        Packet pkg = new Packet(ManagerPacketType.MANAGER_CMD);
        pkg.putStr(cmd);
        sendTCP(pkg);
    }

    /**
     * 执行管理命令
     */
    public void execute()
    {
        if (isConnection())
        {
            System.out.println("Connection is successed!");
            login();
        }
        else
        {
            System.out.println("Connection is failed!");
            return;
        }

        while (true)
        {
            try
            {
                System.out.print("> ");
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(System.in));
                String line = reader.readLine();
                if (line.length() < 1)
                    break;
                if (line.toCharArray()[0] == '/')
                {
                    line = line.replaceFirst("/", "&");
                }
                if (line == "quit")
                {
                    break;
                }
                if (isConnection())
                {
                    sendCmd(line);
                }
                else
                {
                    System.out.println("didn't connect to andy server!");
                }
            }
            catch (Exception ex)
            {
                System.out.println("Error:" + ex.getMessage());
            }
        }

        System.exit(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dice.BaseClient#onConnect()
     */
    @Override
    public void onConnect()
    {
        // 此处使用新线程执行输入捕获，否则会阻塞mina的processor线程
        Executors.newFixedThreadPool(1).execute(new Runnable()
        {

            @Override
            public void run()
            {
                execute();
            }
        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dice.BaseClient#onDisconnect()
     */
    @Override
    public void onDisconnect()
    {
        System.out.printf("disconnect to remote server!%n");
    }
}
