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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.JobOfferedActivity;
import com.optic.projectofinal.databinding.CardviewJobOfferedBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.modelsRetrofit.JobsQueryModel;
import com.optic.projectofinal.retrofit.FunctionsApi;
import com.optic.projectofinal.retrofit.RetrofitClient;
import com.optic.projectofinal.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class JobsAdapterFirebase extends FirestoreRecyclerAdapter<Job, JobsAdapterFirebase.ViewHolder> {
    private Context context;

    public JobsAdapterFirebase(Context c, @NonNull FirestoreRecyclerOptions<Job> options) {

        super(options);
        context = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull Job model) {

        String idIterated = getSnapshots().getSnapshot(position).getId();
        holder.binding.title.setText(model.getTitle());
        holder.binding.description.setText(model.getDescription());
        holder.binding.timestamp.setText(Utils.getDateFormattedSimple(model.getTimestamp(), context));
        Glide.with(context).load(model.getThumbnail()).apply(Utils.getOptionsGlide(true)).transform(Utils.getTransformSquareRound()).into(holder.binding.imageJob);

        //load image
        holder.binding.getRoot().setOnClickListener(v -> {
            Intent i = new Intent(context, JobOfferedActivity.class);
            i.putExtra("idJobSelected", idIterated);
            i.putExtra("idUserCreateJobSelected", model.getIdUserOffer());
            context.startActivity(i);
        });
        ////
        RetrofitClient.getClient(RetrofitClient.FIREBASE_FUNCTIONS).create(FunctionsApi.class).jobs(model.getId()).enqueue(new Callback<JobsQueryModel>() {
            @Override
            public void onResponse(Call<JobsQueryModel> call, Response<JobsQueryModel> response) {
                Log.d(TAG_LOG, "onResponse: "+call.request().url().toString());
                if(response.isSuccessful()){
                    if(response.body().getCount()==0){
                        holder.binding.countApplyWorkers.setText("Se el primero");
                        holder.binding.averagePrice.setText("Se el primero");
                    }else{
                        holder.binding.countApplyWorkers.setText("Hay "+response.body()+" trabajadores aplicando.");
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


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_job_offered, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardviewJobOfferedBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardviewJobOfferedBinding.bind(itemView);

        }

    }

}
