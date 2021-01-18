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
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.JobOfferedActivity;
import com.optic.projectofinal.databinding.CardviewJobOfferedBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.modelsRetrofit.JobsQueryModel;
import com.optic.projectofinal.retrofit.FunctionsApi;
import com.optic.projectofinal.retrofit.RetrofitClient;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

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
        holder.binding.timestamp.setText(Utils.getDateFormattedSimple(model.getTimestamp(),context));
        Glide.with(context).load(model.getThumbnail()).apply(Utils.getOptionsGlide(false)).transform(Utils.getTransformSquareRound()).into(holder.binding.imageJob);

        //load image
        holder.binding.getRoot().setOnClickListener(v->{
            Intent i=new Intent(context, JobOfferedActivity.class);
            i.putExtra("idJobSelected",model.getId());
            i.putExtra("idUserCreateJobSelected",model.getIdUserOffer());
            context.startActivity(i);
        });


        RetrofitClient.getClient(RetrofitClient.FIREBASE_FUNCTIONS).create(FunctionsApi.class).jobs(model.getId()).enqueue(new Callback<JobsQueryModel>() {
            @Override
            public void onResponse(Call<JobsQueryModel> call, Response<JobsQueryModel> response) {

                if(response.isSuccessful()){
                    if(response.body().getCount()==0){
                        holder.binding.countApplyWorkers.setText(R.string.jobs_adapter_workers_be_first);
                        holder.binding.averagePrice.setText(R.string.jobs_adapter_workers_be_first);
                    }else{
                        holder.binding.countApplyWorkers.setText(response.body().getCount()+context.getString(R.string.jobs_adapter_workers_apply));
                        holder.binding.averagePrice.setText(Utils.getFormatPrice(response.body().getAverage(),context));
                    }
                }
            }

            @Override
            public void onFailure(Call<JobsQueryModel> call, Throwable t) {
                Log.e(TAG_LOG, "onFailure: "+t.getMessage() );
            }
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
