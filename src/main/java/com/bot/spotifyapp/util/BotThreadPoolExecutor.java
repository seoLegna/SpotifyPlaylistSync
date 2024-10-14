package com.bot.spotifyapp.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BotThreadPoolExecutor extends ThreadPoolExecutor {

    private static final Logger logger = BotLogger.getInstance().getLogger();

    private static final String BOT_THREAD_POOL_EXECUTOR = "BOT_THREAD_POOL_EXECUTOR";

    private static final Config config = Config.getInstance();

    private BotThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        logger.log(Level.INFO, () -> BOT_THREAD_POOL_EXECUTOR + " Context Initialized.");
    }

    private static class InstanceHolder {

        private static BotThreadPoolExecutor getExecutorSingletonInstance() {
            BotThreadPoolExecutor botThreadPoolExecutor = null;
            try {
                int corePoolSize = Integer.parseInt(config.getProperty(Constants.BOT_CORE_POOL_SIZE));
                int maxPoolSize = Integer.parseInt(config.getProperty(Constants.BOT_MAXIMUM_POOL_SIZE));
                int keepAliveTime = Integer.parseInt(config.getProperty(Constants.BOT_KEEP_ALIVE_TIME));
                TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;
                botThreadPoolExecutor = new BotThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, keepAliveTimeUnit, new LinkedBlockingQueue<Runnable>());
            } catch (NumberFormatException e) {
                logger.error(() -> BOT_THREAD_POOL_EXECUTOR + " Invalid Property Value", e);
                throw e;
            } catch (Exception e) {
                logger.error(() -> BOT_THREAD_POOL_EXECUTOR + " ERROR!", e);
                throw e;
            }
            return botThreadPoolExecutor;
        }

        private static final BotThreadPoolExecutor instance = getExecutorSingletonInstance();
    }

    public static BotThreadPoolExecutor getInstance() {
        return InstanceHolder.instance;
    }
}
