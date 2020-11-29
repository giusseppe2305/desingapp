package com.optic.projectofinal.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.CreateJobActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.optic.projectofinal.utils.Utils.MAX_IMAGE_CAN_BE_SELECTED;

public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ViewHolder> {

    private CreateJobActivity context;
    private ArrayList<Uri>uris;
    public ImagePickerAdapter(CreateJobActivity c, ArrayList<Uri> uris) {
        context=c;
        this.uris=uris;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista;
        if(viewType==R.layout.card_view_image_selected){
            vista=LayoutInflater.from(context).inflate(R.layout.card_view_image_selected,parent,false);;
        }else{
            vista=LayoutInflater.from(context).inflate(R.layout.card_view_image_selected_footer,parent,false);;

        }
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePickerAdapter.ViewHolder holder, int position) {
        if(position==uris.size()){
            //if enter is footer layout
            if(uris.size()==MAX_IMAGE_CAN_BE_SELECTED){
                holder.imageFooter.setVisibility(View.GONE);

            }else{
                holder.imageFooter.setVisibility(View.VISIBLE);
            }
            holder.imageFooter.setOnClickListener(v->context.getDialogPhoto().show());

        }else{
            //if enter here them is normal view
            Uri imageIterated=uris.get(position);
            Glide.with(context).load(imageIterated).placeholder(R.drawable.loading_).thumbnail(Glide.with(context).load(R.drawable.loading_)).error(R.drawable.ic_error_404).centerCrop().into(holder.imageSelected);
            holder.deleteImage.setOnClickListener(view -> {
                uris.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,uris.size());
                context.checkCountImagesSelected();
            });
        }

    }

    @Override
    public int getItemCount() {
        return uris.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position==uris.size())?R.layout.card_view_image_selected_footer:R.layout.card_view_image_selected;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageSelected;
        private View imageFooter;
        private CircleImageView deleteImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFooter=itemView.findViewById(R.id.addPhoto);
            imageSelected=itemView.findViewById(R.id.imageSelected);
            deleteImage=itemView.findViewById(R.id.deleteImage);
        }
    }
}
