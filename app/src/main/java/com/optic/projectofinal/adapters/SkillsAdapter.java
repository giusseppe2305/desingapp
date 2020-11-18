package com.optic.projectofinal.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.optic.projectofinal.R;
import com.optic.projectofinal.activites.CategorySelectedActivity;
import com.optic.projectofinal.activites.JobDoneActivity;
import com.optic.projectofinal.models.Category;

import java.util.List;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.ViewHolder> {
    Context context;
    List<Integer> myCategories;
    public SkillsAdapter(Context c, List<Integer> myCategories) {
        context=c;
        this.myCategories=myCategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(context).inflate(R.layout.cardview_skills,parent,false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillsAdapter.ViewHolder holder, int position) {
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, JobDoneActivity.class));

            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent=itemView;
        }
    }
}
