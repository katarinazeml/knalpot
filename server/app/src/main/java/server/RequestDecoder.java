package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestDecoder extends ReplayingDecoder<RequestData> {

    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> out) throws Exception {

        RequestData data = new RequestData();
        System.out.println("data on server");
//        data.setIntValue(in.readInt());
        int strLen = in.readInt();
//        System.out.println(strLen);
        CharSequence str = in.readCharSequence(strLen, charset);
//        System.out.println("might be a position");
//        System.out.println(str);
        data.setStringValue(str.toString());
//        System.out.println(data.getStringValue());
        data.setFloatValue(in.readFloat());
//        System.out.println(data.getFloatValue());
        out.add(data);
    }
}
