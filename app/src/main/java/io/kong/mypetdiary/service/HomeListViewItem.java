package io.kong.mypetdiary.service;

public class HomeListViewItem {

    private String imgUrl;
    private String stTitle;
    private String stContent;
    private String stWeek;
    private String stDate;
    private int stDay;
    private int width;
    private int height;

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setTitle(String title) {
        this.stTitle = title;
    }

    public void setContent(String content) {
        this.stContent = content;
    }

    public void setWeek(String week) {
        this.stWeek = week;
    }

    public void setDate(String date) {
        this.stDate = date;
    }

    public void setDay(int day) {
        this.stDay = day;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public String getTitle() {
        return this.stTitle;
    }

    public String getContent() {
        return this.stContent;
    }

    public String getWeek() {
        return this.stWeek;
    }

    public String getDate() {
        return this.stDate;
    }

    public int getDay() {
        return this.stDay;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}