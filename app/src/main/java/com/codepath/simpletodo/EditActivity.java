package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    String updatedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        EditText etUpdatedItem = (EditText) findViewById(R.id.updateText);
        etUpdatedItem.setText(getIntent().getStringExtra("itemText"));
    }

    public void onUpdateItem(View v) {
        EditText etUpdatedItem = (EditText) findViewById(R.id.updateText);
        Intent data = new Intent();
        data.putExtra("itemText", etUpdatedItem.getText().toString());
        data.putExtra("itemIndex", getIntent().getIntExtra("itemIndex", -1));
        setResult(RESULT_OK, data);
        finish();
    }
}
