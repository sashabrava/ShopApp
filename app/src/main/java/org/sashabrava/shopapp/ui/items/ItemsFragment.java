package org.sashabrava.shopapp.ui.items;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.sashabrava.shopapp.R;
import org.sashabrava.shopapp.ui.ServerResultListener;

/**
 * A fragment representing a list of Items.
 */
public class ItemsFragment extends Fragment {
    private ServerResultListener serverResultListener;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ItemsViewModel mViewModel;
    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemsFragment newInstance(int columnCount) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
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
                return (T) new ItemsViewModel(serverResultListener);
            }
        };
        mViewModel = new ViewModelProvider(this, factory).get(ItemsViewModel.class);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mViewModel.getData(context);
        }
        return view;
    }

    public void onItemReceived() {
        recyclerView.setAdapter((new ItemRecyclerViewAdapter(mViewModel.getItems())));
    }

    public void onItemError() {
        Snackbar.make(requireView(), mViewModel.getErrorText(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}