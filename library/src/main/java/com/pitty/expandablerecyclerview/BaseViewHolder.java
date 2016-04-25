package com.pitty.expandablerecyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class BaseViewHolder<T> extends ViewHolder {
    /**
     * Used to hold the findViewById loading the view
     * 用于保存findViewById加载过的view
     */
    private final SparseArray<View> views;
    public T item;

    /**
     * This method is the only entry point to get a BaseViewHolder.
     *
     * @param parent      The parent arg passed to the getView() method.
     * @param layoutId    na
     * @return A BaseViewHolder instance.
     */
    public static BaseViewHolder get(@NonNull ViewGroup parent, @LayoutRes int layoutId) {
        return new BaseViewHolder(parent, layoutId);
    }

    public static BaseViewHolder get(Context context, ViewGroup parent, @LayoutRes int layoutId) {
        return new BaseViewHolder(context, parent, layoutId);
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
    }

    public BaseViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutId) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    public BaseViewHolder(Context context, ViewGroup parent, @LayoutRes int layoutId) {
        this(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    /**
     * Retrieves the last converted object on this view.
     *
     * @return na
     */
    public T getItem() {
        return item;
    }

    /**
     * Should be called during convert
     *
     * @param item na
     */
    public void setItem(T item) {
        this.item = item;
    }

    /**
     * Retrieve the convertView
     *
     * @return na
     */
    public View getView() {
        return itemView;
    }

    /**
     * This method allows you to retrieve a view and perform custom operations
     * on it, not covered by the BaseViewHolder.
     * If you think it's a common use case, please consider creating a new issue
     * at https://github.com/JoanZapata/base-adapter-helper/issues.
     *
     * @param viewId The id of the view you want to retrieve.
     * @param <V>    na
     * @return na
     */
    public <V extends View> V getView(@IdRes int viewId) {
        return findViewById(viewId);
    }

    /**
     * Due to the findViewById performance too low
     * The findViewById view will be cached, provide the findViewById next time the same view
     * ViewHolder model for the View
     * 由于findViewById性能过低
     * findViewById过的view会被缓存下来，以供下次find相同view的时候
     * ViewHolder模式 查找子View
     *
     * @param viewId viewId
     * @param <V> V
     * @return V
     */
    @SuppressWarnings("unchecked")
    public <V extends View> V findViewById(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (V) view;
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setText(@IdRes int viewId, String value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setText(@IdRes int viewId, @StringRes int value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = findViewById(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId       The view id.
     * @param textColorRes The text color resource id.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setTextColorRes(Context context, @IdRes int viewId, @ColorRes int textColorRes) {
        TextView view = findViewById(viewId);
        view.setTextColor(ContextCompat.getColor(context, textColorRes));
        return this;
    }

    /**
     * Add links into a TextView.
     *
     * @param viewId The id of the TextView to linkify.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setTextLinkify(@IdRes int viewId) {
        TextView view = findViewById(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     *
     * @param viewId   na
     * @param typeface na
     * @return na
     */
    public BaseViewHolder setTextTypeface(@IdRes int viewId, Typeface typeface) {
        TextView view = findViewById(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel
     * rendering.
     *
     * @param typeface na
     * @param viewIds  na
     * @return na
     */
    public BaseViewHolder setTextTypeface(Typeface typeface, @IdRes int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = findViewById(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = findViewById(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = findViewById(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = findViewById(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = findViewById(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * Will download an image from a URL and put it in an ImageView.
     * It uses Square's Picasso library to download the image asynchronously and
     * put the result into the ImageView.
     * Picasso manages recycling of views in a ListView.
     * If you need more control over the Picasso settings, use
     * {BaseViewHolder#setImageBuilder}.
     *
     * @param viewId   The view id.
     * @param imageUrl The image URL.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setImageUrl(Context context, @IdRes int viewId, String imageUrl) {
        ImageView view = findViewById(viewId);
        Picasso.with(context).load(imageUrl).into(view);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     *
     * @return
     */
    public BaseViewHolder setImageUrl(Context context, @IdRes int viewId, String url,
                                               @DrawableRes int defaultImageId) {
        ImageView view = findViewById(viewId);
        Picasso.with(context).load(url).placeholder(defaultImageId).into(view);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     *
     * @return
     */
    public BaseViewHolder setImageUrl(Context context, @IdRes int viewId, String url,
                                               Drawable defaultImage) {
        ImageView view = findViewById(viewId);
        Picasso.with(context).load(url).placeholder(defaultImage).into(view);
        return this;
    }

    /**
     * Will download an image from a URL and put it in an ImageView.<br/>
     *
     * @param viewId         The view id.
     * @param requestBuilder The Picasso request builder. (e.g.
     *                       Picasso.with(context).load(imageUrl))
     * @return The BaseViewHolder for chaining.
     * public BaseViewHolder setImageBuilder(int viewId,
     *                                      RequestCreator requestBuilder) {
     *    ImageView view = findViewById(viewId);
     *    requestBuilder.into(view);
     *    return this;
     * }
     */
    public BaseViewHolder setImageBuilder(@IdRes int viewId, RequestCreator requestBuilder) {
        ImageView view = findViewById(viewId);
        requestBuilder.into(view);
        return this;
    }

    /**
     * Add an action to set the image of an image view. Can be called multiple
     * times.
     *
     * @param viewId na
     * @param bitmap na
     * @return na
     */
    public BaseViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = findViewById(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     *
     * @param viewId na
     * @param value  na
     * @return na
     */
    public BaseViewHolder setAlpha(@IdRes int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            findViewById(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            findViewById(viewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for GONE.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setVisible(@IdRes int viewId, boolean visible) {
        View view = findViewById(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * Sets the progress of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setProgress(@IdRes int viewId, int progress) {
        ProgressBar view = findViewById(viewId);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the progress and max of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @param max      The max value of a ProgressBar.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setProgress(@IdRes int viewId, int progress, int max) {
        ProgressBar view = findViewById(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     *
     * @param viewId The view id.
     * @param max    The max value of a ProgressBar.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setMax(@IdRes int viewId, int max) {
        ProgressBar view = findViewById(viewId);
        view.setMax(max);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setRating(@IdRes int viewId, float rating) {
        RatingBar view = findViewById(viewId);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @param max    The range of the RatingBar to 0...max.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setRating(@IdRes int viewId, float rating, int max) {
        RatingBar view = findViewById(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param tag    The tag;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setTag(@IdRes int viewId, Object tag) {
        View view = findViewById(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setTag(@IdRes int viewId, int key, Object tag) {
        View view = findViewById(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param viewId  The view id.
     * @param checked The checked status;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setChecked(@IdRes int viewId, boolean checked) {
        Checkable view = findViewById(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * Sets the adapter of a adapter view.
     *
     * @param viewId  The view id.
     * @param adapter The adapter;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setAdapter(@IdRes int viewId, Adapter adapter) {
        AdapterView<Adapter> view = findViewById(viewId);
        view.setAdapter(adapter);
        return this;
    }

    /**
     * set the on item click listener
     * 设置Item的点击事件
     *
     * @param listener listener
     * @param position position
     */
    public void setOnItemClickListener(final OnItemClickListener listener, final int position) {
        if (listener == null) {
            itemView.setOnClickListener(null);
        } else {
            itemView.setOnClickListener(new OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(v, position);
                }
            });
        }
    }

    /**
     * set the on item long click listener
     * 设置Item的长点击事件
     *
     * @param listener listener
     * @param position position
     */
    public void setOnItemLongClickListener(final OnItemLongClickListener listener, final int position) {
        if (listener == null) {
            itemView.setOnLongClickListener(null);
        } else {
            itemView.setOnLongClickListener(new OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    return listener.onItemLongClick(v, position);
                }
            });
        }
    }

    /**
     * Sets the on click listener of the view.
     *
     * @param viewId
     *            The view id.
     * @param listener
     *            The on click listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnClickListener(@IdRes int viewId, OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * Sets the on touch listener of the view.
     *
     * @param viewId
     *            The view id.
     * @param listener
     *            The on touch listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnTouchListener(@IdRes int viewId, OnTouchListener listener) {
        View view = findViewById(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * Sets the on long click listener of the view.
     *
     * @param viewId
     *            The view id.
     * @param listener
     *            The on long click listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnLongClickListener(@IdRes int viewId, OnLongClickListener listener) {
        View view = findViewById(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * the click listeners callback
     * 点击事件回调
     */
    public interface OnItemClickListener {
        /**
         * on item click call back
         *
         * @param convertView convertView
         * @param position position
         */
        void onItemClick(View convertView, int position);
    }

    /**
     * the long click listeners callback
     * 长点击事件回调
     */
    public interface OnItemLongClickListener {
        /**
         * on item long click call back
         *
         * @param convertView convertView
         * @param position position
         * @return true false
         */
        boolean onItemLongClick(View convertView, int position);
    }
}
