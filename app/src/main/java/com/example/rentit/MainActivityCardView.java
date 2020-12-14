package com.example.rentit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivityCardView extends AppCompatActivity {
    private TextView editTextCity, editTextTypeCar, editTextInsurance,
            editTextDateStart, editTextRemarks, textViewFeedbackGrade;
    private CardCar cardCar;
    private Button buttonReturnMain, buttonImage, buttonSendFeed, buttonSeeFeedback;
    private ImageView imageView;
    private int position;
    // private Feedback feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_card_view);

        setCard();
        position = 1;
        textViewFeedbackGrade = findViewById(R.id.textViewFeedbackGrade);
        buttonImage = findViewById(R.id.buttonImage);
        buttonReturnMain = findViewById(R.id.buttonMainM);
        buttonSendFeed = findViewById(R.id.buttonSendFeed);
        buttonSeeFeedback = findViewById(R.id.buttonSeeFeedback);

        editTextCity = findViewById(R.id.textViewPriceAndAreaM);
        editTextDateStart = findViewById(R.id.textViewSrartDateM);
        editTextInsurance = findViewById(R.id.textViewInsuranceAndPhoneM);
        editTextRemarks = findViewById(R.id.textViewRemarksM);
        editTextTypeCar = findViewById(R.id.textViewTypeCarAndYearM);
        imageView = findViewById(R.id.imageMainM);

        editTextTypeCar.setText("סוג רכב: " + cardCar.getTypeCar() + "   משנת: " + cardCar.getYearCar());
        editTextRemarks.setText("הערות: " + cardCar.getRemarks());
        editTextInsurance.setText("ביטוח: " + cardCar.getInsurance() + "   "
                + cardCar.getName() + ": " + cardCar.getPhone());
        editTextDateStart.setText("מ-" + cardCar.getDateStart() + "   עד ה-" + cardCar.getDateEnd());
        editTextCity.setText("מחיר ליום: " + cardCar.getPriceDay() + "     אזור " + cardCar.getArea() + ": " + cardCar.getCity());
        Glide.with(this)
                .load(cardCar.getImageViewArrayListName().get(0))
                .into(imageView);
        if (cardCar.getImageViewArrayListName().size() == 1)
            buttonImage.setVisibility(View.GONE);

        if (cardCar.getRemarks().isEmpty())
            editTextRemarks.setVisibility(View.GONE);
        buttonReturnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityCardView.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(view)
                        .load(cardCar.getImageViewArrayListName().get(position))
                        .into(imageView);
                position++;
                if (position == cardCar.getImageViewArrayListName().size())
                    position = 0;

            }
        });

        buttonSendFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String emailReport = "";
                if (firebaseUser != null) {
                    try {
                        if (!firebaseUser.getEmail().toString().isEmpty())
                            emailReport = firebaseUser.getEmail().toString();
                        else emailReport = firebaseUser.getPhoneNumber().toString();
                    } catch (RuntimeException e) {
                        emailReport = firebaseUser.getPhoneNumber().toString();
                    }

                } else {
                    TextView textView = findViewById(R.id.textViewWarnFeed);
                    textView.setVisibility(View.VISIBLE);
                    buttonSendFeed.setError("אנא הירשם או התחבר");
                    buttonSendFeed.requestFocus();
                    return;
                }
                Intent intent = new Intent(MainActivityCardView.this, MainActivity_Feedback.class);
                intent.putExtra("email", cardCar.getEmail());
                intent.putExtra("emailReporting", emailReport);
                intent.putExtra("id", cardCar.getId());

                startActivity(intent);
            }
        });
        buttonSeeFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivityCardView.this, MainActivityListFeedbackPage.class);
                intent.putExtra("email", cardCar.getEmail());
                startActivity(intent);
            }
        });
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String emailUser = "";
//        if (firebaseUser != null) {
//            try {
//                if (!firebaseUser.getEmail().toString().isEmpty())
//                    emailUser = firebaseUser.getEmail().toString();
//                else emailUser = firebaseUser.getPhoneNumber().toString();
//            } catch (RuntimeException e) {
//                emailUser = firebaseUser.getPhoneNumber().toString();
//            }
//        }
//if(!cardCar.getEmail().equals(emailUser))
        seeMePlus();

    }

    private void seeMePlus() {
        DatabaseReference cardRef2 = FirebaseDatabase.getInstance().getReference("CardsApprov");
        cardRef2.child(cardCar.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) return;
                CardCar cardCar2 = new CardCar();
                cardCar2 = dataSnapshot.getValue(CardCar.class);
                cardCar2.addOneSeeCard();
                DatabaseReference cardRef6 = FirebaseDatabase.getInstance().getReference();
                cardRef6.child("CardsApprov").child(cardCar.getKey()).setValue(cardCar2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
    }


    private void setCard() {
        cardCar = new CardCar();
        Bundle bundle = getIntent().getExtras();
        cardCar.setKey(bundle.getString("key"));
        cardCar.setEmail(bundle.getString("email"));
        cardCar.setArea(bundle.getString("area"));
        cardCar.setNumImage(bundle.getInt("numImage"));
        cardCar.setCity(bundle.getString("city"));
        cardCar.setPhone(bundle.getString("phone"));
        cardCar.setYearCar(bundle.getString("yearCar"));
        cardCar.setTypeCar(bundle.getString("typeCar"));
        cardCar.setRemarks(bundle.getString("remarks"));
        cardCar.setPriceDay(bundle.getString("priceDay"));
        cardCar.setDateEnd(bundle.getString("dateEnd"));
        cardCar.setDateStart(bundle.getString("dateStart"));
        cardCar.setName(bundle.getString("name"));
        cardCar.setInsurance(bundle.getString("insurance"));
        cardCar.setId(bundle.getInt("id"));
        cardCar.setPermissionToPublish(bundle.getInt("permissionToPublish"));
        cardCar.setRejection(bundle.getString("rejection"));

        for (int i = 1; i <= cardCar.getNumImage(); i++)
            cardCar.addImageViewArrayListName(bundle.getString("image" + i));

    }

}