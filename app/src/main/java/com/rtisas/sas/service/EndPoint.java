package com.rtisas.sas.service;

import com.rtisas.sas.model.AccessToken;
import com.rtisas.sas.model.Request;
import com.rtisas.sas.model.ResponseServer;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EndPoint {


    String Ws = "WSSTTCFacade";
    String idbd = "sas";
    String token = "7077eb357c339e9f5e56cd5847f24cce";

    @GET("distancematrix/json")
    Call<ResponseServer> distance(@Query("origins") String llOrigen, @Query("destinations") String llDestino, @Query("key") String apikey);

    /* @POST(Ws+"/oauth/token?grant_type=password&client_id=root-client-id&client_secret=67890&username=root&password=123456&tenantID="+idbd)
    Call<AccessToken> accesstoken();*/

    @GET("/dev/users/")
    Call<AccessToken> oauth();

    @GET("geocode/json")
    Call<ResponseServer> geocode(@Query("latlng") String latLng, @Query("language") String lenguaje, @Query("key") String apikey);

    @POST("/v1/city/list")
    Call<AccessToken> accesstoken();

    @POST("/v1/service/CompanyData")
    Call<ResponseServer> companyData(@Query("access_token")String token);

    @POST("/v1/client/requestPasswordApp") ///requestPasswordApp TernadId también - Listo
    Call<ResponseServer> getcode(@Query("email") String email, @Query("access_token") String token, @Query("TenantID") String tenantId);

    @POST("/v1/client/getFavorites")
    Call<ResponseServer> getfavorite(@Query("email") String email, @Query("access_token") String token);

    @POST("/v1/client/getWallet")
    Call<ResponseServer> updatepay(@Query("email") String email, @Query("access_token") String token);

    @POST("/v1/card/getCards")
    Call<ResponseServer> getCards(@Query("email") String email, @Query("access_token") String token);

    @POST("/v1/service/listByClientApp")
    Call<ResponseServer> history(@Query("email") String email, @Query("access_token") String token);

    @POST("/v1/client/getReservations")
    Call<ResponseServer> schedule(@Query("email") String email, @Query("access_token") String token);

    @POST("/v1/pqrs/getByUser") // /pqrs/getByUser - Listo
    Call<ResponseServer> listpqrs(@Query("email") String email, @Query("access_token") String token);

    @POST("/v1/client/traerAfes")
    Call<ResponseServer> afes(@Query("email") String email, @Query("access_token") String token);

    @POST("/v1/client/deleteFavorite")
    Call<ResponseServer> deletefavorite(@Query("id") String id, @Query("access_token") String token);

    @POST("/v1/card/remove")
    Call<ResponseServer> deletecard(@Query("id") String id, @Query("access_token") String token);

    @POST("/v1/client/login")
    Call<ResponseServer> login(@Query("access_token") String access_token, @Body Request request);

    @POST("/v1/client/register")
    Call<ResponseServer> register(@Query("access_token") String access_token, @Body Request Request);

    @POST("/v1/client/changePassword")
    Call<ResponseServer> newpassword(@Query("access_token") String access_token, @Body Request Request);

    @POST("/v1/client/addFavorite")
    Call<ResponseServer> addfavorite(@Query("access_token") String token, @Body Request Request);

    @POST("/v1/service/getVehicles")
    Call<ResponseServer> vehicle(@Query("access_token") String token, @Body Request Request);

    @POST("/v1/service/calcularTarifa")
    Call<ResponseServer> rate(@Query("access_token") String token, @Body Request Request);

    @POST("/v1/service/traerModelos")
    Call<ResponseServer> models(@Query("access_token") String token, @Body Request Request);

    @POST("/v1/service/traerConductores")
    Call<ResponseServer> qualification(@Query("access_token") String token, @Body Request Request);

    @POST("/v1/service/solicitarACK")
    Call<ResponseServer> requestservice(@Query("access_token") String token, @Body Request Request);

    @POST("/v1/service/cancelByClient")
    Call<ResponseServer> cancel(@Query("access_token") String token, @Body Request Request);

    @POST("/v1/driver/calificar")
    Call<ResponseServer> qualify(@Query("access_token") String token, @Body Request Request);

    @POST("/v1/client/sendMessage")
    Call<ResponseServer> sendmessage(@Query("access_token") String token, @Body Request Request);

    @POST("/v1/card/register")
    Call<ResponseServer> addcard(@Query("access_token") String token, @Body Request Request);

    @POST("/v1/pqrs/save") //v1/pqrs/save - Listo
    Call<ResponseServer> pqrsSave(@Query("access_token") String token, @Body Request Request);

    @POST("/v1/code/redeem")
    Call<ResponseServer> promo(@Query("access_token")String token, @Body Request request);
//test
    @POST("/v1/service/checkServiceStatus")
    Call<ResponseServer> state(@Query("access_token")String token, @Body Request request);

}
