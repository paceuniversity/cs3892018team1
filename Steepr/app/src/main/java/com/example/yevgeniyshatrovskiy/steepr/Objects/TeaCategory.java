package com.example.yevgeniyshatrovskiy.steepr.Objects;

import java.util.ArrayList;

public class TeaCategory {

    String categoryName;
    ArrayList<Recipe> recipes = new ArrayList<>();


    public TeaCategory(){

    }

    public TeaCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void addRecipes(Recipe rec){
        this.recipes.add(rec);
    }
}
