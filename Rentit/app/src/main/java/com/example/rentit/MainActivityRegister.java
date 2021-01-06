package com.example.rentit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MainActivityRegister extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference cardRef;

    private TextView textViewWarnEmail, textViewWarnPassword1, textViewWarnPassword2, textViewWarnAll;
    private FirebaseAuth mAuth;
    private RegisterInformation registerInformation = null;
    private Button loginButtonIn;
    private Button registerButton, buttonPhone, buttonSmsCode;
    private Button returnButton;
    private EditText editTextEmail, editTextPhone, editTextSmsCode;
    private EditText editTextPass1;
    private EditText editTextPass2;
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mobile;

    private CheckBox checkBox;
    private CheckBox checkBoxTerms;

    private Boolean flagCode = false;

    private Dialog d;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        setmCallBacks();//register phone

        checkBoxTerms=findViewById(R.id.checkBoxTerms);
        textViewWarnEmail = findViewById(R.id.textWarnEmail);
        textViewWarnPassword1 = findViewById(R.id.textWarnPassword1);
        textViewWarnPassword2 = findViewById(R.id.textWarnPassword2);
        textViewWarnAll = findViewById(R.id.textWarnAll);

        registerButton = findViewById(R.id.register);
        buttonSmsCode = findViewById(R.id.buttonCodeGo);
        buttonPhone = findViewById(R.id.buttonRegisterPhone);
        returnButton = findViewById(R.id.mainPage);
        editTextSmsCode = findViewById(R.id.editTextSmsCode);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.email);
        editTextPass1 = findViewById(R.id.editTextTextPassword);
        editTextPass2 = findViewById(R.id.password2);
        buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialod();
            }
        });



        registerButton.setOnClickListener(new View.OnClickListener() {//email
            @Override
            public void onClick(View view) {
                register();
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityRegister.this, MainActivity.class);
                startActivity(intent);
            }
        });

        checkBoxTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                textViewWarnAll.setText("תקנון חייב להיות מאושר");
                checkBoxTerms.setChecked(true);
                textViewWarnAll.setVisibility(View.VISIBLE);


            }
        });
    }


    private void saveRegisterDataFireBaseEmail() {
        cardRef = database.getReference("RegisterInformation").push();
        cardRef.setValue(registerInformation);
        Intent intent = new Intent(MainActivityRegister.this, MainActivityPageUser.class);
        startActivity(intent);
    }


    private void registerFirebase(String email, String password) {
        final String TAG = "tag";

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            saveRegisterDataFireBaseEmail();
                        } else {
                            textViewWarnAll.setText("אימייל או סיסמא לא תקינים");
                            textViewWarnAll.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    public void register() {//warng err
        textViewWarnPassword2.setVisibility(View.GONE);
        textViewWarnPassword1.setVisibility(View.GONE);
        textViewWarnAll.setVisibility(View.GONE);
        textViewWarnEmail.setVisibility(View.GONE);

        String email = editTextEmail.getText().toString();
        if (ErrWarn.errEmail(email)) {
            textViewWarnEmail.setText("אימייל לא קיים");
            textViewWarnEmail.setVisibility(View.VISIBLE);
        } else {
            String pass1 = "" + editTextPass1.getText().toString();
            if (pass1.length() < 6) {
                textViewWarnPassword1.setText("סיסמא קצרה מדי, לפחות 6 תווים");
                textViewWarnPassword1.setVisibility(View.VISIBLE);
            } else {
                String pass2 = editTextPass2.getText().toString();
                if (pass1.equals(pass2)) {

                    registerInformation = new RegisterInformation();
                    registerInformation.setEmail(email);
                    registerInformation.setPassword(pass1);
                    registerFirebase(email, pass1);            //register


                } else {
                    textViewWarnPassword2.setText("סיסמא לא תואמת");
                    textViewWarnPassword2.setVisibility(View.VISIBLE);
                }
            }
        }
    }



    private void dialod() {

        d = new Dialog(this);
        d.setContentView(R.layout.login);
        d.setTitle("Login");

        d.setCancelable(true);
        checkBox = (CheckBox) d.findViewById(R.id.checkBoxPhone);

        checkBox.setChecked(true);
        checkBox.setVisibility(View.GONE);

        editTextEmail = (EditText) d.findViewById(R.id.loginEmail);
        editTextPassword = (EditText) d.findViewById(R.id.loginPassword);
        loginButtonIn = (Button) d.findViewById(R.id.loginButton2);
        editTextEmail.setText("");
        editTextEmail.setInputType(3);
        editTextEmail.setHint("פלאפון");
        editTextPassword.setVisibility(View.GONE);
        editTextPassword.setText("");
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

            }
        });
        loginButtonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (flagCode) {
                    String code = editTextPassword.getText().toString().trim();
                    if (code.isEmpty() || code.length() < 6) {
                        editTextPassword.setError("Enter valid code");
                        editTextPassword.requestFocus();
                        return;
                    } else
                        verifyVerificationCode(code);
                } else {

                    mobile = editTextEmail.getText().toString().trim();
                    if (mobile.isEmpty() || mobile.length() < 10) {
                        editTextEmail.setError("Enter a valid mobile");
                        editTextEmail.requestFocus();
                        return;
                    } else {
                        mobile = mobile;

                        sendVerificationCode(mobile);
                        editTextPassword.setVisibility(View.VISIBLE);
                    }
                }
            }

        });
        d.show();

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
                Toast.makeText(MainActivityRegister.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                flagCode = true;
                mVerificationId = s;


                //  mResendToken = forceResendingToken;
            }
        };
    }

    private void verifyVerificationCode(String code) {

        PhoneAuthCredential credential;
        //creating the credential
        // editTextEmail.setText(mVerificationId);
        try {
            credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        } catch (RuntimeException e) {
            editTextPassword.setError("משו השתבש לחץ כניסה שוב");
            editTextPassword.requestFocus();
            return;

        }

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivityRegister.this, new OnCompleteListener<AuthResult>() {
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
                                    } else {
                                        d.dismiss();
                                        Intent intent = new Intent(MainActivityRegister.this, MainActivityPageUser.class);

                                        startActivity(intent);


                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            //verification successful we will start the profile activity

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
        Intent intent = new Intent(MainActivityRegister.this, MainActivityPageUser.class);
        startActivity(intent);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+972" + mobile)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(MainActivityRegister.this)
                .setCallbacks(mCallBacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_app,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent2;
        switch(item.getItemId()){
            case R.id.mainMenuRegister:
                intent2 = new Intent(MainActivityRegister.this, MainActivity.class);
                startActivity(intent2);
                return true;
            case R.id.mainIconMenuRegister:
                intent2 = new Intent(MainActivityRegister.this, MainActivity.class);
                startActivity(intent2);
                return true;

            default:   return super.onOptionsItemSelected(item);
        }
    }


}