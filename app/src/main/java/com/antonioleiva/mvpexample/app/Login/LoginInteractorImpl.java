package com.antonioleiva.mvpexample.app.Login;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.antonioleiva.mvpexample.app.main.VolleyClient;

import org.json.JSONArray;
import org.json.JSONException;

public class LoginInteractorImpl implements LoginInteractor {

    @Override
    public void login(final String username, final String password, final OnLoginFinishedListener listener, final Context context) {
        // Mock login. I'm creating a handler to delay the answer a couple of seconds
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
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
                    //listener.onSuccess();
                    String url = "https://jsonplaceholder.typicode.com/posts";
                    JsonArrayRequest jsArrayRequest = new JsonArrayRequest (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    String userName = response.getJSONObject(i).getString("userName");
                                    String userFirstName = response.getJSONObject(i).getString("userFirstName");
                                    String userLastName = response.getJSONObject(i).getString("userLastName");

                                    //datos.add(new UserDetail(userName, userFirstName, userLastName));

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
        }, 2000);
    }
}