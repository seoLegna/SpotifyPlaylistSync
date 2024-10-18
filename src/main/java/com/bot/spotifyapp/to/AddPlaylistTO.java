package com.bot.spotifyapp.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter@Setter@ToString
public class AddPlaylistTO {

    // not spotify required field
    private String playlistId;

    private Integer position;

    private List<String> uris;

    private String snapshot_id;

    // not spotify required field
    private String message;
}
