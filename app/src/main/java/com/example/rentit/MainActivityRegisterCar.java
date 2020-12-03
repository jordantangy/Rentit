package com.example.rentit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class MainActivityRegisterCar extends AppCompatActivity {
    private RegisterInformation registerInformation;
    private ArrayList<String> arrayListImageName;


    private DatabaseReference cardRef2;


    private DatabaseReference cardRef;

    // private ProgressDialog progressDialog;

    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    private TextView textViewWarnPhone, textViewWarnName, textViewWarnPrice, textViewWarnAll;
    private TextView textViewWarnTypeCar, textViewWarnYearCar, textViewWarnInsurance, textViewWarnArea;
    private TextView textViewWarnDateStart, textViewWarnDateEnd, textViewWarnCity, textViewWarnRemarker, textViewWarnImage;


    private CardCar cardCar = new CardCar();
    ArrayList<Uri> arrayListImageViewUri = new ArrayList<>();
    ArrayList<String> areaList = new ArrayList<>();
    ArrayList<ImageView> arrayListImageView = new ArrayList<>();
    private ImageView imageViewTemp = null;
    private ImageView imageView1;
    int k;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private EditText editTextCity;
    private EditText editTextName;
    private EditText editTextTypeCar;
    private EditText editTextInsurance;
    private EditText editTextYearCar;
    private EditText editTextPriceForDay;
    private EditText editTextPhone;
    private EditText editTextDateStart;
    private EditText editTextDateEnd;
    private EditText editTextRemarks;
    private EditText editTextRejection;


    private Button buttonRegister, buttonDelete, buttonUpdate;
    private Button buttonMyPage;
    private Spinner areaSpinner;

    private String email;
    private String key = "ggg";
    private String key2 = "vvv";
    private String keyCard = "0";

    private int flagI;
    private int num;
    private String url = "";
    private int i;
    private String area = "";
    private boolean flag = false;
    private CardCar cardCarEdit;
    private boolean flagEdit;
    private boolean flagMain;
    private boolean flagManagementCardsApprov;

    private Bundle bundle;
    private DatabaseReference mDatabase;
    private String removeImage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register_car);
        flagManagementCardsApprov = false;
        flagEdit = false;
        flagMain = false;
        try {
            bundle = getIntent().getExtras();
            flagManagementCardsApprov = bundle.getBoolean("flagManagement");
        } catch (RuntimeException e) {
        }
        try {
            bundle = getIntent().getExtras();
            flagMain = bundle.getBoolean("flagMain");
        } catch (RuntimeException e) {
        }
        try {
            bundle = getIntent().getExtras();
            flagEdit = bundle.getBoolean("flagEdit");
        } catch (RuntimeException e) {
        }


        setTextViewWarn();
        setEditTxtId();
        if (flagEdit || flagMain) {
            cardCarEdit = new CardCar();
            setCard();
            setTextCard();
        }
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null)
            email = firebaseUser.getEmail().toString();
        // progressDialog = new ProgressDialog(this);


        setListSpinerArea();
        setSpinerArea();

        buttonDelete = findViewById(R.id.buttonDelete);
        buttonMyPage = findViewById(R.id.buttonMyPage);
        buttonRegister = findViewById(R.id.buttonReturnMainPage);

        if (flagManagementCardsApprov) {
            editTextRejection = findViewById(R.id.editTextRejection);
            buttonDelete.setText("סירוב");
            buttonDelete.setVisibility(View.VISIBLE);
            buttonRegister.setText("אישור");
            buttonMyPage.setText("לעמוד הראשי");

        }
        if (flagEdit) {
            buttonRegister.setText("לשינוי פרטים");
            buttonDelete = findViewById(R.id.buttonDelete);
            buttonDelete.setVisibility(View.VISIBLE);
        }
        if (flagEdit || flagManagementCardsApprov) {
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flagManagementCardsApprov) {
                        cardsRejection();


                    } else
                        deleteCard();

                }
            });
        }
        if (flagMain && !flagManagementCardsApprov) {
            buttonRegister.setVisibility(View.GONE);
            buttonDelete = findViewById(R.id.buttonDelete);
            buttonDelete.setVisibility(View.GONE);
            buttonUpdate = findViewById(R.id.upgrade3);
            buttonUpdate.setVisibility(View.GONE);
            buttonMyPage.setText("לעמוד הראשי");

        }

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flagManagementCardsApprov) {
                    cardsApprov();

                } else {
                    setWarnGone();
                    setCardDetails();
                    if (!flag) retrieveData();
                }

            }
        });


        arrayListImageView.add(imageView1);
        arrayListImageView.add(imageView2);
        arrayListImageView.add(imageView3);
        arrayListImageView.add(imageView4);

        if (!flagMain) {
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flagEdit) removeImage = cardCarEdit.getImageViewArrayListName().get(0);
                    openImage(imageView1);

                }
            });
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flagEdit && cardCarEdit.getImageViewArrayListName().size() > 1)
                        removeImage = cardCarEdit.getImageViewArrayListName().get(1);
                    openImage(imageView2);

                }
            });
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flagEdit && cardCarEdit.getImageViewArrayListName().size() > 2)
                        removeImage = cardCarEdit.getImageViewArrayListName().get(2);
                    openImage(imageView3);

                }
            });
            imageView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flagEdit && cardCarEdit.getImageViewArrayListName().size() > 3)
                        removeImage = cardCarEdit.getImageViewArrayListName().get(3);
                    openImage(imageView4);

                }
            });
        }

        buttonMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flagMain) {
                    Intent intent = new Intent(MainActivityRegisterCar.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivityRegisterCar.this, MainActivityPageUser.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void cardsApprov() {
        cardCarEdit.setPermissionToPublish(1);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CardsApprov").push();
        final String keyApprov = ref.getKey();
        ref.setValue(cardCarEdit);
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("CardsWaitApprov");
        ref2.child(cardCarEdit.getKey()).removeValue();

        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("RegisterInformation");
        ref3.child("cardsUser").orderByChild("email").equalTo(cardCarEdit.getEmail());
        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(MainActivityRegisterCar.this, snapshot.toString(), Toast.LENGTH_LONG).show();
                editTextRemarks.setText(snapshot.toString());
                for (DataSnapshot child : snapshot.getChildren()) {
                    registerInformation = child.getValue(RegisterInformation.class);
                    key2 = child.getKey();
                }
                editTextRemarks.setText("lll" + registerInformation.getEmail());

                List<CardCar> cardCarList = registerInformation.getCardsUser();
                for (int j = 0; j < cardCarList.size(); j++) {
                    if (cardCarList.get(j).getId() == cardCarEdit.getId()) {
                        registerInformation.getCardsUser().get(j).setPermissionToPublish(1);
                        registerInformation.getCardsUser().get(j).setKey(keyApprov);
                        editTextRemarks.setText("lll" + registerInformation.getCardsUser().get(j).getPermissionToPublish());
                        break;
                    }
                }
                DatabaseReference cardR = FirebaseDatabase.getInstance().getReference();
                cardR.child("RegisterInformation").child(key2).setValue(registerInformation);

                Intent intent = new Intent(MainActivityRegisterCar.this, MainActivityPageUser.class);
                startActivity(intent);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void cardsRejection() {
        cardCarEdit.setPermissionToPublish(2);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CardsRejection").push();
        final String keyRejection = ref.getKey();
        ref.setValue(cardCarEdit);
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("CardsWaitApprov");
        ref2.child(cardCarEdit.getKey()).removeValue();


        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("RegisterInformation");
        ref3.child("cardsUser").orderByChild("email").equalTo(cardCarEdit.getEmail());
        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(MainActivityRegisterCar.this, snapshot.toString(), Toast.LENGTH_LONG).show();
                editTextRemarks.setText(snapshot.toString());
                for (DataSnapshot child : snapshot.getChildren()) {
                    registerInformation = child.getValue(RegisterInformation.class);
                    key2 = child.getKey();
                }
                editTextRemarks.setText("lll" + registerInformation.getEmail());

                List<CardCar> cardCarList = registerInformation.getCardsUser();
                for (int j = 0; j < cardCarList.size(); j++) {
                    if (cardCarList.get(j).getId() == cardCarEdit.getId()) {
                        registerInformation.getCardsUser().get(j).setPermissionToPublish(2);
                        registerInformation.getCardsUser().get(j).setKey(keyRejection);
                        editTextRemarks.setText("lll" + registerInformation.getCardsUser().get(j).getPermissionToPublish());
                        break;
                    }
                }
                DatabaseReference cardR = FirebaseDatabase.getInstance().getReference();
                cardR.child("RegisterInformation").child(key2).setValue(registerInformation);

                Intent intent = new Intent(MainActivityRegisterCar.this, MainActivityPageUser.class);
                startActivity(intent);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void setTextCard() {
        List<String> urlImage = cardCarEdit.getImageViewArrayListName();
        cardCar.setImageViewArrayListName(urlImage);
        Glide.with(this).load(urlImage.get(0)).into(imageView1);
        if (urlImage.size() > 1) {
            Glide.with(this).load(urlImage.get(1)).into(imageView2);
        }
        if (urlImage.size() > 2) {
            Glide.with(this).load(urlImage.get(2)).into(imageView3);
        }
        if (urlImage.size() > 3) {
            Glide.with(this).load(urlImage.get(3)).into(imageView4);
        }


        editTextCity.setText(cardCarEdit.getCity());
        editTextName.setText(cardCarEdit.getName());
        editTextTypeCar.setText(cardCarEdit.getTypeCar());
        editTextInsurance.setText(cardCarEdit.getInsurance());
        editTextYearCar.setText(cardCarEdit.getYearCar());
        editTextPriceForDay.setText(cardCarEdit.getPriceDay());
        editTextPhone.setText(cardCarEdit.getPhone());
        editTextDateStart.setText(cardCarEdit.getDateStart());
        editTextDateEnd.setText(cardCarEdit.getDateEnd());
        editTextRemarks.setText(cardCarEdit.getRemarks());
    }

    private void setEditTxtId() {
        imageView1 = findViewById(R.id.image13);
        imageView2 = findViewById(R.id.image23);
        imageView3 = findViewById(R.id.image33);
        imageView4 = findViewById(R.id.image43);
        editTextCity = findViewById(R.id.city3);
        editTextName = findViewById(R.id.nameMen3);
        editTextTypeCar = findViewById(R.id.typeCar3);
        editTextInsurance = findViewById(R.id.insurance3);
        editTextYearCar = findViewById(R.id.yearCar3);
        editTextPriceForDay = findViewById(R.id.manyDay3);
        editTextPhone = findViewById(R.id.phone3);
        editTextDateStart = findViewById(R.id.startDate23);
        editTextDateEnd = findViewById(R.id.endDate23);
        editTextRemarks = findViewById(R.id.remarksRegisterCar3);
    }

    private void setWarnGone() {
        textViewWarnName.setVisibility(View.GONE);
        textViewWarnAll.setVisibility(View.GONE);
        textViewWarnArea.setVisibility(View.GONE);
        textViewWarnCity.setVisibility(View.GONE);
        textViewWarnDateEnd.setVisibility(View.GONE);
        textViewWarnDateStart.setVisibility(View.GONE);
        textViewWarnImage.setVisibility(View.GONE);
        textViewWarnInsurance.setVisibility(View.GONE);
        textViewWarnPhone.setVisibility(View.GONE);
        textViewWarnPrice.setVisibility(View.GONE);
        textViewWarnRemarker.setVisibility(View.GONE);
        textViewWarnTypeCar.setVisibility(View.GONE);
        textViewWarnYearCar.setVisibility(View.GONE);
    }

    private void setTextViewWarn() {

        textViewWarnName = findViewById(R.id.warnName);
        textViewWarnAll = findViewById(R.id.warnAll);
        textViewWarnArea = findViewById(R.id.warnArea);
        textViewWarnCity = findViewById(R.id.warnCity);
        textViewWarnDateEnd = findViewById(R.id.warnDataEnd);
        textViewWarnDateStart = findViewById(R.id.warnDateStart);
        textViewWarnImage = findViewById(R.id.warnImage);
        textViewWarnInsurance = findViewById(R.id.warnInsurance);
        textViewWarnPhone = findViewById(R.id.warnPhone);
        textViewWarnPrice = findViewById(R.id.warnPrice);
        textViewWarnRemarker = findViewById(R.id.warnRemarker);
        textViewWarnTypeCar = findViewById(R.id.warnTypeCar);
        textViewWarnYearCar = findViewById(R.id.warnYearCar);


    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }


    private void setCardDetails() {
        flag = false;
        boolean flag2 = false;

        if (!flagEdit) {
            flag2 = ErrWarn.errImage(area, arrayListImageViewUri.size(), textViewWarnImage);
            if (!flag && flag2) flag = true;
        }

        flag2 = ErrWarn.errArea(area, textViewWarnArea);
        if (!flag && flag2) flag = true;
        cardCar.setArea(area);

        String edit = editTextName.getText().toString();
        flag2 = ErrWarn.errName(edit, textViewWarnName);
        if (!flag && flag2) flag = true;
        cardCar.setName(edit);

        edit = editTextCity.getText().toString();
        flag2 = ErrWarn.errCity(edit, textViewWarnCity);
        if (!flag && flag2) flag = true;
        cardCar.setCity(edit);

        edit = editTextDateStart.getText().toString();
        flag2 = ErrWarn.errStartDate(edit, textViewWarnDateStart);
        if (!flag && flag2) flag = true;
        cardCar.setDateStart(edit);

        edit = editTextDateEnd.getText().toString();
        flag2 = ErrWarn.errEndData(edit, textViewWarnDateEnd);
        if (!flag && flag2) flag = true;
        cardCar.setDateEnd(edit);

        edit = editTextInsurance.getText().toString();
        flag2 = ErrWarn.errInsurance(edit, textViewWarnInsurance);
        if (!flag && flag2) flag = true;
        cardCar.setInsurance(edit);

        edit = editTextPhone.getText().toString();
        flag2 = ErrWarn.errPhone(edit, textViewWarnPhone);
        if (!flag && flag2) flag = true;
        cardCar.setPhone(edit);

        edit = editTextPriceForDay.getText().toString();
        flag2 = ErrWarn.errPrice(edit, textViewWarnPrice);
        if (!flag && flag2) flag = true;
        cardCar.setPriceDay(edit);

        edit = editTextRemarks.getText().toString();
        flag2 = ErrWarn.errRemarker(edit, textViewWarnRemarker);
        if (!flag && flag2) flag = true;
        cardCar.setRemarks(edit);

        edit = editTextTypeCar.getText().toString();
        flag2 = ErrWarn.errTypeCar(edit, textViewWarnTypeCar);
        if (!flag && flag2) flag = true;
        cardCar.setTypeCar(edit);

        edit = editTextYearCar.getText().toString();
        flag2 = ErrWarn.errYearCar(edit, textViewWarnYearCar);
        if (!flag && flag2) flag = true;
        cardCar.setYearCar(edit);

        if (flag) {
            textViewWarnAll.setText("אחד הנתונים לא תקין");
            textViewWarnAll.setVisibility(View.VISIBLE);
        }
    }

    public void openImage(ImageView imageView) {
        imageViewTemp = imageView;

        try {
            if (ActivityCompat.checkSelfPermission(MainActivityRegisterCar.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivityRegisterCar.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    arrayListImageViewUri.add(selectedImage);
                    imageViewTemp.setImageURI(selectedImage);
                    arrayListImageView.add(imageViewTemp);
                    if (flagEdit && removeImage.length() > 0) {
                        cardCar.removeImageViewArrayListName(removeImage);
                        removeImage = "";
                    }
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    arrayListImageViewUri.add(selectedImage);
                    imageViewTemp.setImageURI(selectedImage);
                    arrayListImageView.add(imageViewTemp);
                    if (flagEdit && removeImage.length() > 0) {
                        cardCar.removeImageViewArrayListName(removeImage);
                        removeImage = "";
                    }
                }
                break;
        }
    }


    public void setListSpinerArea() {
        areaSpinner = findViewById(R.id.spinnerArea3);
        if (flagEdit) areaList.add(cardCarEdit.getArea());
        else areaList.add("בחר אזור");
        areaList.add("צפון");
        areaList.add("מרכז");
        areaList.add("דרום");
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, areaList);
        areaSpinner.setAdapter(areaAdapter);
    }

    public void setSpinerArea() {
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


    public void retrieveData() {


        cardRef = FirebaseDatabase.getInstance().getReference();
        cardRef2 = FirebaseDatabase.getInstance().getReference("RegisterInformation");

        cardRef2.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    registerInformation = child.getValue(RegisterInformation.class);
                    key2 = child.getKey();

                }
                cardCar.setId(registerInformation.getId());
                upladUriFirebase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void upladUriFirebase() {
        cardCar.setEmail(email);
        cardCar.setNumImage(arrayListImageViewUri.size());

        arrayListImageName = new ArrayList<String>();
        registerInformation.setId(registerInformation.getId() + 1);
        if (flagEdit) cardCar.setImageViewArrayListName(cardCarEdit.getImageViewArrayListName());

        flagI = 0;
        final int length = arrayListImageViewUri.size();
        if (flagEdit && length == 0) {
            DatabaseReference cardRef5 = FirebaseDatabase.getInstance().getReference("CardsWaitApprov").push();
            cardCar.setKey(cardRef.toString());
            cardRef5.child(keyCard).setValue(cardCar);
            registerInformation.addCardcar(cardCar);

            cardRef.child("RegisterInformation").child(key2).setValue(registerInformation);

            Toast.makeText(MainActivityRegisterCar.this, "כרטיס נשמר בהצלחה", Toast.LENGTH_SHORT).show();

            deleteCard();
        } else {
            for (i = 0; i < length && i < 4; i++) {


                num = i + 1;
                String s = registerInformation.getEmail() + "/" + registerInformation.getId() + "" + num + ".jpg";
                final StorageReference riversRef = mStorageRef.child(s);
                riversRef.putFile(arrayListImageViewUri.get(i))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        url = uri.toString();
                                        flagI++;
                                        if (!keyCard.equals("0"))
                                            registerInformation.getCardsUser().get(registerInformation.getCardsUser().size() - 1).addImageViewArrayListName(url);
                                        cardCar.addImageViewArrayListName(url);


                                        if (keyCard.equals("0")) {
                                            DatabaseReference cardRef4 = FirebaseDatabase.getInstance().getReference("CardsWaitApprov").push();
                                            keyCard = cardRef4.getKey();
                                            cardCar.setKey(keyCard);
                                            registerInformation.addCardcar(cardCar);
                                            cardRef4.setValue(cardCar);
                                        } else {
                                            DatabaseReference cardRef5 = FirebaseDatabase.getInstance().getReference("CardsWaitApprov");
                                            cardRef5.child(keyCard).setValue(cardCar);
                                        }

                                        cardRef.child("RegisterInformation").child(key2).setValue(registerInformation);
                                        Toast.makeText(MainActivityRegisterCar.this, "כרטיס נשמר בהצלחה", Toast.LENGTH_SHORT).show();

                                        if (flagEdit) deleteCard();
                                        else if (flagI == length) {
                                            Intent intent = new Intent(MainActivityRegisterCar.this, MainActivityPageUser.class);
                                            startActivity(intent);
                                        }


                                    }
                                });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(MainActivityRegisterCar.this, "תמונה לא נשמרה", Toast.LENGTH_SHORT).show();
                            }
                        });
                //   Toast.makeText(MainActivityRegisterCar.this, "צלחה"+riversRef.getDownloadUrl().toString(), Toast.LENGTH_SHORT).show();

            }
        }


        // finish();
    }


    private void setCard() {
        Bundle bundle = getIntent().getExtras();
        cardCarEdit.setKey(bundle.getString("key"));
        cardCarEdit.setEmail(bundle.getString("email"));
        cardCarEdit.setArea(bundle.getString("area"));
        cardCarEdit.setNumImage(bundle.getInt("numImage"));
        cardCarEdit.setCity(bundle.getString("city"));
        cardCarEdit.setPhone(bundle.getString("phone"));
        cardCarEdit.setYearCar(bundle.getString("yearCar"));
        cardCarEdit.setTypeCar(bundle.getString("typeCar"));
        cardCarEdit.setRemarks(bundle.getString("remarks"));
        cardCarEdit.setPriceDay(bundle.getString("priceDay"));
        cardCarEdit.setDateEnd(bundle.getString("dateEnd"));
        cardCarEdit.setDateStart(bundle.getString("dateStart"));
        cardCarEdit.setName(bundle.getString("name"));
        cardCarEdit.setInsurance(bundle.getString("insurance"));
        cardCarEdit.setId(bundle.getInt("id"));
        cardCarEdit.setPermissionToPublish(bundle.getInt("permissionToPublish"));

        for (int i = 1; i <= cardCarEdit.getNumImage(); i++)
            cardCarEdit.addImageViewArrayListName(bundle.getString("image" + i));

    }

    public void deleteCard() {

        mDatabase = FirebaseDatabase.getInstance().getReference("RegisterInformation");

        cardRef2 = FirebaseDatabase.getInstance().getReference("RegisterInformation");

        cardRef2.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    registerInformation = child.getValue(RegisterInformation.class);
                    key = child.getKey();

                }

                for (int i = 0; i < registerInformation.getCardsUser().size(); i++) {
                    if (cardCarEdit.getId() == registerInformation.getCardsUser().get(i).getId()) {
                        registerInformation.removeCardCar(i);
                        Toast.makeText(MainActivityRegisterCar.this, key, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                mDatabase.child(key).setValue(registerInformation);
                if (cardCarEdit.getPermissionToPublish() == 0) {
                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("CardsWaitApprov");
                    ref2.child(cardCarEdit.getKey()).removeValue();
                } else if (cardCarEdit.getPermissionToPublish() == 1) {
                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("CardsApprov");
                    ref2.child(cardCarEdit.getKey()).removeValue();
                } else if (cardCarEdit.getPermissionToPublish() == 2) {
                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("CardsRejection");
                    ref2.child(cardCarEdit.getKey()).removeValue();
                }
                Intent intent = new Intent(MainActivityRegisterCar.this, MainActivityPageUser.class);
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}