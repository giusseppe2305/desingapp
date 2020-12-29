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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.ValuationActivity;
import com.optic.projectofinal.UI.activities.fragments.tabsFragments.auctions.MyAuctionsFragment;
import com.optic.projectofinal.databinding.CardviewJobOfferedBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.providers.ApplyJobWorkerDatabaseProvider;
import com.optic.projectofinal.providers.JobsDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class JobsAdapterSettings extends RecyclerView.Adapter<JobsAdapterSettings.ViewHolder> {

    private MyAuctionsFragment  context;
    private ArrayList<Job> listJobs;
    private Job.State state;

    public JobsAdapterSettings(MyAuctionsFragment c, ArrayList<Job> listJobs, Job.State state) {
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
    public void onBindViewHolder(@NonNull JobsAdapterSettings.ViewHolder holder, int position) {
        Job job = listJobs.get(position);
        if (state == Job.State.IN_PROGRESS) {
            holder.binding.getRoot().setBackgroundColor(Color.BLUE);
            holder.binding.getRoot().setOnClickListener(view -> new MaterialAlertDialogBuilder(context.getContext())
                    .setTitle(context.getString(R.string.jobs_adapter_alert_confirm_title))
                    .setMessage(context.getString(R.string.jobs_adapter_alert_confirm_message))
                    .setPositiveButton(context.getString(R.string.jobs_adapter_alert_confirm_positive_button), (dialogInterface, i) -> {
                        Job update = new Job();
                        update.setId(job.getId());
                        update.setState(Job.State.FINISHED);

                        new ApplyJobWorkerDatabaseProvider().getApplyWorker(job.getId(),job.getIdUserApply()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                update.setTotalPrice(documentSnapshot.getDouble("price"));
                                new JobsDatabaseProvider().updateJob(update).addOnFailureListener(v -> Log.e(TAG_LOG, "put finished job onClick: " + v.getMessage()));
                                context.loadData();
                            }
                        }).addOnFailureListener(e -> Log.e(TAG_LOG, "onBindViewHolder: "+e.getMessage() ));

                        dialogInterface.dismiss();

                        new MaterialAlertDialogBuilder(context.getContext())
                                .setTitle(context.getString(R.string.jobs_adapter_alert_opinion_title))
                                .setMessage(context.getString(R.string.jobs_adapter_alert_opinion_message))
                                .setPositiveButton(context.getString(R.string.jobs_adapter_alert_opinion_positive_button),(dialogInterface1, i1) -> {
                                    Intent intent=new Intent(context.getContext(), ValuationActivity.class);
                                    intent.putExtra("idJob",job.getId());
                                    context.startActivity(intent);

                                })
                                .setNegativeButton(context.getString(R.string.jobs_adapter_alert_opinion_negative_button),null)
                                .show();
                    }).setNegativeButton(context.getString(R.string.jobs_adapter_alert_confirm_negative_button), null)
            .show());
        } else if (state == Job.State.FINISHED) {
            holder.binding.getRoot().setBackgroundColor(Color.RED);
        }
        holder.binding.title.setText(job.getTitle());
        holder.binding.description.setText(job.getDescription());
        holder.binding.timestamp.setText(Utils.getDateFormattedSimple(job.getTimestamp(),context.getContext()));
        Glide.with(context).load(job.getThumbnail()).apply(Utils.getOptionsGlide(true)).transform(Utils.getTransformSquareRound()).into(holder.binding.imageJob);
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
