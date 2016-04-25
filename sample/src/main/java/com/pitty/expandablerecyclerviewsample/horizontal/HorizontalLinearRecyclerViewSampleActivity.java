package com.pitty.expandablerecyclerviewsample.horizontal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.pitty.expandablerecyclerview.BaseExpandableRecyclerViewAdapter.OnGroupCollapseListener;
import com.pitty.expandablerecyclerview.BaseExpandableRecyclerViewAdapter.OnGroupExpandListener;
import com.pitty.expandablerecyclerviewsample.R;

import java.util.ArrayList;
import java.util.List;

public class HorizontalLinearRecyclerViewSampleActivity extends AppCompatActivity
        implements OnGroupExpandListener, OnGroupCollapseListener {

    private static final int NUM_TEST_DATA_ITEMS = 20;
    private static final int EXPAND_COLLAPSE_SINGLE_PARENT_INDEX = 2;

    private RecyclerView mRecyclerView;
    private Button mExpandParentTwoButton;
    private Button mCollapseParentTwoButton;
    private Button mExpandAllButton;
    private Button mCollapseAllButton;

    private List<String> mGroups = new ArrayList<>();
    private List<List<String>> mChildren = new ArrayList<>();

    private HorizontalExpandableAdapter mExpandableAdapter;

    public static Intent newIntent(Context context) {
        return new Intent(context, HorizontalLinearRecyclerViewSampleActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_linear_recycler_view_sample);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_horizontal_linear_recycler_view_sample_recyclerView);

        Button expandParentTwoButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_expand_parent_two_button);
        expandParentTwoButton.setOnClickListener(mExpandParentTwoClickListener);

        Button collapseParentTwoButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_collapse_parent_two_button);
        collapseParentTwoButton.setOnClickListener(mCollapseParentTwoClickListener);

        Button expandAllButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_expand_all_button);
        expandAllButton.setOnClickListener(mExpandAllClickListener);

        Button collapseAllButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_collapse_all_button);
        collapseAllButton.setOnClickListener(mCollapseAllClickListener);

        Button addToEndButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_add_to_end_button);
        addToEndButton.setOnClickListener(mAddToEndClickListener);

        Button removeFromEndButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_remove_from_end_button);
        removeFromEndButton.setOnClickListener(mRemoveFromEndClickListener);

        Button addToSecondButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_add_to_second_button);
        addToSecondButton.setOnClickListener(mAddToSecondClickListener);

        Button removeSecondbutton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_remove_second_button);
        removeSecondbutton.setOnClickListener(mRemoveSecondClickListener);

        Button addChild = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_add_child);
        addChild.setOnClickListener(mAddChildClickListener);

        Button removeChild = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_remove_child);
        removeChild.setOnClickListener(mRemoveChildClickListener);

        Button addMultipleParents = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_add_multiple_parents);
        addMultipleParents.setOnClickListener(mAddMultipleParentsClickListener);

        Button modifyLastParent = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_modify_last_parent);
        modifyLastParent.setOnClickListener(mModifyLastParentClickListener);

        Button modifyLastChild = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_modify_last_child);
        modifyLastChild.setOnClickListener(mModifyLastChildClickListener);

        Button expandRangeButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_expand_range);
        expandRangeButton.setOnClickListener(mExpandRangeClickListener);

        Button collapseRangeButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_collapse_range);
        collapseRangeButton.setOnClickListener(mCollapseRangeClickListener);

        Button addTwoChildren = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_add_two_children);
        addTwoChildren.setOnClickListener(mAddTwoChildrenClickListener);

        Button removeTwoChildren = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_remove_two_children);
        removeTwoChildren.setOnClickListener(mRemoveTwoChildrenClickListener);

        Button modifyTwoChildren = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_modify_two_children);
        modifyTwoChildren.setOnClickListener(mModifyTwoChildrenClickListener);

        Button removeTwoParents = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_remove_two_parents);
        removeTwoParents.setOnClickListener(mRemoveTwoParentsClickListener);

        Button modifyTwoParents = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_modify_two_parents);
        modifyTwoParents.setOnClickListener(mModifyTwoParentsClickListener);

        Button parentMoveButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_parent_move);
        parentMoveButton.setOnClickListener(mParentMoveClickListener);

        Button childMoveButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_child_move);
        childMoveButton.setOnClickListener(mChildMoveClickListener);

        // Create a new adapter with 20 test data items
        setUpTestData(NUM_TEST_DATA_ITEMS);
        mExpandableAdapter = new HorizontalExpandableAdapter(this, mGroups, mChildren);

        // Attach this activity to the Adapter as the ExpandCollapseListener
        mExpandableAdapter.setOnGroupExpandListener(this);
        mExpandableAdapter.setOnGroupCollapseListener(this);

        // Set the RecyclerView's adapter to the ExpandableAdapter we just created
        mRecyclerView.setAdapter(mExpandableAdapter);
        // Set the layout manager to a LinearLayout manager for vertical list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        String toastMessage = getString(R.string.item_collapsed, groupPosition);
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        String toastMessage = getString(R.string.item_expanded, groupPosition);
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener mExpandParentTwoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mExpandableAdapter.expandGroup(EXPAND_COLLAPSE_SINGLE_PARENT_INDEX);
        }
    };

    private View.OnClickListener mCollapseParentTwoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mExpandableAdapter.collapseGroup(EXPAND_COLLAPSE_SINGLE_PARENT_INDEX);
        }
    };

    private View.OnClickListener mExpandAllClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mExpandableAdapter.expandAll();
        }
    };

    private View.OnClickListener mCollapseAllClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mExpandableAdapter.collapseAll();
        }
    };

    private OnClickListener mAddToEndClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<String> childList = new ArrayList<>();
            int parentNumber = mGroups.size();

            String horizontalChild = getString(R.string.child_text, parentNumber);
            childList.add(horizontalChild);

            horizontalChild = getString(R.string.second_child_text, parentNumber);
            childList.add(horizontalChild);

            horizontalChild = getString(R.string.third_child_text, parentNumber);
            childList.add(horizontalChild);

            String horizontalParent = getString(R.string.parent_text, parentNumber);

            mGroups.add(horizontalParent);
            mChildren.add(childList);
            mExpandableAdapter.notifyGroupItemInserted(mGroups.size() - 1);
        }
    };

    private OnClickListener mAddToSecondClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<String> childList = new ArrayList<>();

            String horizontalChild = getString(R.string.child_insert_text, 1);
            childList.add(horizontalChild);

            horizontalChild = getString(R.string.child_insert_text, 2);
            childList.add(horizontalChild);

            String horizontalParent = getString(R.string.inserted_parent_text);

            mGroups.add(1, horizontalParent);
            mChildren.add(1, childList);
            mExpandableAdapter.notifyGroupItemInserted(1);
        }
    };

    private OnClickListener mRemoveSecondClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mGroups.size() < 2) {
                return;
            }

            mGroups.remove(1);
            mChildren.remove(1);
            mExpandableAdapter.notifyGroupItemRemoved(1);

        }
    };

    private OnClickListener mRemoveFromEndClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mGroups.size() < 1) {
                return;
            }
            int removeIndex = mGroups.size() - 1;
            mGroups.remove(removeIndex);
            mChildren.remove(removeIndex);
            mExpandableAdapter.notifyGroupItemRemoved(removeIndex);
        }
    };

    private OnClickListener mAddChildClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mChildren.size() < 2) {
                return;
            }

            List<String> childList = mChildren.get(1);
            String horizontalChild = getString(R.string.added_child, childList.size());
            childList.add(horizontalChild);
            mExpandableAdapter.notifyChildItemInserted(1, childList.size() - 1);
        }
    };

    private OnClickListener mRemoveChildClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mChildren.size() < 2) {
                return;
            }

            List<String> childList = mChildren.get(1);
            if (childList.size() < 2) {
                return;
            }

            childList.remove(1);
            mExpandableAdapter.notifyChildItemRemoved(1, 1);
        }
    };

    private OnClickListener mAddMultipleParentsClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // Adds first parent
            ArrayList<String> childList = new ArrayList<>();

            String horizontalChild = getString(R.string.child_insert_text, 1);
            childList.add(horizontalChild);

            horizontalChild = getString(R.string.child_insert_text, 2);
            childList.add(horizontalChild);

            String horizontalParent = getString(R.string.inserted_parent_text);

            mGroups.add(1, horizontalParent);
            mChildren.add(1, childList);

            childList = new ArrayList<>();

            horizontalChild = getString(R.string.child_insert_text, 1);
            childList.add(horizontalChild);

            horizontalChild = getString(R.string.child_insert_text, 2);
            childList.add(horizontalChild);

            horizontalParent = getString(R.string.inserted_parent_text);

            mGroups.add(2, horizontalParent);
            mChildren.add(2, childList);

            mExpandableAdapter.notifyGroupItemRangeInserted(1, 2);
        }
    };

    private OnClickListener mModifyLastParentClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int parentNumber = mGroups.size() - 1;
            String newHorizontalParent = getString(R.string.modified_parent_text, parentNumber);
            int childSize = mChildren.get(parentNumber).size();
            List<String> childItemList = new ArrayList<>();
            for (int i = 0; i < childSize; i++) {
                String horizontalChild = getString(R.string.modified_child_text, i);
                childItemList.add(horizontalChild);
            }
            mGroups.set(parentNumber, newHorizontalParent);
            mChildren.set(parentNumber, childItemList);
            mExpandableAdapter.notifyGroupItemChanged(parentNumber);
        }
    };

    private OnClickListener mModifyLastChildClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int parentNumber = mGroups.size() - 1;
            List<String> childList = mChildren.get(parentNumber);
            int childNumber = childList.size() - 1;
            String horizontalChild = getString(R.string.modified_child_text, childNumber);
            childList.set(childNumber, horizontalChild);
            mExpandableAdapter.notifyChildItemChanged(parentNumber, childNumber);
        }
    };

    private OnClickListener mExpandRangeClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mExpandableAdapter.expandGroupRange(4, 3);
        }
    };

    private OnClickListener mCollapseRangeClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mExpandableAdapter.collapseGroupRange(2, 4);
        }
    };

    private OnClickListener mAddTwoChildrenClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            List<String> childList = mChildren.get(mGroups.size() - 1);

            String horizontalChild = getString(R.string.added_child, childList.size());
            childList.add(horizontalChild);

            horizontalChild = getString(R.string.added_child, childList.size());
            childList.add(horizontalChild);
            mExpandableAdapter.notifyChildItemRangeInserted(mGroups.size() - 1, childList.size() - 2, 2);
        }
    };

    private OnClickListener mRemoveTwoChildrenClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            List<String> childList = mChildren.get(mGroups.size() - 1);
            int childSize = childList.size();
            if (childSize < 1) {
                return;
            }

            childList.remove(childSize - 1);
            if (childSize < 2) {
                mExpandableAdapter.notifyChildItemRemoved(mGroups.size() - 1, childSize - 1);
                return;
            }
            childList.remove(childSize - 2);
            mExpandableAdapter.notifyChildItemRangeRemoved(mGroups.size() - 1, childSize - 2, 2);
        }
    };

    private OnClickListener mModifyTwoChildrenClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mGroups.size() < 1) {
                return;
            }

            int parentNumber = mGroups.size() - 1;
            List<String> childList = mChildren.get(parentNumber);
            if (childList.size() == 0) {
                return;
            }

            int childNumber = childList.size() - 1;
            String horizontalChild = getString(R.string.modified_child_text, childNumber);
            childList.set(childNumber, horizontalChild);

            if (childList.size() == 1) {
                mExpandableAdapter.notifyChildItemChanged(parentNumber, childNumber);
                return;
            }

            childNumber = childList.size() - 2;
            horizontalChild = getString(R.string.modified_child_text, childNumber);
            childList.set(childNumber, horizontalChild);

            mExpandableAdapter.notifyChildItemRangeChanged(parentNumber, childNumber, 2);
        }
    };

    private OnClickListener mRemoveTwoParentsClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mGroups.size() < 1) {
                return;
            }
            int index = mGroups.size() - 1;
            mGroups.remove(index);
            mChildren.remove(index);
            if (mGroups.size() < 1) {
                mExpandableAdapter.notifyGroupItemRemoved(index);
                return;
            }
            mGroups.remove(index - 1);
            mChildren.remove(index - 1);

            mExpandableAdapter.notifyGroupItemRangeRemoved(index - 1, 2);
        }
    };

    private OnClickListener mModifyTwoParentsClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mGroups.size() < 1) {
                return;
            }

            int parentNumber = mGroups.size() - 1;
            String newHorizontalParent = getString(R.string.modified_parent_text, parentNumber);
            int childSize = mChildren.get(parentNumber).size();
            List<String> childItemList = new ArrayList<>();
            for (int i = 0; i < childSize; i++) {
                String horizontalChild = getString(R.string.modified_child_text, i);
                childItemList.add(horizontalChild);
            }
            mChildren.set(parentNumber, childItemList);
            mGroups.set(parentNumber, newHorizontalParent);
            if (mGroups.size() < 2) {
                mExpandableAdapter.notifyGroupItemChanged(parentNumber);
                return;
            }

            parentNumber = mGroups.size() - 2;
            newHorizontalParent = getString(R.string.modified_parent_text, parentNumber);
            childSize = mChildren.get(parentNumber).size();
            childItemList = new ArrayList<>();
            for (int i = 0; i < childSize; i++) {
                String horizontalChild = getString(R.string.modified_child_text, i);
                childItemList.add(horizontalChild);
            }
            mGroups.set(parentNumber, newHorizontalParent);
            mChildren.set(parentNumber, childItemList);
            mExpandableAdapter.notifyGroupItemRangeChanged(parentNumber, 2);
        }
    };

    private OnClickListener mParentMoveClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mGroups.size() < 4) {
                return;
            }

            String horizontalParent = mGroups.remove(1);
            List<String> childList = mChildren.remove(1);
            mGroups.add(3, horizontalParent);
            mChildren.add(3, childList);
            mExpandableAdapter.notifyGroupItemMoved(1, 3);
        }
    };

    private OnClickListener mChildMoveClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mGroups.size() < 2) {
                return;
            }

            List<String> childList = mChildren.get(1);
            if (childList.size() < 2) {
                return;
            }
            String child = childList.remove(0);
            childList.add(childList.size(), child);
            mExpandableAdapter.notifyChildItemMoved(1, 0, 1, childList.size() - 1);
        }
    };

    private void setUpTestData(int numItems) {
        mGroups.clear();
        mChildren.clear();
        for (int i = 0; i < numItems; i++) {
            List<String> childItemList = new ArrayList<>();

            childItemList.add(getString(R.string.child_text, i));

            // Evens get 2 children, odds get 1
            if (i % 2 == 0) {
                childItemList.add(getString(R.string.second_child_text, i));
            }

            String horizontalParent = getString(R.string.parent_text, i);
            mGroups.add(horizontalParent);
            mChildren.add(childItemList);
        }
    }
}
