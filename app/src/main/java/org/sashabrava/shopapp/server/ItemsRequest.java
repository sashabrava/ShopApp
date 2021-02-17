package org.sashabrava.shopapp.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sashabrava.shopapp.models.Item;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

public class ItemsRequest {
    private static ItemsRequest instance;
    private RequestQueue requestQueue;
    static Integer STATUS_SUCCESS = 10;
    static Integer STATUS_UNEXPECTED_SERVER_REPLY = 12;
    static Integer STATUS_UNKNOWN = 14;
    Context context;

    public ItemsRequest(Context context) {
        this.context = context;
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

    public void templateRequest(@Nullable Object object, String shortUrl, Method jsonHandleFunction,  View view, @Nullable Method ifSuccessful, @Nullable Method ifFailure) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(view.getContext());
        boolean http = sharedPreferences.getBoolean("http", true);
        String serverPath = sharedPreferences.getString("server_path", "");
        String serverPort = sharedPreferences.getString("server_port", "8080");
        String fullUrl = String.format(
                "%s://%s:%s/%s", http ? "http" : "https",
                serverPath, serverPort, shortUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, fullUrl,
                response -> {
                    try {
                        //JSONObject jsonObject = new JSONObject(response);
                        Object resultObject = jsonHandleFunction.invoke(object, response);
                        Pair<Integer, Object> result = new Pair<>(STATUS_UNKNOWN, null);
                        if (resultObject instanceof Pair) {
                            Pair<?, ?> uncheckedPair = (Pair<?, ?>) resultObject;
                            Integer status = STATUS_UNKNOWN;
                            Object calculationsResultObject = null;
                            if (uncheckedPair.first.getClass() == Integer.class)
                                status = (Integer) uncheckedPair.first;
                            if (uncheckedPair.second != null) {
                                calculationsResultObject = uncheckedPair.second;
                            }
                            result = new Pair<>(status, calculationsResultObject);
                        }
                        if (result.first.equals(STATUS_SUCCESS)) {
                            Log.d("Response", response);
                            if (ifSuccessful != null)
                                ifSuccessful.invoke(object, view, result.second);
                        } else {
                            String text = String.format(Locale.getDefault(), "Couldn't process request %s, status code %d", fullUrl, result.first);
                            Log.d("Response", text);
                            if (ifFailure != null)
                                ifFailure.invoke(object, view, text);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            String text = "Request couldn't be resolved, ShopApp Server is down or can't process the request";
            Log.d("Response", text);
            Log.d("Error.Response", error.toString());
            if (ifFailure != null)
                try {
                    ifFailure.invoke(object, view, text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        });
        requestQueue.add(stringRequest);
    }

    static public Pair<Integer, Object> checkServerAlive(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.get("status").equals("Running")) {
                return new Pair<>(STATUS_SUCCESS, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Pair<>(STATUS_UNEXPECTED_SERVER_REPLY, null);
    }

    static public Pair<Integer, Object> getItemJson(String response) {
        try {
            Gson gson = new Gson();
            Item item = gson.fromJson(response, Item.class);
            return new Pair<>(STATUS_SUCCESS, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Pair<>(STATUS_UNEXPECTED_SERVER_REPLY, null);
    }
    static public Pair<Integer, Object> getItemsJson(String response) {
        try {
            Gson gson = new Gson();
            Item[] items = gson.fromJson(response, Item[].class);
            return new Pair<>(STATUS_SUCCESS, items);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Pair<>(STATUS_UNEXPECTED_SERVER_REPLY, null);
    }
}
