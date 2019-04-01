package com.rtstudio.projetomeuapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rtstudio.projetomeuapp.R;
import com.rtstudio.projetomeuapp.classes.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raphael Rodrigues on 31/03/2019
 */
public class CustomListAdapter extends ArrayAdapter<Card> {

    private static final String TAG = "CustomListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView numOS;
        TextView tipoServico;
        TextView nomeCliente;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public CustomListAdapter(Context context, int resource, List<Card> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //sets up the image loader library
//        setupImageLoader();

        //get the persons information
        int numOS = getItem(position).getNumOS();
        String TipoServico = getItem(position).getTipoServico();
        String nomeCliente = getItem(position).getNomeCliente();


        try{
            //create the view result for showing the animation
            final View result;

            //ViewHolder object
            ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);

                holder= new ViewHolder();
                holder.numOS = convertView.findViewById(R.id.card_tvNumOs);
                holder.tipoServico = convertView.findViewById(R.id.card_tvTipoServico);
                holder.nomeCliente= convertView.findViewById(R.id.card_tvNomeCliente);


                result = convertView;

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
                result = convertView;
            }


            Animation animation = AnimationUtils.loadAnimation(mContext,
                    (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
            result.startAnimation(animation);
            lastPosition = position;
//
//            holder.numOS.setText(numOS);
//            holder.tipoServico.setText(TipoServico);
//            holder.nomeCliente.setText(nomeCliente);

            //create the imageloader object
//            ImageLoader imageLoader = ImageLoader.getInstance();

//            int defaultImage = mContext.getResources().getIdentifier("@drawable/image_failed",null,mContext.getPackageName());

            //create display options
//            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
//                    .cacheOnDisc(true).resetViewBeforeLoading(true)
//                    .showImageForEmptyUri(defaultImage)
//                    .showImageOnFail(defaultImage)
//                    .showImageOnLoading(defaultImage).build();
//
//            //download and display image from url
//            imageLoader.displayImage(imgUrl, holder.image, options);

            return convertView;
        }catch (IllegalArgumentException e){
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }

    }

    /**
     * Required for setting up the Universal Image loader Library
     */
//    private void setupImageLoader(){
//        // UNIVERSAL IMAGE LOADER SETUP
//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .cacheOnDisc(true).cacheInMemory(true)
//                .imageScaleType(ImageScaleType.EXACTLY)
//                .displayer(new FadeInBitmapDisplayer(300)).build();
//
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                mContext)
//                .defaultDisplayImageOptions(defaultOptions)
//                .memoryCache(new WeakMemoryCache())
//                .discCacheSize(100 * 1024 * 1024).build();
//
//        ImageLoader.getInstance().init(config);
//        // END - UNIVERSAL IMAGE LOADER SETUP
//    }
}
