package com.optic.projectofinal.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.ProfileDetailsActivity;
import com.optic.projectofinal.databinding.CardviewWorkerMainBinding;
import com.optic.projectofinal.modelsRetrofit.WorkerQueryModel;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.LikeWorkersDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.util.List;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class WorkersqueryAdapter extends RecyclerView.Adapter<WorkersqueryAdapter.ViewHolder> {


    private AuthenticationProvider mAuth;
    private final Context context;
    private List<WorkerQueryModel> listWorker;
    private LikeWorkersDatabaseProvider mLikeProvider;

    public WorkersqueryAdapter(Context c, List<WorkerQueryModel> listWorker) {

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
        WorkerQueryModel model = listWorker.get(position);

        checkIfExistLike(holder.binding.likeWorker, model.getId());
        holder.binding.likeWorker.setOnClickListener(view -> {
            if (holder.binding.likeWorker.getTag().equals(true)) {
                //if exist like then we remove it
                holder.binding.likeWorker.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.unLikedWorker)));
                holder.binding.likeWorker.setTag(false);
            } else {
                holder.binding.likeWorker.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.likedWorker)));
                holder.binding.likeWorker.setTag(true);
            }
            mLikeProvider.doLike(model.getId());
        });

        holder.binding.getRoot().setOnClickListener(view -> {
            Intent i = new Intent(context, ProfileDetailsActivity.class);
            i.putExtra("idUserToSee", model.getId());
            context.startActivity(i);

        });


        holder.binding.price.setText(Utils.roundToHalf(model.getPrice())+" "+Utils.getCurrency(context).getSymbol());
        holder.binding.lastNameWorker.setText(Utils.capitalizeString(model.getLastName()));
        holder.binding.nameWorker.setText(Utils.capitalizeString(model.getName()));
        holder.binding.aboutWorker.setText(model.getAbout() );

        holder.binding.valuated.setRating((float) model.getAverageValuation());


        Glide.with(context).load(model.getProfileImage()).apply(Utils.getOptionsGlide(false)).into(holder.binding.imageWorker);
    }

    @Override
    public int getItemCount() {
        return listWorker.size();
    }

    private void checkIfExistLike(ImageView btnLikeWorker, String id) {
        mLikeProvider.chekIfExistLikeToWorker(id).addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.getDocuments().size() > 0) {
                btnLikeWorker.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.likedWorker)));
                btnLikeWorker.setTag(true);
            } else {
                btnLikeWorker.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.unLikedWorker)));
                btnLikeWorker.setTag(false);
            }
        }).addOnFailureListener(e -> Log.e(TAG_LOG, "fail to get if exist like" + e.getMessage()));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardviewWorkerMainBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardviewWorkerMainBinding.bind(itemView);
        }

    }

}
