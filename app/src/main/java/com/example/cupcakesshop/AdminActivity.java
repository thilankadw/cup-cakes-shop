package com.example.cupcakesshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.cupcakesshop.databinding.ActivityAdminBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button logoutbtn;

    @NonNull
    ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Use binding.getRoot() here

        auth = FirebaseAuth.getInstance();
        logoutbtn = findViewById(R.id.logooutbtn);

        // Set up the logout button listener
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        replaceFragment(new AdminOrderFragment());

        binding.adminbottomnavigationview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.navigation_order) {
                    selectedFragment = new AdminOrderFragment();
                } else if (item.getItemId() == R.id.navigation_category) {
                    selectedFragment = new AdminCategoryFragment();
                } else if (item.getItemId() == R.id.navigation_cupcake) {
                    selectedFragment = new AdminCupcakeFragment();
                }
                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.admin_fragment_container, fragment)
                .commit();
    }

    private void signOut() {
        auth.signOut();
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
