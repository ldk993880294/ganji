package com.ganji.auth.controller;


import com.ganji.auth.config.JwtProperties;
import com.ganji.auth.service.AuthService;
import com.ganji.common.util.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController

@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {


    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(
            @RequestParam("username")String username,
            @RequestParam("password")String password,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        String token = this.authService.accredit(username, password);
        if(StringUtils.isBlank(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //将token写入cookie中，并制定HTTPONLY为true，防止通过JS获取
        CookieUtils.setCookie(request,response,jwtProperties.getCookieName()
        ,token,jwtProperties.getExpire()*60,null,true);


        return ResponseEntity.ok().build();
    }


}
