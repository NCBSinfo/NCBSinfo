package com.rohitsuratekar.NCBSinfo.common.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.ContactsData;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;

public class ContactAdd extends AppCompatActivity {
    private EditText inputName, inputNumber, inputDepartment, inputPosition;
    private TextInputLayout inputLayoutName, inputLayoutNumber, inputLayoutDepartment, inputLayoutPosition;
    int forEdit;
    int feldID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_add);
        Intent intent = getIntent();
        forEdit = intent.getIntExtra("forEdit", 0);
        feldID = intent.getIntExtra("feldID", 0);
        if (forEdit == 0) {
            setTitle(getString(R.string.contact_add));
        } else {
            setTitle(getString(R.string.contact_edit));
        }

        inputLayoutName = (TextInputLayout) findViewById(R.id.contact_input_layout_name);
        inputLayoutNumber = (TextInputLayout) findViewById(R.id.contact_input_layout_number);
        inputLayoutDepartment = (TextInputLayout) findViewById(R.id.contact_input_layout_department);
        inputLayoutPosition = (TextInputLayout) findViewById(R.id.contact_input_layout_position);


        inputName = (EditText) findViewById(R.id.contact_input_name);
        inputNumber = (EditText) findViewById(R.id.contact_input_number);
        inputDepartment = (EditText) findViewById(R.id.contact_input_department);
        inputPosition = (EditText) findViewById(R.id.contact_input_position);
        Button btnSignUp = (Button) findViewById(R.id.contact_submit);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputNumber.addTextChangedListener(new MyTextWatcher(inputNumber));


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        if (forEdit == 1) {
            ContactsData db = new ContactsData(getBaseContext());
            inputName.setText(db.get(feldID).getName());
            inputNumber.setText(db.get(feldID).getExtension());
            inputDepartment.setText(db.get(feldID).getDepartment());
            inputPosition.setText(db.get(feldID).getPosition());
            btnSignUp.setText("Change");
        }

    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }
        int pseudoIncrement = 1;
        String contactName = inputName.getText().toString();
        String contactNumber = inputNumber.getText().toString();
        String contactDepartment = inputDepartment.getText().toString();
        String contactPosition = inputPosition.getText().toString();
        if (contactDepartment.length() == 0) {
            contactDepartment = "N/A";
        }
        if (contactPosition.length() == 0) {
            contactPosition = "N/A";
        }
        ContactsData db = new ContactsData(getBaseContext());

        if (forEdit == 0) {
            db.add(new ContactModel(pseudoIncrement, contactName, contactDepartment, contactPosition, contactNumber, "0"));
            Toast.makeText(getBaseContext(), contactName + " added to contact list.", Toast.LENGTH_LONG).show();
        } //zero  added to make it not favorite
        else {
            db.update(new ContactModel(feldID, contactName, contactDepartment, contactPosition, contactNumber, "0"));
            Toast.makeText(getBaseContext(), contactName + " changed!", Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(ContactAdd.this, Contacts.class);
        startActivity(intent);

    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.contact_error1));
            requestFocus(inputName);
            inputLayoutNumber.setErrorEnabled(false);
            return false;
        } else if (inputNumber.getText().toString().trim().isEmpty()) {
            inputLayoutNumber.setError(getString(R.string.contact_error2));
            requestFocus(inputNumber);
            inputLayoutName.setErrorEnabled(false);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
            inputLayoutNumber.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
