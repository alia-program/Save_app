package com.example.save_app;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Edit_class extends ConstraintLayout implements TextWatcher {
    EditText editText;
    CheckBox checkBox;

    String s;
    Boolean b;

    public Edit_class(@NonNull Context context) {
        super(context);
    }

    public Edit_class(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View.inflate(context,R.layout.edit,this);

        editText = findViewById(R.id.editText);
        checkBox = findViewById(R.id.checkbox);

        editText.addTextChangedListener(this);
    }

    public void setEditText(String string) {
        editText.setText(string);
    }

    public String getEditText() {
        try {
            s = String.valueOf(editText.getText());
        }catch (NullPointerException n){
            n.printStackTrace();
        }
        return s;
    }


    public void setCheckBox(Boolean bool) {
        checkBox.setChecked(bool);
    }

    public Boolean getCheckBox(){
        try {
            b = checkBox.isChecked();
        }catch (NullPointerException n){
            n.printStackTrace();
        }
        return b;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
