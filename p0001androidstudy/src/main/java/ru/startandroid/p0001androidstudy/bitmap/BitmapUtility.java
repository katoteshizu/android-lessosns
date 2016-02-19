package ru.startandroid.p0001androidstudy.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;

import utilities.Utilities;

/**
 * Created by Work on 2/5/2016.
 */
public class BitmapUtility {

    private final static String TAG = BitmapUtility.class.getSimpleName();
    private final static int mWidth = 100;
    private final static int mHeight = 150;


    public static void convertImage(Context context, int faceID, File inputFile, File outputFile) {
        Bitmap tmpBitmap;
        Bitmap resizedBitmap;
        if (!outputFile.exists()) {
            try {
                if (faceID > 0 && inputFile == null) {
                    tmpBitmap = Glide.with(context).load(faceID).asBitmap().centerCrop().into(mWidth, mHeight).get();
                } else {
                    tmpBitmap = Glide.with(context).load(inputFile).asBitmap().centerCrop().into(mWidth, mHeight).get();
                }
                resizedBitmap = Bitmap.createBitmap(tmpBitmap, 0, 0, mWidth, mHeight);
                FileOutputStream fileOutput = new FileOutputStream(outputFile);
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutput);
                fileOutput.close();
            } catch (Exception e) {
                Utilities.logE(BitmapUtility.class.getSimpleName(), Log.getStackTraceString(e));
            }
        } else {
            Utilities.logW(TAG, "File already exists.");
        }
    }
}
