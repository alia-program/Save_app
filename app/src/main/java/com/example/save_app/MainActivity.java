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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Edit_class edit;

    int index = 0;
    int number = index+1;
    int json_index;

    JSONObject object;
    JSONArray jsonArray;
    LinearLayout layout;

    String file_Name = "saveTodo.json";
    File json_File;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        //indexFileの作成
        preferences = getSharedPreferences("my_settings", Context.MODE_PRIVATE);

        index = preferences.getInt("index",0);
        json_index = jsonArray.length();
        Log.d("index", String.valueOf(index));
        Log.d("index j", String.valueOf(json_index));

        setItem();

        json_File = new File(getApplicationContext().getFilesDir(),file_Name);

    }

    View.OnClickListener save_Button = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                add_JsonArray();
                Log.d("error", "Ok");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("error", "saveButton");
            }
        }
    };

    View.OnClickListener add_Button = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            add_ItemView("aaaaaa",false);
            Log.d("index", String.valueOf(index));
            index++;
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        try {
            add_JsonArray();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("index",index);
        editor.apply();
    }


    //Jsonの作成
    public void add_JsonArray() throws JSONException {
        Log.d("add_json s", String.valueOf(index));
        if (0 != jsonArray.length() && 0 < index){//JsonFileが保存できていない場合、indexとjsonArrayの数が一致しないためエラー

            for (int view_i = 0; view_i < index ; view_i++){

                Edit_class ed= (Edit_class) layout.findViewById(view_i);
                //アイテム作成
                JSONObject json_item = new JSONObject();
                json_item.put("EditText",ed.getEditText());
                json_item.put("checkBox",ed.getCheckBox());
                //アイテムを配列に入れる
                jsonArray.put(view_i,json_item);

            }
            //オブジェクトに配列を入れる
            object.put("json_array" + index,edit);
            Log.d("aaaa",jsonArray.toString(2));

            //indexの保存
            save_index();

        }else {
            Toast.makeText(getApplicationContext() , "ファイルの欠損により保存に失敗しました。", Toast.LENGTH_LONG).show();
            Log.d("error","Jsonファイルが破損しました");
        }

    }

    //開始時のアイテムセット
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
        if (json_index == 0){
            for (int i = 0; i < index; i++){
                add_ItemView("aaa",true);
            }
        }
    }

    //アイテムの生成
    private void add_ItemView(String s,Boolean b){
        //itemのインスタンスを作成して追加
        edit = new Edit_class(getApplicationContext(),null);
        layout.addView(edit);

        //引数を値に
        edit.setEditText(s);
        edit.setCheckBox(b);
        edit.setId(index);
        Log.d("Id", String.valueOf(edit.getId()));
    }

    //Indexの数値保存
    public void save_index(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("index",index);
        editor.apply();
    }



        public void save_JsonArray(JSONArray jsonArray){
        try (FileWriter writer = new FileWriter(json_File)) {
            writer.write(String.valueOf(jsonArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONArray get_JsonArray(){
        JSONArray getJson;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(json_File));
            getJson = bufferedReader.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getJson;
    }







}