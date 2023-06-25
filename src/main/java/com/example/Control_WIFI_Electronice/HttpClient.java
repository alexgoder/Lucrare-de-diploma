package com.example.Control_WIFI_Electronice;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;


public class HttpClient extends MainActivity {
    private int red=0;
    private int green=0;
    private int blue=0;
    String baseUrl="http://192.168.4.1:80/RGB";
    public HttpClient(){}

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public void sendRequest() throws URISyntaxException {
        final String URL = composePostUrl();//"http://192.168.4.1:80/RGB?red=123&green=0&blue=123";


        HashMap<String, String> params = new HashMap<String, String>();

        JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public void sendReqCentrala(int temp){
        final String URL = composePostUrlPid(temp);
        HashMap<String, String> params = new HashMap<String, String>();

        JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public void sendReqSemafor(int red,int green,int blue){
        final String URL = composePostUrlSemafor(red,green,blue);
        HashMap<String, String> params = new HashMap<String, String>();

        JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    private String composePostUrlPid(int temp) {
        String rtn= "http://192.168.4.1:80/PID?temp="+String.valueOf(temp);
        return rtn;
    }

    private String composePostUrlSemafor(int red,int green,int yellow) {
        String rtn= "http://192.168.4.1:80/semafor?red="+String.valueOf(red)+"&green="+String.valueOf(green)+"&yellow="+String.valueOf(yellow);
        return rtn;
    }

    public String getRequest(String url) throws URISyntaxException {
        final String URL = "http://192.168.4.1:80/"+url;
        final String[] resp = {""};
// pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            resp[0] +=response.toString();
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
        return resp[0].toString();
    }

    private String givePropperLength(int color){
        String finalStr="";
        if(color>9 && color<100)
        {
            finalStr+="0"+String.valueOf(color);
        }
        else
        {
            if(color<10)
                finalStr+="00"+String.valueOf(color);
            else
                finalStr+=String.valueOf(color);
        }
        return finalStr;
    }

    private String composePostUrl()
    {
        String finalUrl=baseUrl;
        String sRed,sGreen,sBlue;
        sRed=givePropperLength(red);
        sGreen=givePropperLength(green);
        sBlue=givePropperLength(blue);
        finalUrl+="?red="+sRed+"&green="+sGreen+"&blue="+sBlue;
        return finalUrl;
    }

    public InputStream getReq1(String addOn) throws IOException {
        InputStream str;
        URL url = new URL("http://192.168.4.1:80/"+addOn);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //readStream(in);
            str=in;
        } finally {
            urlConnection.disconnect();
        }

        return str;
    }


}


