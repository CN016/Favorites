package com.favorites.favorites.service;

import com.alibaba.druid.util.StringUtils;
import com.favorites.favorites.mapper.UserMapper;
import com.favorites.favorites.mapper.WebMapper;
import com.favorites.favorites.objects.StructK;
import com.favorites.favorites.objects.User;
import com.favorites.favorites.objects.Web;
import com.favorites.favorites.utils.EncryptionUtils;
import com.favorites.favorites.utils.MD5Utils;
import com.favorites.favorites.utils.SafeJwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.favorites.favorites.utils.PasswordUtil.validatePassword;

@Service
public class UserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private WebMapper webMapper;

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

    public String modifyK(Map<String, String> map, HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claim = SafeJwtUtil.getClaim(token);
        Integer userId = (Integer) claim.get("id");

        String oldK = map.get("old_k");
        String newk = map.get("new_k");

        String salt = mapper.selectSaltByUserId(userId);
        String encodeK = MD5Utils.getMd5Pwd(oldK, salt);
        String databaseK = mapper.selectKByUserId(userId);
        if (StringUtils.isEmpty(databaseK)){
            return "请先设置K密码";
        }
        if (!databaseK.equals(encodeK)){
            return "K密码错误";
        }
        if (!validatePassword(newk)){
            return "密码格式不正确，要求密码包含大小写字母、数字和特殊字符，并且长度在8到16位之间";
        }



        List<Web> web1 = webMapper.selectWebByUserId(userId);
        List<Web> webs = new LinkedList<>();
        for (int i = 0; i < web1.size(); i++) {
            Web web = web1.get(i);
            String url = web.getUrl();
            String webUsername = web.getWebUsername();
            String webPassword = web.getWebPassword();
            String eventReminder = web.getEventReminder();

            try {
                url = EncryptionUtils.decrypt(url,encodeK);
                webPassword = EncryptionUtils.decrypt(webPassword,encodeK);
                webUsername = EncryptionUtils.decrypt(webUsername,encodeK);
//                eventReminder = EncryptionUtils.decrypt(eventReminder,encodeK);

                Web web2 = new Web();
                web2.setId(web.getId());
                web2.setUrl(url);
                web2.setWebPassword(webPassword);
                web2.setWebUsername(webUsername);
                web2.setEventReminder(eventReminder);

                webs.add(web2);

            } catch (Exception e) {
                e.printStackTrace();
                return "解密失败";
            }
        }

        encodeK = MD5Utils.getMd5Pwd(newk, salt);

        for (int i = 0; i < webs.size(); i++) {
            Web web = webs.get(i);
            String kPassword = encodeK;
            String url = web.getUrl();
            String webUsername = web.getWebUsername();
            String webPassword = web.getWebPassword();
//            String eventReminder = web.getEventReminder();

            try {
                url = EncryptionUtils.encrypt(url,kPassword);
                webPassword = EncryptionUtils.encrypt(webPassword,kPassword);
                webUsername = EncryptionUtils.encrypt(webUsername,kPassword);
//                eventReminder = EncryptionUtils.encrypt(eventReminder,kPassword);
                //新密文
                webMapper.updateById(web.getId(),userId,url,webUsername,webPassword,"eventReminder");


            } catch (Exception e) {
                e.printStackTrace();
                return "K密码修改失败，请联系管理员";
            }
        }

        mapper.saveK(encodeK,userId);
        return "K密码修改成功";

    }

    public String modifyPassword(Map<String, String> map, HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claim = SafeJwtUtil.getClaim(token);
        Integer userId = (Integer) claim.get("id");

        String oldPassword = map.get("old_password");
        String newPassword = map.get("new_password");

        String salt = mapper.selectSaltByUserId(userId);
        String encodePassword = MD5Utils.getMd5Pwd(oldPassword, salt);

        String databaseP = mapper.selectPassword(encodePassword);

        if (StringUtils.isEmpty(databaseP)){
            return "旧密码错误";
        }

        if (!validatePassword(newPassword)){
            return "密码格式不正确，要求密码包含大小写字母、数字和特殊字符，并且长度在8到16位之间";
        }
        newPassword = MD5Utils.getMd5Pwd(newPassword,salt);
        mapper.updateP(newPassword,userId);
        return "修改成功";

    }
}
