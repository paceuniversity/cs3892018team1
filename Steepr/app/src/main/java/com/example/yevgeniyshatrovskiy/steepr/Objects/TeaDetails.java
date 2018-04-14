package com.example.yevgeniyshatrovskiy.steepr.Objects;

public class TeaDetails {

    private String imageName;
    private String backgroundColor;
    private String textColor;
    private String categoryName;

    public TeaDetails(String categoryName, String imageName, String backgroundColor, String textColor) {
        this.imageName = imageName;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

}
