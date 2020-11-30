package com.optic.projectofinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.CardviewJobOfferedBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.utils.Utils;

import java.util.List;

public class AuctionsAdapter extends RecyclerView.Adapter<AuctionsAdapter.ViewHolder> {
    Context context;
    List<Job> listJobs;

    public AuctionsAdapter(Context c, List<Job> listJobs) {
        context = c;
        this.listJobs = listJobs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.cardview_job_offered, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AuctionsAdapter.ViewHolder holder, int position) {
        Job job = listJobs.get(position);
        holder.binding.title.setText(job.getTitle());
        holder.binding.description.setText(job.getDescription());
        holder.binding.timestamp.setText(Utils.getStringFromTimestamp(job.getTimestamp()));

//do subquery
        Glide.with(context).load("https://i.ytimg.com/vi/sB8n58FHBxw/hqdefault.jpg?sqp=-oaymwEYCKgBEF5IVfKriqkDCwgBFQAAiEIYAXAB&rs=AOn4CLCTm9lQUW36rZaFXadAoH1TH3sW0w").apply(Utils.getOptionsGlide(true))
        .transform(Utils.getTransformSquareRound()).into(holder.binding.imageJob);
        holder.binding.countApplyWorkers.setText("2 trabajadores en la puja");
        holder.binding.averagePrice.setText("20.00");

    }

    @Override
    public int getItemCount() {
        return listJobs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardviewJobOfferedBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardviewJobOfferedBinding.bind(itemView);
        }
    }
}
