package com.example.rentit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivityManagementCardsApprov extends AppCompatActivity {
    private Dialog d;

    private DatabaseReference cardRef2;
    private List<CardCar> arrayListCards;
    private CardCarAdapter cardCarAdapter = null;
    private ListView lv1;
    private Button buttonMainReturn, buttonRejection, buttonApprov, buttonWitheApprov,
      buttonRejectionFeed, buttonApprovFedd, buttonWitheApprovFeed;
    private TextView textViewRejection, textViewApprov, textViewWitheApprov;

    private int numCards = -1;
    private CardCar cardCar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_management_cards_approv);

        setId();
        numFeed("FeedbackWaitApprov");
        numFeed("FeedbackApprov");
        numFeed("FeedbackRejection");
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
        buttonWitheApprovFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playFeed("FeedbackWaitApprov");

            }
        });
        buttonApprovFedd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playFeed("FeedbackApprov");

            }
        });
        buttonRejectionFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playFeed("FeedbackRejection");

            }
        });


         playCards2("CardsWaitApprov");



    }

    private void numFeed(final String type) {
        cardRef2 = FirebaseDatabase.getInstance().getReference(type);
        cardRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    numCards=0;
                }
                else {

                    final ArrayList<Feedback> arrayListFeed = new ArrayList();
                    Feedback feedback;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        feedback = child.getValue(Feedback.class);
                        arrayListFeed.add(feedback);

                    }
                    numCards = arrayListFeed.size();
                }

                if (type.equals("FeedbackWaitApprov")) {
                    TextView textView=findViewById(R.id.textViewNumFeedWait);
                    textView.setText("" + numCards);
                }
                else if (type.equals("FeedbackRejection")) {
                    TextView textView=findViewById(R.id.textViewNumFeedRejecsen);
                    textView.setText("" + numCards);
                }
                else if (type.equals("FeedbackApprov")) {
                    TextView textView=findViewById(R.id.textViewNumFeedApprove);
                    textView.setText("" + numCards);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setId() {
        textViewWitheApprov = findViewById(R.id.textViewNumWaitAprove);
        textViewApprov = findViewById(R.id.textViewNumAprove);
        textViewRejection = findViewById(R.id.textViewNumReject);
        buttonApprovFedd=findViewById(R.id.buttonAprrovFeedbeck);
        buttonRejectionFeed=findViewById(R.id.buttonRejecsanFeedback);
        buttonWitheApprovFeed=findViewById(R.id.buttonWiatAprrovFeedbeck);
        buttonApprov = findViewById(R.id.buttonApprov);
        buttonWitheApprov = findViewById(R.id.buttonWhite);
        buttonRejection = findViewById(R.id.buttonRejection);
        buttonMainReturn = findViewById(R.id.buttonMainReturn);
    }

    private void numCards() {
        playCards("CardsApprov");
        playCards("CardsRejection");
        playCards("CardsWaitApprov");
    }

    private void playCards(final String type) {
        cardRef2 = FirebaseDatabase.getInstance().getReference(type);
        cardRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCards = new ArrayList();
                cardCar = new CardCar();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    try {
                        cardCar = child.getValue(CardCar.class);
                        arrayListCards.add(cardCar);

                    } catch (RuntimeException e) {

                    }
                }

                numCards = arrayListCards.size();
                if (type.equals("CardsWaitApprov"))
                    textViewWitheApprov.setText("" + numCards);
                else if (type.equals("CardsRejection"))
                    textViewRejection.setText("" + numCards);
                else if (type.equals("CardsApprov"))
                    textViewApprov.setText("" + numCards);


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

                    try {
                        cardCar = child.getValue(CardCar.class);
                        arrayListCards.add(cardCar);

                    } catch (RuntimeException e) {

                    }
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

    private void playFeed(final String type) {
        cardRef2 = FirebaseDatabase.getInstance().getReference(type);
        cardRef2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    return;
                }
                final ArrayList<Feedback>arrayListFeed = new ArrayList();
                Feedback feedback;
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                        feedback = child.getValue(Feedback.class);
                        arrayListFeed.add(feedback);

                }

                  //   Toast.makeText(MainActivityManagementCardsApprov.this, "sss"+arrayListFeed.size(), Toast.LENGTH_LONG).show();
//
                    FeedbeckAdapter feedbeckAdapter = new FeedbeckAdapter(MainActivityManagementCardsApprov.this, 0, 0, arrayListFeed,true);
                    //phase 4 reference to listview
                    lv1 = (ListView) findViewById(R.id.lvMange);
                    lv1.setAdapter(feedbeckAdapter);
//
                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                          dialod(arrayListFeed.get(i),type);
                        }
                    });
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
        intent.putExtra("rejection", cardCar.getRejection());

        for (int i = 1; i <= cardCar.getImageViewArrayListName().size(); i++) {
            intent.putExtra("image" + i, cardCar.getImageViewArrayListName().get(i - 1));
        }


    }
    private void dialod(final Feedback feedback, final String type) {
        d = new Dialog(this);
        d.setContentView(R.layout.feedback_approve);
        d.setTitle("Manage");

        d.setCancelable(true);
        Button buttonApproveManage=(Button)d.findViewById(R.id.buttonApproveManage);
        Button buttonRejectionManege=(Button)d.findViewById(R.id.buttonRejectionManage);
        Button buttonDeleteManege=(Button)d.findViewById(R.id.buttonDeleteFeed);


        buttonApproveManage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FeedbackApprov").push();
              final String keyApprov = ref.getKey();
              DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("FeedbackWaitApprov");
              ref2.child(feedback.getKey()).removeValue();
              feedback.setKey(keyApprov);
              ref.setValue(feedback);
              Intent intent = new Intent(MainActivityManagementCardsApprov.this, MainActivityManagementCardsApprov.class);
              startActivityForResult(intent, 0);
          }
      });
      buttonRejectionManege.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FeedbackRejection").push();
              final String keyApprov = ref.getKey();
              DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("FeedbackWaitApprov");
              ref2.child(feedback.getKey()).removeValue();
              feedback.setKey(keyApprov);
              ref.setValue(feedback);
              Intent intent = new Intent(MainActivityManagementCardsApprov.this, MainActivityManagementCardsApprov.class);

              startActivityForResult(intent, 0);
          }
      });
      buttonDeleteManege.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(type);
              ref2.child(feedback.getKey()).removeValue();
              Intent intent = new Intent(MainActivityManagementCardsApprov.this, MainActivityManagementCardsApprov.class);

              startActivityForResult(intent, 0);

          }
      });
        d.show();

    }
}