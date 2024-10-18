package com.bot.spotifyapp.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class ErrorResponseTO {

    private Integer status;

    private String message;
}
