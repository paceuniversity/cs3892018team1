package com.example.yevgeniyshatrovskiy.steepr.Objects;

import java.util.ArrayList;

public class TeaCategory {

    String categoryName;
    ArrayList<Recipe> recipes = new ArrayList<>();


    public TeaCategory(){

    }

    public TeaCategory(Recipe rec) {
        this.categoryName = rec.getCategory();
        this.recipes.add(rec);
    }
//
//    public TeaCategory(String categoryName, Recipe rec) {
//        this.categoryName = categoryName;
//        this.recipes.add(rec);
//    }

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

    @Override
    public String toString() {
        String all = "";
        for(Recipe re : recipes){
            all += re.toString();
        }
            return all;

    }
}
