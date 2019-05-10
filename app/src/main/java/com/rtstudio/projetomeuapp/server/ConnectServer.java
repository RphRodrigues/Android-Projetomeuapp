package com.rtstudio.projetomeuapp.server;

import android.util.Log;

import com.google.gson.Gson;
import com.rtstudio.projetomeuapp.classes.OrdemServico;

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
        String url_post = "http://192.168.0.5:8080/ProjetoWebService/webresources/inserirOS";

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
                ordemServico.setSyncStatus(OrdemServico.SYNC_STATUS_TRUE);
                Log.i("Response", "post: " + response.toString() + " -> " + response.body().string());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<OrdemServico> get() {
        String url_get = "http://192.168.0.5:8080/ProjetoWebService/webresources/getListaOS";

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
                List<OrdemServico> listOS = new ArrayList<>(Arrays.asList(arrayOS));
                return listOS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void put(OrdemServico ordemServico) {
        String url_post = "http://192.168.0.5:8080/ProjetoWebService/webresources/atualizarOS/" + ordemServico.getOrdemServicoId();

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
        String url_post = "http://192.168.0.5:8080/ProjetoWebService/webresources/DeletarOS/" + ordemServicoId;

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
