package com.techcare.assistdr.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.techcare.assistdr.AppointmentDetailActivity;
import com.techcare.assistdr.R;
import com.techcare.assistdr.api.tablesclass.TableAppointmentDetails;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private Context context;
    private List<TableAppointmentDetails> dataList;

    public AppointmentAdapter(Context context, List<TableAppointmentDetails> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_appointment_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TableAppointmentDetails appointmentDetails =dataList.get(position);
        holder.Name.setText(appointmentDetails.getAppointmentname());
        holder.Phone.setText(appointmentDetails.getAppointmentphone());
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
        public ViewHolder(@NonNull View itemView) {
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
            TableAppointmentDetails appointmentDetails = dataList.get(pos);
            Intent intent= new Intent(context, AppointmentDetailActivity.class);
            intent.putExtra("AppointmentId", appointmentDetails.getAppointmentId());
            context.startActivity(intent);
        }
    }
}
