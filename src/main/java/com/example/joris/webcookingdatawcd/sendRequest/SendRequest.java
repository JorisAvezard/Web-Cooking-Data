package com.example.joris.webcookingdatawcd.sendRequest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendRequest {

    public SendRequest() {

    }

    public InputStream sendRequest(URL url) throws Exception {
        InputStream inputStream = null;
        try {
            // Ouverture de la connexion
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Connexion à l'URL
            urlConnection.connect();

            // Si le serveur nous répond avec un code OK
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream =  urlConnection.getInputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
