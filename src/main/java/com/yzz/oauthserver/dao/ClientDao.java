package com.yzz.oauthserver.dao;

import org.apache.ibatis.annotations.Param;

import com.yzz.oauthserver.bean.Client;

public interface ClientDao {
    int deleteByPrimaryKey(Long id);

    int insert(Client record);

    int insertSelective(Client record);

    Client selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Client record);

    int updateByPrimaryKey(Client record);
    
    int check( @Param("clientId")String clientId,@Param("clientSecret")String clientSecret);
    
    int checkClient(String clientId);
}