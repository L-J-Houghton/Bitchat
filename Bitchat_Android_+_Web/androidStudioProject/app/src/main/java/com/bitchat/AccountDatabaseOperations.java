package com.bitchat;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


//AccountDatabaseOperations

//Containing the request functions used by the application to communicate with the database while logged out.
//NOTE: attemptLogin functionality should eventually be moved to here from the LoginActivity

public class AccountDatabaseOperations {

    //the url of the php script containing the database querying functions. (seperate to the other one)
    private final String url = "http://doc.gold.ac.uk/~rcorb001/bitChat/db_operations_account2.0.php";

    //used to register an account using the email and password arguements
    public void registerAccount(final String email, final String password,final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("log", response);
                        if (response != "failed") {
                            Toast.makeText(context, "A confirmation email has been sent to " + email,
                                    Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(context, "Registration was unsuccessful, please try again later",
                                    Toast.LENGTH_LONG).show();


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("log", "ERROR-CONNECTING");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("REQUEST_TYPE", "REGISTER");
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };


        queue.add(stringRequest);


    }

}
