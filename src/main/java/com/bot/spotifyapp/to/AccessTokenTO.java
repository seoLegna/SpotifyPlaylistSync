package com.bot.spotifyapp.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class AccessTokenTO {

    private String access_token;

    private String token_type;

    private String scope;

    private Integer expires_in;

    private String refresh_token;
}
