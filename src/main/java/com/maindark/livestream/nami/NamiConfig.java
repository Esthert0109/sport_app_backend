package com.maindark.livestream.nami;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nami")
@Data
@Getter
public class NamiConfig {

    private String host;
    private String user;
    private String secretKey;
    private String footballUrl;
    private String basketballUrl;
    private String footballCompetitionUrl;
    private String footballTeamUrl;
    private String footballMatchUrl;
    private String updateDataUrl;
    private String footballMatchLiveUrl;
    private String footballLineUpUrl;
    private String footballLiveUrl;
    private String footballVenueUrl;
    private String footballRefereeUrl;
    private String footballCoachUrl;
    private String footballLiveAddress;



    public String getIdUrl(String idUrl){
        return this.getHost() + idUrl + "?user=" + this.getUser() +"&secret=" + this.getSecretKey()+"&id=";

    }

    public String getTimeUrl(String timeUrl){
        return this.getHost() + timeUrl + "?user=" + this.getUser() +"&secret=" + this.getSecretKey()+"&time=";

    }

    public String getNormalUrl(String normalUrl){
        return this.getHost() + normalUrl + "?user=" + this.getUser() +"&secret=" + this.getSecretKey();
    }

    public String getFootballLiveAddress(String normalUrl){
        return normalUrl + "?user=" + this.getUser() +"&secret=" + this.getSecretKey();
    }


}
