package com.pitty.expandablerecyclerview;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import com.pitty.expandablerecyclerview.BaseViewHolder.OnItemClickListener;
import com.pitty.expandablerecyclerview.BaseViewHolder.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class QuickRecyclerViewAdapter<T, VH extends ViewHolder> extends Adapter<VH> {
    private final List<T> mList;
    private int mLayoutResId;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public QuickRecyclerViewAdapter() {
        mList = new ArrayList<>();
    }

    public QuickRecyclerViewAdapter(@LayoutRes int layoutResId) {
        mList = new ArrayList<>();
        mLayoutResId = layoutResId;
    }

    public QuickRecyclerViewAdapter(List<T> data) {
        mList = data == null ? new ArrayList<T>() : new ArrayList<>(data);
    }

    public QuickRecyclerViewAdapter(@LayoutRes int layoutResId, List<T> data) {
        this(data);
        mLayoutResId = layoutResId;
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mList.size();
    }

    @SuppressWarnings("unchecked")
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getSize() {
        return mList.size();
    }

    @SuppressWarnings("unchecked")
    public void setList(List<T> list) {
        mList.clear();
        if (list == null) {
            return;
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    /** Clear data list */
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void add(T elem) {
        mList.add(elem);
        notifyItemInserted(mList.size());
    }

    public void addAll(List<T> elem) {
        int index = mList.size() - 1;
        mList.addAll(elem);
        notifyItemRangeInserted(index, elem.size());
    }

    @SuppressWarnings("unchecked")
    public void addAll(Collection list) {
        int index = mList.size() - 1;
        mList.addAll(list);
        notifyItemRangeInserted(index, list.size());
    }

    public void set(T oldElem, T newElem) {
        set(mList.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        mList.set(index, elem);
        notifyItemChanged(index);
    }

    public void remove(T elem) {
        remove(mList.indexOf(elem));
    }

    public void remove(int index) {
        mList.remove(index);
        notifyItemRemoved(index);
    }

    public void replaceAll(List<T> elem) {
        mList.clear();
        mList.addAll(elem);
        notifyDataSetChanged();
    }

    public boolean contains(T elem) {
        return mList.contains(elem);
    }

    public List getList() {
        return mList;
    }

    /**
     * Please return RecyclerView loading layout Id array
     * 请返回RecyclerView加载的布局Id数组
     *
     * @return 布局Id数组
     */
    public int getLayoutId(int viewType) {
        return mLayoutResId;
    }

    /**
     * butt joint the onBindViewHolder and
     * If you want to write logic in onBindViewHolder, you can write here
     * 对接了onBindViewHolder
     * onBindViewHolder里的逻辑写在这
     *
     * @param viewHolder viewHolder
     * @param position position
     */
    public abstract void onBindRecycleViewHolder(BaseViewHolder<T> viewHolder, int position);

    @Override
    @SuppressWarnings("unchecked")
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutId(viewType);
        if (layoutId <= 0) {
            return null;
        }

        return (VH) BaseViewHolder.get(parent, layoutId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            BaseViewHolder<T> baseHolder = (BaseViewHolder<T>) holder;
            onBindRecycleViewHolder(baseHolder, position);
            baseHolder.setOnItemClickListener(mOnItemClickListener, position);
            baseHolder.setOnItemLongClickListener(mOnItemLongClickListener, position);
            baseHolder.setItem(getItem(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * set the on item click listener
     * 设置点击事件
     *
     * @param listener onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * set the on item long click listener
     * 设置长点击事件
     *
     * @param listener onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }
}
