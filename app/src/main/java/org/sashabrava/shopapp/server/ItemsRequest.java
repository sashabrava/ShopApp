package org.sashabrava.shopapp.server;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.sashabrava.shopapp.models.Item;

import java.lang.reflect.Method;
import java.util.Locale;

public class ItemsRequest {
    private static ItemsRequest instance;
    private RequestQueue requestQueue;
    static Integer STATUS_SUCCESS = 10;
    static Integer STRING_REQUEST_ERROR=11;
    static Integer STATUS_UNEXPECTED_SERVER_REPLY = 12;
    static Integer STATUS_UNKNOWN_ERROR = 13;
    static Integer STATUS_UNKNOWN = 14;

    public ItemsRequest(Context context) {
        //this.context = context;
        requestQueue = getRequestQueue(context);
    }

    public static synchronized ItemsRequest getInstance(Context context) {
        if (instance == null) {
            instance = new ItemsRequest(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(Context context) {
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

    /* public Boolean templateRequest(@Nullable Object object, String shortUrl,Method jsonHandleFunction){
         return templateRequest(object, shortUrl, jsonHandleFunction, null, null, null);
     }*/
    public void templateRequest(@Nullable Object object, String shortUrl, Method jsonHandleFunction, @Nullable View view, @Nullable Method ifSuccessful, @Nullable Method ifFailure) {
        String fullUrl = String.format("http://192.168.0.102:8080/%s", shortUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, fullUrl,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Object resultObject = jsonHandleFunction.invoke(object, jsonObject);
                        Pair<Integer, Object> result = new Pair<>(STATUS_UNKNOWN, null);
                        if (resultObject instanceof Pair) {
                            Pair<?, ?> uncheckedPair = (Pair<?, ?>) resultObject;
                            Integer status = STATUS_UNKNOWN;
                            Object calculationsResultObject = null;
                            if (uncheckedPair.first.getClass() == Integer.class)
                                status = (Integer) uncheckedPair.first;
                            if (uncheckedPair.second != null){
                                calculationsResultObject = uncheckedPair.second;
                            }
                            result = new Pair<>(status, calculationsResultObject);
                        }
                        if (result.first.equals(STATUS_SUCCESS)) {
                            Log.d("Response", response);
                            if (ifSuccessful != null)
                                ifSuccessful.invoke(object, view, result.second);
                        }
                        /* Snackbar.make(view, jsonObject.toString(), Snackbar.LENGTH_LONG)
                                 .setAction("Action", null).show();*/
                        else {
                            String text = String.format(Locale.getDefault(), "Couldn't process request %s, status code %d", fullUrl, result.first);
                         /*Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                                 .setAction("Action", null).show();*/
                            Log.d("Response", text);
                            if (ifFailure != null)
                                ifFailure.invoke(object, view, text);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            String text = String.format(Locale.getDefault(),"Request couldn't be resolved, Seems that ShopApp Server is down");
            Log.d("Response", text);
            if (ifFailure != null)
                try {
                    ifFailure.invoke(object, view, text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        });
        requestQueue.add(stringRequest);
    }

    static public Pair<Integer, Object> checkServerAlive(JSONObject jsonObject) {
        try {
            if (jsonObject.get("status").equals("Running")) {
                return new Pair<>(STATUS_SUCCESS, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Pair<>(STATUS_UNEXPECTED_SERVER_REPLY, null);
    }

    static public Pair<Integer, Object> checkItemJson(JSONObject jsonObject) {
        try {
            Gson gson = new Gson();
            Item item = gson.fromJson(jsonObject.toString(), Item.class);
            return new Pair<>(STATUS_SUCCESS, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Pair<>(STATUS_UNEXPECTED_SERVER_REPLY, null);
    }

    /*public Boolean getServerOnline(JSONObject jsonObject) {
        return serverOnline;
    }*/

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
