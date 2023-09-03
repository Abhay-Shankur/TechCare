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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techcare.findmydr.modules.AppointmentDetails;
import com.techcare.findmydr.databinding.ActivityAppointmentBinding;

import java.util.Calendar;

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
                setAppointment(api, getIntent().getExtras().getString("Firestore Id"), name, bday,radio,phone,schdate+" "+schtime);
            }
        });
    }

//    Creating an Appointment Object and setting it into Firestore and FirestoreId assigned to Doctor into RTDb.
    private void setAppointment(String pid, String drFsId, String aptname, String aptbdate, String aptgender, String aptphone, String aptsch) {

        AppointmentDetails appointmentDetails = new AppointmentDetails();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        firestore.collection("Doctors")
                .document(drFsId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String doctorUid=task.getResult().get("doctorUid").toString();
                            firestore.collection("Appointments")
                                    .add(appointmentDetails)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            firestore.collection("Appointments")
                                                    .document(documentReference.getId())
                                                    .update("appointmentId",documentReference.getId());

                                            reference.child("Appointments")
                                                    .child(doctorUid)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot snapshot) {
                                                            reference.child("Appointments")
                                                                    .child(doctorUid)
                                                                    .child(String.valueOf(snapshot.getChildrenCount()))
                                                                    .setValue(documentReference.getId());
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError error) {
                                                            Log.e("TAG", "onCancelled: ", error.toException());
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Log.e("TAG", "onFailure: ", e);
                                        }
                                    });
                        }
                    }
                });
    }

//    Function for setting the BirthDate and schedule.
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
        }
    }
}