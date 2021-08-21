package java0.nio01.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 实现一个过滤器
 */
public class MyFilter extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        FullHttpRequest fullRequest = (FullHttpRequest) msg;
        FullHttpResponse response = null;
        try {
            if (!urlFilter(fullRequest.uri())){
                response = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST,Unpooled.wrappedBuffer("bad request".getBytes("UTF-8")));
                ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                return;
            }
            ctx.fireChannelRead(msg);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 对请求进行过滤，如果包含请求中包含“test”直接过滤
     *
     * @param uri
     */
    private boolean urlFilter(String uri){
        if (uri.contains("test")){
            return false;
        }
        return true;
    }

}
