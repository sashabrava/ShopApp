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

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;
import org.sashabrava.shopapp.MainActivity;
import org.sashabrava.shopapp.R;
import org.sashabrava.shopapp.server.ItemsRequest;

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
        // TODO: Use the ViewModel
        checkBundle();
    }
    private boolean checkBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String bundleID = bundle.getString("id", "Value not specified");
            try {int itemID = Integer.parseInt(bundleID);

                return getData(itemID);
            }
            catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
            Snackbar.make(getView(), "You have opened Fragment without Item ID, therefore it can't be displayed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        return false;
    }
    private boolean getData(int itemID){
        ItemsRequest itemsRequest = ItemsRequest.getInstance(getContext());
        /*try {
            itemsRequest.templateRequest(this,
                    "api/check-alive",
                    ItemsRequest.class.getMethod("checkServerAlive", JSONObject.class),
                    getView(),
                    MainActivity.class.getMethod("fabGreen", View.class),
                    MainActivity.class.getMethod("fabRed", View.class)
            );
            return true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }*/
       return false;
    }

}