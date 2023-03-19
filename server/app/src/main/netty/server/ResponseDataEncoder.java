package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

// Encode the message.
public class ResponseDataEncoder
        extends MessageToByteEncoder<ResponseData> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          ResponseData msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getIntValue());
        out.writeInt(msg.getStringValue().length());
        out.writeFloat(msg.getFloatValue());
        out.writeCharSequence(msg.getStringValue(), StandardCharsets.UTF_8);
    }
}
