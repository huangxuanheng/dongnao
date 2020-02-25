package com.dongnaoedu.network.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NIOClientV2 {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("localhost", 8080));

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (selectionKey.isConnectable()) { // 连接到远程服务器
                    try {
                        if (socketChannel.finishConnect()) { // 完成连接
                            // 连接成功
                            System.out.println("连接成功-" + socketChannel);

                            ByteBuffer buffer = ByteBuffer.allocateDirect(20480);

                            // 切换到感兴趣的事件
                            selectionKey.attach(buffer);
                            selectionKey.interestOps(SelectionKey.OP_WRITE);
                        }
                    } catch (IOException e) {
                        // 连接失败
                        e.printStackTrace();
                        return;
                    }
                } else if (selectionKey.isWritable()) {// 可以开始写数据
                    ByteBuffer buf = (ByteBuffer) selectionKey.attachment();
                    buf.clear();
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("请输入：");
                    // 发送内容
                    String msg = scanner.nextLine();
                    scanner.close();

                    buf.put(msg.getBytes());
                    buf.flip();

                    while (buf.hasRemaining()) {
                        socketChannel.write(buf);
                    }

                    // 切换到感兴趣的事件
                    selectionKey.interestOps(SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {// 可以开始读数据
                    // 读取响应
                    System.out.println("收到服务端响应:");
                    ByteBuffer requestBuffer = ByteBuffer.allocate(1024);

                    while (socketChannel.isOpen() && socketChannel.read(requestBuffer) != -1) {
                        // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
                        if (requestBuffer.position() > 0) break;
                    }
                    requestBuffer.flip();
                    byte[] content = new byte[requestBuffer.remaining()];
                    requestBuffer.get(content);
                    System.out.println(new String(content));
//                    selectionKey.interestOps(SelectionKey.OP_WRITE);

                }
            }
        }
    }

}
