package com.cytex.moswag.view.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cytex.moswag.R;
import com.cytex.moswag.constants.URLConstants;
import com.cytex.moswag.util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText name,address,phonenumber,password;
    Button register,backtologin;
    StringRequest request;
    String URL= URLConstants.URL_REGISTER;
    private AutoCompleteTextView email;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=(EditText) findViewById(R.id.name);
        email=(AutoCompleteTextView) findViewById(R.id.email);
        address=(EditText) findViewById(R.id.address);
        phonenumber=(EditText) findViewById(R.id.phonenumber);
        password=(EditText) findViewById(R.id.password);
        register=(Button) findViewById(R.id.register);
        backtologin=(Button) findViewById(R.id.backtologin);


        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog=new ProgressDialog(Register.this);
                progressDialog.show();
                request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String res=jsonObject.getString("response");


                            if(res.equals("success")){
                                progressDialog.dismiss();

                                name.setText("");
                                email.setText("");
                                phonenumber.setText("");
                                address.setText("");
                                password.setText("");
                                startActivity(new Intent(Register.this,LoginActivity.class));
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(Register.this,"Sorry something is wrong with the server, please contact developer",Toast.LENGTH_LONG).show();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this,"Sorry could not connect to the database, please make sure you are connected to your host",Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<>();
                        params.put("name",name.getText().toString());
                        params.put("email",email.getText().toString());
                        params.put("phonenumber",phonenumber.getText().toString());
                        params.put("address",address.getText().toString());
                        params.put("password",password.getText().toString());
                        return params;
                    }
                };
                VolleySingleton.getInstance(Register.this).addToRequestQueue(request);
            }
        });

    }
}
