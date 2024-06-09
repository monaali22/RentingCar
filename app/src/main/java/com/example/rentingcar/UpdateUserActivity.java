package com.example.rentingcar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserActivity extends BaseActivity {

    private EditText editTextID, editTextUsername, editTextEmail, editTextAddress, editTextPassword;
    private Button buttonUpdate;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_update_user, findViewById(R.id.content_frame));

        editTextID = findViewById(R.id.editTextID);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating User...");

        populateUserInfo();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }

    private void populateUserInfo() {
        SharedPrefUserManager sharedPrefUserManager = SharedPrefUserManager.getInstance(this);

        String userId = sharedPrefUserManager.getUserID();
        String username = sharedPrefUserManager.getUsername();
        String email = sharedPrefUserManager.getEmail();
        String address = sharedPrefUserManager.getAddress();
        String password = sharedPrefUserManager.getPassword();

        editTextID.setText(userId);
        editTextUsername.setText(username);
        editTextEmail.setText(email);
        editTextAddress.setText(address);
        editTextPassword.setText(password);
    }

    private void updateUser() {
        String updatedID = editTextID.getText().toString().trim();
        String updatedUsername = editTextUsername.getText().toString().trim();
        String updatedEmail = editTextEmail.getText().toString().trim();
        String updatedAddress = editTextAddress.getText().toString().trim();
        String updatedPassword = editTextPassword.getText().toString().trim();

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UConstants.URL_Update,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");
                            if (!error) {
                                // Update successful
                                showDialog("Success", message);
                            } else {
                                // Update failed
                                showDialog("Error", message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showDialog("Error", "JSON Parsing Error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        showDialog("Error", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID", updatedID);
                params.put("userName", updatedUsername);
                params.put("Email", updatedEmail);
                params.put("Address", updatedAddress);
                params.put("Password", updatedPassword);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
