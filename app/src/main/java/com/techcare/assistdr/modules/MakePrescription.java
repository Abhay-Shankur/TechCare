package com.techcare.assistdr.modules;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.Toast;

import com.techcare.assistdr.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class MakePrescription {
    final String TAG="Make Prescription";
    Context context;
    final int outerWidth=595,outerHeight=842,margin=20;

    PdfDocument pdfDocument;
    PdfDocument.PageInfo pageInfo;
    PdfDocument.Page page;

    Paint paint,bgPaint;
    Canvas canvas;

    Bitmap img,scaledImg;
    TextPaint textPaint;
    StaticLayout staticLayout;
    int textWidth;
    String text;

//    Var of patients data
    String appointmentId;
    String patientName;
    String patientAge;
    String patientGender;
    String patientPhone;
    String patientBloodGroup;
    String advice;
    String[] medicine;
    String followup;

//    Constructor
    public  MakePrescription() {}

    public MakePrescription(Context context) {
        this.context=context;
    }

    public String getAppointmentId() { return appointmentId; }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientName() { return patientName; }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientPhone() { return patientPhone; }

    public void setPatientPhone(String patientPhone) { this.patientPhone = patientPhone; }

    public String getPatientBloodGroup() {
        return patientBloodGroup;
    }

    public void setPatientBloodGroup(String patientBloodGroup) { this.patientBloodGroup = patientBloodGroup; }

    public String getAdvice() { return advice; }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String[] getMedicine() {
        return medicine;
    }

    public void setMedicine(String[] medicine) {
        this.medicine = medicine;
    }

    public String getFollowup() {
        return followup;
    }

    public void setFollowup(String followup) {
        this.followup = followup;
    }

    //    Method for creating PDF
    public void onCreatePdf(){

        pdfDocument=new PdfDocument();
        pageInfo=new PdfDocument.PageInfo.Builder(outerWidth, outerHeight, 1).create();
        page= pdfDocument.startPage(pageInfo);

        paint=new Paint();
        bgPaint=new Paint();
        canvas=page.getCanvas();
        int xPos,yPos;

//        Draw Images
        // Image 3 background as image
        img= BitmapFactory.decodeResource(context.getResources(), R.drawable.pdfpicture3);
        scaledImg= Bitmap.createScaledBitmap(img, 555, 553, false);
        canvas.drawBitmap(scaledImg, margin, ((outerHeight-(2*margin))-553)/2, bgPaint);
        // Image 1
        img= BitmapFactory.decodeResource(context.getResources(), R.drawable.pdfpicture1);
        scaledImg= Bitmap.createScaledBitmap(img, 185, 150, false);
        canvas.drawBitmap(scaledImg, ((outerWidth-(2*margin))*2)/3, margin, paint);
        // Image 2
        img= BitmapFactory.decodeResource(context.getResources(), R.drawable.pdfpicture2);
        scaledImg= Bitmap.createScaledBitmap(img, 23, 30, false);
//        canvas.drawBitmap(scaledImg, ((outerWidth-(2*margin))-23)/2, 200, paint);
        canvas.drawBitmap(scaledImg, margin+220, margin+200, paint);

//        Draw Line
        paint=new Paint();
//        paint.setTypeface(context.getResources().getFont(R.font.fredoka_bold));
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(5f);
        canvas.drawLine(margin, 170, 350, 170, paint);
        canvas.drawLine(230, 180, 230, 750, paint);

//        DR Name
        textPaint = new TextPaint();
        textPaint.setTypeface(context.getResources().getFont(R.font.fredoka_bold));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        textPaint.setAntiAlias(true);
        text="Dr. Rajesh Verma";
        textWidth=((outerWidth-(2*margin))*2)/3;
        staticLayout = new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false); // Wrap line
//        int yPos = (outerHeight / 2) - (staticLayout.getHeight() / 2);
        canvas.save();
        canvas.translate(margin+0, margin+0);
        staticLayout.draw(canvas);
        canvas.restore();

//        Date
        textPaint = new TextPaint();
        textPaint.setTypeface(context.getResources().getFont(R.font.fredoka_bold));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(15);
        textPaint.setAntiAlias(true);
        text=new Date().toLocaleString();
        textWidth=185;
        staticLayout = new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false); // Wrap line
//        int yPos = (outerHeight / 2) - (staticLayout.getHeight() / 2);
        canvas.save();
        canvas.translate(370, margin+150+10);
        staticLayout.draw(canvas);
        canvas.restore();

//        Patient
        textWidth=210;
        xPos=margin;
        yPos=200;
        textPaint= new TextPaint();
        textPaint.setTypeface(context.getResources().getFont(R.font.fredoka_medium));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(25);
        textPaint.setAntiAlias(true);

        text="Name : "+getPatientName();
        staticLayout= new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        canvas.save();
        canvas.translate(xPos, yPos);
        staticLayout.draw(canvas);
        yPos += staticLayout.getHeight()+10;
        canvas.restore();

        text="Age : "+getPatientAge();
        staticLayout= new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        canvas.save();
        canvas.translate(xPos, yPos);
        staticLayout.draw(canvas);
        yPos += staticLayout.getHeight()+10;
        canvas.restore();

        text="Gender : "+getPatientGender();
        staticLayout= new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        canvas.save();
        canvas.translate(xPos, yPos);
        staticLayout.draw(canvas);
        yPos += staticLayout.getHeight()+10;
        canvas.restore();

        text="Advice : "+getAdvice();
        staticLayout= new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        canvas.save();
        canvas.translate(xPos, yPos);
        staticLayout.draw(canvas);
//        yPos += staticLayout.getHeight();
        canvas.restore();

//        Medicine
        textPaint= new TextPaint();
        textPaint.setTypeface(context.getResources().getFont(R.font.fredoka_semibold));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(25);
        textPaint.setAntiAlias(true);
        yPos= 270;
        xPos= 270;
        textWidth=300;
        for (String text: getMedicine()) {
            staticLayout= new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_CENTER, 1, 0, false);
            canvas.save();
            canvas.translate(xPos, yPos);
            staticLayout.draw(canvas);
            canvas.restore();
            yPos+=staticLayout.getHeight()+10;
//            Log.d(TAG, "onCreatePdf: "+yPos+" "+textPaint.descent()+" "+textPaint.ascent());
        }

//        Bottom Info
        textPaint= new TextPaint();
        textPaint.setTypeface(context.getResources().getFont(R.font.fredoka_bold));
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(25);
        textPaint.setAntiAlias(true);
        text="Developed and Created by Assist DR.";
        textWidth= 555;
        staticLayout= new StaticLayout(text,textPaint, textWidth, Layout.Alignment.ALIGN_CENTER, 1, 0, false);
        canvas.save();
        canvas.translate(0, outerHeight-margin-staticLayout.getHeight());
        staticLayout.draw(canvas);
        canvas.restore();
//        yPos = margin+staticLayout.getHeight();

        pdfDocument.finishPage(page);
//        savePdf();
//        pdfDocument.close();
    }

//    Method for Saving PDF
    public void savePdf() {

        Date date=new Date();
        String fileName=date.toLocaleString()+" "+getAppointmentId()+".pdf";
//        String fileName=date.toLocaleString()+".pdf";
//        String fileName="test.pdf";
        File file=new File(context.getExternalFilesDir(null)+File.separator+"Prescriptions"+File.separator+fileName);
        try {
//            Log.d(TAG, "savePdf: "+fileName);
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.exists()){
            Toast.makeText(context, "Prescription Created Successfully : "+fileName, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Prescription Couldn't Created ", Toast.LENGTH_LONG).show();
        }

        pdfDocument.close();
    }
}
