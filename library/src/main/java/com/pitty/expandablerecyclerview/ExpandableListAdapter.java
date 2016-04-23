package com.pitty.expandablerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.List;

/**
 * An adapter that links a {@link RecyclerView} with the underlying
 * data. The implementation of this interface will provide access
 * to the data of the children (categorized by groups), and also instantiate
 * {@link View}s for children and groups.
 */
public interface ExpandableListAdapter<GVH extends ViewHolder, CVH extends ViewHolder> {
    /**
     * Gets the number of groups.
     *
     * @return the number of groups
     */
    int getGroupCount();

    /**
     * Gets the number of children in a specified group.
     *
     * @param groupPosition the position of the group for which the children
     *            count should be returned
     * @return the children count in the specified group
     */
    int getChildrenCount(int groupPosition);

    /**
     * Gets the data associated with the given group.
     *
     * @param groupPosition the position of the group
     * @return the data child for the specified group
     */
    Object getGroup(int groupPosition);

    /**
     * Gets the data associated with the given child within the given group.
     *
     * @param groupPosition the position of the group that the child resides in
     * @param childPosition the position of the child with respect to other
     *            children in the group
     * @return the data of the child
     */
    Object getChild(int groupPosition, int childPosition);

    boolean isExpanded(int groupPosition);

    /**
     * @see ListAdapter#isEmpty()
     */
    boolean isEmpty();

    void notifyGroupItemInserted(int groupPos);

    void notifyGroupItemRangeInserted(int groupPos, int itemCount);

    void notifyGroupItemRemoved(int groupPos);

    void notifyGroupItemRangeRemoved(int groupPos, int itemCount);

    void notifyGroupItemChanged(int groupPos);

    void notifyGroupItemRangeChanged(int groupPos, int itemCount);

    void notifyGroupItemMoved(int groupPos, int toGroupPos);

    void notifyChildItemInserted(int groupPos, int childPos);

    void notifyChildItemRangeInserted(int groupPos, int childPos, int itemCount);

    void notifyChildItemRemoved(int groupPos, int childPos);

    void notifyChildItemRangeRemoved(int groupPos, int childPos, int itemCount);

    void notifyChildItemChanged(int groupPos, int childPos);

    void notifyChildItemRangeChanged(int groupPos, int childPos, int itemCount);

    void notifyChildItemMoved(int groupPos, int childPos, int toGroupPos, int toChildPos);

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindGroupViewHolder(ViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see RecyclerView.Adapter#getItemViewType(int)
     * @see RecyclerView.Adapter#onBindViewHolder(ViewHolder, int)
     */
    GVH onCreateGroupViewHolder(ViewGroup parent, int viewType);

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindGroupViewHolder(ViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see RecyclerView.Adapter#getItemViewType(int)
     * @see RecyclerView.Adapter#onBindViewHolder(ViewHolder, int)
     */
    CVH onCreateChildViewHolder(ViewGroup parent, int viewType);

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link android.widget.ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     *
     * Override {@link RecyclerView.Adapter#onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param groupHolder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param groupPosition The position of the item within the adapter's data set.
     */
    void onBindGroupViewHolder(GVH groupHolder, int groupPosition);

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link android.widget.ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     *
     * Override {@link RecyclerView.Adapter#onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param childHolder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param groupPosition The position of the item within the adapter's data set.
     */
    void onBindChildViewHolder(CVH childHolder, int groupPosition, int childPosition);
}
