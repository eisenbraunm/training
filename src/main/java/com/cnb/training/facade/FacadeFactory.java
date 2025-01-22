package com.cnb.training.facade;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class FacadeFactory {

    private static Map facadeHash = new HashMap();

    static {
        try {
            facadeHash.put("ChemistryFacade", "com.cnb.training.facade.ChemistryFacade");

        } catch (Exception e) {
            //Don't care about exception.
        }
    }

    /**
     * return an instance of the specified facade. The actual class to return is determined by looking
     * for the key in a properties file. If the key exists, that is the Facade that will be returned,
     * otherwise the default facade for the specified object will be returned.
     *
     * @param key the key of the desired Facade.
     * @return Facade the desired Facade.
     */
    public static Facade getFacade(String key) throws Exception {
        Facade facade = null;
        try {
            String facadeName = (String) facadeHash.get(key);
            facade = (Facade) Class.forName(facadeName).getDeclaredConstructor().newInstance();

        } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | IllegalAccessException | InstantiationException | ClassNotFoundException  ex) {
            throw new Exception(ex);

        }
        return facade;
    }
}
