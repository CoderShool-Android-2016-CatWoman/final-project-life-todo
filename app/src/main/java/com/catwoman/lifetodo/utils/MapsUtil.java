package com.catwoman.lifetodo.utils;

/**
 * Created by annt on 4/24/16.
 */
public class MapsUtil {
    public static String getStaticMapUrl(String center, int z, int w, int h, String apiKey) {
        return "https://maps.googleapis.com/maps/api/staticmap"
                + "?center=" + center
                + "&zoom=" + z
                + "&size=" + w + "x" + h
                + "&scale=2"
                + "&key=" + apiKey;
    }
}
