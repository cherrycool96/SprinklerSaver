package com.example.michelletjoa.sprinklersaver;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

/**
 * Created by michelletjoa on 4/4/15.
 */
public class HttpActivity{

    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static void main(String[] args) throws IOException {
        HttpActivity example = new HttpActivity();
        String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
        System.out.println(response);
    }


}
