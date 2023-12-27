package com.favorites.favorites.service;

import com.alibaba.druid.util.StringUtils;
import com.favorites.favorites.mapper.UserMapper;
import com.favorites.favorites.mapper.WebMapper;
import com.favorites.favorites.objects.Web;
import com.favorites.favorites.utils.EncryptionUtils;
import com.favorites.favorites.utils.SafeJwtUtil;
import com.favorites.favorites.utils.MD5Utils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class WebService {

    @Autowired
    private WebMapper mapper;

    @Autowired
    private UserMapper userMapper;

    public String save(Web web, HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claim = SafeJwtUtil.getClaim(token);
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

            return "网站信息添加成功";

        } catch (Exception e) {
            e.printStackTrace();
            return "加密失败";
        }
    }

    public Object get(HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claim = SafeJwtUtil.getClaim(token);
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

        List<Web> web1 = mapper.selectWebByUserId(userId);
        List<Web> webs = new LinkedList<>();
        for (int i = 0; i < web1.size(); i++) {
            Web web = web1.get(i);
            String url = web.getUrl();
            String webUsername = web.getWebUsername();
            String webPassword = web.getWebPassword();
            String eventReminder = web.getEventReminder();

            try {
                url = EncryptionUtils.decrypt(url,kPassword);
                webPassword = EncryptionUtils.decrypt(webPassword,kPassword);
                webUsername = EncryptionUtils.decrypt(webUsername,kPassword);
//                eventReminder = EncryptionUtils.decrypt(eventReminder,kPassword);

                Web web2 = new Web();
                web2.setId(web.getId());
                web2.setUrl(url);
                web2.setWebPassword(webPassword);
                web2.setWebUsername(webUsername);
                web2.setEventReminder(null);

                webs.add(web2);

            } catch (Exception e) {
                e.printStackTrace();
                return "解密失败";
            }
        }
        return webs;

    }

    public Object add(HttpServletRequest request, Web web) {
        String token = request.getHeader("token");
        Claims claim = SafeJwtUtil.getClaim(token);
        Integer userId = (Integer) claim.get("id");

        web.setUserId(userId);
        mapper.saveTheWeb(web.getUserId(), web.getUrl(), web.getWebUsername(), web.getWebPassword(), web.getEventReminder());

        return "success";

    }

    public Object delete(HttpServletRequest request, String id) {
        String token = request.getHeader("token");
        Claims claim = SafeJwtUtil.getClaim(token);
        Integer userId = (Integer) claim.get("id");

        mapper.deleteById(id,userId);
        return "删除成功";
    }

    public Object modify(Web web, HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claim = SafeJwtUtil.getClaim(token);
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
//            eventReminder = EncryptionUtils.encrypt(eventReminder,kPassword);

            mapper.updateById(web.getId(),userId,url,webUsername,webPassword,"eventReminder");

            return "网站信息更新成功";

        } catch (Exception e) {
            e.printStackTrace();
            return "加密失败";
        }
    }
}
