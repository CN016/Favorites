package com.favorites.favorites.controller;

import com.favorites.favorites.objects.Web;
import com.favorites.favorites.service.WebService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    @Autowired
    private WebService service;

    @RequestMapping(value = "/favorites", method = RequestMethod.POST)
    @ResponseBody
    public String saveWebInfo(@RequestBody Web web , HttpServletRequest request){
        return service.save(web, request);
    }

    @RequestMapping(value = "/favorites/get", method = RequestMethod.GET)
    @ResponseBody
    public Object getWebInfo(HttpServletRequest request){
        return service.get(request);
    }

    @RequestMapping(value = "/favorites/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteWebInfo(@PathVariable String id , HttpServletRequest request){
        return service.delete(request,id);
    }

    @RequestMapping(value = "/favorites/modify", method = RequestMethod.PUT)
    @ResponseBody
    public Object modifyWebInfo(@RequestBody Web web , HttpServletRequest request){
        return service.modify(web,request);
    }

}
