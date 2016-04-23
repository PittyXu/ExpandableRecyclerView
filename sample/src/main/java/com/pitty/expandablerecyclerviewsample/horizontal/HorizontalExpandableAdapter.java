package com.pitty.expandablerecyclerviewsample.horizontal;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.pitty.expandablerecyclerview.BaseExpandableListAdapter;
import com.pitty.expandablerecyclerviewsample.R;

import java.util.ArrayList;
import java.util.List;

public class HorizontalExpandableAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;
    private List<String> mGroups = new ArrayList<>();
    private List<List<String>> mChildren = new ArrayList<>();

    public HorizontalExpandableAdapter(Context context, List<String> groups,
                                              List<List<String>> children) {
        mInflater = LayoutInflater.from(context);
        mGroups = groups;
        mChildren = children;
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<String> children = mChildren.get(groupPosition);
        if (null != children) {
            return children.size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<String> children = mChildren.get(groupPosition);
        if (null != children) {
            return children.get(childPosition);
        }
        return null;
    }

    @Override
    public ViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return new GroupViewHolder(mInflater.inflate(R.layout.list_item_parent_horizontal, parent, false));
    }

    @Override
    public ViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return new ChildViewHolder(mInflater.inflate(R.layout.list_item_child_horizontal, parent, false));
    }

    @Override
    public void onBindGroupViewHolder(ViewHolder groupHolder, int groupPosition) {
        String item = (String) getGroup(groupPosition);
        ((GroupViewHolder) groupHolder).mTextView.setText(item);
        ((GroupViewHolder) groupHolder).setExpand(isExpanded(groupPosition));
    }

    @Override
    public void onBindChildViewHolder(ViewHolder childHolder, int groupPosition,
                                             int childPosition) {
        String item = (String) getChild(groupPosition, childPosition);
        ((ChildViewHolder) childHolder).mTextView.setText(item);
    }


    static class ChildViewHolder extends ViewHolder {
        private TextView mTextView;

        public ChildViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.list_item_horizontal_child_textView);
        }
    }

    static class GroupViewHolder extends ViewHolder implements OnGroupExpandListener,
                                                                       OnGroupCollapseListener {
        private static final float INITIAL_POSITION = 0.0f;
        private static final float ROTATED_POSITION = 180f;

        private final ImageView mArrowExpandImageView;
        private TextView mTextView;

        public GroupViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.list_item_parent_horizontal_parent_textView);
            mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.list_item_parent_horizontal_arrow_imageView);
        }

        public void setExpand(boolean expanded) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (expanded) {
                    mArrowExpandImageView.setRotation(ROTATED_POSITION);
                } else {
                    mArrowExpandImageView.setRotation(INITIAL_POSITION);
                }
            }
        }

        @Override
        public void onGroupCollapse(int groupPosition) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                RotateAnimation rotateAnimation;
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION, INITIAL_POSITION,
                                                             RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                                         RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                rotateAnimation.setDuration(200);
                rotateAnimation.setFillAfter(true);
                mArrowExpandImageView.startAnimation(rotateAnimation);
            }
        }

        @Override
        public void onGroupExpand(int groupPosition) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                RotateAnimation rotateAnimation;
                rotateAnimation = new RotateAnimation(ROTATED_POSITION, INITIAL_POSITION,
                                                             RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                                             RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                rotateAnimation.setDuration(200);
                rotateAnimation.setFillAfter(true);
                mArrowExpandImageView.startAnimation(rotateAnimation);
            }
        }
    }
}
