package com.antonioleiva.mvpexample.app.Login;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.antonioleiva.mvpexample.app.main.VolleyClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class LoginInteractorImpl implements LoginInteractor {

    ArrayList datos = new ArrayList();

    @Override
    public void login(final String username, final String password, final OnLoginFinishedListener listener, final Context context) {
        boolean error = false;
        if (TextUtils.isEmpty(username)){
            listener.onUsernameError();
            error = true;
            return;
        }
        if (TextUtils.isEmpty(password)){
            listener.onPasswordError();
            error = true;
            return;
        }
        if (!error){

            String url = "https://api.myjson.com/bins/zuj5h";
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            String userFirstName = response.getJSONObject(i).getString("First Name");
                            String userLastName = response.getJSONObject(i).getString("Last Name");
                            String userID = response.getJSONObject(i).getString("ID");

                            /*
                            * popular List/Arraylist, mandarlo a listener.onSuccess();
                            * */
                            //datos.add(userFirstName, userLastName, userID);
                            //listener.onSuccess();
                        } catch (JSONException ex) {
                            System.out.println(ex);
                        }
                    }
                    /*adapter.notifyDataSetChanged();
                    hideProgressBar();*/
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String hola = error.toString();
                    // TODO Auto-generated method stub
                }
            });
            // Access the RequestQueue through your singleton class.
            VolleyClient.getInstance(context).addToRequestQueue(jsArrayRequest);
        }
    }
}