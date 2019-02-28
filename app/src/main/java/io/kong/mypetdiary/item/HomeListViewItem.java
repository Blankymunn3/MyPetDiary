package io.kong.mypetdiary.item;


public class HomeListViewItem {
    private String imgUrl;
    private String stUserID;
    private String stTitle;
    private String stContent;
    private String stWeek;
    private int stDay;
    private String stDate;

    public void setStUserID(String userID) {
        this.stUserID = userID;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public void setTitle(String title) {
        this.stTitle = title ;
    }
    public void setContent(String content) {
        this.stContent = content;
    }
    public void setWeek(String week) {
        this.stWeek = week;
    }
    public void setDay(int day) {
        this.stDay = day;
    }
    public void setDate(String date) {
        this.stDate = date;
    }

    public String getStUserID() {
        return this.stUserID;
    }
    public String getImgUrl() {
        return this.imgUrl ;
    }
    public String getTitle() {
        return this.stTitle ;
    }
    public String getContent() {
        return this.stContent;
    }
    public String getWeek() {
        return this.stWeek;
    }
    public int getDay() {
        return this.stDay ;
    }
    public String getDate() {
        return this.stDate;
    }
}