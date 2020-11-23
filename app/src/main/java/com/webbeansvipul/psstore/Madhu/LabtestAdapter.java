package com.webbeansvipul.psstore.Madhu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webbeansvipul.psstore.R;

import java.util.ArrayList;

public class LabtestAdapter extends RecyclerView.Adapter<LabtestAdapter.MyViewHolder> {
    ArrayList<LabModel.labtestlist> al;
    Context context;

    public LabtestAdapter(ArrayList<LabModel.labtestlist> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPrice , tvDescription  ;

        public MyViewHolder(View view) {
            super(view);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            tvPrice = (TextView) view.findViewById(R.id.tvprice);
            tvName = (TextView) view.findViewById(R.id.tvtestName);
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
        holder.tvPrice.setText("Offer Price - Rs. "+data.price);
        holder.tvDescription.setText(data.details);

    }


    @Override
    public int getItemCount() {
        return al.size();
    }
}
