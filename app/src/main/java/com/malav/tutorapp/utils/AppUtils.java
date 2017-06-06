package com.malav.tutorapp.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.malav.tutorapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Random;

/**
 * Created by shahmalav on 31/07/16.
 */

public class AppUtils {

    private static String TAG = "Utility";

    /**
     * This method is used to set the height of listView element
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        //I added this to try to fix half hidden row
        totalHeight++;

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * generates an alphanumeric string based on specified length.
     * @param length # of characters to generate
     * @return random string
     */
    public static String generateRandomString(int length) {

        Random random = new Random((new Date()).getTime());

        char[] values = {'a','b','c','d','e','f','g','h','i','j',
                'k','l','m','n','o','p','q','r','s','t',
                'u','v','w','x','y','z','0','1','2','3',
                '4','5','6','7','8','9'};
        String out = "";
        for (int i=0;i<length;i++) {
            int idx=random.nextInt(values.length);
            out += values[idx];
        }
        return out;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            Log.d("GetBitmapFromUrl", "getBitmapFromURL: "+url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            Log.e("GetBitmapFromUrl", "getBitmapFromURL: "+ src, e );
            return null;
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static StringBuilder formatDate(int arg1, int arg2, int arg3){
        String arg2String = Integer.toString(arg2+1), arg3String = Integer.toString(arg3);
        if ((arg2 + 1) <= 9) {
            arg2String = "0" + (arg2 + 1);
        }
        if ((arg3) <= 9) {
            arg3String = "0" + arg3;
        }
        return new StringBuilder().append(arg1).append("-").append(arg2String).append("-").append(arg3String);
    }

    public static String formatTime(int hourOfDay, int minute){
        String hourString = "";
        String minuteString = "";

        if(hourOfDay<10){
            hourString = "0" + hourOfDay;
        }else{
            hourString = Integer.toString(hourOfDay);
        }

        if(minute<10){
            minuteString = "0" + minute;
        }else{
            minuteString = Integer.toString(minute);
        }

        return hourString + ":" + minuteString + ":00";
    }

    public static String getPathVideo(Uri uri, Context context) {

        String path="";
        String document_id="";
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if(cursor!=null) {
            cursor.moveToFirst();
            document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();
        }

        cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);

        if(cursor!=null){
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            cursor.close();
        }

        return path;
    }

    //method to get the file path from uri
    public static String getPath(Uri uri, Context context) {
        String path="";
        String document_id="";
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if(cursor!=null) {
            cursor.moveToFirst();
            document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();
        }

        cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);

        if(cursor!=null){
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }

        return path;
    }

    public static void fullScreenImage(Activity activity, View v, String imgUrl){

        Log.d(TAG, "fullScreenImage: imgUrl: " + imgUrl);
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View inflatedView = layoutInflater.inflate(R.layout.activity_fullscreen_image, null,false);
        ImageView imageView = (ImageView) inflatedView.findViewById(R.id.imgDisplay);
        Button close = (Button) inflatedView.findViewById(R.id.btnClose);

        //Load image via Glide
        Glide.clear(imageView);
        if(CommonUtils.isNotNull(imgUrl)){
            Glide.with(activity).load(imgUrl).crossFade(500).into(imageView);
        }

        // get device size
        Display display = activity.getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        final PopupWindow popWindow = new PopupWindow(inflatedView, width, height-50, true );
       // popWindow.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.fb_popup_bg));
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        popWindow.setAnimationStyle(R.style.PopupAnimation);
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0,100);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });
    }

    public static int getStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return activity.getWindow().getStatusBarColor();
        }
        return 0;
    }

    public static void setStatusBarColor(int color, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

    public static void sendPasswordEmail(String toEmailId, String password, Context context){
        BackgroundMail.newBuilder(context)
                .withUsername("noreply.holistree@gmail.com")
                .withPassword("malavjaini")
                .withMailto(toEmailId)
                .withSubject("Your Password")
                .withBody("Your password for HolisTree app is " + password + ". Change the password once you login.")
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                    }
                })
                .send();
    }

    public static final int[] COLORS = {
            Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120),
            Color.rgb(106, 167, 134), Color.rgb(53, 194, 209), Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
            Color.rgb(118, 174, 175), Color.rgb(42, 109, 130), Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134), Color.rgb(179, 48, 80), Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53), Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
            Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)
    };

}
