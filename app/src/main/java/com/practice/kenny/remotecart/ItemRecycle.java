package com.practice.kenny.remotecart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ItemRecycle extends RecyclerView.Adapter<ItemRecycle.ViewHolder> {
        private List<Items> values;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView txtHeader;
            public TextView txtFooter;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                txtHeader = v.findViewById(R.id.firstLine);
                txtFooter = v.findViewById(R.id.secondLine);
            }
        }

        public void add(int position, Items item) {
            values.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(int position) {
            values.remove(position);
            notifyItemRemoved(position);
        }

        // Provide a suitable constructor (depends on the kind of dataset)
    public ItemRecycle(List<Items> myDataset) {
            values = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ItemRecycle.ViewHolder onCreateViewHolder(ViewGroup parent,
        int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v =
                    inflater.inflate(R.layout.row_layout, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ItemRecycle.ViewHolder vh = new ItemRecycle.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ItemRecycle.ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final String name = values.get(position).getItemName();
            final String footer = values.get(position).getItemPrice();
            holder.txtHeader.setText(name);
            holder.txtHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ItemInfo.class);
                    DBAdapter mDbAdapter = new DBAdapter(v.getContext());
                    mDbAdapter.createDatabase();
                    mDbAdapter.open();
                    String itemID = String.valueOf(values.get(position).getItemID());
                    String storeID = String.valueOf(values.get(position).getStoreID());
                    String itemName = String.valueOf(values.get(position).getItemName());
                    String itemPrice = String.valueOf(values.get(position).getItemPrice());
                    intent.putExtra("ItemID", itemID);
                    intent.putExtra("StoreID", storeID);
                    intent.putExtra("ItemName", itemName);
                    intent.putExtra("ItemPrice", itemPrice);
                    context.startActivity(intent);
                    mDbAdapter.close();
                }
            });

            holder.txtFooter.setText("$"+footer);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return values.size();
        }

    }
