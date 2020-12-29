package com.optic.projectofinal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.CategorySelectedActivity;
import com.optic.projectofinal.models.SubCategory;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;

public class SubCategoriesAdapterFirebase extends RecyclerView.Adapter<SubCategoriesAdapterFirebase.ViewHolder> {
    private CategorySelectedActivity context;
    private ArrayList<SubCategory>listSubcatories;
    public SubCategoriesAdapterFirebase(CategorySelectedActivity c, ArrayList<SubCategory> listSubcatories ) {
        context=c;
        this.listSubcatories=listSubcatories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(context).inflate(R.layout.cardview_job_offered,parent,false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoriesAdapterFirebase.ViewHolder holder, int position) {
        SubCategory iterated=listSubcatories.get(position);
        holder.nameSubcategory.setText(Utils.capitalizeString(iterated.getName()));
    }

    @Override
    public int getItemCount() {
        return listSubcatories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameSubcategory,sizeSubcategory;
        private View parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent=itemView;
        }
    }
    public ArrayList<SubCategory> getList(){
        return listSubcatories;
    }
}
