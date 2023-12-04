package com.favorites.favorites;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from t_user where username = #{user_name} and password = #{pass_word}")
    User selectByUsernameAndPassword(@Param("user_name")String username,@Param("pass_word") String password);

    @Insert("insert into t_user (username , password, salt) values (#{user_name},#{pass_word},#{salt})")
    void insertOneUser(@Param("user_name")String username,@Param("pass_word") String password , @Param("salt") String salt);

    @Select("select username from t_user where username = #{user_name}")
    String selectByUsername(@Param("user_name") String username);

    @Select("select * from t_user where username = #{user_name}")
    User selectUserByUsername(@Param("user_name") String username);

    @Select("select salt from t_user where id = #{id}")
    String selectSaltByUserId( @Param("id") Integer id);


}
