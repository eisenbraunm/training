package com.cnb.training.connectid.database;



import com.cnb.training.connectid.database.model.OrgEntity;
import com.cnb.training.connectid.database.model.RedirectEntity;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class OrgDao extends DatabaseConnectDAO {

    private Map<Long, String> productIdMap = initializeProductIdMap();


    public OrgEntity getAuthenticatedUser(String shibEntityId, String shibScope) {
        Connection connAdmin = null;
        try {
            connAdmin = this.getConnectionAdmin();
        } catch (Exception e) {
            System.out.println("failed to  get admin connection");
            e.printStackTrace();
        }

        String sqlStatement = "SELECT        TOP (1) DOMAIN_ID, DOMAIN_NAME " +
                "FROM            V_DOMAIN_SUBSCRIPTIONS " +
                "WHERE        (ACTIVE = 1) AND (SHIB_ENTITY_ID =?) and SHIB_SCOPE=?";


        PreparedStatement tStatement = null;
        ResultSet tResultSet = null;
        OrgEntity ent = new OrgEntity();
        try {
            tStatement = connAdmin.prepareStatement(sqlStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            tStatement.setString(1, shibEntityId);
            tStatement.setString(2, shibScope);
            tResultSet = tStatement.executeQuery();

            while (tResultSet.next()) {

                ent.setDomainId(tResultSet.getLong("DOMAIN_ID"));
                ent.setDomainName(tResultSet.getString("DOMAIN_NAME"));
                ent.setShibEntityId(shibEntityId);
                ent.setShibScope(shibScope);
                ent.setAppsMap(getProductAccessMap(connAdmin, ent.getDomainId()));
                break;
            }
            ;
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            try {
                tResultSet.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
            try {
                tStatement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
            try {
                connAdmin.close();
            } catch (Exception e) {
                System.out.println("failed to close connection");
                e.printStackTrace();
            }
        }

        return ent;

    }


    public Map<String, Boolean> getProductAccessMap(Connection connAdmin, Long domainId) {

        String sqlStatement = "SELECT        APPLICATION_ID, APPLICATION " +
                " FROM            V_DOMAIN_SUBSCRIPTIONS " +
                " WHERE        (DOMAIN_ID = ?) AND (ACTIVE = 1)  AND (APPLICATION <> '1078')";

        Map<String, Boolean> productAccessMap = initializeProductAccessMap();
        PreparedStatement tStatement = null;
        ResultSet tResultSet = null;

        try {

            tStatement = connAdmin.prepareStatement(sqlStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            tStatement.setLong(1, domainId);

            tResultSet = tStatement.executeQuery();
            Long testLong;
            while (tResultSet.next()) {

                testLong = tResultSet.getLong("APPLICATION_ID");
                String acronym = productIdMap.get(testLong);
                productAccessMap.replace(acronym, Boolean.TRUE);


            }

//            Set a product to unsubscribed for testing
//            productAccessMap.replace("CCD", Boolean.FALSE);
        } catch (SQLException e) {
            // log.error("UserDAO SQL error. SQL=" + sqlStatement, e);
            System.out.println(e.getMessage());
            // throw new ApplicationException("getProductAccessMap", e);
        } finally {
            try {
                tResultSet.close();
            } catch (Exception e) {
                // log.info("Error closing a ResultSet for " + this.getClass().getName() + ": " + e.getMessage(), e);
            }
            try {
                tStatement.close();
            } catch (Exception e) {
                //   log.info("Error closing a PreparedStatement for " + this.getClass().getName() + ": " + e.getMessage(), e);
            }
        }
        return productAccessMap;
    }


    public RedirectEntity getRedirectEnt(Long redirectId) {
        Connection connAdmin = null;
        try {
            connAdmin = this.getConnectionResources();
        } catch (Exception e) {
            System.out.println("failed to  get resources connection");
            e.printStackTrace();
        }

        String sqlStatement = "SELECT        ID, APPLICATION_ID, ACRONYM, LOGON_REFER_URL,QS_PARAMS_JSON  FROM  TBL_REDIRECTS WHERE        (ID = ?)";


        PreparedStatement tStatement = null;
        ResultSet tResultSet = null;
        RedirectEntity ent = new RedirectEntity();
        try {
            tStatement = connAdmin.prepareStatement(sqlStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            tStatement.setLong(1, redirectId);
            tResultSet = tStatement.executeQuery();

            while (tResultSet.next()) {

                ent.setRedirectId(tResultSet.getLong("ID"));
                ent.setApplicationId(tResultSet.getLong("APPLICATION_ID"));
                ent.setAcronym(tResultSet.getString("ACRONYM"));
                ent.setLogonReferUrl(tResultSet.getString("LOGON_REFER_URL"));
                //get the encryptEntId, used when logging in from full entry display via the portal;

                try {
                    if (tResultSet.getString("QS_PARAMS_JSON") != null) {
                        String jsonString = tResultSet.getString("QS_PARAMS_JSON");
                        jsonString=  jsonString.replace("{\"values\":", "");
                        jsonString=  jsonString.replace("}}", "}");
                        JSONObject object = (JSONObject) JSONValue.parse(jsonString);
                        String encryptEntId = object.get("encryptEntId").toString();

                        if (encryptEntId != null && !encryptEntId.isEmpty()) {
                            ent.setEncryptEntId(encryptEntId);
                        }
                    }
                } catch (Exception e) {

                }
                break;
            }
            ;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            //throw new ApplicationException("UserDAO:getRedirectEnt", e);
        } finally {
            try {
                tResultSet.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                //  log.info("Error closing a ResultSet for " + this.getClass().getName() + ": " + e.getMessage());
            }
            try {
                tStatement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                //  log.info("Error closing a PreparedStatement for " + this.getClass().getName() + ": " + e.getMessage());
            }
            try {
                connAdmin.close();
            } catch (Exception e) {
                System.out.println("failed to close connection");
                e.printStackTrace();
            }
        }

        return ent;

    }


    private Map<Long, String> initializeProductIdMap() {
        Map<Long, String> map = new HashMap<>();

        map.put(1009L, "HBCP");
        map.put(1002L, "CCD");
        map.put(1015L, "DNP");
        map.put(1003L, "POC");
        map.put(1016L, "DOC");
        map.put(1018L, "DOD");
        map.put(1017L, "DIOC");
        map.put(1083L, "DMNP");
        map.put(1082L, "DFC");
        map.put(1001L, "DCCC");
        map.put(1010L, "POLY");
        return map;
    }

    private Map<String, Boolean> initializeProductAccessMap() {
        Map<String, Boolean> map = new HashMap<>();

        map.put("HBCP", false);
        map.put("CCD", false);
        map.put("DNP", false);
        map.put("POC", false);
        map.put("DOC", false);
        map.put("DOD", false);
        map.put("DIOC", false);
        map.put("DMNP", false);
        map.put("DFC", false);
        map.put("DCCC", false);
        map.put("POLY", false);
        return map;
    }


}
