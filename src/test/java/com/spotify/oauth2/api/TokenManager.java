package com.spotify.oauth2.api;

import com.spotify.oauth2.utils.ConfigLoader;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.spotify.oauth2.api.Route.API;
import static com.spotify.oauth2.api.Route.TOKEN;

public class TokenManager {
    private static String access_token;
    private static Instant expiry_time;


    public synchronized static String getToken() {
        try {
            if (access_token == null || Instant.now().isAfter(expiry_time)) {
                System.out.println("Renewing the token....... ");
                Response response = renewToken();
                access_token = response.path("access_token");
                int tokenExpiresInSeconds = response.path("expires_in");
                expiry_time = Instant.now().plusSeconds(tokenExpiresInSeconds - 300);
            } else {
                System.out.println("Token is good to use.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Abort !! Token generation failed.");
        }
        return access_token;
    }

    private static Response renewToken() {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("client_id", ConfigLoader.getInstance().getClientId());
        formParams.put("client_secret", ConfigLoader.getInstance().getClientSecret());
        formParams.put("grant_type", ConfigLoader.getInstance().getGrantType());
        formParams.put("refresh_token", ConfigLoader.getInstance().getRefreshToken());

        Response response = RestApi.postAccount(API + TOKEN, formParams);

        if (response.statusCode() != 200) {
            throw new RuntimeException("Abort !! Token generation failed as status code not 200.");
        }

        return response;
    }
}
