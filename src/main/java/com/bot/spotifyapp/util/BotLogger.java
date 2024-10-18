package com.bot.spotifyapp.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;

public class BotLogger {

    private static Logger mainLogger;

    private BotLogger() {
        super();
        File file = new File(Constants.CONFIG_HOME + "log4j.xml");
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        loggerContext.setConfigLocation(file.toURI());
        mainLogger = LogManager.getLogger(Constants.WHATSAPP_BOT);
    }

    private static class InstanceHolder {

        private static final BotLogger logger = new BotLogger();

    }

    public static BotLogger getInstance() {
        return InstanceHolder.logger;
    }

    public Logger getLogger() {
        return mainLogger;
    }

}
