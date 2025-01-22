package com.cnb.training.connectid.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class CookieTools {


    /**
     * Retrieve the cookie value from the given servlet request based on the given cookie name.
     *
     * @param request The HttpServletRequest to be used.
     * @param name The cookie name to retrieve the value for.
     * @return The cookie value associated with the given cookie name.
     */
    public static String getCookieValue(HttpServletRequest request, String name) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {

                if (cookie != null && name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Set the cookie value in the given servlet response based on the given cookie name and expiration interval.
     *
     * @param response The HttpServletResponse to be used.
     * @param name The cookie name to associate the cookie value with.
     * @param value The actual cookie value to be set in the given servlet response.
     * @param maxAge The expiration interval in seconds. If this is set to 0, then the cookie will immediately expire.
     */
    public static void setCookieValue(HttpServletResponse response, String name, String value, int maxAge, String hostDomain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);

       // cookie.("ChemNetBase Cookie");
        cookie.setPath("/");


        cookie.setDomain(hostDomain);

        response.addCookie(cookie);

    }

    //this has no max age so is only valid whilst browser open
    public static void setAcctCookieValue(HttpServletResponse response, String domainId, String hostDomain) {
        Cookie cookie = new Cookie("TandF.ACCT.CNB.cookieId", "shib"+ domainId);

      //  cookie.setComment("AuthenticationChemNetBase Cookie");
        cookie.setPath("/");
        cookie.setDomain(hostDomain);
        response.addCookie(cookie);

    }

    public static void deleteCookiesLogout(HttpServletResponse response, String domainId, String hostDomain) {
        Cookie acctCookie = new Cookie("TandF.ACCT.CNB.cookieId", "shib"+ domainId);
        //shib indenifies that this is from OAthens and holds domain Id
        acctCookie.setMaxAge(0);
       // acctCookie.setComment("AuthenticationChemNetBase Cookie");
        acctCookie.setPath("/");
        acctCookie.setDomain(hostDomain);
        response.addCookie(acctCookie);
    }
}


