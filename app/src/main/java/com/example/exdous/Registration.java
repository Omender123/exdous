package com.example.exdous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
TextView Login,responsess;
EditText name,email,username,password,referralCode,countryCode,phone,country;
Button resgister_button;
KProgressHUD progressDialog;
    String url="http://13.233.136.56:8080/api/user/register";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Login=findViewById(R.id.txtLogin);
        responsess=findViewById(R.id.respones);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
        name=findViewById(R.id.ed_name);
        email=findViewById(R.id.ed_email);
        username=findViewById(R.id.ed_username);
        password=findViewById(R.id.ed_password);
        referralCode=findViewById(R.id.ed_referral_code);
        countryCode=findViewById(R.id.country);
        phone=findViewById(R.id.phone);
        country=findViewById(R.id.country);
        resgister_button=findViewById(R.id.btn_register);

        resgister_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               signup();
            }
        });

    }

    public void signup() {

        if (validate() == false) {
            onSignupFailed();
            return;
        }
        saveToServerDB();
    }

    public void onSignupSuccess() {
        resgister_button.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Please fill all requirement ", Toast.LENGTH_LONG).show();

        resgister_button.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String names = name.getText().toString().trim();
        String emails = email.getText().toString().trim();
        String mobile = phone.getText().toString().trim();
        String passwords = password.getText().toString().trim();
        String usernames = username.getText().toString().trim();
        String refercode=referralCode.getText().toString().trim();
        String country_name=country.getText().toString().trim();
        String country_Code=countryCode.getText().toString().trim();
        if (names.isEmpty()) {
            name.setError("Please enter name");
            requestFocus(name);
            valid = false;
        } else {
            name.setError(null);
        }


        if (emails.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emails).matches()) {
            email.setError("enter a valid email address");
            requestFocus(email);
            valid = false;
        } else {
            email.setError(null);
        }
        if (mobile.isEmpty() || mobile.length()<10) {
            phone.setError("enter a Mobile");
            requestFocus(phone);
            valid = false;
        } else {
           phone.setError(null);
        }

        if (passwords.isEmpty() || passwords.length() < 4 || passwords.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            requestFocus(password);
            valid = false;
        } else {
            password.setError(null);
        }

        if (usernames.isEmpty()) {
            username.setError("Please enter username");
            requestFocus(username);
            valid = false;
        } else {
            username.setError(null);
        }
        if (refercode.isEmpty()) {
            referralCode.setError("Please enter Referral Code");
            requestFocus(referralCode);
            valid = false;
        } else {
            referralCode.setError(null);
        }
        if (country_Code.isEmpty()) {
            countryCode.setError("Please enter Country Code");
            requestFocus(countryCode);
            valid = false;
        } else {
            countryCode.setError(null);
        }

        if (country_name.isEmpty()) {
            country.setError("Please enter Country Name");
            requestFocus(country);
            valid = false;
        } else {
            country.setError(null);
        }

        return valid;
    }

    private void saveToServerDB() {

        String names = name.getText().toString().trim();
        String emails = email.getText().toString().trim();
        String mobile = phone.getText().toString().trim();
        String passwords = password.getText().toString().trim();
        String usernames = username.getText().toString().trim();
        String refercode=referralCode.getText().toString().trim();
        String country_name=country.getText().toString().trim();
        String country_Code=countryCode.getText().toString().trim();

        progressDialog = KProgressHUD.create(Registration.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                responsess.setText(response);
                Toast.makeText(Registration.this, ""+response, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
               Toast.makeText(getBaseContext(), "Your Email and Username is all ready exits", Toast.LENGTH_LONG).show();
                responsess.setText(error.toString());
             //  Toast.makeText(Registration.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name",names);
                params.put("email",emails);
                params.put("username",usernames);
                params.put("password",passwords);
                params.put("referalCode",refercode);
                params.put("countryCode",country_Code);
                params.put("phone",mobile);
                params.put("country",country_name);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                // headers.put("Authorization", "Bearer "+Token);

                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

       }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}