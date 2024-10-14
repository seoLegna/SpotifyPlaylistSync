package com.bot.spotifyapp.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class ArtistsTO {

    private ExternalUrlsTO external_urls;

    private String href;

    private String id;

    private String name;

    private String type;

    private String uri;
}
