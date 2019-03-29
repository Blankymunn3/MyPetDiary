package io.kong.mypetdiary.item;

public class PetItem {

    static String stPetUrl;
    static String stPetName;
    static String stPetBirth;
    static int stPetKind;
    static String stPetCome;

    public void setStPetName(String stPetName) {
        PetItem.stPetName = stPetName;
    }

    public void setStPetBirth(String stPetBirth) {
        PetItem.stPetBirth = stPetBirth;
    }

    public void setStPetCome(String stPetCome) {
        PetItem.stPetCome = stPetCome;
    }

    public void setStPetKind(int stPetKind) {
        PetItem.stPetKind = stPetKind;
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

    public int getStPetKind() {
        return stPetKind;
    }

}
