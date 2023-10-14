package com.maindark.livestream.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StreamToListUtil {
    public static <T> ArrayList<T> getArrayListFromStream(Stream<T> stream){
        List<T> list = stream.toList();
        // Create an ArrayList of the List
        //ArrayList<T> arrayList = new ArrayList<T>(list);
        // Return the ArrayList
        return new ArrayList<T>(list);
    }
}
