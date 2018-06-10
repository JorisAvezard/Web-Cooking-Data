package com.example.joris.webcookingdatawcd.object;

import java.util.ArrayList;
import java.util.List;

public class Data {

    String data;
    String login;
    public List<String> activites;
    public List<String> maladies;
    public List<String> regimes;
    public List<String> allergies;

    public String sexe;
    public String age;
    public String taille;
    public String poids;
    public String activite;
    public String maladie;
    public String regime;
    public String allergie;
    public String besoin;

    public Data(String data) {
        this.data = data;
    }

    public Data() {
        activites = new ArrayList<String>();
        maladies = new ArrayList<String>();
        regimes = new ArrayList<String>();
        allergies = new ArrayList<String>();

        sexe = "";
        age = "";
        taille = "";
        poids = "";
        activite = "";
        maladie = "";
        regime = "";
        allergie = "";
        besoin = "";
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

    public List<String> getActivites() {
        return activites;
    }

    public List<String> getMaladies() {
        return maladies;
    }

    public void setMaladies(List<String> maladies) {
        this.maladies = maladies;
    }

    public List<String> getRegimes() {
        return regimes;
    }

    public void setRegimes(List<String> regimes) {
        this.regimes = regimes;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public void setActivites(List<String> activites) {
        this.activites = activites;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getPoids() {
        return poids;
    }

    public void setPoids(String poids) {
        this.poids = poids;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public String getMaladie() {
        return maladie;
    }

    public void setMaladie(String maladie) {
        this.maladie = maladie;
    }

    public String getRegime() {
        return regime;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }

    public String getAllergie() {
        return allergie;
    }

    public void setAllergie(String allergie) {
        this.allergie = allergie;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBesoin() {
        return besoin;
    }

    public void setBesoin(String besoin) {
        this.besoin = besoin;
    }
}
