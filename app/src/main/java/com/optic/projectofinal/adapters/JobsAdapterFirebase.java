package com.optic.projectofinal.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

public class JobsAdapterFirebase extends FirestoreRecyclerAdapter<Job, JobsAdapterFirebase.ViewHolder> {
    Context context;
    UserDatabaseProvider mUserProvider;

    public JobsAdapterFirebase(Context c, @NonNull FirestoreRecyclerOptions<Job> options) {
        super(options);
        context = c;
        mUserProvider = new UserDatabaseProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull Job model) {
        String idIterated=getSnapshots().getSnapshot(position).getId();
        holder.binding.title.setText(model.getTitle());
        holder.binding.description.setText(model.getDescription());
        holder.binding.timestamp.setText(Utils.getDateFormatted(model.getTimestamp()));
        Glide.with(context).load(model.getImages().get(0)).apply(Utils.getOptionsGlide(true)).transform(Utils.getTransformSquareRound()).into(holder.binding.imageJob);

        //load image
        holder.binding.getRoot().setOnClickListener(v->{
            Intent i=new Intent(context, JobOfferedActivity.class);
            i.putExtra("idJobSelected",idIterated);
            i.putExtra("idUserCreateJobSelected",model.getIdUserOffer());
            context.startActivity(i);
        });
        ////ocultar mis propios tranajos
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
            binding=CardviewJobOfferedBinding.bind(itemView);

        }

    }

}
