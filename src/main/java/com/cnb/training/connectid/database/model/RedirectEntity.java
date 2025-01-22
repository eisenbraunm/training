package com.cnb.training.connectid.database.model;

public class RedirectEntity {
    private Long redirectId;
    private String encryptEntId;
    private String logonReferUrl;
    private Long applicationId;
    private String acronym;

    public Long getRedirectId() {
        return redirectId;
    }

    public void setRedirectId(Long redirectId) {
        this.redirectId = redirectId;
    }

    public String getEncryptEntId() {
        return encryptEntId;
    }

    public void setEncryptEntId(String encryptEntId) {
        this.encryptEntId = encryptEntId;
    }

    public String getLogonReferUrl() {
        return logonReferUrl;
    }

    public void setLogonReferUrl(String logonReferUrl) {
        this.logonReferUrl = logonReferUrl;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }
}
