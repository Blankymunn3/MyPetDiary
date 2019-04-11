package io.kong.mypetdiary.item;

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
    private int stPetKind;

    public MyPageListViewItem(int type, String imgPetUri, String stPetName, String stPetBirth, String stPetCome, int stPetKind) {
        this.type = type;
        this.imgPetUri = imgPetUri;
        this.stPetName = stPetName;
        this.stPetBirth = stPetBirth;
        this.stPetCome = stPetCome;
        this.stPetKind = stPetKind;
    }

    public MyPageListViewItem(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
    public String getImgPetUri() {
        return this.imgPetUri;
    }
    public String getStPetName() {
        return this.stPetName;
    }
    public String getStPetBirth() {
        return this.stPetBirth;
    }
    public String getStPetCome() {
        return this.stPetCome;
    }
    public int getStPetKind() {
        return this.stPetKind;
    }


}

