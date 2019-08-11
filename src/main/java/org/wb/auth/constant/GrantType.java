package org.wb.auth.constant;

public enum GrantType {

    PASSWORD("password"),
    IMPLICIT("implicit"),
    REFRESH_TOKEN("refresh_token"),
    AUTHORIZATION_CODE("authorization_code");
    private String name;

    GrantType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
