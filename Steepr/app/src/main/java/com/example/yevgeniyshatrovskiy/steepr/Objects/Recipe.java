package com.example.yevgeniyshatrovskiy.steepr.Objects;

import java.util.ArrayList;

/**
 * Created by Yevgeniy on 3/28/18.
 */

public class Recipe {

    private String name;
    private String description;
    private int secondsToSteep;
    private ArrayList<String> ingredients = new ArrayList();
    private String imageURL;
    private String category;

    public Recipe(String name, String description, int secondsToSteep, ArrayList<String> ingredients) {
        this.name = name;
        this.description = description;
        this.secondsToSteep = secondsToSteep;
        this.ingredients = ingredients;
    }

    public Recipe(String name) {
        this.name = name;
    }

    public Recipe() {
        //Empty constructor needed for Firebase
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSecondsToSteep() {
        return secondsToSteep;
    }

    public void setSecondsToSteep(int secondsToSteep) {
        this.secondsToSteep = secondsToSteep;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
