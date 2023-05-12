package com.techcare.findmydr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.techcare.findmydr.api.ApiClient;
import com.techcare.findmydr.api.ApiInterface;
import com.techcare.findmydr.api.response.ResponseAppointmentDetails;
import com.techcare.findmydr.api.response.ResponseAppointments;
import com.techcare.findmydr.api.tablesclass.TableAppointments;
import com.techcare.findmydr.databinding.ActivityAppointmentBinding;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AppointmentActivity extends AppCompatActivity {

    ActivityAppointmentBinding activityAppointmentBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAppointmentBinding= ActivityAppointmentBinding.inflate(getLayoutInflater());
        setContentView(activityAppointmentBinding.getRoot());
        getSupportActionBar().setTitle(" ");

        activityAppointmentBinding.btnBkApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=activityAppointmentBinding.radioGroup.getCheckedRadioButtonId();
                RadioButton r= findViewById(id);

//              TODO check for not empty
                String name=activityAppointmentBinding.textLayoutNameEdit.getText().toString();
                String phone=activityAppointmentBinding.textLayoutPhoneEdit.getText().toString();
                String radio=r.getText().toString();
                String bday=activityAppointmentBinding.editTextBirthDate.getText().toString();
                String schdate=activityAppointmentBinding.editTextSchDate.getText().toString();
                String schtime=activityAppointmentBinding.editTextSchTime.getText().toString();

//                TODO Design Login
//                TODO set respective id's
                String api= FirebaseAuth.getInstance().getCurrentUser().getUid();
                setAppointment(api, getIntent().getExtras().getString("Doctor Id"), name, bday,radio,phone,schdate+" "+schtime);
//                setAppointment("g", "BKjM1xWLynT4IoY2aw06JDwcAGq1", name, bday,radio,phone,schdate+" "+schtime);

            }
        });
    }

    private void setAppointment(String pid, String drid, String aptname, String aptbdate, String aptgender, String aptphone, String aptsch) {
        Retrofit retrofit= ApiClient.getClient();
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);

        apiInterface.setAppointment(pid, drid, aptsch).enqueue(new Callback<ResponseAppointments>() {
            @Override
            public void onResponse(Call<ResponseAppointments> call, Response<ResponseAppointments> response) {
                if (response!=null) {
                    if (response.body().getStatusCode().equals("200") && response.body().getStatusMessage().equals("Data Inserted Successfully")){
                        TableAppointments appointments=response.body().getDataList().get(0);
                        String aptid=appointments.getAppointmentId();

                        apiInterface.setAppointmentDetails(pid, aptid, aptname, aptbdate, aptgender, aptphone, aptsch).enqueue(new Callback<ResponseAppointmentDetails>() {
                            @Override
                            public void onResponse(Call<ResponseAppointmentDetails> call, Response<ResponseAppointmentDetails> response) {
                                Toast.makeText(AppointmentActivity.this, "Appointed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseAppointmentDetails> call, Throwable t) {
                                Toast.makeText(AppointmentActivity.this, "Fail inner", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(AppointmentActivity.this, response.body().getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAppointments> call, Throwable t) {
                Toast.makeText(AppointmentActivity.this, "Fail outer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleDateAndTime(View view) {
        final Calendar calendar= Calendar.getInstance();
        if (view.getId() == activityAppointmentBinding.btnSetBday.getId()){

            int Y=calendar.get(Calendar.YEAR);
            int M=calendar.get(Calendar.MONTH);
            int D=calendar.get(Calendar.DATE);

            DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                    Calendar c=Calendar.getInstance();
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DATE, date);

                    CharSequence dateSequence= DateFormat.format("yyyy-MM-dd",c);
                    activityAppointmentBinding.editTextBirthDate.setText(dateSequence);
                }
            }, Y, M, D);
            datePickerDialog.show();
        } else if (view.getId() == activityAppointmentBinding.btnSetSchdate.getId()) {

            int Y=calendar.get(Calendar.YEAR);
            int M=calendar.get(Calendar.MONTH);
            int D=calendar.get(Calendar.DATE);

            DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                    Calendar c=Calendar.getInstance();
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DATE, date);

                    CharSequence dateSequence= DateFormat.format("yyyy-MM-dd",c);
                    activityAppointmentBinding.editTextSchDate.setText(dateSequence);
                }
            }, Y, M, D);
            datePickerDialog.show();
        } else if (view.getId() == activityAppointmentBinding.btnSetSchtime.getId()) {

            int H=calendar.get(Calendar.HOUR);
            int M=calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog= new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int min) {
                    Calendar c=Calendar.getInstance();
                    c.set(Calendar.HOUR, hour);
                    c.set(Calendar.MINUTE, min);
                    c.set(Calendar.SECOND, 0);

                    CharSequence timeSequence= DateFormat.format("HH:mm:ss",c);
                    activityAppointmentBinding.editTextSchTime.setText(timeSequence);
                }
            }, H, M, true);
            timePickerDialog.show();
        } else  {

        }
    }

}