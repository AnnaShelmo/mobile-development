package com.mirea.shelmichas.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onClickShowDialog(View view) {
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "mirea");
    }

    public void onOkClicked() {
        Toast.makeText(this,
                "Вы выбрали \"Иду дальше\"",
                Toast.LENGTH_LONG).show();
    }

    public void onNeutralClicked() {
        Toast.makeText(this,
                "Вы выбрали \"На паузе\"",
                Toast.LENGTH_LONG).show();
    }

    public void onCancelClicked() {
        Toast.makeText(this,
                "Вы выбрали \"Нет\"",
                Toast.LENGTH_LONG).show();
    }

    public void onClickShowTimeDialog(View view) {
        MyTimeDialogFragment dialog = new MyTimeDialogFragment();
        dialog.show(getSupportFragmentManager(), "timeDialog");
    }

    public void onClickShowDateDialog(View view) {
        MyDateDialogFragment dialog = new MyDateDialogFragment();
        dialog.show(getSupportFragmentManager(), "dateDialog");
    }

    public void onClickShowProgressDialog(View view) {
        MyProgressDialogFragment dialog = new MyProgressDialogFragment();
        dialog.show(getSupportFragmentManager(), "progressDialog");
    }
}