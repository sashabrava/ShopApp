package org.sashabrava.shopapp.ui.items;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;
import org.sashabrava.shopapp.MainActivity;
import org.sashabrava.shopapp.R;
import org.sashabrava.shopapp.models.Item;
import org.sashabrava.shopapp.server.ItemsRequest;
import org.sashabrava.shopapp.ui.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
static public int ITEMS_HEADER_ID = -1;
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

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            ItemsRequest itemsRequest = ItemsRequest.getInstance(view.getContext());
            try {
                itemsRequest.templateRequest(this,
                        "api/items",
                        ItemsRequest.class.getMethod("getItemsJson", String.class),
                        view,
                        ItemsFragment.class.getMethod("onSuccessfulItemsRequest", View.class, Object.class),
                        //MainActivity.class.getMethod("fabGreen", View.class, Object.class),
                        ItemsFragment.class.getMethod("onFailureItemsRequest", View.class, String.class)
                );
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    public void onSuccessfulItemsRequest(View view, Object object) {
        if (object instanceof Item[]) {
            Item[] items = (Item[]) object;
            RecyclerView recyclerView = (RecyclerView) view;
            ArrayList<Item> arrayListItem = new ArrayList<>(Arrays.asList(items));
            arrayListItem.add(0, new Item(ITEMS_HEADER_ID));
            recyclerView.setAdapter((new ItemRecyclerViewAdapter(arrayListItem)));
        }
    }

    public void onFailureItemsRequest(View view, String errorText) {
        Snackbar.make(view, errorText, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}