package com.favorites.favorites.mapper;

import com.favorites.favorites.objects.Web;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WebMapper {

    @Insert("insert into t_web (user_id,url,web_username,web_password,event_reminder)  values ( #{uid},#{url},#{name},#{p},#{e} ) ")
    void saveTheWeb( @Param("uid") Integer userId ,@Param("url") String url,@Param("name") String webUsername, @Param("p") String webPassword,@Param("e") String eventReminder );

    @Select("select * from t_web where user_id = #{uid}")
    List<Web> selectWebByUserId(@Param("uid") Integer userId );

    @Delete("DELETE FROM t_web WHERE id = #{id} and user_id = #{uid};")
    void deleteById( @Param("id") String id , @Param("uid") Integer userId);

    @Update("update t_web set url = #{url} , web_username = #{name}, web_password = #{p} where id = #{id} and user_id = #{uid} ")
    void updateById( @Param("id") Integer id, @Param("uid") Integer userId ,@Param("url") String url,@Param("name") String webUsername, @Param("p") String webPassword,@Param("e") String eventReminder );
}
