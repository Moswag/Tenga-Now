/*
 * Copyright (c) 2018. http://moswag@cytex.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Webster Moswa <webstermoswa11@Gmail.com>, 2018.
 */

package com.cytex.moswag.domain.mock;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cytex.moswag.AppController;
import com.cytex.moswag.constants.URLConstants;
import com.cytex.moswag.model.CenterRepository;
import com.cytex.moswag.model.entities.Company;
import com.cytex.moswag.model.entities.Product;
import com.cytex.moswag.model.entities.ProductCategoryModel;
import com.cytex.moswag.util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * This class serve as fake server and provides dummy product and category with real Image Urls taken from flipkart
 */
public class FakeWebServer {

    private static FakeWebServer fakeServer;
    private final String JSON_URL= URLConstants.URL_GET_PRODUCTS;
    private StringRequest request;
    Context context = AppController.getInstance();


    public static FakeWebServer getFakeWebServer() {


        if (null == fakeServer) {
            fakeServer = new FakeWebServer();
        }
        return fakeServer;
    }

    void initiateFakeServer() {

        addCategory();

    }

    public void addCategory() {

        ArrayList<ProductCategoryModel> listOfCategory = getCompanies();

//
//        listOfCategory
//                .add(new ProductCategoryModel(
//                        "Softpala",
//                        "1234 Craneborne",
//                        "Harare",
//                        "http://192.168.43.59/tenganow/storage/companylogo/$2y$10$5XFHOexe5BiPhXD7dCDquuDseS6XCvrXGtY7QpYHW2Tn.92x20MTW.jpg"));

//        listOfCategory
//                .add(new ProductCategoryModel(
//                        "Chicken Slice",
//                        "Best food outlet through out the nation",
//                        "0%",
//                        "http://192.168.43.59/NatPharm//images/bb.jpg"));

        CenterRepository.getCenterRepository().setListOfCategory(listOfCategory);
    }

    public void getAllCompanyProducts(String companyID,String companyName) {
        //tablets

        ConcurrentHashMap<String, ArrayList<Product>> productMap = new ConcurrentHashMap<String, ArrayList<Product>>();

        //get view pager

        //get company id and select products from server side using volley
        ArrayList<Product> productlist = new ArrayList<Product>();
        productlist=getProducts(companyID);
        productMap.put(companyName, productlist);

        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);

    }




    public void getAllProducts(String companyID,String companyName) {

        if (companyID!=null) {

            getAllCompanyProducts(companyID,companyName);
        }

    }

    public ArrayList<ProductCategoryModel> getCompanies(){
        final ArrayList<ProductCategoryModel> companies = new ArrayList<>();
        request = new StringRequest(Request.Method.GET, URLConstants.URL_GET_COMPANIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArry = new JSONArray(response);
                    for(int i=0 ; i< jsonArry.length();i++) {

                        JSONObject jsonObject = (JSONObject) jsonArry.get(i);
                        ProductCategoryModel company =new ProductCategoryModel();
                        company.setCompanyName(jsonObject.getString("company_name"));
                        company.setCompanyAddress(jsonObject.getString("company_address"));
                        company.setCompanyLocation(jsonObject.getString("location"));
                        company.setCompanyImageUrl(jsonObject.getString("logo"));
                        company.setCompanyID(jsonObject.getString("id"));
                        company.setEcocash(jsonObject.getString("ecocash"));

                        companies.add(company);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(),"Failed to connect to database",Toast.LENGTH_LONG).show();
            }
        });


        VolleySingleton.getInstance(context).addToRequestQueue(request);
        return companies;
    }


    public ArrayList<Product>  getProducts(final String companyID){
        final ArrayList<Product> productl = new ArrayList<Product>();
        request = new StringRequest(Request.Method.POST, URLConstants.URL_GET_COMPANY_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArry = new JSONArray(response);
                    for(int i=0 ; i< jsonArry.length();i++) {

                        JSONObject jsonObject = (JSONObject) jsonArry.get(i);
                        Product product =new Product();
                        product.setProductName(jsonObject.getString("name"));
                        product.setDescription(jsonObject.getString("description"));
                        product.setCompanyID(jsonObject.getString("company_id"));
                        product.setDiscount(jsonObject.getString("discount"));
                        product.setSalePrice(jsonObject.getString("price"));
                        product.setQuantity(jsonObject.getString("quantity"));
                        product.setImageUrl(jsonObject.getString("picture"));
                        product.setProductId(jsonObject.getString("id"));


                        productl.add(product);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            // setuprecyclerview(lstCompany);

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(),"Failed to connect to database",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("company_id",companyID);
                return hashMap;
            }
        };

        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
        return productl;
    }


}
