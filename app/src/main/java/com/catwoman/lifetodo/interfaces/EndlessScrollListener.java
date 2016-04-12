package com.catwoman.lifetodo.interfaces;

/**
 * Created by HUONGVU on 4/9/2016.
 */
public interface EndlessScrollListener {
    /**
     * Loads more data.
     * @param position
     * @return true loads data actually, false otherwise.
     */
    public boolean onLoadMore(int position);
}

