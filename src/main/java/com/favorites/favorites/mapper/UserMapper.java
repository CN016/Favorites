package com.favorites.favorites.mapper;

import com.favorites.favorites.objects.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from t_user where username = #{user_name} and password = #{pass_word}")
    User selectByUsernameAndPassword(@Param("user_name")String username, @Param("pass_word") String password);

    @Insert("insert into t_user (username , password, salt) values (#{user_name},#{pass_word},#{salt})")
    void insertOneUser(@Param("user_name")String username,@Param("pass_word") String password , @Param("salt") String salt);

    @Select("select username from t_user where username = #{user_name}")
    String selectByUsername(@Param("user_name") String username);

    @Select("select password from t_user where password = #{p}")
    String selectPassword(@Param("p") String password);

    @Select("select * from t_user where username = #{user_name}")
    User selectUserByUsername(@Param("user_name") String username);

    @Select("select salt from t_user where id = #{id}")
    String selectSaltByUserId( @Param("id") Integer id);

    @Update("update t_user set k_password = #{k} where id = #{id}")
    void saveK(@Param("k")String k , @Param("id")Integer userId);

    @Update("update t_user set password = #{p} where id = #{id}")
    void updateP(@Param("p")String p , @Param("id")Integer userId);

    @Select("select k_password from t_user where id = #{id}")
    String selectKByUserId( @Param("id") Integer id);

}
