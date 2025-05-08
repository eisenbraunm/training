package com.cnb.training.ui;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@RequestScoped

@Named("MenuBean")
public class MenuBean {

    public void gotoOpenAthens() {


        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/connectid/OpenAthensRequest.xhtml?faces-redirect=true");

        //  return "OPENATHENS";
    }
/*  this is an alternative, using the faces-config - but you do not get the address in the browser
    public String gotoOpenAthens() {

        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/connectid/OpenAthensRequest.xhtml?faces-redirect=true");

        return "OPENATHENS";
    }
*/

    public void gotoMainMenu() {

        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/menus/MainMenu.xhtml?faces-redirect=true");
    }


    public void gotoFormsME() {

        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/JSFexamples/FormsME.xhtml?faces-redirect=true");

    }

    public void gotoStrucDraw() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/struc/StrucDraw.xhtml?faces-redirect=true");
    }

    public void gotoModal() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/struc/Modal.xhtml?faces-redirect=true");
    }

    public void gotoDialog() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/struc/Dialog.xhtml?faces-redirect=true");
    }
}
