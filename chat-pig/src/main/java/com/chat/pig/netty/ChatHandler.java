package com.chat.pig.netty;

import com.chat.pig.constant.MsgActionEnum;
import com.chat.pig.service.IChatService;
import com.chat.pig.utils.JsonUtils;
import com.chat.pig.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述: 自定义handler，处理消息
 * TextWebSocketFrame: netty中用于为websocket专门处理文本的对象，frame是消息的载体
 *
 * @author li
 * @date 2019/12/23
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 用于记录和管理所有客户端的channel
     */
    private static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //获取客户端发送来的消息
        String context = msg.text();
        DataContent dataContent = JsonUtils.jsonToPojo(context,DataContent.class);
        //获取channel
        Channel currentChannel = ctx.channel();
        //判断消息类型,根据不同类型处理不同业务
        assert dataContent != null;
        Integer action = dataContent.getAction();

        if (action.equals(MsgActionEnum.CONNECT.type)){
            //当websocket第一次open时，初始化channel，
            String sendId = dataContent.getChatMsg().getSendId();
            log.info("SendId - {} Is Open",sendId);
            //将channel和userId关联
            UserChannelRel.put(sendId,currentChannel);
        }else if (action.equals(MsgActionEnum.CHAT.type)){
            //聊天类型消息，保存聊天记录到数据库,标记消息的签收状态[未签收]
            ChatMsg chatMsg = dataContent.getChatMsg();
            //获取响应信息
            String acceptId = chatMsg.getAcceptId();
            //保存聊天信息到数据库,且标记为未签收
            IChatService chatService = (IChatService) SpringUtil.getBean("chatServiceImpl");
            String msgId = chatService.saveMsg(chatMsg);
            chatMsg.setMsgId(msgId);

            //发送消息
            //从全局用户channel中获取接收方的channel
            Channel acceptChannel = UserChannelRel.getChannel(acceptId);
            if (acceptChannel == null){
                // TODO channel为空，表示用户离线，推送消息
                log.info("用户离线，推送消息");
            }else{
                //从channelGroup中查找channel是否存在
                Channel findChannel = users.find(acceptChannel.id());
                if (findChannel != null){
                    //用户在线
                    acceptChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(chatMsg)));
                }else{
                    //TODO 用户离线
                    log.info("用户离线，推送消息");
                }
            }
        }else if (action.equals(MsgActionEnum.SIGNED.type)){
            //签收消息类型，针对具体的消息进行签收
            IChatService chatService = (IChatService) SpringUtil.getBean("chatServiceImpl");
            //扩展字段在signed类型中，代表要签收的消息的id，逗号间隔
            String msgIdsStr = dataContent.getExtend();
            String[] msgIds = msgIdsStr.split(",");
            List<String> msgIdList = new ArrayList<>(msgIds.length);
            Arrays.stream(msgIds).forEach(e -> {
                if (StringUtils.isNoneBlank(e)){
                    msgIdList.add(e);
                }
            });
            log.info("Need Signed's MsgId - {}",msgIdList.toString());
            chatService.signMsg(msgIdList);
        }else if (action.equals(MsgActionEnum.KEEPALIVE.type)){
            //心跳类型

        }

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.debug("Handler Added - {}",ctx.channel().id().asLongText());
        //获取channel
        //将channel放入ChannelGroup中进行管理
        users.add(ctx.channel());

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.debug("Handler Removed: channelLongId - {}",ctx.channel().id().asLongText());
        users.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("ChatHandler Exception: {}",cause.getMessage());
        //发生异常后关闭channel
        ctx.channel().close();
        //从channelGroup中移除
        users.remove(ctx.channel());
    }
}
