package com.example.rentit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity_Feedback extends AppCompatActivity {
    private EditText editTextFeedback, editTextEmailCar;
    private Spinner spinner;
    Button buttonSend, buttonReturnMain;
    int grade = -1;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__feedbaek);


        spinner = findViewById(R.id.spinnerGrade);
        editTextEmailCar = findViewById(R.id.editTextEmailCar);
        flag = false;
        Bundle bundle = getIntent().getExtras();
        try {
            flag = bundle.getBoolean("flag");
        } catch (RuntimeException e) {
        }
        if (flag) editTextEmailCar.setVisibility(View.VISIBLE);
        editTextFeedback = findViewById(R.id.editTextWordsGrade);
        buttonReturnMain = findViewById(R.id.buttonReturnMainGrade);
        buttonSend = findViewById(R.id.buttonSendGrade);
        setSpinner();


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (grade == -1) {
                    editTextFeedback.setError("אנא הכנס ציון");
                    editTextFeedback.requestFocus();
                    return;
                }
                if (flag && editTextEmailCar.getText().toString().isEmpty()) {
                    editTextEmailCar.setError("אנא הכנס אימייל או פון של בעל הרכב");
                    editTextEmailCar.requestFocus();
                    return;
                }
                Feedback feedback = new Feedback();
                Bundle bundle = getIntent().getExtras();
                int id = -1;
                try {
                    id = bundle.getInt("id");
                } catch (RuntimeException e) {
                }
                String emailReport = bundle.getString("emailReporting");
                String emailCar="";
                if (flag)
                    emailCar = editTextEmailCar.getText().toString();
                else
                    emailCar=bundle.getString("email");
                feedback.setId(id);
                feedback.setEmailCar(emailCar);
                feedback.setGrade(grade);
                feedback.setEmailReporting(emailReport);
                if (!editTextFeedback.getText().toString().isEmpty())
                    feedback.setFeedback(editTextFeedback.getText().toString());

                DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("FeedbackWaitApprov").push();
               feedback.setKey(cardRef.getKey());
                cardRef.setValue(feedback);
                Toast.makeText(MainActivity_Feedback.this, "נשלח בהצלחה", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity_Feedback.this, MainActivity.class);
                startActivity(intent);


            }
        });
        buttonReturnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Feedback.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setSpinner() {
        final ArrayList<String> arrayListGrade = new ArrayList();
        arrayListGrade.add("בחר מספר");
        arrayListGrade.add("1");
        arrayListGrade.add("2");
        arrayListGrade.add("3");
        arrayListGrade.add("4");
        arrayListGrade.add("5");
        arrayListGrade.add("6");
        arrayListGrade.add("7");
        arrayListGrade.add("8");
        arrayListGrade.add("9");
        arrayListGrade.add("10");

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, arrayListGrade);

        spinner.setAdapter(gradeAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0)
                    grade = Integer.valueOf(arrayListGrade.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}