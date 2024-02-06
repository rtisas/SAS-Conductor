package com.rti.sas.service;

import android.util.Log;

import com.rti.sas.model.AccessToken;
import com.rti.sas.model.EventService;
import com.rti.sas.model.Request;
import com.rti.sas.model.ResponseServer;

import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConsumeService{

    private static final String InternetError="Por favor verifique su conexion a Internet";
    private static final String APIKEY = "AIzaSyA0zDt5akxbmHgdrfGhNf-UD0oTDxoempE";
    //private static final String APIKEY = "AIzaSyALGZDDFGasHl3SB0TUCH0h0iLRD2KGI7c";
    private static final String GOOGLE = "google";
    private static final String API_GAT_WAY= "api_gat_way";
    private static final String ERROR = "error";
    private static final String SERV = "server";
    private static final String Ok="OK";
    private ResponseServer responseServer;
    private EndPoint endPoint;
    private String result,actions,tokenRTI;

    public ConsumeService(){
        tokenRTI = "";
    }


    public void ConsumerService (final String action, final Object request){

        endPoint = GeneratorService.CreateService(EndPoint.class, API_GAT_WAY);
        Call<AccessToken> token = endPoint.oauth();

        token.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if(response.isSuccessful()){
                    tokenRTI = response.body().getAccess_token();
                    endPoint = GeneratorService.CreateService(EndPoint.class, SERV);
                    Call<ResponseServer> callservice = ServiceCall(action,tokenRTI,request);
                    Log.e("action", action);
                    callservice.enqueue(new Callback<ResponseServer>() {
                        @Override
                        public void onResponse(Call<ResponseServer> call, Response<ResponseServer> response) {
                            if(response.isSuccessful()){
                                actions = action;
                                result = Ok;
                                responseServer = response.body();
                                EventBus.getDefault().post(new EventService(responseServer,actions,result));
                            }else{
                                System.out.println("pasa la error 1 "+response.errorBody());
                                actions = ERROR;
                                result = InternetError;
                                EventBus.getDefault().post(new EventService(actions,result));
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseServer> call, Throwable t) {
                            actions = ERROR;
                            result = InternetError;
                            EventBus.getDefault().post(new EventService(actions,result));
                        }
                    });
                }else{
                    //actions = ERROR; //Problema conexion
                    actions = ERROR;
                    result = InternetError;
                    EventBus.getDefault().post(new EventService(actions,result));
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                System.out.println("Error en B" );

            }
        });
    }

    public void ConsumerGoogle (final String action, final String parameter1, final String parameter2){

        endPoint = GeneratorService.CreateService(EndPoint.class,GOOGLE);
        Call<ResponseServer> callgoogle;
        if(action.equals("distance")){callgoogle = endPoint.distance(parameter1,parameter2,APIKEY);}
        else{callgoogle = endPoint.geocode(parameter1,parameter2,APIKEY);}
        callgoogle.enqueue(new Callback<ResponseServer>() {
            @Override
            public void onResponse(Call<ResponseServer> call, Response<ResponseServer> response) {
                if(response.body().getStatus().equals(Ok)){
                    result = Ok;
                    actions = action;
                    responseServer = response.body();
                    EventBus.getDefault().post(new EventService(responseServer,actions,result));
                }else{
                    actions = ERROR;
                    result = InternetError;
                    EventBus.getDefault().post(new EventService(actions,result));
                }
            }
            @Override
            public void onFailure(Call<ResponseServer> call, Throwable t) {
                actions = ERROR;
                result = InternetError;
                EventBus.getDefault().post(new EventService(actions,result));
            }
        });
    }

    private Call<ResponseServer> ServiceCall (String action, String token, Object object){

        Request request = null;
        String message = null;

        if(object instanceof Request){
            request = (Request) object;
        }else{
            message = (String) object;
        }

        Call<ResponseServer> call = null;
        switch (action) {
            case "getcode":
                call = endPoint.getcode(message,token,"sas");
                break;
            case "getfavorite":
                call = endPoint.getfavorite(message,token);
                break;
            case "deletefavorite":
                call = endPoint.deletefavorite(message,token);
                break;
            case "getCards":
                call = endPoint.getCards(message,token);
                break;
            case "deletecard":
                call = endPoint.deletecard(message,token);
                break;
            case "updatepay":
                call = endPoint.updatepay(message,token);
                break;
            case "history":
                call = endPoint.history(message,token);
                break;
            case "schedule":
                call = endPoint.schedule(message,token);
                break;
            case "listpqrs":
                call = endPoint.listpqrs(message,token);
                break;
            case "login":
                call = endPoint.login(token, request);
                break;
            case "register":
                call = endPoint.register(token, request);
                break;
            case "newpassword":
                call = endPoint.newpassword(token, request);
                break;
            case "addfavorite":
                call = endPoint.addfavorite(token, request);
                break;
            case "vehicle":
                call = endPoint.vehicle(token,request);
                break;
            case "rate":
                call = endPoint.rate(token, request);
                break;
            case "rateh":
                call = endPoint.rate(token, request);
                break;
            case "models":
                call = endPoint.models(token, request);
                break;
            case "afes":
                call = endPoint.afes(message, token);
                break;
            case "qualification":
                call = endPoint.qualification(token, request);
                break;
            case "requestservice":
                call = endPoint.requestservice(token, request);
                break;
            case "cancel":
                call = endPoint.cancel(token, request);
                break;
            case "cancel1":
                call = endPoint.cancel(token, request);
                break;
            case "qualify":
                call = endPoint.qualify(token, request);
                break;
            case "sendmessage":
                call = endPoint.sendmessage(token, request);
                break;
            case "addcard":
                call = endPoint.addcard(token, request);
                break;
            case "pqrsSave":
                call = endPoint.pqrsSave(token, request);
                break;
            case "promo":
                call = endPoint.promo(token, request);
                break;
            case "state":
                call = endPoint.state(token, request);
                break;
            case "companyData":
                call = endPoint.companyData(token);
                break;
        }
        return call;

    }

}
