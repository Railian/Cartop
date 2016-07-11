package com.cartop.android.core.models;

import com.google.gson.annotations.SerializedName;

public class Token {

    //region fields
    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;
    //endregion

    //region Getters And Setters
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    //endregion

    //region toString
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Token{");
        sb.append("tokenType='").append(tokenType).append('\'');
        sb.append(", accessToken='").append(accessToken).append('\'');
        sb.append(", refreshToken='").append(refreshToken).append('\'');
        sb.append('}');
        return sb.toString();
    }
    //endregion
}
