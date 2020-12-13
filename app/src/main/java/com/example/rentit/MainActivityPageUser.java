package com.example.rentit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class MainActivityPageUser extends AppCompatActivity {
    private Button cardButton;
    private Button pageMainButton;
    private CardCarAdapter cardCarAdapter;
    private ListView lv;
    private DatabaseReference cardRef2;
    private TextView textViewExplanation;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String email = "";
    private RegisterInformation registerInformation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_user);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("טוען כרטיסים...");
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        try {
            if (!firebaseUser.getEmail().toString().isEmpty())
                email = firebaseUser.getEmail().toString();
            else email = firebaseUser.getPhoneNumber().toString();
        } catch (RuntimeException e) {
            email = firebaseUser.getPhoneNumber().toString();
        }

        //Toast.makeText(MainActivityPageUser.this, "" + email, Toast.LENGTH_LONG).show();

        if (email.equals("arielrentit@gmail.com")) {
            Intent intent = new Intent(MainActivityPageUser.this, MainActivityManagementCardsApprov.class);
            startActivityForResult(intent, 0);
        }
        cardRef2 = FirebaseDatabase.getInstance().getReference("RegisterInformation");
        // progressDialog.setMessage("Registering Please Wait...");
        //   Toast.makeText(MainActivityPageUser.this, email, Toast.LENGTH_LONG).show();

        // progressDialog.show();
        cardRef2.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    registerInformation = child.getValue(RegisterInformation.class);
                }
                if (registerInformation != null && registerInformation.getCardsUser().size() > 0) {
                    cardCarAdapter = new CardCarAdapter(MainActivityPageUser.this, 0, 0, registerInformation.getCardsUser());
                    //phase 4 reference to listview
                    lv = (ListView) findViewById(R.id.lv);
                    lv.setAdapter(cardCarAdapter);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(MainActivityPageUser.this, MainActivityRegisterCar.class);
                            intent.putExtra("position", i);
                            pass(intent, registerInformation.getCardsUser().get(i));
                            startActivityForResult(intent, 0);
                        }
                    });
                } else {
                    textViewExplanation = findViewById(R.id.textViewExplanationPage);
                    textViewExplanation.setText("אין כרטיסים להצגה");
                    textViewExplanation.setTextColor(-65536);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }

        });
        cardButton = findViewById(R.id.publish);
        pageMainButton = findViewById(R.id.pageMain2);

        cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityPageUser.this, MainActivityRegisterCar.class);
                startActivity(intent);
            }
        });

        pageMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityPageUser.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void pass(Intent intent, CardCar cardCar) {
        intent.putExtra("flagEdit", true);
        intent.putExtra("name", cardCar.getName());
        intent.putExtra("typeCar", cardCar.getTypeCar());
        intent.putExtra("area", cardCar.getArea());
        intent.putExtra("city", cardCar.getCity());
        intent.putExtra("dateEnd", cardCar.getDateEnd());
        intent.putExtra("dateStart", cardCar.getDateStart());
        intent.putExtra("id", cardCar.getId());
        intent.putExtra("insurance", cardCar.getInsurance());
        intent.putExtra("numImage", cardCar.getNumImage());
        intent.putExtra("permissionToPublish", cardCar.getPermissionToPublish());
        intent.putExtra("phone", cardCar.getPhone());
        intent.putExtra("priceDay", cardCar.getPriceDay());
        intent.putExtra("remarks", cardCar.getRemarks());
        intent.putExtra("yearCar", cardCar.getYearCar());
        intent.putExtra("email", cardCar.getEmail());
        intent.putExtra("key", cardCar.getKey());
        intent.putExtra("rejection", cardCar.getRejection());

        for (int i = 1; i <= cardCar.getImageViewArrayListName().size(); i++) {
            intent.putExtra("image" + i, cardCar.getImageViewArrayListName().get(i - 1));
        }


    }

}