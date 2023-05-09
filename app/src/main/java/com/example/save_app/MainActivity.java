package com.example.save_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Edit_class edit;

    int index = 0;
    int json_index;

    JSONObject object;
    JSONArray jsonArray;
    LinearLayout layout;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getApplicationContext().getSharedPreferences("my_settingsa", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        index = preferences.getInt("index",0);



        layout = findViewById(R.id.linerLayout);

        Button button = findViewById(R.id.Save_button);
        button.setOnClickListener(save_Button);

        ImageButton imageButton = findViewById(R.id.imagebutton);
        imageButton.setOnClickListener(add_Button);

        //JsonFileの作成
        try {
            object = new JSONObject("{\"box\":\"null\"}");
            jsonArray = new JSONArray();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //
        json_index = jsonArray.length();
        Log.d("index", String.valueOf(index));
        Log.d("index j", String.valueOf(json_index));

        setItem();

        /*add_View
        for (int in = 0; in < i; in++){
            Edit_class edit_class = new Edit_class(getApplicationContext(),null);
            edit_class.setId('e' + i);
            layout.addView(edit_class);
            Log.d("aaa", String.valueOf(i));
        }
        */

    }





    View.OnClickListener save_Button = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                add_JsonArray();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener add_Button = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            add_ItemView("aaaaaa",false);
            index++;
            Log.d("index", String.valueOf(index));
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            add_JsonArray();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("ttt", jsonArray.toString());

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("index",index);
        editor.apply();
    }




    //Jsonの作成・保存
    public void add_JsonArray() throws JSONException {
            for (int view_i = 0; view_i <= index; view_i++){

                Edit_class ed= findViewById(view_i);

                //アイテム作成
                JSONObject json_item = new JSONObject();
                json_item.put("EditText",ed.getEditText());
                json_item.put("checkBox",ed.getCheckBox());

                /*
                *
                */
                Log.d("aaaa", ed.getEditText());
                Log.d("aaaa", json_item.getString("EditText"));
                Log.d("aaaa", String.valueOf(json_item.getBoolean("checkBox")));

                //アイテムを配列に入れる
                jsonArray.put(json_item);

            }
        //オブジェクトに配列を入れる
        object.put("json_array" + index,edit);

        Log.d("aaaaaaaa", jsonArray.toString());
    }

    public void setItem() {
        for (int i = 0; i < json_index; i++){
            try {
                JSONObject getJson = jsonArray.getJSONObject(i);
                String s = getJson.getString("EditText");
                Boolean b = getJson.getBoolean("checkBox");

                add_ItemView(s ,b);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //保存前用
        if (json_index <= 0){
            for (int i = 0; i <= index; i++){
                add_ItemView("aaa",true);

                edit.setId(i);
                Log.d("indextag", String.valueOf(edit.getTag()));
            }
        }


    }

    private void add_ItemView(String s,Boolean b){
        edit = new Edit_class(getApplicationContext(),null);
        layout.addView(edit);

        edit.setEditText(s);
        edit.setCheckBox(b);

    }

}