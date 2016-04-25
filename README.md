# Expandable RecyclerView

A custom RecyclerView which allows for an expandable view to be attached to each ViewHolder

# Usage

Only Need extends <b>BaseExpandableRecyclerViewAdapter</b> like Android's Expandable List View.


    public class ExpandableAdapter extends BaseExpandableRecyclerViewAdapter {
      @Override
      public int getGroupCount() {
        return 0;
      }

      @Override
      public int getChildrenCount(int groupPosition) {
        return 0;
      }

      @Override
      public Object getGroup(int groupPosition) {
        return null;
      }

      @Override
      public Object getChild(int groupPosition, int childPosition) {
        return null;
      }

      @Override
      public ViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return null;
      }

      @Override
      public ViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return null;
      }

      @Override
      public void onBindGroupViewHolder(ViewHolder groupHolder, int groupPosition) {
      }

      @Override
      public void onBindChildViewHolder(ViewHolder childHolder, int groupPosition, int childPosition) {
      }

And can add Expand Listener or toggle Expand by the Adapter Object.

