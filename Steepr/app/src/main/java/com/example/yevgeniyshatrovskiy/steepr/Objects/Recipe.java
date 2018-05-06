package com.example.yevgeniyshatrovskiy.steepr.Objects;

import java.util.ArrayList;

/**
 * Created by Yevgeniy on 3/28/18.
 */

public class Recipe {

    private String name;
    private String description;
    private String chineseDescription;
    private int secondsToSteep;
    private ArrayList<String> ingredients = new ArrayList();
    private String imageURL;
    private String category;
    private String backGroundColor;
    private String backGroundImage;
    private String textColor;
    private String chineseCategory;
    private String chineseName;
    private int temperature;

    public Recipe(String name, String description, int secondsToSteep, int temperature,
                  ArrayList<String> ingredients, String imageURL, String category,
                  String backGroundColor, String backGroundImage, String textColor,
                  String chineseCategory, String chineseName, String chineseDescription) {
        this.name = name;
        this.description = description;
        this.secondsToSteep = secondsToSteep;
        this.temperature = temperature;
        this.ingredients = ingredients;
        this.imageURL = imageURL;
        this.category = category;
        this.backGroundColor = backGroundColor;
        this.backGroundImage = backGroundImage;
        this.textColor = textColor;
        this.chineseCategory = chineseCategory;
        this.chineseName = chineseName;
    }

    public Recipe(String name, String description, int secondsToSteep, ArrayList<String> ingredients) {
        this.name = name;
        this.description = description;
        this.secondsToSteep = secondsToSteep;
        this.ingredients = ingredients;
    }

    public Recipe(String name, String description, int secondsToSteep, int temperature, ArrayList<String> ingredients, String imageURL, String category, String backGroundColor, String backGroundImage, String textColor) {
        this.name = name;
        this.description = description;
        this.secondsToSteep = secondsToSteep;
        this.temperature = temperature;
        this.ingredients = ingredients;
        this.imageURL = imageURL;
        this.category = category;
        this.backGroundColor = backGroundColor;
        this.backGroundImage = backGroundImage;
        this.textColor = textColor;
    }


    public String getChineseCategory() {
        return chineseCategory;
    }

    public void setChineseCategory(String chineseCategory) {
        this.chineseCategory = chineseCategory;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseDescription() {return chineseDescription;}

    public void setChineseDescription(String chineseDescription) {this.chineseDescription = chineseDescription;}

    public String getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(String backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public String getBackGroundImage() {
        return backGroundImage;
    }

    public void setBackGroundImage(String backGroundImage) {
        this.backGroundImage = backGroundImage;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
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

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
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

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", secondsToSteep=" + secondsToSteep +
                ", temperature=" + temperature +
                ", ingredients=" + ingredients +
                ", imageURL='" + imageURL + '\'' +
                ", category='" + category + '\'' +
                ", backGroundColor='" + backGroundColor + '\'' +
                ", backGroundImage='" + backGroundImage + '\'' +
                ", textColor='" + textColor + '\'' +
                '}';
    }
}
