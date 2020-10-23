package de.stuttgart.syzl3000.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class Circle implements Parcelable {

    private String title;
    private List<String> users;  // needs to be List<User> later
    private String image_url;

    public Circle() {
    }

    public Circle(String title, List<String> users, String image_url) {
        this.title = title;
        this.users = users;
        this.image_url = image_url;
    }

    protected Circle(Parcel in) {
        title = in.readString();
        users = in.createStringArrayList();
        image_url = in.readString();
    }

    public static final Creator<Circle> CREATOR = new Creator<Circle>() {
        @Override
        public Circle createFromParcel(Parcel in) {
            return new Circle(in);
        }

        @Override
        public Circle[] newArray(int size) {
            return new Circle[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @NonNull
    @Override
    public String toString() {
        return "Recipe{" +
                "title='" + title + '\'' +
                ", users=" + users +
                ", image_url=" + image_url +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }


}
