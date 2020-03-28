package com.ganji.auth.config;


import com.leyou.common.utils.RsaUtils;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;


@ConfigurationProperties("ganji.jwt")
public class JwtProperties {

    private String secret;

    private String pubKeyPath;
    private String priKeyPath;
    private int expire;
    private String cookieName;
    private PublicKey publicKey; //公钥

    private PrivateKey privateKey; //私钥

    private static final Logger logger= LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct//在构造方法之前执行该方法
    public void init(){
        try{
            File pubKey=new File(pubKeyPath);
            File priKey=new File(priKeyPath);

            this.publicKey= RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey=RsaUtils.getPrivateKey(priKeyPath);
        }catch(Exception e) {
            e.printStackTrace();
            logger.info("获取公钥私钥失败");
        }
    }


    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public static Logger getLogger() {
        return logger;
    }
}
