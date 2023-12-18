package com.favorites.favorites.service;

import com.alibaba.druid.util.StringUtils;
import com.favorites.favorites.mapper.UserMapper;
import com.favorites.favorites.mapper.WebMapper;
import com.favorites.favorites.objects.Web;
import com.favorites.favorites.utils.EncryptionUtils;
import com.favorites.favorites.utils.JwtUtil;
import com.favorites.favorites.utils.MD5Utils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebService {

    @Autowired
    private WebMapper mapper;

    @Autowired
    private UserMapper userMapper;

    public String save(Web web, HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claim = JwtUtil.getClaim(token);
        Integer userId = (Integer) claim.get("id");

        String kPassword = userMapper.selectKByUserId(userId);
        String url = web.getUrl();
        String webUsername = web.getWebUsername();
        String webPassword = web.getWebPassword();
        String eventReminder = web.getEventReminder();

        try {
            url = EncryptionUtils.encrypt(url,kPassword);
            webPassword = EncryptionUtils.encrypt(webPassword,kPassword);
            webUsername = EncryptionUtils.encrypt(webUsername,kPassword);
            eventReminder = EncryptionUtils.encrypt(eventReminder,kPassword);

            mapper.saveTheWeb(userId,url,webUsername,webPassword,eventReminder);

            return "网站信息保存成功";

        } catch (Exception e) {
            e.printStackTrace();
            return "加密失败";
        }
    }

    public Object get(HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claim = JwtUtil.getClaim(token);
        Integer userId = (Integer) claim.get("id");
        String k = request.getHeader("k");
        String salt = userMapper.selectSaltByUserId(userId);
        String encodeK = MD5Utils.getMd5Pwd(k, salt);
        String kPassword = userMapper.selectKByUserId(userId);
        if (StringUtils.isEmpty(kPassword)){
            return "请先设置k密码";
        }
        if (!kPassword.equals(encodeK)){
            return "k密码错误";
        }

        Web web = mapper.selectWebByUserId(userId);
        String url = web.getUrl();
        String webUsername = web.getWebUsername();
        String webPassword = web.getWebPassword();
        String eventReminder = web.getEventReminder();

        try {
            url = EncryptionUtils.decrypt(url,kPassword);
            webPassword = EncryptionUtils.decrypt(webPassword,kPassword);
            webUsername = EncryptionUtils.decrypt(webUsername,kPassword);
            eventReminder = EncryptionUtils.decrypt(eventReminder,kPassword);

            Web web1 = new Web();
            web1.setUrl(url);
            web1.setWebPassword(webPassword);
            web1.setWebUsername(webUsername);
            web1.setEventReminder(eventReminder);

            return web1;

        } catch (Exception e) {
            e.printStackTrace();
            return "解密失败";
        }
    }

}