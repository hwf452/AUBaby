package com.halong.aubaby.util.picutil1;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;



public class ItemAdapter extends BaseAdapter {
    DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    String[] imageUrls;
    Context context;

    public ItemAdapter(String[] imageUrls, Context context) {
        super();
        this.imageUrls = imageUrls;
        this.context = context;
        options = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.ic_stub)//设置图片在下载期间显示的图片
        .showImageForEmptyUri(R.drawable.ic_empty)//设置图片Uri为空或是错误的时候显示的图片
         .showImageOnFail(R.drawable.ic_error)//设置图片加载/解码过程中错误时候显示的图片
         .cacheInMemory(true)//是否緩存都內存中
         .cacheOnDisc(true)//是否緩存到sd卡上
         .considerExifParams(true)
          .build();
    }

    private class ViewHolder {
        public TextView text;
        public ImageView image;
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list_image, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText("Item " + (position + 1));
        // ImageLoader
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageUrls[position], holder.image, options, animateFirstListener);

        return convertView;
    }
}