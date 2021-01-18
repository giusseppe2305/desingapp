package com.optic.projectofinal.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.ImageSliderLayoutItemBinding;
import com.optic.projectofinal.models.SliderItem;
import com.optic.projectofinal.utils.Utils;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.List;

public class SliderAdapterExample extends
        SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<SliderItem> mSliderItems;

    public SliderAdapterExample(Context context,List<SliderItem> images) {
        this.context = context;
        mSliderItems=images;
    }

    public void renewItems(List<SliderItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();

    }

    public void addItem(SliderItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();

    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        // was only null 2 parameters
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, parent,false);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        SliderItem sliderItem = mSliderItems.get(position);

        viewHolder.binding.tvAutoImageSlider.setText(sliderItem.getDescription());
        viewHolder.binding.tvAutoImageSlider.setTextSize(16);
        viewHolder.binding.tvAutoImageSlider.setTextColor(Color.WHITE);

        Glide.with(context).load(sliderItem.getImage()).apply(Utils.getOptionsGlide(false))
                .into(viewHolder.binding.ivAutoImageSlider);
        viewHolder.itemView.setOnClickListener(view ->
                new StfalconImageViewer.Builder<>(context, mSliderItems.toArray(new SliderItem[]{}),
                            (imageView, image) ->
                                    Glide.with(context).load(image.getImage()).apply(Utils.getOptionsGlide(false))
                                            .into(imageView))
                        .withStartPosition(mSliderItems.indexOf(sliderItem)).withHiddenStatusBar(false).show());
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        private ImageSliderLayoutItemBinding binding;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            binding=ImageSliderLayoutItemBinding.bind(itemView);
        }
    }

}