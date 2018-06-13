package com.example.web.demo.mapper;

import com.example.web.demo.model.AddFriendModel;
import com.example.web.demo.model.SearchModel;
import com.example.web.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * time：
 * Description：
 */
@Mapper
@Repository
public interface UserMapper {
    /**
     * @param user 用户名
     * @return 用户
     */
    User findUserByAccount(User user);
    /**
     * 通过Id找到与之关联的好友
     * @param userId
     * @return
     */
    List<User> findUserFriendsById(int userId);
    /**
     * 通过账号来搜索好友
     * @param userAccount
     * @return
     */
    User searchFriendByAccount(@Param("userAccount") String userAccount);


    SearchModel searchFriendExit(@Param("MyId") int myId , @Param("FriendId") int friendId);

    /**
     * 添加好友
     * @param addFriendModel
     */
    void addFriendById(AddFriendModel addFriendModel);

    void addFriendByIdFriend(AddFriendModel addFriendModel);

    /**
     * 通过Id号找信息
     * @param userId
     * @return
     */
    User searchFriendById(int userId);

    /**
     * 删除好友操作
     * @param searchModel
     */
    void deleteFriend(SearchModel searchModel);
}
