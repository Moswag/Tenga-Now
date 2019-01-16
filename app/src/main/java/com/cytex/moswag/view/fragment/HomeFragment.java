package com.cytex.moswag.view.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cytex.moswag.R;
import com.cytex.moswag.constants.URLConstants;
import com.cytex.moswag.db.DBHelper;
import com.cytex.moswag.domain.api.ProductCategoryLoaderTask;
import com.cytex.moswag.model.CenterRepository;
import com.cytex.moswag.model.entities.Company;
import com.cytex.moswag.model.entities.ProductCategoryModel;
import com.cytex.moswag.util.Utils;
import com.cytex.moswag.util.Utils.AnimationType;
import com.cytex.moswag.util.VolleySingleton;
import com.cytex.moswag.view.activities.ECartHomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    int mutedColor = R.attr.colorPrimary;
    private CollapsingToolbarLayout collapsingToolbar;
    private RecyclerView recyclerView;
    private Request request;
    /**
     * The double back to exit pressed once.
     */
    private boolean doubleBackToExitPressedOnce;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    private Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_product_category, container, false);

       // CenterRepository.getCenterRepository().getListOfProductsInShoppingList().clear(); //clear cart at the initialisation of companies

        view.findViewById(R.id.search_item).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Utils.switchFragmentWithAnimation(R.id.frag_container,
                                new SearchProductFragment(),
                                ((ECartHomeActivity) getActivity()), null,
                                AnimationType.SLIDE_UP);

                    }
                });

        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.anim_toolbar);
        ((ECartHomeActivity) getActivity()).setSupportActionBar(toolbar);
        ((ECartHomeActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ECartHomeActivity) getActivity()).getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        collapsingToolbar = (CollapsingToolbarLayout) view
                .findViewById(R.id.collapsing_toolbar);

        collapsingToolbar.setTitle("Companies");

        ImageView header = (ImageView) view.findViewById(R.id.header);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.meal);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                mutedColor = palette.getMutedColor(R.color.primary_500);
                collapsingToolbar.setContentScrimColor(mutedColor);
                collapsingToolbar.setStatusBarScrimColor(R.color.black_trans80);
            }
        });

        getCompanies();
        recyclerView = (RecyclerView) view.findViewById(R.id.scrollableview);
        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(linearLayoutManager);





//
//		if (simpleRecyclerAdapter == null) {
//			simpleRecyclerAdapter = new CategoryListAdapter(getActivity());
//			recyclerView.setAdapter(simpleRecyclerAdapter);
//
//			simpleRecyclerAdapter
//					.SetOnItemClickListener(new OnItemClickListener() {
//
//						@Override
//						public void onItemClick(View view, int position) {
//
//							if (position == 0) {
//								CenterRepository.getCenterRepository()
//										.getAllElectronics();
//							} else if (position == 1) {
//								CenterRepository.getCenterRepository()
//										.getAllFurnitures();
//							}
//							Utils.switchFragmentWithAnimation(
//									R.id.frag_container,
//									new ProductOverviewFragment(),
//									((ECartHomeActivity) getActivity()), null,
//									AnimationType.SLIDE_LEFT);
//
//						}
//					});
//		}

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    if (doubleBackToExitPressedOnce) {
                        // super.onBackPressed();

                        if (mHandler != null) {
                            mHandler.removeCallbacks(mRunnable);
                        }

                        getActivity().finish();

                        return true;
                    }

                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(getActivity(),
                            "Please click BACK again to exit",
                            Toast.LENGTH_SHORT).show();

                    mHandler.postDelayed(mRunnable, 2000);

                }
                return true;
            }
        });

        return view;

    }


    public void getCompanies(){
        final DBHelper dbHelper=new DBHelper(getActivity());
        SQLiteDatabase database=dbHelper.getWritableDatabase();
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


                        boolean checkP=dbHelper.checkCompany(jsonObject.getString("id"));
                        if(checkP){
                            saveToLocalStorageOrUpdate(company,false);
                        }
                        else {

                            saveToLocalStorageOrUpdate(company,true);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setuprecyclerview(companies);

            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Failed to connect to database",Toast.LENGTH_LONG).show();
            }
        });


        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);

    }




    private void setuprecyclerview(List<ProductCategoryModel> companies) {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        new ProductCategoryLoaderTask(recyclerView, getActivity(),companies).execute();
    }

    private void saveToLocalStorageOrUpdate(ProductCategoryModel company,boolean save){
        DBHelper dbHelper=new DBHelper(getActivity());
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        if(save){
            dbHelper.saveCompanyToLocalDatabase(company,database);
            Toast.makeText(getActivity(),"Saved company to localdb",Toast.LENGTH_SHORT).show();
        }
        else{
            dbHelper.updateCompanyLocalDatabase(company,database);
        }

        dbHelper.close();
    }


}
