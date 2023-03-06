package pipeline.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

// Throw an exception when there is not enough data in the buffer for the reading operation.

// When the exception is caught the buffer is rewound to the beginning and the decoder waits
// for a new portion of data.
// Decoding stops when the out list is not empty after decode execution.

public class RequestDecoder extends ReplayingDecoder<RequestData> {

    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> out) throws Exception {

        RequestData data = new RequestData();
        data.setIntValue(in.readInt());
        int strLen = in.readInt();
        CharSequence str = in.readCharSequence(strLen, charset);
        data.setStringValue(str.toString());
        out.add(data);
    }
}
