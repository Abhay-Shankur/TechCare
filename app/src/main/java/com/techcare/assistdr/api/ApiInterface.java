package com.techcare.assistdr.api;

import com.techcare.assistdr.api.response.ResponseAppointment;
import com.techcare.assistdr.api.response.ResponseAppointmentLists;
import com.techcare.assistdr.api.response.ResponseDoctors;
import com.techcare.assistdr.api.response.ResponseDrAuth;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

//    Auth
    @FormUrlEncoded
    @POST("drauth/postData.php")
    Call<ResponseDrAuth> postDrAuth(@Field("uid") String drAuthUid, @Field("name") String drAuthName, @Field("email") String drAuthEmail,
                                    @Field("pass") String drAuthPass, @Field("phone") String drAuthPhone);

//    Doctor
    @FormUrlEncoded
    @POST("doctors/postData.php")
    Call<ResponseDoctors> postDoctor(@Field("uid") String drUid, @Field("name") String drName, @Field("profilePic") String drProfilePic, @Field("specialis") String drSpecialis, @Field("education") String drEducation, @Field("homeTown") String drHomeTown);

    @FormUrlEncoded
    @POST("doctors/putData.php")
    Call<ResponseDoctors> putDoctor(@Field("uid") String drUid, @Field("name") String drName, @Field("profilePic") String drProfilePic, @Field("specialis") String drSpecialis, @Field("education") String drEducation, @Field("homeTown") String drHomeTown);

//    Appointment List
    @FormUrlEncoded
    @POST("appointments/getData.php")
    Call<ResponseAppointmentLists> getAppointmentsList(@Field("api-key-dr") String apiKeyDr);

//    Appointment
    @FormUrlEncoded
    @POST("appointmentdetail/getData.php")
    Call<ResponseAppointment> getAppointmentDetail(@Field("api-key-dr") String  apiKeyDr, @Field("apt-id") String aptId);
}
