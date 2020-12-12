package com.example.rentit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private CheckBox checkBox;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mobile;
    private String mVerificationId;
    private Boolean flagCode = false;
    private Dialog d;
    private EditText editTextEmail, editTextPassword;
    private EditText editTextStartdate, editTextEndData;
    private Button loginButtonMain, loginButtonIn, searchButton, buttonSeeAll;
    private ProgressDialog progressDialog;
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

    private CardCarAdapter cardCarAdapter = null;
    private ListView lv1;
    private CardCarAdapter toyAdapter1;
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

        progressDialog = new ProgressDialog(this);
        flagCode = false;
        setmCallBacks();
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


        viewCards();//make cards list view

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("טוען כרטיסים...");
                progressDialog.show();
                search();


            }
        });
        loginButtonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(MainActivity.this, "!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);


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
                    if (cardCarAdapter != null) cardCarAdapter.clear();
                    //phase 4 reference to listview

                    buttonSeeAll.setVisibility(View.VISIBLE);
                    toyAdapter1 = new CardCarAdapter(MainActivity.this, 0, 0, arrayListCards2);
                    lv11 = (ListView) findViewById(R.id.lvMange2);
                    lv11.setAdapter(toyAdapter1);

                    lv11.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(MainActivity.this, MainActivityCardView.class);
                            intent.putExtra("flagMain", true);
                            pass(intent, arrayListCards2.get(i));


                            startActivityForResult(intent, 0);
                        }
                    });
                } else textViewWarnSearch.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


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
    }//TODO:this

    private void viewCards() {
        progressDialog.setMessage("טוען כרטיסים...");
        progressDialog.show();
        textViewWarnSearch.setVisibility(View.GONE);

        cardRef2 = FirebaseDatabase.getInstance().getReference("CardsApprov");

        cardRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCards = new ArrayList();
                CardCar card;
               // int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                   // i++;
                  //  if (i > 30) break;
                    card = child.getValue(CardCar.class);
                    arrayListCards.add(card);
                }
                if (arrayListCards.size() > 0) {
                    // Toast.makeText(MainActivityPageUser.this, ""+registerInformation.getCardsUser().get(0).getImageViewArrayListName().get(0), Toast.LENGTH_LONG).show();
                    buttonSeeAll.setVisibility(View.GONE);

                    cardCarAdapter = new CardCarAdapter(MainActivity.this, 0, 0, arrayListCards);
                    //phase 4 reference to listview
                    lv1 = (ListView) findViewById(R.id.lvMange2);
                    lv1.setAdapter(cardCarAdapter);

                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(MainActivity.this, MainActivityCardView.class);
                            intent.putExtra("flagMain", true);
                            pass(intent, arrayListCards.get(i));
                            startActivityForResult(intent, 0);
                        }
                    });
                }
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });



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
        intent.putExtra("flagEdit", true);
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
    private void dialod() {
        d = new Dialog(this);
        d.setContentView(R.layout.login);
        d.setTitle("Login");

        d.setCancelable(true);
        checkBox = (CheckBox) d.findViewById(R.id.checkBoxPhone);
//        if (checkBox.isChecked()) {
//           // checkBox.setChecked(false);
//        }

        editTextEmail = (EditText) d.findViewById(R.id.loginEmail);
        editTextPassword = (EditText) d.findViewById(R.id.loginPassword);
        loginButtonIn = (Button) d.findViewById(R.id.loginButton2);
        textViewWarnAll=(TextView) d.findViewById(R.id.textWarnAll);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    editTextEmail.setText("");
                    editTextEmail.setInputType(3);
                    editTextEmail.setHint("פלאפון");
                    editTextPassword.setVisibility(View.GONE);
                    editTextPassword.setText("");
                } else {
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                    editTextEmail.setInputType(1);
                    editTextEmail.setHint("אימייל");
                    editTextPassword.setVisibility(View.VISIBLE);
                }
            }
        });
        loginButtonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkBox.isChecked()) {//login email

                    if (editTextEmail.getText().toString().isEmpty()||
                            ErrWarn.errEmail(editTextEmail.getText().toString())) {
                        editTextEmail.setError("אימייל לא תקין");
                        editTextEmail.requestFocus();
                        return;
                    }
                    if (editTextPassword.getText().toString().isEmpty()) {
                        editTextPassword.setError("סיסמא לא תקינה");
                        editTextPassword.requestFocus();
                        return;
                    }
                    loginIn();
                }
                else {        //login phome
                    if (flagCode) {//VerificationCode
                        String code = editTextPassword.getText().toString().trim();
                        if (code.isEmpty() || code.length() < 6) {
                            editTextPassword.setError("Enter valid code");
                            editTextPassword.requestFocus();
                            return;
                        } else
                            verifyVerificationCode(code);
                    } else {//send code

                        mobile = editTextEmail.getText().toString().trim();
                        if (mobile.isEmpty() || mobile.length() < 10) {
                            editTextEmail.setError("Enter a valid mobile");
                            editTextEmail.requestFocus();
                            return;
                        } else {
                            flagCode = true;


                            sendVerificationCode(mobile);
                            editTextPassword.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        d.show();

    }

    private void loginIn() {

        String emailLogin = editTextEmail.getText().toString();
       String passwordLogin = editTextPassword.getText().toString();
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
//                                textViewWarnAll.setText("אימייל או סיסמא לא נכונים");
//                                textViewWarnAll.setVisibility(View.VISIBLE);
                                editTextPassword.setError("אימייל או סיסמא לא נכונים");
                                editTextPassword.requestFocus();
                                
                                Toast.makeText(MainActivity.this, "הכניסה נכשלה", Toast.LENGTH_SHORT).show();
                            }
                            // progressDialog.dismiss();
                        }
                    });

        }









    private void setmCallBacks() {
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //Getting the code sent by SMS
                String code = phoneAuthCredential.getSmsCode();
                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    editTextPassword.setText(code);
                    //verifying the code
                    verifyVerificationCode(code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
            }
        };
    }

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential;
        //creating the credential
        Toast.makeText(MainActivity.this, mVerificationId, Toast.LENGTH_LONG).show();
      // editTextEmail.setText(mVerificationId);
      try{
           credential = PhoneAuthProvider.getCredential(mVerificationId, code);
      }
      catch (RuntimeException e){
          editTextPassword.setError("משו השתבש לחץ כניסה שוב");
          editTextPassword.requestFocus();
       return;

      }

        //signing the user
      signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerInformation = null;
                            mobile = mobile.substring(1);
                            DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("RegisterInformation");
                            ref3.orderByChild("email").equalTo("+972" + mobile).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot child : snapshot.getChildren()) {

                                        registerInformation = child.getValue(RegisterInformation.class);
                                    }
                                    if (!snapshot.exists()) {

                                        registerInformation = new RegisterInformation();
                                        registerInformation.setEmail("+972" + mobile);
                                        saveRegisterDataFireBase();
                                    }
                                    else {
                                       d.dismiss();
                                        Intent intent = new Intent(MainActivity.this, MainActivityPageUser.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {

                            //verification unsuccessful.. display an error message

                            editTextPassword.setError("Enter valid code");
                            editTextPassword.requestFocus();

                            return;

                        }
                    }
                });
    }

    private void saveRegisterDataFireBase() {

        DatabaseReference cardRef4 = FirebaseDatabase.getInstance().getReference("RegisterInformation").push();
        cardRef4.setValue(registerInformation);
        d.dismiss();
        Toast.makeText(MainActivity.this, "הייתי פה", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MainActivityPageUser.class);
        startActivity(intent);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+972" + mobile)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(MainActivity.this)
                .setCallbacks(mCallBacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}
