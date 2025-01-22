package com.cnb.training.connectid;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.net.HttpURLConnection;

import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import com.cnb.training.ui.GlobalBean;
import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.openid.connect.sdk.*;
import com.nimbusds.oauth2.sdk.id.*;
import com.sun.tools.javac.Main;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//https://connect2id.com/products/nimbus-oauth-openid-connect-sdk/examples/openid-connect/oidc-auth


@RequestScoped

@Named("OpenAthensBuildRequestBean")

public class BuildRequest implements Serializable {

    @Inject
    private AuthenticatonParms authenticatonParms;
    public  void createRequest () throws URISyntaxException, IOException, InterruptedException {

        //first pick up the environment dependent  variables from the context
        HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String clientSecret = ((HttpServletRequest) httpRequest).getSession().getServletContext().getInitParameter("secret");
        String clientIDstr= ((HttpServletRequest) httpRequest).getSession().getServletContext().getInitParameter("clientId");
        String callbackURI= ((HttpServletRequest) httpRequest).getSession().getServletContext().getInitParameter("callbackURI");
// The client ID provisioned by the OpenID provider when
// the client was registered
        ClientID clientID = new ClientID(clientIDstr);

// The client callback URL

        URI callback = new URI(callbackURI);


// Generate random state string to securely pair the callback to this request
        State state = new State();
        authenticatonParms.setState(state);
// Generate nonce for the ID token
        Nonce nonce = new Nonce();
        authenticatonParms.setNonce(nonce);
// Compose the OpenID authentication request (for the code flow)
        AuthenticationRequest request = new AuthenticationRequest.Builder(
                new ResponseType("code"),
                new Scope("openid"),
                clientID,
                callback)
                .endpointURI(new URI("https://connect.openathens.net/oidc/auth"))
                .state(state)
                .nonce(nonce)

                .build();


        System.out.println(request.toURI());


        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        FacesContext.getCurrentInstance().responseComplete();
        System.out.println(request.toURI());

        ((HttpServletResponse) response).sendRedirect(request.toURI().toString());
    }

    public static void main(String[] args)
    {
        try
        {
          //  createRequest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
