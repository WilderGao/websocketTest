package com.example.web.demo.mapper;

import com.example.web.demo.model.Msg;
import com.example.web.demo.model.PauseMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Administrator
 * @time：
 * @Discription：
 */
@Mapper
@Repository
public interface MsgMapper {
    /**
     * 服务器暂存消息
     * @param msg
     */
    void savePauseMessage(Msg msg);

    /**
     * 将暂存的信息发送给上线的用户
     * @param toUserAccount 上线用户的账号
     * @return
     */
    List<PauseMsg> selectPauseMessage(@Param("toUserAccount") String toUserAccount );

    /**
     * 删除保存在服务器的聊天信息
     * @param toUserAccount
     */
    void deletePauseMessage(@Param("toUserAccount") String toUserAccount);
}
