package com.techcare.assistdr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import com.techcare.assistdr.databinding.ActivityMakePrescriptionBinding;
import com.techcare.assistdr.databinding.ActivityMakePrescriptionCopyBinding;

import java.util.ArrayList;

public class MakePrescriptionActivityCopy extends AppCompatActivity {

    private ActivityMakePrescriptionCopyBinding activityMakePrescriptionBinding;
    public SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    public Intent recognizerIntent;
    private static final int RECOGNIZER_REQUSET_CODE=3812;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMakePrescriptionBinding= ActivityMakePrescriptionCopyBinding.inflate(getLayoutInflater());
        setContentView(activityMakePrescriptionBinding.getRoot());
        getSupportActionBar().hide();

        if (ContextCompat.checkSelfPermission(MakePrescriptionActivityCopy.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MakePrescriptionActivityCopy.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECOGNIZER_REQUSET_CODE);
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MakePrescriptionActivityCopy.this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getCallingPackage());
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        activityMakePrescriptionBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechRecognizer.startListening(recognizerIntent);
            }
        });

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
//                Log.d("TAG", "onReadyForSpeech: ");
            }

            @Override
            public void onBeginningOfSpeech() {
                activityMakePrescriptionBinding.CustomStatusTextView.setText("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {
//                Log.d("TAG", "onRmsChanged: "+v);
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
//                Log.d("TAG", "onBufferReceived: "+bytes.toString());
            }

            @Override
            public void onEndOfSpeech() {
                activityMakePrescriptionBinding.CustomStatusTextView.setText("End...");
//                speechRecognizer.stopListening();
//                speechRecognizer.startListening(recognizerIntent);
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MakePrescriptionActivityCopy.this);
                recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getCallingPackage());
//                recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
                speechRecognizer.startListening(recognizerIntent);
            }

            @Override
            public void onError(int i) {
//                Log.d("TAG", "onError: "+i);
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                activityMakePrescriptionBinding.CustomResultTextView.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                activityMakePrescriptionBinding.CustomResultTextView.setText(data.get(0));
//                for (int i=0; i<=data.size(); i++) {
//                    if (data.get(i).contains("stop")) {
//                        Log.d("TAG", "onPartialResults: "+data.get(0));
//    //                    TODO Continues speech and wake on keyword
//    //                    speechRecognizer.startListening(recognizerIntent);
//    //                    speechRecognizer.startListening(recognizerIntent);
//    //                    speechRecognizer.stopListening();
//                    }
//                }
//                for (String line : data) {
////                    if (line.contains("hello")) {
////                        Log.d("TAG", "onPartialResults: "+line);
////                    }
//                    Log.d("TAG", "onPartialResults: "+line);
//                }
//                if (data.get(0).contains("stop")) {
//                    Log.d("TAG", "onPartialResults: "+data.get(0));
////                    TODO Continues speech and wake on keyword
////                    speechRecognizer.startListening(recognizerIntent);
////                    speechRecognizer.startListening(recognizerIntent);
////                    speechRecognizer.stopListening();
//                }
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.d("TAG", "onEvent: "+i);
            }
        });



//        MakePrescription makePrescription= new MakePrescription(MakePrescriptionActivity.this);
//        makePrescription.onCreatePdf();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==RECOGNIZER_REQUSET_CODE && grantResults[0]== PackageManager.PERMISSION_DENIED) {
            textToSpeech.speak("Permission for Audio not Granted", TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
}