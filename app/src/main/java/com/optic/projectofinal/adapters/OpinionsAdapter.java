package com.optic.projectofinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.CardviewJobDoneBinding;
import com.optic.projectofinal.models.Opinion;
import com.optic.projectofinal.utils.Utils;

import java.util.List;

public class OpinionsAdapter extends RecyclerView.Adapter<OpinionsAdapter.ViewHolder> {
    Context context;
    List<Opinion> listOpinions;
    public OpinionsAdapter(Context c, List<Opinion> listOpinions) {
        context=c;
        this.listOpinions =listOpinions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(context).inflate(R.layout.cardview_job_done,parent,false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull OpinionsAdapter.ViewHolder holder, int position) {
        Opinion opinion=listOpinions.get(position);
        holder.binding.average.setText(String.valueOf(opinion.getAverageValuation()));
        holder.binding.message.setText(opinion.getMessage());
        holder.binding.timestamp.setText(Utils.getStringFromTimestamp(opinion.getTimestamp()));
        holder.binding.title.setText(opinion.getTitleJob());
        Glide.with(context).load(opinion.getImageJob()).apply(Utils.getOptionsGlide(true)).transform(Utils.getTransformSquareRound()).into(holder.binding.image);
        if(opinion.isFromJob()){
        }
    }

    @Override
    public int getItemCount() {
        return listOpinions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardviewJobDoneBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CardviewJobDoneBinding.bind(itemView);
        }
    }
}
