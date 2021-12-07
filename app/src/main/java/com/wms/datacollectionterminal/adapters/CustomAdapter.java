package com.wms.datacollectionterminal.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wms.datacollectionterminal.R;
import com.wms.datacollectionterminal.entities.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<ProductEntity> data;

    public CustomAdapter() {
        this.data = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView size;
        private final TextView price;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.list_product_name);
            size = view.findViewById(R.id.list_product_size);
            price = view.findViewById(R.id.list_product_price);
        }


        @SuppressLint("SetTextI18n")
        public void bind(ProductEntity productEntity) {
            if (productEntity == null) return;

            this.name.setText("Name: " + productEntity.getProductName());

            this.size.setText("Size: " + productEntity.getWeight() + "x" +
                    productEntity.getHeight() + "x" +
                    productEntity.getLength());

            this.price.setText("Price: " + productEntity.getPrice() + "тг.");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_row_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<ProductEntity> getData() {
        return data;
    }

    public void setData(List<ProductEntity> data) {
        this.data = data;
    }

    public void addRow(ProductEntity product) {
        data.add(product);
//        notifyDataSetChanged();
        notifyItemInserted(data.size() - 1);
    }

    public interface ClickItem{
        void click(ProductEntity productEntity);
    }
}