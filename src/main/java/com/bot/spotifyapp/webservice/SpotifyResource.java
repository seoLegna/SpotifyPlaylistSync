package com.bot.spotifyapp.webservice;

import com.bot.spotifyapp.exception.BotException;
import com.bot.spotifyapp.service.impl.SpotifyServiceImpl;
import com.bot.spotifyapp.service.intf.SpotifyServiceIntf;
import com.bot.spotifyapp.to.AccessTokenTO;
import com.bot.spotifyapp.to.AddPlaylistTO;
import com.bot.spotifyapp.to.ErrorResponseTO;
import com.bot.spotifyapp.to.TracksTO;
import com.bot.spotifyapp.util.BotLogger;
import com.bot.spotifyapp.util.BotThreadPoolExecutor;
import com.bot.spotifyapp.util.Constants;
import com.bot.spotifyapp.util.ValidateProperties;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

@Path("/spotify")
public class SpotifyResource {

    private final ThreadPoolExecutor executor = BotThreadPoolExecutor.getInstance();

    private final Logger logger = BotLogger.getInstance().getLogger();

    private static final String SPOTIFY_RESOURCE = "SPOTIFY_RESOURCE";

    private final SpotifyServiceIntf spotifyService = SpotifyServiceImpl.getInstance();

    /*
    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    public void getAccessToken(@Suspended final AsyncResponse response) {

        executor.execute(() -> {
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getAccessToken : start");
            AccessTokenTO accessTokenTO = spotifyService.getAccessToken();
            response.resume(accessTokenTO);
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getAccessToken : end");
        });
    }
    */

    @GET
    @Path("/authorize")
    @Produces(MediaType.TEXT_HTML)
    public void authorizeApp(@Suspended final AsyncResponse response) {

        executor.execute(() -> {
            ThreadContext.put(Constants.UUID, UUID.randomUUID().toString());
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " authorizeApp : start");
            try {
                URI resUri = spotifyService.authorizeApp();
                response.resume(Response.seeOther(resUri).build());
            } catch (BotException e) {
                response.resume(e.getError());
            } catch (Exception e) {
                ErrorResponseTO error = new ErrorResponseTO();
                error.setStatus(801);
                error.setMessage(e.getMessage());
                response.resume(error);
            }
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " authorizeApp : end");
        });
    }

    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    public void getUserAccessToken(@Suspended final AsyncResponse response,
                                   @DefaultValue("") @QueryParam("code") String code,
                                   @DefaultValue("") @QueryParam("state") String state,
                                   @DefaultValue("") @QueryParam("error") String error) {

        executor.execute(() -> {
            ThreadContext.put(Constants.UUID, UUID.randomUUID().toString());
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getUserAccessToken : start");
            try {
                ValidateProperties.arePropertiesValidForGetUserAccessToken(code, state, error);
                AccessTokenTO accessTokenTO = spotifyService.getUserAccessToken(code);
                response.resume(accessTokenTO);
            }  catch (BotException e) {
                response.resume(e.getError());
            } catch (Exception e) {
                ErrorResponseTO err = new ErrorResponseTO();
                err.setStatus(801);
                err.setMessage(e.getMessage());
                response.resume(err);
            }
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getUserAccessToken : end");
        });

    }

    @GET
    @Path("/my-liked-songs")
    @Produces(MediaType.APPLICATION_JSON)
    public void getMySavedSongs(@Suspended final AsyncResponse response,
                                @DefaultValue("") @QueryParam("market") String market,
                                @DefaultValue("20") @QueryParam("limit") Integer limit,
                                @DefaultValue("0") @QueryParam("offset") Integer offset) {

        executor.execute(() -> {
            ThreadContext.put(Constants.UUID, UUID.randomUUID().toString());
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getMySavedSongs : start");
            try {
                ValidateProperties.arePropertiesValidForGetMySavedSongs(market, limit);
                TracksTO tracksTO = spotifyService.getMyLikedSongs(market, limit, offset);
                response.resume(tracksTO);
            } catch (BotException e) {
                response.resume(e.getError());
            } catch (Exception e) {
                ErrorResponseTO error = new ErrorResponseTO();
                error.setStatus(801);
                error.setMessage(e.getMessage());
                response.resume(error);
            }
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getAccessToken : end");
        });
    }

    /*
    @GET
    @Path("/refresh-token")
    @Produces(MediaType.APPLICATION_JSON)
    public void getRefreshToken(@Suspended final AsyncResponse response) {

        executor.execute(() -> {
            ThreadContext.put(Constants.UUID, UUID.randomUUID().toString());
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getRefreshToken : start");
            try {
                AccessTokenTO refreshToken = spotifyService.getUserRefreshToken();
                response.resume(refreshToken);
            } catch (BotException e) {
                response.resume(e.getError());
            } catch (Exception e) {
                ErrorResponseTO error = new ErrorResponseTO();
                error.setStatus(801);
                error.setMessage(e.getMessage());
                response.resume(error);
            }
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getRefreshToken : end");
        });
    }

    @GET
    @Path("/refresh-token-scheduler")
    @Produces(MediaType.APPLICATION_JSON)
    public void getRefreshTokenForScheduler(@Suspended final AsyncResponse response) {

        executor.execute(() -> {
            ThreadContext.put(Constants.UUID, UUID.randomUUID().toString());
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getRefreshToken : start");
            try {
                AccessTokenTO refreshToken = spotifyService.getRefreshTokenForScheduler();
                response.resume(refreshToken);
            } catch (BotException e) {
                response.resume(e.getError());
            } catch (Exception e) {
                ErrorResponseTO error = new ErrorResponseTO();
                error.setStatus(801);
                error.setMessage(e.getMessage());
                response.resume(error);
            }
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getRefreshToken : end");
        });
    }
     */

    /*

    @GET
    @Path("/uris")
    @Produces(MediaType.APPLICATION_JSON)
    public void getUris(@Suspended final AsyncResponse response) {

        executor.execute(() -> {
            ThreadContext.put(Constants.UUID, UUID.randomUUID().toString());
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getUris : start");
            try {
                TempTO songs = spotifyService.getListOfLikedSongUris();
                response.resume(songs);
            } catch (BotException e) {
                response.resume(e.getError());
            } catch (Exception e) {
                ErrorResponseTO error = new ErrorResponseTO();
                error.setStatus(801);
                error.setMessage(e.getMessage());
                response.resume(error);
            }
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getUris : end");
        });
    }

     */

    @POST
    @Path("add-tracks-to-playlist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addTracksToPlaylist(@Suspended final AsyncResponse response, AddPlaylistTO playlist) {
        executor.execute(() -> {
            ThreadContext.put(Constants.UUID, UUID.randomUUID().toString());
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " addTracksToPlaylist : start");
            try {
                ValidateProperties.validatePlaylistForAddTracksToPlaylist(playlist);
                AddPlaylistTO resp = spotifyService.addSongsToPlaylist(playlist.getPlaylistId(), playlist.getPosition(), playlist.getUris());
                response.resume(resp);
            } catch (BotException e) {
                response.resume(e.getError());
            } catch (Exception e) {
                ErrorResponseTO error = new ErrorResponseTO();
                error.setStatus(801);
                error.setMessage(e.getMessage());
                response.resume(error);
            }
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " addTracksToPlaylist : end");
        });
    }

    @POST
    @Path("move-liked-songs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void moveLikedSongs(@Suspended final AsyncResponse response, AddPlaylistTO playlist) {
        executor.execute(() -> {
            ThreadContext.put(Constants.UUID, UUID.randomUUID().toString());
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " moveLikedSongs : start");
            try {
                ValidateProperties.validatePlaylistForMoveLikedSongs(playlist);
                AddPlaylistTO resp = spotifyService.moveAllLikedSongsToPlaylist(playlist.getPlaylistId());
                response.resume(resp);
            } catch (BotException e) {
                response.resume(e.getError());
            } catch (Exception e) {
                ErrorResponseTO error = new ErrorResponseTO();
                error.setStatus(801);
                error.setMessage(e.getMessage());
                response.resume(error);
            }
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " moveLikedSongs : end");
        });
    }

    @GET
    @Path("/tracks-from-playlist/{playlistId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getPlaylistSongs(@Suspended final AsyncResponse response,
                                @DefaultValue("") @QueryParam("market") String market,
                                @DefaultValue("20") @QueryParam("limit") Integer limit,
                                @DefaultValue("0") @QueryParam("offset") Integer offset,
                                 @PathParam("playlistId") String playlistId) {

        executor.execute(() -> {
            ThreadContext.put(Constants.UUID, UUID.randomUUID().toString());
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getPlaylistSongs : start");
            try {
                ValidateProperties.arePropertiesValidForGetPlaylistSongs(market, limit, playlistId);
                TracksTO tracksTO = spotifyService.getPlaylistSongs(playlistId, market, limit, offset, null);
                response.resume(tracksTO);
            } catch (BotException e) {
                response.resume(e.getError());
            } catch (Exception e) {
                ErrorResponseTO error = new ErrorResponseTO();
                error.setStatus(801);
                error.setMessage(e.getMessage());
                response.resume(error);
            }
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " getPlaylistSongs : end");
        });
    }

    @GET
    @Path("/sync-with-liked/{playlistId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void syncWithLiked(@Suspended final AsyncResponse response, @PathParam("playlistId") String playlistId) {

        executor.execute(() -> {
            ThreadContext.put(Constants.UUID, UUID.randomUUID().toString());
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " syncWithLiked : start");
            try {
                ValidateProperties.arePropertiesValidForSyncWithLiked(playlistId);
                AddPlaylistTO resp = spotifyService.syncPlaylistWithLikedSongs(playlistId);
                response.resume(resp);
            } catch (BotException e) {
                response.resume(e.getError());
            } catch (Exception e) {
                ErrorResponseTO error = new ErrorResponseTO();
                error.setStatus(801);
                error.setMessage(e.getMessage());
                response.resume(error);
            }
            logger.log(Level.INFO, () -> SPOTIFY_RESOURCE + " syncWithLiked : end");
        });
    }


}
