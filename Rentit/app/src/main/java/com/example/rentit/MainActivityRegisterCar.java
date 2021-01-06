package com.example.rentit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class MainActivityRegisterCar extends AppCompatActivity {
    private RegisterInformation registerInformation;


    private DatabaseReference cardRef2;
    private DatabaseReference cardRef;

    private ProgressDialog progressDialog;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    private TextView textViewWarnAll;
    private TextView textViewWarnArea;
    private TextView textViewWarnImage;


    private CardCar cardCar = new CardCar();
    private ArrayList<Uri> arrayListImageViewUri = new ArrayList<>();
    private ArrayList<String> areaList = new ArrayList<>();
    private ImageView imageViewTemp = null;
    private Uri uri1 = null;
    private Uri uri2 = null;
    private Uri uri3 = null;
    private Uri uri4 = null;
    private Uri uriSave = null;
    private int numImage = 0;


    private ImageView imageView1;
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
    private String url = "";
    private String area = "";
    private boolean flag = false;
    private CardCar cardCarEdit;
    private boolean flagEdit;
    private boolean flagMain;
    private boolean flagManagementCardsApprov;

    private Bundle bundle;
    private DatabaseReference mDatabase;
    private String removeImage = "";
    private int flagImageDeleteConfirmation = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register_car);

        progressDialog = new ProgressDialog(this);
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
        try {
            if (!firebaseUser.getEmail().toString().isEmpty())
                email = firebaseUser.getEmail().toString();
            else {
                email = firebaseUser.getPhoneNumber().toString();
                editTextPhone.setText(email);
            }
        } catch (RuntimeException e) {
            email = firebaseUser.getPhoneNumber().toString();
            editTextPhone.setText(email);
        }

        progressDialog = new ProgressDialog(this);


        setListSpinerArea();
        setSpinerArea();

        buttonDelete = findViewById(R.id.buttonDelete);
        buttonMyPage = findViewById(R.id.buttonMyPage);
        buttonRegister = findViewById(R.id.buttonReturnMainPage);

        if (flagManagementCardsApprov) {
            editTextRemarks.setHint("הסבר סירוב");
            editTextRejection = findViewById(R.id.editTextRejection);
            if (cardCarEdit.getPermissionToPublish() == 0) {
                buttonRegister.setText("אישור");
                buttonDelete.setText("סירוב");
                buttonDelete.setVisibility(View.VISIBLE);
            } else {
                email = cardCarEdit.getEmail();
                buttonDelete.setText("מחיקת כרטיס");
                buttonDelete.setVisibility(View.VISIBLE);
                buttonRegister.setVisibility(View.GONE);
            }
            buttonMyPage.setText("לעמוד שלי");

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
                    if (!buttonDelete.getText().equals("מחיקת כרטיס")) {
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("שומר כרטיס אנא חכה להשלמת התהליך...");

                if (flagManagementCardsApprov) {
                    cardsApprov();

                } else {
                    setWarnGone();


                    setCardDetails();

                    if (!flag) retrieveData();
                }

            }
        });


        if (!flagMain) {
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numImage = 1;
                    if (flagEdit && flagImageDeleteConfirmation != 1) {
                        dialogeImageDeleteConfirmation();
                        //           removeImage = cardCarEdit.getImageViewArrayListName().get(0);
                    }
                    if (!flagEdit || flagImageDeleteConfirmation == 1) openImage(imageView1, uri1);

                }
            });
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numImage = 2;
                    if (flagEdit && flagImageDeleteConfirmation != 1) {
                        dialogeImageDeleteConfirmation();
                        //           removeImage = cardCarEdit.getImageViewArrayListName().get(0);
                    }
                    if (!flagEdit || flagImageDeleteConfirmation == 1) openImage(imageView2, uri1);

                }
            });
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numImage = 3;
                    if (flagEdit && flagImageDeleteConfirmation != 1) {
                        dialogeImageDeleteConfirmation();
                        //           removeImage = cardCarEdit.getImageViewArrayListName().get(0);
                    }
                    if (!flagEdit || flagImageDeleteConfirmation == 1) openImage(imageView3, uri1);

                }
            });
            imageView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numImage = 4;
                    if (flagEdit && flagImageDeleteConfirmation != 1) {
                        dialogeImageDeleteConfirmation();
                        //           removeImage = cardCarEdit.getImageViewArrayListName().get(0);
                    }
                    if (!flagEdit || flagImageDeleteConfirmation == 1) openImage(imageView4, uri1);

                }
            });
        }
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//        calendar.clear();
//        Long today = MaterialDatePicker.todayInUtcMilliseconds();

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        // MaterialDatePicker.Builder builder=MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("בחר תאריך");
        final MaterialDatePicker materialDatePicker = builder.build();

        final Button buttonData = findViewById(R.id.buttonDate);
        buttonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair selectedDates = (Pair) materialDatePicker.getSelection();
//              then obtain the startDate & endDate from the range
                final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
//              assigned variables
                Date startDate = rangeDate.first;
                Date endDate = rangeDate.second;
//              Format the dates in ur desired display mode

                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
//              Display it by setText
                cardCar.setDateStart(simpleFormat.format(startDate).toString());
                cardCar.setDateEnd(simpleFormat.format(endDate).toString());

                buttonData.setTextColor((int)R.color.colorBlack);
                buttonData.setText(" " + simpleFormat.format(startDate) + " Second : " + simpleFormat.format(endDate));

            }
        });

        buttonMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivityRegisterCar.this, MainActivityPageUser.class);
                startActivity(intent);

            }
        });

    }


    private void cardsApprov() {
        cardCarEdit.setPermissionToPublish(1);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CardsApprov").push();
        final String keyApprov = ref.getKey();
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("CardsWaitApprov");
        ref2.child(cardCarEdit.getKey()).removeValue();
        cardCarEdit.setKey(keyApprov);
        ref.setValue(cardCarEdit);

        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("RegisterInformation");
        ref3.orderByChild("email").equalTo(cardCarEdit.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Toast.makeText(MainActivityRegisterCar.this, snapshot.toString(), Toast.LENGTH_LONG).show();
                for (DataSnapshot child : snapshot.getChildren()) {
                    registerInformation = child.getValue(RegisterInformation.class);
                    key2 = child.getKey();
                }
                //  editTextRemarks.setText("lll" + registerInformation.getEmail());

                List<CardCar> cardCarList = registerInformation.getCardsUser();
                for (int j = 0; j < cardCarList.size(); j++) {
                    if (cardCarList.get(j).getId() == cardCarEdit.getId()) {
                        registerInformation.getCardsUser().get(j).setPermissionToPublish(1);
                        registerInformation.getCardsUser().get(j).setKey(keyApprov);
                        // editTextRemarks.setText("lll" + registerInformation.getCardsUser().get(j).getPermissionToPublish());
                        break;
                    }
                }
                DatabaseReference cardR = FirebaseDatabase.getInstance().getReference();
                cardR.child("RegisterInformation").child(key2).setValue(registerInformation);
                progressDialog.dismiss();
                Intent intent = new Intent(MainActivityRegisterCar.this, MainActivityPageUser.class);
                startActivity(intent);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void cardsRejection() {
        cardCarEdit.setRejection(editTextRemarks.getText().toString());
        cardCarEdit.setPermissionToPublish(2);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CardsRejection").push();
        final String keyRejection = ref.getKey();
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("CardsWaitApprov");
        ref2.child(cardCarEdit.getKey()).removeValue();
        cardCarEdit.setKey(keyRejection);
        ref.setValue(cardCarEdit);


        DatabaseReference cardRef2 = FirebaseDatabase.getInstance().getReference("RegisterInformation");

        cardRef2.orderByChild("email").equalTo(cardCarEdit.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Toast.makeText(MainActivityRegisterCar.this, snapshot.toString(), Toast.LENGTH_LONG).show();
                //   editTextRemarks.setText(snapshot.toString());
                for (DataSnapshot child : snapshot.getChildren()) {
                    registerInformation = child.getValue(RegisterInformation.class);
                    key2 = child.getKey();
                }


                List<CardCar> cardCarList = registerInformation.getCardsUser();
                for (int j = 0; j < cardCarList.size(); j++) {
                    if (cardCarList.get(j).getId() == cardCarEdit.getId()) {
                        registerInformation.getCardsUser().get(j).setPermissionToPublish(2);
                        registerInformation.getCardsUser().get(j).setRejection(
                                editTextRemarks.getText().toString());

                        registerInformation.getCardsUser().get(j).setKey(keyRejection);
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
        Glide.with(this).load(urlImage.get(0)).into(imageView4);
        if (urlImage.size() > 1) {
            Glide.with(this).load(urlImage.get(1)).into(imageView2);
        }
        if (urlImage.size() > 2) {
            Glide.with(this).load(urlImage.get(2)).into(imageView3);
        }
        if (urlImage.size() > 3) {
            Glide.with(this).load(urlImage.get(3)).into(imageView1);
        }


        editTextCity.setText(cardCarEdit.getCity());
        editTextName.setText(cardCarEdit.getName());
        editTextTypeCar.setText(cardCarEdit.getTypeCar());
        editTextInsurance.setText(cardCarEdit.getInsurance());
        editTextYearCar.setText(cardCarEdit.getYearCar());
        editTextPriceForDay.setText(cardCarEdit.getPriceDay());
        editTextPhone.setText(cardCarEdit.getPhone());
        Button buttonDate = findViewById(R.id.buttonDate);
        buttonDate.setText("מ-" + cardCarEdit.getDateStart() + " עד-" + cardCarEdit.getDateEnd());
        cardCar.setDateEnd(cardCarEdit.getDateEnd());
        cardCar.setDateStart(cardCarEdit.getDateStart());
        editTextRemarks.setText(cardCarEdit.getRemarks());
        if (cardCarEdit.getPermissionToPublish() == 2 && cardCarEdit.getRejection().length() > 0) {
            editTextRemarks.setText("סורב מפני: " + cardCarEdit.getRejection());
            editTextRemarks.setTextColor(-65536);
        }
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
        editTextRemarks = findViewById(R.id.remarksRegisterCar3);
    }

    private void setWarnGone() {
        textViewWarnAll.setVisibility(View.GONE);
        textViewWarnArea.setVisibility(View.GONE);
        textViewWarnImage.setVisibility(View.GONE);



    }

    private void setTextViewWarn() {

        textViewWarnAll = findViewById(R.id.warnAll);
        textViewWarnArea = findViewById(R.id.warnArea);
        textViewWarnImage = findViewById(R.id.warnImage);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setCardDetails() {
        flag = false;
        boolean flag2 = false;

        if (!flagEdit) {
            flag2 = ErrWarn.errImage(arrayListImageViewUri.size(), textViewWarnImage);//TODO:b
            if (!flag && flag2) flag = true;
        }

        flag2 = ErrWarn.errArea(area, textViewWarnArea);
        if (!flag && flag2) flag = true;
        cardCar.setArea(area);

        String edit = editTextName.getText().toString();
        flag2 = ErrWarn.errName(edit, editTextName);
        if (!flag && flag2) flag = true;
        cardCar.setName(edit);

        edit = editTextCity.getText().toString();
        flag2 = ErrWarn.errCity(edit, editTextCity);
        if (!flag && flag2) flag = true;
        cardCar.setCity(edit);

        edit = cardCar.getDateStart();
        if (edit.length() == 0||MainActivityManagementCardsApprov.passDate(edit)) {//TODO: HERE
            flag2 = true;
            Button button = findViewById(R.id.buttonDate);
            button.setTextColor(-65536);
            button.setText("אנא הכנס תאריכים תקינים");
        }
        if (!flag && flag2) flag = true;


        edit = editTextInsurance.getText().toString();
        flag2 = ErrWarn.errInsurance(edit, editTextInsurance);
        if (!flag && flag2) flag = true;
        cardCar.setInsurance(edit);

        edit = editTextRemarks.getText().toString();
        flag2 = ErrWarn.errRemarker(edit, editTextRemarks);
        if (!flag && flag2) flag = true;
        cardCar.setRemarks(edit);

        edit = editTextPhone.getText().toString();
        flag2 = ErrWarn.errPhone(edit, editTextPhone);
        if (!flag && flag2) flag = true;
        cardCar.setPhone(edit);

        edit = editTextPriceForDay.getText().toString();
        flag2 = ErrWarn.errPrice(edit, editTextPriceForDay);
        if (!flag && flag2) flag = true;
        cardCar.setPriceDay(edit);


        edit = editTextTypeCar.getText().toString();
        flag2 = ErrWarn.errTypeCar(edit, editTextTypeCar);
        if (!flag && flag2) flag = true;
        cardCar.setTypeCar(edit);

        edit = editTextYearCar.getText().toString();
        flag2 = ErrWarn.errYearCar(edit, editTextYearCar);
        if (!flag && flag2) flag = true;
        cardCar.setYearCar(edit);

        if (flag) {
            textViewWarnAll.setText("אחד הנתונים לא תקין");
            textViewWarnAll.setVisibility(View.VISIBLE);
        }
    }

    public void openImage(ImageView imageView, Uri uri) {
        imageViewTemp = imageView;
        uriSave = uri;


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
                    imageViewTemp.setImageURI(selectedImage);
                    setUriImage(selectedImage);
                    arrayListImageViewUri.add(selectedImage);

                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageViewTemp.setImageURI(selectedImage);
                    setUriImage(selectedImage);
                    arrayListImageViewUri.add(selectedImage);


                }
                break;
        }
    }

    private void setUriImage(Uri selectedImage) {
        if (numImage == 1)
            uri1 = selectedImage;
        if (numImage == 2)
            uri2 = selectedImage;
        if (numImage == 3)
            uri3 = selectedImage;
        if (numImage == 4)
            uri4 = selectedImage;
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
        //  progressDialog = new ProgressDialog(this);

        progressDialog.show();

        cardRef = FirebaseDatabase.getInstance().getReference();
        cardRef2 = FirebaseDatabase.getInstance().getReference("RegisterInformation");

        cardRef2.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    registerInformation = child.getValue(RegisterInformation.class);
                    key2 = child.getKey();

                }

                arrayListImageViewUri = new ArrayList<>();
                if (uri4 != null) arrayListImageViewUri.add(uri4);
                if (uri1 != null) arrayListImageViewUri.add(uri1);
                if (uri2 != null) arrayListImageViewUri.add(uri2);
                if (uri3 != null) arrayListImageViewUri.add(uri3);


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

        registerInformation.setId(registerInformation.getId() + 1);

        flagI = 0;
        //  final int length = arrayListImageViewUri.size();
        if (flagEdit && flagImageDeleteConfirmation != 1) {
            DatabaseReference cardRef5 = FirebaseDatabase.getInstance().getReference("CardsWaitApprov").push();
            cardCar.setKey(cardRef5.getKey());
            cardRef5.setValue(cardCar);
            registerInformation.addCardcar(cardCar);
            DatabaseReference cardRef7 = FirebaseDatabase.getInstance().getReference();
            cardRef7.child("RegisterInformation").child(key2).setValue(registerInformation);

            Toast.makeText(MainActivityRegisterCar.this, "כרטיס נשמר בהצלחה", Toast.LENGTH_SHORT).show();

            deleteCard();
        } else {
            //  for (i = 0; i < length && i < 4; i++) {


            imageAndSave(1);

        }
    }

    private void imageAndSave(final int num) {

        String s = registerInformation.getEmail() + "/" + registerInformation.getId() + "" + num + ".jpg";
        StorageReference riversRef = mStorageRef.child(s);
        riversRef.putFile(arrayListImageViewUri.get(num - 1))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                url = uri.toString();
                                cardCar.addImageViewArrayListName(url);
                                if (arrayListImageViewUri.size() == num) {
                                    fireBase();
                                    return;
                                }
                                imageAndSave(num + 1);

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

    }

    private void fireBase() {
        DatabaseReference cardRef4 = FirebaseDatabase.getInstance().getReference("CardsWaitApprov").push();
        keyCard = cardRef4.getKey();
        cardCar.setKey(keyCard);
        registerInformation.addCardcar(cardCar);
        cardRef4.setValue(cardCar);

        DatabaseReference cardRef6 = FirebaseDatabase.getInstance().getReference();
        cardRef6.child("RegisterInformation").child(key2).setValue(registerInformation);
        Toast.makeText(MainActivityRegisterCar.this, "כרטיס נשמר בהצלחה", Toast.LENGTH_SHORT).show();

        if (flagEdit) {
            deleteCard();
            return;
        }

        progressDialog.dismiss();
        Intent intent = new Intent(MainActivityRegisterCar.this, MainActivityPageUser.class);
        startActivity(intent);


    }

    // finish();
    //}


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
        cardCarEdit.setRejection(bundle.getString("rejection"));

        for (int i = 1; i <= cardCarEdit.getNumImage(); i++)
            cardCarEdit.addImageViewArrayListName(bundle.getString("image" + i));

    }

    public void deleteCard() {

//        mDatabase = FirebaseDatabase.getInstance().getReference("RegisterInformation");
//
//        cardRef2 = FirebaseDatabase.getInstance().getReference("RegisterInformation");
//
//        cardRef2.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    registerInformation = child.getValue(RegisterInformation.class);
//                    key = child.getKey();
//
//                }
//
//                for (int i = 0; i < registerInformation.getCardsUser().size(); i++) {
//                    if (cardCarEdit.getId() == registerInformation.getCardsUser().get(i).getId()) {
//                        registerInformation.removeCardCar(i);
//                        // Toast.makeText(MainActivityRegisterCar.this, key, Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                }
//
//                mDatabase.child(key).setValue(registerInformation);
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
        progressDialog.dismiss();
        Intent intent = new Intent(MainActivityRegisterCar.this, MainActivityPageUser.class);
        startActivity(intent);

//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent2;
        switch (item.getItemId()) {
            case R.id.mainMenu:
                intent2 = new Intent(MainActivityRegisterCar.this, MainActivity.class);
                startActivity(intent2);
                return true;
            case R.id.mainIconMenu:
                intent2 = new Intent(MainActivityRegisterCar.this, MainActivity.class);
                startActivity(intent2);
                return true;
            case R.id.myPageMenu:
                intent2 = new Intent(MainActivityRegisterCar.this, MainActivityPageUser.class);
                startActivity(intent2);
                return true;
            case R.id.cardCarMenu:
                intent2 = new Intent(MainActivityRegisterCar.this, MainActivityRegisterCar.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dialogeImageDeleteConfirmation() {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog_image_delete);
        d.setTitle("Image delete confirmation");

        d.setCancelable(true);
        Button buttonYes = (Button) d.findViewById(R.id.buttonYes);
        Button buttonNo = (Button) d.findViewById(R.id.buttonNo);
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagImageDeleteConfirmation = 1;
                imageView1.setImageResource(R.drawable.ic_car);
                imageView2.setImageResource(R.drawable.ic_car);
                imageView3.setImageResource(R.drawable.ic_car);
                imageView4.setImageResource(R.drawable.ic_car);
                cardCar.setImageViewArrayListName(new ArrayList<String>());
                d.dismiss();
            }
        });
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagImageDeleteConfirmation = 2;
                d.dismiss();

            }
        });
        d.show();

    }
}