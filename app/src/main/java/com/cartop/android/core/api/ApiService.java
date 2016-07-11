package com.cartop.android.core.api;

import com.cartop.android.core.models.Program;
import com.cartop.android.core.models.ProgramsPage;
import com.cartop.android.core.models.Token;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {

    @POST(ApiFactory.API_PREFIX + "/oauth/token")
    @FormUrlEncoded
    Call<Token> getToken(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("username") String username,
            @Field("password") String password
    );

    @POST(ApiFactory.API_PREFIX + "/attach")
    @FormUrlEncoded
    Call<ResponseBody> attachToDriver(
            @Query("access_token") String accessToken,
            @Field("driver_id") String driverId,
            @Field("imei") String imei,
            @Field("phone") String phone
    );

    @POST(ApiFactory.API_PREFIX + "/device-information")
    @FormUrlEncoded
    Call<ResponseBody> sendDeviceInformation(
            @Query("access_token") String accessToken,
            @Field("system_time") long driverId,
            @Field("status") String status,
            @Field("imei") String imei,
            @Field("latitude") float latitude,
            @Field("longitude") float longitude,
            @Field("sim_signal") int simSignal,
            @Field("traffic") long traffic,
            @Field("last_boot") long lastBoot
    );

    @GET(ApiFactory.API_PREFIX + "/admin/programs")
    Call<ProgramsPage> getPrograms(
            @Query("access_token") String accessToken,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @GET(ApiFactory.API_PREFIX + "/program/{program_id}")
    Call<Program> getProgram(
            @Path("program_id") int programId,
            @Query("access_token") String accessToken
    );

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}
