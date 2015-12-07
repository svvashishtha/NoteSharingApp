package com.app.notes.serverApi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.app.notes.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Random;

/**
 * Request Manager Class for Managing the Network Requests using the Volley
 * Library
 *
 * @author Ankur Parashar
 */
public class RequestManager {

    private static RequestManager mRequestManager;

    /**
     * Queue which Manages the Network Requests :-)
     */
    private static RequestQueue mRequestQueue;

    // ImageLoader Instance
    // private static ImageLoader mImageLoader;

    private static final ColorDrawable transparentDrawable = new ColorDrawable(
            android.R.color.transparent);

    private RequestManager() {

    }

    public static RequestManager get(Context context) {

        if (mRequestManager == null)
            mRequestManager = new RequestManager();

        return mRequestManager;
    }

    /**
     * @param context application context
     */
    public static RequestQueue getInstance(Context context) {

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);

        }

        return mRequestQueue;

    }

    public void cancelAll(boolean isCancel, String tag) {
        if (isCancel)
            mRequestQueue.cancelAll(tag);
    }

	/*
     * public static ImageLoader getImageLoader() {
	 * 
	 * if (mImageLoader == null) mImageLoader = new ImageLoader(mRequestQueue,
	 * new ImageCache() {
	 * 
	 * private final LruCache<String, Bitmap> mCache = new LruCache<String,
	 * Bitmap>( 10);
	 * 
	 * public void putBitmap(String url, Bitmap bitmap) { mCache.put(url,
	 * bitmap);
	 * 
	 * }
	 * 
	 * public Bitmap getBitmap(String url) { return mCache.get(url); }
	 * 
	 * });
	 * 
	 * return mImageLoader;
	 * 
	 * }
	 */

    /**
     * This class is used to request profile pictures for different users
     *
     * @param context
     * @param imageView
     * @param imgUrl
     * @param
     */
    public void requestImage(final Context context, final ImageView imageView,
                             final String imgUrl) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true).cacheInMemory(true)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)

                .bitmapConfig(Bitmap.Config.ARGB_8888).build();
        //to show placeholder image

        imageView.setImageResource(R.drawable.ic_photo_black);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage(imgUrl, imageView, options,
                new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                        imageView.setBackgroundColor(0);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);
                    }
                });

    }

    private int getRandomNumber() {
        Random r = new Random();
        int i = r.nextInt(5 - 0 + 1) + 0;
        return i;
    }

    /**
     * Sets a {@link Bitmap} to an
     * {@link ImageView} using a fade-in animation. If there is a
     * {@link android.graphics.drawable.Drawable} already set on the ImageView
     * then use that as the image to fade from. Otherwise fade in from a
     * transparent Drawable.
     */
    /*
	 * public static void setImageBitmap(final ImageView imageView, final Bitmap
	 * bitmap, Resources resources, boolean fadeIn) {
	 * 
	 * // If we're fading in and on HC MR1+ if (fadeIn &&
	 * FinoitKit.isHoneycomb()) { // Use ViewPropertyAnimator to run a simple
	 * fade in + fade out // animation to update the // ImageView imageView
	 * .animate() .scaleY(0.95f) .scaleX(0.95f) .alpha(0f) .setDuration(
	 * imageView.getDrawable() == null ? 0 :
	 * AppAnimationUtils.ANIMATION_FADE_IN_TIME / 2) .setListener(new
	 * AnimatorListenerAdapter() {
	 * 
	 * @Override public void onAnimationEnd(Animator animation) {
	 * 
	 * if (imageView instanceof ProfileImage) {
	 * 
	 * ((ProfileImage) imageView).maskImage(bitmap, R.drawable.ic_img_box2);
	 * 
	 * } else { imageView.setImageBitmap(bitmap); }
	 * 
	 * imageView .animate() .alpha(1f) .scaleY(1f) .scaleX(1f) .setDuration(
	 * AppAnimationUtils.ANIMATION_FADE_IN_TIME / 2) .setListener(null); }
	 * 
	 * }); } else if (fadeIn) { // Otherwise use a TransitionDrawable to fade in
	 * Drawable initialDrawable = null; if (imageView.getDrawable() != null) {
	 * initialDrawable = imageView.getDrawable(); } else { initialDrawable =
	 * transparentDrawable; } BitmapDrawable bitmapDrawable = new
	 * BitmapDrawable(resources, bitmap); // Use TransitionDrawable to fade in
	 * final TransitionDrawable td = new TransitionDrawable( new Drawable[] {
	 * initialDrawable, bitmapDrawable }); imageView.setImageDrawable(td);
	 * td.startTransition(AppAnimationUtils.ANIMATION_FADE_IN_TIME);
	 * 
	 * } else { // No fade in, just set bitmap directly
	 * imageView.setImageBitmap(bitmap); }
	 * 
	 * }
	 */
    public void requestImage1(final Context context, final ImageView imageView,
                              final String imgUrl, final RequestBitmap requestBitmap, int position) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true).cacheInMemory(true)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)

                .bitmapConfig(Bitmap.Config.ARGB_8888).build();
		/*if (position == -1) {
			imageView.setBackgroundColor(context.getResources().getColor(
					ZUIUtils.getColorValue(getRandomNumber())));
		} else {
			imageView.setBackgroundColor(context.getResources().getColor(
					ZUIUtils.getColorValue(position)));
		}*/
        ImageLoader.getInstance().displayImage(imgUrl, imageView, options,
                new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                        if (requestBitmap != null) {
                            requestBitmap.onRequestCompleted(bitmap);
                            imageView.setBackgroundColor(0);
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);
                    }
                });
    }

    public void requestImage2(final Context context, final ImageView imageView,
                              final String imgUrl, final RequestBitmap requestBitmap, int position) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true).cacheInMemory(true)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)

                .bitmapConfig(Bitmap.Config.ARGB_8888).build();
		/*imageView.setBackgroundColor(context.getResources().getColor(
				ZUIUtils.getColorValue(position)));*/
        ImageLoader.getInstance().displayImage(imgUrl, imageView, options,
                new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                        imageView.setBackgroundColor(0);
                        requestBitmap.onRequestCompleted(bitmap);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);
                    }
                });
    }

    public interface RequestBitmap {
        void onRequestCompleted(Bitmap bitmap);
    }
}
