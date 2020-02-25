package com.dongnaoedu.network.nio.demo;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
        while (!socketChannel.finishConnect()) {
            // 没连接上,则一直等待
            Thread.yield();
        }
        for (int i = 0; i < 10; i++) {
            String str = "h";
//            for (int j = 0; j < 2000; j++) {
//                str += j;
//            }
            socketChannel.write(ByteBuffer.wrap(str.getBytes()));
            Thread.sleep(1);
        }
        socketChannel.close();
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("请输入：");
//        // 发送内容
//        String msg = scanner.nextLine();
//        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
//        while (buffer.hasRemaining()) {
//            socketChannel.write(buffer);
//        }
//        // 读取响应
//        System.out.println("收到服务端响应:");
//        ByteBuffer requestBuffer = ByteBuffer.allocate(1024);
//
//        while (socketChannel.isOpen() && socketChannel.read(requestBuffer) != -1) {
//            // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
//            if (requestBuffer.position() > 0) break;
//        }
//        requestBuffer.flip();
//        byte[] content = new byte[requestBuffer.limit()];
//        requestBuffer.get(content);
//        System.out.println(new String(content));
//        scanner.close();
//        socketChannel.close();
    }

}
