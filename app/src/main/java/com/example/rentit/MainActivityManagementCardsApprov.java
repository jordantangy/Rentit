package com.example.rentit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivityManagementCardsApprov extends AppCompatActivity {
    private DatabaseReference cardRef2;
    private List<CardCar> arrayListCards;
    private RegisterInformation registerInformation;
    private CardCarAdapter cardCarAdapter = null;
    private ListView lv1;
    private Button buttonMainReturn, buttonRejection, buttonApprov, buttonWitheApprov;
    private TextView textViewRejection, textViewApprov, textViewWitheApprov;
    private boolean flagNum=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_management_cards_approv);
        textViewWitheApprov = findViewById(R.id.textViewNumWaitAprove);
        textViewApprov = findViewById(R.id.textViewNumAprove);
        textViewRejection= findViewById(R.id.textViewNumReject);
        buttonApprov = findViewById(R.id.buttonApprov);
        buttonWitheApprov = findViewById(R.id.buttonWhite);
        buttonRejection = findViewById(R.id.buttonRejection);
        buttonMainReturn = findViewById(R.id.buttonMainReturn);
        numCards();
        buttonApprov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playCards2("CardsApprov");
            }
        });
        buttonWitheApprov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playCards2("CardsWaitApprov");
            }
        });
        buttonRejection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playCards2("CardsRejection");

            }
        });
        buttonMainReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityManagementCardsApprov.this, MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });


        playCards2("CardsWaitApprov");


    }

    private void numCards() {
        playCards("CardsApprov");

        playCards("CardsWaitApprov");

        playCards("CardsRejection");
    }

    private void playCards(final String type) {
        cardRef2 = FirebaseDatabase.getInstance().getReference(type);
        cardRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCards = new ArrayList();
                CardCar cardCar;
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    cardCar = child.getValue(CardCar.class);
                    arrayListCards.add(cardCar);
                }

                   int numCards = arrayListCards.size();
                    if (type.equals("CardsWaitApprov"))
                        textViewWitheApprov.setText(""+numCards);
                    if (type.equals("CardsRejection"))
                        textViewRejection.setText(""+numCards);
                    if (type.equals("CardsApprov"))
                        textViewApprov.setText(""+numCards);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void playCards2(final String type) {
        cardRef2 = FirebaseDatabase.getInstance().getReference(type);
        cardRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCards = new ArrayList();
                CardCar cardCar;
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    cardCar = child.getValue(CardCar.class);
                    arrayListCards.add(cardCar);
                }

                if (arrayListCards.size() > 0) {
                    // Toast.makeText(MainActivityPageUser.this, ""+registerInformation.getCardsUser().get(0).getImageViewArrayListName().get(0), Toast.LENGTH_LONG).show();

                    cardCarAdapter = new CardCarAdapter(MainActivityManagementCardsApprov.this, 0, 0, arrayListCards);
                    //phase 4 reference to listview
                    lv1 = (ListView) findViewById(R.id.lvMange);
                    lv1.setAdapter(cardCarAdapter);

                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(MainActivityManagementCardsApprov.this, MainActivityRegisterCar.class);
                            intent.putExtra("flagManagement", true);
                            intent.putExtra("flagMain", true);
                            pass(intent, arrayListCards.get(i));
                            startActivityForResult(intent, 0);
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void pass(Intent intent, CardCar cardCar) {
        intent.putExtra("flag", true);
        intent.putExtra("name", cardCar.getName());
        intent.putExtra("typeCar", cardCar.getTypeCar());
        intent.putExtra("area", cardCar.getArea());
        intent.putExtra("city", cardCar.getCity());
        intent.putExtra("dateEnd", cardCar.getDateEnd());
        intent.putExtra("dateStart", cardCar.getDateStart());
        intent.putExtra("id", cardCar.getId());
        intent.putExtra("insurance", cardCar.getInsurance());
        intent.putExtra("numImage", cardCar.getImageViewArrayListName().size());
        intent.putExtra("permissionToPublish", cardCar.getPermissionToPublish());
        intent.putExtra("phone", cardCar.getPhone());
        intent.putExtra("priceDay", cardCar.getPriceDay());
        intent.putExtra("remarks", cardCar.getRemarks());
        intent.putExtra("yearCar", cardCar.getYearCar());
        intent.putExtra("email", cardCar.getEmail());
        intent.putExtra("key", cardCar.getKey());

        for (int i = 1; i <= cardCar.getImageViewArrayListName().size(); i++) {
            intent.putExtra("image" + i, cardCar.getImageViewArrayListName().get(i - 1));
        }


    }
}