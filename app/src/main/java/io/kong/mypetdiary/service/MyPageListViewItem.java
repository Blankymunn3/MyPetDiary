package io.kong.mypetdiary.service;

public class MyPageListViewItem {
    private int type;

    private String stBrothers;
    private String imgPetUri;
    private String stPetName;
    private String stPetBirth;


    public int getType() {
        return type;
    }

    public String getStBrothers() {
        return stBrothers;
    }

    public String getImgPetUri() {
        return imgPetUri;
    }

    public String getStPetName() {
        return stPetName;
    }

    public String getStPetBirth() {
        return stPetBirth;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStBrothers(String stBrothers) {
        this.stBrothers = stBrothers;
    }


    public void setImgPetUri(String imgPetUri) {
        this.imgPetUri = imgPetUri;
    }

    public void setStPetName(String stPetName) {
        this.stPetName = stPetName;
    }

    public void setStPetBirth(String stPetBirth) {
        this.stPetBirth = stPetBirth;
    }


}

