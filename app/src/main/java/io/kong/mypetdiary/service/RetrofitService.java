package io.kong.mypetdiary.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {
    String URL = "http://13.209.207.99:3000";

    @GET("/user_table")
    Call<ResponseBody> doubleCheck(@Query("user_id") String user_id);

    @POST("/join")
    Call<ResponseBody> join(@Query("user_id") String user_id, @Query("user_pw") String user_pw, @Query("user_name") String user_name,
                            @Query("user_birth") String user_birth, @Query("user_area") String user_area, @Query("user_kakao") int user_kakao);

    @POST("/pet_join")
    Call<ResponseBody> pet_join(@Query("user_id") String user_id, @Query("pet_name") String pet_name, @Query("pet_birth") String pet_birth,
                                @Query("pet_come") String pet_come, @Query("pet_kind") String pet_kind);
}
