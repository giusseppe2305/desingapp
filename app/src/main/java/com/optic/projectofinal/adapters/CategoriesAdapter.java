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
import com.optic.projectofinal.UI.activities.CategorySelectedActivity;
import com.optic.projectofinal.models.Category;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    Context context;
    List<Category> myCategories;
    public CategoriesAdapter(Context c,List<Category> myCategories) {
        context=c;
        this.myCategories=myCategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(context).inflate(R.layout.cardview_categories_main,parent,false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {
        final Category iteratedCategory=myCategories.get(position);
        holder.tvTitle.setText(iteratedCategory.getTitle());
        holder.ivImage.setImageResource(iteratedCategory.getIdImage());
        holder.cardViewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(context, CategorySelectedActivity.class);
                i.putExtra("category",iteratedCategory.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivImage;
        CardView cardViewParent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            ivImage=itemView.findViewById(R.id.ivImage);
            cardViewParent=itemView.findViewById(R.id.carViewCategorie);
        }
    }
}
