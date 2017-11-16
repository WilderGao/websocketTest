package com.example.web.demo.service.Impl;

import com.example.web.demo.enums.methodEnum;
import com.example.web.demo.mapper.MsgMapper;
import com.example.web.demo.mapper.UserMapper;
import com.example.web.demo.model.*;
import com.example.web.demo.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @time：
 * @Discription：
 */
@Service
@Component
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MsgMapper msgMapper;
    /**
     * 对登录内容进行检验
     * @param userCheck
     * @return
     */
    @Override
    public FeedBack<List<User>> checkLogin(User userCheck) {
        FeedBack<List<User>> feedBack = new FeedBack<>();
        //从数据库中操作得到对应User
        User resultUser = userMapper.findUserByAccount(userCheck);
        if (resultUser == null){
            feedBack.setState(methodEnum.LOGINERROR.getState());
            feedBack.setData(null);
            return feedBack;
        }else {
            //通过这个user得到他的好友信息，当然不能得到密码
            List<User> friendsList = userMapper.findUserFriendsById(resultUser.getUserId());
            System.out.println(friendsList);
            feedBack.setState(methodEnum.OK.getState());
            feedBack.setData(friendsList);
            return feedBack;
        }
    }

    @Override
    public void handleMessage(Msg msg , Map<User,Session>sessionMap) {
        FeedBack<Msg> feedBack = new FeedBack<>();
        Gson gson = new Gson();
        //得到对应的聊天信息
        if (msg == null){
            //聊天内容为空
            feedBack.setState(methodEnum.ERROR_MESSAGE.getState());
        }else {
            boolean isSendSuccessful = false;
            //遍历sessionMap，将信息发送到特定的session客户端
            for (Map.Entry<User,Session> entry:sessionMap.entrySet()){
                //找到对应的user
                if (msg.getToUser().getUserAccount().equals(entry.getKey().getUserAccount())){
                    //将信息发送到对应的session中
                    try {
                        isSendSuccessful = true;
                        entry.getValue().getBasicRemote().sendText(methodEnum.CHAT_USER.getState()+gson.toJson(msg));
                    } catch (IOException e) {
                        System.out.println("信息发送失败");
                        e.printStackTrace();
                    }
                }
            }
            if (!isSendSuccessful){
                //表明信息没有发送过去，也就是说那个客户端不在线
                //将数据保存到数据库
                msgMapper.savePauseMessage(msg);
                System.out.println("数据暂存到服务器中");
            }
        }
    }

    @Override
    public FeedBack<User> getUserByAccount(String account) {
        FeedBack<User> feedBack = new FeedBack<>();
        //通过数据库搜索到
        User searchUser = userMapper.searchFriendByAccount(account);
        if (searchUser == null){
            System.out.println("找不到对应用户");
            feedBack.setState(methodEnum.USER_NOT_EXIT.getState());
            feedBack.setData(null);
            return feedBack;
        }else {
            feedBack.setState(methodEnum.OK.getState());
            feedBack.setData(searchUser);
            return feedBack;
        }
    }

    @Override
    public FeedBack<Integer> addFriend(AddFriendModel friendModel) {
        FeedBack<Integer> feedBack = new FeedBack<>();
        //先得到本人的Id
        friendModel.setMyId(userMapper.searchFriendByAccount(friendModel.getMyAccount()).getUserId());

        SearchModel searchModel = userMapper.searchFriendExit(friendModel.getMyId(),friendModel.getFriendId());

        //假如相同则证明已经是它的好友了，就没必要唧唧歪歪了
        if (searchModel != null){
            //证明已经是好友了
            feedBack.setState(methodEnum.ALREADY_YOUR_FRIEND.getState());
            feedBack.setData(methodEnum.ALREADY_YOUR_FRIEND.getState());
            return feedBack;
        }else {
            //将好友信息插入到表中
            userMapper.addFriendById(friendModel);
            userMapper.addFriendByIdFriend(friendModel);

            //书写添加成功的返回格式
            feedBack.setState(methodEnum.ADD_SUCCESS.getState());
            feedBack.setData(methodEnum.ADD_SUCCESS.getState());
            return feedBack;
        }
    }

    @Override
    public FeedBack<User> searchFriendInformation(int userId) {
        FeedBack<User> feedBack = new FeedBack<>();
        User user = userMapper.searchFriendById(userId);
        System.out.println("搜索的好友"+user);
        if (user != null){
            feedBack.setState(methodEnum.OK.getState());
            feedBack.setData(user);
            return feedBack;
        }else {
            feedBack.setState(methodEnum.LOGINERROR.getState());
            return feedBack;
        }

    }

    @Override
    public FeedBack<Integer> deleteFriend(AddFriendModel friendModel) {
        FeedBack<Integer> feedBack = new FeedBack<>();
        SearchModel searchModel = new SearchModel();
        searchModel.setFriendId(friendModel.getFriendId());
        //得到自己的Id
        searchModel.setMyId(userMapper.searchFriendByAccount(friendModel.getMyAccount()).getUserId());
        //进行删除操作
        try {
            userMapper.deleteFriend(searchModel);
            feedBack.setState(methodEnum.OK.getState());
            feedBack.setData(methodEnum.OK.getState());
            return feedBack;
        }catch (Exception e){
            e.printStackTrace();
            feedBack.setState(methodEnum.DELETE_ERROR.getState());
            feedBack.setData(methodEnum.DELETE_ERROR.getState());
            return feedBack;
        }finally {}
    }

    @Override
    public FeedBack<List<Msg>> searchPauseMsg(User user) {
        FeedBack<List<Msg>> msgListFeedBack = new FeedBack<>();
        List<PauseMsg> pauseMsgList = msgMapper.selectPauseMessage(user.getUserAccount());
        List<Msg> msgList = changePauseMsgToMsg(pauseMsgList);
        if (msgList!=null) {
            msgListFeedBack.setState(methodEnum.OK.getState());
            msgListFeedBack.setData(msgList);
        }else {
            msgListFeedBack.setState(methodEnum.NO_PAUSE_MESSAGE.getState());
        }

        return msgListFeedBack;
    }

    @Override
    public void deletePauseMsg(User user) {
        msgMapper.deletePauseMessage(user.getUserAccount());
    }


    private List<Msg> changePauseMsgToMsg(List<PauseMsg> pauseMsgList){
        List<Msg> msgList = new ArrayList<>();
        for (PauseMsg pauseMsg : pauseMsgList) {
            Msg msg = new Msg();
            msg.setId(pauseMsg.getId());
            msg.setContent(pauseMsg.getContent());
            msg.setType(pauseMsg.getType());
            msg.setFromUserAccount(pauseMsg.getFromUserAccount());
            msg.setToUserAccount(pauseMsg.getToUserAccount());
            msgList.add(msg);
        }
        return msgList;
    }
}
