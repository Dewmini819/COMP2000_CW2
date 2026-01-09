package uk.ac.plymouth.restaurantcw2;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiClient {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface JsonCallback {
        void onSuccess(JSONObject json);
        void onError(String message);
    }

    private static String readStream(InputStream is) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();
        return sb.toString();
    }

    public static void getAsync(String endpoint, JsonCallback cb) {
        executor.execute(() -> {
            try {
                JSONObject result = get(endpoint);
                mainHandler.post(() -> cb.onSuccess(result));
            } catch (Exception e) {
                mainHandler.post(() -> cb.onError(e.getMessage()));
            }
        });
    }

    public static void postAsync(String endpoint, JSONObject body, JsonCallback cb) {
        executor.execute(() -> {
            try {
                JSONObject result = post(endpoint, body);
                mainHandler.post(() -> cb.onSuccess(result));
            } catch (Exception e) {
                mainHandler.post(() -> cb.onError(e.getMessage()));
            }
        });
    }

    public static void putAsync(String endpoint, JSONObject body, JsonCallback cb) {
        executor.execute(() -> {
            try {
                JSONObject result = put(endpoint, body);
                mainHandler.post(() -> cb.onSuccess(result));
            } catch (Exception e) {
                mainHandler.post(() -> cb.onError(e.getMessage()));
            }
        });
    }

    public static void deleteAsync(String endpoint, JsonCallback cb) {
        executor.execute(() -> {
            try {
                JSONObject result = delete(endpoint);
                mainHandler.post(() -> cb.onSuccess(result));
            } catch (Exception e) {
                mainHandler.post(() -> cb.onError(e.getMessage()));
            }
        });
    }

    private static JSONObject get(String endpoint) throws Exception {
        URL url = new URL(Config.BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestMethod("GET");

        int code = conn.getResponseCode();
        InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        String resp = (is != null) ? readStream(is) : "";
        conn.disconnect();

        if (resp.isEmpty()) return new JSONObject();

        JSONObject json = new JSONObject(resp);
        if (code < 200 || code >= 300) {
            String detail = json.has("detail") ? json.optString("detail") : resp;
            throw new Exception("HTTP " + code + ": " + detail);
        }
        return json;
    }

    static JSONObject post(String endpoint, JSONObject body) throws Exception {
        URL url = new URL(Config.BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        if (body != null) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.toString().getBytes(StandardCharsets.UTF_8));
            }
        }

        int code = conn.getResponseCode();
        InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        String resp = (is != null) ? readStream(is) : "";
        conn.disconnect();

        if (resp.isEmpty()) return new JSONObject();

        JSONObject json = new JSONObject(resp);
        if (code < 200 || code >= 300) {
            String detail = json.has("detail") ? json.optString("detail") : resp;
            throw new Exception("HTTP " + code + ": " + detail);
        }
        return json;
    }

    private static JSONObject put(String endpoint, JSONObject body) throws Exception {
        URL url = new URL(Config.BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        if (body != null) {
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.toString().getBytes(StandardCharsets.UTF_8));
            }
        }

        int code = conn.getResponseCode();
        InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        String resp = (is != null) ? readStream(is) : "";
        conn.disconnect();

        if (resp.isEmpty()) return new JSONObject();

        JSONObject json = new JSONObject(resp);
        if (code < 200 || code >= 300) {
            String detail = json.has("detail") ? json.optString("detail") : resp;
            throw new Exception("HTTP " + code + ": " + detail);
        }
        return json;
    }

    private static JSONObject delete(String endpoint) throws Exception {
        URL url = new URL(Config.BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestMethod("DELETE");

        int code = conn.getResponseCode();
        InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        String resp = (is != null) ? readStream(is) : "";
        conn.disconnect();

        if (resp.isEmpty()) return new JSONObject();

        JSONObject json = new JSONObject(resp);
        if (code < 200 || code >= 300) {
            String detail = json.has("detail") ? json.optString("detail") : resp;
            throw new Exception("HTTP " + code + ": " + detail);
        }
        return json;
    }
}
