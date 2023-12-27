package com.favorites.favorites.service;

import com.favorites.favorites.mapper.EventMapper;
import com.favorites.favorites.objects.Event;
import com.favorites.favorites.utils.SafeJwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventMapper mapper;


    public String add(Event event, HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claim = SafeJwtUtil.getClaim(token);
        Integer userId = (Integer) claim.get("id");
        System.out.println(event);
        mapper.addOne(userId,event.getUrl(),event.getEventTime()*1000,event.getEvent(),event.getRemark());

        return "添加事项成功";
    }

    public List<Event> list(HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claim = SafeJwtUtil.getClaim(token);
        Integer userId = (Integer) claim.get("id");

        List<Event> list = mapper.getListByUserID(userId);
        Iterator<Event> iterator = list.iterator();
        Date now = new Date();

        while (iterator.hasNext()) {
            Event event = iterator.next();
            Long eventTime = event.getEventTime();
            System.out.println("now:"+now.getTime());
            System.out.println("e:"+eventTime);
            if (now.after(new Date(eventTime))) {
                iterator.remove(); // 使用迭代器安全地移除元素
            }
        }
        return list;
    }

    public String delete(HttpServletRequest request, String e_id) {
        String token = request.getHeader("token");
        Claims claim = SafeJwtUtil.getClaim(token);
        Integer userId = (Integer) claim.get("id");

        mapper.deleteByUserIdAndEId(userId,e_id);
        return "删除成功";
    }
}
