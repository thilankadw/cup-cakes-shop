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
        } else {
            Log.e(TAG, "onCreate: getArguments() is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_page, container, false);

        TextView categoryTitle = view.findViewById(R.id.category_title);
        categoryTitle.setText(category.getName() + " Cupcakes");

        RecyclerView recyclerView = view.findViewById(R.id.cupcakesRecyclerView);
        cupcakes = new ArrayList<>();
        Log.d(TAG, "Cupcake 1 ");
        adapter = new CupcakeAdapter(cupcakes, cupcake -> {
            Log.d(TAG, "Cupcake 2 ");
            // Navigate to the order page with the selected cupcake details
            OrderPageFragment orderPageFragment = OrderPageFragment.newInstance(cupcake);
            Log.d(TAG, "Cupcake 3 ");
            ((HomeActivity) requireActivity()).replaceOrderPageFragment(orderPageFragment);
            Log.d(TAG, "Cupcake 4 ");
        });
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "Cupcake 5 ");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Log.d(TAG, "Cupcake 6 ");
        initializeCupcakes(category.getName());
        Log.d(TAG, "Cupcake 7 ");

        return view;
    }

    private void initializeCupcakes(String selectedCategory) {
        if (databaseCupcakes != null) {
            Log.d(TAG, "Cupcake 8 ");
            databaseCupcakes.orderByChild("category").equalTo(selectedCategory)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d(TAG, "Cupcake 9 ");
                            cupcakes.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.d(TAG, "Cupcake 10 ");
                                Cupcake cupcake = snapshot.getValue(Cupcake.class);
                                if (cupcake != null) {
                                    // Log all attributes of the cupcake
                                    Log.d(TAG, "Cupcake Name: " + cupcake.getName());
                                    Log.d(TAG, "Cupcake Category: " + cupcake.getCategory());
                                    Log.d(TAG, "Cupcake Price: " + cupcake.getPrice());

                                    cupcakes.add(cupcake);
                                }else {
                                    Log.d(TAG, "Cupcake Null ");
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "Database error: " + databaseError.getMessage());
                        }
                    });
        } else {
            Log.e(TAG, "initializeCupcakes: DatabaseReference is null");
        }
    }

}

