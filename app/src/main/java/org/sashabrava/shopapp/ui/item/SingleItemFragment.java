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
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.sashabrava.shopapp.R;
import org.sashabrava.shopapp.models.Item;
import org.sashabrava.shopapp.server.ItemsRequest;

import java.util.Locale;

public class SingleItemFragment extends Fragment {

    private SingleItemViewModel mViewModel;

    public static SingleItemFragment newInstance() {
        return new SingleItemFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.single_item_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SingleItemViewModel.class);
        checkBundle();
    }
    private boolean checkBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String bundleID = bundle.getString("id", "Value not specified");
            try {
                int itemID = Integer.parseInt(bundleID);
                Log.d("Bundle Single Item", String.format("Successfully received ID %d", itemID));
                return getData(itemID);
            }
            catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        String text = "You have opened Fragment without Item ID, therefore it can't be displayed";
        Log.d("Bundle Single Item", text);
            Snackbar.make(getView(), text, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        return false;
    }
    private boolean getData(int itemID){
        ItemsRequest itemsRequest = ItemsRequest.getInstance(getContext());
        String shortUrl = String.format(Locale.getDefault(),"api/items/%d", itemID);
        try {
            itemsRequest.templateRequest(this,
                    shortUrl,
                    ItemsRequest.class.getMethod("getItemJson", String.class),
                    getView(),
                    SingleItemFragment.class.getMethod("onItemReceived", View.class, Object.class),
                    SingleItemFragment.class.getMethod("onItemError", View.class, String.class)
            );
            return true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
       return false;
    }
    public void onItemReceived(View view, Object object){
        if (object instanceof Item){
            mViewModel.setItem((Item)object);
            ((TextView) view.findViewById(R.id.single_item_id)).setText(String.format(Locale.getDefault(), "%d", mViewModel.getItem().getId()));
            ((TextView) view.findViewById(R.id.single_item_title)).setText(mViewModel.getItem().getTitle());
            ((TextView) view.findViewById(R.id.single_item_description)).setText(mViewModel.getItem().getDescription());
            Snackbar.make(requireView(), "Single Item Successfully received", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        Log.d("Fragment Single Item", object.toString());

    }
    public void onItemError(View view, String errorText){
        Snackbar.make(view, errorText, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

}