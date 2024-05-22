package com.example.cupcakesshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.cupcakesshop.databinding.ActivityAdminBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    @NonNull
    ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Use binding.getRoot() here

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
}
