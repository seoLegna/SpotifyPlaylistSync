package com.bot.spotifyapp.util;

import org.quartz.*;

public class BotQuartzClient {

    private final BotQuartzScheduler scheduler = BotQuartzScheduler.getInstance();

    private BotQuartzClient() {
        super();
    }

    private static class InstanceHolder {

        private static final BotQuartzClient instance = new BotQuartzClient();
    }

    public static BotQuartzClient getInstance() {
        return InstanceHolder.instance;
    }

    public void startRefreshTokenJob() {
        JobDetail refreshJob = JobBuilder.newJob(BotRefreshTokenQuartzJob.class)
                .withIdentity("refreshJob", "refreshGroup")
                .build();
        Trigger refreshTrigger = TriggerBuilder.newTrigger()
                .withIdentity("refreshTrigger", "refreshGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(50)
                        .repeatForever())
                .build();
        this.scheduler.scheduleJob(refreshJob, refreshTrigger);
    }

    public void startLikedSongsSyncJob() {
        JobDetail syncJob = JobBuilder.newJob(BotSyncLikedSongsQuartzJob.class)
                .withIdentity("syncJob", "syncGroup")
                .build();
        Trigger syncTrigger = TriggerBuilder.newTrigger()
                .withIdentity("syncTrigger", "syncGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever())
                .build();
        this.scheduler.scheduleJob(syncJob, syncTrigger);
    }
}
