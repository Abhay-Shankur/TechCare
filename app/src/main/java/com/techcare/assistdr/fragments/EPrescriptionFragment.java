package com.techcare.assistdr.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techcare.assistdr.MakePrescriptionActivity;
import com.techcare.assistdr.adapters.PrescriptionAdapter;
import com.techcare.assistdr.databinding.FragmentEPrescriptionBinding;
import com.techcare.assistdr.modules.PrescriptionFile;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class EPrescriptionFragment extends Fragment {

//    Initialize
    FragmentEPrescriptionBinding fragmentEPrescriptionBinding;
    ArrayList<PrescriptionFile> prescriptionFileArrayList;

//    Constructor
    public EPrescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentEPrescriptionBinding= FragmentEPrescriptionBinding.inflate(inflater, container, false);

//        Floating to Open Make Prescription Activity
//        fragmentEPrescriptionBinding.addFileFloatingButton.setVisibility(View.GONE);
        fragmentEPrescriptionBinding.addFileFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext(), MakePrescriptionActivity.class);
                intent.putExtra("AppointmentId", "Null");
                startActivity(intent);
            }
        });

        return  fragmentEPrescriptionBinding.getRoot();
    }

    @Override // Things to get after Refresh
    public void onResume() {
        super.onResume();

        String path=getContext().getExternalFilesDir(null)+File.separator+"Prescriptions";
        File dir=new File(path);
        File[] files=dir.listFiles();
        if (files.length!=0) {
            fragmentEPrescriptionBinding.prescriptionCardView.setVisibility(View.GONE);
            fragmentEPrescriptionBinding.prescriptionRecyclerView.setVisibility(View.VISIBLE);

            prescriptionFileArrayList= new ArrayList<>();
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

            for (File file:files) {
                String[] arrDate=file.getName().split("\\s");
                ArrayList<String> arrayList=new ArrayList<>(Arrays.asList(arrDate));
//            SimpleDateFormat dateFormat= new SimpleDateFormat()
                String temp=arrayList.remove(arrayList.size()-1).split("\\.")[0];
                String date=arrayList.get(0)+arrayList.get(1)+arrayList.get(2);
                String time=arrayList.get(3)+arrayList.get(4);
                String filePath=file.getPath();

                PrescriptionFile prescriptionFile= new PrescriptionFile(temp, temp, date, time, filePath);
                prescriptionFileArrayList.add(prescriptionFile);
            }

            fragmentEPrescriptionBinding.prescriptionRecyclerView.setHasFixedSize(true);
            fragmentEPrescriptionBinding.prescriptionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            PrescriptionAdapter prescriptionAdapter= new PrescriptionAdapter(getContext(), prescriptionFileArrayList);
            fragmentEPrescriptionBinding.prescriptionRecyclerView.setAdapter(prescriptionAdapter);
            
        } else  {
            fragmentEPrescriptionBinding.prescriptionRecyclerView.setVisibility(View.GONE);
            fragmentEPrescriptionBinding.prescriptionCardView.setVisibility(View.VISIBLE);
        }

    }
}