package com.example.rentit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword, editTextStartdate, editTextEndData;
    private Button loginButtonMain, loginButtonIn, searchButton, buttonSeeAll;
    private Dialog d;
    //  private ProgressDialog progressDialog;TODO :  ProgressDialog
    private Button registerButton;
    private Button feedbekButton;
    private Spinner areaSpinner;
    private Spinner priceSpinner;
    private ArrayList<String> areaList = new ArrayList<>();
    private ArrayList<String> priceList = new ArrayList<>();
    private DatabaseReference cardRef2;
    private DatabaseReference cardRef;

    private TextView textViewWarnEmail, textViewWarnPassword, textViewWarnAll, textVieeTittel;


    private RegisterInformation registerInformation;
    private RegisterInformation registerInformation1;

    private ToyAdapter toyAdapter = null;
    private ListView lv1;
    private ToyAdapter toyAdapter1;
    private ListView lv11;
    private String area = "";
    private String price = "";
    private List<CardCar> arrayListCards;
    private List<CardCar> arrayListCards2;

    private Boolean flagConnected;
    private TextView textViewWarnSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        textVieeTittel = findViewById(R.id.title);
        editTextStartdate = findViewById(R.id.startData);
        editTextEndData = findViewById(R.id.endData);
        areaSpinner = findViewById(R.id.area);
        textViewWarnSearch = findViewById(R.id.textViewWarnSearch);
        priceSpinner = findViewById(R.id.price);
        setLists();
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, areaList);
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, priceList);
        areaSpinner.setAdapter(areaAdapter);
        priceSpinner.setAdapter(priceAdapter);
        setSpinerArea();


        searchButton = findViewById(R.id.search);
        buttonSeeAll = findViewById(R.id.buttonSeeAll);
        registerButton = findViewById(R.id.register);
        feedbekButton = findViewById(R.id.feedback);
        loginButtonMain = findViewById(R.id.login);
        firebaseUser = mAuth.getCurrentUser();
        flagConnected = false;
        if (firebaseUser != null) {
            flagConnected = true;
            loginButtonMain.setText("יציאה");
            registerButton.setText("לעמוד שלי");
        }


        viewCards();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();


            }
        });
        loginButtonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(MainActivity.this, "!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    loginButtonMain.setText("כניסה");
                    registerButton.setText("הרשמה");

                } else
                    dialod();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Intent intent = new Intent(MainActivity.this, MainActivityPageUser.class);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(MainActivity.this, MainActivityRegister.class);
                    startActivity(intent);
                }
            }
        });

        buttonSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCards();

            }
        });

        feedbekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivityFeedback.class);
                startActivity(intent);
            }
        });
    }

    private void search() {
        textViewWarnSearch.setVisibility(View.GONE);
        cardRef = FirebaseDatabase.getInstance().getReference("CardsApprov");
        //   Toast.makeText(MainActivityPageUser.this, email, Toast.LENGTH_LONG).show();
        //cardRef2.child("cardsUser").orderByChild("priceDay").equalTo("100");
        cardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCards2 = new ArrayList<>();
                CardCar cardCar;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    cardCar = child.getValue(CardCar.class);
                    searchMatch(area, price, "" + editTextStartdate.getText().toString(),
                            "" + editTextEndData.getText().toString(), cardCar);
                }
                if (arrayListCards2.size() > 0) {
                    if (toyAdapter != null) toyAdapter.clear();

                    // Toast.makeText(MainActivityPageUser.this, ""+registerInformation.getCardsUser().get(0).getImageViewArrayListName().get(0), Toast.LENGTH_LONG).show();
                    buttonSeeAll.setVisibility(View.VISIBLE);
                    toyAdapter1 = new ToyAdapter(MainActivity.this, 0, 0, arrayListCards2);
                    //phase 4 reference to listview
                    lv11 = (ListView) findViewById(R.id.lvMange);
                    lv11.setAdapter(toyAdapter1);

                    lv11.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(MainActivity.this, MainActivityRegisterCar.class);
                            intent.putExtra("flagMain", true);
                            pass(intent, arrayListCards2.get(i));


                            startActivityForResult(intent, 0);
                        }
                    });
                } else textViewWarnSearch.setVisibility(View.VISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchMatch(String area, String price, String startDate, String endDate, CardCar card) {


        if (area.equals(card.getArea())) {

            if (dateMatch(card, startDate, endDate)) {

                String priceCardString = card.getPriceDay();
                int priceCard = Integer.valueOf(priceCardString);
                if (price.equals("50-150")) {
                    if (priceCard >= 50 && priceCard < 150) arrayListCards2.add(card);
                } else if (price.equals("150-250")) {
                    if (priceCard >= 150 && priceCard < 250) arrayListCards2.add(card);
                } else if (price.equals("250-350")) {
                    if (priceCard >= 250 && priceCard < 350) arrayListCards2.add(card);
                } else if (price.equals("350-450")) {
                    if (priceCard >= 350 && priceCard < 450)
                        arrayListCards2.add(card);
                } else if (price.equals("450-550")) {
                    if (priceCard >= 450 && priceCard < 550)
                        arrayListCards2.add(card);
                } else if (price.equals("550+")) {
                    if (priceCard >= 550) arrayListCards2.add(card);
                }
            }
        }

    }


    private boolean dateMatch(CardCar card, String startDate, String endDate) {
        return true;
    }

    private void viewCards() {
        textViewWarnSearch.setVisibility(View.GONE);

        cardRef2 = FirebaseDatabase.getInstance().getReference("CardsApprov");
        //   Toast.makeText(MainActivityPageUser.this, email, Toast.LENGTH_LONG).show();
        // textViewTitle.setText("jjjjj");

        cardRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCards = new ArrayList();
                CardCar card;
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    i++;
                    if (i > 30) break;
                    card = child.getValue(CardCar.class);
                    arrayListCards.add(card);
                }
                if (arrayListCards.size() > 0) {
                    // Toast.makeText(MainActivityPageUser.this, ""+registerInformation.getCardsUser().get(0).getImageViewArrayListName().get(0), Toast.LENGTH_LONG).show();
                    buttonSeeAll.setVisibility(View.GONE);

                    toyAdapter = new ToyAdapter(MainActivity.this, 0, 0, arrayListCards);
                    //phase 4 reference to listview
                    lv1 = (ListView) findViewById(R.id.lvMange);
                    lv1.setAdapter(toyAdapter);

                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(MainActivity.this, MainActivityRegisterCar.class);
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

    private void dialod() {
        d = new Dialog(this);
        d.setContentView(R.layout.login);
        d.setTitle("Login");

        d.setCancelable(true);
        textViewWarnAll = (TextView) d.findViewById(R.id.txtWarnAll);
        textViewWarnEmail = (TextView) d.findViewById(R.id.txtWarnEmail);
        textViewWarnPassword = (TextView) d.findViewById(R.id.txtWarnPssword);
        editTextEmail = (EditText) d.findViewById(R.id.loginEmail);
        editTextPassword = (EditText) d.findViewById(R.id.loginPassword);
        loginButtonIn = (Button) d.findViewById(R.id.loginButton2);
        loginButtonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewWarnPassword.setVisibility(View.GONE);
                textViewWarnEmail.setVisibility(View.GONE);


                loginIn();


            }
        });
        d.show();

    }

    private void loginIn() {
//        progressDialog.setMessage("Registering Please Wait...");
//        progressDialog.show();
        String emailLogin = editTextEmail.getText().toString();
        if (ErrWarn.errEmail(emailLogin)) {
            textViewWarnEmail.setText("אימייל לא תקין");
            textViewWarnEmail.setVisibility(View.VISIBLE);
        }
        String passwordLogin = "";
        passwordLogin = editTextPassword.getText().toString();
        if (passwordLogin.length() == 0) {
            textViewWarnPassword.setText("סיסמא לא תקינה");
            textViewWarnPassword.setVisibility(View.VISIBLE);
        } else {

            mAuth.signInWithEmailAndPassword(emailLogin, passwordLogin)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "הכניסה הצליחה", Toast.LENGTH_SHORT).show();
                                loginButtonMain.setText("יציאה");
                                Intent intent = new Intent(MainActivity.this, MainActivityPageUser.class);
                                startActivity(intent);
                                d.dismiss();
                            } else {
                                textViewWarnAll.setText("אימייל או סיסמא שגויים");
                                textViewWarnAll.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this, "הכניסה נכשלה", Toast.LENGTH_SHORT).show();
                            }
                            // progressDialog.dismiss();
                        }
                    });
        }


    }


    private void setLists() {
        areaList.add("בחר אזור");
        areaList.add("צפון");
        areaList.add("מרכז");
        areaList.add("דרום");

        priceList.add("טווח מחיר");
        priceList.add("50-150");
        priceList.add("150-250");
        priceList.add("250-350");
        priceList.add("350-450");
        priceList.add("450-550");
        priceList.add("550+");
    }


    public void setSpinerArea() {
        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                price = priceList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                area = areaList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        for (int i = 1; i <= cardCar.getImageViewArrayListName().size(); i++) {
            intent.putExtra("image" + i, cardCar.getImageViewArrayListName().get(i - 1));
        }


    }


}