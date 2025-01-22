package com.cnb.training.connectid;

import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.openid.connect.sdk.Nonce;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@SessionScoped
@Named("AuthenticatonParms")

public class AuthenticatonParms implements Serializable{
    private State state;
    private Nonce nonce;
    String shibEntityId;
    String eduPersonScopedAffiliation;
    String shibScope ;
    String jsonAuthStr;
    AccessToken accessToken;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Nonce getNonce() {
        return nonce;
    }

    public void setNonce(Nonce nonce) {
        this.nonce = nonce;
    }

    public String getShibEntityId() {
        return shibEntityId;
    }

    public void setShibEntityId(String shibEntityId) {
        this.shibEntityId = shibEntityId;
    }

    public String getEduPersonScopedAffiliation() {
        return eduPersonScopedAffiliation;
    }

    public void setEduPersonScopedAffiliation(String eduPersonScopedAffiliation) {
        this.eduPersonScopedAffiliation = eduPersonScopedAffiliation;
    }

    public String getShibScope() {
        return shibScope;
    }

    public void setShibScope(String shibScope) {
        this.shibScope = shibScope;
    }

    public String getJsonAuthStr() {
        return jsonAuthStr;
    }

    public void setJsonAuthStr(String jsonAuthStr) {
        this.jsonAuthStr = jsonAuthStr;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }
}
