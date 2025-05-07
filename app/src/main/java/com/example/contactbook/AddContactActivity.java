package com.example.contactbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class AddContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //################################################################

        // Navigere til MainActivity
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //################################################################

        // Gemmer nye kontakter
        Button addBtn = findViewById(R.id.btn_add); //Definere knappen til at tilføje nye kontakter

        addBtn.setOnClickListener(v -> {

            EditText nameInput = findViewById(R.id.inp_name); // Henter name input
            EditText phoneInput = findViewById(R.id.inp_phone); // Henter phone input
            EditText mailInput = findViewById(R.id.inp_mail); // henter email input

            String name = nameInput.getText().toString(); // Konverter nameInput til String
            String phone = phoneInput.getText().toString(); // Konverter phoneInput til String
            String mail = mailInput.getText().toString(); // Konverter mailInput til String

            if (name.isEmpty() || phone.isEmpty() || mail.isEmpty()) { // Valider om input felterne er fyldt
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Contact contact = new Contact( name, phone, mail); // Ny Contact instance

            SharedPreferences prefs = getSharedPreferences("contacts", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit(); // åbner "SharedPreferences" databasen

            Gson gson = new Gson();
            String json = prefs.getString("contact_list", null);
            Type type = new TypeToken<ArrayList<Contact>>(){}.getType();
            List<Contact> contactList = json == null ? new ArrayList<>() : gson.fromJson(json, type);

            contactList.add(contact); // Tilføjer ny kontakt til listen
            editor.putString("contact_list", gson.toJson(contactList)); //Konverter the opdateret list til JSON
            editor.apply(); // Gemmer ændringer i SharePreferences

            Toast.makeText(this, "Contact added!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        });
    }
}