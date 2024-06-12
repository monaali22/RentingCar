package com.example.rentingcar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Update extends BaseActivityAdmin {

    private EditText updateIdEditText;
    private EditText updateModelEditText;
    private EditText updateMakeDateEditText;
    private EditText updatePriceEditText;
    private EditText updateImageEditText;
    private EditText updateDescriptionEditText;
    private EditText updateRatingEditText;
    private EditText updateNumAvailableEditText;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_update_car, findViewById(R.id.content_frame));

        updateIdEditText = findViewById(R.id.updateIdEditText);
        updateModelEditText = findViewById(R.id.updateModelEditText);
        updateMakeDateEditText = findViewById(R.id.updateMakeDateEditText);
        updatePriceEditText = findViewById(R.id.updatePriceEditText);
        updateImageEditText = findViewById(R.id.updateImageEditText);
        updateDescriptionEditText = findViewById(R.id.updateDescriptionEditText);
        updateRatingEditText = findViewById(R.id.updateRatingEditText);
        updateNumAvailableEditText = findViewById(R.id.updateNumAvailableEditText);
        updateButton = findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String updatedId = updateIdEditText.getText().toString();
                final String updatedModel = updateModelEditText.getText().toString();
                final String updatedMakeDate = updateMakeDateEditText.getText().toString();
                final String updatedPrice = updatePriceEditText.getText().toString();
                final String updatedImage = updateImageEditText.getText().toString();
                final String updatedDescription = updateDescriptionEditText.getText().toString();
                final String updatedRating = updateRatingEditText.getText().toString();
                final String updatedNumAvailable = updateNumAvailableEditText.getText().toString();

                String url = "http://192.168.56.1/Android/V1/updateCars.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("JSON Response", response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean error = jsonObject.getBoolean("error");
                                    if (!error) {
                                        Toast.makeText(Update.this, "Record updated successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        String errorMessage = jsonObject.getString("message");
                                        Toast.makeText(Update.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Update.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(Update.this, "Error updating record", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("updatedId", updatedId);
                        params.put("updatedModel", updatedModel);
                        params.put("updatedMakeDate", updatedMakeDate);
                        params.put("updatedPrice", updatedPrice);
                        params.put("updatedImage", updatedImage);
                        params.put("updatedDescription", updatedDescription);
                        params.put("updatedRating", updatedRating);
                        params.put("updatedNumAvailable", updatedNumAvailable);
                        return params;
                    }
                };

                Volley.newRequestQueue(Update.this).add(stringRequest);
            }
        });
    }
}
