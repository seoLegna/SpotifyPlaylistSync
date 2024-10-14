package com.bot.spotifyapp.service.impl;

import com.bot.spotifyapp.client.SpotifyClient;
import com.bot.spotifyapp.exception.BotException;
import com.bot.spotifyapp.service.intf.SpotifyServiceIntf;
import com.bot.spotifyapp.to.*;
import com.bot.spotifyapp.type.BotExceptionType;
import com.bot.spotifyapp.util.*;
import jakarta.ws.rs.core.UriBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SpotifyServiceImpl implements SpotifyServiceIntf {

    private final Config config = Config.getInstance();

    private final Logger logger = BotLogger.getInstance().getLogger();

    private static final String SPOTIFY_SERVICE_IMPL = " SPOTIFY_SERVICE_IMPL ";

    private final SpotifyClient client = SpotifyClient.getInstance();

    private String token = null;

    private String refreshToken = null;

    private String playlistId = null;

    private static final String FIELD_FOR_TOTAL_NAME_ID = "total,items(track(name,uri))";

    private Integer totalSongsInPlaylistForSync = null;

    private boolean syncCalled = false;

    private boolean appAuthorized = false;

    private SpotifyServiceImpl() {
        super();
    }

    private static class InstanceHolder {

        private static final SpotifyServiceImpl instance = new SpotifyServiceImpl();
    }

    public static SpotifyServiceImpl getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public AccessTokenTO getServerToServerAccessToken() throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getAccessToken : start");
        String clientId = config.getProperty(Constants.CLIENT_ID);
        String clientSecret = config.getProperty(Constants.CLIENT_SECRET);
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getAccessToken : end");
        return client.getServerToServerAccessToken(clientId, clientSecret);
    }

    @Override
    public URI authorizeApp() throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " authorizeApp : start");
        String clientId = config.getProperty(Constants.CLIENT_ID);
        String responseType = "code";
        String redirectUri = "http://localhost:8080/spotifyapp/res/spotify/token";
        String state = UUID.randomUUID().toString().substring(0, 16);
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " authorizeApp state : " + state);
        String scope = "user-library-read playlist-modify-public playlist-modify-private";
        UriBuilder builder = UriBuilder.fromPath("https://accounts.spotify.com/authorize").queryParam("client_id", clientId)
                .queryParam("response_type", responseType)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state", state)
                .queryParam("scope", scope);
        CacheMap cacheMap = CacheMap.getInstance();
        cacheMap.addToCache(state, ThreadContext.get(Constants.UUID));
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " authorizeApp : end");
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " authorizeApp URI = " + builder.toString());
        URI responseUri = null;
        try {
            responseUri = new URI(builder.toString());
        } catch (URISyntaxException e) {
            logger.log(Level.ERROR, () -> SPOTIFY_SERVICE_IMPL + " URI Syntax Exception ", e);
        }
        return responseUri;
    }

    @Override
    public AccessTokenTO getUserAccessToken(String code) throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getUserAccessToken : start");
        String clientId = config.getProperty(Constants.CLIENT_ID);
        String clientSecret = config.getProperty(Constants.CLIENT_SECRET);
        AccessTokenTO accessToken = client.getUserAccessToken(clientId, clientSecret, code);
        if (accessToken.getAccess_token() != null && !accessToken.getAccess_token().isEmpty()) {
            this.token = accessToken.getAccess_token();
        }
        if (accessToken.getRefresh_token() != null && !accessToken.getRefresh_token().isEmpty()) {
            this.refreshToken = accessToken.getRefresh_token();
        }
        this.appAuthorized = true;
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getUserAccessToken : end");
        return accessToken;
    }

    @Override
    public TracksTO getMyLikedSongs(String market, Integer limit, Integer offset) throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getMyLikedSongs : start");
        String accessToken = token;
        TracksTO tracksTO = client.getMyLikedSongs(accessToken, market, limit, offset);
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getMyLikedSongs : end");
        return tracksTO;
    }

    @Override
    public AccessTokenTO getUserRefreshToken() throws BotException {
        if (this.appAuthorized) {
            logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getUserRefreshToken : start");
            String clientId = config.getProperty(Constants.CLIENT_ID);
            String clientSecret = config.getProperty(Constants.CLIENT_SECRET);
            AccessTokenTO accessToken = client.getUserRefreshToken(clientId, clientSecret, this.refreshToken);
            if (accessToken.getAccess_token() != null && !accessToken.getAccess_token().isEmpty()) {
                this.token = accessToken.getAccess_token();
            }
            if (accessToken.getRefresh_token() != null && !accessToken.getRefresh_token().isEmpty()) {
                this.refreshToken = accessToken.getRefresh_token();
            }
            logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getUserRefreshToken : end");
            return accessToken;
        }
        return null;
    }

    /*

    @Override
    public AccessTokenTO getRefreshTokenForScheduler() {
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getRefreshTokenForScheduler : start");
        AccessTokenTO at = new AccessTokenTO();
        at.setRefresh_token(this.refreshToken);
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getRefreshTokenForScheduler : end");
        return at;
    }

     */

    @Override
    public AddPlaylistTO moveAllLikedSongsToPlaylist(String playlistId) throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " moveAllLikedSongsToPlaylist : start");
        List<String> listOfLikedSongsUris = getListOfLikedSongUris();
        Collections.reverse(listOfLikedSongsUris);
        int totalItems = listOfLikedSongsUris.size();
        int fromIndex = 0;
        int toIndex = Math.min(totalItems, 100);
        List<String> subList = null;
        while (toIndex < totalItems) {
            subList = listOfLikedSongsUris.subList(fromIndex, toIndex);
            addSongsToPlaylist(playlistId, null, subList);
            fromIndex = toIndex;
            toIndex += 100;
        }
        toIndex = totalItems;
        subList = listOfLikedSongsUris.subList(fromIndex, toIndex);
        AddPlaylistTO resp = addSongsToPlaylist(playlistId, null, subList);
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " moveAllLikedSongsToPlaylist : end");
        return resp;
    }

    @Override
    public AddPlaylistTO addSongsToPlaylist(String playlistId, Integer position, List<String> trackUris) throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " addSongsToPlaylist : start");
        AddPlaylistTO response = client.addSongsToPlaylist(this.token, playlistId, position, trackUris);
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " addSongsToPlaylist : end");
        return response;
    }

    private List<String> getListOfLikedSongUris() throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getListOfLikedSongUris : start");
        List<String> listOfLikedSongsUris = new ArrayList<>();
        int limit = 50;
        int offset = 0;
        TracksTO tracks = client.getMyLikedSongs(this.token, null, limit, offset);
        int total = tracks.getTotal();
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " Total Liked Songs : " + total);
        while (tracks.getItems() != null && !tracks.getItems().isEmpty()) {
            for (ItemsTO items : tracks.getItems()) {
                listOfLikedSongsUris.add(items.getTrack().getUri());
            }
            offset += limit;
            tracks = client.getMyLikedSongs(this.token, null, limit, offset);
        }
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " Total Song URIs Added To List : " + listOfLikedSongsUris.size());
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getListOfLikedSongUris : end");
        return listOfLikedSongsUris;
    }

    @Override
    public AddPlaylistTO syncPlaylistWithLikedSongs(String playlistId) throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " syncPlaylistWithLikedSongs : start");
        TracksTO playlistTrack = getPlaylistSongs(playlistId, null, 1, 0, FIELD_FOR_TOTAL_NAME_ID);
        this.totalSongsInPlaylistForSync = playlistTrack.getTotal();
        this.playlistId = playlistId;
        playlistTrack = getPlaylistSongs(playlistId, null, 1, this.totalSongsInPlaylistForSync - 1, FIELD_FOR_TOTAL_NAME_ID);
        TracksTO tracksTO = client.getMyLikedSongs(this.token, null, 50 , 0);
        AddPlaylistTO resp = syncSongsToPlaylist(tracksTO, playlistTrack);
        this.syncCalled = true;
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " syncPlaylistWithLikedSongs : end");
        return resp;
    }

    private AddPlaylistTO syncSongsToPlaylist(TracksTO likedSongTracks, TracksTO playlistTrack) {
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " syncSongsToPlaylist : start");
        String trackToSyncAfter = playlistTrack.getItems().get(0).getTrack().getUri();
        List<String> tracksToAdd = new ArrayList<>();
        for (ItemsTO item : likedSongTracks.getItems()) {
            if (item.getTrack().getUri().equals(trackToSyncAfter)) {
                break;
            }
            tracksToAdd.add(item.getTrack().getUri());
        }
        if (tracksToAdd.isEmpty()) {
            logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " Playlist is already synced! ");
            AddPlaylistTO resp = new AddPlaylistTO();
            resp.setPlaylistId(this.playlistId);
            resp.setMessage(" Playlist is already synced! ");
            return resp;
        }
        if (tracksToAdd.size() == 50) {
            logger.log(Level.ERROR, () -> SPOTIFY_SERVICE_IMPL + " The last track from the playlist was not found in the recent 50 liked songs. ");
            throw new BotException("The last track from the playlist was not found in the recent 50 liked songs.", BotExceptionType.ERROR);
        }
        Collections.reverse(tracksToAdd);
        AddPlaylistTO resp = addSongsToPlaylist(this.playlistId, null, tracksToAdd);
        this.totalSongsInPlaylistForSync += tracksToAdd.size();
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " syncSongsToPlaylist : start");
        return resp;
    }

    @Override
    public void keepPlaylistSynced() throws BotException {
        if (this.playlistId != null && !this.playlistId.isEmpty() && this.syncCalled) {
            logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " keepPlaylistSynced : start");
            TracksTO playlistTrack = getPlaylistSongs(playlistId, null, 1, this.totalSongsInPlaylistForSync - 1, FIELD_FOR_TOTAL_NAME_ID);
            TracksTO tracksTO = client.getMyLikedSongs(this.token, null, 50 , 0);
            syncSongsToPlaylist(tracksTO, playlistTrack);
            logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " keepPlaylistSynced : end");
        }
    }

    @Override
    public TracksTO getPlaylistSongs(String playlistId, String market, Integer limit, Integer offset, String field) {
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getPlaylistSongs : start");
        TracksTO playlistTracks = client.getPlaylistSongs(this.token, playlistId, market, limit, offset, null);
        logger.log(Level.INFO, () -> SPOTIFY_SERVICE_IMPL + " getPlaylistSongs : end");
        return playlistTracks;
    }


}
