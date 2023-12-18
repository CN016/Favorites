package com.favorites.favorites.controller;

import com.favorites.favorites.objects.Web;
import com.favorites.favorites.service.WebService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
