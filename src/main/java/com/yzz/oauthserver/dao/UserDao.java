package com.yzz.oauthserver.dao;

import org.apache.ibatis.annotations.Param;

import com.yzz.oauthserver.bean.User;

public interface UserDao {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    
    User login(@Param("userName")String userName, @Param("userPassword")String userPassword);
}