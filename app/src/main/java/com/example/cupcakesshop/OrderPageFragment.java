package com.example.cupcakesshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderPageFragment extends Fragment {

    private static final String ARG_CUPCAKE = "cupcake";

    private TextView totalPrice;
    private float productPrice;
    private Cupcake cupcake;
    private EditText quantityInput, cardNumberInput, cardholderNameInput, expirationDateInput, cvvInput;

    public static OrderPageFragment newInstance(Cupcake cupcake) {
        OrderPageFragment fragment = new OrderPageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CUPCAKE, cupcake);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_page, container, false);

        if (getArguments() != null) {
            cupcake = (Cupcake) getArguments().getSerializable(ARG_CUPCAKE);
            productPrice = cupcake.getPrice();
        }

        TextView productName = view.findViewById(R.id.product_name);
        TextView productPriceView = view.findViewById(R.id.product_price);
        quantityInput = view.findViewById(R.id.quantity_input);
        totalPrice = view.findViewById(R.id.total_price);
        Button navigateToPayment = view.findViewById(R.id.navigate_to_payment);

        // Card details input fields
        cardNumberInput = view.findViewById(R.id.card_number_input);
        cardholderNameInput = view.findViewById(R.id.cardholder_name_input);
        expirationDateInput = view.findViewById(R.id.expiration_date_input);
        cvvInput = view.findViewById(R.id.cvv_input);

        // Set product details
        productName.setText(cupcake.getName());
        productPriceView.setText("$" + productPrice);

        // Calculate total price when quantity changes
        quantityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotalPrice(s.toString());
            }
        });

        // Navigate to payment page
        navigateToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });

        return view;
    }

    private void calculateTotalPrice(String quantityStr) {
        int quantity = quantityStr.isEmpty() ? 0 : Integer.parseInt(quantityStr);
        float total = quantity * productPrice;
        totalPrice.setText("$" + String.format("%.2f", total));
    }

    private void placeOrder() {
        String quantityStr = quantityInput.getText().toString().trim();
        String cardNumber = cardNumberInput.getText().toString().trim();
        String cardholderName = cardholderNameInput.getText().toString().trim();
        String expirationDate = expirationDateInput.getText().toString().trim();
        String cvv = cvvInput.getText().toString().trim();

        if (quantityStr.isEmpty()) {
            quantityInput.setError("Quantity cannot be empty.");
            return;
        }
        if (cardNumber.isEmpty()) {
            cardNumberInput.setError("Card number cannot be empty.");
            return;
        }
        if (cardholderName.isEmpty()) {
            cardholderNameInput.setError("Cardholder name cannot be empty.");
            return;
        }
        if (expirationDate.isEmpty()) {
            expirationDateInput.setError("Expiration date cannot be empty.");
            return;
        }
        if (cvv.isEmpty()) {
            cvvInput.setError("CVV cannot be empty.");
            return;
        }

        int quantity = Integer.parseInt(quantityStr);
        float price = quantity * productPrice;

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        String orderId = ordersRef.push().getKey();
        String status = "Pending";

        // Get the current user ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Order order = new Order(
                orderId,
                cupcake.getName(),
                cupcake.getCategory(),
                quantity,
                price,
                status,
                userId
        );

        if (orderId != null) {
            ordersRef.child(orderId).setValue(order).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Order placed successfully", Toast.LENGTH_SHORT).show();
                    ((HomeActivity) getActivity()).replaceWithHomeFragment();
                } else {
                    Toast.makeText(getActivity(), "Failed to place order", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
