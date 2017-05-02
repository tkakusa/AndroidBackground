package com.example.takondwakakusa.androidbackgroundcomm;

import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Takondwa Kakusa on 4/17/2017.
 */

@RunWith(AndroidJUnit4.class)
public class BackgroundEventTest {
    /*
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
        String jsonresponse;
        JSONObject jsonObject;
        String channelName = "New Channel";
        String longChannelName = "This channel name is way too long and should not pass the test";
        String messageContent = "New Message";
        String longMessage = "This message is way too long and should not be added because of the sheer length of space it takes up";
        /***Test the creation of a new channel***/
    /*
        jsonresponse = mRestfulCalls.createChannel(channelName);
        jsonObject = (JSONObject) new JSONObject(jsonresponse);

        //Check that the channel was accepted
        assertThat((int)jsonObject.get("code"), is(200));

        //Add a channel name that is too long
        jsonresponse = mRestfulCalls.createChannel(longChannelName);
        jsonObject = (JSONObject) new JSONObject(jsonresponse);

        //Check that the channel was denied
        assertThat((int)jsonObject.get("code"), is(400));

        //Add a channel name that already exists
        jsonresponse = mRestfulCalls.createChannel(channelName);
        jsonObject = (JSONObject) new JSONObject(jsonresponse);

        //Check that the channel was denied
        assertThat((int)jsonObject.get("code"), is(409));


        /***Test Getting the channel back***/
    /*
        jsonresponse = mRestfulCalls.getChannels();
        jsonObject = (JSONObject) new JSONObject(jsonresponse);
        channels = jsonObject.getJSONArray("channels");

        //Make sure the length of the list is correct
        assertThat(channels.length(), is(1));

        //Make sure the response type and name are correct
        channel = (JSONObject) channels.get(0);
        assertThat((int)jsonObject.get("code"), is(200));
        assertThat(channel.getString("name"), is(channelName));

        /***Test Sending a new message***/
    /*
        //Send a single message
        jsonresponse = mRestfulCalls.sendMessage(channelName, messageContent);
        jsonObject = (JSONObject) new JSONObject(jsonresponse);

        //Make sure response type is correct
        assertThat((int)jsonObject.get("code"), is(200));

        //Send a long message
        jsonresponse = mRestfulCalls.sendMessage(channelName, longMessage);
        jsonObject = (JSONObject) new JSONObject(jsonresponse);

        //Make sure response type is correct
        assertThat((int)jsonObject.get("code"), is(400));

        /***Test Getting a the most recent messages***/
    /*
        //Post four more messages
        mRestfulCalls.sendMessage(channelName, messageContent + "1");
        mRestfulCalls.sendMessage(channelName, messageContent + "2");
        mRestfulCalls.sendMessage(channelName, messageContent + "3");
        mRestfulCalls.sendMessage(channelName, messageContent + "4");

        //Get the most recent messages
        jsonresponse = mRestfulCalls.getMostRecent(channelName);
        jsonObject = (JSONObject) new JSONObject(jsonresponse);
        messages = jsonObject.getJSONArray("messages");

        //check the length of the message array
        assertThat(messages.length(), is(5));

        //Check that each message is in order
        for (int i = 0; i < 4; i++) {
            message = (JSONObject) messages.get(i);
            assertThat(message.getString("body"), is(messageContent + Integer.toString(4-i)));
            assertThat(message.getString("id"), is(Integer.toString(4-i)));
        }

        //add six more message
        mRestfulCalls.sendMessage(channelName, messageContent + "5");
        mRestfulCalls.sendMessage(channelName, messageContent + "6");
        mRestfulCalls.sendMessage(channelName, messageContent + "7");
        mRestfulCalls.sendMessage(channelName, messageContent + "8");
        mRestfulCalls.sendMessage(channelName, messageContent + "9");
        mRestfulCalls.sendMessage(channelName, messageContent + "0");

        //make sure only the most recent 10 are obtained
        jsonresponse = mRestfulCalls.getMostRecent(channelName);
        jsonObject = (JSONObject) new JSONObject(jsonresponse);
        messages = jsonObject.getJSONArray("messages");

        //check the length of the message array
        assertThat(messages.length(), is(10));

        /***Test loading previous messages***/
    /*
        jsonresponse = mRestfulCalls.loadMore(channelName, 11);
        jsonObject = (JSONObject) new JSONObject(jsonresponse);
        messages = jsonObject.getJSONArray("messages");

        //check the length of the message array
        assertThat(messages.length(), is(10));

        for (int i = 0; i < 9; i++) {
            message = (JSONObject) messages.get(i);
            assertThat(message.getString("body"), is(messageContent + Integer.toString(9-i)));
            assertThat(message.getString("id"), is(Integer.toString(10-i)));
        }

    }
*/
}
