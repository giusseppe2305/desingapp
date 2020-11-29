package com.optic.projectofinal.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.LikeWorkersDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

public class WorkersAdapterFirebase extends FirestoreRecyclerAdapter<User, WorkersAdapterFirebase.ViewHolder> {

    private AuthenticationProvider mAuth;
    private final Context context;
    private LikeWorkersDatabaseProvider mLikeProvider;

    public WorkersAdapterFirebase(Context c, @NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
        context = c;
        mAuth = new AuthenticationProvider();
        mLikeProvider=new LikeWorkersDatabaseProvider(mAuth.getIdCurrentUser());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_worker_main, parent, false);

        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull User model) {
        if(mAuth.getIdCurrentUser()==model.getId()){
            holder.view.setVisibility(View.GONE);
        }
        checkIfExistLike(holder.btnLikeWorker,model.getId());
        
        holder.btnLikeWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btnLikeWorker.getTag().equals(true)){
                    //if exist like then we remove it
                    holder.btnLikeWorker.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.unLikedWorker)));
                    holder.btnLikeWorker.setTag(false);
                }else{
                    holder.btnLikeWorker.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.likedWorker)));
                    holder.btnLikeWorker.setTag(true);
                }
                mLikeProvider.doLike(model.getId());
            }
        });

        holder.lastName.setText(Utils.capitalizeString(model.getLastName()));
        holder.name.setText(Utils.capitalizeString(model.getName()));
        holder.aboutWorker.setText(model.getAbout());
        Glide.with(context).load(model.getProfileImage()).placeholder(R.drawable.loading_).thumbnail(Glide.with(context).load(R.drawable.loading_)).error(R.drawable.ic_error_404).centerInside().into(holder.imageProfile);
    }

    private void checkIfExistLike(ImageView btnLikeWorker, String id) {
        mLikeProvider.chekIfExistLikeToWorker(id).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    btnLikeWorker.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.likedWorker)));
                    btnLikeWorker.setTag(true);
                }else{
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
        private ImageView imageProfile;
        private TextView name,lastName,aboutWorker;
        private View view;
        private ImageView btnLikeWorker;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile =itemView.findViewById(R.id.imageWorker);
            name=itemView.findViewById(R.id.nameWorker);
            aboutWorker=itemView.findViewById(R.id.aboutWorker);
            lastName=itemView.findViewById(R.id.lastNameWorker);
            btnLikeWorker =itemView.findViewById(R.id.likeWorker);
            view=itemView;
        }

    }

}
