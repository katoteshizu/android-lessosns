package ru.startandroid.p0001androidstudy.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import utilities.Utilities;

/**
 * Created by Work on 2/5/2016.
 */
public class BitmapUtility {

    private final static String TAG = BitmapUtility.class.getSimpleName();

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static boolean setImage(ImageView imgView, String path, int maxSize) {
        long start = System.currentTimeMillis();
        boolean result = false;
        File faceFile = new File(path);

        if (faceFile.exists()) {

            Bitmap faceBitmap;
            Bitmap convertedFace;
            faceBitmap = BitmapFactory.decodeFile(faceFile.getAbsolutePath());
            convertedFace = getResizedBitmap(faceBitmap, maxSize);
            imgView.setImageBitmap(convertedFace);
            result = true;
        }
        long end = System.currentTimeMillis();
        Utilities.logD(BitmapUtility.class.getSimpleName(), "Get_ setImage: " + (end - start));
        return result;

    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static File getImageFile(Context context, int faceID, File inputFile, File outputFile) {
        Bitmap tmpBitmap;
        Bitmap resizedBitmap;
        if (!outputFile.exists()) {
            try {
                if (faceID > 0 && inputFile == null) {
                    tmpBitmap = Glide.with(context).load(faceID).asBitmap().centerCrop().into(100, 150).get();
                } else {
                    tmpBitmap = Glide.with(context).load(inputFile).asBitmap().centerCrop().into(100, 150).get();
                }
                resizedBitmap = Bitmap.createBitmap(tmpBitmap, 0, 0, 100, 150);
                FileOutputStream fileOutput = new FileOutputStream(outputFile);
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutput);
                fileOutput.close();
            } catch (Exception e) {
                Utilities.logE(BitmapUtility.class.getSimpleName(), Log.getStackTraceString(e));
            }
        } else {
            Utilities.logW(TAG, "File already exists.");
        }
        return outputFile;
    }
}
