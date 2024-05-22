package com.example.cupcakesshop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryPageFragment extends Fragment {

    private static final String TAG = "CategoryPageFragment";
    private static final String ARG_CATEGORY = "category";
    private Category category;
    private List<Cupcake> cupcakes;
    private CupcakeAdapter adapter;
    private DatabaseReference databaseCupcakes;

    public static CategoryPageFragment newInstance(Category category) {
        CategoryPageFragment fragment = new CategoryPageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = (Category) getArguments().getSerializable(ARG_CATEGORY);
            databaseCupcakes = FirebaseDatabase.getInstance().getReference("cupcakes");
            initializeCupcakes(category.getName());
        } else {
            Log.e(TAG, "onCreate: getArguments() is null");
        }
    }

    private void initializeCupcakes(String selectedCategory) {
        cupcakes = new ArrayList<>();
        if (databaseCupcakes != null) {
            databaseCupcakes.orderByChild("category").equalTo(selectedCategory)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            cupcakes.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Cupcake cupcake = snapshot.getValue(Cupcake.class);
                                if (cupcake != null) {
                                    cupcake.setImageUrl(snapshot.child("imageUrl").getValue(String.class));
                                    cupcakes.add(cupcake);
                                }
                            }
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle possible errors.
                        }
                    });
        } else {
            Log.e(TAG, "initializeCupcakes: DatabaseReference is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_page, container, false);

        TextView categoryTitle = view.findViewById(R.id.category_title);
        categoryTitle.setText(category.getName() + " Cupcakes");

        RecyclerView recyclerView = view.findViewById(R.id.cupcakesRecyclerView);
        adapter = new CupcakeAdapter(cupcakes, cupcake -> {
            // Navigate to the order page with the selected cupcake details
            OrderPageFragment orderPageFragment = OrderPageFragment.newInstance(cupcake);
            ((HomeActivity) requireActivity()).replaceOrderPageFragment(orderPageFragment);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
