package com.bot.spotifyapp.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter@Setter@ToString
public class TracksTO {

    private String href;

    private Integer limit;

    private String next;

    private Integer offset;

    private String previous;

    private Integer total;

    private List<ItemsTO> items;
}
