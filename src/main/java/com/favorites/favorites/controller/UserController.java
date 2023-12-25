package com.favorites.favorites.controller;

import com.favorites.favorites.objects.StructK;
import com.favorites.favorites.objects.User;
import com.favorites.favorites.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class UserController {

    @Autowired
    private UserService service;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody User user, HttpServletResponse response){
        return service.login(user, response);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestBody User user){
        return service.register(user);
    }


    @RequestMapping(value = "/settingK", method = RequestMethod.POST)
    @ResponseBody
    public String settingKPassword(@RequestBody StructK k, HttpServletRequest request){
        return service.setK(k, request);
    }




}
