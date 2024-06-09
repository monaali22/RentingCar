package com.example.rentingcar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextID , editTextPassword ;
    private Button buttonLogin , buttonregester;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login);
        editTextID = (EditText) findViewById(R.id.userLoginID);
        editTextPassword = (EditText) findViewById(R.id.userLoginPassword);
        buttonLogin = (Button) findViewById(R.id.UserButtnLogin);
        buttonregester = (Button) findViewById(R.id.butUserRegesterActivity);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait.....");
        buttonLogin.setOnClickListener(this);
        buttonregester.setOnClickListener(this);


    }

    private void showMessageDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close the dialog
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void userLogin() {
        final String ID = editTextID.getText().toString().trim();
        final String Password = editTextPassword.getText().toString().trim();

        if (ID.isEmpty() || Password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, UConstants.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("LoginResponse", "Response: " + response); // Log the entire response

                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")) {
                        // Extract values safely
                        String userID = obj.getString("ID");
                        String userName = obj.getString("userName");
                        String userEmail = obj.getString("Email"); // Add this line to retrieve email
                        String userAddress = obj.getString("Address"); // Add this line to retrieve address
                        String userPassword = Password; // Password isn't returned by the server, use the entered one

                        // Save user details in shared preferences
                        SharedPrefUserManager.getInstance(getApplicationContext()).userLogin(
                                userID,
                                userName,
                                userEmail, // Add email parameter
                                userAddress, // Add address parameter
                                userPassword
                        );

                        Toast.makeText(getApplicationContext(), "User login successful", Toast.LENGTH_LONG).show();

                        // Navigate to the main activity or dashboard
                        startActivity(new Intent(UserLoginActivity.this, UserActivity.class));
                        finish(); // Finish the current activity to prevent going back to login screen
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("LoginError", "JSON Parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "JSON Parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        String errorMessage = "Login Error: " + volleyError.toString();
                        if (volleyError.networkResponse != null) {
                            errorMessage += "\nStatus Code: " + volleyError.networkResponse.statusCode;
                            try {
                                errorMessage += "\nResponse Data: " + new String(volleyError.networkResponse.data, "UTF-8");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        volleyError.printStackTrace();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID", ID);
                params.put("Password", Password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin) {
            userLogin();
        } else if (v == buttonregester) {
            startActivity(new Intent(this, UserRegistrationActivity.class));
        }
    }
}