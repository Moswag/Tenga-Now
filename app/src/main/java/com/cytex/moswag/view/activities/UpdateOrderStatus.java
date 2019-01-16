package com.cytex.moswag.view.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cytex.moswag.R;
import com.cytex.moswag.constants.URLConstants;
import com.cytex.moswag.model.entities.Order;
import com.cytex.moswag.util.UserSessionManager;
import com.cytex.moswag.util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateOrderStatus extends AppCompatActivity {
    TextView tv_order;
    Spinner spinner;
    Button submit;
    StringRequest request;
    String URL= URLConstants.URL_GET_STATUS;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order_status);

        final String date=getIntent().getExtras().getString("date");
        String order=getIntent().getExtras().getString("oder");


         tv_order=(TextView) findViewById(R.id.order);
         spinner=(Spinner) findViewById(R.id.spinner);
         submit=(Button) findViewById(R.id.btnsubmit);

         session=new UserSessionManager(this);

         HashMap<String,String> getdata=session.getUserDetails();
        final String username=getdata.get(UserSessionManager.KEY_User);


        tv_order.setText(order);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1);
        adapter.add("Received");
        adapter.add("Not Received");
        spinner.setAdapter(adapter);

        final String status=spinner.getSelectedItem().toString();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonrequest(date,status,username);
            }
        });

    }
    private void jsonrequest(final String date,final String status,final String username) {
        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Updating status");
        progressDialog.show();


        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject =new JSONObject(response) ;
                    String status=jsonObject.getString("response");

                    if(status.equals("success")){

                        Toast.makeText(getApplicationContext(),"Successfully updated status",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),ECartHomeActivity.class));

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Failed to update status",Toast.LENGTH_LONG).show();

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Failed to connect to database",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("status",status);
                hashMap.put("username",username);
                hashMap.put("date",date);

                return hashMap;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
