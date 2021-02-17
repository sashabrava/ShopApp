package org.sashabrava.shopapp.ui.privacy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.sashabrava.shopapp.R;

public class PrivacyFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PrivacyViewModel privacyViewModel = new ViewModelProvider(this).get(PrivacyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_privacy, container, false);
        final TextView textView = root.findViewById(R.id.text_privacy);
        privacyViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
}