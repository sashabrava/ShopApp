package org.sashabrava.shopapp.ui.item;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sashabrava.shopapp.R;

public class SingleItemFragment extends Fragment {

    private SingleItemViewModel mViewModel;

    public static SingleItemFragment newInstance() {
        return new SingleItemFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Log.d("bundle.single.item", bundle.getString("id", "Value is unknown"));
         }
        return inflater.inflate(R.layout.single_item_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SingleItemViewModel.class);
        // TODO: Use the ViewModel
    }

}