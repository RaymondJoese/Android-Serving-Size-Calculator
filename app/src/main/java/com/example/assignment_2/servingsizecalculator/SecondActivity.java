package com.example.assignment_2.servingsizecalculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.PrivateKey;

public class SecondActivity extends AppCompatActivity {

    private static final String EXTRA_PotName = "com.example.assignment_2.servingsizecalculator; - the Pot Name";
    private static final String EXTRA_PotWeight = "com.example.assignment_2.servingsizecalculator; - the Pot Weight";

    private Pot potToBeEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if (getIntent().getExtras() != null){
            extractDataFromIntent();
            editPotUI();
        }

        setupOkButton();
        setupCancelAddingPot();
    }



    private void extractDataFromIntent() {
        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRA_PotName);
        int weight = intent.getIntExtra(EXTRA_PotWeight, 0);
        potToBeEdit = new Pot(name, weight);
    }


    @SuppressLint("SetTextI18n")
    private void editPotUI() {
        EditText name = findViewById(R.id.et_name);
        name.setText(potToBeEdit.getName());
        EditText weight = findViewById(R.id.et_weight);
        weight.setText(""+ potToBeEdit.getWeightInG());
    }

    private void setupOkButton() {
        Button btn_OK = findViewById(R.id.btnOK);
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Extract data from the UI:
                String pot_name;
                int pot_weight;
                try {
                    EditText edit_name =  findViewById(R.id.et_name);
                    pot_name = edit_name.getText().toString();
                    if (pot_name.length() == 0){
                        Toast.makeText(getApplicationContext(), "Empty Name  Not Allowed!" , Toast.LENGTH_SHORT).show();
                        return;
                    }

                    EditText edit_weight =  findViewById(R.id.et_weight);
                    String pot_weight_string = edit_weight.getText().toString();
                    pot_weight = Integer.parseInt(pot_weight_string);
                } catch (IllegalArgumentException except) {
                    Toast.makeText(getApplicationContext(), "Empty Weight  Not Allowed!" , Toast.LENGTH_SHORT).show();
                    return;
                }

                // Pass data back:
                Intent intent = new Intent();
                intent.putExtra("InputStringName", pot_name);
                intent.putExtra("InputIntWeight", pot_weight);
                setResult(Activity.RESULT_OK, intent);

                finish();

            }
        });

    }

    private void setupCancelAddingPot() {
        Button btn = findViewById(R.id.btnCancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, SecondActivity.class);
    }

    public static Intent makeEditIntent(Context context, Pot aPot) {
        Intent intent = new Intent(context, SecondActivity.class);
        intent.putExtra(EXTRA_PotName, aPot.getName());
        intent.putExtra(EXTRA_PotWeight, aPot.getWeightInG());
        return intent;
    }
}
