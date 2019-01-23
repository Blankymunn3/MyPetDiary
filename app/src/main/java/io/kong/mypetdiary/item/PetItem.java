package io.kong.mypetdiary.item;

public class PetItem {

    static String stPetName;
    static String stPetBirth;
    static String stPetKind;
    static String stPetCome;

    public static void setStPetName(String stPetName) {
        PetItem.stPetName = stPetName;
    }

    public static void setStPetBirth(String stPetBirth) {
        PetItem.stPetBirth = stPetBirth;
    }

    public static void setStPetCome(String stPetCome) {
        PetItem.stPetCome = stPetCome;
    }

    public static void setStPetKind(String stPetKind) {
        PetItem.stPetKind = stPetKind;
    }


    public static String getStPetName() {
        return stPetName;
    }

    public static String getStPetBirth() {
        return stPetBirth;
    }

    public static String getStPetCome() {
        return stPetCome;
    }

    public static String getStPetKind() {
        return stPetKind;
    }

}
