package com.cnb.training.facade;



import com.cnb.training.DAO.ChemistryDAO;
import com.cnb.training.DAO.DatabaseConnectDAO;
import com.cnb.training.DAO.DataAccessFactory;
import com.cnb.training.ui.JSFMegan.CompoundEnt;
import com.cnb.training.util.exception.DataAccessException;

import java.sql.Connection;
import java.util.ArrayList;


public class ChemistryFacade extends DatabaseConnectDAO implements Facade {

    public ChemistryFacade() {
    }

    public ArrayList<CompoundEnt> selectAllCompounds() throws Exception {
        ArrayList<CompoundEnt> returnList = null;
        Connection connection = null;
        ChemistryDAO dao = (ChemistryDAO) DataAccessFactory.getDAO("ChemistryDAO");
        try {
            connection = this.getConnTraining();

                returnList = dao.selectCompoundList(connection);

        } catch (Exception ae) {
             System.out.println("Error in collecting from dao list collection in ChemistryFacade:" + ae.getMessage());
            throw new Exception("Error retrieving list collection in ChemistryFacade:" + ae.getMessage());
        } finally {
            close(connection);
        }

        return returnList;
    }


    public Long addNewCompound(CompoundEnt ent) throws Exception  {
        Long compoundId= null;
        Connection connCNB = null;
        ChemistryDAO dao = (ChemistryDAO) DataAccessFactory.getDAO("ChemistryDAO");
        try {
            connCNB = this.getConnTraining();
            compoundId = dao.addCompound(ent,connCNB  );
        } catch (Exception ae) {
            throw ae;
        } finally {
            close(connCNB);
        }
        return compoundId;
    }

}

