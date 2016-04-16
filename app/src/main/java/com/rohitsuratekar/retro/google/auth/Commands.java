package com.rohitsuratekar.retro.google.auth;
import com.rohitsuratekar.retro.google.auth.response.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Commands{
    @FormUrlEncoded
    @POST("/oauth2/v4/token")
    Call<AccessToken> getAccessToken(@Field("client_id") String clientId,
                                     @Field("client_secret") String clientSecret,
                                     @Field("code") String code,
                                     @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("/oauth2/v4/token")
    Call<AccessToken> ExchangeRefreshToken(@Field("client_id") String clientId,
                                                    @Field("client_secret") String clientSecret,
                                                  @Field("refresh_token") String RefreshToken,
                                                  @Field("grant_type") String grantType);

}

