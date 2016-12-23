package com.humax.toy.rxretrotest.module;

/**
 * Created by Tony on 16/9/6
 */
public class ResultInstance {
    private String login;
    private int id;
    private int contributions;

    public ResultInstance(String login, int id, int contributions) {
        this.login = login;
        this.id = id;
        this.contributions = contributions;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }
}
