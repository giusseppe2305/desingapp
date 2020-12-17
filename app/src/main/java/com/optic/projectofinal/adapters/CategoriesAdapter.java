package com.optic.projectofinal.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.CategorySelectedActivity;
import com.optic.projectofinal.databinding.CardviewCategoriesMainBinding;
import com.optic.projectofinal.models.Category;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private Context context;
    private List<Category> myCategories;
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
        holder.binding.tvTitle.setText(context.getResources().getString(iteratedCategory.getIdTitle()));

        holder.binding.ivImage.setImageResource(iteratedCategory.getIdImage());
        holder.binding.getRoot().setOnClickListener(view -> {
            Intent i= new Intent(context, CategorySelectedActivity.class);
            i.putExtra("category",iteratedCategory.getId());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return myCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardviewCategoriesMainBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CardviewCategoriesMainBinding.bind(itemView);

        }
    }
}
