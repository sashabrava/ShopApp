package org.sashabrava.shopapp.ui.privacy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PrivacyViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PrivacyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is a Privacy fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}