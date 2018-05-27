package com.example.joris.webcookingdatawcd.object;

import java.util.ArrayList;
import java.util.List;

public class GardeManger {

    List<String> contenu = new ArrayList<String>();

    public GardeManger(List<String> contenu) {
        this.contenu = contenu;
    }

    public List<String> getContenu() {
        return contenu;
    }
}
