package com.example.rentingcar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private Context context;
    private List<CarAdmin> cars;
    private static final String DELETE_CAR_URL = "http://192.168.56.1/Android/V1/deleteCars.php";
    private RequestQueue requestQueue;

    public CarAdapter(Context context, List<CarAdmin> cars) {
        this.context = context;
        this.cars = cars;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final CarAdmin car = cars.get(position);
        CardView c = holder.c;
        ImageView image = c.findViewById(R.id.carImageView);
        Glide.with(context).load(car.getImage_url()).into(image);

        TextView txt = c.findViewById(R.id.carIdTextView);
        txt.setText(String.valueOf(car.getId()));

        TextView carNameTextView = c.findViewById(R.id.carModelTextView);
        carNameTextView.setText(car.getModel());

        TextView descriptionTextView = c.findViewById(R.id.carDescriptionTextView);
        descriptionTextView.setText(car.getDescription());

        TextView priceTextView = c.findViewById(R.id.carPriceTextView);
        priceTextView.setText(String.valueOf(car.getPrice()));

        TextView stateTextView = c.findViewById(R.id.carRatingTextView);
        stateTextView.setText(String.valueOf(car.getRating()));

        TextView numTextView = c.findViewById(R.id.carNumAvailableTextView);
        numTextView.setText(String.valueOf(car.getNum_available()));
        TextView dateTextView = c.findViewById(R.id.carMakeDateTextView);
        numTextView.setText(String.valueOf(car.getMake_date()));


        ImageButton deleteButton = c.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the car ID
                int carId = car.getId();
                // Send POST request to delete the car
                deleteCar(carId, position);
            }
        });
        ImageButton updateButton = c.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the UpdateActivity
                Intent intent = new Intent(context, Update.class);
                context.startActivity(intent);

            }
        });


        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* openUpdateCarActivity(car);*/
            }
        });
    }
    private void deleteCar(int carId, final int position) {
        // Create a StringRequest to send a POST request to the PHP script
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_CAR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse the response JSON
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");
                            // Check if deletion was successful
                            if (!error) {
                                // Get the car at the specified position
                                CarAdmin car = cars.get(position);
                                // Decrement the number of cars for the current Car object
                                car.setNum_available(car.getNum_available() - 1);
                                // If the number of cars is now 0, set it to 0 and remove the car from the list
                                if (car.getNum_available() == 0) {
                                    cars.remove(position);
                                    // Notify adapter about the removal
                                    notifyItemRemoved(position);
                                    // Optionally, notifyItemRangeChanged to refresh subsequent items
                                    notifyItemRangeChanged(position, cars.size());
                                } else {
                                    // Notify adapter about the change
                                    notifyItemChanged(position);
                                }
                                // Show a toast message
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            } else {
                                // Show error message
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Show error message
                        Toast.makeText(context, "Error deleting car", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Set POST parameters (carId)
                Map<String, String> params = new HashMap<>();
                params.put("carId", String.valueOf(carId));
                return params;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public void reduceCarCount(int position) {
        CarAdmin car = cars.get(position);
        if (car.getNum_available() > 1) {
            car.setNum_available(car.getNum_available() - 1);
            notifyItemChanged(position);
            Toast.makeText(context, "Car has been deleted", Toast.LENGTH_SHORT).show();
        } else {
            cars.remove(position);
            notifyItemRemoved(position);
            // Optionally, notifyItemRangeChanged to refresh subsequent items
            notifyItemRangeChanged(position, cars.size());
        }
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        public CardView c;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            c = (CardView) itemView;
            this.c = c; // Initialize the CardView
        }
    }

}
