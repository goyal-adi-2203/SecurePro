package com.example.securepro.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.securepro.BuildConfig;
import com.example.securepro.R;

public class GlideClientUtil {
    private static String baseUrl = BuildConfig.BASE_URL;
//    private static String baseUrl = BuildConfig.BASE_URL3;
    private static Glide glide = null;

    public static void getGlide(Context context, String path, ImageView view){
        Glide.with(context)
                .load(baseUrl + path)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.default_pro_pic)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                )
                .into(view);
    }
}
