package com.example.rentingcar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartMainActivity extends BaseActivity {
    private ListView listViewCars;
    private ProgressDialog progressDialog;
    private List<Car> carList;
    private ArrayAdapter<Car> cartAdapter; // Change to ArrayAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_cart_main, findViewById(R.id.content_frame));


        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Cars...");
        progressDialog.show();

        // Initialize carList
        carList = new ArrayList<>();

        // Initialize listViewCars
        listViewCars = findViewById(R.id.listViewCars);

        // Initialize cartAdapter
        cartAdapter = new ArrayAdapter<Car>(this, R.layout.cart_list_item_layout, R.id.textViewCarModel, carList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Button deleteButton = view.findViewById(R.id.buttonDelete);

                // Handle button click
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Implement cancellation logic here
                        Car carToDelete = getItem(position);

                        Log.d("CarID", "Car ID: " + carToDelete.getId());

                        if (carToDelete != null) {
                            String userIdString = SharedPrefUserManager.getInstance(CartMainActivity.this).getUserID();
                            int userId = Integer.parseInt(userIdString);
                            Log.d("UserID", "User ID: " + userId);

                            showConfirmationDialog(carToDelete, userId, position);
                        }
                    }
                });

                return view;
            }
        };

        // Set adapter to listViewCars
        listViewCars.setAdapter(cartAdapter);

        // Load booked cars
        loadBookedCars();
    }

    private void loadBookedCars() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                UConstants.URL_BOOKED_CARS + "?user_id=" + SharedPrefUserManager.getInstance(this).getUserID(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.dismiss();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String model = jsonObject.getString("model");
                                double price = jsonObject.getDouble("price");
                                int make_date = jsonObject.getInt("make_date");


                                // Create a Car object
                                Car car = new Car(id, model , price , make_date );
                                // Add the car object to the list
                                carList.add(car);
                            }
                            // Notify the adapter of changes
                            cartAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CartMainActivity.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(CartMainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void showConfirmationDialog(Car carToDelete, int userId, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Booking")
                .setMessage("Are you sure you want to delete the booking?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cancelBooking(carToDelete.getId(), userId, position);
                    }
                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void cancelBooking(int car_Id, int user_Id, int position) {
        // Log the parameters being sent
        Log.d("CancelBooking", "Canceling booking for Car ID: " + car_Id + ", User ID: " + user_Id);

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create the URL with parameters
        String url = UConstants.URL_Delete_BOOKED_CARS;

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        // Handle the response from the server
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");
                            showResponseDialog(message);
                            if (success) {
                                // Booking cancellation successful
                                carList.remove(position);
                                cartAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CartMainActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors
                Toast.makeText(CartMainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("car_id", String.valueOf(car_Id));
                params.put("user_id", String.valueOf(user_Id));
                return params;
            }
        };

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }

    private void showResponseDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Response")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}