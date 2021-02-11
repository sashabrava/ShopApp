package org.sashabrava.shopapp.ui.item;

import androidx.lifecycle.ViewModel;

import org.sashabrava.shopapp.models.Item;

public class SingleItemViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}