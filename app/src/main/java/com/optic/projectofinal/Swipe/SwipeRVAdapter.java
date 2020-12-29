package com.optic.projectofinal.Swipe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.fragments.tabsFragments.auctions.MyAuctionsFragment;
import com.optic.projectofinal.databinding.SwipeItemContainerBinding;
import com.optic.projectofinal.models.Category;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.models.Resource;
import com.optic.projectofinal.models.Skill;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.ApplyJobWorkerDatabaseProvider;
import com.optic.projectofinal.providers.JobsDatabaseProvider;
import com.optic.projectofinal.providers.SubcategoriesDatabaseProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class SwipeRVAdapter<T> extends RecyclerView.Adapter<SwipeRVAdapter<T>.SwipeRVViewHolder> {



    private final Class<T> type;
    private ArrayList<T> list;
    private Context context;
    private MyAuctionsFragment myAuctionsFragment;

    public SwipeRVAdapter(ArrayList<T> list, Context context, Class<T> type) {
        this.list = list;
        this.context = context;
        this.type = type;
    }
    public SwipeRVAdapter(ArrayList<T> list, MyAuctionsFragment context, Class<T> type) {
        this.list = list;
        this.context = context.getContext();
        myAuctionsFragment=context;
        this.type = type;
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
     *
     * @param position
     */
    public void removeSwipeItem(int position) {
        list.remove(position);
        this.notifyItemRemoved(position);
    }

    /**
     * Add a Contact *contact* into the recyclerview at index *position*
     *
     * @param position
     */
    public void addSwipeItem(int position, T obj) {
        list.add(position, obj);
        this.notifyItemInserted(position);
    }

    @Override
    public void onBindViewHolder(@NonNull SwipeRVViewHolder swipeRVViewHolder, int i) {
        Object ibIterated = list.get(i);
        if (ibIterated instanceof Skill) {

            Skill obj = (Skill) ibIterated;

            swipeRVViewHolder.binding.skill.title.setText(obj.getTitle());
            swipeRVViewHolder.binding.skill.price.setText(Utils.getFormatPrice(obj.getPricePerHour(),context));
            ///category
            Category categ = Utils.getCategoryByIdJson(context, obj.getIdCategory());
            swipeRVViewHolder.binding.skill.category.setText(categ.getTitleString());
            swipeRVViewHolder.binding.skill.imgSkill.setImageResource(categ.getIdImage());
            new SubcategoriesDatabaseProvider().getSubCategoryById(obj.getIdSubcategory()).addOnSuccessListener(documentSnapshot ->
            {
                if(documentSnapshot.exists()){
                    swipeRVViewHolder.binding.skill.subCategory.setText(documentSnapshot.getString("name"));
                }
            }).addOnFailureListener(e -> Log.e(TAG_LOG, "onBindViewHolder: "+e.getMessage() ));

        }
        if (ibIterated instanceof Resource) {

            Resource obj = (Resource) ibIterated;
            swipeRVViewHolder.binding.resource.titleResource.setText(obj.getTitleString());
            swipeRVViewHolder.binding.resource.imgResorce.setImageResource(obj.getIdImage());

        }
        if (ibIterated instanceof Job) {

            Job obj = (Job) ibIterated;
            swipeRVViewHolder.binding.jobOffered.title.setText(obj.getTitle());
            swipeRVViewHolder.binding.jobOffered.description.setText(obj.getDescription());
            swipeRVViewHolder.binding.jobOffered.timestamp.setText(Utils.getDateFormattedSimple(obj.getTimestamp(),context));


            Glide.with(context).load(obj.getThumbnail()).apply(Utils.getOptionsGlide(true)).transform(Utils.getTransformSquareRound()).into(swipeRVViewHolder.binding.jobOffered.imageJob);

            swipeRVViewHolder.binding.jobOffered.getRoot().setOnClickListener(v -> {
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context).setTitle(R.string.swipe_adapter_job_dialog_title)
                        .setNegativeButton(R.string.swipe_adapter_job_dialog_close, null);
                new ApplyJobWorkerDatabaseProvider().getAllById(obj.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> listApplyWorkers = queryDocumentSnapshots.getDocuments();
                        Log.e(TAG_LOG, "onSuccess: tamaño "+listApplyWorkers.size() );
                         if(listApplyWorkers.size()>0){
                             Log.e(TAG_LOG, "onSuccess: entro true" );
                            List<Task<DocumentSnapshot>> listTasks=new ArrayList<>();
                             for(DocumentSnapshot it:listApplyWorkers){
                                if(it.exists()){
                                    Task<DocumentSnapshot> task = new UserDatabaseProvider().getUser(it.getString("idWorkerApply")).addOnFailureListener(v -> Log.e(TAG_LOG, "SwipeRVAdapter getuser onSuccess: " + v.getMessage()));
                                    listTasks.add(task);
                                }
                             }
                             Tasks.whenAllComplete(listTasks).addOnSuccessListener(tasks -> {
                                 List<User> listUsersApplyThisJob=new ArrayList<>();

                                 for( Task<?> itTask:tasks){
                                     DocumentSnapshot itUser= (DocumentSnapshot) itTask.getResult();
                                     listUsersApplyThisJob.add(itUser.toObject(User.class));
                                 }
                                 dialog.setSingleChoiceItems(new ArrayAdapter<User>(context, R.layout.textbox_gender, listUsersApplyThisJob), 0, (dialogInterface, numb) ->
                                         new MaterialAlertDialogBuilder(context).setTitle(R.string.swipe_adapter_job_dialog_confirm_title)
                                         .setMessage(context.getString(R.string.swipe_adapter_job_dialog_confirm_selected)+listUsersApplyThisJob.get(numb))
                                         .setPositiveButton(R.string.swipe_adapter_job_dialog_confirm_positive_button,(dialogInterface1, i1) -> {
                                             Job updateJob=new Job();
                                             updateJob.setId(obj.getId());
                                             updateJob.setIdUserApply(listUsersApplyThisJob.get(numb).getId());
                                             updateJob.setState(Job.State.IN_PROGRESS);
                                             new JobsDatabaseProvider().updateJob(updateJob).addOnFailureListener(v1 -> Log.e(TAG_LOG, "updatejob select worker onClick: "+ v1.getMessage() ));
                                             myAuctionsFragment.loadData();
                                             dialogInterface.dismiss();
                                         })
                                         .setNegativeButton(R.string.swipe_adapter_job_dialog_cancel,(dialogInterface1, i1) -> dialogInterface1.dismiss())
                                         .show()).show();

                             });


                         }else{
                             dialog.setMessage(R.string.swipe_adapter_job_dialog_message);
                             dialog.show();
                         }
                    }
                }).addOnFailureListener(vv-> Log.e(TAG_LOG, "MyAuctionsFragment onBindViewHolder: "+vv.getMessage() ));


            });

        }

    }

    public class SwipeRVViewHolder extends RecyclerView.ViewHolder {
        private SwipeItemContainerBinding binding;

        public SwipeRVViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SwipeItemContainerBinding.bind(itemView);
            if (type.getName().equals(Resource.class.getName())) {
                binding.resource.getRoot().setVisibility(View.VISIBLE);

            } else if (type.getName().equals(Skill.class.getName())) {
                binding.skill.getRoot().setVisibility(View.VISIBLE);

            } else if (type.getName().equals(Job.class.getName())) {
                binding.jobOffered.getRoot().setVisibility(View.VISIBLE);

            }
        }

        public CardView getForegroundContainer() {
            if (type.getName().equals(Resource.class.getName())) {

                return binding.resource.getRoot();

            } else if (type.getName().equals(Skill.class.getName())) {
                return binding.skill.getRoot();

            } else if (type.getName().equals(Job.class.getName())) {
                return binding.jobOffered.getRoot();
            }
            return null;
        }


    }


}
