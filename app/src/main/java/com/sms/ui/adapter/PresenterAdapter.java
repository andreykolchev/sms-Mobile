package com.sms.ui.adapter;

/*
 * sms
 * Created by A.Kolchev  9.3.2016
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sms.Constants;
import com.sms.R;
import com.sms.data.model.Presenter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PresenterAdapter extends RecyclerView.Adapter<PresenterAdapter.ViewHolder> {

    public final String TAG = getClass().getSimpleName();
    private final Context mContext;
    private List<Presenter> items;

    public PresenterAdapter(Context context, List<Presenter> items) {
        this.mContext = context;
        this.items = items;
    }

    public void setItems(List<Presenter> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView itemName;
        TextView itemDescription;
        Button itemButton;

        public ViewHolder(View v, int viewType) {
            super(v);
            imageView = (ImageView) itemView.findViewById(R.id.item_list_image);
            itemName = (TextView) itemView.findViewById(R.id.item_list_name);
            itemDescription = (TextView) itemView.findViewById(R.id.item_list_description);
            itemButton = (Button) itemView.findViewById(R.id.item_list_button);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.presenter_list_item_view, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "item View clicked", Toast.LENGTH_SHORT).show();
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

        final Presenter item = items.get(position);
        holder.itemName.setText(item.getItem().getDescription());
        holder.itemDescription.setText(item.getItem().getCategory().getName());
        holder.itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "item button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        Picasso.with(mContext).load(Constants.IMAGE_URL + item.getImageURI())
                .error(R.drawable.ic_no_image)
                .placeholder(R.drawable.ic_no_image)
                .resize(45, 45)
                .into(holder.imageView);

//        Picasso.Builder picassoBuilder = new Picasso.Builder(mContext);
//        picassoBuilder.listener(new Picasso.Listener()
//        {
//            @Override
//            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//               exception.printStackTrace();
//            }
//
//        });
//        picassoBuilder.build().load(Constants.IMAGE_URL + item.getImageURI()).into(holder.imageView);

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
