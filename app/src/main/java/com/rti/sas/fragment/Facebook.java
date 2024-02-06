package com.rti.sas.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.rti.sas.R;
import org.json.JSONObject;

public class Facebook extends Fragment {

    private CallbackManager callbackManager;
    private EventsFragments eventsFragments;
    private LoginButton facebook;

    public Facebook() {
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_facebook, container, false);
        facebook = (LoginButton) view.findViewById(R.id.facebook);
        facebook.setReadPermissions("email", "user_friends", "public_profile");
        facebook.setFragment(this);
        facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        eventsFragments.FragmentInteraction(object.optString("email"),object.optString("first_name"),object.optString("last_name"));
                    }
                });
                Bundle bundle = new Bundle();
                bundle.putString("fields", "first_name,last_name,email");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.e("facebook","error");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("facebook","error");
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventsFragments) {
            eventsFragments = (EventsFragments) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public interface EventsFragments {
        void FragmentInteraction(String email, String name, String apellido);
    }
}
