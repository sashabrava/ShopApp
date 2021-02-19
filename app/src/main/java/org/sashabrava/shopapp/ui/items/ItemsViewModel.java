package org.sashabrava.shopapp.ui.items;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import org.sashabrava.shopapp.models.Item;
import org.sashabrava.shopapp.server.ServerRequest;
import org.sashabrava.shopapp.ui.ServerResultListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemsViewModel extends ViewModel {
    static public int ITEMS_HEADER_ID = -1;
    private ArrayList<Item> items;
    private String errorText;
    private final ServerResultListener serverResultListener;

    public ItemsViewModel(ServerResultListener serverResultListener) {
        this.serverResultListener = serverResultListener;
    }


    public List<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    void getData(Context context) {
        ServerRequest serverRequest = ServerRequest.getInstance(context);
        try {
            serverRequest.templateRequest(this,
                    "api/items",
                    ServerRequest.class.getMethod("getItemsJson", String.class),
                    context,
                    this.getClass().getMethod("onSuccessfulItemsRequest", Object.class),
                    this.getClass().getMethod("onFailureItemsRequest", String.class)
            );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void onSuccessfulItemsRequest(Object object) {
        if (object instanceof Item[]) {
            Item[] items = (Item[]) object;
            ArrayList<Item> arrayListItem = new ArrayList<>(Arrays.asList(items));
            arrayListItem.add(0, new Item(ITEMS_HEADER_ID));
            setItems(arrayListItem);

            serverResultListener.onSuccess();
        }
    }

    public void onFailureItemsRequest(String errorText) {
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
