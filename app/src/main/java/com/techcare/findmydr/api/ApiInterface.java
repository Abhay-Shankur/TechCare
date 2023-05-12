package com.techcare.findmydr.api;


import com.techcare.findmydr.api.response.ResponseAppointmentDetails;
import com.techcare.findmydr.api.response.ResponseAppointments;
import com.techcare.findmydr.api.response.ResponseDoctors;
import com.techcare.findmydr.api.response.ResponsePatient;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
//
////    Auth
//    @FormUrlEncoded
//    @POST("drauth/postData.php")
//    Call<ResponseDrAuth> postDrAuth(@Field("uid") String drAuthUid, @Field("name") String drAuthName, @Field("email") String drAuthEmail,
//                                    @Field("pass") String drAuthPass, @Field("phone") String drAuthPhone);
//
////    Appointment List
//    @FormUrlEncoded
//    @POST("appointments/getData.php")
//    Call<ResponseAppointmentLists> getAppointmentsList(@Field("api-key-dr") String apiKeyDr);
//
////    Appointment
//    @FormUrlEncoded
//    @POST("appointmentdetail/getData.php")
//    Call<ResponseAppointment> getAppointmentDetail(@Field("api-key-dr") String  apiKeyDr, @Field("apt-id") String aptId);
//    Patients
    @FormUrlEncoded
    @POST("patients/getData.php")
    Call<ResponsePatient> getPatients(@Field("api-key") String apiKey);

    @FormUrlEncoded
    @POST("patients/getData.php")
    Call<ResponsePatient> getPatient(@Field("api-key") String apiKey, @Field("pt-id") String ptuid);

    @FormUrlEncoded
    @POST("patients/postData.php")
    Call<ResponsePatient> setPatient(@Field("pt-email") String ptemail, @Field("pt-uid") String ptuid, @Field("pt-name") String ptname, @Field("pt-pass") String ptpass, @Field("pt-bday") String ptbday, @Field("pt-gen") String ptgen, @Field("pt-phn") String ptphn, @Field("pt-flw") String ptflw);

//    Doctors
//    @FormUrlEncoded
    @POST("doctors/getData.php")
    Call<ResponseDoctors> getDoctors();

    @FormUrlEncoded
    @POST("doctors/getData.php")
    Call<ResponseDoctors> getDoctors(@Field("api-key") String apiKey);

//    Doctor
    @FormUrlEncoded
    @POST("doctors/getData.php")
    Call<ResponseDoctors> getDoctor(@Field("api-key") String apiKey, @Field("dr-uid") String druid);

//    Appointments
    @FormUrlEncoded
    @POST("appointments/postData.php")
    Call<ResponseAppointments> setAppointment(@Field("api-key-patient") String apiKey, @Field("dr-id") String drUid, @Field("sch") String schedule);

//    Appointment Details
    @FormUrlEncoded
    @POST("appointmentdetail/postData.php")
    Call<ResponseAppointmentDetails> setAppointmentDetails(@Field("api-key-patient") String apiKey, @Field("apt-id") String aptId, @Field("apt-name") String aptName, @Field("apt-bdate") String aptBdate, @Field("apt-gen") String aptGender, @Field("apt-phn") String aptPhone, @Field("apt-sch") String aptSchedule);

}
