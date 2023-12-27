package com.favorites.favorites.controller;

import com.favorites.favorites.objects.Event;
import com.favorites.favorites.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EventController {

    @Autowired
    private EventService service;


    @RequestMapping(value = "/event/add" , method = RequestMethod.POST)
    @ResponseBody
    public String addEvent(@RequestBody Event event, HttpServletRequest request){
        return service.add(event,request);
    }

    @RequestMapping(value = "/event/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Event> getAll(HttpServletRequest request){
        return service.list(request);
    }

    @RequestMapping(value = "/event/delete/{e_id}",method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteOne(HttpServletRequest request, @PathVariable String e_id){
        return service.delete(request,e_id);
    }


}
