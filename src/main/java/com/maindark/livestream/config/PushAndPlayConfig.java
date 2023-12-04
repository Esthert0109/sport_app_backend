package com.maindark.livestream.config;

import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.GeneratePushUrlUtil;
import com.maindark.livestream.util.UUIDUtil;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "push")
@Data
@Getter
public class PushAndPlayConfig {
    private String host;
    private String secretKey;
    private String appName;

    public String getPushCode() {
        String streamId = UUIDUtil.uuid();
        return new StringBuilder().append(streamId).append("?").append(GeneratePushUrlUtil.getSafeUrl(this.secretKey, streamId, DateUtil.convertDateToLongTime())).toString();

    }

    public String getLiveHost() {
        return this.host + "/" + this.appName;
    }
}
