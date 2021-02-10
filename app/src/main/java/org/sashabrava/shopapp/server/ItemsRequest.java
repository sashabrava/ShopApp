package org.sashabrava.shopapp.server;

import android.content.Context;
import android.util.Log;
import android.view.View;

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

public class ItemsRequest {
    View view;
    public ItemsRequest(View view){
        this.view = view;
    }
 public boolean request(){
     RequestQueue queue = Volley.newRequestQueue(view.getContext());
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
 }
public boolean singleItem(){
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
}
}
