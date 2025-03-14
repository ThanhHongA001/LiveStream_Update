package com.example.livestream_update.Ringme.Helper;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

import com.vtm.ringme.utils.Log;

import java.io.InputStream;

public class MessageHelper {
    //    private static final String formatTimePattern = "dd/MM/yyyy HH:mm";
    //    private static final SimpleDateFormat spf = new SimpleDateFormat(formatTimePattern);
    private final static String TAG = "MessageHelper";

    /**
     * @param fileName
     * @return
     * @author ThaoDV
     */
    public static String getFileType(String fileName) {
        String fileType = "";
        try {
            for (int i = fileName.length() - 1; i >= 0; i--) {
                if (fileName.charAt(i) == '.') {
                    break;
                }
                fileType = fileName.charAt(i) + fileType;
            }
        } catch (Exception e) {
            Log.e(TAG, "getFileType", e);
        }
        return fileType.toLowerCase();
    }


    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
     * object when decoding bitmaps using the decode* methods from
     * {@link BitmapFactory}. This implementation calculates the closest
     * inSampleSize that will result in the final decoded bitmap having a width
     * and height equal to or larger than the requested width and height. This
     * implementation does not ensure a power of 2 is returned for inSampleSize
     * which can be faster when decoding but results in a larger bitmap which
     * isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run
     *                  through a decode* method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (reqWidth <= 0 || reqHeight <= 0) {
            return inSampleSize;
        }
        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee a final image
            // with both dimensions larger than or equal to the requested height
            // and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }


    public static Bitmap decodeEmoticonIcon(InputStream inputStream,
                                            int reqWidth) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // SkImageDecoder::Factory returned null -> change from true to false
        BitmapFactory.decodeStream(inputStream, null, options);
        options.inPreferredConfig = Config.RGB_565;
        try {
            inputStream.reset();
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqWidth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }




}