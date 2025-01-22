package com.cnb.training.DAO;



import com.cnb.training.connectid.database.DatabaseConnectDAO;

import com.cnb.training.ui.GlobalBean;
import com.cnb.training.ui.JSFMegan.CompoundEnt;
import com.cnb.training.util.exception.DataAccessException;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * MyBean myBean = CDI.current().select( MyBean.class ).get()
 *
 * @author EisenbraunM
 */


public class ChemistryDAO extends DatabaseConnectDAO implements DataAccess {

    private Logger log = LogManager.getLogger(ChemistryDAO.class);



    public ArrayList<CompoundEnt> selectCompoundList(Connection connection) throws Exception {

        String sqlStatement = "SELECT        ID, NAME " +
                "FROM            TBL_COMPOUND " +
                "ORDER BY NAME";

        ArrayList<CompoundEnt> results =new ArrayList<>();
        try (PreparedStatement tStatement = connection.prepareStatement(sqlStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            try (ResultSet tResultSet = tStatement.executeQuery();) {
                while (tResultSet.next()) {
                    results.add(populateCompoundEnt(tResultSet));
                }
            }

        } catch (SQLException e) {




            log.error("selectCompoundList ",e );
        }

        return results;
    }


    private CompoundEnt populateCompoundEnt(ResultSet tResultSet) throws SQLException {
        Long testLong;

        CompoundEnt ent = new CompoundEnt();

        testLong = tResultSet.getLong("ID");
        if (!tResultSet.wasNull()) {
            ent.setID(testLong);
        }

        ent.setName(tResultSet.getString("NAME"));


        return ent;
    }

    public Long addCompound(CompoundEnt ent, Connection connection)throws Exception {

        String sqlStatement = "INSERT INTO  TBL_COMPOUND( NAME) VALUES(?)";

        Long compoundId = null;

        try (PreparedStatement tStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)) {
            tStatement.setString(1, ent.getName());

            tStatement.executeUpdate();

            try (ResultSet generatedKeys  = tStatement.getGeneratedKeys();) {

                if (generatedKeys.next()) {
                    compoundId  = generatedKeys.getLong(1);
                }
            }

        } catch (SQLException e) {
            log.error("ChemistryDAO  SQL error", sqlStatement, e);
            throw new Exception(e.getMessage());
        }

        return compoundId;
    }


}
