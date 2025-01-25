package ru.radya.ycfunc;

public class IamEntity {
    private String iamToken;
    private String expiresAt;

    public IamEntity(String iamToken, String expiresAt) {
        this.iamToken = iamToken;
        this.expiresAt = expiresAt;
    }

    public void setIamToken(String iamToken) {
        this.iamToken = iamToken;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getIamToken() {
        return iamToken;
    }

    public String getExpiresAt() {
        return expiresAt;
    }
}
