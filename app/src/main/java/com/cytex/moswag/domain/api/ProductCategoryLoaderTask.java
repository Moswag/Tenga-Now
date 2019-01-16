/*
 * Copyright (c) 2018. http://moswag@cytex.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Webster Moswa <webstermoswa11@Gmail.com>, 2018.
 */

package com.cytex.moswag.domain.api;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.cytex.moswag.R;
import com.cytex.moswag.domain.mock.FakeWebServer;
import com.cytex.moswag.model.entities.ProductCategoryModel;
import com.cytex.moswag.util.AppConstants;
import com.cytex.moswag.util.Utils;
import com.cytex.moswag.util.Utils.AnimationType;
import com.cytex.moswag.view.activities.ECartHomeActivity;
import com.cytex.moswag.view.adapter.CategoryListAdapter;
import com.cytex.moswag.view.adapter.CategoryListAdapter.OnItemClickListener;
import com.cytex.moswag.view.fragment.ProductOverviewFragment;

import java.util.List;

/**
 * The Class ImageLoaderTask.
 */
public class ProductCategoryLoaderTask extends AsyncTask<String, Void, Void> {

    private static final int NUMBER_OF_COLUMNS = 2;
    private Context context;
    private RecyclerView recyclerView;
    List<ProductCategoryModel> companies;

    public ProductCategoryLoaderTask(RecyclerView listView, Context context, List<ProductCategoryModel> companies) {

        this.companies=companies;
        this.recyclerView = listView;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        if (null != ((ECartHomeActivity) context).getProgressBar())
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.VISIBLE);

    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (null != ((ECartHomeActivity) context).getProgressBar())
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.GONE);

        if (recyclerView != null) {
            final CategoryListAdapter simpleRecyclerAdapter = new CategoryListAdapter(
                    context,companies);

            recyclerView.setAdapter(simpleRecyclerAdapter);

            simpleRecyclerAdapter
                    .SetOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {


                            AppConstants.CURRENT_CATEGORY = position;

                            //pass company id and name to the viewPager
                            Utils.switchFragmentWithAnimation(
                                    R.id.frag_container,
                                    new ProductOverviewFragment(companies.get(position).getCompanyID(),companies.get(position).getCompanyName()),
                                    ((ECartHomeActivity) context), null,
                                    AnimationType.SLIDE_LEFT);

                        }
                    });
        }

    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FakeWebServer.getFakeWebServer().addCategory();

        return null;
    }

}