package com.maindark.livestream.sms;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.redis.SMSKey;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.UUIDUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
@Service
@Slf4j
public class SMSService {

    @Resource
    SMSConfig smsConfig;

    @Resource
    RedisService redisService;
    public  Boolean sendSMS(String mobileNumber)  {
        // 随机的四位数OTP
        Random random = new Random();
        int OTPNumber = random.nextInt(9000) + 1000;
        String messageContent = smsConfig.getContent() + OTPNumber;
        // 自动生成一个 referenceID
        String referenceID = UUIDUtil.uuid();
        String param = "apiKey=" + smsConfig.getApiKey()
                + "&messageContent=" + messageContent
                + "&recipients=" + mobileNumber
                + "&referenceID=" + referenceID;
        RestTemplate restTemplate = new RestTemplate();
        String url =  smsConfig.getUrl() + param;
       // log.info("send sms url:{}",url);
        String result = sendGet(smsConfig.getUrl(),param,"UTF-8");
        //String result = restTemplate.getForObject(url,String.class);
        log.info("sms result:{}",result);
        JSONObject resultObj = JSON.parseObject(result);
        String status = (String)resultObj.get("status");
        Integer maxCount = redisService.get(SMSKey.smsLimit,mobileNumber,Integer.class);
        // check message limit
        if(maxCount != null) {
            if(maxCount > 10) {
                throw new GlobalException(CodeMsg.SMS_CODE_SEND_ERROR);
            }
        }

        if(StringUtils.equals("ok",status)) {
            redisService.incr(SMSKey.smsLimit,mobileNumber,0);
            redisService.set(SMSKey.smsKey,mobileNumber,String.valueOf(OTPNumber));
            return true;
        } else {
            log.error("send msg url:{}",url);
            log.error("send smg result:{}",result);
            throw new GlobalException(CodeMsg.SMS_CODE_SEND_ERROR);
        }
    }

    public static String sendGet(String url, String param, String contentType)
    {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try
        {
            String urlNameString = StringUtils.isNotBlank(param) ? url + param : url;
            log.info("sendGet - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null)
            {
                result.append(line);
            }
            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendGet ConnectException, url=" + url + ",param=" + param, e);
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendGet SocketTimeoutException, url=" + url + ",param=" + param, e);
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendGet IOException, url=" + url + ",param=" + param, e);
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtil.sendGet Exception, url=" + url + ",param=" + param, e);
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (Exception ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

}
