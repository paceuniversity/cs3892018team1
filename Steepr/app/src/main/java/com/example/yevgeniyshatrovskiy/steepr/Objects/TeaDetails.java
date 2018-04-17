package com.example.yevgeniyshatrovskiy.steepr.Objects;

public class TeaDetails {

    private String imageName;
    private String backgroundColor;
    private String textColor;
    private String categoryName;
    private String chineseName;
    private String chineseCategory;

    public TeaDetails(String categoryName, String imageName,
                      String backgroundColor, String textColor,
                      String chineseName, String chineseCategory) {
        this.imageName = imageName;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.categoryName = categoryName;
        this.chineseName = chineseName;
        this.chineseCategory = chineseCategory;
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

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseCategory() {
        return chineseCategory;
    }

    public void setChineseCategory(String chineseCategory) {
        this.chineseCategory = chineseCategory;
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
