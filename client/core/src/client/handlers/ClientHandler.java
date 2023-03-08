package client.handlers;


import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.knalpot.knalpot.Player.Player;
import pipeline.server.RequestData;
import pipeline.server.ResponseData;

import java.util.Scanner;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        Scanner scanner = new Scanner(System.in);
        RequestData msg = new RequestData();
        System.out.println("Enter something hehe:");
        String temporaryMsg = scanner.nextLine();
        msg.setStringValue(temporaryMsg);
        //msg.setIntValue(210);
        //msg.setStringValue("ona vigljadit kak mommy");
        ChannelFuture future = ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if (msg instanceof ResponseData responseData) {
            System.out.println(responseData.getStringValue());
        } else {
            System.out.println("Received unexpected message: " + msg);
        }
        ctx.close();
    }
}
