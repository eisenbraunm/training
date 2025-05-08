package com.cnb.training.ui;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@ViewScoped
@Named("DialogBean")
public class DialogBean implements Serializable {

    private String molFile;

    public String getMolFile() {
        return molFile;
    }

    public void setMolFile(String molFile) {
        this.molFile = molFile;
        System.out.println(molFile);
    }
}
