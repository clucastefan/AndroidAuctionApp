package com.example.auctionapp.CustomAdapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.FirebaseDatabase.Tabele.Product;
import com.example.auctionapp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/");
    private OnCardClickListener listener;



    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvDen.setText(product.getDenumire());
        holder.tvPret.setText(product.getPret());
        holder.tvDesc.setText(product.getDescriere());
        Glide.with(context)
                .load(productList.get(position).getImgUrl())
                .placeholder(R.mipmap.ico)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView tvDen,tvPret,tvDesc;
        public ImageView imageView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDen = itemView.findViewById(R.id.ios_prod_tv_den);
            tvPret = itemView.findViewById(R.id.ios_prod_tv_pret);
            tvDesc = itemView.findViewById(R.id.ios_prod_tv_desc);
            imageView = itemView.findViewById(R.id.ios_prod_imageView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            //////////////
            //////////////

        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    listener.onCardClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.setHeaderTitle("AAAAAAAAAAAAAAAAA");
            MenuItem action_1 = menu.add(Menu.NONE,1,1,"Add to auction");
            MenuItem action_delete = menu.add(Menu.NONE,2,2,"Delete");
            MenuItem action_update = menu.add(Menu.NONE,3,3,"Update");

            action_1.setOnMenuItemClickListener(this);
            action_delete.setOnMenuItemClickListener(this);
            action_update.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            listener.onAuctionClick(position);
                            return true;
                        case 2:
                            listener.onDeleteClick(position);
                            return true;
                        case 3:
                            listener.onUpdateClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnCardClickListener{
        void onCardClick(int position);
        void onAuctionClick(int position);
        void onDeleteClick(int position);
        void onUpdateClick(int position);
    }
    public void setOnCardClickListener(OnCardClickListener listener){
        this.listener=listener;
    }
    ////////////////////////////////
    ////////////////////////////////
}
