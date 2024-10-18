package com.bot.spotifyapp.service.intf;

import com.bot.spotifyapp.to.AccessTokenTO;
import com.bot.spotifyapp.to.AddPlaylistTO;
import com.bot.spotifyapp.to.ErrorResponseTO;
import com.bot.spotifyapp.to.TracksTO;

import java.net.URI;
import java.util.List;

public interface SpotifyServiceIntf {

    public AccessTokenTO getServerToServerAccessToken();

    public URI authorizeApp();

    public AccessTokenTO getUserAccessToken(String code);

    public TracksTO getMyLikedSongs(String market, Integer limit, Integer offset);

    public AccessTokenTO getUserRefreshToken();

    // public AccessTokenTO getRefreshTokenForScheduler();

    public AddPlaylistTO moveAllLikedSongsToPlaylist(String playlistId);

    public AddPlaylistTO addSongsToPlaylist(String playlistId, Integer position, List<String> trackUris);

    public AddPlaylistTO syncPlaylistWithLikedSongs(String playlistId);

    public void keepPlaylistSynced();

    public TracksTO getPlaylistSongs(String playlistId, String market, Integer limit, Integer offset, String field);
}
