package org.sashabrava.shopapp.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sashabrava.shopapp.server.ServerRequest;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> mErrorText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mErrorText = new MutableLiveData<>();
        //mText.setValue("This is home fragment");
    }
    void getData(Context context){
        ServerRequest serverRequest = ServerRequest.getInstance(context);
        String shortUrl = "api/";
        try {
            serverRequest.templateRequest(this,
                    shortUrl,
                    ServerRequest.class.getMethod("getHomeText", String.class),
                    context,
                    this.getClass().getMethod("onSuccessfulItemsRequest", Object.class),
                    this.getClass().getMethod("onFailureItemsRequest", String.class)
            );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    public void onSuccessfulItemsRequest(Object object) {
        if (object instanceof String) {
            mText.setValue((String)object);
        }
    }

    public void onFailureItemsRequest(String errorText) {
        mErrorText.setValue(errorText);
    }
    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getErrorText() {
        return mErrorText;
    }


}