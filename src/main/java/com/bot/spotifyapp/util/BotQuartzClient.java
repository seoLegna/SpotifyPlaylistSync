package com.bot.spotifyapp.util;

import org.quartz.*;

public class BotQuartzClient {

    private final BotQuartzScheduler scheduler = BotQuartzScheduler.getInstance();

    private static final String REFRESH_JOB = "refreshJob";

    private static final String REFRESH_GROUP = "refreshGroup";

    private static final String REFRESH_TRIGGER = "refreshTrigger";

    private static final String SYNC_JOB = "syncJob";

    private static final String SYNC_GROUP = "syncGroup";

    private static final String SYNC_TRIGGER = "syncTrigger";

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
                .withIdentity(REFRESH_JOB, REFRESH_GROUP)
                .build();
        Trigger refreshTrigger = TriggerBuilder.newTrigger()
                .withIdentity(REFRESH_TRIGGER, REFRESH_GROUP)
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(50)
                        .repeatForever())
                .build();
        this.scheduler.scheduleJob(refreshJob, refreshTrigger);
    }

    public void startLikedSongsSyncJob() {
        JobDetail syncJob = JobBuilder.newJob(BotSyncLikedSongsQuartzJob.class)
                .withIdentity(SYNC_JOB, SYNC_GROUP)
                .build();
        Trigger syncTrigger = TriggerBuilder.newTrigger()
                .withIdentity(SYNC_TRIGGER, SYNC_GROUP)
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever())
                .build();
        this.scheduler.scheduleJob(syncJob, syncTrigger);
    }
}
