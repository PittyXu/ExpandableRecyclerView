package com.pitty.expandablerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HeterogeneousExpandableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseExpandableRecyclerViewAdapter<GVH extends ViewHolder, CVH extends ViewHolder>
        extends RecyclerView.Adapter<ViewHolder>
        implements ExpandableListAdapter<GVH, CVH>, HeterogeneousExpandableList {

    private ArrayList<ListPosition> mPositionList = new ArrayList<>();

    private void init() {
        int count = getGroupCount();
        if (mPositionList.size() < count) {
            for (int i = 0; i < count; i++) {
                ListPosition position = new ListPosition();
                position.expanded = false;
                position.groupPos = i;
                position.childPos = -1;
                position.type = ListPosition.GROUP;
                mPositionList.add(position);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType >= ListPosition.GROUP) {
            return onCreateGroupViewHolder(parent, viewType - ListPosition.GROUP);
        } else if (viewType >= ListPosition.CHILD) {
            return onCreateChildViewHolder(parent, viewType - ListPosition.CHILD);
        } else {
            throw new IllegalStateException("Incorrect ViewType found");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListPosition pos = mPositionList.get(position);
        OnItemClickListener listener = new OnItemClickListener(pos);
        holder.itemView.setOnClickListener(listener);
        if (pos.type >= ListPosition.GROUP) {
            if (holder instanceof OnGroupExpandListener) {
                listener.mExpandListener = (OnGroupExpandListener) holder;
            }
            if (holder instanceof OnGroupCollapseListener) {
                listener.mCollapseListener = (OnGroupCollapseListener) holder;
            }
            onBindGroupViewHolder((GVH) holder, pos.groupPos);
        } else {
            onBindChildViewHolder((CVH) holder, pos.groupPos, pos.childPos);
        }
    }

    @Override
    public synchronized int getItemCount() {
        init();
        return mPositionList.size();
    }

    @Override
    public synchronized int getItemViewType(int position) {
        ListPosition pos = mPositionList.get(position);
        if (ListPosition.GROUP == pos.type) {
            int gType = getGroupType(pos.groupPos);
            return gType + ListPosition.GROUP;
        } else {
            int cType = getChildType(pos.groupPos, pos.childPos);
            return cType + ListPosition.CHILD;
        }
    }

    @Override
    public synchronized void notifyGroupItemInserted(int groupPos) {
        notifyItemInserted(insertGroupItem(groupPos, 1));
    }

    @Override
    public synchronized void notifyGroupItemRangeInserted(int groupPos, int itemCount) {
        notifyItemRangeInserted(insertGroupItem(groupPos, itemCount), itemCount);
    }

    private int insertGroupItem(int groupPos, int count) {
        for (int i = 0; i < mPositionList.size(); i++) {
            ListPosition pos = mPositionList.get(i);
            if (pos.type == ListPosition.GROUP &&
                    pos.groupPos == groupPos) {
                for (int j = 0; j < count; j++) {
                    ListPosition nPos = new ListPosition();
                    nPos.groupPos = groupPos + j;
                    nPos.childPos = -1;
                    nPos.type = ListPosition.GROUP;
                    mPositionList.add(i + j, nPos);
                }
                for (int j = i + count; j < mPositionList.size(); j++) {
                    ListPosition p = mPositionList.get(j);
                    p.groupPos = p.groupPos + count;
                }
                return i;
            }
        }

        for (int j = 0; j < count; j++) {
            ListPosition nPos = new ListPosition();
            nPos.groupPos = groupPos + j;
            nPos.childPos = -1;
            nPos.type = ListPosition.GROUP;
            mPositionList.add(nPos);
        }
        return mPositionList.size() - count;
    }

    @Override
    public synchronized void notifyGroupItemChanged(int groupPos) {
        int startIndex = 0;
        int changeCount = 0;
        for (int i = 0; i < mPositionList.size(); i++) {
            ListPosition pos = mPositionList.get(i);
            if (pos.groupPos == groupPos) {
                if (pos.type == ListPosition.GROUP) {
                    startIndex = i;
                }
                changeCount++;
            }
        }
        notifyItemRangeChanged(startIndex, changeCount);
    }

    @Override
    public synchronized void notifyGroupItemRangeChanged(int groupPos, int itemCount) {
        int startIndex = -1;
        int groupChangeCount = 0;
        int changeCount = 0;
        for (int i = 0; i < mPositionList.size() && groupChangeCount <= itemCount; i++) {
            ListPosition pos = mPositionList.get(i);
            if (pos.type == ListPosition.GROUP && pos.groupPos == groupPos) {
                startIndex = i;
            }
            if (startIndex >= 0) {
                if (pos.type == ListPosition.GROUP) {
                    groupChangeCount++;
                }
                changeCount++;
            }
        }
        notifyItemRangeChanged(startIndex, changeCount);
    }

    @Override
    public synchronized void notifyGroupItemMoved(int groupPos, int toGroupPos) {
        int toGroup = toGroupPos;
        if (toGroupPos > groupPos) {
            toGroup++;
        }
        List<ListPosition> fromPos = new ArrayList<>();
        int fromIndex = mPositionList.size() - 1;
        int toIndex = mPositionList.size();

        for (int i = 0; i < mPositionList.size(); i++) {
            ListPosition pos = mPositionList.get(i);
            if (pos.groupPos == groupPos) {
                pos.groupPos = toGroupPos;
                fromPos.add(pos);
                if (pos.type == ListPosition.GROUP) {
                    fromIndex = i;
                }
            } else if (pos.groupPos == toGroup &&
                         pos.type == ListPosition.GROUP) {
                toIndex = i;
            }
        }

        if (fromPos.size() <= 0) {
            return;
        }
        mPositionList.removeAll(fromPos);
        for (int i = mPositionList.size() - 1; i >= fromIndex; i--) {
            ListPosition pos = mPositionList.get(i);
            pos.groupPos = pos.groupPos - 1;
        }
        if (toIndex > fromIndex) {
            toIndex = toIndex - fromPos.size();
        }
        notifyItemRangeRemoved(fromIndex, fromPos.size());

        mPositionList.addAll(toIndex, fromPos);
        for (int i = toIndex + fromPos.size(); i < mPositionList.size(); i++) {
            ListPosition pos = mPositionList.get(i);
            pos.groupPos = pos.groupPos + 1;
        }
        notifyItemRangeInserted(toIndex, fromPos.size());
    }

    @Override
    public synchronized void notifyGroupItemRemoved(int groupPos) {
        int lastIndex = mPositionList.size() - 1;
        int removedCount = 0;
        for (int i = lastIndex; i >= 0; i--) {
            ListPosition pos = mPositionList.get(i);
            if (pos.groupPos == groupPos) {
                lastIndex = i;
                removedCount++;
                mPositionList.remove(i);
            }
        }
        for (int i = lastIndex; i < mPositionList.size(); i++) {
            ListPosition pos = mPositionList.get(i);
            pos.groupPos = pos.groupPos - 1;
        }
        notifyItemRangeRemoved(lastIndex, removedCount);
    }

    @Override
    public synchronized void notifyGroupItemRangeRemoved(int groupPos, int itemCount) {
        int removedCount = 0;
        int startIndex = 0;
        int groupCount = 0;
        for (int i = 0; i < mPositionList.size() && groupCount <= itemCount; i++) {
            ListPosition pos = mPositionList.get(i);
            if (pos.type == ListPosition.GROUP && pos.groupPos == groupPos) {
                startIndex = i;
                groupCount++;
                removedCount++;
            } else if (pos.type == ListPosition.GROUP && groupCount > 0) {
                groupCount++;
                removedCount++;
            } else if (groupCount > 0) {
                removedCount++;
            }
        }

        for (int i = mPositionList.size() - 1; i >= startIndex; i--) {
            if (i > startIndex + removedCount) {
                ListPosition pos = mPositionList.get(i);
                pos.groupPos = pos.groupPos - itemCount;
            } else {
                mPositionList.remove(i);
            }
        }
        notifyItemRangeRemoved(startIndex, removedCount);
    }

    @Override
    public synchronized void notifyChildItemInserted(int groupPos, int childPos) {
        if (isExpanded(groupPos)) {
            int childIndex = insertChildItem(groupPos, childPos, 1);
            if (childIndex >= 0) {
                notifyItemInserted(childIndex);
            }
        }
    }

    @Override
    public synchronized void notifyChildItemRangeInserted(int groupPos, int childPos, int itemCount) {
        if (isExpanded(groupPos)) {
            int childIndex = insertChildItem(groupPos, childPos, itemCount);
            if (childIndex > 0) {
                notifyItemRangeInserted(childIndex, itemCount);
            }
        }
    }

    private int insertChildItem(int groupPos, int childPos, int itemCount) {
        for (int i = 0; i < mPositionList.size(); i++) {
            ListPosition pos = mPositionList.get(i);
            if (pos.groupPos == groupPos && pos.childPos == childPos) {
                for (int j = 0; j < itemCount; j++) {
                    ListPosition nPos = new ListPosition();
                    nPos.groupPos = groupPos;
                    nPos.childPos = childPos + j;
                    nPos.type = ListPosition.CHILD;
                    mPositionList.add(i + j, nPos);
                }
                for (int j = i + itemCount; j < mPositionList.size(); j++) {
                    ListPosition p = mPositionList.get(j);
                    if (p.type == ListPosition.CHILD &&
                            p.groupPos == groupPos) {
                        p.childPos = p.childPos + itemCount;
                    }
                }
                return i;
            }
        }
        for (int i = mPositionList.size() - 1; i >= 0 ; i--) {
            ListPosition pos = mPositionList.get(i);
            if (pos.groupPos == groupPos) {
                for (int j = 0; j < itemCount; j++) {
                    ListPosition position = new ListPosition();
                    position.groupPos = groupPos;
                    position.childPos = childPos + j;
                    position.type = ListPosition.CHILD;
                    mPositionList.add(i + j + 1, position);
                }
                return i + 1;
            }
        }
        return -1;
    }

    @Override
    public synchronized void notifyChildItemChanged(int groupPos, int childPos) {
        for (int i = 0; i < mPositionList.size(); i++) {
            ListPosition pos = mPositionList.get(i);
            if (pos.type == ListPosition.CHILD &&
                    pos.groupPos == groupPos &&
                    pos.childPos == childPos) {
                notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    public synchronized void notifyChildItemRangeChanged(int groupPos, int childPos, int itemCount) {
        for (int i = 0; i < mPositionList.size(); i++) {
            ListPosition pos = mPositionList.get(i);
            if (pos.type == ListPosition.CHILD &&
                    pos.groupPos == groupPos &&
                    pos.childPos == childPos) {
                notifyItemRangeChanged(i, itemCount);
                return;
            }
        }
    }

    @Override
    public synchronized void notifyChildItemRemoved(int groupPos, int childPos) {
        if (!isExpanded(groupPos)) {
            return;
        }
        for (int i = mPositionList.size() - 1; i >= 0; i--) {
            ListPosition pos = mPositionList.get(i);
            if (pos.type == ListPosition.CHILD &&
                    pos.groupPos == groupPos &&
                    pos.childPos == childPos) {
                mPositionList.remove(i);
                for (int j = i; j < mPositionList.size(); j++) {
                    ListPosition p = mPositionList.get(j);
                    if (p.type == ListPosition.CHILD &&
                            p.groupPos == groupPos) {
                        p.childPos = p.childPos - 1;
                    }
                }
                notifyItemRemoved(i);
                return;
            }
        }
    }

    @Override
    public synchronized void notifyChildItemRangeRemoved(int groupPos, int childPos, int itemCount) {
        if (!isExpanded(groupPos)) {
            return;
        }
        int removed = 0;
        for (int i = mPositionList.size() - 1; i >= 0; i--) {
            ListPosition pos = mPositionList.get(i);
            if (pos.type == ListPosition.CHILD &&
                    pos.groupPos == groupPos &&
                    pos.childPos == childPos) {
                for (int j = itemCount; j >= 0; j--) {
                    if (mPositionList.size() > i + j) {
                        ListPosition p = mPositionList.get(i + j);
                        if (pos.type == ListPosition.CHILD &&
                                    p.groupPos == groupPos) {
                            mPositionList.remove(i + j);
                            removed++;
                        }
                    }
                }
                for (int j = i; j < mPositionList.size(); j++) {
                    ListPosition p = mPositionList.get(j);
                    if (p.type == ListPosition.CHILD &&
                            p.groupPos == groupPos) {
                        p.childPos = p.childPos - removed;
                    }
                }
                notifyItemRangeRemoved(i, removed);
                return;
            }
        }
    }

    @Override
    public synchronized void notifyChildItemMoved(int groupPos, int childPos, int toGroupPos, int toChildPos) {
        if (!isExpanded(groupPos)) {
            return;
        }
        int find = 0;
        int from = 0;
        int to = 0;
        for (int i = mPositionList.size() - 1; i >= 0 && find < 2; i--) {
            ListPosition pos = mPositionList.get(i);
            if (pos.type == ListPosition.CHILD &&
                    pos.groupPos == groupPos &&
                    pos.childPos == childPos) {
                find++;
                from = i;
            } else if (pos.type == ListPosition.CHILD &&
                         pos.groupPos == toGroupPos &&
                         pos.childPos == toChildPos) {
                find++;
                to = i;
            }
        }
        Collections.swap(mPositionList, from, to);
        notifyItemMoved(from, to);
    }

    public void expandAll() {
        expandGroupRange(0, getGroupCount());
    }

    public void expandGroupRange(int startGroupPos, int groupCount) {
        int endGroupIndex = startGroupPos + groupCount;
        for (int i = startGroupPos; i < endGroupIndex; i++) {
            expandGroup(i);
        }
    }

    public synchronized boolean expandGroup(int groupPos) {
        if (isExpanded(groupPos)) { // Already expanded.
            return false;
        }
        boolean retValue = false;
        int index = 0;
        int added = 0;
        for (int i = 0; i < mPositionList.size() && !retValue; i++) {
            ListPosition pos = mPositionList.get(i);
            if (pos.type == ListPosition.GROUP &&
                    pos.groupPos == groupPos) {
                index = i;
                int cCount = getChildrenCount(groupPos);
                for (int j = 0; j < cCount; j++) {
                    ListPosition cPos = new ListPosition();
                    cPos.groupPos = groupPos;
                    cPos.childPos = j;
                    cPos.type = ListPosition.CHILD;
                    mPositionList.add(i + j + 1, cPos);
                    added++;
                }
                pos.expanded = true;
                retValue = true;
            }
        }
        if (mOnGroupExpandListener != null) {
            mOnGroupExpandListener.onGroupExpand(groupPos);
        }
        notifyItemChanged(index);
        notifyItemRangeInserted(index + 1, added);
        return retValue;
    }

    public void collapseAll() {
        collapseGroupRange(0, getGroupCount());
    }

    public void collapseGroupRange(int startGroupPos, int groupCount) {
        int endGroupIndex = startGroupPos + groupCount;
        for (int i = startGroupPos; i < endGroupIndex; i++) {
            collapseGroup(i);
        }
    }

    public synchronized boolean collapseGroup(int groupPos) {
        if (!isExpanded(groupPos)) {
            return false;
        }
        boolean retValue = false;
        int index = 0;
        int removed = 0;
        for (int i = 0; i < mPositionList.size() && !retValue; i++) {
            ListPosition pos = mPositionList.get(i);
            if (pos.type == ListPosition.GROUP &&
                    pos.groupPos == groupPos) {
                int cCount = getChildrenCount(groupPos);
                if (mPositionList.size() > i + cCount) {
                    for (int j = cCount - 1; j >= 0; j--) {
                        ListPosition p = mPositionList.get(i + j + 1);
                        if (p.type == ListPosition.CHILD &&
                                    p.groupPos == groupPos) {
                            mPositionList.remove(i + j + 1);
                            removed++;
                        }
                    }
                }
                index = i;
                retValue = true;
                pos.expanded = false;
            }
        }
        if (mOnGroupCollapseListener != null) {
            mOnGroupCollapseListener.onGroupCollapse(groupPos);
        }
        notifyItemChanged(index);
        notifyItemRangeRemoved(index + 1, removed);
        return retValue;
    }

    @Override
    public boolean isExpanded(int groupPosition) {
        for (int i = 0; i < getItemCount(); i++) {
            ListPosition pos = mPositionList.get(i);
            if (pos.type == ListPosition.GROUP &&
                    pos.groupPos == groupPosition) {
                return pos.expanded;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return getGroupCount() == 0;
    }

    public int getGroupType(int groupPosition) {
        return 0;
    }

    public int getGroupTypeCount() {
        return 1;
    }

    public int getChildType(int groupPosition, int childPosition) {
        return 0;
    }

    public int getChildTypeCount() {
        return 1;
    }

    private static class ListPosition {
        public final static int GROUP = 1 << 30;
        public final static int CHILD = 1 << 29;

        public boolean expanded = false;
        public int groupPos;
        public int childPos;
        public int type;
    }

    private class OnItemClickListener implements OnClickListener {
        private OnGroupExpandListener mExpandListener;
        private OnGroupCollapseListener mCollapseListener;
        private final ListPosition mPosition;

        OnItemClickListener(ListPosition pos) {
            mPosition = pos;
        }

        @Override
        public void onClick(View v) {
            if (mPosition.type >= ListPosition.GROUP) {
                if (null != mOnGroupClickListener) {
                    mOnGroupClickListener.onGroupClick(v, mPosition.groupPos);
                }
                if (isExpanded(mPosition.groupPos)) {
                    if (null != mExpandListener) {
                        mExpandListener.onGroupExpand(mPosition.groupPos);
                    }
                } else if (null != mCollapseListener) {
                    mCollapseListener.onGroupCollapse(mPosition.groupPos);
                }
            } else {
                if (null != mOnChildClickListener) {
                    mOnChildClickListener.onChildClick(v, mPosition.groupPos, mPosition.childPos);
                }
            }
        }
    }

    /** Used for being notified when a group is collapsed */
    public interface OnGroupCollapseListener {
        /**
         * Callback method to be invoked when a group in this expandable list has
         * been collapsed.
         *
         * @param groupPosition The group position that was collapsed
         */
        void onGroupCollapse(int groupPosition);
    }

    private OnGroupCollapseListener mOnGroupCollapseListener;

    public void setOnGroupCollapseListener(
            OnGroupCollapseListener onGroupCollapseListener) {
        mOnGroupCollapseListener = onGroupCollapseListener;
    }

    /** Used for being notified when a group is expanded */
    public interface OnGroupExpandListener {
        /**
         * Callback method to be invoked when a group in this expandable list has
         * been expanded.
         *
         * @param groupPosition The group position that was expanded
         */
        void onGroupExpand(int groupPosition);
    }

    private OnGroupExpandListener mOnGroupExpandListener;

    public void setOnGroupExpandListener(
            OnGroupExpandListener onGroupExpandListener) {
        mOnGroupExpandListener = onGroupExpandListener;
    }

    /**
     * Interface definition for a callback to be invoked when a group in this
     * expandable list has been clicked.
     */
    public interface OnGroupClickListener {
        /**
         * Callback method to be invoked when a group in this expandable list has
         * been clicked.
         *
         * @param v The view within the expandable list/ListView that was clicked
         * @param groupPosition The group position that was clicked
         */
        void onGroupClick(View v, int groupPosition);
    }

    private OnGroupClickListener mOnGroupClickListener = new OnGroupClickListener() {

        @Override
        public void onGroupClick(View v, int groupPosition) {
            if (!isExpanded(groupPosition)) {
                expandGroup(groupPosition);
            } else {
                collapseGroup(groupPosition);
            }
        }
    };

    public void setOnGroupClickListener(OnGroupClickListener onGroupClickListener) {
        mOnGroupClickListener = onGroupClickListener;
    }

    /**
     * Interface definition for a callback to be invoked when a child in this
     * expandable list has been clicked.
     */
    public interface OnChildClickListener {
        /**
         * Callback method to be invoked when a child in this expandable list has
         * been clicked.
         *
         * @param v The view within the expandable list/ListView that was clicked
         * @param groupPosition The group position that contains the child that
         *        was clicked
         * @param childPosition The child position within the group
         */
        void onChildClick(View v, int groupPosition, int childPosition);
    }

    private OnChildClickListener mOnChildClickListener;

    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        mOnChildClickListener = onChildClickListener;
    }
}
