package com.example.cupcakesshop;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class AdminCategoryFragment extends Fragment {

    private RecyclerView adminCategoryRecyclerView;
    private AdminCategoryListAdapter adminCategoryAdapter;
    private List<Category> categoryList;
    private EditText categoryname;
    private Button addcategorybtn;
    private DatabaseReference databaseCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_category, container, false);

        databaseCategories = FirebaseDatabase.getInstance().getReference("categories");

        // Initialize the RecyclerView
        adminCategoryRecyclerView = view.findViewById(R.id.adminCategoryRecyclerView);
        adminCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the category list and adapter
        categoryList = new ArrayList<>();
        adminCategoryAdapter = new AdminCategoryListAdapter(categoryList);
        adminCategoryRecyclerView.setAdapter(adminCategoryAdapter);

        // Initialize EditText and Button for adding new categories
        categoryname = view.findViewById(R.id.categoryname);
        addcategorybtn = view.findViewById(R.id.addcategorybtn);

        // Load categories from Firebase
        loadCategoriesFromFirebase(databaseCategories, categoryList, adminCategoryAdapter);

        // Add category button click listener
        addcategorybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });

        return view;
    }

    private void loadCategoriesFromFirebase(DatabaseReference databaseReference, List<Category> categories, AdminCategoryListAdapter adapter) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Category category = postSnapshot.getValue(Category.class);
                    if (category != null) {
                        categories.add(category);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCategory() {
        String categoryName = categoryname.getText().toString().trim();
        if (TextUtils.isEmpty(categoryName)) {
            categoryname.setError("Category name cannot be empty");
            return;
        }

        String id = databaseCategories.push().getKey();
        Category category = new Category(categoryName);
        databaseCategories.child(id).setValue(category).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Category added", Toast.LENGTH_SHORT).show();
                categoryname.setText("");
            } else {
                Toast.makeText(getContext(), "Failed to add category", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
