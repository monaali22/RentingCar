package com.example.rentingcar;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivityAdmin extends BaseActivityAdmin {
    private EditText searchEditText;
    private RecyclerView recyclerView;
    public ArrayList<CarAdmin> carList = new ArrayList<>();
    private CarAdapter adapter;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, findViewById(R.id.content_frame));

        // Initialize views
        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarAdapter(this, carList);
        recyclerView.setAdapter(adapter);

        // Initialize RequestQueue
        queue = Volley.newRequestQueue(this);

        // Fetch data from server
        fetchDataFromServer();

        // Set up the search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    searchCars(query);
                } else {
                    carList.clear(); // Clear the list if the search query is empty
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchDataFromServer() {
        String url = "http://192.168.56.1/Android/V1/readCars.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("ID");
                                String carName = jsonObject.getString("carName");
                                String description = jsonObject.getString("Description");
                                double price = jsonObject.getDouble("Price");
                                int state = jsonObject.getInt("State");
                                String imageUrl = jsonObject.getString("imageUrl");
                                int numberOfCars = jsonObject.getInt("numberOfCars");

                                CarAdmin car = new CarAdmin(id, carName, description, price, state, imageUrl, numberOfCars);
                                carList.add(car);
                            }
                            adapter.notifyDataSetChanged(); // Notify adapter about data change
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }

    private void searchCars(String query) {
        String url = "http://192.168.56.1/Android/V1/searchCars.php?query=" + query;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        carList.clear(); // Clear existing data
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("ID");
                                String carName = jsonObject.getString("carName");
                                String description = jsonObject.getString("Description");
                                double price = jsonObject.getDouble("Price");
                                int state = jsonObject.getInt("State");
                                String imageUrl = jsonObject.getString("imageUrl");
                                int numberOfCars = jsonObject.getInt("numberOfCars");

                                CarAdmin car = new CarAdmin(id, carName, description, price, state, imageUrl, numberOfCars);
                                carList.add(car);
                            }
                            adapter.notifyDataSetChanged(); // Notify adapter about data change
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }
}
