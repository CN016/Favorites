package com.favorites.favorites;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserMapper mapper;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody User user, HttpServletResponse response){
        String username = user.getUsername();
        String password = user.getPassword();

        if ( "".equals(username) || username == null || "".equals(password) || password == null ){
            return "用户名或密码不能为空";
        }
        User databaseUser = mapper.selectUserByUsername(username);
        if (databaseUser == null){
            return "用户名或密码错误";
        }
        String salt = databaseUser.getSalt();
        String input = MD5Utils.getMd5Pwd(password, salt);
        String databasePassword = databaseUser.getPassword();
        if (!input.equals(databasePassword)){
            return "用户名或密码错误";
        }

        Map<String,Object> data = new HashMap<>();
        data.put("id",databaseUser.getId());

        String token = JwtUtil.generate(data);
        response.setHeader("token",token);
        return "登陆成功";
    }


    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!.])(?=.*[a-zA-Z0-9@#$%^&+=!]).{8,16}$";

    public static boolean validatePassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestBody User user){
        String username = user.getUsername();
        String password = user.getPassword();

        if ( "".equals(username) || username == null || "".equals(password) || password == null ){
            return "用户名或密码不能为空";
        }

        String username1 = mapper.selectByUsername(username);

        if (username1!=null){
            return "该用户已被注册";
        }

        if (!validatePassword(password)){
            return "密码格式不正确，要求密码包含大小写字母、数字和特殊字符，并且长度在8到16位之间";
        }

        String salt = MD5Utils.getSalt();
        String encodePassword = MD5Utils.getMd5Pwd(password,salt);

        mapper.insertOneUser(username,encodePassword,salt);
        return "注册成功";

    }

    @RequestMapping(value = "/settingK", method = RequestMethod.POST)
    @ResponseBody
    public String settingKPassword(@RequestBody StructK k, HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claim = JwtUtil.getClaim(token);
        Integer id = (Integer) claim.get("id");
        String salt = mapper.selectSaltByUserId(id);
        String encodeK = MD5Utils.getMd5Pwd(k.getK(), salt);
        



    }

}
