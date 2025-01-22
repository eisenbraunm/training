package com.cnb.training.connectid;

import com.cnb.training.ui.GlobalBean;
import com.cnb.training.ui.ParameterBean;
import com.nimbusds.jwt.JWT;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.*;
import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.openid.connect.sdk.*;
import com.nimbusds.oauth2.sdk.id.*;
import com.nimbusds.oauth2.sdk.http.*;
import com.nimbusds.oauth2.sdk.id.*;
import com.nimbusds.oauth2.sdk.token.*;
import net.minidev.json.JSONArray;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URLDecoder;
@WebServlet(name = "openAthensServlet", value = "/openid_connect_login")

public class CNBopenAthensResponse extends HttpServlet {
    @Inject
  GlobalBean global;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpServletRequest httpRequest = request;
        HttpSession httpSession = httpRequest.getSession();
        AuthenticationResponse responseAuth=null;

        //first pick up the environment dependent  variables from the context
        String secret = ((HttpServletRequest) request).getSession().getServletContext().getInitParameter("secret");
        String clientIDstr = ((HttpServletRequest) request).getSession().getServletContext().getInitParameter("clientId");
        String callbackURI = ((HttpServletRequest) request).getSession().getServletContext().getInitParameter("callbackURI");

       System.out.println("got the request! in the CNBopenAthensResponse servlet class");
        AuthenticatonParms  params = CDI.current().select(AuthenticatonParms.class).get();

        try {
            String uriQS=request.getRequestURI() +"?" +request.getQueryString();
            System.out.println("request.getRequestURI() "+ uriQS);
          responseAuth = AuthenticationResponseParser.parse( new URI(uriQS));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        if (! responseAuth.getState().equals(params.getState())) {
            System.err.println("Unexpected authentication response");
            return;
        }
        if (responseAuth instanceof AuthenticationErrorResponse) {
            // The OpenID provider returned an error
            System.err.println(responseAuth.toErrorResponse().getErrorObject());
            return;
        }

// Retrieve the authorisation code, to use it later at the token endpoint
        AuthorizationCode code = responseAuth.toSuccessResponse().getAuthorizationCode();


System.out.println(" AuthorizationCode code " + code.toJSONString() );




/// ********************Now decode the response and get the access tokens*********************************
        URI callback = null;
        try {
            callback = new URI(callbackURI);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        AuthorizationGrant codeGrant = new AuthorizationCodeGrant(code, callback);
// The credentials to authenticate the client at the token endpoint
        ClientID clientID = new ClientID(clientIDstr);
        Secret clientSecret = new Secret(secret);
        ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);


        //https://docs.openathens.net/providers/quickstart-for-openathens-keystone
       // userinfo endpoint https://connect.openathens.net/oidc/userinfo
// The token endpoint
        URI tokenEndpoint = null;
        try {
            tokenEndpoint = new URI("https://connect.openathens.net/oidc/token");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

// Make the token request
        TokenRequest tokenRequest= new TokenRequest(tokenEndpoint, clientAuth, codeGrant);

        TokenResponse tokenResponse = null;
        try {
            tokenResponse = OIDCTokenResponseParser.parse(tokenRequest.toHTTPRequest().send());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (! responseAuth.indicatesSuccess()) {
            // We got an error response...
            TokenErrorResponse errorResponse = tokenResponse.toErrorResponse();
        }
        OIDCTokenResponse successResponse = (OIDCTokenResponse)tokenResponse.toSuccessResponse();

// Get the ID and access token, the server may also return a refresh token
        JWT idToken = successResponse.getOIDCTokens().getIDToken();
        AccessToken accessToken = successResponse.getOIDCTokens().getAccessToken();
        RefreshToken refreshToken = successResponse.getOIDCTokens().getRefreshToken();


        System.out.println("access token :" + accessToken.toJSONString());
        System.out.println(" id token: " + idToken.getParsedString());

        /// ********************Now try to get the claims*********************************



        URI userInfoEndpoint = null;
        try {
            userInfoEndpoint= new URI("https://connect.openathens.net/oidc/userinfo");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }


// Make the request
        HTTPResponse httpResponse = new UserInfoRequest(userInfoEndpoint, accessToken)
                .toHTTPRequest()
                .send();

// Parse the response
        UserInfoResponse userInfoResponse = null;
        try {
            userInfoResponse = UserInfoResponse.parse(httpResponse);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (! userInfoResponse.indicatesSuccess()) {
            // The request failed, e.g. due to invalid or expired token
            System.out.println(userInfoResponse.toErrorResponse().getErrorObject().getCode());
            System.out.println(userInfoResponse.toErrorResponse().getErrorObject().getDescription());
            return;
        }

// Extract the claims
        UserInfo userInfo = userInfoResponse.toSuccessResponse().getUserInfo();

        String jsonstring  = userInfo.toJSONString();

        System.out.println("jsonstring :" + jsonstring);
        String shibEntityId = userInfo.getClaim("realmName").toString();
        String eduPersonScopedAffiliation ="the eduPersonScopedAffiliation not passed back by the connection - look in the openathens settings";

  try {
      eduPersonScopedAffiliation=  userInfo.getClaim("eduPersonScopedAffiliation").toString();

    } catch (Exception e) {
System.out.println("the eduPersonScopedAffiliation not passed back by the connection - look in the openathens settings");
    }
        String shibScope  = userInfo.getClaim("derivedEduPersonScope").toString();
        shibEntityId = shibEntityId.replace("\"", "");
        shibScope = shibScope.replace("\"", "");

        //now read them into the params bean

        params.setShibEntityId(shibEntityId );

params.setShibScope(shibScope);
params.setEduPersonScopedAffiliation(eduPersonScopedAffiliation);
params.setJsonAuthStr(jsonstring);
        //**********************
        response.sendRedirect("http://localhost:8090/CNBtraining/connectid/UserConnectedClaims.xhtml");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


}
