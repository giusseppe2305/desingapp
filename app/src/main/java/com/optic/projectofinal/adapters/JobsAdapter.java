package com.optic.projectofinal.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.ValuationActivity;
import com.optic.projectofinal.UI.activities.fragments.tabsFragments.auctions.MyAuctionsFragment;
import com.optic.projectofinal.databinding.CardviewJobOfferedBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.providers.JobsDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {
    private static final String TAG = "own";
    MyAuctionsFragment  context;
    ArrayList<Job> listJobs;
    private Job.State state;

    public JobsAdapter(MyAuctionsFragment c, ArrayList<Job> listJobs, Job.State state) {
        context = c;
        this.listJobs = listJobs;
        this.state = state;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context.getContext()).inflate(R.layout.cardview_job_offered, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull JobsAdapter.ViewHolder holder, int position) {
        Job job = listJobs.get(position);
        if (state == Job.State.IN_PROGRESS) {
            holder.binding.getRoot().setBackgroundColor(Color.BLUE);
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MaterialAlertDialogBuilder(context.getContext())
                            .setTitle("Confirmacio")
                            .setMessage("Esta seguro de marcar como terminado el trabajo")
                            .setPositiveButton("Terminado", (dialogInterface, i) -> {
                                Job update = new Job();
                                update.setId(job.getId());
                                update.setState(Job.State.FINISHED);
                                new JobsDatabaseProvider().updateJob(update).addOnFailureListener(v -> Log.e(TAG, "put finished job onClick: " + v.getMessage()));
                                context.loadData();
                                dialogInterface.dismiss();

                                new MaterialAlertDialogBuilder(context.getContext())
                                        .setTitle("Opinion")
                                        .setMessage("¿Quieres poner la opinion ahora?")
                                        .setPositiveButton("Opinar",(dialogInterface1, i1) -> {
                                            Intent intent=new Intent(context.getContext(), ValuationActivity.class);
                                            intent.putExtra("idJob",job.getId());
                                            context.startActivity(intent);

                                        })
                                        .setNegativeButton("Despues",null)
                                        .show();
                            }).setNegativeButton("Cancelar", null)
                    .show();
                }
            });
        } else if (state == Job.State.FINISHED) {
            holder.binding.getRoot().setBackgroundColor(Color.RED);
        }
        holder.binding.title.setText(job.getTitle());
        holder.binding.description.setText(job.getDescription());
        holder.binding.timestamp.setText(Utils.getDateFormatted(job.getTimestamp()));
        Glide.with(context).load(job.getImages().get(0)).apply(Utils.getOptionsGlide(true)).transform(Utils.getTransformSquareRound()).into(holder.binding.imageJob);
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
