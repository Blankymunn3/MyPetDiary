package io.kong.mypetdiary.item;

public class KakaoUserItem {

    static String nickName;
    static String email;
    static String profileImagePath;
    static String thumnailPath;
    static String UUID;
    static long userId;

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public void setThumnailPath(String thumnailPath) {
        this.thumnailPath = thumnailPath;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public static String getNickName() {
        return nickName;
    }

    public static String getEmail() {
        return email;
    }

    public static String getProfileImagePath() {
        return profileImagePath;
    }

    public static String getThumnailPath() {
        return thumnailPath;
    }

    public static String getUUID() {
        return UUID;
    }

    public static long getUserId() {
        return userId;
    }

}
