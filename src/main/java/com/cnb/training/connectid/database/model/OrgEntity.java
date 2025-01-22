package com.cnb.training.connectid.database.model;

import java.util.Map;

public class OrgEntity {
    private String domainName;
    private Long domainId;
    private String shibEntityId;
    private String shibScope;
    private Map<String, Boolean> appsMap;


    public Map<String, Boolean> getAppsMap() {
        return appsMap;
    }

    public void setAppsMap(Map<String, Boolean> appsMap) {
        this.appsMap = appsMap;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Long getDomainId() {return domainId;}

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getShibEntityId() {
        return shibEntityId;
    }

    public void setShibEntityId(String shibEntityId) {
        this.shibEntityId = shibEntityId;
    }

    public String getShibScope() {return shibScope;}

    public void setShibScope(String shibScope) {this.shibScope = shibScope;}
}
