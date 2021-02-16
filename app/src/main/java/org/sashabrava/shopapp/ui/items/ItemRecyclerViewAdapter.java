package org.sashabrava.shopapp.ui.items;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;
import org.sashabrava.shopapp.R;

import org.sashabrava.shopapp.models.Item;
import org.sashabrava.shopapp.server.ItemsRequest;
import org.sashabrava.shopapp.ui.dummy.DummyContent.DummyItem;
import org.sashabrava.shopapp.ui.item.SingleItemFragment;

import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    /* private final List<DummyItem> mValues;

     public ItemRecyclerViewAdapter(List<DummyItem> items) {
         mValues = items;
     }*/
    private final List<Item> mValues;

    public ItemRecyclerViewAdapter(List<Item> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        if (holder.mItem.getId() != ItemsFragment.ITEMS_HEADER_ID) {
            holder.mContentView.setText(mValues.get(position).getTitle());
            holder.mIdView.setText(String.format(Locale.getDefault(), "%d", mValues.get(position).getId()));

            holder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putInt("id", mValues.get(position).getId());
                NavController navController = Navigation.findNavController((AppCompatActivity) v.getContext(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_single_item, bundle);
            });
        } else {
            holder.mIdView.setText("Item id");
            holder.mContentView.setText("Item title");
            holder.mIdView.setTextColor(Color.GRAY);
            holder.mContentView.setTextColor(Color.GRAY);
        }

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        //public DummyItem mItem;
        public Item mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}