package com.sms.ui.adapter;

/*
 * sms
 * Created by A.Kolchev  24.2.2016
 */

/*
 * Retro
 * Created by A.Kolchev  21.12.2015
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sms.R;
import com.sms.data.exception.ModelException;
import com.sms.data.model.Route;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private final Context mContext;
    private List<Route> items;

    public RouteAdapter(Context context, List<Route> items) {
        this.mContext = context;
        this.items = items;

    }

    public void setItems(List<Route> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox itemIsDone;
        TextView itemTime;
        TextView itemName;
        TextView itemDescription;

        public ViewHolder(View v, int viewType) {
            super(v);
            itemIsDone = (CheckBox) itemView.findViewById(R.id.trade_point_is_done);
            itemTime = (TextView) itemView.findViewById(R.id.trade_point_time);
            itemName = (TextView) itemView.findViewById(R.id.trade_point_name);
            itemDescription = (TextView) itemView.findViewById(R.id.trade_point_description);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_list_item_view, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "route View clicked", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder = new ViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Route item = items.get(position);
        holder.itemName.setText(item.getTradePoint().getAddress());
        holder.itemDescription.setText(item.getTradePoint().getDescription());
        holder.itemTime.setText(item.getTime());
        holder.itemIsDone.setChecked(item.isDone());
        holder.itemIsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setIsDone(!item.isDone());
                item.setChanged(true);
                try {
                    item.save();
                } catch (ModelException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
