package com.example.securepro.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.securepro.BuildConfig;
import com.example.securepro.R;

public class GlideClientUtil {
    private static String baseUrl = BuildConfig.BASE_URL2;
    private static Glide glide = null;

    public static void getGlide(Context context, String path, ImageView view){
        Glide.with(context)
                .load(baseUrl + path)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.default_pro_pic)
                )
                .into(view);
    }
}
