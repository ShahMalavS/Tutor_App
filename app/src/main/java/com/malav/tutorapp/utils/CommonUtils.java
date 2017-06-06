package com.malav.tutorapp.utils;

/**
 * Created by shahmalav on 14/08/16.
 */

public class CommonUtils {
    public static boolean hasText(String string) {

        return string!=null && !string.trim().isEmpty();
    }

    public static boolean hasNoText(String string) {
        return !hasText(string);
    }

    public static boolean equalIgnoreCase(String string1, String string2) {

        return string1!=null && string2!=null && string1.equalsIgnoreCase(string2);
    }

    public static boolean isNull(String string){

        return string==null || string.equalsIgnoreCase("null") || string.trim().length()==0;
    }

    public static boolean isNotNull(String string){
        return !isNull(string);
    }

    public static boolean isZero(int value){
        return value==0;
    }

    public static boolean isNonZero(int value){
        return !isZero(value);
    }
}
