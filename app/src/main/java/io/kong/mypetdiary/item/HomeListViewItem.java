package io.kong.mypetdiary.item;

import android.graphics.drawable.Drawable;

public class HomeListViewItem {
    private Drawable iconDrawable;
    private String stTitle;
    private String stContent;
    private String stWeek;
    private String stDay;

    public void setIcon(Drawable icon) {
        iconDrawable = icon;
    }
    public void setTitle(String title) {
        stTitle = title ;
    }
    public void setContent(String content) {
        stContent = content;
    }
    public void setWeek(String week) {
        stWeek = week;
    }
    public void setDay(String day) {
        stDay = day;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
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
    public String getDay() {
        return this.stDay ;
    }
}