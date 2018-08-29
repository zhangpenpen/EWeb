package com.dao;

import com.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int selectCountByName(String user);

    int selectCountByEmail(String user);

    String selectQuestion(String question);

    int checkAnswer(@Param("username") String username,@Param("question") String question, @Param("answer") String answer);

    User selectLogin(@Param("username") String username, @Param("password") String password);

    int checkPassword(@Param("password") String password, @Param("id") int id);

    int checkEmail(@Param("email") String email, @Param("id") int id);

    int updatePasswordByUsername(@Param("username") String username,@Param("password") String password);
}