package com.example.cupcakesshop;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class AdminCupcakeListAdapter extends RecyclerView.Adapter<AdminCupcakeListAdapter.CupcakeViewHolder> {

    private List<Cupcake> cupcakes;


    public AdminCupcakeListAdapter(List<Cupcake> cupcakes) {
        this.cupcakes = cupcakes;
    }

    @NonNull
    @Override
    public CupcakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cupcake, parent, false);
        return new CupcakeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CupcakeViewHolder holder, int position) {
        Cupcake cupcake = cupcakes.get(position);
        holder.bind(cupcake);
    }

    @Override
    public int getItemCount() {
        return cupcakes.size();
    }

    public static class CupcakeViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;
        private ImageView image;

        public CupcakeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
        }

        public void bind(Cupcake cupcake) {
            name.setText(cupcake.getName());
            price.setText(cupcake.getPrice());
            Picasso.get().load(Uri.parse((String) cupcake.getImageUrl())).into(image);
        }
    }
}
