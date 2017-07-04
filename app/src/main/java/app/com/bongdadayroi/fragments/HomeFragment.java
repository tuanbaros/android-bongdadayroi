package app.com.bongdadayroi.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.activities.AdsActivity;
import app.com.bongdadayroi.activities.EXOMediaActivity;
import app.com.bongdadayroi.adapters.HomeAdapter;
import app.com.bongdadayroi.models.MyData;
import app.com.bongdadayroi.models.MyVideo;
import app.com.bongdadayroi.myapp.ScreenSize;
import app.com.bongdadayroi.views.MyImage;

/**
 * Created by tuan on 24/03/2016.
 */
public class HomeFragment extends Fragment {
    private int count = 0;
    private TextView textView;
    private MyData myData;
    private MyVideo myVideo;
    private MyImage imageView;
    private Animation fadeIn, fadeOut, slide;
    private Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_layout, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_home, listView,
            false);
        myData = MyData.getInstance();
        myVideo = myData.getArrayHome().get(0);
        textView = (TextView) header.findViewById(R.id.tvTitleFirst);
        textView.setText(myVideo.getTitle());
        textView.setLines(2);
        imageView = (MyImage) header.findViewById(R.id.ivHomeFirst);
        imageView.setMyWidth(ScreenSize.WIDTH);
        imageView.setMyHeight(ScreenSize.WIDTH * 9 / 16);
        imageView.setSize();
        imageView.setDefaultImageResId(R.drawable.no_image);
        imageView.setErrorImageResId(R.drawable.no_image);
        imageView.setImageUrl(myVideo.getAvatar_medium());
        listView.addHeaderView(header);
        HomeAdapter homeAdapter = new HomeAdapter(getContext());
        listView.setAdapter(homeAdapter);
        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getHeaderImageView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setImageBitmap(bitmap);
                textView.setText(myVideo.getTitle());
                imageView.startAnimation(fadeIn);
                textView.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        getHeaderImageView();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EXOMediaActivity.class);
                intent.putExtra("video", myData.getArrayHome().get(count));
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void getHeaderImageView() {
        if (count + 1 == myData.getArrayHome().size()) {
            count = 0;
        } else {
            count = count + 1;
        }
        myVideo = myData.getArrayHome().get(count);
        AndroidNetworking.get(myVideo.getAvatar_medium())
            .setTag("imageRequestTag")
            .setPriority(Priority.MEDIUM)
            .setBitmapMaxHeight(ScreenSize.WIDTH * 9 / 16)
            .setBitmapMaxWidth(ScreenSize.WIDTH)
            .setBitmapConfig(Bitmap.Config.ARGB_8888)
            .build()
            .getAsBitmap(new BitmapRequestListener() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    HomeFragment.this.bitmap = bitmap;
                    imageView.startAnimation(fadeOut);
                    textView.startAnimation(fadeOut);
                }

                @Override
                public void onError(ANError error) {
                    // handle error
                }
            });
    }
}
