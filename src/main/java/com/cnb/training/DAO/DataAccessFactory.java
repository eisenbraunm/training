

package com.cnb.training.DAO;




import com.cnb.training.util.exception.DataAccessException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


public class DataAccessFactory {


    private static Map daoHash = new HashMap();

    static {
        try {

            daoHash.put("ChemistryDAO", "com.cnb.training.DAO.ChemistryDAO");


        } catch (Exception e) {
            //Don't care about exception
        }
    }

    /**
     * return an instance of the request Data Access object.
     *
     * @param_daoName the full name of the desired Data Access object.
     * @return DataAccess the desired Data Access object.
     */
//
//      return an instance of the request Data Access object.
//
//     @param daoName the full name of the desired Data Access object.
//    @return DataAccess the desired Data Access object.

    public static DataAccess getDAO(String daoKey) throws DataAccessException {
        DataAccess dao = null;
        try {
            String daoName = (String) daoHash.get(daoKey);
            //     dao = (DataAccess) Class.forName(daoName).newInstance();
            dao = (DataAccess) Class.forName(daoName).getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | IllegalAccessException | InstantiationException | ClassNotFoundException  ex) {
            throw new DataAccessException("failed to get DAO " +daoKey);
            //Don't care about exception
        }
        return dao;
    }
}
