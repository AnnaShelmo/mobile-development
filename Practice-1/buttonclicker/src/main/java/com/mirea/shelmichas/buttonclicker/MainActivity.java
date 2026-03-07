package com.mirea.shelmichas.buttonclicker;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

    private TextView textViewStudent;
    private Button btnWhoAmI;
    private Button btnItIsNotMe;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStudent = findViewById(R.id.textViewStudent);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        btnItIsNotMe = findViewById(R.id.btnItIsNotMe);
        checkBox = findViewById(R.id.checkBox);

        // Первый способ
        View.OnClickListener oclBtnWhoAmI = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewStudent.setText("Мой номер по списку № Х");
                checkBox.setChecked(true);
            }
        };

        btnWhoAmI.setOnClickListener(oclBtnWhoAmI);

        // Второй способ
        btnItIsNotMe.setOnClickListener(v -> {
            textViewStudent.setText("Это не я сделал");
            checkBox.setChecked(false);
        });
    }
}