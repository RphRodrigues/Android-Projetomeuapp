package com.rtstudio.projetomeuapp.server;

import android.util.Log;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.modelo.OrdemServico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Raphael Rodrigues on 06/05/2019.
 */
public class ConnectServer {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static boolean post(OrdemServico ordemServico) {
        String url_post = "http://ec2-18-221-156-122.us-east-2.compute.amazonaws.com:8080/sinapse-ws/webresources/android/inserirOS";

        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();

        String json = gson.toJson(ordemServico);

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url_post)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {

                Log.i("Response", "post: " + response.toString() + " -> " + response.body().string());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<OrdemServico> get() {
        String url_get = "http://ec2-18-221-156-122.us-east-2.compute.amazonaws.com:8080/sinapse-ws/webresources/android/getListaOS";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url_get)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        String jsonRetorno;
        Gson gson = new Gson();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                jsonRetorno = response.body().string();

                OrdemServico[] arrayOS = gson.fromJson(jsonRetorno, OrdemServico[].class);

                return new ArrayList<>(Arrays.asList(arrayOS));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void put(OrdemServico ordemServico) {
        String url_post = "http://ec2-18-221-156-122.us-east-2.compute.amazonaws.com:8080/sinapse-ws/webresources/android/atualizarOS/";

        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();

        String json = gson.toJson(ordemServico);

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url_post)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                Log.i("Response", "post: " + response.toString() + " -> " + response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int ordemServicoId) {
        String url_post = "http://ec2-18-221-156-122.us-east-2.compute.amazonaws.com:8080/sinapse-ws/webresources/android/DeletarOS/" + ordemServicoId;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url_post)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                Log.i("Response", "post: " + response.toString() + " -> " + response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
