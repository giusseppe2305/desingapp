package com.optic.projectofinal.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.CardViewWorkerApplyJobBinding;
import com.optic.projectofinal.models.ApplyJob;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

public class ApplyJobAdapterFirebase extends FirestoreRecyclerAdapter<ApplyJob, ApplyJobAdapterFirebase.ViewHolder> {
    Context context;
    UserDatabaseProvider mUserProvider;

    public ApplyJobAdapterFirebase(Context c, @NonNull FirestoreRecyclerOptions<ApplyJob> options) {
        super(options);
        context = c;
        mUserProvider = new UserDatabaseProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull ApplyJob model) {
        holder.binding.descriptionApplyJob.setText(model.getMessage());
        holder.binding.priceApplyJob.setText(String.valueOf(model.getPrice()));
        ///load user data
        String idWorker=getSnapshots().getSnapshot(position).getId().trim();

        loadApplyWorkerData(holder, model.getIdWorkerApply());
    }

    private void loadApplyWorkerData(@NonNull ViewHolder holder, String worker) {
        mUserProvider.getUser(worker).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot!=null&& documentSnapshot.exists()){
                    String name=documentSnapshot.getString("name");
                    String profileImage=documentSnapshot.getString("profileImage");
                    holder.binding.nameUser.setText(name);
                    Glide.with(context).load(profileImage).apply(Utils.getOptionsGlide(false)).transform(Utils.getTransformSquareRound()).into(holder.binding.imageProfile);
                }else{
                    Log.e("own", "onSuccess ApplyJobAdapterFirebase->onBindViewHolder : null" );
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("own", "onFailure: ApplyJobAdapterFirebase->onBindViewHolde");
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_worker_apply_job, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardViewWorkerApplyJobBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CardViewWorkerApplyJobBinding.bind(itemView);

        }

    }

}
