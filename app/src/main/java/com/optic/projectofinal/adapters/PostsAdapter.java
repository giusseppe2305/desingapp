package com.optic.projectofinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.optic.projectofinal.R;

import java.util.ArrayList;
import java.util.Arrays;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    Context context;
    ArrayList<Integer>numeros;
    public PostsAdapter(Context c) {
        context=c;
        numeros=new ArrayList<>();
        numeros.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(context).inflate(R.layout.cardview_worker_main,parent,false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        holder.title.setText("titulo "+numeros.get(position));
        if(position%2==0){
            Glide.with(context).load(R.drawable.prueba2).placeholder(R.drawable.loading_).thumbnail(Glide.with(context).load(R.drawable.loading_)).error(R.drawable.ic_error_404).centerInside().into(holder.ivPost);
        }else{
            Glide.with(context).load(R.drawable.prueba).placeholder(R.drawable.loading_).thumbnail(Glide.with(context).load(R.drawable.loading_)).error(R.drawable.ic_error_404).centerInside().into(holder.ivPost);
        }
    }

    @Override
    public int getItemCount() {
        return numeros.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPost;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPost=itemView.findViewById(R.id.imageWorker);
            title=itemView.findViewById(R.id.nameWorker);
        }
    }
}
