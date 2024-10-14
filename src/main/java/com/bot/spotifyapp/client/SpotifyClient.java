package com.bot.spotifyapp.client;

import com.bot.spotifyapp.exception.BotException;
import com.bot.spotifyapp.to.AccessTokenTO;
import com.bot.spotifyapp.to.AddPlaylistTO;
import com.bot.spotifyapp.to.ErrorResponseTO;
import com.bot.spotifyapp.to.TracksTO;
import com.bot.spotifyapp.type.BotExceptionType;
import com.bot.spotifyapp.util.BotLogger;
import com.bot.spotifyapp.util.Config;
import com.bot.spotifyapp.util.Constants;
import com.google.gson.Gson;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class SpotifyClient {

    private final Client client;

    private final WebTarget target;

    private final Client tokenClient;

    private final WebTarget tokenTarget;

    private static final Logger logger = BotLogger.getInstance().getLogger();

    private static final String SPOTIFY_CLIENT = "SPOTIFY_CLIENT ";

    private static final String AUTHORIZATION = "Authorization";

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String BEARER = "Bearer ";

    private static final String APPLICATION_JSON_CONTENT_TYPE = "application/json";

    private static final String APPLICATION_FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";

    private static final String BASIC = "Basic ";

    private final Gson gson = new Gson();

    private SpotifyClient(String uri) {
        super();
        tokenClient = ClientBuilder.newClient();
        tokenTarget = tokenClient.target("https://accounts.spotify.com");
        client = ClientBuilder.newClient();
        target = client.target(uri);
    }

    private static class InstanceHolder {

        private static final SpotifyClient instance = new SpotifyClient("https://api.spotify.com");
    }

    public static SpotifyClient getInstance() {
        return InstanceHolder.instance;
    }

    public AccessTokenTO getServerToServerAccessToken(String clientId, String clientSecret) throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getAccessToken : start");
        WebTarget getTokenTarget = tokenTarget.path("api").path("token");
        Invocation.Builder builder = getTokenTarget.request(MediaType.APPLICATION_JSON);
        builder.header(CONTENT_TYPE, APPLICATION_FORM_CONTENT_TYPE);
        Form form = new Form();
        form.param("grant_type", "client_credentials");
        form.param("client_id", clientId);
        form.param("client_secret", clientSecret);
        try (Response response = builder.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE))) {
            if (response.getStatus() == 200) {
                AccessTokenTO accessToken = response.readEntity(AccessTokenTO.class);
                String token = accessToken.toString();
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + token);
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getAccessToken : end");
                return accessToken;
            } else {
                ErrorResponseTO error = response.readEntity(ErrorResponseTO.class);
                logger.log(Level.ERROR, () -> SPOTIFY_CLIENT + error.toString());
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getAccessToken : end");
                throw new BotException(error.getMessage(), BotExceptionType.BAD_STATUS, error);
            }
        } catch (ResponseProcessingException e) {
            logger.log(Level.ERROR, () -> SPOTIFY_CLIENT + Constants.ERROR_PROCESSING_REQUEST, e);
            logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getAccessToken : end");
            throw new BotException(e.getMessage(), BotExceptionType.ERROR);
        }
    }

    /*
    public Response authorizeApp(String clientId) {
        logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " authorizeApp : start");
        WebTarget getTokenTarget = tokenTarget.path("authorize");
        String responseType = "code";
        String redirectUri = "http://localhost:8080/spotifyapp/res/spotify/token";
        String state = UUID.randomUUID().toString().substring(0, 16);
        logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " authorizeApp state : " + state);
        String scope = "user-library-read";
        getTokenTarget.queryParam("client_id", clientId)
                .queryParam("response_type", responseType)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state", state)
                .queryParam("scope", scope);
        Invocation.Builder builder  = getTokenTarget.request(MediaType.TEXT_HTML);
        logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " authorizeApp : end");
        return builder.get();
    }
     */

    public AccessTokenTO getUserAccessToken(String clientId, String clientSecret, String code) throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getUserAccessToken : start");
        WebTarget getTokenTarget = tokenTarget.path("api").path("token");
        Invocation.Builder builder = getTokenTarget.request(MediaType.APPLICATION_JSON);
        String authorizationMap = clientId + ":" + clientSecret;
        byte[] byteAuthorization = authorizationMap.getBytes();
        builder.header(CONTENT_TYPE, APPLICATION_FORM_CONTENT_TYPE);
        builder.header(AUTHORIZATION, BASIC + Base64.getEncoder().encodeToString(byteAuthorization));
        String redirectUri = "http://localhost:8080/spotifyapp/res/spotify/token";
        Form form = new Form();
        form.param("grant_type", "authorization_code");
        form.param("code", code);
        form.param("redirect_uri", redirectUri);
        try (Response response = builder.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE))) {
            if (response.getStatus() == 200) {
                AccessTokenTO accessToken = response.readEntity(AccessTokenTO.class);
                String token = accessToken.toString();
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + token);
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getUserAccessToken : end");
                return accessToken;
            } else {
                ErrorResponseTO error = response.readEntity(ErrorResponseTO.class);
                logger.log(Level.ERROR, () -> SPOTIFY_CLIENT + error.toString());
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getUserAccessToken : end");
                throw new BotException(error.getMessage(), BotExceptionType.BAD_STATUS, error);
            }
        } catch (ResponseProcessingException e) {
            logger.log(Level.ERROR, () -> SPOTIFY_CLIENT + Constants.ERROR_PROCESSING_REQUEST, e);
            logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getUserAccessToken : end");
            throw new BotException(e.getMessage(), BotExceptionType.ERROR);
        }
    }

    public AccessTokenTO getUserRefreshToken(String clientId, String clientSecret, String refreshToken) throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getUserRefreshToken : start");
        WebTarget getTokenTarget = tokenTarget.path("api").path("token");
        Invocation.Builder builder = getTokenTarget.request(MediaType.APPLICATION_JSON);
        String authorizationMap = clientId + ":" + clientSecret;
        byte[] byteAuthorization = authorizationMap.getBytes();
        builder.header(CONTENT_TYPE, APPLICATION_FORM_CONTENT_TYPE);
        builder.header(AUTHORIZATION, BASIC + Base64.getEncoder().encodeToString(byteAuthorization));
        Form form = new Form();
        form.param("grant_type", "refresh_token");
        form.param("refresh_token", refreshToken);
        try (Response response = builder.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE))) {
            if (response.getStatus() == 200) {
                AccessTokenTO accessToken = response.readEntity(AccessTokenTO.class);
                String token = accessToken.toString();
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + token);
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getUserRefreshToken : end");
                return accessToken;
            } else {
                logger.log(Level.ERROR, () -> SPOTIFY_CLIENT + " Bad Response Code : " + response.getStatus());
                ErrorResponseTO error = response.readEntity(ErrorResponseTO.class);
                logger.log(Level.ERROR, () -> SPOTIFY_CLIENT + error.toString());
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getUserRefreshToken : end");
                throw new BotException(error.getMessage(), BotExceptionType.BAD_STATUS, error);
            }
        } catch (ResponseProcessingException e) {
            logger.log(Level.ERROR, () -> SPOTIFY_CLIENT + Constants.ERROR_PROCESSING_REQUEST, e);
            logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getUserRefreshToken : end");
            throw new BotException(e.getMessage(), BotExceptionType.ERROR);
        }
    }

    // max limit : 50
    public TracksTO getMyLikedSongs(String token, String market, Integer limit, Integer offset) throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getMyLikedSongs : start");
        WebTarget getLikedSongsTarget = target.path("v1").path("me").path("tracks");
        if (market != null && market.length() == 2) {
            getLikedSongsTarget = getLikedSongsTarget.queryParam("market", market);
        }
        if (limit != null && limit != 0 && limit <= 50) {
            getLikedSongsTarget = getLikedSongsTarget.queryParam("limit", limit);
        }
        if (offset != null) {
            getLikedSongsTarget = getLikedSongsTarget.queryParam("offset", offset);
        }
        logger.log(Level.INFO, SPOTIFY_CLIENT + " URL : " + getLikedSongsTarget.getUri().toString());
        Invocation.Builder builder = getLikedSongsTarget.request(MediaType.APPLICATION_JSON);
        builder.header(AUTHORIZATION, BEARER + token);
        try (Response response = builder.get();) {
            if (response.getStatus() == 200) {
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getMyLikedSongs Response Code : " + response.getStatus());
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getMyLikedSongs : end");
                return response.readEntity(TracksTO.class);
            } else {
                ErrorResponseTO error = response.readEntity(ErrorResponseTO.class);
                logger.log(Level.ERROR, () -> SPOTIFY_CLIENT + error.toString());
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getMyLikedSongs : end");
                throw new BotException(error.getMessage(), BotExceptionType.BAD_STATUS, error);
            }
        } catch (ResponseProcessingException e) {
            logger.log(Level.ERROR, () -> SPOTIFY_CLIENT + Constants.ERROR_PROCESSING_REQUEST, e);
            logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getMyLikedSongs : end");
            throw new BotException(e.getMessage(), BotExceptionType.ERROR);
        }
    }

    // max tracks: 100
    public AddPlaylistTO addSongsToPlaylist(String token, String playlistId, Integer position, List<String> tracks) throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " addSongsToPlaylist : start");
        WebTarget addSongsToPlaylistTarget = target.path("v1").path("playlists").path(playlistId).path("tracks");
        Invocation.Builder builder = addSongsToPlaylistTarget.request(MediaType.APPLICATION_JSON);
        builder.header(AUTHORIZATION, BEARER + token);
        builder.header(CONTENT_TYPE, APPLICATION_JSON_CONTENT_TYPE);
        AddPlaylistTO body = new AddPlaylistTO();
        if (position != null && position != 0) {
            body.setPosition(position);
        }
        if (tracks != null && !tracks.isEmpty()) {
            body.setUris(tracks);
        } else {
            logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " addSongsToPlaylist : end");
            throw new BotException(" The tracks provided are null or empty! ", BotExceptionType.ERROR);
        }
        try (Response response = builder.post(Entity.entity(body, MediaType.APPLICATION_JSON));) {
            if (response.getStatus() == 201) {
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " addSongsToPlaylist Response Code : " + response.getStatus());
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " addSongsToPlaylist : end");
                AddPlaylistTO goodResp = response.readEntity(AddPlaylistTO.class);
                goodResp.setMessage("Songs added to playlist successfully");
                return goodResp;
            } else {
                ErrorResponseTO error = response.readEntity(ErrorResponseTO.class);
                logger.log(Level.ERROR, () -> SPOTIFY_CLIENT + error.toString());
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " addSongsToPlaylist : end");
                throw new BotException(error.getMessage(), BotExceptionType.BAD_STATUS, error);
            }
        } catch (ResponseProcessingException e) {
            logger.log(Level.ERROR, () -> SPOTIFY_CLIENT + Constants.ERROR_PROCESSING_REQUEST, e);
            logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " addSongsToPlaylist : end");
            throw new BotException(e.getMessage(), BotExceptionType.ERROR);
        }
    }

    // max limit : 50
    public TracksTO getPlaylistSongs(String token, String playlistId, String market, Integer limit, Integer offset, String fields) throws BotException {
        logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getPlaylistSongs : start");
        WebTarget getLikedSongsTarget = target.path("v1").path("playlists").path(playlistId).path("tracks");
        if (market != null && market.length() == 2) {
            getLikedSongsTarget = getLikedSongsTarget.queryParam("market", market);
        }
        if (limit != null && limit != 0 && limit <= 50) {
            getLikedSongsTarget = getLikedSongsTarget.queryParam("limit", limit);
        }
        if (offset != null) {
            getLikedSongsTarget = getLikedSongsTarget.queryParam("offset", offset);
        }
        if (fields != null && !fields.isEmpty()) {
            getLikedSongsTarget = getLikedSongsTarget.queryParam("fields", fields);
        }
        logger.log(Level.INFO, SPOTIFY_CLIENT + " URL : " + getLikedSongsTarget.getUri().toString());
        Invocation.Builder builder = getLikedSongsTarget.request(MediaType.APPLICATION_JSON);
        builder.header(AUTHORIZATION, BEARER + token);
        try (Response response = builder.get();) {
            if (response.getStatus() == 200) {
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getPlaylistSongs Response Code : " + response.getStatus());
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getPlaylistSongs : end");
                return response.readEntity(TracksTO.class);
            } else {
                ErrorResponseTO error = response.readEntity(ErrorResponseTO.class);
                logger.log(Level.ERROR, () -> SPOTIFY_CLIENT + error.toString());
                logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getPlaylistSongs : end");
                throw new BotException(error.getMessage(), BotExceptionType.BAD_STATUS, error);
            }
        } catch (ResponseProcessingException e) {
            logger.log(Level.ERROR, () -> SPOTIFY_CLIENT + Constants.ERROR_PROCESSING_REQUEST, e);
            logger.log(Level.INFO, () -> SPOTIFY_CLIENT + " getPlaylistSongs : end");
            throw new BotException(e.getMessage(), BotExceptionType.ERROR);
        }
    }
}
