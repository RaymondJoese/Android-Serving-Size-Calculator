package com.example.assignment_2.servingsizecalculator;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Vector;

// Array of options -> ArrayAdpter -> ListView

// List view: {views: da_items.}

public class MainActivity extends AppCompatActivity {
//    public  static final String SAVED_POTS = "Saved_Pots_Collection";
    private PotCollection pc = new PotCollection();
    private static final int REQUEST_CODE_GETPOT = 1116;
    private static final int REQUEST_CODE_EditPot = 2018;
    private int indexOfEditPot;
    private SharedPreferences savedPotPreference;
    private Gson gson;
    private Vector<Pot> vPots;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Restore preferences:
        gson = new Gson();
        savedPotPreference = getPreferences(0);
        String json = savedPotPreference.getString("myPots", "");
        pc = gson.fromJson(json, new TypeToken<PotCollection>(){}.getType());

        // launch Second Activity (add pot)
        setupAddPotButton();
        // Build adapter and config the ListView
        populateListView();
        // launch Third Activity (calculate pot) & pass the corresponding pot in
        registerClickCallback();
        // longClick open submenu for editing and deletion
        setupPotEditAndDelete();
    }


    private void populateListView() {
        // Create List of Pots (initial Pots for testing)
//        Pot A = new Pot("Big Fry Pan", 206);
//        Pot B = new Pot("Huge Pot", 1002);
//        pc.addPot(A);
//        pc.addPot(B);

        // Build Adapter
        String[] myPotCollection = pc.getPotDescriptions();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,           // Context for activity
                R.layout.added_pots,    // Layout ot use (create)
                myPotCollection);       //  Items to be diesplayed

        // Configure the list view
        ListView list = findViewById(R.id.listViewPots);
        list.setAdapter(adapter);
    }

    private void setupAddPotButton() {
        Button btn = findViewById(R.id.btnAddPot);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SecondActivity.makeIntent(MainActivity.this);
//                startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_GETPOT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_EditPot:
                if (resultCode == Activity.RESULT_OK){
                    // Extract the edited pot
                    Pot edited_pot = getPotFromIntent(data);
                    // Replace the original one
                    pc.changePot(edited_pot, indexOfEditPot);
                    populateListView();
                    break;
                }
            case REQUEST_CODE_GETPOT:
                if (resultCode == Activity.RESULT_OK) {
                    // Extract the new pot
                    Pot new_Pot = getPotFromIntent(data);
                    // Add to POT Collection
                    pc.addPot(new_Pot);
                    populateListView();
                    Log.i("MyApp", "Result Pot name: " + new_Pot.getName() + " with " + new_Pot.getWeightInG() + "g.");
                    break;
                } else {
                    Log.i("MyApp", "Activity Canceled");
                }
        }
    }

    private Pot getPotFromIntent(Intent data) {
        // Get the name and weight of pot
        String pot_name = data.getStringExtra("InputStringName");
        int pot_weight = data.getIntExtra("InputIntWeight", 0);
//                    int pot_weight = Integer.parseInt(pot_weight_string);

        return new Pot(pot_name, pot_weight);
    }


    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.listViewPots);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Pot potTobeCalculated = pc.getPot(position);
                Intent intent = ThirdActivity.makeIntent(MainActivity.this, potTobeCalculated);
                startActivity(intent);
            }
        });
    }

    private void setupPotEditAndDelete() {
        ListView list = (ListView)findViewById(R.id.listViewPots);
        registerForContextMenu(list);


        // good for edit only when long press item in ListView
//        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
//                Pot potToBeEdit = pc.getPot(position);
//                indexOfEditPot = position;
//                Intent intent = SecondActivity.makeEditIntent(MainActivity.this, potToBeEdit);
//                startActivityForResult(intent, REQUEST_CODE_EditPot);
//                return true;
//            }
//        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.long_click_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_edit:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Pot potToBeEdit = pc.getPot(info.position);
                indexOfEditPot = info.position;
                Intent intent = SecondActivity.makeEditIntent(MainActivity.this, potToBeEdit);
                startActivityForResult(intent, REQUEST_CODE_EditPot);
                return true;
            case R.id.menu_del:
                AdapterView.AdapterContextMenuInfo other_info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                pc.delPot(other_info.position);
                populateListView();
                return true;
            default:
                return super.onContextItemSelected(item);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save data on sharePreference
        savedPotPreference = getPreferences(0);
        SharedPreferences.Editor pcEditor = savedPotPreference.edit();
        gson = new Gson();
        String json = gson.toJson(pc);
        pcEditor.putString("myPots", json);
        pcEditor.apply();


    }
}

