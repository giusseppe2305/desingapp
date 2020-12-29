package com.optic.projectofinal.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.SwipeItemSkillForegroundBinding;
import com.optic.projectofinal.models.Category;
import com.optic.projectofinal.models.Skill;
import com.optic.projectofinal.providers.SubcategoriesDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.util.List;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.ViewHolder> {
    private Context context;
    private List<Skill> listSkills;
    private SubcategoriesDatabaseProvider mSubCate;
    public SkillsAdapter(Context c, List<Skill> listSkills) {
        context=c;
        this.listSkills=listSkills;
        this.mSubCate=new SubcategoriesDatabaseProvider();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(context).inflate(R.layout.swipe_item_skill_foreground,parent,false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillsAdapter.ViewHolder holder, int position) {
        System.out.println("position "+position);
        Skill obj= listSkills.get(position);
        holder.binding.title.setText(obj.getTitle());


        holder.binding.price.setText(Utils.getFormatPrice(obj.getPricePerHour(),context));
        ///category
        Category categ= Utils.getCategoryByIdJson(context,obj.getIdCategory());
        holder.binding.category.setText(categ.getTitleString());
        holder.binding.imgSkill.setImageResource(categ.getIdImage());
        mSubCate.getSubCategoryById(obj.getIdSubcategory()).addOnSuccessListener(documentSnapshot ->
        {
            if(documentSnapshot.exists()){
                holder.binding.subCategory.setText(documentSnapshot.getString("name"));
            }
        }).addOnFailureListener(e -> Log.e(TAG_LOG, "onBindViewHolder: "+e.getMessage() ));

    }

    @Override
    public int getItemCount() {
        return listSkills.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeItemSkillForegroundBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SwipeItemSkillForegroundBinding.bind(itemView);
        }
    }
}
