package com.example.joris.webcookingdatawcd.object;

public class Data {

    String data;
    String login;

    public Data(String data) {
        this.data = data;
    }

    public String toString() {
        return "Data [data=" + data + "]";
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
