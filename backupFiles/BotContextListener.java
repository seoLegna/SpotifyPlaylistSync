package com.bot.spotifyapp.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BotContextListener implements ServletContextListener {

    private final String BOT_CONTEXT_LISTENER = "BOT_CONTEXT_LISTENER";

    private Logger logger = BotLogger.getInstance().getLogger();

    private BotThreadPoolExecutor botThreadPoolExecutor;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.log(Level.INFO, BOT_CONTEXT_LISTENER, "Context Initialized.");
        try {
            int corePoolSize = Integer.parseInt(Config.getProperty(Constants.BOT_CORE_POOL_SIZE));
            int maxPoolSize = Integer.parseInt(Config.getProperty(Constants.BOT_MAXIMUM_POOL_SIZE));
            int keepAliveTime = Integer.parseInt(Config.getProperty(Constants.BOT_KEEP_ALIVE_TIME));
            TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;
            botThreadPoolExecutor = new BotThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, keepAliveTimeUnit, new LinkedBlockingQueue<Runnable>());
        } catch (NumberFormatException e) {
            logger.error(BOT_CONTEXT_LISTENER, " Invalid Property Value", e);
        } catch (Exception e) {
            logger.error(BOT_CONTEXT_LISTENER, " ERROR!", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        this.botThreadPoolExecutor.shutdown();
    }

}
