package server;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ProcessingHandler
        extends ChannelInboundHandlerAdapter {
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("Server " + incoming.remoteAddress() + " has joined");
        }
        channels.add(ctx.channel());
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("Server " + incoming.remoteAddress() + " has disconnected");
        }
        channels.remove(ctx.channel());
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        RequestData requestData = (RequestData) msg;
        ResponseData responseData = new ResponseData();
        responseData.setIntValue(requestData.getIntValue());
        responseData.setStringValue(requestData.getStringValue());
        responseData.setFloatValue(requestData.getFloatValue());
        System.out.println("sent back response from server");
        // ChannelFuture future = ctx.writeAndFlush(responseData);
        //future.addListener(ChannelFutureListener.CLOSE);
        //ctx.write(requestData);
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            if (channel != incoming) {
                channel.writeAndFlush(incoming.remoteAddress() + " coordinates: " + responseData);
            }
        }
    }
}
