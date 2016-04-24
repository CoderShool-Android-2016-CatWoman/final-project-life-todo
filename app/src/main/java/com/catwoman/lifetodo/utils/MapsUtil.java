package com.catwoman.lifetodo.utils;

/**
 * Created by annt on 4/24/16.
 */
public class MapsUtil {
    public static String getStaticMapUrl(String center, String apiKey) {
        return "https://maps.googleapis.com/maps/api/staticmap"
                + "?center=" + center
                + "&zoom=10"
                + "&size=200x200"
                + "&scale=2"
                + "&key=" + apiKey;
    }
}
