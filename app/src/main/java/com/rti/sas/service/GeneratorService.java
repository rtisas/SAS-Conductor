package com.rti.sas.service;

import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeneratorService {

    //rti  = https://www.rtisasdev.com.co
    //Miguel = http://192.168.1.210:8080
    //produccion sas = https://www.solas.com.co
    //Pedro = http://192.168.1.211:8080
    //Domingo = http://192.168.1.155:8080
    //Domingocasa = http://192.168.1.51:8080

            //URL del servidor
    public static final String URL_SERVER = "http://18.204.218.84:8082";
    public static final String URL_GOOGLE = "https://maps.googleapis.com/maps/api/";
    public static final String URL_API_GAT_WAY = "https://ltsq6apf94.execute-api.us-east-1.amazonaws.com";

            //construccion del endpoing con retrofit
    private static Retrofit.Builder server = new Retrofit.Builder().baseUrl(String.valueOf(HttpUrl.parse(URL_SERVER))).addConverterFactory(GsonConverterFactory.create());
    private static Retrofit.Builder google = new Retrofit.Builder().baseUrl(String.valueOf(HttpUrl.parse(URL_GOOGLE))).addConverterFactory(GsonConverterFactory.create());
    private static Retrofit.Builder amazonGatWay = new Retrofit.Builder().baseUrl(String.valueOf(HttpUrl.parse(URL_API_GAT_WAY))).addConverterFactory(GsonConverterFactory.create());

            //caracteristicas de la conexion
    private static OkHttpClient.Builder client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);

            //creador de la conexion con el servidor
    public static <S> S CreateService (Class<S> serviceClass, String ruta){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //logging.setLevel(HttpLoggingInterceptor.Level.BODY);//imprime los log en consola
        client.addInterceptor(logging);
        Retrofit retrofit;
        if(ruta.equals("server")){
            server.client(client.build());
            retrofit = server.build();
        }
        else if(ruta.equals("api_gat_way")){
            amazonGatWay.client(client.build());
            retrofit = amazonGatWay.build();
        }
        else{
            google.client(client.build());
            retrofit = google.build();
        }
        return retrofit.create(serviceClass);
    }
}
