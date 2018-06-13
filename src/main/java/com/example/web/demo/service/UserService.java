package com.example.web.demo.service;

import com.example.web.demo.model.AddFriendModel;
import com.example.web.demo.model.FeedBack;
import com.example.web.demo.model.Msg;
import com.example.web.demo.model.User;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @time：
 * @Discription：
 */
public interface UserService {
    /**
     * 获取好友列表
     * @param userCheck
     * @return
     */
    FeedBack<List<User>> checkLogin(User userCheck);

    /**
     * 打包整理发送过来的信息
     * @param msg
     * @param sessionMap
     */
    void handleMessage(Msg msg, Map<User,Session> sessionMap);

    /**
     * 搜索好友
     * @param account
     * @return
     */
    FeedBack<User> getUserByAccount(String account);

    /**
     * 添加好友
     * @param friendModel
     * @return
     */
    FeedBack addFriend(AddFriendModel friendModel);

    /**
     *
     * @param userId
     * @return
     */
    FeedBack<User> searchFriendInformation(int userId);

    /**
     * 删除好友
     * @param friendModel
     * @return
     */
    FeedBack<Integer> deleteFriend(AddFriendModel friendModel);

    /**
     * 查找暂时保存的信息
     * @param user
     * @return
     */
    FeedBack<List<Msg>> searchPauseMsg(User user);

    /**
     * 删除暂存在服务器中的信息
     * @param user
     */
    void deletePauseMsg(User user);
}
