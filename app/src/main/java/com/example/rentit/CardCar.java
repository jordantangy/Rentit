package com.example.rentit;

import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class CardCar {
//    -key: String
//    -seeCard: int
//    -email: String
//    -numImage: int
//    -id: int
//    -permissionToPublish: int
//    -name: String
//    -typeCar: String
//    -yearCar: String
//    -phone: String
//    -priceDay: String
//    -insurance: String
//    -area: String
//    -city: String
//    -dateStart: String
//    -dateEnd: String
//    -remarks: String
//    -rejection: String
//    -imageViewArrayListName: List<String>


    private String key="";
    private int seeCard=0;
    private String email="";
    private int numImage=0;
    private int id=0;
    private int permissionToPublish=0;
    private String name="";
    private String typeCar="";
    private String yearCar="";
    private String phone="";
    private String priceDay="";
    private String insurance="";
    private String area="";
    private String city="";
    private String dateStart="";
    private String dateEnd="";
    private String remarks="";
    private String rejection="";
    private List<String> imageViewArrayListName=new ArrayList<>();



    public CardCar() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
//+removeImageViewArrayListName (String)
//    +addImageViewArrayListName (String)
//        +addOneSeeCard ()
    public void removeImageViewArrayListName(String url){
        imageViewArrayListName.remove(url);
}
    public void addImageViewArrayListName(String url){
        imageViewArrayListName.add(url);
    }

    public List<String> getImageViewArrayListName() {
        return imageViewArrayListName;
    }

    public void setImageViewArrayListName(List<String> imageViewArrayListName) {
        this.imageViewArrayListName = imageViewArrayListName;
    }

    public int getPermissionToPublish() {
        return permissionToPublish;
    }

    public void setPermissionToPublish(int permissionToPublish) {
        this.permissionToPublish = permissionToPublish;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeCar() {
        return typeCar;
    }

    public void setTypeCar(String typeCar) {
        this.typeCar = typeCar;
    }

    public String getYearCar() {
        return yearCar;
    }

    public void setYearCar(String yearCar) {
        this.yearCar = yearCar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPriceDay() {
        return priceDay;
    }

    public void setPriceDay(String priceDay) {
        this.priceDay = priceDay;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumImage() {
        return numImage;
    }

    public void setNumImage(int numImage) {
        this.numImage = numImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRejection() {
        return rejection;
    }

    public void setRejection(String rejection) {
        this.rejection = rejection;
    }

    public int getSeeCard() {
        return seeCard;
    }
    public void addOneSeeCard() {
        this.seeCard++;
    }
    public void setSeeCard(int seeCard) {
        this.seeCard = seeCard;
    }
}
