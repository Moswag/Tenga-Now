package com.cytex.moswag.view.fragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cytex.moswag.R;
import com.cytex.moswag.constants.URLConstants;
import com.cytex.moswag.model.entities.Order;
import com.cytex.moswag.model.entities.Product;
import com.cytex.moswag.util.UserSessionManager;
import com.cytex.moswag.util.Utils;
import com.cytex.moswag.util.VolleySingleton;
import com.cytex.moswag.view.activities.ECartHomeActivity;
import com.cytex.moswag.view.adapter.MyOdrderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrder extends Fragment {
    private Toolbar mToolbar;
    SearchView searchView;
    RecyclerView listshowrcy;

    MyOdrderAdapter adapter;

    //volley
    StringRequest request;
    String URL= URLConstants.URL_GET_ORDER;

    //session
    UserSessionManager sessionManager;

    List<Order> productlists=new ArrayList<Order>();


    public MyOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_my_order, container, false);
        getActivity().setTitle("My Order");

        sessionManager=new UserSessionManager(getActivity());
        HashMap<String,String> session=sessionManager.getUserDetails();
       final String username=session.get(UserSessionManager.KEY_User);

        mToolbar = (Toolbar) rootView.findViewById(R.id.htab_toolbar);
        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationIcon(R.drawable.ic_drawer);

        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ECartHomeActivity) getActivity()).getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        mToolbar.setTitleTextColor(Color.WHITE);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    Utils.switchContent(R.id.frag_container,
                            Utils.HOME_FRAGMENT,
                            ((ECartHomeActivity) (getContext())),
                            Utils.AnimationType.SLIDE_UP);

                }
                return true;
            }
        });






        listshowrcy=(RecyclerView)rootView.findViewById(R.id.listshow);
        listshowrcy.setHasFixedSize(true);
        jsonrequest(username);

        return rootView;
    }

    private void jsonrequest(final String username) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Getting my order ");
        progressDialog.show();


        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONArray jsonArry = new JSONArray(response);
                    for(int i=0 ; i< jsonArry.length();i++) {

                        JSONObject jsonObject = (JSONObject) jsonArry.get(i);
                        Order product =new Order();
                        product.setDescription(jsonObject.getString("description"));
                        product.setAmount(jsonObject.getString("amount"));
                        product.setStatus(jsonObject.getString("status"));
                        product.setDate(jsonObject.getString("created_at"));


                        productlists.add(product);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setuprecyclerview(productlists);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Failed to connect to database",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("username",username);

                return hashMap;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void setuprecyclerview(List<Order> lstPosts) {
        //
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        listshowrcy.setLayoutManager(layoutManager);
        adapter=new MyOdrderAdapter(lstPosts,getActivity());
        listshowrcy.setAdapter(adapter);


    }




}
