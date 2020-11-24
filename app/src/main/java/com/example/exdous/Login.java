package com.example.exdous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {
    TextView signin,responsess;
    EditText username,password;
    Button Login;
    KProgressHUD progressDialog;
    String url="http://13.233.136.56:8080/api/user/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signin=findViewById(R.id.txtsignup);
        responsess=findViewById(R.id.respones);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Registration.class));
            }
        });

        username=findViewById(R.id.ed_username1);
        password=findViewById(R.id.ed_password1);

        Login=findViewById(R.id.btn_login);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        Paper.init(this);

        //Default language is English

        String language=Paper.book().read("language");
        if (language==null)
            Paper.book().write("language","en");

        updateView((String)Paper.book().read("language"));
    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this,language);

       // Resources resources =context.getResources();
     /*   startActivity(getIntent());
        finish();
*/
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
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
       }else  if(item.getItemId()==R.id.eng){
           Paper.book().write("language","hi");
           updateView((String)Paper.book().read("language"));
       }
        return true;
    }

    public void signIn() {
        if (validate() == false) {
            onSignupFailed();
            return;
        }
        saveToServerDB();

    }
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Please fill all requirement ", Toast.LENGTH_LONG).show();

        Login.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

          String passwords = password.getText().toString().trim();
        String usernames = username.getText().toString().trim();

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

        return valid;
    }

    private void saveToServerDB() {

        String passwords = password.getText().toString().trim();
        String usernames = username.getText().toString().trim();

        progressDialog = KProgressHUD.create(Login.this)
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
                Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                Toast.makeText(getBaseContext(), "Some thing is Wrong", Toast.LENGTH_LONG).show();
                responsess.setText(error.toString());
                //  Toast.makeText(Registration.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username",usernames);
                params.put("password",passwords);


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
}