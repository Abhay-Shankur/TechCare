package com.techcare.assistdr.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.techcare.assistdr.AppointmentDetailActivity;
import com.techcare.assistdr.R;
import com.techcare.assistdr.modules.AppointmentDetails;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private Context context;
    private List<AppointmentDetails> dataList;

    public AppointmentAdapter(Context context, List<AppointmentDetails> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_appointment_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull  ViewHolder holder, int position) {
        AppointmentDetails appointmentDetails =dataList.get(position);
        holder.Name.setText(appointmentDetails.getappointmentName());
        holder.Phone.setText(appointmentDetails.getappointmentPhone());
        holder.Date.setText(appointmentDetails.getAppointmentSchedule());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

//  Inner View Holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout constraintLayout;
        ImageView imageView;
        TextView Name,Phone,Date;
        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            constraintLayout=itemView.findViewById(R.id.appointmentCardviewLayout);
            imageView=itemView.findViewById(R.id.appointmentCardviewImage);
            Name=itemView.findViewById(R.id.appointmentCardviewName);
            Phone=itemView.findViewById(R.id.appointmentCardviewPhone);
            Date=itemView.findViewById(R.id.appointmentCardviewDate);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            int pos= this.getAdapterPosition();
            AppointmentDetails appointmentDetails = dataList.get(pos);
            Intent intent= new Intent(context, AppointmentDetailActivity.class);
            intent.putExtra("AppointmentId", appointmentDetails.getAppointmentId());
            context.startActivity(intent);
        }
    }
}
