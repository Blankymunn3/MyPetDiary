package io.kong.mypetdiary.service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitService {
    String URL = "http://13.209.93.19:3000";

    @GET("/user_table")
    Call<ResponseBody> doubleCheck(@Query("user_id") String user_id);

    @GET("/login")
    Call<ResponseBody> login(@Query("user_id") String user_id, @Query("user_pw") String user_pw);

    @GET("/pet_login")
    Call<ResponseBody> pet_login(@Query("user_id") String user_id);

    @POST("/join")
    Call<ResponseBody> join(@Query("user_id") String user_id, @Query("user_pw") String user_pw, @Query("user_name") String user_name,
                            @Query("user_birth") String user_birth, @Query("user_profile") String user_profile,
                            @Query("user_area") String user_area, @Query("user_kakao") int user_kakao);

    @POST("/pet_join")
    Call<ResponseBody> pet_join(@Query("user_id") String user_id, @Query("pet_name") String pet_name, @Query("pet_birth") String pet_birth,
                                @Query("pet_come") String pet_come, @Query("pet_kind") String pet_kind);

    @Multipart
    @POST("uploadProfile")
    Call<ResponseBody> uploadPhoto(@Part MultipartBody.Part image, @Part("upload") RequestBody name, @Query("user_id") String user_id);

    @Multipart
    @POST("uploadDiary")
    Call<ResponseBody> uploadDiary(@Part MultipartBody.Part image, @Part("upload") RequestBody name, @Query("user_id") String user_id,
                                   @Query("diary_today_comment") String diary_today_comment, @Query("diary_content") String diary_content,
                                   @Query("diary_date") String diary_date, @Query("diary_weather") String diary_weather, @Query("diary_week") String diary_week);
    @GET("/diary")
    Call<ResponseBody> selectDiary(@Query("user_id") String user_id, @Query("diary_date") String diary_date);
}
