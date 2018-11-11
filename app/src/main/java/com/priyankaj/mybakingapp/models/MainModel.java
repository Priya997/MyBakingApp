package com.priyankaj.mybakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MainModel implements Parcelable {

    @SerializedName("id")
    private String recipe_id;
    @SerializedName("name")
    private String recipe_name;
    @SerializedName("ingredients")
    private ArrayList<IngredientsModel> ingredients;
    @SerializedName("steps")
    private ArrayList<StepsModel> steps;
    @SerializedName("servings")
    private String servings;
    @SerializedName("image")
    private String image;


    protected MainModel(Parcel in) {
        recipe_id = in.readString();
        recipe_name = in.readString();
        ingredients = new ArrayList<IngredientsModel>();
        in.readList(this.ingredients, IngredientsModel.class.getClassLoader());
        steps = new ArrayList<StepsModel>();
        in.readList(this.steps, StepsModel.class.getClassLoader());
        servings = in.readString();
        image = in.readString();
    }

    public static final Parcelable.Creator<MainModel> CREATOR = new Parcelable.Creator<MainModel>() {
        @Override
        public MainModel createFromParcel(Parcel in) {
            return new MainModel(in);
        }

        @Override
        public MainModel[] newArray(int size) {
            return new MainModel[size];
        }
    };

    public String getHome_id() {
        return recipe_id;
    }

    public void setHome_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getHome_name() {
        return recipe_name;
    }

    public void setHome_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public ArrayList<IngredientsModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<IngredientsModel> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<StepsModel> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<StepsModel> steps) {
        this.steps = steps;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recipe_id);
        dest.writeString(this.recipe_name);
        dest.writeList(this.ingredients);
        dest.writeList(this.steps);
        dest.writeString(this.servings);
        dest.writeString(this.image);
    }
}
