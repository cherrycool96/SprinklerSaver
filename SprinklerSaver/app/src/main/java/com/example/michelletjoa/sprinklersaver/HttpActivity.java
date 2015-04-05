package com.example.michelletjoa.sprinklersaver;

import android.os.AsyncTask;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

/**
 * Created by michelletjoa on 4/4/15.
 */
public class HttpActivity{

    private final OkHttpClient client = new OkHttpClient();
    public void run(String onOrOff) throws Exception {
        Request request = new Request.Builder()
                .url("http://192.168.2.17:7655/Sprinklers"+onOrOff)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                String respond = response.message();
                System.out.print(respond);
            }
        });
    }
}