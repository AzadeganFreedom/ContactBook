package com.example.contactbook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditContactActivity extends AppCompatActivity {


    private EditText nameInput, phoneInput, emailInput;
    private Button updateButton, cancelButton;
    private int contactIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameInput = findViewById(R.id.inp_name);
        phoneInput = findViewById(R.id.inp_phone);
        emailInput = findViewById(R.id.inp_mail);
        updateButton = findViewById(R.id.btn_update);
        cancelButton = findViewById(R.id.btn_cancel);

        Intent intent = getIntent();
        Contact contact = (Contact) intent.getSerializableExtra("contact");
        contactIndex = intent.getIntExtra("index", -1);

        if (contact != null) {
            nameInput.setText(contact.getName());
            phoneInput.setText(contact.getPhone());
            emailInput.setText(contact.getEmail());
        }

        updateButton.setOnClickListener(v -> {
            String updatedName = nameInput.getText().toString();
            String updatedPhone = phoneInput.getText().toString();
            String updatedEmail = emailInput.getText().toString();

            Contact updatedContact = new Contact(updatedName, updatedPhone, updatedEmail);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedContact", updatedContact);
            resultIntent.putExtra("index", contactIndex);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        cancelButton.setOnClickListener(v -> finish());
    }
}