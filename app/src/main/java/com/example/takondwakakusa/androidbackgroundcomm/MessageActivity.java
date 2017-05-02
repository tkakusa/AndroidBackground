package com.example.takondwakakusa.androidbackgroundcomm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.takondwakakusa.androidbackgroundcomm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Takondwa Kakusa on 5/1/2017.
 */

public class MessageActivity extends AppCompatActivity {
    Button button;
    TextView textView;
    ServerRestClientUsage serverRestClientUsage;
    private int message_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent i = getIntent();

        String data = i.getExtras().getString("title","");
        textView = (TextView) findViewById(R.id.title);
        textView.setText(data);
        serverRestClientUsage = new ServerRestClientUsage();

        addRefreshButton();
        addLoadButton();
        addSendButton();
    }

    public void addRefreshButton() {
        button = (Button) findViewById(R.id.refreshbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_count = 10;
                serverRestClientUsage.getMostRecent(textView.getText().toString(), new ServerRestClientUsage.Callback<String>() {
                    @Override
                    public void onResponse(String s) throws JSONException {
                        updateListView(s);
                        Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void addLoadButton() {
        button = (Button) findViewById(R.id.loadbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_count += 10;
                serverRestClientUsage.loadMore(textView.getText().toString(), message_count, new ServerRestClientUsage.Callback<String>() {
                    @Override
                    public void onResponse(String s) throws JSONException {
                        updateListView(s);
                        Toast.makeText(getApplicationContext(), "Loaded More Messages", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void addSendButton() {
        button = (Button) findViewById(R.id.sendbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_count = 10;
                EditText editText = (EditText) findViewById(R.id.messagebox);
                String new_message = (String) editText.getText().toString();

                //check for empty message
                if(new_message.equals("")) {
                    errorMessage("ERROR: You can't send an empty message!");
                    return;
                }
                serverRestClientUsage.postMessagebyName(getBaseContext(), textView.getText().toString(), new_message, new ServerRestClientUsage.Callback<String>() {
                    @Override
                    public void onResponse(String s) throws JSONException {
                        serverRestClientUsage.getMostRecent(textView.getText().toString(), new ServerRestClientUsage.Callback<String>() {
                            @Override
                            public void onResponse(String s) throws JSONException {
                                updateListView(s);
                            }
                        });
                    }
                });

                //hide keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //toast notification
                Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();

                //reset the EditText
                editText.setText("");
            }
        });
    }

    public void updateListView(String s) {
        List<String> s_list = new ArrayList<String>();

        JSONObject jsonObject = null;
        JSONArray jsonArray = null;

        try {
            jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = (JSONObject) jsonArray.get(i);
                s_list.add(jsonObject.getString("text"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            errorMessage(s);
        }

        String[] s_array = new String[s_list.size()];
        s_list.toArray(s_array);

        ListView listView = (ListView) findViewById(R.id.messageview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, s_array);
        listView.setAdapter(adapter);
    }

    private void errorMessage(String toThis) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(toThis);
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        AlertDialog alert1 = builder.create();
        alert1.show();
    }
}
