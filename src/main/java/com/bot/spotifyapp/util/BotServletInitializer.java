package com.bot.spotifyapp.util;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/res")
public class BotServletInitializer extends ResourceConfig {

    public BotServletInitializer() {
        packages("com.bot.spotifyapp");
    }
}
