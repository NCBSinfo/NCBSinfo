package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AddNewContact extends AppCompatActivity {
    private EditText inputName, inputNumber, inputDepartment, inputPosition;
    private TextInputLayout inputLayoutName, inputLayoutNumber, inputLayoutDepartment, inputLayoutPosition;
    private Button btnSignUp;
    int forEdit;
    int feldID;
    int focusPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);
        Intent intent = getIntent();
        forEdit = intent.getIntExtra("forEdit", 0);
        feldID = intent.getIntExtra("feldID", 0);
        if (forEdit==0){ setTitle("Add Contact");}
        else{setTitle("Edit Contact");}

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutNumber = (TextInputLayout) findViewById(R.id.input_layout_contact_number);
        inputLayoutDepartment = (TextInputLayout) findViewById(R.id.input_layout_department);
        inputLayoutPosition = (TextInputLayout) findViewById(R.id.input_layout_position);


        inputName = (EditText) findViewById(R.id.input_name);
        inputNumber = (EditText) findViewById(R.id.input_contact_number);
        inputDepartment = (EditText) findViewById(R.id.input_department);
        inputPosition = (EditText) findViewById(R.id.input_position);
        btnSignUp = (Button) findViewById(R.id.btn_signup);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputNumber.addTextChangedListener(new MyTextWatcher(inputNumber));
        inputDepartment.addTextChangedListener(new MyTextWatcher(inputDepartment));
        inputPosition.addTextChangedListener(new MyTextWatcher(inputPosition));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        if (forEdit == 1){
            DBHandler db = new DBHandler(this);
            inputName.setText(db.getDocument(feldID).getContactName());
            inputNumber.setText(db.getDocument(feldID).getContactExtension());
            inputDepartment.setText(db.getDocument(feldID).getContactDepartment());
            inputPosition.setText(db.getDocument(feldID).getContactPosition());
            btnSignUp.setText("Change");
        }

    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName()) {
            return;
        }
        int pseudoIncrement=1;
        String contactName = inputName.getText().toString();
        String contactNumber = inputNumber.getText().toString();
        String contactDepartment = inputDepartment.getText().toString();
        String contactPosition = inputPosition.getText().toString();
        if (contactDepartment.length()==0){contactDepartment = "N/A";}
        if (contactPosition.length()==0){contactPosition = "N/A";}
        DBHandler db = new DBHandler(this);
        if (forEdit==0){ db.addContact(new SQfields(pseudoIncrement,contactName, contactDepartment, contactPosition,contactNumber,"0"));
            Toast.makeText(getBaseContext(), contactName+" added to contact list.", Toast.LENGTH_LONG).show(); } //zero  added to make it not favorite
        else{db.updateDocument(new SQfields(feldID, contactName, contactDepartment, contactPosition,contactNumber,"0"));
            Toast.makeText(getBaseContext(), contactName+" changed!", Toast.LENGTH_LONG).show(); }
        db.close();
        Intent intent = new Intent(AddNewContact.this, ContactActivity.class);
        startActivity(intent);

    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Invalid Name");
            requestFocus(inputName);
            return false;
        }
        else if (inputNumber.getText().toString().trim().isEmpty()) {
            inputLayoutNumber.setError("Contact number can not be empty");
            requestFocus(inputNumber);
            return false;
        }
        else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }



    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_contact_number:
                    validateName();
                    break;
                case R.id.input_department:
                    break;
                case R.id.input_position:
                    break;
            }
        }
    }
}