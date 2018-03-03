package com.bitchat;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


//Class DatabaseOperations contains all the request functions used by the application to communicate with the
//database while logged in.

public class DatabaseOperations {

    //the url of the php script containing the database querying functions.
    private final String url = "http://doc.gold.ac.uk/~rcorb001/bitChat/db_operations2.0.php";


    //the context that the functions are being called from is always passed into one of the functions
    //below as an argument as it is required by MySingleton to add the request to the request queue.


    //used to pull all the messages within a given forum to be displayed in the chat list.
    //The String chosenForum is used as a post parameter to indicate which forum the messages should
    //be pulled from.
    public void pullChatList(final String chosenForum, final Context context) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            MainActivity.chatArray = new JSONArray(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("REQUEST_TYPE", "PULL_CHAT");
                params.put("area", chosenForum);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }

    //used to post a message to a given forum.
    //The String chosenForum is again used as a post parameter to indicate to the php script which
    //forum to insert the message into. The String text contains the message text to be posted
    // and the username contains the username.
    public void postMessage(final String chosenForum, final String text, final String username, final Context context) {

        Log.d("details", text + " " + username + " " + chosenForum);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response", response);

                        pullChatList(chosenForum, context);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("REQUEST_TYPE", "POST_MESSAGE");
                params.put("message", text);
                params.put("username", username);
                params.put("area", chosenForum);

                return params;
            }
        };


        MySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }

    //used to pull the list of area forums that are available and update the value of forumArray which
    //is the array the array adapter uses in the ForumListFragment for the ListView.
    public void pullForums(final Context context) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            MainActivity.forumArray = (new JSONArray(response));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("REQUEST_TYPE", "PULL_FORUM");

                return params;
            }
        };


        MySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }

    //Similar to the above function. Used to pull the list of user forums and update the value of
    // forumArray used by the FriendsFragment
    public void pullUsers(Context context) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            MainActivity.forumArray = (new JSONArray(response));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("REQUEST_TYPE", "PULL_FRIENDS");

                return params;
            }
        };


        MySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }

    //This function takes a new username and the older username as strings and sends them as POST parameters for the
    //PHP Script to locate the user and update the value of the username to the new username.
    public void updateUsername(final String newUsername, final String oldUsername, final Context context) {

        final String trimmedNewUsername = newUsername.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        MainActivity.username = newUsername;
                        Log.d("response", response);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response", String.valueOf(error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("REQUEST_TYPE", "UPDATE_USERNAME");
                params.put("new_username", trimmedNewUsername);
                params.put("old_username", oldUsername);
                return params;
            }
        };


        MySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }


}
