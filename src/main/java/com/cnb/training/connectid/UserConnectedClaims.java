package com.cnb.training.connectid;


import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@RequestScoped

@Named("UserConnectedClaimsBean")
public class UserConnectedClaims implements Serializable {
    @Inject
    private AuthenticatonParms authenticatonParms;  //not used here yet, but could be used to set up a query to the database to confirm authentication

    public void backToMenu() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/menus/MainMenu.xhtml?faces-redirect=true");
    }

}
