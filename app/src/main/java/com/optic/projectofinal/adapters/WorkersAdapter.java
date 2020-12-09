package com.optic.projectofinal.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.ProfileDetailsActivity;
import com.optic.projectofinal.databinding.CardviewWorkerMainBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.JobsDatabaseProvider;
import com.optic.projectofinal.providers.LikeWorkersDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;

public class WorkersAdapter extends RecyclerView.Adapter<WorkersAdapter.ViewHolder> {

    private static final String TAG = "own";
    private AuthenticationProvider mAuth;
    private final Context context;
    private ArrayList<User> listWorker;
    private LikeWorkersDatabaseProvider mLikeProvider;

    public WorkersAdapter(Context c, ArrayList<User> listWorker) {

        context = c;
        this.listWorker = listWorker;
        mAuth = new AuthenticationProvider();
        mLikeProvider = new LikeWorkersDatabaseProvider(mAuth.getIdCurrentUser());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_worker_main, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User model = listWorker.get(position);
        new JobsDatabaseProvider().getValuationsFromUser(model.getId()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                float totalAmountValuation = 0;
                for (DocumentSnapshot it : queryDocumentSnapshots.getDocuments()) {
                    totalAmountValuation += it.toObject(Job.class).getValuation().getAverageTotal();
                }
                if (totalAmountValuation > 0) {
                    holder.binding.valuated.setRating(totalAmountValuation / queryDocumentSnapshots.getDocuments().size());
                }
            }
        }).addOnFailureListener(v -> Log.e(TAG, "loadUserData onSuccess: addOnFailureListener"));

        checkIfExistLike(holder.binding.likeWorker, model.getId());

        holder.binding.likeWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.binding.likeWorker.getTag().equals(true)) {
                    //if exist like then we remove it
                    holder.binding.likeWorker.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.unLikedWorker)));
                    holder.binding.likeWorker.setTag(false);
                } else {
                    holder.binding.likeWorker.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.likedWorker)));
                    holder.binding.likeWorker.setTag(true);
                }
                mLikeProvider.doLike(model.getId());
            }
        });
        holder.binding.getRoot().setOnClickListener(view -> {
            Intent i = new Intent(context, ProfileDetailsActivity.class);
            i.putExtra("idUserToSee", model.getId());
            context.startActivity(i);

        });


        holder.binding.lastNameWorker.setText(Utils.capitalizeString(model.getLastName()));
        holder.binding.nameWorker.setText(Utils.capitalizeString(model.getName()));
        holder.binding.aboutWorker.setText(model.getAbout());
        Glide.with(context).load(model.getProfileImage()).placeholder(R.drawable.loading_).thumbnail(Glide.with(context).load(R.drawable.loading_)).error(R.drawable.ic_error_404).centerInside().into(holder.binding.imageWorker);
    }

    @Override
    public int getItemCount() {
        return listWorker.size();
    }

    private void checkIfExistLike(ImageView btnLikeWorker, String id) {
        mLikeProvider.chekIfExistLikeToWorker(id).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    btnLikeWorker.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.likedWorker)));
                    btnLikeWorker.setTag(true);
                } else {
                    btnLikeWorker.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.unLikedWorker)));
                    btnLikeWorker.setTag(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Fallo al comprobar si existe like", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardviewWorkerMainBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardviewWorkerMainBinding.bind(itemView);
        }

    }

}
