package com.darunkar.design_patterns.flyweight;

import java.util.Objects;

public class TextStyle {

    private final String fontFamily;
    private final int fontSize;
    private final String color;

    public TextStyle(String fontFamily, int fontSize, String color){
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.color = color;
    }

    public void applyStyle(String character, int position) {
        System.out.println("Rendering '" + character + "' at " + position +
                " using [" + fontFamily + ", " + fontSize + "px, " + color + "]");
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof TextStyle)) return false;
        TextStyle that = (TextStyle) o;
        return fontSize == that.fontSize && fontFamily.equals(that.fontFamily) && color.equals((that.color));
    }

    @Override
    public int hashCode() {
        return Objects.hash(fontFamily, fontSize, color);
    }
}
