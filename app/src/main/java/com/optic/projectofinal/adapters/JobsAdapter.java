package com.optic.projectofinal.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.JobOfferedActivity;
import com.optic.projectofinal.databinding.CardviewJobOfferedBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    private Context  context;
    private ArrayList<Job> listJobs;

    public JobsAdapter(Context c, ArrayList<Job> listJobs) {
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
    public void onBindViewHolder(@NonNull JobsAdapter.ViewHolder holder, int position) {
        Job model = listJobs.get(position);

        holder.binding.title.setText(model.getTitle());
        holder.binding.description.setText(model.getDescription());
        holder.binding.timestamp.setText(Utils.getDateFormatted(model.getTimestamp(),context));
        Glide.with(context).load(model.getImages().get(0)).apply(Utils.getOptionsGlide(true)).transform(Utils.getTransformSquareRound()).into(holder.binding.imageJob);

        //load image
        holder.binding.getRoot().setOnClickListener(v->{
            Intent i=new Intent(context, JobOfferedActivity.class);
            i.putExtra("idJobSelected",model.getId());
            i.putExtra("idUserCreateJobSelected",model.getIdUserOffer());
            context.startActivity(i);
        });
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
