package com.example.rentit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivityRegister extends AppCompatActivity {
    private FirebaseDatabase database;
int a;
    private DatabaseReference cardRef;
    private TextView textViewWarnEmail, textViewWarnPassword1, textViewWarnPassword2, textViewWarnAll;

    private FirebaseAuth mAuth;
    private RegisterInformation registerInformation;

    private Button registerButton;
    private Button returnButton;
    private EditText editTextEmail;
    private EditText editTextPass1;
    private EditText editTextPass2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        textViewWarnEmail = findViewById(R.id.textWarnEmail);
        textViewWarnPassword1 = findViewById(R.id.textWarnPassword1);
        textViewWarnPassword2 = findViewById(R.id.textWarnPassword2);
        textViewWarnAll = findViewById(R.id.textWarnAll);


        registerButton = findViewById(R.id.register);
        returnButton = findViewById(R.id.mainPage);
        editTextEmail = findViewById(R.id.email);
        editTextPass1 = findViewById(R.id.editTextTextPassword);
        editTextPass2 = findViewById(R.id.password2);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();

                //   saveRegisterDataFireBase();
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityRegister.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void saveRegisterDataFireBase() {


        cardRef = database.getReference("RegisterInformation").push();


        cardRef.setValue(registerInformation);

        Toast.makeText(MainActivityRegister.this, "הייתי פה", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivityRegister.this, MainActivityPageUser.class);
        startActivity(intent);
    }



    private void registerFirebase(String email, String password) {
        final String TAG = "tag";
        Toast.makeText(MainActivityRegister.this, email, Toast.LENGTH_SHORT).show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            saveRegisterDataFireBase();
                        } else {
                            textViewWarnAll.setText("אימייל או סיסמא לא תקינים");
                            textViewWarnAll.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    public void register() {
        textViewWarnPassword2.setVisibility(View.GONE);
        textViewWarnPassword1.setVisibility(View.GONE);
        textViewWarnAll.setVisibility(View.GONE);
        textViewWarnEmail.setVisibility(View.GONE);

        String email = editTextEmail.getText().toString();
        if (ErrWarn.errEmail(email)) {
            textViewWarnEmail.setText("אימייל לא קיים");
            textViewWarnEmail.setVisibility(View.VISIBLE);
        }else {
            String pass1 =""+ editTextPass1.getText().toString();
            if (pass1.length() < 6) {
                textViewWarnPassword1.setText("סיסמא קצרה מדי, לפחות 6 תווים");
                textViewWarnPassword1.setVisibility(View.VISIBLE);
            }
            else {
                String pass2 = editTextPass2.getText().toString();
                if (pass1.equals(pass2)) {

                    registerInformation = new RegisterInformation();
                    registerInformation.setEmail(email);
                    registerInformation.setPassword(pass1);
                    registerFirebase(email, pass1);


                } else {
                    textViewWarnPassword2.setText("סיסמא לא תואמת");
                    textViewWarnPassword2.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}