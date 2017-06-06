package com.malav.tutorapp.custom;

/**
 * Created by shahmalav on 19/09/16.
 */

public class SpinnerWithTag {
    public String string;
    public String tag;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public SpinnerWithTag(String stringPart, String tagPart) {
        string = stringPart;
        tag = tagPart;
    }

    @Override
    public String toString() {
        return string;
    }
}
