package com.cnb.training.ui.JSFMegan;

import com.cnb.training.facade.ChemistryFacade;
import com.cnb.training.facade.FacadeFactory;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@Named("JSFformMegBean")
public class JSFformMeg implements Serializable {
    private List<CompoundEnt> compoundList = new ArrayList<>();
    private CompoundEnt addCompoundEnt;
    private CompoundEnt selectCompoundEnt;

    public List<CompoundEnt> getCompoundList() {
        return compoundList;
    }

    public void setCompoundList(List<CompoundEnt> compoundList) {
        this.compoundList = compoundList;
    }

    public CompoundEnt getAddCompoundEnt() {
        return addCompoundEnt;
    }

    public void setAddCompoundEnt(CompoundEnt addCompoundEnt) {
        this.addCompoundEnt = addCompoundEnt;
    }

    public CompoundEnt getSelectCompoundEnt() {
        return selectCompoundEnt;
    }

    public void setSelectCompoundEnt(CompoundEnt selectCompoundEnt) {
        this.selectCompoundEnt = selectCompoundEnt;
    }

    @PostConstruct
    public void init() {
// load the data
        loadCompounds();
        //you must create a new instance of the compound ent so it can be used when adding a compound
        this.addCompoundEnt= new CompoundEnt();
    }


    public void loadCompounds() {
        int result = 0;

        PrimeFaces instance = PrimeFaces.current();
        try {
            ChemistryFacade facade = (ChemistryFacade) FacadeFactory.getFacade("ChemistryFacade");

            compoundList = facade.selectAllCompounds();


            if (instance.isAjaxRequest()) {
                instance.ajax().update("CompoundsTableForm");
                instance.ajax().update("growlForm");

            }
        } catch (Exception e) {
            System.out.println("error in loading compounds");


        }
    }


    public void addCompound() {
        Long compoundId = null;
        PrimeFaces instance = PrimeFaces.current();
        try {


            ChemistryFacade facade = (ChemistryFacade) FacadeFactory.getFacade("ChemistryFacade");

            compoundId = facade.addNewCompound(addCompoundEnt);

            //requery the database to load the new compound into the list

            loadCompounds();

            //enter a message and update the table showing the compounds and the growl error message forms using ajax
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Save Successful", "New compound added to database"));
            if (instance.isAjaxRequest()) {
                instance.ajax().update("CompoundsTableForm");
                instance.ajax().update("growlForm");
            }

            //now clear the add compound entity so it does not appear with the previous values when next called
            this.addCompoundEnt= new CompoundEnt();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fail!", "New compound cound not be added."+ e.getMessage()));
            if (instance.isAjaxRequest()) {

                instance.ajax().update("growlForm");
            }
            System.out.println("error in adding compounds");
        }
    }
}

