package io.kong.mypetdiary.item;

public class HomeListViewItem {

    private String imgUrl;
    private String stTitle;
    private String stContent;
    private String stWeek;
    private String stDate;
    private int stDay;
    private int width;
    private int height;

    public HomeListViewItem(String imgUrl, String stTitle, String stContent, String stWeek, String stDate, int stDay, int width, int height) {
        this.imgUrl = imgUrl;
        this.stTitle = stTitle;
        this.stContent = stContent;
        this.stWeek = stWeek;
        this.stDate = stDate;
        this.stDay = stDay;
        this.width = width;
        this.height = height;
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
    public String getDate() {
        return this.stDate;
    }
    public int getDay() {
        return this.stDay ;
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
}