package com.bot.spotifyapp.util;

import com.bot.spotifyapp.exception.BotException;
import com.bot.spotifyapp.type.BotExceptionType;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class Config {

    private static final Logger logger = BotLogger.getInstance().getLogger();
    private static final String CONFIG = "CONFIG";
    private static Configuration config;

    private Config() {
        super();
        Configurations configs = new Configurations();
        try {
            config = configs.properties(Constants.CONFIG_HOME + "botconfig.properties");
            logger.log(Level.INFO, () -> CONFIG + " Config Properties found.");
        } catch (ConfigurationException ce) {
            logger.error(() -> CONFIG + " Config Not Found!", ce);
        }
    }

    private static class InstanceHolder {

        private static final Config config = new Config();
    }

    public static Config getInstance() {
        return InstanceHolder.config;
    }

    public String getProperty(String key) {
        if (key == null || key.isEmpty()) {
            throw new BotException("Bad Config Key Provided", BotExceptionType.ERROR);
        }
        String value = config.getString(key);
        if (value == null || value.isEmpty()) {
            logger.log(Level.INFO, () -> CONFIG + " Property : " + key + " Not Found!");
            throw new BotException("No Config Value Found For Key Provided");
        }
        logger.log(Level.INFO, () -> CONFIG + " Property : " + key);
        return value;
    }

}
