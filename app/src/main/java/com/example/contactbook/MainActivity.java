package com.example.contactbook;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView contactListView;
    List<Contact> contactList = new ArrayList<>(); // Alle Contact objecter bliver gemt her
    ArrayAdapter<String> adapter; // Forbinder contactList til ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //################################################################

        contactListView = findViewById(R.id.list_contacts);

        loadContacts();

        List<String> contactNames = new ArrayList<>();
        for (Contact c : contactList) {
            contactNames.add(c.getName());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactNames);
        contactListView.setAdapter(adapter);

        contactListView.setOnItemClickListener((parent, view, position, id) -> {
            Contact selectedContact = contactList.get(position);

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_contact_details, null);

            TextView tvPhone = dialogView.findViewById(R.id.tv_phone);
            TextView tvEmail = dialogView.findViewById(R.id.tv_email);

            tvPhone.setText("Phone: " + selectedContact.getPhone());
            tvEmail.setText("Email: " + selectedContact.getEmail());

            tvPhone.setOnClickListener(v1 -> {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + selectedContact.getPhone()));
                startActivity(dialIntent);
            });

            tvEmail.setOnClickListener(v2 -> {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + selectedContact.getEmail()));
                startActivity(emailIntent);
            });

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(selectedContact.getName())
                    .setView(dialogView)
                    .setPositiveButton("Close", null)
                    .setNegativeButton("Edit", (dialog, which) -> {
                        Intent intent = new Intent(MainActivity.this, EditContactActivity.class);
                        intent.putExtra("contact", selectedContact);
                        intent.putExtra("index", position);
                        startActivityForResult(intent, 1);
                    })
                    .setNeutralButton("Delete", (dialog, which) -> {
                        contactList.remove(position);
                        saveContacts();
                        onResume();
                        Toast.makeText(MainActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                    })
                    .show();

        });

        //################################################################

        // Navigere til AddContactActivity
        findViewById(R.id.btn_addaccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
                startActivity(intent);
            }
        });
    }

    //################################################################

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Contact updatedContact = (Contact) data.getSerializableExtra("updatedContact");
            int index = data.getIntExtra("index", -1);

            if (index != -1) {
                contactList.set(index, updatedContact);
                saveContacts();
                onResume();
            }
        }
    }


    //################################################################

    private void loadContacts() {
        SharedPreferences prefs = getSharedPreferences("contacts", MODE_PRIVATE); // Åbner SharedPreferences databasen
        Gson gson = new Gson(); // Bruges til at konverter JSON string til Java objekter og omvendt
        String json = prefs.getString("contact_list", null);
        Type type = new TypeToken<ArrayList<Contact>>() {
        }.getType();
        contactList = json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }

    //################################################################

    private void saveContacts() {
        SharedPreferences prefs = getSharedPreferences("contacts", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contactList);
        editor.putString("contact_list", json);
        editor.apply();
    }


    //################################################################

    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
        Collections.sort(contactList, (c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));

        List<String> contactNames = new ArrayList<>();
        for (Contact c : contactList) {
            contactNames.add(c.getName());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactNames);
        contactListView.setAdapter(adapter);
    }
}