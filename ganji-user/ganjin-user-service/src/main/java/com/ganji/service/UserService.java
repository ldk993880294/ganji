package com.ganji.service;


import com.ganji.mapper.UserMapper;
import com.ganji.utils.CodecUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX="user:verify:";

    public Boolean checkUser(String data, Integer type) {

        User user=new User();

        if(type==1){
            user.setUsername(data);
        }else if(type==2){
            user.setPhone(data);
        }else{
            return null;
        }
        return this.userMapper.selectCount(user)==0;
    }


    public void sendVerifyCode(String phone) {
        if(!StringUtils.isNotBlank(phone)){
            return;
        }
        String code= NumberUtils.generateCode(6);
        //发送消息到mq中
        Map<String,String> msg=new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);
        this.amqpTemplate.convertAndSend("ganji.sms.exchange","verifycode.sms",msg);

        //保存验证码到redis中作比较
        this.stringRedisTemplate.opsForValue().set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);




    }

    public void register(User user, String code) {
        //从redis中去验证码
        String redisPhone=stringRedisTemplate.opsForValue().get(KEY_PREFIX+user.getPhone());
        //验证验证码
        if(!StringUtils.equals(redisPhone,code)){
            return;
        }
        //验证通过生成盐

        String salt= CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
        //新增用户
        user.setId(null);
        user.setCreated(new Date());
        this.userMapper.insertSelective(user);
    }


    public User queryUser(String username, String password) {
        User user=new User();
        user.setUsername(username);
        User user1 = this.userMapper.selectOne(user);
        if(user1==null){
            return null;
        }
        password= CodecUtils.md5Hex(password, user1.getSalt());

        //和数据库里面的作比较
        if(StringUtils.equals(password,user1.getPassword())){
            return user1;
        }
        return null;
    }
}
