package com.example.takondwakakusa.androidbackgroundcomm;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

import static java.lang.Double.isNaN;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Takondwa Kakusa on 4/17/2017.
 */

@RunWith(AndroidJUnit4.class)
public class BackgroundEventTest {

    private ServerRestClientUsage mRestfulCalls;
    JSONArray channels;
    JSONObject channel;
    JSONArray messages;
    JSONObject message;

    @Before
    public void createCallInstance() {
        mRestfulCalls = new ServerRestClientUsage();
    }
    @Test
    public void useAppContext() throws Exception {
        ServerRestClientUsage serverRestClientUsage;
        serverRestClientUsage = new ServerRestClientUsage();
        String jsonresponse;
        JSONObject jsonObject;
        String channelName = "New Channel";
        String longChannelName = "This channel name is way too long and should not pass the test";
        String messageContent = "New Message";
        String longMessage = "This message is way too long and should not be added because of the sheer length of space it takes up";
        int messageLength;
        /***Test the channel manipulation***/
        //Test getting channels
        serverRestClientUsage.getChannelsTest(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(responseBody, "UTF-8"));
                    assertThat(jsonArray.length(), is(6));
                    Log.d("GetChannel", "Test Passed");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

        });

        serverRestClientUsage.getChannels(new ServerRestClientUsage.Callback<String>() {
            @Override
            public void onResponse(String s) throws JSONException {
                try {
                    Log.d("StringReceived", "String Received: " + s);
                    JSONArray jsonArray = new JSONArray(s);
                    assertThat(jsonArray.length(), is(6));
                    Log.d("GetChannel", "Test Passed");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //Test getting channel by name
        serverRestClientUsage.getChannelbyName("TEST CHANNEL", new ServerRestClientUsage.Callback<String>() {
            @Override
            public void onResponse(String s) throws JSONException {
                Log.d("StringReceived", "String Received: " + s);
                JSONObject jsonObject = new JSONObject(s);
                assertThat(jsonObject.getString("name"), is("TEST CHANNEL"));
            }
        });

        //Test getting posts
        serverRestClientUsage.getMostRecent("TEST CHANNEL", new ServerRestClientUsage.Callback<String>() {
            @Override
            public void onResponse(String s) throws JSONException {
                JSONArray jsonArray = new JSONArray(s);
                assertThat(jsonArray.length(), is(0));
            }
        });

        //Test posting message
        serverRestClientUsage.postMessagebyName(null, "TEST CHANNEL", "Test Message", new ServerRestClientUsage.Callback<String>() {
            @Override
            public void onResponse(String s) throws JSONException {
                Log.d("Integer", "Integer Obtained: " + s);
                //assertThat(Integer.getInteger(s), is(true));
            }
        });

        //Test making sure a post was sent
        serverRestClientUsage.getMostRecent("TEST CHANNEL", new ServerRestClientUsage.Callback<String>() {
            @Override
            public void onResponse(String s) throws JSONException {
                JSONArray jsonArray = new JSONArray(s);
                assertThat(jsonArray.length(), is(1));
            }
        });

        //Post 14 more messages
        for (int i = 0; i < 14; i++) {
            serverRestClientUsage.postMessagebyName(null, "TEST CHANNEL", "Test Message", new ServerRestClientUsage.Callback<String>() {
                @Override
                public void onResponse(String s) throws JSONException {

                }
            });
        }

        //Test that only 10 messages are received
        serverRestClientUsage.getMostRecent("TEST CHANNEL", new ServerRestClientUsage.Callback<String>() {
            @Override
            public void onResponse(String s) throws JSONException {
                JSONArray jsonArray = new JSONArray(s);
                assertThat(jsonArray.length(), is(10));
            }
        });

        //Test that only 15 messages are loaded when asking for 20
        serverRestClientUsage.loadMore("TEST CHANNEL", 20, new ServerRestClientUsage.Callback<String>() {
            @Override
            public void onResponse(String s) throws JSONException {
                JSONArray jsonArray = new JSONArray(s);
                assertThat(jsonArray.length(), is(16));
            }
        });


        //Delete all of the posts
        serverRestClientUsage.deleteMessages("TEST CHANNEL", new ServerRestClientUsage.Callback<String>() {
            @Override
            public void onResponse(String s) throws JSONException {

            }
        });
    }
}
