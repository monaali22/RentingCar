package com.example.rentingcar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Add_AdminActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextID, editTextUsername, editTextEmail, editTextAddress, editTextPassword;
    private Button buttonRegister;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_admin);

        // Initialize the EditText fields
        editTextID = findViewById(R.id.adminSignupID);
        editTextUsername = findViewById(R.id.AdminSignupName);
        editTextEmail = findViewById(R.id.AdminSignupEmail);
        editTextAddress = findViewById(R.id.AdminSignupAddress);
        editTextPassword = findViewById(R.id.AdminSignupPassword);

        // Initialize the Button and ensure it is cast correctly
        buttonRegister = findViewById(R.id.button);

        // Initialize the ProgressDialog
        progressDialog = new ProgressDialog(this);

        // Set the click listener for the Button
        buttonRegister.setOnClickListener(this);

        // Restore saved state if available
        if (savedInstanceState != null) {
            editTextID.setText(savedInstanceState.getString("ID"));
            editTextUsername.setText(savedInstanceState.getString("userName"));
            editTextEmail.setText(savedInstanceState.getString("email"));
            editTextAddress.setText(savedInstanceState.getString("address"));
            editTextPassword.setText(savedInstanceState.getString("password"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current state of the EditText fields
        outState.putString("ID", editTextID.getText().toString().trim());
        outState.putString("userName", editTextUsername.getText().toString().trim());
        outState.putString("email", editTextEmail.getText().toString().trim());
        outState.putString("address", editTextAddress.getText().toString().trim());
        outState.putString("password", editTextPassword.getText().toString().trim());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore the saved state of the EditText fields
            editTextID.setText(savedInstanceState.getString("ID"));
            editTextUsername.setText(savedInstanceState.getString("userName"));
            editTextEmail.setText(savedInstanceState.getString("email"));
            editTextAddress.setText(savedInstanceState.getString("address"));
            editTextPassword.setText(savedInstanceState.getString("password"));
        }
    }

    private void registerAdmin() {
        final String ID = editTextID.getText().toString().trim();
        final String userName = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String address = editTextAddress.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        progressDialog.setMessage("Registering Admin...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UConstants.URL_REGISTERAdmin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("Response", "Response: " + response); // Add this line to log the response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");

                            // Show the message in an AlertDialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(Add_AdminActivity.this);
                            builder.setMessage(message);
                            builder.setPositiveButton("OK", (dialog, which) -> {
                                if (!error) {
                                    // If registration is successful, open LoginActivity
                                    Intent intent = new Intent(Add_AdminActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Close the registration activity
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "JSON Parsing Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        String errorMessage = "Error: " + error.toString();
                        if (error.networkResponse != null) {
                            errorMessage += "\nStatus Code: " + error.networkResponse.statusCode;
                            try {
                                errorMessage += "\nResponse Data: " + new String(error.networkResponse.data, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID", ID);
                params.put("userName", userName);
                params.put("Email", email);
                params.put("Address", address);
                params.put("Password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister) {
            registerAdmin();
        }
    }


}