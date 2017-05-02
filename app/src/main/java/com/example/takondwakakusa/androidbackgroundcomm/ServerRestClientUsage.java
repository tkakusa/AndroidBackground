package com.example.takondwakakusa.androidbackgroundcomm;

import android.content.Context;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Takondwa Kakusa on 4/29/2017.
 */



public class ServerRestClientUsage {

    int fullLength = 0;

    public interface Callback<T> {
        void onResponse(T t) throws JSONException;
    }

    public void getChannels(final Callback<String> callback) {

        final String[] output = {new String()};
        String url = "channels";
        RestfulCalls restfulCalls = new RestfulCalls();
        HttpResponse response = null;

        RestfulCalls.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                if (callback != null) {
                    try {
                        callback.onResponse(response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                if (callback != null) {
                    try {
                        callback.onResponse(timeline.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        });

    }


    public void getChannel(int cid, final Callback<String> callback) {
        String url = "channels/" + Integer.toString(cid);
        RestfulCalls.get(url, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                if (callback != null) {
                    try {
                        callback.onResponse(response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                if (callback != null) {
                    try {
                        callback.onResponse(timeline.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getChannelbyName(final String channelName, final Callback<String> callback) {
        fullLength = 0;
        getChannels(new Callback<String>() {
            @Override
            public void onResponse(String s) throws JSONException {
                final JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    getChannel(Integer.parseInt(jsonArray.get(i).toString()), new Callback<String>() {
                        @Override
                        public void onResponse(String s) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(s);
                                String name = jsonObject.getString("name");
                                if ( name.equals( channelName )) {
                                    if (callback != null) {
                                        try {
                                            callback.onResponse(s);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                else {
                                    if (increment(jsonArray.length())) {
                                        if (callback != null) {
                                            try {
                                                callback.onResponse("Channel Could Not Be Found");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    public boolean increment(int length) {
        fullLength = fullLength + 1;
        if (fullLength == length) {
            return true;
        }
        return false;
    }

    public void getMostRecent(String channelName, final Callback<String> callback) {
        getChannelbyName(channelName, new Callback<String>() {
            @Override
            public void onResponse(String s) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonObject1 = new JSONArray();
                    JSONArray jsonArray = jsonObject.getJSONArray("posts");
                    if (jsonArray.length() >= 10) {
                        for (int i = jsonArray.length() - 10; i < jsonArray.length(); i++) {
                            jsonObject1.put(jsonArray.get(i));
                        }
                        if (callback != null) {
                            try {
                                callback.onResponse(jsonObject1.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (callback != null) {
                            try {
                                callback.onResponse(jsonArray.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        callback.onResponse(s);
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
    }
    public void loadMore(String channelName, final int latestLoaded, final Callback<String> callback) {
        getChannelbyName(channelName, new Callback<String>() {
            @Override
            public void onResponse(String s) throws JSONException {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonObject1 = new JSONArray();
                JSONArray jsonArray = jsonObject.getJSONArray("posts");
                if (jsonArray.length() >= latestLoaded) {
                    for (int i = jsonArray.length() - latestLoaded; i < jsonArray.length(); i++) {
                        jsonObject1.put(jsonArray.get(i));
                    }
                    if (callback != null) {
                        try {
                            callback.onResponse(jsonObject1.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (callback != null) {
                        try {
                            callback.onResponse(jsonArray.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public static void postMessage(Context context, int cid, String message, final Callback<String> callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("text", message);
            String url = "channels/" + Integer.toString(cid) + "/posts";
            StringEntity entity = new StringEntity(jsonObject.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));

            RequestParams rp = new RequestParams();
            rp.add("desc", message);
            RestfulCalls.post(context, url, entity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (callback != null) {
                        try {
                            callback.onResponse(new String(responseBody, "UTF-8"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void postMessagebyName(final Context context, final String channelName, final String message, final Callback<String> callback) {

        getChannels(new Callback<String>() {
            @Override
            public void onResponse(String s) throws JSONException {
                final JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    getChannel(Integer.parseInt(jsonArray.get(i).toString()), new Callback<String>() {
                        @Override
                        public void onResponse(String s) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(s);
                                String name = jsonObject.getString("name");
                                if ( name.equals( channelName )) {
                                    String cid = jsonObject.getString("id");
                                    String posturl = "channels/" + cid + "/posts";
                                    jsonObject.put("text", message);

                                    StringEntity entity = new StringEntity(jsonObject.toString());
                                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));

                                    RequestParams rp = new RequestParams();
                                    rp.add("desc", message);

                                    RestfulCalls.post(context, posturl, entity, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            if (callback != null) {
                                                try {
                                                    callback.onResponse(new String(responseBody, "UTF-8"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        }
                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

/*
    public static String updateMessage(int cid, int pid, String postName, String message, final Callback<String> callback) {
        final String[] output = new String[1];
        RequestParams rp = new RequestParams();
        rp.add("name", postName);
        rp.add("desc", message);

        String url = "channels/" + Integer.toString(cid) + "/posts" + Integer.toString(pid);
        RestfulCalls.put(url, rp, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (callback != null) {
                    try {
                        callback.onResponse(new String(responseBody, "UTF-8"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("PutResponse", "---------------- this is response : " + output[0]);
                Log.d("PutStatus", "---------------- this is status : " + statusCode);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

        });

        return output[0];
    }
    */
    /*
    public static String createChannel(String channelName, String channelDesc) {
        RequestParams rp = new RequestParams();
        rp.add("name", channelName);
        rp.add("desc", channelDesc);

        String url = "channels";
        RestfulCalls.post(url, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Log.d("ArrayResponse", "---------------- this is response : " + response);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.d("ArrayFailure", "---------------- this is response : " + response);
            }

        });
        return "done";
    }
    */
/*
    public String deleteChannel(final String channelName) {
        final String[] channels = new String[1];
        final String[] output = {new String()};
        try {
            getChannels(new Callback<String>() {
                @Override
                public void onResponse(String s) throws JSONException {
                    boolean found = false;
                    int cid = 0;
                    channels[0] = s;
                    Log.d("Channels", "---------------- these are the channels : " + channels[0]);
                    JSONArray jsonArray = new JSONArray(channels[0]);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String channel = getChannel((int) jsonArray.get(i), new Callback<String>() {
                            @Override
                            public void onResponse(String s) throws JSONException {

                            }
                        });
                        JSONObject jsonObject = new JSONObject(channel);
                        if ( channelName == jsonObject.getString("name")) {
                            found = true;
                            cid = (int) jsonObject.get("id");
                            Log.d("CIDFound", "---------------- this is cid : " + cid);
                            break;
                        }
                    }
                    if (found) {
                        String url = "/channels/" + Integer.toString(cid);
                        RestfulCalls.delete(url, null, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    output[0] = new String(responseBody, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                Log.d("PutResponse", "---------------- this is response : " + output[0]);
                                Log.d("PutStatus", "---------------- this is status : " + statusCode);
                            }
                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }

                        });
                    }
                    else {
                        output[0] = "NOT FOUND";
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output[0];
    }
*/
    /*
    public String deletePost(final String channelName, int pid) {
        final String[] channels = new String[1];
        final String[] output = {new String()};
        try {
            getChannels(new Callback<String>() {
                @Override
                public void onResponse(String s) throws JSONException {
                    boolean found = false;
                    int cid = 0;
                    channels[0] = s;
                    Log.d("Channels", "---------------- these are the channels : " + channels[0]);
                    JSONArray jsonArray = new JSONArray(channels[0]);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String channel = getChannel((int) jsonArray.get(i), new Callback<String>() {
                            @Override
                            public void onResponse(String s) throws JSONException {

                            }
                        });
                        JSONObject jsonObject = new JSONObject(channel);
                        if ( channelName == jsonObject.getString("name")) {
                            found = true;
                            cid = (int) jsonObject.get("id");
                            Log.d("CIDFound", "---------------- this is cid : " + cid);
                            break;
                        }
                    }
                    if (found) {
                        String url = "/channels/" + Integer.toString(cid);
                        RestfulCalls.delete(url, null, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    output[0] = new String(responseBody, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                Log.d("PutResponse", "---------------- this is response : " + output[0]);
                                Log.d("PutStatus", "---------------- this is status : " + statusCode);
                            }
                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }

                        });
                    }
                    else {
                        output[0] = "NOT FOUND";
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output[0];
    }
    */
}
