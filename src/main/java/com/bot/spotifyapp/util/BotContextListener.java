package com.bot.spotifyapp.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

@WebListener
public class BotContextListener implements ServletContextListener {

    private static final String BOT_CONTEXT_LISTENER = " BOT_CONTEXT_LISTENER ";

    private final Logger logger = BotLogger.getInstance().getLogger();

    private final BotThreadPoolExecutor botThreadPoolExecutor = BotThreadPoolExecutor.getInstance();

    private final BotQuartzScheduler scheduler = BotQuartzScheduler.getInstance();

    private final BotQuartzClient quartzClient = BotQuartzClient.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ThreadContext.put(Constants.UUID, "main");
        this.scheduler.startScheduler();
        this.quartzClient.startRefreshTokenJob();
        this.quartzClient.startLikedSongsSyncJob();
        logger.log(Level.INFO, () -> BOT_CONTEXT_LISTENER + " Context Initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        this.botThreadPoolExecutor.shutdown();
        this.scheduler.stopScheduler();
        logger.log(Level.INFO, () -> BOT_CONTEXT_LISTENER + " Context Destroyed.");
    }
}
