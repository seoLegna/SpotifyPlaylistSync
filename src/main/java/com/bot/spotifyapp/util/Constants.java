package com.bot.spotifyapp.util;

public class Constants {

    private Constants() {
        super();
    }

    public static final String WHATSAPP_BOT = "WHATSAPP_BOT";

    public static final String CONFIG_HOME = "<<Path to your configuration files>>";

    public static final String ERROR_PROCESSING_REQUEST = " Error Processing Response ";

    public static final String UUID = "uuid";

    public static final Integer STATUS_CODE_BAD_REQUEST = 800;

    public static final Integer CUSTOM_STATUS_CODE_ERROR = 801;

    public static final Integer STATUS_CODE_OK = 200;

    public static final Integer STATUS_CODE_OK_POST = 201;

    public static final String AUTHORIZATION_API_REDIRECT_URI = "<<path-to-your-container>>/res/spotify/token";

    //Config properties

    public static final String BOT_CORE_POOL_SIZE = "core.pool.size";

    public static final String BOT_MAXIMUM_POOL_SIZE = "maximum.pool.size";

    public static final String BOT_KEEP_ALIVE_TIME = "keep.alive.time";

    public static final String CLIENT_ID = "client.id";

    public static final String CLIENT_SECRET = "client.secret";

}
