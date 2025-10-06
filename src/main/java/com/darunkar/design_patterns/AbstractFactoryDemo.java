package com.darunkar.design_patterns;

import javax.print.attribute.standard.MediaSize;

interface Button{
    void render();
}

interface Checkbox{
    void render();
}

class LightButton implements Button{

    @Override
    public void render() {
        System.out.println("Rendering light button");
    }
}

class DarkButton implements Button{

    @Override
    public void render() {
        System.out.println("Rendering Dark Button");
    }
}

class LightCheckbox implements Checkbox{

    @Override
    public void render() {
        System.out.println("Rendering Light Checkbox");
    }
}

class DarkCheckbox implements Checkbox{

    @Override
    public void render() {
        System.out.println("Rendering Dark Checkbox..");
    }
}

interface UIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

class LightUIFactory implements UIFactory {

    @Override
    public Button createButton() {
        return new LightButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new LightCheckbox();
    }
}

class DarkUIFactory implements UIFactory {

    @Override
    public Checkbox createCheckbox() {
        return new DarkCheckbox();
    }

    @Override
    public Button createButton() {
        return new DarkButton();
    }
}

class UIFactoryProvider{

    public static UIFactory getFactory(String type) {
        if(type.equalsIgnoreCase("light")) {
            return new LightUIFactory();
        }else if(type.equalsIgnoreCase("dark")){
            return new DarkUIFactory();
        }

        throw new IllegalArgumentException("Unknown Theme : " + type);
    }
}

public class AbstractFactoryDemo {
    public static void main(String[] args) {
        // Imagine this comes from a config file or environment variable
        String appTheme = System.getProperty("app.theme", "dark");

        // Factory provided dynamically
        UIFactory uiFactory = UIFactoryProvider.getFactory(appTheme);

        // Client code is theme-agnostic
        Button btn = uiFactory.createButton();
        Checkbox chk = uiFactory.createCheckbox();

        btn.render();
        chk.render();
    }
}
