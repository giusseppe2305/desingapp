package com.optic.projectofinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.CardviewResourceItemBinding;
import com.optic.projectofinal.models.Resource;
import com.optic.projectofinal.utils.Utils;

import java.util.List;

public class ResourcesAdapter extends RecyclerView.Adapter<ResourcesAdapter.ViewHolder> {
    private Context context;
    private List<Resource> listResources;
    public ResourcesAdapter(Context c, List<Resource> listResources) {
        context=c;
        this.listResources=listResources;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(context).inflate(R.layout.cardview_resource_item,parent,false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourcesAdapter.ViewHolder holder, int position) {
        Resource it=listResources.get(position);
        Glide.with(context).load(it.getIdImage()).apply(Utils.getOptionsGlide(true)).into(holder.binding.image);
        holder.binding.image.setOnClickListener(view -> Toast.makeText(context, it.getTitleString(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return listResources.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardviewResourceItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CardviewResourceItemBinding.bind(itemView);
        }
    }
}
