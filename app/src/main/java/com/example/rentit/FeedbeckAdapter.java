package com.example.rentit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class FeedbeckAdapter extends ArrayAdapter<Feedback> {

    private Context context;
    private List<Feedback> objects;
    private Feedback feedback;
    private View view;
    private Boolean flag = false;


    public FeedbeckAdapter(Context context, int resource, int textViewResourceId, List<Feedback> objects, boolean flag) {
        super(context, resource, textViewResourceId, objects);
        this.flag = flag;
        this.context = context;
        this.objects = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        view = layoutInflater.inflate(R.layout.feedbeck_list, parent, false);
        feedback = objects.get(position);


        TextView grade = (TextView) view.findViewById(R.id.textViewGradeList);
        TextView feedbekWords = (TextView) view.findViewById(R.id.textViewFeedbackList);
        TextView emailReport = (TextView) view.findViewById(R.id.textViewEmailReportLIst);
        TextView emailCar = (TextView) view.findViewById(R.id.textViewEmailCarLIst);
        TextView carId = (TextView) view.findViewById(R.id.textViewIdCarList);


        grade.setText("ציון: " + feedback.getGrade());
        feedbekWords.setText("משוב: " + feedback.getFeedback());
        if(flag){
            emailReport.setText("כותב: "+feedback.getEmailReporting());
            emailCar.setText("על: "+feedback.getEmailCar());
            carId.setText("id: "+feedback.getId());
            emailCar.setVisibility(View.VISIBLE);
            emailReport.setVisibility(View.VISIBLE);
            carId.setVisibility(View.VISIBLE);
        }


        return view;
    }


}