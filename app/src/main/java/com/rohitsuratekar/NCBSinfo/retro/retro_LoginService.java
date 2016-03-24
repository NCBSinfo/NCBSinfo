package com.rohitsuratekar.NCBSinfo.retro;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Dexter on 3/7/2016.
 */
public interface retro_LoginService {
    @FormUrlEncoded
    @POST("/oauth2/v4/token")
    Call<retro_Response_AccessToken> getAccessToken(@Field("client_id") String clientId,
                                     @Field("client_secret") String clientSecret,
                                     @Field("code") String code,
                                     @Field("grant_type") String grantType,
                                     @Field("approval_prompt") String prompt,
                                     @Field("redirect_uri") String redirecturi);

    @FormUrlEncoded
    @POST("/oauth2/v4/token")
    Call<retro_Response_AccessToken> ExchangeRefreshToken(@Field("client_id") String clientId,
                                                    @Field("client_secret") String clientSecret,
                                                  @Field("refresh_token") String RefreshToken,
                                                  @Field("grant_type") String grantType);

}

