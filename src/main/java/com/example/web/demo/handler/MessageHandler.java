package com.example.web.demo.handler;

import com.example.web.demo.SpringContext;
import com.example.web.demo.model.*;
import com.example.web.demo.service.Impl.UserServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;

/**
 * @Author:高键城
 * @time：
 * @Discription：
 */
public class MessageHandler {
    private static final int LOGIN_METHOD = 1;      //登录指令
    private static final int SEARCH_USER = 2;       //搜索用户指令
    private static final int USER_CHAT = 3 ;        //发送聊天
    private static final int ADD_FRIEND = 7;        //添加好友
    private static final int FRIEND_INFORMATION = 8;    //查看好友消息
    private static final int DELETE_FRIEND = 9;     //删除好友
    private static Gson gson;

    @Autowired
    UserServiceImpl userService;
    public MessageHandler(){
        gson = new Gson();
        userService = SpringContext.getBean("userServiceImpl",UserServiceImpl.class);
    }

    public String messageHandler(String message , Map<User,Session> sessionMap){
        ReceiveTo receiveTo = gson.fromJson(message , new TypeToken<ReceiveTo>(){}.getType());
        System.out.println("在handler打印出内容"+receiveTo);
        switch (receiveTo.getMethod()){
            //登录的情况
            case LOGIN_METHOD:
                //保存两部分信息，一部分保存好友列表信息，一部分保存聊天信息
                ReceiveTo<User> receiveAsUser = gson.fromJson(message , new TypeToken<ReceiveTo<User>>(){}.getType());
                //获得登录者的信息
                User loginUser = receiveAsUser.getRequestBody();
                FeedBack<List<User>> userListFeedBack = userService.checkLogin(loginUser);
                FeedBack<List<Msg>> msgListFeedBack = userService.searchPauseMsg(loginUser);
                userService.deletePauseMsg(loginUser);
                return LOGIN_METHOD+gson.toJson(userListFeedBack)+"&"+gson.toJson(msgListFeedBack);

            //聊天的情况
            case USER_CHAT:
                //聊天记录不要保存在服务器中，只要保存在客户端就好
                ReceiveTo<Msg> receiveAsMsg = gson.fromJson(message , new TypeToken<ReceiveTo<Msg>>(){}.getType());
                userService.handleMessage(receiveAsMsg.getRequestBody(),sessionMap);
                break;

            //搜索好友的情况
            case SEARCH_USER:
                //得到用户信息并打包成一个feedBack类
                ReceiveTo<String> receiveAsSearch = gson.fromJson(message,new TypeToken<ReceiveTo<String>>(){}.getType());
                FeedBack<User> searchUserFeedBack = userService.getUserByAccount(receiveAsSearch.getRequestBody());
                return SEARCH_USER+gson.toJson(searchUserFeedBack);

            case ADD_FRIEND:
                //添加好友的指令
                ReceiveTo<AddFriendModel> receiveAsAddFriend = gson.fromJson(message,new TypeToken<ReceiveTo<AddFriendModel>>(){}.getType());
                FeedBack<Integer> addFriendFeedBack = userService.addFriend(receiveAsAddFriend.getRequestBody());
                return ADD_FRIEND+gson.toJson(addFriendFeedBack);

            case FRIEND_INFORMATION:
                //查看好友信息的指令
                ReceiveTo<Integer> receiveAsFriendInformation = gson.fromJson(message,new TypeToken<ReceiveTo<Integer>>(){}.getType());
                FeedBack<User> friendInformationFeedBack = userService.searchFriendInformation(receiveAsFriendInformation.getRequestBody());
                return FRIEND_INFORMATION+gson.toJson(friendInformationFeedBack);

            case DELETE_FRIEND:
                //删除好友的情况
                ReceiveTo<AddFriendModel> receiveAsDeleteFriend = gson.fromJson(message,new TypeToken<ReceiveTo<AddFriendModel>>(){}.getType());
                FeedBack<Integer> deleteFriendFeedBack = userService.deleteFriend(receiveAsDeleteFriend.getRequestBody());
                return DELETE_FRIEND+gson.toJson(deleteFriendFeedBack);
            default:
                break;
        }
        return null;
    }


}
