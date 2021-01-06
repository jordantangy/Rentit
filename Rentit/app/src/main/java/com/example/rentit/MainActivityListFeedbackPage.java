package com.example.rentit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivityListFeedbackPage extends AppCompatActivity {
    private Button pageMainButton;
    private ListView lv;
    private DatabaseReference cardRef2;
    private TextView textViewExplanation;
    private ProgressDialog progressDialog;
    private String email = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list_feedback_page);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("טוען כרטיסים...");
        progressDialog.show();
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");

        cardRef2 = FirebaseDatabase.getInstance().getReference("FeedbackApprov");

        cardRef2.orderByChild("emailCar").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    textViewExplanation = findViewById(R.id.textViewNoFeedback);
                    textViewExplanation.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    return;
                }
                int grade = 0;
                ArrayList<Feedback> arrayListFeedbacks = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Feedback feedback = child.getValue(Feedback.class);
                    grade += feedback.getGrade();
                    arrayListFeedbacks.add(feedback);
                }
                grade = grade / arrayListFeedbacks.size();
                TextView textViewGrade = findViewById(R.id.textViewGradeFeedbackPageList);
                textViewGrade.setText("ציון כללי: " + grade);

                FeedbeckAdapter feedbeckAdapter = new FeedbeckAdapter(MainActivityListFeedbackPage.this, 0, 0, arrayListFeedbacks, false);
                //phase 4 reference to listview
                lv = (ListView) findViewById(R.id.lvFeedbaek);
                lv.setAdapter(feedbeckAdapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }

        });
        pageMainButton = findViewById(R.id.pageMainFeedbackList);


        pageMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityListFeedbackPage.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    

}