package com.optic.projectofinal.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.ImageSliderLayoutItemBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

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
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        SliderItem sliderItem = mSliderItems.get(position);

        viewHolder.binding.tvAutoImageSlider.setText(sliderItem.getDescription());
        viewHolder.binding.tvAutoImageSlider.setTextSize(16);
        viewHolder.binding.tvAutoImageSlider.setTextColor(Color.WHITE);
        System.out.println("adapter url > "+sliderItem.getImage());

        Glide.with(context).load(sliderItem.getImage()).placeholder(R.drawable.loading_).thumbnail(Glide.with(context).load(R.drawable.loading_)).error(R.drawable.ic_error_404).into(viewHolder.binding.ivAutoImageSlider);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new StfalconImageViewer.Builder<SliderItem>(context, mSliderItems.toArray(new SliderItem[]{}), new ImageLoader<SliderItem>() {

                    @Override
                    public void loadImage(ImageView imageView, SliderItem image) {

                        Glide.with(context).load(image.getImage()).placeholder(R.drawable.loading_).thumbnail(Glide.with(context).load(R.drawable.loading_)).error(R.drawable.ic_error_404).into(imageView);
                    }
                }).withStartPosition(mSliderItems.indexOf(sliderItem)).withHiddenStatusBar(false).show();
            }
        });
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