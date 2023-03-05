package client_test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import server.ResponseData;

import java.nio.charset.Charset;
import java.util.List;

public class ResponseDataDecoder
        extends ReplayingDecoder<ResponseData> {

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> out) throws Exception {

        ResponseData data = new ResponseData();
        data.setIntValue(in.readInt());
        // int strLen = in.readInt();
        // data.setStringValue(in.readCharSequence(strLen, Charset.defaultCharset()).toString());
        out.add(data);
    }
}
