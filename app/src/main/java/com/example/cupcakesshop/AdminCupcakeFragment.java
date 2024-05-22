package com.example.cupcakesshop;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminCupcakeFragment extends Fragment {

    private ArrayList<Cupcake> cupcakes = new ArrayList<>();

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private EditText categoryname;
    private EditText price;
    private Spinner categorySpinner;
    private ImageView cupcakeImageView;
    private Button selectImageButton;
    private Button addcategorybtn;
    private AdminCupcakeListAdapter adapter;
    private DatabaseReference databaseCategories;
    private DatabaseReference databaseCupcakes;
    private StorageReference storageReference;


    public AdminCupcakeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }

        databaseCategories = FirebaseDatabase.getInstance().getReference("categories");
        storageReference = FirebaseStorage.getInstance().getReference("cupcakes");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_cupcake, container, false);

        categoryname = view.findViewById(R.id.categoryname);
        price = view.findViewById(R.id.price);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        addcategorybtn = view.findViewById(R.id.addcategorybtn);

        loadCategoriesIntoSpinner();



        addcategorybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCupcake();
            }
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
    }


    private void loadCupcakesFromFirebase(DatabaseReference databaseReference, List<Cupcake> cupcakes, AdminCupcakeListAdapter adapter) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cupcakes.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Cupcake cupcake = postSnapshot.getValue(Cupcake.class);
                    if (cupcake != null) {
                        cupcakes.add(cupcake);
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


    private void loadCategoriesIntoSpinner() {
        databaseCategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> categories = new ArrayList<>();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryName = categorySnapshot.child("name").getValue(String.class);
                    categories.add(categoryName);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadCupcake() {
        final String name = categoryname.getText().toString().trim();
        final String priceValue = price.getText().toString().trim();
        final String category = categorySpinner.getSelectedItem().toString();

        if (name.isEmpty() || priceValue.isEmpty()) {
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = databaseCupcakes.push().getKey();
        Cupcake cupcake = new Cupcake(name, category, Float.parseFloat(priceValue));
        databaseCupcakes.child(id).setValue(cupcake).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Cupcake added", Toast.LENGTH_SHORT).show();
                categoryname.setText("");
                price.setText("");
                categorySpinner.setSelection(0);
            } else {
                Toast.makeText(getContext(), "Failed to add category", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
