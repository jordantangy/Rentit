package com.example.rentit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivityCardView extends AppCompatActivity {
    private TextView editTextCity, editTextTypeCar, editTextInsurance,
            editTextDateStart,editTextRemarks;
    private CardCar cardCar;
    private Button buttonReturnMain, buttonImage;
    private ImageView imageView;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_card_view);

setCard();
        position = 1;
        buttonImage = findViewById(R.id.buttonImage);
        buttonReturnMain = findViewById(R.id.buttonMainM);

        editTextCity = findViewById(R.id.textViewPriceAndAreaM);
        editTextDateStart = findViewById(R.id.textViewSrartDateM);
        editTextInsurance = findViewById(R.id.textViewInsuranceAndPhoneM);
        editTextRemarks = findViewById(R.id.textViewRemarksM);
        editTextTypeCar = findViewById(R.id.textViewTypeCarAndYearM);
        imageView = findViewById(R.id.imageMainM);

        editTextTypeCar.setText("סוג רכב: " + cardCar.getTypeCar()+"   משנת: " + cardCar.getYearCar());
        editTextRemarks.setText("הערות: " + cardCar.getRemarks());
        editTextInsurance.setText("ביטוח: " + cardCar.getInsurance()+"   "
                +cardCar.getName() + ": " + cardCar.getPhone());
        editTextDateStart.setText("מ-" + cardCar.getDateStart()+"   עד ה-" + cardCar.getDateEnd());
        editTextCity.setText("מחיר ליום: " + cardCar.getPriceDay()+"     אזור " + cardCar.getArea() + ": " + cardCar.getCity());
        Glide.with(this)
                .load(cardCar.getImageViewArrayListName().get(0))
                .into(imageView);
if(cardCar.getImageViewArrayListName().size()==1)
    buttonImage.setVisibility(View.GONE);

if(cardCar.getRemarks().isEmpty())
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
                if(position==cardCar.getImageViewArrayListName().size())
                    position=0;

            }
        });

    }

    private void setCard() {
        cardCar=new CardCar();
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