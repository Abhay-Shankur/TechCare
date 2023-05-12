package com.techcare.findmydr.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techcare.findmydr.DoctorDetailActivity;
import com.techcare.findmydr.R;
import com.techcare.findmydr.api.tablesclass.TableDoctors;

import java.util.List;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder> {

    Context context;
    List<TableDoctors> list;

    public DoctorListAdapter(Context context, List<TableDoctors> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_top_doctors_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TableDoctors doctorInfo=list.get(position);
        holder.profileImg.setImageDrawable(Drawable.createFromPath(doctorInfo.getDoctorProfilepic()));
        holder.drName.setText(doctorInfo.getDoctorName());
        holder.drSpecialis.setText(doctorInfo.getDoctorSpecialis());
        holder.drRating.setText(doctorInfo.getDoctorRating());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profileImg;
        TextView drName,drSpecialis,drRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this::onClick);
            profileImg=itemView.findViewById(R.id.profile_imageView);
            drName=itemView.findViewById(R.id.name_textView);
            drSpecialis=itemView.findViewById(R.id.specialis_textView);
            drRating=itemView.findViewById(R.id.rating_textView);
        }

        @Override
        public void onClick(View view) {
            int pos=this.getAdapterPosition();
            TableDoctors doctorInfo=list.get(pos);
            Intent intent= new Intent(context, DoctorDetailActivity.class);
            intent.putExtra("Doctor Id",doctorInfo.getDoctorUid());
            context.startActivity(intent);
        }
    }
}
