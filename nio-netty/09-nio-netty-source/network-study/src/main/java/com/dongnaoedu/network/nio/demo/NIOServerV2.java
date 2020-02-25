package com.dongnaoedu.network.nio.demo;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 结合Selector实现非阻塞服务器
 */
public class NIOServerV2 {

    public static void main(String[] args) throws Exception {
        // 1. 创建服务端的channel对象
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false); // 设置为非阻塞模式

        // 2. 创建Selector
        Selector selector = Selector.open();

        // 3. 把服务端的channel注册到selector，注册accept事件
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);

        // 4. 绑定端口，启动服务
        serverSocketChannel.socket().bind(new InetSocketAddress(8080)); // 绑定端口
        System.out.println("启动成功");

        while (true) {
            // 5. 启动selector（管家）
            selector.select();// 阻塞，直到事件通知才会返回

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("收到新连接：" + socketChannel);
                } else if (key.isReadable()) {// 客户端连接有数据可以读时触发
                    try {
                        SocketChannel socketChannel = (SocketChannel) key.channel();

                        ByteBuffer requestBuffer = ByteBuffer.allocate(1024);
                        while (socketChannel.isOpen() && socketChannel.read(requestBuffer) != -1) {
                            // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
                            if (requestBuffer.position() > 0) break;
                        }
                        if (requestBuffer.position() == 0) continue; // 如果没数据了, 则不继续后面的处理
                        requestBuffer.flip();
                        byte[] content = new byte[requestBuffer.remaining()];
                        requestBuffer.get(content);
                        System.out.println(new String(content));
                        System.out.println("收到数据,来自：" + socketChannel.getRemoteAddress());
                        // TODO 业务操作 数据库 接口调用等等

                        // 响应结果 200
                        String response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Length: 11\r\n\r\n" +
                                "Hello World";
                        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                        while (buffer.hasRemaining()) {
                            socketChannel.write(buffer);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        key.cancel();
                    }
                }
            }
        }
//        while (true) {
//            SocketChannel socketChannel = serverSocketChannel.accept(); // 获取新tcp连接通道
//            // tcp请求 读取/响应
//            if (socketChannel != null) {
//                System.out.println("收到新连接 : " + socketChannel.getRemoteAddress());
//                socketChannel.configureBlocking(false); // 默认是阻塞的,一定要设置为非阻塞
//                channels.add(socketChannel);
//            } else {
//                // 没有新连接的情况下,就去处理现有连接的数据,处理完的就删除掉
//                Iterator<SocketChannel> iterator = channels.iterator();
//                while (iterator.hasNext()) {
//                    SocketChannel ch = iterator.next();
//                    try {
//                        ByteBuffer requestBuffer = ByteBuffer.allocate(1024);
//
//                        if (ch.read(requestBuffer) == 0) {
//                            // 等于0,代表这个通道没有数据需要处理,那就待会再处理
//                            continue;
//                        }
//                        while (ch.isOpen() && ch.read(requestBuffer) != -1) {
//                            // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
//                            if (requestBuffer.position() > 0) break;
//                        }
//                        if (requestBuffer.position() == 0) continue; // 如果没数据了, 则不继续后面的处理
//                        requestBuffer.flip();
//                        byte[] content = new byte[requestBuffer.limit()];
//                        requestBuffer.get(content);
//                        System.out.println(new String(content));
//                        System.out.println("收到数据,来自：" + ch.getRemoteAddress());
//
//                        // 响应结果 200
//                        String response = "HTTP/1.1 200 OK\r\n" +
//                                "Content-Length: 11\r\n\r\n" +
//                                "Hello World";
//                        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
//                        while (buffer.hasRemaining()) {
//                            ch.write(buffer);
//                        }
//                        iterator.remove();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        iterator.remove();
//                    }
//                }
//            }
//        }
    }
}
