package com.cnb.training.connectid.utils;


import com.cnb.training.connectid.database.OrgDao;
import com.cnb.training.connectid.database.model.ProductEntity;
import com.cnb.training.connectid.database.model.RedirectEntity;
import jakarta.servlet.http.HttpServletRequest;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogonUtils {


    public static Map<String, String> getValuesFromQSstring(String querystring) {

        Map<String, String> qsValues = new HashMap();
        if (querystring != null && querystring.contains("=")) {

            String params[] = querystring.split("&");

            for (String param : params) {

                String keyvalue[] = param.split("=");

                String key = keyvalue[0];
                String value = null;
                if (keyvalue.length == 2) {
                    value = keyvalue[1];
                }

                qsValues.put(key, value);
            }

        }
        return qsValues;

    }

    public static RedirectEntity getRedirectEnt(HttpServletRequest request, String environment) {
        RedirectEntity ent = null;
        String cookieQSstring = CookieTools.getCookieValue(request, "TandF.ACCT.CNB.cookieQS");

        if (cookieQSstring == null) {

            return null;

        } else {
            try {
                Map<String, String> cookieQSmap = LogonUtils.getValuesFromQSstring(cookieQSstring);
                Long redirectId = Long.parseLong(cookieQSmap.get("redir"));

                OrgDao dao = new OrgDao();
                ent = dao.getRedirectEnt(redirectId);
            } catch (Exception e) {
                return null;
            }


        }

        //  httpServletResponse.sendRedirect("https://twitter.com");
        return ent;

    }

    public static String buildReturnProductURL(String environment, RedirectEntity redirectEnt) {
        String redirectURL = null;
        if (redirectEnt.getLogonReferUrl() == null || redirectEnt.getLogonReferUrl().isEmpty()) {
            return null;
        }


        if (redirectEnt.getRedirectId() != null) {
            redirectURL = redirectEnt.getLogonReferUrl() + "?redir=" + redirectEnt.getRedirectId();

            if (redirectEnt.getEncryptEntId() != null) {
                try {
                    redirectURL += "&ide=" + URLEncoder.encode(redirectEnt.getEncryptEntId(), "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            redirectURL = redirectURL;
        } else {
            redirectURL = redirectEnt.getLogonReferUrl();
        }


        return redirectURL;

    }


    public static List<ProductEntity> buildSubsList(String environment, Map<String, Boolean> appsMap) {
        List<ProductEntity> list = new ArrayList<ProductEntity>();
        for (Map.Entry<String, Boolean> entry : appsMap.entrySet()) {
            ProductEntity ent = new ProductEntity();
            String acro = entry.getKey();
            ent.setAcronym(acro);
            ent.setHasAccess(entry.getValue());
            //*********** note the bottom 2 lines should be re-enabld in the final product******
            // this is the will be from a list giving the names of all the products and URL depending on their name and acronym
           // ent.setProductURL(AppConfig.getProductHome(acro, environment));
           // ent.setProductName(AppConfig.getProductFullName(acro));
            list.add(ent);
        }

        return list;

    }


}
