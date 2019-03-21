package io.kong.mypetdiary.service;

public class MyPageListViewItem {

    public static final String EXTRA_PET_URL = "EXTRA_PET_URL";
    public static final String EXTRA_PET_NAME = "EXTRA_PET_NAME";
    public static final String EXTRA_PET_BIRTH = "EXTRA_PET_BIRTH";
    public static final String EXTRA_PET_COME = "EXTRA_PET_COME";
    public static final String EXTRA_PET_KIND = "EXTRA_PET_KIND";

    private int type;

    private String imgPetUri;
    private String stPetName;
    private String stPetBirth;
    private String stPetCome;
    private String stPetKind;


    public int getType() {
        return type;
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

    public String getStPetCome() {
        return stPetCome;
    }

    public String getStPetKind() {
        return stPetKind;
    }

    public void setType(int type) {
        this.type = type;
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

    public void setStPetCome(String stPetCome) {
        this.stPetCome = stPetCome;
    }

    public void setStPetKind(String stPetKind) {
        this.stPetKind = stPetKind;
    }


}

