package com.bot.spotifyapp.util;

import com.bot.spotifyapp.exception.BotException;
import com.bot.spotifyapp.to.AddPlaylistTO;
import com.bot.spotifyapp.type.BotExceptionType;

public class ValidateProperties {

    private ValidateProperties() {
        super();
    }

    public static void arePropertiesValidForGetUserAccessToken(String code, String state, String error) throws BotException {
        if (code == null || code.isEmpty()) {
            if (error == null || error.isEmpty()) {
                throw new BotException("Code and Error are both null or empty!", BotExceptionType.ERROR);
            }
            throw new BotException(error, BotExceptionType.ERROR);
        }
        if (state == null || state.isEmpty()) {
            throw new BotException("State is null or empty!", BotExceptionType.ERROR);
        }
        String createdState = CacheMap.getInstance().getFromCache(state);
        if (createdState == null) {
            throw new BotException("The state provided does not exist!", BotExceptionType.ERROR);
        }
        CacheMap.getInstance().removeFromCache(state);
    }

    public static void arePropertiesValidForGetMySavedSongs(String market, Integer limit) throws BotException {
        if (market != null && !market.trim().isEmpty()) {
            if (market.length() != 2) {
                throw new BotException("The value provided for market is invalid! Should be only 2 characters.", BotExceptionType.ERROR);
            }
        }
        if (limit != null) {
            if (limit == 0 || limit > 50) {
                throw new BotException("The value provided for limit is invalid! Should be between 1 and 50.");
            }
        }
    }

    public static void validatePlaylistForAddTracksToPlaylist(AddPlaylistTO playlist) throws BotException {
        if (playlist.getPlaylistId() == null || playlist.getPlaylistId().isEmpty()) {
            throw new BotException("No playlistId provided in the request!", BotExceptionType.ERROR);
        }
        if (playlist.getUris() == null || playlist.getUris().isEmpty()) {
            throw new BotException("No tracks provided to add to playlist!", BotExceptionType.ERROR);
        }
    }

    public static void validatePlaylistForMoveLikedSongs(AddPlaylistTO playlist) throws BotException {
        if (playlist.getPlaylistId() == null || playlist.getPlaylistId().isEmpty()) {
            throw new BotException("No playlistId provided in the request!", BotExceptionType.ERROR);
        }
    }

    public static void arePropertiesValidForGetPlaylistSongs(String market, Integer limit, String playlistId) throws BotException {
        if (playlistId == null || playlistId.length() != 22) {
            throw new BotException("No playlist id provided!", BotExceptionType.ERROR);
        }
        if (market != null && !market.trim().isEmpty()) {
            if (market.length() != 2) {
                throw new BotException("The value provided for market is invalid! Should be only 2 characters.", BotExceptionType.ERROR);
            }
        }
        if (limit != null) {
            if (limit == 0 || limit > 50) {
                throw new BotException("The value provided for limit is invalid! Should be between 1 and 50.");
            }
        }
    }

    public static void arePropertiesValidForSyncWithLiked(String playlistId) throws BotException {
        if (playlistId == null || playlistId.length() != 22) {
            throw new BotException("No playlist id provided!", BotExceptionType.ERROR);
        }
    }
}
