package com.optic.projectofinal.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.JobOfferedActivity;
import com.optic.projectofinal.databinding.CardviewJobOfferedBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.providers.ApplyJobWorkerDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.util.List;
import java.util.Locale;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class AuctionsAdapter extends RecyclerView.Adapter<AuctionsAdapter.ViewHolder> {

    private Context context;
    private List<Job> listJobs;

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
        Glide.with(context).load(job.getThumbnail()).apply(Utils.getOptionsGlide(false))
                .transform(Utils.getTransformSquareRound()).into(holder.binding.imageJob);

        new ApplyJobWorkerDatabaseProvider().getAllById(job.getId()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    holder.binding.countApplyWorkers.setText(String.format(Locale.getDefault(),
                            "%d %s", queryDocumentSnapshots.size(),
                            context.getString(R.string.jobs_adapter_workers_apply)));
                    double average = 0;
                    if (queryDocumentSnapshots.size() > 0) {
                        for (QueryDocumentSnapshot it : queryDocumentSnapshots) {
                            average += it.getDouble("price");
                        }
                        average = average / queryDocumentSnapshots.size();
                    }
                    if (average == 0) {
                        holder.binding.averagePrice.setText(R.string.jobs_adapter_workers_be_first);
                    } else {
                        holder.binding.averagePrice.setText(Utils.getFormatPrice(average,context));
                    }
                }
        ).addOnFailureListener(e -> Log.e(TAG_LOG, "onBindViewHolder: failure get num applyworkers " + e.getMessage()));

        holder.binding.getRoot().setOnClickListener(v->{
            Intent i=new Intent(context, JobOfferedActivity.class);
            i.putExtra("idJobSelected",job.getId());
            i.putExtra("idUserCreateJobSelected",job.getIdUserOffer());
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
