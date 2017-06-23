package com.example.coutlootinventory;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

/**
 * Created by karan on 12/05/2017.
 */

public class InventoryApplication extends Application {

    private static InventoryApplication application;
    boolean loggedIn = false;

    String userId = "", userName = "", userCity = "", userProfileImage = "", userCityName = "", userCode = "", userEmail = "", userNumber = "", userGender = "", tokenId = "", deviceId = "", loginType = "", socialId = "", dob = "";

    String sessionId = "";


    public static InventoryApplication getInstance() {
        return application;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getUserCity() {
        return userCity;
    }

    public String getUserCityName() {
        return userCityName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getDob() {
        return dob;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public String getLoginType() {
        return loginType;
    }

    public String getSocialId() {
        return socialId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public void setUserCityName(String userCityName) {
        this.userCityName = userCityName;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public static InventoryApplication getApplication() {
        return application;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public static void setApplication(InventoryApplication application) {
        InventoryApplication.application = application;
    }


}
