package com.techcare.assistdr.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.techcare.assistdr.BuildConfig;
import com.techcare.assistdr.R;
import com.techcare.assistdr.modules.PrescriptionFile;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder>{
    private Context context;
    private List<PrescriptionFile> prescriptionFileList;

    public PrescriptionAdapter(Context context, List<PrescriptionFile> prescriptionFileList) {
        this.context = context;
        this.prescriptionFileList = prescriptionFileList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_file_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        PrescriptionFile prescriptionFile= prescriptionFileList.get(position);
        holder.fileName.setText(prescriptionFile.getName());
        holder.fileDate.setText(prescriptionFile.getDate());
        holder.fileTime.setText(prescriptionFile.getTime());
    }

    @Override
    public int getItemCount() {
        return prescriptionFileList.size();
    }

    //    Inner Class View Holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView fileName;
        public TextView fileDate;
        public TextView fileTime;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            fileName= itemView.findViewById(R.id.fileName_textView);
            fileDate= itemView.findViewById(R.id.fileDate_textView);
            fileTime= itemView.findViewById(R.id.fileTime_textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos= this.getAdapterPosition();
            Toast.makeText(context, "Position : "+pos, Toast.LENGTH_SHORT).show();
            PrescriptionFile prescriptionFile= prescriptionFileList.get(pos);
            Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                File file = new File(prescriptionFile.getFilePath());
                Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider", file);
                pdfOpenintent.setDataAndType(path, "application/pdf");
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            } else {
                pdfOpenintent.setDataAndType(Uri.parse(prescriptionFile.getFilePath()), "application/pdf");
            }
            try {
                context.startActivity(pdfOpenintent);
            } catch (ActivityNotFoundException e) {
                Log.d("TAG", "onClick: "+e.getLocalizedMessage());
            }
        }
    }
}
