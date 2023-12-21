package com.favorites.favorites.service;

import com.favorites.favorites.mapper.UserMapper;
import com.favorites.favorites.objects.StructK;
import com.favorites.favorites.objects.User;
import com.favorites.favorites.utils.MD5Utils;
import com.favorites.favorites.utils.SafeJwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.favorites.favorites.utils.PasswordUtil.validatePassword;

@Service
public class UserService {

    @Autowired
    private UserMapper mapper;

    public String login(User user, HttpServletResponse response){
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

        String token = SafeJwtUtil.generate(data);
        response.setHeader("token",token);
        return "登陆成功";
    }

    public String register(User user){
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

    public String setK(StructK k, HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claim = SafeJwtUtil.getClaim(token);
        Integer id = (Integer) claim.get("id");

        if (!validatePassword(k.getK())){
            return "密码格式不正确，要求密码包含大小写字母、数字和特殊字符，并且长度在8到16位之间";
        }

        String salt = mapper.selectSaltByUserId(id);
        String encodeK = MD5Utils.getMd5Pwd(k.getK(), salt);
        mapper.saveK(encodeK,id);
        return "保存成功";
    }

}
