package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.spotify.oauth2.api.StatusCode.*;
import static com.spotify.oauth2.utils.FakerUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Epic("Spotify OAuth 2.0")
@Feature("Playlist API")
public class PlaylistTests extends BaseTest {

    @Story("Create the Playlist")
    @Test
    public void shouldBeAbleToCreatePlaylist() {
        Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(), false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), CODE_201);
        assertThatPlaylist(response.as(Playlist.class), requestPlaylist);
    }

    @Story("Get the Playlist")
    @Link(name = "WebSite" , type = "my-link")
    @TmsLink("TMS-1234")
    @Issue("Issue-1234")
    @Description("This test case is failing due to public field assertion.")
    @Test(description = "The User should be able to Get the playlist.")
    public void shouldBeAbleToGetPlaylist() {
        Playlist requestPlaylist = playlistBuilder("Update Playlist", "Update playlist description", true);
        Response response = PlaylistApi.get(DataLoader.getInstance().getGetPlaylistId());
        assertStatusCode(response.statusCode(), CODE_200);
        assertThatPlaylist(response.as(Playlist.class), requestPlaylist);
    }

    @Story("Update the Playlist")
    @Test
    public void shouldBeAbleToUpdatePlaylist() {
        Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(), false);
        Response response = PlaylistApi.update(requestPlaylist, DataLoader.getInstance().getUpdatePlaylistId());
        assertStatusCode(response.statusCode(), CODE_200);
    }

    @Story("Create the Playlist")
    @Test
    public void shouldNotBeAbleToCreatePlaylistWithoutName() {
        Playlist requestPlaylist = playlistBuilder("", generateDescription(), false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), CODE_400);
        assertThatError(response.as(Error.class), CODE_400);
    }

    @Story("Create the Playlist")
    @Test
    public void shouldNotBeAbleToCreatePlaylistWithExpiredToken() {
        Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(), false);
        Response response = PlaylistApi.post(requestPlaylist, "invalidToken");
        assertStatusCode(response.statusCode(), CODE_401);
        assertThatError(response.as(Error.class), CODE_401);
    }

    @Step
    public Playlist playlistBuilder(String name, String description, boolean _public) {
        return Playlist.builder().
                name(name).
                description(description).
                _public(_public).build();
    }

    @Step()
    public void assertThatPlaylist(Playlist responsePlaylist, Playlist requestPlaylist) {
        assertThat(responsePlaylist.getName(), is(equalTo(requestPlaylist.getName())));
        assertThat(responsePlaylist.getDescription(), is(equalTo(requestPlaylist.getDescription())));
        assertThat(responsePlaylist.get_public(), is(equalTo(requestPlaylist.get_public())));
    }

    @Step
    public void assertStatusCode(int actualStatusCode, StatusCode expectedStatusCode) {
        assertThat(actualStatusCode, equalTo(expectedStatusCode.code));
    }

    @Step
    public void assertThatError(Error error, StatusCode expectedStatusCode) {
        assertThat(error.getInnerError().getStatus(), is(equalTo(expectedStatusCode.code)));
        assertThat(error.getInnerError().getMessage(), is(equalTo(expectedStatusCode.msg)));
    }

}