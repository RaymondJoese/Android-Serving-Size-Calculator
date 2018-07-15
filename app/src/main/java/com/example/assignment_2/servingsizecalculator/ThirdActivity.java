package com.example.assignment_2.servingsizecalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ThirdActivity extends AppCompatActivity implements TextWatcher {

    private static final String EXTRACT_POT_NAME = "ExtractPotName";
    private static final String EXTRACT_POT_WEIGHT = "ExtractPotWeight";
    private Pot potToBeCal;
    private EditText editText_total_w;
    private EditText editText_num_serving;
    private int food_w = 0;
    private int num_serving = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        extractDataFromIntent();
        initUI();
        updateUI_EditTotalWeight();
        setupBackButton();
    }


    private void initUI() {
        TextView tv_pot_name = (TextView)findViewById(R.id.tv_pot_name);
        tv_pot_name.setText(potToBeCal.getName());

        TextView tv_pot_weight = findViewById(R.id.tv_pot_Weight);
        tv_pot_weight.setText(String.valueOf(potToBeCal.getWeightInG()));
    }

    private void updateUI_EditTotalWeight() {
        editText_total_w = (EditText)findViewById(R.id.et_weight_w_food);
        editText_total_w.addTextChangedListener(ThirdActivity.this);
        editText_num_serving = (EditText)findViewById(R.id.et_num_serving);
        editText_num_serving.addTextChangedListener(ThirdActivity.this);
    }



    private void extractDataFromIntent() {
        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRACT_POT_NAME);
        int weight = intent.getIntExtra(EXTRACT_POT_WEIGHT, 0);
        potToBeCal = new Pot(name, weight);
    }

    private void setupBackButton() {
        Button btn_Back = findViewById(R.id.btnBack);
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public static Intent makeIntent(Context context, Pot aPot) {
        Intent intent = new Intent(context, ThirdActivity.class);
        intent.putExtra(EXTRACT_POT_NAME, aPot.getName());
        intent.putExtra(EXTRACT_POT_WEIGHT, aPot.getWeightInG());
        return intent;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Do nothing
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Do nothing
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void afterTextChanged(Editable editable) {
        TextView tv_foodWeight = (TextView) findViewById(R.id.tv_food_weight);
        TextView tv_servingWeight = (TextView)findViewById(R.id.tv_serving_weight);
        int serving_weight;
        try {
            if (editText_total_w.getText().hashCode() == editable.hashCode()) {
                int total_w = Integer.parseInt(editable.toString());
                int pot_w = potToBeCal.getWeightInG();
                if (total_w >= pot_w) {
                    food_w = total_w - pot_w;
                    tv_foodWeight.setText("" + food_w);
                    if (num_serving != -1 && num_serving != 0){
                        serving_weight = food_w / num_serving;
                        tv_servingWeight.setText(""+serving_weight);
                    }
                } else {
                    food_w = 0;
                    tv_foodWeight.setText("0");
                    tv_servingWeight.setText("-");
                }
            } else if (editText_num_serving.getText().hashCode() == editable.hashCode()) {
                num_serving = Integer.parseInt(editable.toString());
                if (num_serving == 0){
                    Toast.makeText(this, "Ghost Eating?", Toast.LENGTH_SHORT).show();
                    tv_servingWeight.setText("-");
                }else if (food_w > 0){
                    serving_weight = food_w / num_serving;
                    tv_servingWeight.setText(""+serving_weight);
                } else {
                    tv_servingWeight.setText("0");
                }
            }
        } catch (NumberFormatException except){
            tv_foodWeight.setHint("Weight w/ food (g)");
        }
    }
}
