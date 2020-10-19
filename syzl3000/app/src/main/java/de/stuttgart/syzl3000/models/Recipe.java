package de.stuttgart.syzl3000.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Recipe implements Parcelable {

    private String title;
    private String publisher_url;
    private String recipe_id;
    private String source_url;
    private String publisher;
    private String _id;
    private float social_rank;
    private String image_url;
    private String[] ingredients;

    public Recipe() {
    }

    public Recipe(String title, String publisher_url, String recipe_id, String source_url, String publisher, String _id, float social_rank, String image_url, String[] ingredients) {
        this.title = title;
        this.publisher_url = publisher_url;
        this.recipe_id = recipe_id;
        this.source_url = source_url;
        this.publisher = publisher;
        this._id = _id;
        this.social_rank = social_rank;
        this.image_url = image_url;
        this.ingredients = ingredients;
    }


    protected Recipe(Parcel in) {
        title = in.readString();
        publisher_url = in.readString();
        recipe_id = in.readString();
        source_url = in.readString();
        publisher = in.readString();
        _id = in.readString();
        social_rank = in.readFloat();
        image_url = in.readString();
        ingredients = in.createStringArray();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher_url() {
        return publisher_url;
    }

    public void setPublisher_url(String publisher_url) {
        this.publisher_url = publisher_url;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public float getSocial_rank() {
        return social_rank;
    }

    public void setSocial_rank(float social_rank) {
        this.social_rank = social_rank;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "title='" + title + '\'' +
                ", publisher_url='" + publisher_url + '\'' +
                ", recipe_id='" + recipe_id + '\'' +
                ", source_url='" + source_url + '\'' +
                ", publisher='" + publisher + '\'' +
                ", _id='" + _id + '\'' +
                ", social_rank=" + social_rank +
                ", image_url='" + image_url + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(publisher_url);
        dest.writeString(recipe_id);
        dest.writeString(source_url);
        dest.writeString(publisher);
        dest.writeString(_id);
        dest.writeFloat(social_rank);
        dest.writeString(image_url);
        dest.writeStringArray(ingredients);
    }
}
