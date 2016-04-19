package com.catwoman.lifetodo.interfaces;

/**
 * Created by HUONGVU on 4/19/2016.
 */
public interface EditItemListener {
    /**
     * Loads more data.
     * @param itemStatus
     */
    public void EditItem(int position, String itemName, String itemStatus);
}
