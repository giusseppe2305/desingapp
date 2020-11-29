package com.optic.projectofinal.Swipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.SwipeItemContainerBinding;
import com.optic.projectofinal.models.Category;
import com.optic.projectofinal.models.Resource;
import com.optic.projectofinal.models.Skill;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;

public class SwipeRVAdapter<T> extends RecyclerView.Adapter<SwipeRVAdapter<T>.SwipeRVViewHolder> {


    private final Class<T> type;
    private ArrayList<T> list;
    private Context context;
    public SwipeRVAdapter(ArrayList<T> list, Context context,Class<T> type) {
        this.list = list;
        this.context = context;
        this.type=type;
    }

    @NonNull
    @Override
    public SwipeRVViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.swipe_item_container, viewGroup, false);
        return new SwipeRVViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Remove an item from the recyclerview at index *position*
     * @param position
     */
    public void removeSwipeItem(int position){
        list.remove(position);
        this.notifyItemRemoved(position);
    }

    /**
     * Add a Contact *contact* into the recyclerview at index *position*
     * @param position
     */
    public void addSwipeItem(int position, T obj){
        list.add(position,obj);
        this.notifyItemInserted(position);
    }

    @Override
    public void onBindViewHolder(@NonNull SwipeRVViewHolder swipeRVViewHolder, int i) {
        Object ibIterated=list.get(i);
        if(ibIterated instanceof Skill){

            Skill obj= (Skill) ibIterated;
            swipeRVViewHolder.binding.skill.title.setText(obj.getTitle());
            swipeRVViewHolder.binding.skill.price.setText(String.valueOf(obj.getPricePerHour()));
            ///category
            Category categ=Utils.getCategoryByIdJson(context,obj.getIdCategory());
            swipeRVViewHolder.binding.skill.category.setText(categ.getTitleString());
            swipeRVViewHolder.binding.skill.imgSkill.setImageResource(categ.getIdImage());
            //Glide.with(context).load(categ.getIdImage()).into(swipeRVViewHolder.binding.skill.imgSkill);
            swipeRVViewHolder.binding.skill.subCategory.setText(obj.getIdSubcategory());

        }
        if(ibIterated instanceof Resource){

            Resource obj= (Resource) ibIterated;
            swipeRVViewHolder.binding.resource.titleResource.setText(obj.getTitleString());
            swipeRVViewHolder.binding.resource.imgResorce.setImageResource(obj.getIdImage());

        }

    }
    public class SwipeRVViewHolder extends RecyclerView.ViewHolder {
        private SwipeItemContainerBinding binding;

        public SwipeRVViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SwipeItemContainerBinding.bind(itemView);
            if(type.getName().equals(Resource.class.getName())){
              binding.resource.getRoot().setVisibility(View.VISIBLE);

            }else if(type.getName().equals(Skill.class.getName())){
                binding.skill.getRoot().setVisibility(View.VISIBLE);

            }
        }
        public CardView getForegroundContainer() {
            if(type.getName().equals(Resource.class.getName())){

                return binding.resource.getRoot();

            }else if(type.getName().equals(Skill.class.getName())){
                return binding.skill.getRoot();

            }
            return null;
        }


    }


}
