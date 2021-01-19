package com.example.exdous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exdous.Helper.LocaleHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {
    TextView signin,responsess,textView,to;
    EditText username,password;
    Button Login;
    TextInputLayout usern,pass;
    KProgressHUD progressDialog;
    String url="http://13.233.136.56:8080/api/user/login";


    SwitchCompat switchCompat;
    SharedPreferences sharedPreferences = null;

    String id,Token,locations,ipAddress,os;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(Login.this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                getLocation();

        }else{
            ActivityCompat.requestPermissions(Login.this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}
                    ,44);
        }
        getosName();
        Ipaddress();
        signin = findViewById(R.id.txtsignup);
        responsess = findViewById(R.id.respones);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });

        //language
        usern=findViewById(R.id.user);
        pass=findViewById(R.id.pass);
        textView=findViewById(R.id.textView);
        to=findViewById(R.id.to);
        //input
        username = findViewById(R.id.ed_username1);
        password = findViewById(R.id.ed_password1);
        Login = findViewById(R.id.btn_login);

        switchCompat = findViewById(R.id.switchCompat);


        sharedPreferences = getSharedPreferences("night",0);
        Boolean booleanValue = sharedPreferences.getBoolean("night_mode",true);
        if (booleanValue){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchCompat.setChecked(true);
           // imageView.setImageResource(R.drawable.night);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchCompat.setChecked(true);
                //    imageView.setImageResource(R.drawable.night);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode",true);
                    editor.commit();
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchCompat.setChecked(false);
                 //   imageView.setImageResource(R.drawable.night);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode",false);
                    editor.commit();

                }
            }
        });


        //Toast.makeText(Login.this, ""+id+"\n"+Token+"\n"+os+"\n"+locations+"\n"+ipAddress, Toast.LENGTH_SHORT).show();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                  ApI1();
           // Toast.makeText(Login.this, ""+id+"\n"+Token+"\n"+os+"\n"+locations+"\n"+ipAddress, Toast.LENGTH_SHORT).show();
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

        Resources resources =context.getResources();
        usern.setHint(resources.getString(R.string.username));
        pass.setHint(resources.getString(R.string.password));
        Login.setText(resources.getString(R.string.login));
        signin.setText(resources.getString(R.string.signUp));
        textView.setHint(resources.getString(R.string.welcome));
        to.setHint(resources.getString(R.string.sign_to));
        getSupportActionBar().setTitle(resources.getString(R.string.app_name));

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
              //   responsess.setText(response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                   String result=jsonObject.getString("result");
                   JSONObject jsonObject1=new JSONObject(result);
                   id=jsonObject1.getString("_id");
                   Token =jsonObject.getString("token");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
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
    public void getosName(){
      StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
            os= String.valueOf(builder.append(fieldName));
            //    builder.append("sdk=").append(fieldValue);

            }
        }
    }
    public void Ipaddress(){
        try {
            //permition
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL myIp = new URL("https://checkip.amazonaws.com/");
            URLConnection c = myIp.openConnection();
            c.setConnectTimeout(1000);
            c.setReadTimeout(1000);

            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
           // textView.setText(in.readLine());
            ipAddress=in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                // Initialize Location
                Location location =task.getResult();
                if (location!=null){
                    try {
                        // Initialize geoCoder
                        Geocoder geocoder = new Geocoder(Login.this, Locale.getDefault());
                        // Initialize addressList
                        List<Address> addresses =geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );
                        locations=addresses.get(0).getSubLocality()+","+addresses.get(0).getLocality();
                       /* textView1.setText(Html.fromHtml("<font color='#6200EE'><b>Latitude : </b><br></font>"
                                +addresses.get(0).getLocality()));*//* +addresses.get(0).getLatitude()+"<br>"+"<font color='#6200EE'><b>Longitude : </b><br></font> "
                            +addresses.get(0).getLongitude()));
 *//*               //    Toast.makeText(MainActivity.this, ""+addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
                  */  }catch (Exception e){
                    }

                }
            }
        });
    }

    public void ApI1(){
        String url="http://13.233.136.56:8080/api/user/systemInformation";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // hidepDialog();
             responsess.setText(response);
                 //Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
                Toast.makeText(Login.this, ""+id+"\n"+Token+"\n"+os+"\n"+locations+"\n"+ipAddress, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                hidepDialog();
         //       Toast.makeText(getBaseContext(), "Some thing is Wrong", Toast.LENGTH_LONG).show();
                responsess.setText(error.toString());
                //  Toast.makeText(Registration.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("ip",ipAddress);
                params.put("jwtToken",Token);
                params.put("os",os);
                params.put("location",locations);


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
}