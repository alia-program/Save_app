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

    int index = 1;

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

        //ボタン類
        Button reset = findViewById(R.id.reset);
        reset.setOnClickListener(reset_);
        Button button = findViewById(R.id.Save_button);
        button.setOnClickListener(save_Button);
        ImageButton imageButton = findViewById(R.id.imagebutton);
        imageButton.setOnClickListener(add_Button);

        //起動時にファイルの作成
        create_Files();
    }

    //Log.d("index", String.valueOf(index));
    //Log.d("index j", String.valueOf(jsonArray.length()));

    //リセットボタン(仮)
    View.OnClickListener reset_ = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences.Editor editor = preferences.edit();
            index = 0;
            editor.putInt("index",0);
            editor.apply();

            try (FileWriter writer = new FileWriter(json_File)) {
                writer.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    //セーブボタン(仮)
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

    //追加ボタン
    View.OnClickListener add_Button = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            add_ItemView("aaaaaa",false,0,index);
            index++;
            Log.d("index", String.valueOf(index));
        }
    };

    //アプリから離れたとき
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




    //Indexの数値保存
    public void save_index(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("index",index);
        editor.apply();
    }

    //Json形式の保存
    public void save_JsonArray(JSONArray array){
        try (FileWriter writer = new FileWriter(json_File)) {
            writer.write(array.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //保存したJsonファイルを取り出す
    public String get_JsonArray(){
        String json_string = "empty";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(json_File));
            json_string = bufferedReader.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json_string;
    }

    //開始時のアイテムセット
    public void setItem() throws JSONException {
        jsonArray = new JSONArray(get_JsonArray());
        object.put("Box",jsonArray);
        for (int i = 0; i < index; i++){
            try {
                JSONObject json_item = jsonArray.getJSONObject(i);

                String s = json_item.getString("EditText");
                Boolean b = json_item.getBoolean("checkBox");

                add_ItemView(s ,b,1,i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Jsonの作成
    public void add_JsonArray() throws JSONException {
        Log.d("add_json index", String.valueOf(index));
        Log.d("add_json json", String.valueOf(jsonArray.length()));
        if (jsonArray.length() >= 0 && index >= 0){//JsonFileが保存できていない場合、indexとjsonArrayの数が一致しないためエラー
            for (int view_i = 0; view_i < index ; view_i++){

                JSONObject json_item = new JSONObject();

                Edit_class ed= layout.findViewById(view_i);
                Log.d("error", String.valueOf(view_i));
                //アイテム作成
                json_item.put("EditText",ed.getEditText());
                json_item.put("checkBox",ed.getCheckBox());
                //アイテムを配列に入れる
                jsonArray.put(view_i,json_item);

            }
            //オブジェクトに配列を入れる
            object.put("Box",jsonArray);
            Log.d("aaaa", String.valueOf(object.length()));

            //indexの保存
            save_index();
            save_JsonArray(jsonArray);

        }else {
            Toast.makeText(getApplicationContext() , "ファイルの欠損により保存に失敗しました。", Toast.LENGTH_LONG).show();
            Log.d("error","Jsonファイルが破損しました");
        }

    }

    //viewの生成
    private void add_ItemView(String s,Boolean b,int i,int id){
        //itemのインスタンスを作成して追加
        edit = new Edit_class(getApplicationContext(),null);
        layout.addView(edit);

        //引数を値に
        edit.setEditText(s);
        edit.setCheckBox(b);
        if (i == 0){//スタート用
            edit.setId(index);
            Log.d("error", String.valueOf(index));
        }else {//再開用
            edit.setId(id);
        }
    }

    //ファイルの作成
    private void create_Files(){
        //indexFileの作成
        preferences = getSharedPreferences("my_settings", Context.MODE_PRIVATE);
        index = preferences.getInt("index",0);

        json_File = new File(getApplicationContext().getFilesDir(),file_Name);
        //JsonFileの作成
        try {
            object = new JSONObject();
            if (index == 0){
                jsonArray = new JSONArray();
            }else {
                setItem();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}