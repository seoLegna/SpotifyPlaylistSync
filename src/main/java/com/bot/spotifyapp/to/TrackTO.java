package com.bot.spotifyapp.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter@Setter@ToString
public class TrackTO {

    private AlbumTO album;

    private ArtistsTO artists;

    private List<String> available_markets;

    private Integer disc_number;

    private Integer duration_ms;

    private Boolean explicit;

    private ExternalIdsTO external_ids;

    private ExternalUrlsTO external_urls;

    private String href;

    private String id;

    private Boolean is_playable;

    private LinkedFromTO linked_from;

    private RestrictionsTO restrictions;

    private String name;

    private Integer popularity;

    private String preview_url;

    private Integer track_number;

    private String type;

    private String uri;

    private Boolean is_local;
}
