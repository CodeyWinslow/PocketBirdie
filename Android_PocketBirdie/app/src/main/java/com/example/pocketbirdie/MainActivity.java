package com.example.pocketbirdie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String DEBUG_TAG = "ACTIVITY_MAIN";
    ImageButton AddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWidgets();
    }

    private void getWidgets()
    {
        AddButton = findViewById(R.id.main_add_button);
        AddButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.main_add_button:
                Log.d(DEBUG_TAG, "button clicked!");
        }
    }
}