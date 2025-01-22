package com.cnb.training.ui;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("helloBean")

@SessionScoped

public class Hello  implements Serializable{
    private String welcome= "Welcome from the bean!";

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }
}

