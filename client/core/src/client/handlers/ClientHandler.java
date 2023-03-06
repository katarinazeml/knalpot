package client.handlers;


import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import pipeline.server.RequestData;
import pipeline.server.ResponseData;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {

        RequestData msg = new RequestData();
        msg.setIntValue(210);
        msg.setStringValue("time for a blunt rotation");
        ChannelFuture future = ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if (msg instanceof ResponseData) {
            ResponseData responseData = (ResponseData) msg;
            System.out.println("Received response: " + responseData.getIntValue() + " " + responseData.getStringValue());
        } else {
            System.out.println("Received unexpected message: " + msg);
        }
        ctx.close();
    }
}