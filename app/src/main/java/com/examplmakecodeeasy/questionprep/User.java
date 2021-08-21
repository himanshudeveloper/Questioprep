package com.examplmakecodeeasy.questionprep;

public class User {
    private String name;
    private String email;
    private String pass;
    private String profile;
    private long coin;

    public User() {
    }

    public long getCoin() {
        return coin;
    }

    public void setCoin(long coin) {
        this.coin = coin;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    private String referCode ="none";
    private long coins =25;

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public User(String name, String email, String pass) {

    }

    public User(String name, String email, String pass, String referCode) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.referCode = referCode;
    }

    public User(String name, String email, String profile, long coins) {
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.coin = coins;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name=name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }
}
