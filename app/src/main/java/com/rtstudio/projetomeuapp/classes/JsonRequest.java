package com.rtstudio.projetomeuapp.classes;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Raphael Rodrigues on 29/04/2019.
 */
public class JsonRequest {

    public static String requesitarJson(String uri) throws Exception {
        URL url = new URL(uri);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String linha;
        StringBuilder stringJson = new StringBuilder();

        while ((linha = bufferedReader.readLine()) != null) {
            stringJson.append(linha);
        }

        urlConnection.disconnect();

        return stringJson.toString();
    }
}
