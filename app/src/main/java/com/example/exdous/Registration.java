package com.example.exdous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.exdous.Helper.LocaleHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class Registration extends AppCompatActivity {
    TextInputLayout In_name,In_email,In_username,In_password,In_Referral,In_country_code,In_phone,In_Country;
TextView Login,responsess,textView1;
EditText name,email,username,password,referralCode,countryCode,phone,country;
Button resgister_button;
KProgressHUD progressDialog;
    String url="http://13.233.136.56:8080/api/user/register";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // language
        textView1=findViewById(R.id.textView1);
        In_name=findViewById(R.id.In_name);
        In_email=findViewById(R.id.In_email);
        In_username=findViewById(R.id.In_username);
        In_password=findViewById(R.id.In_password);
        In_Referral=findViewById(R.id.In_Referral);
        In_country_code=findViewById(R.id.In_country_code);
        In_phone=findViewById(R.id.In_phone);
        In_Country=findViewById(R.id.In_Country);



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
        countryCode=findViewById(R.id.country_code);
        phone=findViewById(R.id.phone);
        country=findViewById(R.id.country);
        resgister_button=findViewById(R.id.btn_register);

        resgister_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               signup();
            }
        });
        updateView((String) Paper.book().read("language"));
    }
    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this,language);

        Resources resources =context.getResources();
        In_username.setHint(resources.getString(R.string.username));
        In_password.setHint(resources.getString(R.string.password));
        In_email.setHint(resources.getString(R.string.email));
        In_name.setHint(resources.getString(R.string.name));
        In_Referral.setHint(resources.getString(R.string.referal_code));
        In_country_code.setHint(resources.getString(R.string.country_code));
        In_phone.setHint(resources.getString(R.string.phone));
        In_Country.setHint(resources.getString(R.string.country));
        resgister_button.setText(resources.getString(R.string.registration));
        Login.setText(resources.getString(R.string.login));
        textView1.setHint(resources.getString(R.string.registration));
         getSupportActionBar().setTitle(resources.getString(R.string.app_name));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.eng){
            Paper.book().write("language","en");
            updateView((String)Paper.book().read("language"));
        }else  if(item.getItemId()==R.id.Hindi){
            Paper.book().write("language","hi");
            updateView((String)Paper.book().read("language"));
        }else if(item.getItemId()==R.id.Mandarin_Chinese){
            Paper.book().write("language","mc");
            updateView((String)Paper.book().read("language"));
        }else if(item.getItemId()==R.id.Arabic){
            Paper.book().write("language","ar");
            updateView((String)Paper.book().read("language"));
        }else if(item.getItemId()==R.id.French){
            Paper.book().write("language","fr");
            updateView((String)Paper.book().read("language"));
        }
        else if(item.getItemId()==R.id.Spanish){
            Paper.book().write("language","sp");
            updateView((String)Paper.book().read("language"));
        }
        else if(item.getItemId()==R.id.Russian){
            Paper.book().write("language","ru");
            updateView((String)Paper.book().read("language"));
        }
        else if(item.getItemId()==R.id.Portuguese){
            Paper.book().write("language","pt");
            updateView((String)Paper.book().read("language"));
        }
        else if(item.getItemId()==R.id.Indonesian){
            Paper.book().write("language","in");
            updateView((String)Paper.book().read("language"));
        }else if(item.getItemId()==R.id.Japanese){
            Paper.book().write("language","ja");
            updateView((String)Paper.book().read("language"));
        }
        else if(item.getItemId()==R.id.Urdu){
            Paper.book().write("language","ur");
            updateView((String)Paper.book().read("language"));
        }
        else if(item.getItemId()==R.id.german){
            Paper.book().write("language","gr");
            updateView((String)Paper.book().read("language"));
        }
        else if(item.getItemId()==R.id.Turkish){
            Paper.book().write("language","tr");
            updateView((String)Paper.book().read("language"));
        }
        else if(item.getItemId()==R.id.Vietnamese){
            Paper.book().write("language","vi");
            updateView((String)Paper.book().read("language"));
        }
        else if(item.getItemId()==R.id.Thai){
            Paper.book().write("language","th");
            updateView((String)Paper.book().read("language"));
        }
        return true;
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
    /*    if (refercode.isEmpty()) {
            referralCode.setError("Please enter Referral Code");
            requestFocus(referralCode);
            valid = false;
        } else {
            referralCode.setError(null);
        }*/
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