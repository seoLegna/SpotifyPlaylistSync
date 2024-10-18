package com.bot.spotifyapp.util;

import com.bot.spotifyapp.service.impl.SpotifyServiceImpl;
import com.bot.spotifyapp.service.intf.SpotifyServiceIntf;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BotSyncLikedSongsQuartzJob implements Job {

    private final SpotifyServiceIntf spotifyService = SpotifyServiceImpl.getInstance();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        spotifyService.keepPlaylistSynced();
    }
}