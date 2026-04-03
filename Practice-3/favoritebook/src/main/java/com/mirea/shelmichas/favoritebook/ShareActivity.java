package com.mirea.shelmichas.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        TextView textView = findViewById(R.id.textViewBook);
        editText = findViewById(R.id.editTextBook);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String book = extras.getString(MainActivity.KEY);
            textView.setText(String.format("Любимая книга разработчика – %s", book));
        }
    }

    public void sendResult(View view) {

        String text = editText.getText().toString();

        Intent data = new Intent();
        data.putExtra(MainActivity.USER_MESSAGE, text);

        setResult(Activity.RESULT_OK, data);
        finish();
    }
}