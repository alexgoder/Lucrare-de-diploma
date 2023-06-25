package com.example.Control_WIFI_Electronice;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

class PostData extends AsyncTask<String, Void, String> {



    @Override
    protected String doInBackground(String... strings) {
        try {

            // on below line creating a url to post the data.
            URL url = new URL("http://192.168.4.1:80/RGB");

            // on below line opening the connection.
            HttpURLConnection client = (HttpURLConnection) url.openConnection();

            // on below line setting method as post.
            client.setRequestMethod("POST");

            // on below line setting content type and accept type.
            client.setRequestProperty("Content-Type", "application/form-data");
            client.setRequestProperty( "charset", "utf-8");
            client.setRequestProperty("Accept", "application/form-data");


            // on below line setting client.
            client.setDoOutput(true);

            // on below line we are creating an output stream and posting the data.
            try (OutputStream os = client.getOutputStream()) {
                byte[] input = strings[0].getBytes(Charset.forName("UTF-8"));
                os.write(input, 0, input.length);
            }

            // on below line creating and initializing buffer reader.
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(client.getInputStream(), Charset.forName("UTF-8")))) {

                // on below line creating a string builder.
                StringBuilder response = new StringBuilder();

                // on below line creating a variable for response line.
                String responseLine = null;

                // on below line writing the response
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // on below line displaying a toast message.
                //Toast.makeText(MainActivity, "Data has been posted to the API.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

            // on below line handling the exception.
            e.printStackTrace();
            //Toast.makeText(MainActivity.this, "Fail to post the data : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
