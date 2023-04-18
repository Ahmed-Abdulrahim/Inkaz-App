package com.elkfrawy.engaz.data.remote

import android.accessibilityservice.GestureDescription.StrokeDescription
import com.elkfrawy.engaz.data.CarId
import com.elkfrawy.engaz.domain.model.*
import retrofit2.Response
import retrofit2.http.*

interface Api {

    // user
    @POST("user/login")
    suspend fun login(
        @Query("number") mobile: Long,
        @Query("password") password: String
    ): Response<CarId>

    @POST("user/register")
    suspend fun register(
        @Query("name") name: String,
        @Query("number") number: Long,
        @Query("NationalId") nationalID: String,
        @Query("password") password: String,
        @Query("address") address: String,
        @Query("car_name") car_name: String,
        @Query("car_model") car_model: Int,
        @Query("car_number") car_number: Int,
        @Query("car_color") car_color: String,
        @Query("car_license") car_license: String,
    ): Response<Long>

    @GET("user/show/{id}")
    suspend fun getUser(@Path("id") id: Long): Response<User>

    @POST("userUpdate/email/{id}")
    suspend fun updateEmail(@Path("id") id: Long, @Query("email") email: String): Response<Unit>

    @POST("userUpdate/password/{id}")
    suspend fun updatePassword(
        @Path("id") id: Long,
        @Query("password") password: String
    ): Response<Unit>

    @POST("userUpdate/number/{id}")
    suspend fun updateMobile(@Path("id") id: Long, @Query("number") number: Long): Response<Unit>

    @POST("userUpdate/Location/{id}")
    suspend fun updateLatLng(
        @Path("id") id: Long,
        @Query("latitude") lat: String,
        @Query("longitude") lng: String,
    ): Response<Unit>

    @PUT("user/userInfo/{id}")
    fun updateUserInfo(
        @Path("id") id: Long, @Query("name") name: String,
        @Query("NationalId") nationalID: String,
        @Query("address") address: String,
    ):Response<Unit>

    @POST("userUpdate/rating/{id}")
    fun updateRating(@Path("id") id: Long, @Query("rating") rate: Int): Response<Unit>

    // Car
/*    @POST("car/insert")
    suspend fun addCar(
        @Query("car_name") name: String,
        @Query("car_model") car_model: Int,
        @Query("car_number") car_number: Int,
        @Query("car_color") car_color: String,
        @Query("car_license") car_license: String,
        @Query("user_id") user_id: Long
    )*/

    @GET("car/show/{user_id}")
    suspend fun getCar(@Path("user_id") user_id: Long): Response<List<Car>>

    @GET("history/show/{user_id}")
    suspend fun getHistory(@Path("user_id") user_id: Long): Response<List<History>>

    @PUT("car/carInfo/{user_id}")
    suspend fun updateCarInfo(
        @Path("user_id") user_id: Long,
        @Query("car_name") car_name: String,
        @Query("car_model") car_model: Int,
        @Query("car_number") car_number: Int,
        @Query("car_color") car_color: String,
        @Query("car_license") car_license: String,
    ):Response<Unit>


    // rate
    @POST("rating/insert/{id}")
    suspend fun insertRating(@Path("id")id: Long,
                             @Query("user_id2") user_id: Long,
                             @Query("rating") rating: Int,
                             @Query("message") message: String):Response<Unit>

    @GET("rating/get/{id}")
    suspend fun getRating(@Path("id") id: Long):Response<List<Rate>>


    @Headers("Content-Type: application/json",
        "Authorization:key=AAAArD3AZ1Q:APA91bGVEKZCfTvHG-AE__l2CHaQ-_dl5I7KZAjHD_6MgrpCPfbzCvKIiEMUODdFDqq6-S5OrjsR7L2CPSLderiI5f2enrdmPujHMJ5gReMGLZZwSB69Tjq6qo1oPpTCdWjlz9JrKPZb")
    @POST("fcm/send")
    suspend fun sendNotification(@Body notificationResponse: NotificationResponse):Response<Unit>
















}