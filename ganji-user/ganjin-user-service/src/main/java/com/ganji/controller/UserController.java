package com.ganji.controller;


import com.ganji.service.UserService;
import com.leyou.user.pojo.User;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    /**
     *校验用户是否注册过
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean>  checkUser(
            @PathVariable("data")String data,
            @PathVariable("type")Integer type
    ){
      Boolean bol=this.userService.checkUser(data,type);
        if(bol==null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bol);
    }


    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(
            @RequestParam("phone")String phone
    ){
        this.userService.sendVerifyCode(phone);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(
            @Valid User user,
            @RequestParam("code") String code
    ){
        this.userService.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("query")
    public ResponseEntity<User> queryUser(
            @RequestParam("username")String username,
            @RequestParam("password")String password
    ){
        User user=this.userService.queryUser(username,password);
        if(user==null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(user);
    }

}
