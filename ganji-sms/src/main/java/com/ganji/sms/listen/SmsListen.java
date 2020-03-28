package com.ganji.sms.listen;


import com.aliyuncs.exceptions.ClientException;
import com.ganji.sms.config.SmsConfig;
import com.ganji.sms.util.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


import java.util.Map;

@Component
public class SmsListen {

    @Autowired
    private SmsConfig smsConfig;

    @Autowired
    private SmsUtils smsUtils;

    @RabbitListener(bindings=@QueueBinding(
            value=@Queue(value="ganji.sms.queue",durable="true"),
            exchange=@Exchange(value="ganji.sms.exchange",ignoreDeclarationExceptions = "true",type= ExchangeTypes.TOPIC),
            key={"verifycode.sms"}
    ))
    public void SendSms(Map<String,String> msg) throws ClientException {
        if(CollectionUtils.isEmpty(msg)){
            return;
        }

        String phone=msg.get("phpne");
        String code=msg.get("code");

        if(StringUtils.isNoneBlank(phone)&&StringUtils.isNoneBlank(code)){
            SmsUtils.sendSms(phone,code,smsConfig);
        }

    }
}
