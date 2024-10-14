package com.bot.spotifyapp.util;

import com.bot.spotifyapp.exception.BotException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class BotQuartzScheduler {

    private final SchedulerFactory schedulerFactory;

    private final Scheduler scheduler;

    private final Logger logger = BotLogger.getInstance().getLogger();

    private static final String BOT_QUARTZ_SCHEDULER = " BOT_QUARTZ_SCHEDULER ";

    private static final String SCHEDULER_EXCEPTION = " Scheduler Exception ";

    private BotQuartzScheduler() {
        super();
        try {
            this.schedulerFactory = new StdSchedulerFactory(Constants.CONFIG_HOME + "quartz.properties");
            this.scheduler = schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            logger.log(Level.ERROR, () -> BOT_QUARTZ_SCHEDULER + SCHEDULER_EXCEPTION, e);
            throw new BotException(e.getMessage());
        }
    }

    private static class InstanceHolder {

        private static final BotQuartzScheduler instance = new BotQuartzScheduler();
    }

    public static BotQuartzScheduler getInstance() {
        return InstanceHolder.instance;
    }

    public void startScheduler() {
        logger.log(Level.INFO, () -> BOT_QUARTZ_SCHEDULER + " Scheduler Started! ");
        try {
            this.scheduler.start();
        } catch (SchedulerException e) {
            logger.log(Level.ERROR, () -> BOT_QUARTZ_SCHEDULER + SCHEDULER_EXCEPTION, e);
            throw new BotException(e.getMessage());
        }
    }

    public void stopScheduler() {
        logger.log(Level.INFO, () -> BOT_QUARTZ_SCHEDULER + " Scheduler Stopped! ");
        try {
            this.scheduler.shutdown(true);
        } catch (SchedulerException e) {
            logger.log(Level.ERROR, () -> BOT_QUARTZ_SCHEDULER + SCHEDULER_EXCEPTION, e);
            throw new BotException(e.getMessage());
        }
    }

    public void scheduleJob(JobDetail job, Trigger trigger) {
        logger.log(Level.INFO, () -> BOT_QUARTZ_SCHEDULER + " Job Scheduled! ");
        try {
            this.scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            logger.log(Level.ERROR, () -> BOT_QUARTZ_SCHEDULER + SCHEDULER_EXCEPTION, e);
            throw new BotException(e.getMessage());
        }
    }
}
