package com.example.rentit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaSync;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.concurrent.CountDownLatch;


public class CardCarAdapter extends ArrayAdapter<CardCar> {

    private ImageView ivProduct;
    private Context context;
    private List<CardCar> objects;
    private CardCar temp;
    private boolean go = false;
    private View view;
    private boolean flagSee=false;


    public CardCarAdapter(Context context, int resource, int textViewResourceId, List<CardCar> objects) {
        super(context, resource, textViewResourceId, objects);
        this.flagSee = false;
        this.context = context;
        this.objects = objects;

    }

    public CardCarAdapter(Context context, int resource, int textViewResourceId, List<CardCar> objects, boolean flagSee) {
        super(context, resource, textViewResourceId, objects);
        this.flagSee = flagSee;
        this.context = context;
        this.objects = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        view = layoutInflater.inflate(R.layout.activity_card_list, parent, false);

        ivProduct = (ImageView) view.findViewById(R.id.imageViewListCard);
        temp = objects.get(position);
        String url = "";
        if (temp.getImageViewArrayListName().size() > 0) {
            url = temp.getImageViewArrayListName().get(0);
            Glide.with(view)
                    .load(url)
                    .into(ivProduct);

        }
        TextView phone = (TextView) view.findViewById(R.id.phoneListCard);

        TextView permission = (TextView) view.findViewById(R.id.textViewPermission);
        TextView price = (TextView) view.findViewById(R.id.priceDayListCard);
        TextView area = (TextView) view.findViewById(R.id.areaListCard);
        TextView date = (TextView) view.findViewById(R.id.dateListCard);
        TextView insurance = (TextView) view.findViewById(R.id.insuranceListCard);
        TextView typeCar = (TextView) view.findViewById(R.id.typeCarListCard);
        TextView seeMe = (TextView) view.findViewById(R.id.textViewSeeMe);
        ImageView imageSee = (ImageView) view.findViewById(R.id.imageViewSee);
if(flagSee){
    seeMe.setText(""+temp.getSeeCard());
    seeMe.setVisibility(View.VISIBLE);
    imageSee.setVisibility(View.VISIBLE);
}
        if (temp.getPermissionToPublish() == 1) permission.setVisibility(View.GONE);
        else if (temp.getPermissionToPublish() == 0) {
            permission.setText("הכרטיס ממתין לאישור");
            permission.setVisibility(View.VISIBLE);
        } else if (temp.getPermissionToPublish() == 2) {
            permission.setText("הכרטיס סורב");
            permission.setVisibility(View.VISIBLE);
        }
        phone.setText("פלאפון: " + temp.getPhone());
        price.setText("מחיר ליום: " + temp.getPriceDay());
        area.setText("אזור " + temp.getArea() + ": " + temp.getCity());
        date.setText("מה-" + temp.getDateStart() + " עד ה-" + temp.getDateEnd());
        insurance.setText("ביטוח: " + temp.getInsurance());

        typeCar.setText("סוג רכב: " + temp.getTypeCar());

        return view;
    }


}