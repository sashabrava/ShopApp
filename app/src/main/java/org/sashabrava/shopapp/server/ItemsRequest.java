package org.sashabrava.shopapp.server;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.sashabrava.shopapp.models.Item;

import java.lang.reflect.Method;
import java.util.function.Function;

public class ItemsRequest {
    private static Context context;
    private static ItemsRequest instance;
    private RequestQueue requestQueue;
    private Boolean serverOnline;

    public ItemsRequest(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
        serverOnline = null;
    }

    public static synchronized ItemsRequest getInstance(Context context) {
        if (instance == null) {
            instance = new ItemsRequest(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }


    /*public boolean request(){
     //RequestQueue queue = Volley.newRequestQueue(view.getContext());
     String url ="http://192.168.0.102:8080/api";

// Request a string response from the provided URL.
     StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
             new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {
                     // Display the first 500 characters of the response string.
                     try {
                         JSONObject jsonObject = new JSONObject(response);
                         String reply = jsonObject.getString("text");
                         Log.d("Response", reply);
                         Snackbar.make(view,reply, Snackbar.LENGTH_LONG)
                                 .setAction("Action", null).show();
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }

                     //textView.setText("Response is: "+ response.substring(0,500));
                 }
             }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {
             Log.d("Response", "That didn't work!");
         }
     });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
     return true;
 }*/

    public Boolean templateRequest(@Nullable Object object, String shortUrl,Method jsonHandleFunction){
        return templateRequest(object, shortUrl, jsonHandleFunction, null, null, null);
    }
    public Boolean templateRequest(@Nullable Object object, String shortUrl, Method jsonHandleFunction, @Nullable View view, @Nullable Method ifSuccessful, @Nullable Method ifFailure) {
        String fullUrl = String.format("http://192.168.0.102:8080/%s", shortUrl);
        final Boolean[] result = {null};
        StringRequest stringRequest = new StringRequest(Request.Method.GET, fullUrl,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        result[0] = (Boolean) jsonHandleFunction.invoke(object, jsonObject);
                        if (result[0]){
                            serverOnline = true;
                            Log.d("Response", jsonObject.toString());
                            if (ifSuccessful != null )
                                ifSuccessful.invoke(object, view);


                        }
                        /* Snackbar.make(view, jsonObject.toString(), Snackbar.LENGTH_LONG)
                                 .setAction("Action", null).show();*/

                        else {
                            String text = String.format("JSONObject has invalid format for request %s", fullUrl);
                         /*Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                                 .setAction("Action", null).show();*/
                            Log.d("Response", text);
                            if (ifFailure != null )
                                ifFailure.invoke(object, view);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            serverOnline = false;
            Log.d("Response", "That didn't work!");
            if (ifFailure != null )
               try {
                   ifFailure.invoke(object, view);
               }
            catch (Exception e){
                   e.printStackTrace();
            }
        });
        requestQueue.add(stringRequest);
        return result[0];
    }

    static public boolean checkServerAlive(JSONObject jsonObject) {
        try {
            if (jsonObject.get("status").equals("Running")) {
                return true;
            }
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean getServerOnline() {
        return serverOnline;
    }

/*public boolean singleItem(){
    RequestQueue queue = Volley.newRequestQueue(view.getContext());
    String url ="http://192.168.0.102:8080/api/items/1";
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    try {
                        Gson gson = new Gson();
                        Item item = gson.fromJson(response, Item.class);
                        //JSONObject jsonObject = new JSONObject(response);
                        //String reply = jsonObject.getString("text");
                        //Log.d("Response", reply);
                        Snackbar.make(view, item.toString(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //textView.setText("Response is: "+ response.substring(0,500));
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("Response", "That didn't work!");
            Snackbar.make(view, "Can't get a response from server", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    });
    queue.add(stringRequest);
    return true;
}*/
}
