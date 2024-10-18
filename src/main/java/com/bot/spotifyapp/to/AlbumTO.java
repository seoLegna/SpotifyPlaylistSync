package com.bot.spotifyapp.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter@Setter@ToString
public class AlbumTO {

    private String album_type;

    private Integer total_tracks;

    private List<String> available_markets;

    private ExternalUrlsTO external_urls;

    private String href;

    private String id;

    private List<ImagesTO> images;

    private String name;

    private String release_date;

    private String release_date_precision;

    private RestrictionsTO restrictions;

    private String type;

    private String uri;

    private ArtistsTO artists;
}
