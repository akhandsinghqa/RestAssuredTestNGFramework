package com.spotify.oauth2.api.applicationApi;

import com.spotify.oauth2.api.RestApi;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.ConfigLoader;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.spotify.oauth2.api.Route.*;
import static com.spotify.oauth2.api.TokenManager.getToken;

public class PlaylistApi {

    @Step
    public static Response post(Playlist requestPlaylist) {
        return RestApi.post(getToken(), USERS + "/" + ConfigLoader.getInstance().getUserId() + PLAYLISTS, requestPlaylist);
    }

    @Step
    public static Response post(Playlist requestPlaylist, String token) {
        return RestApi.post(token, USERS + "/" + ConfigLoader.getInstance().getUserId() + PLAYLISTS, requestPlaylist);
    }

    @Step
    public static Response get(String playlistId) {
        return RestApi.get(getToken(), PLAYLISTS + "/" + playlistId);
    }

    @Step
    public static Response update(Playlist requestPlaylist, String playlistId) {
        return RestApi.update(getToken(), PLAYLISTS + "/" + playlistId, requestPlaylist);
    }
}
