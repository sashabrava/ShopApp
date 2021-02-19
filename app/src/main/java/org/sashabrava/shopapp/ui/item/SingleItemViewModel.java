package org.sashabrava.shopapp.ui.item;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import org.sashabrava.shopapp.models.Item;
import org.sashabrava.shopapp.server.ServerRequest;
import org.sashabrava.shopapp.ui.ServerResultListener;

import java.util.Locale;

public class SingleItemViewModel extends ViewModel {
    private Item item;
    private String errorText;
    private final ServerResultListener serverResultListener;

    public SingleItemViewModel(ServerResultListener serverResultListener) {
        this.serverResultListener = serverResultListener;
    }


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    void getData(Context context, int itemID){
        ServerRequest serverRequest = ServerRequest.getInstance(context);
        String shortUrl = String.format(Locale.getDefault(),"api/items/%d", itemID);
        try {
            serverRequest.templateRequest(this,
                    shortUrl,
                    ServerRequest.class.getMethod("getItemJson", String.class),
                    context,
                    this.getClass().getMethod("onItemReceived", Object.class),
                    this.getClass().getMethod("onItemError", String.class)
            );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    public void onItemReceived(Object object){
        if (object instanceof Item){
            setItem((Item)object);
            serverResultListener.onSuccess();
        }
        Log.d("Fragment Single Item", object.toString());
    }
    public void onItemError(String errorText){
        setErrorText(errorText);
        serverResultListener.onError();
    }
    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}