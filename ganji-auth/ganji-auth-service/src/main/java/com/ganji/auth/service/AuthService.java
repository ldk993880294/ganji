package com.ganji.auth.service;


import com.ganji.auth.client.UserClient;
import com.ganji.auth.config.JwtProperties;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {


    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;


    public String accredit(String username, String password) {

        //调用微服务，执行查询
        User user = this.userClient.queryUser(username, password);
        if(user==null){
            return null;
        }

        try{
            //如果有查询结果，则生成token
          String token=JwtUtils.generateToken(new UserInfo(user.getId(),user.getUsername()),
                    jwtProperties.getPrivateKey(),jwtProperties.getExpire());
            return token;
        }catch (Exception e){
          e.printStackTrace();
        }

        return null;

    }
}
