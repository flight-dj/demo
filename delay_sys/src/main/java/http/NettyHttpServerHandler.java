package http;

import factory.ConsumerProducerTaskFactory;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;
import message.delay.ConsumerProducerTask;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import timing.template.BusinessTask;
import timing.template.DateToX;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BackupState backupState = BackupState.ERROR;

        if (msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;
            if (req.getUri().equals("/chronos/backup")) {
                backupState = BackupState.SUCCESS;
                System.out.println("backupState:" + backupState.getDesc());

                ConsumerProducerTask consumerProducerTask = ConsumerProducerTaskFactory.getInstance();

                send(consumerProducerTask, 1);

            }
        } else {
            System.out.println("request is not FullHttpRequest");
        }

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(backupState.getDesc().getBytes("utf-8")));
        response.headers().set(Names.CONTENT_TYPE, "text/plain;charset=UTF-8");
        response.headers().set(Names.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(Names.CONNECTION, Values.KEEP_ALIVE);
        ctx.write(response);
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    private void send(ConsumerProducerTask consumerProducerTask, int i) {
        BusinessTask businessTask = new BusinessTask();
        businessTask.setUrl("127.0.0.1");
        try {
            businessTask.setMessage(new Message("TopicTest" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            ));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        businessTask.setInitialDelay(new DateToX(TimeUnit.MILLISECONDS, i*10000));
        businessTask.setCycle(false);

        consumerProducerTask.schedule(businessTask.getUrl(), businessTask.getMessage(), businessTask.getInitialDelay());
    }
}