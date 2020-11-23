package com.webbeansvipul.psstore.Madhu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.webbeansvipul.psstore.R;

import java.util.ArrayList;

public class DrAdapter extends RecyclerView.Adapter<DrAdapter.MyViewHolder> {
    ArrayList<LabModel.labtestlist> al;
    Context context;

    public DrAdapter(ArrayList<LabModel.labtestlist> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPrice , tvDescription  ;
        CardView cardView ;

        public MyViewHolder(View view) {
            super(view);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            tvPrice = (TextView) view.findViewById(R.id.tvprice);
            tvName = (TextView) view.findViewById(R.id.tvtestName);
            cardView = (CardView)view.findViewById(R.id.drCard);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_test_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final LabModel.labtestlist data = al.get(position);

        holder.tvName.setText(data.name);
        holder.tvDescription.setText(data.speciality);
        holder.tvPrice.setVisibility(View.GONE);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context,AskDoctorActivity.class);
                in.putExtra("type","true");
                in.putExtra("name",data.name);
                in.putExtra("id",data.id);
                in.putExtra("speciality",data.speciality);
                context.startActivity(in);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }
}
