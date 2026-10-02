package views.viva.views.Funcionalidades.API;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class APICommunication {

    //esse método consome a rota login e devolve o JSON em forma de string (todas as informações do JSON
    /*Exemplo de uso:
        String a = APICommunicationLibrary.getBearerToken("adm1", "123");
     */
    public static String getBearerToken(String username, String password) {
        OkHttpClient client = new OkHttpClient();
        String resp = null;
        String json = "{\"login\": \""+username+"\", \"senha\": \""+password+"\"}";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("https://tcc-r46r.onrender.com/login")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro inesperado: " + response);
            }
            resp = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }


    //esse método converte um JSON que contém muitas informações vindas da função login
    // em apenas o barrer token e retorna ele em forma de string
    /*Exemplo de uso:
        String a = APICommunicationLibrary.getBearerToken("adm1", "123");
        String token = APICommunicationLibrary.convertBearerToken(a);
     */
    public static String ConvertBearerToken(String jsonString) {
        String token = null;

        JSONObject jsonResponse = new JSONObject(jsonString);

        if (jsonResponse.getBoolean("success")) {
            JSONObject tokenJSON = jsonResponse.getJSONObject("obj")
                    .getJSONObject("usuario")
                    .getJSONObject("token");
            token = tokenJSON.getString("token");
        } else {
            token = "erro ao obter token";
        }
        return token;
    }


    //esse é utilizado para rodar requisições POST, apenas aquelas que não enviam nd pelo body
    //o retorno será uma string do JSON obtido
    //caso queira utilizar algo que esteja dentro desse JSON faça como na função convertBearerToken que está na classe APICommunicationLibrary.java
    /*Exemplo de uso:
        String a = APICommunicationLibrary.getBearerToken("adm1", "123");
        String token = APICommunicationLibrary.convertBearerToken(a);
        String b = APICommunicationLibrary.reqGET("https://tcc-r46r.onrender.com/evento/selecao/todos", token");
     */
    public static String ReqGET(String url, String token) {
        OkHttpClient client = new OkHttpClient();
        String resp = null;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro inesperado: " + response);
            }
            resp = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }


    //essa faz requisições PUT, enviando dados pelo body -> deve seguir o seguinte padrao: "{\"parametro1\": \"dado1\", \"parametroN\": \"dadoN\"}"
    //o retorno será uma string contendo o JSON obtido
    //caso queira utilizar algo que esteja dentro desse JSON faça como na função convertBearerToken que está na classe APICommunicationLibrary.java
    /*Exemplo de uso:
        String a = APICommunicationLibrary.getBearerToken("adm1", "123");
        String token = APICommunicationLibrary.convertBearerToken(a);
        String b = APICommunicationLibrary.reqPUT("https://tcc-r46r.onrender.com/vendedor/alterarnome/1",
                token,
                "{\"nomeLoja\": \"novoNome\"}");
     */
    public static String ReqPUT(String url, String token, String body) {
        OkHttpClient client = new OkHttpClient();
        String resp = null;

        RequestBody reqBody;
        if (body == null || body.isEmpty()) {
            reqBody = RequestBody.create("", MediaType.get("application/json; charset=utf-8"));
        } else {
            reqBody = RequestBody.create(body, MediaType.get("application/json; charset=utf-8"));
        }

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .put(reqBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro inesperado: " + response);
            }
            resp = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

}