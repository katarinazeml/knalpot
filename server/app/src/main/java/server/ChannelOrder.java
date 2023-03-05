package server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

// Define inbound and outbound handlers that will process requests and output in the correct order.

public class ChannelOrder extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        ch.pipeline().addLast(
                new RequestDecoder(),
                new ResponseDataEncoder(),
                new ProcessingHandler());
    }
}
