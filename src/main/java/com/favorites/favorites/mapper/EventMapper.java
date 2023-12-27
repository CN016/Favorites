package com.favorites.favorites.mapper;

import com.favorites.favorites.objects.Event;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface EventMapper {


    @Insert("insert into t_event (user_id,url,event_time,event,remark) values (#{uid},#{url},#{etime},#{e},#{r})")
    void addOne(@Param("uid") Integer userId, @Param("url") String url, @Param("etime")Long time,@Param("e")String event,@Param("r") String remark);

    @Select("select * from t_event where user_id = #{uid}")
    List<Event> getListByUserID(@Param("uid") Integer userID);

    @Delete("DELETE FROM t_event where id = #{e_id} and user_id = #{uid}")
    void deleteByUserIdAndEId(@Param("uid") Integer userId, @Param("e_id")String eId);

}
