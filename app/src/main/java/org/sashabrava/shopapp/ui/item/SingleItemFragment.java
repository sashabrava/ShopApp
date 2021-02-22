package org.sashabrava.shopapp.ui.item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import org.sashabrava.shopapp.R;
import org.sashabrava.shopapp.ui.ServerResultListener;

import java.util.Locale;

import timber.log.Timber;

public class SingleItemFragment extends Fragment {

    private SingleItemViewModel mViewModel;
    private ServerResultListener serverResultListener;

    /*public static SingleItemFragment newInstance() {
        return new SingleItemFragment();
    }*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.single_item_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        serverResultListener = new ServerResultListener() {
            @Override
            public void onSuccess() {
                onItemReceived();
            }

            @Override
            public void onError() {
                onItemError();
            }
        };
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SingleItemViewModel(serverResultListener);
            }
        };
        mViewModel = new ViewModelProvider(this, factory).get(SingleItemViewModel.class);
        checkBundle();
    }

    private void checkBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int bundleID = bundle.getInt("id", -1);
            if (bundleID > -1) {
                Timber.d("Successfully received ID %d", bundleID);
                mViewModel.getData(getContext(), bundleID);
                return;
            }
        }
        String text = "You have opened Fragment without Item ID, therefore it can't be displayed";
        Timber.d(text);
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    public void onItemReceived(){
            ((TextView) requireView().findViewById(R.id.single_item_id)).setText(String.format(Locale.getDefault(), "%d", mViewModel.getItem().getId()));
            ((TextView) requireView().findViewById(R.id.single_item_title)).setText(mViewModel.getItem().getTitle());
            ((TextView) requireView().findViewById(R.id.single_item_description)).setText(mViewModel.getItem().getDescription());
            Snackbar.make(requireView(), "Single Item Successfully received", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();


    }
    public void onItemError(){
        Snackbar.make(requireView(), mViewModel.getErrorText(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

}