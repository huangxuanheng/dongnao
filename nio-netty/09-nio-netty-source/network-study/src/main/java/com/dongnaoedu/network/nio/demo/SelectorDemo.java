package com.dongnaoedu.network.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorDemo {
    public static void main(String[] args) throws IOException {
        // 创建网络服务端
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 创建Selector
        Selector selector = Selector.open();
        serverSocketChannel.configureBlocking(false); // 设置为非阻塞模式
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);// serverSocketChannel注册OP_READ事件
        serverSocketChannel.socket().bind(new InetSocketAddress(8080)); // 绑定端口
        while(true) {

            int readyChannels = selector.select();// 会阻塞，直到有事件触发

            if(readyChannels == 0) continue;

            Set<SelectionKey> selectedKeys = selector.selectedKeys();// 获取被触发的事件集合

            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while(keyIterator.hasNext()) {

                SelectionKey key = keyIterator.next();

                if(key.isAcceptable()) {
                    SocketChannel socket = ((ServerSocketChannel) key.channel()).accept();
                    socket.register(selector, SelectionKey.OP_READ);
                    // serverSocketChannel 收到一个新连接，只能作用于ServerSocketChannel

                } else if (key.isConnectable()) {
                    // 连接到远程服务器，只在客户端异步连接时生效

                } else if (key.isReadable()) {
                    // SocketChannel 中有数据可以读

                } else if (key.isWritable()) {
                    // SocketChannel 可以开始写入数据
                }

                // 将已处理的事件移除
                keyIterator.remove();
            }
        }

    }
}
