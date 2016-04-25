package com.pitty.expandablerecyclerview;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class QuickExpandableRecyclerViewAdapter<G, C, GVH extends BaseViewHolder<G>, CVH extends BaseViewHolder<C>>
        extends BaseExpandableRecyclerViewAdapter<GVH, CVH> {
    private final List<G> mGroupList;
    private final List<List<C>> mChildrenList;
    private int mGroupLayoutResId;
    private int mChildLayoutResId;

    public QuickExpandableRecyclerViewAdapter() {
        mGroupList = new ArrayList<>();
        mChildrenList = new ArrayList<>();
    }

    public QuickExpandableRecyclerViewAdapter(@LayoutRes int groupLayoutId, @LayoutRes int childLayoutId) {
        mGroupList = new ArrayList<>();
        mChildrenList = new ArrayList<>();
        mGroupLayoutResId = groupLayoutId;
        mChildLayoutResId = childLayoutId;
    }

    public QuickExpandableRecyclerViewAdapter(List<G> group, List<List<C>> children) {
        mGroupList = group == null ? new ArrayList<G>() : new ArrayList<>(group);
        mChildrenList = children == null ? new ArrayList<List<C>>() : new ArrayList<>(children);
    }

    public QuickExpandableRecyclerViewAdapter(@LayoutRes int groupLayoutId, @LayoutRes int childLayoutId,
                                                     List<G> group, List<List<C>> children) {
        this(group, children);
        mGroupLayoutResId = groupLayoutId;
        mChildLayoutResId = childLayoutId;
    }

    @Override
    public int getGroupCount() {
        return mGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildrenList.get(groupPosition).size();
    }

    @Override
    public G getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public C getChild(int groupPosition, int childPosition) {
        return mChildrenList.get(groupPosition).get(childPosition);
    }

    @Override
    @SuppressWarnings("unchecked")
    public GVH onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getGroupLayoutId(viewType);
        if (layoutId <= 0) {
            return null;
        }
        return (GVH) BaseViewHolder.get(parent, layoutId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CVH onCreateChildViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getChildLayoutId(viewType);
        if (layoutId <= 0) {
            return null;
        }
        return (CVH) BaseViewHolder.get(parent, layoutId);
    }

    /**
     * Please return RecyclerView loading layout Id array
     * 请返回RecyclerView加载的布局Id
     *
     * @return 布局Id
     */
    public @LayoutRes int getGroupLayoutId(int viewType) {
        return mGroupLayoutResId;
    }

    public @LayoutRes int getChildLayoutId(int viewType) {
        return mChildLayoutResId;
    }

    @SuppressWarnings("unchecked")
    public void setList(List<G> group, List<List<C>> children) {
        mGroupList.clear();
        mChildrenList.clear();
        if (group == null) {
            return;
        }
        mGroupList.addAll(group);
        mChildrenList.addAll(children);
        notifyDataSetChanged();
    }

    public void setChildrenList(int groupPosition, List<C> children) {
        List<C> list = mChildrenList.get(groupPosition);
        list.clear();
        if (null == children) {
            return;
        }
        list.addAll(children);
        notifyDataSetChanged();
    }

    /** Clear data list */
    public void clear() {
        mGroupList.clear();
        mChildrenList.clear();
        notifyDataSetChanged();
    }

    public void clearChildren(int groupPosition) {
        List<C> list = mChildrenList.get(groupPosition);
        list.clear();
        notifyDataSetChanged();
    }

    public void add(G elem, List<C> children) {
        mGroupList.add(elem);
        mChildrenList.add(children);
        notifyGroupItemInserted(mGroupList.size());
    }

    public void addChild(int groupPosition, C child) {
        List<C> list = mChildrenList.get(groupPosition);
        list.add(child);
        notifyChildItemInserted(groupPosition, list.size());
    }

    public void addAll(List<G> elem, List<List<C>> children) {
        int index = mGroupList.size() - 1;
        mGroupList.addAll(elem);
        mChildrenList.addAll(children);
        notifyGroupItemRangeInserted(index, elem.size());
    }

    public void addAllChild(int groupPosition, List<C> children) {
        List<C> list = mChildrenList.get(groupPosition);
        int index = list.size() - 1;
        list.addAll(children);
        notifyChildItemRangeInserted(groupPosition, index, children.size());
    }

    public void setGroup(G oldElem, G newElem, List<C> newChildren) {
        setGroup(mGroupList.indexOf(oldElem), newElem, newChildren);
    }

    public void setGroup(int groupIndex, G elem, List<C> newChildren) {
        mGroupList.set(groupIndex, elem);
        mChildrenList.set(groupIndex, newChildren);
        notifyGroupItemChanged(groupIndex);
    }

    public void setChild(G group, C oldChild, C child) {
        int groupIndex = mGroupList.indexOf(group);
        int childIndex = mChildrenList.get(groupIndex).indexOf(oldChild);
        setChild(groupIndex, childIndex, child);
    }

    public void setChild(int groupIndex, int childIndex, C child) {
        List<C> list = mChildrenList.get(groupIndex);
        list.set(childIndex, child);
        notifyChildItemChanged(groupIndex, childIndex);
    }

    public void remove(G elem) {
        remove(mGroupList.indexOf(elem));
    }

    public void remove(int index) {
        mGroupList.remove(index);
        mChildrenList.remove(index);
        notifyGroupItemRemoved(index);
    }

    public void removeChild(int groupIndex, int childIndex) {
        List<C> list = mChildrenList.get(groupIndex);
        list.remove(childIndex);
        notifyChildItemRemoved(groupIndex, childIndex);
    }

    public void replaceAll(List<G> elem, List<List<C>> children) {
        mGroupList.clear();
        mChildrenList.clear();
        mGroupList.addAll(elem);
        mChildrenList.addAll(children);
        notifyDataSetChanged();
    }

    public void replaceAllChildren(int groupIndex, List<C> children) {
        List<C> list = mChildrenList.get(groupIndex);
        list.clear();
        list.addAll(children);
        notifyGroupItemChanged(groupIndex);
    }

    public boolean contains(G elem) {
        return mGroupList.contains(elem);
    }
}
