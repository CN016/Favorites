package com.favorites.favorites.mapper;

import com.favorites.favorites.objects.Web;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WebMapper {

    @Insert("insert into t_web (user_id,url,web_username,web_password,event_reminder)  values ( #{uid},#{url},#{name},#{p},#{e} ) ")
    void saveTheWeb( @Param("uid") Integer userId ,@Param("url") String url,@Param("name") String webUsername, @Param("p") String webPassword,@Param("e") String eventReminder );

    @Select("select * from t_web where user_id = #{uid}")
    Web selectWebByUserId( @Param("uid") Integer userId );

}
