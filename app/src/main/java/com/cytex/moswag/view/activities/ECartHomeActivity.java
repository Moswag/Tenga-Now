/*
 * Copyright (c) 2018. http://moswag@cytex.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Webster Moswa <webstermoswa11@Gmail.com>, 2018.
 */

package com.cytex.moswag.view.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.cytex.moswag.R;
import com.cytex.moswag.constants.URLConstants;
import com.cytex.moswag.db.DBHelper;
import com.cytex.moswag.domain.helper.Connectivity;
import com.cytex.moswag.domain.mining.AprioriFrequentItemsetGenerator;
import com.cytex.moswag.domain.mining.FrequentItemsetData;
import com.cytex.moswag.model.CenterRepository;
import com.cytex.moswag.model.entities.Money;
import com.cytex.moswag.model.entities.Product;
import com.cytex.moswag.util.PreferenceHelper;
import com.cytex.moswag.util.TinyDB;
import com.cytex.moswag.util.UserSessionManager;
import com.cytex.moswag.util.Utils;
import com.cytex.moswag.util.Utils.AnimationType;
import com.cytex.moswag.util.VolleySingleton;
import com.cytex.moswag.view.fragment.HomeFragment;
import com.cytex.moswag.view.fragment.WhatsNewDialog;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ECartHomeActivity extends AppCompatActivity {

    public static final double MINIMUM_SUPPORT = 0.1;
    private static final String TAG = ECartHomeActivity.class.getSimpleName();
    AprioriFrequentItemsetGenerator<String> generator =
            new AprioriFrequentItemsetGenerator<>();
    private int itemCount = 0;
    private Double checkoutAmount = new Double(Double.MIN_VALUE);
    private DrawerLayout mDrawerLayout;

    private TextView checkOutAmount, itemCountTextView;
    private TextView offerBanner;
    private AVLoadingIndicatorView progressBar;

    private NavigationView mNavigationView;

    //exm
    private final String JSON_URL= URLConstants.URL_SEND_ORDER;
    private StringRequest request;

    UserSessionManager session;

    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecart);

        CenterRepository.getCenterRepository().setListOfProductsInShoppingList(
                new TinyDB(getApplicationContext()).getListObject(
                        PreferenceHelper.MY_CART_LIST_LOCAL, Product.class));

        session = new UserSessionManager(getApplicationContext());


        dbHelper=new DBHelper(getApplicationContext());

        itemCount = CenterRepository.getCenterRepository().getListOfProductsInShoppingList()
                .size();

        //	makeFakeVolleyJsonArrayRequest();

        offerBanner = ((TextView) findViewById(R.id.new_offers_banner));

        itemCountTextView = (TextView) findViewById(R.id.item_count);
        itemCountTextView.setSelected(true);
        itemCountTextView.setText(String.valueOf(itemCount));

        checkOutAmount = (TextView) findViewById(R.id.checkout_amount);
        checkOutAmount.setSelected(true);
        checkOutAmount.setText(Money.rupees(checkoutAmount).toString());
        offerBanner.setSelected(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        progressBar = (AVLoadingIndicatorView) findViewById(R.id.loading_bar);

        checkOutAmount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Utils.vibrate(getApplicationContext());

                Utils.switchContent(R.id.frag_container,
                        Utils.SHOPPING_LIST_TAG, ECartHomeActivity.this,
                        AnimationType.SLIDE_UP);

            }
        });


        if (itemCount != 0) {
            for (Product product : CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList()) {

                updateCheckOutAmount(
                        Double.valueOf(product.getSalePrice()),
                        true);
            }
        }

        findViewById(R.id.item_counter).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Utils.vibrate(getApplicationContext());
                        Utils.switchContent(R.id.frag_container,
                                Utils.SHOPPING_LIST_TAG,
                                ECartHomeActivity.this, AnimationType.SLIDE_UP);

                    }
                });

        findViewById(R.id.checkout).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Utils.vibrate(getApplicationContext());

                        showPurchaseDialog();
//                        if(checkOutAmount.getText().toString().equals("0.0")){
//                            showPurchaseDialog();
//                        }
//                        else{
//                            Toast.makeText(getApplicationContext(),"Your cart is currently empty, to buy please add products",Toast.LENGTH_LONG).show();
//                        }



                    }
                });

        Utils.switchFragmentWithAnimation(R.id.frag_container,
                new HomeFragment(), this, Utils.HOME_FRAGMENT,
                AnimationType.SLIDE_UP);

        toggleBannerVisibility();

        mNavigationView
                .setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.home:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.HOME_FRAGMENT,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);

                                return true;

                            case R.id.my_cart:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.SHOPPING_LIST_TAG,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;

                            case R.id.myorder:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.MYORDER_FRAGMENT_TAG,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;

                            case R.id.apriori_result:

                                mDrawerLayout.closeDrawers();

                                startActivity(new Intent(ECartHomeActivity.this, APrioriResultActivity.class));

                                return true;


                            case R.id.contact_us:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.CONTACT_US_FRAGMENT,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;

                            case R.id.settings:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.SETTINGS_FRAGMENT_TAG,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;
                            default:
                                return true;
                        }
                    }
                });

    }

    public AVLoadingIndicatorView getProgressBar() {
        return progressBar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void showMassage(String Title,String Message)
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                showMassage("How to use this App","\n This app help you to orfer Natpharm orders online\n" +
                        "2.\t There is door to door delivery T&C apply\n" +
                        "3.\t You can go grab an order already packed for you at the shop \n" +
                        "4.\t Many more features to enjoy\n" +
                        "5.\t Happy shopping\n");
                return true;
            case R.id.logout:
                android.app.AlertDialog.Builder alertDialogBuilder =  new android.app.AlertDialog.Builder(this)
                        .setTitle("Logout?")
                        .setMessage("Are you sure you want to Logout from " + getString(R.string.app_name) + "?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                session.logoutUser();
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);

                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateItemCount(boolean ifIncrement) {
        if (ifIncrement) {
            itemCount++;
            itemCountTextView.setText(String.valueOf(itemCount));

        } else {
            itemCountTextView.setText(String.valueOf(itemCount <= 0 ? 0
                    : --itemCount));
        }

        toggleBannerVisibility();
    }

    public void updateCheckOutAmount(Double amount, boolean increment) {

        if (increment) {
            checkoutAmount = checkoutAmount + amount;
        } else {
            if (checkoutAmount != 0)
                checkoutAmount = checkoutAmount-amount;
        }

        checkOutAmount.setText(Money.rupees(checkoutAmount).toString());
    }

    public void updateCheckOutAmountAndItemCount(Double checkoutAmount,int itemCount){
        this.itemCount=itemCount;
        this.checkoutAmount=checkoutAmount;
        toggleBannerVisibility();

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Store Shopping Cart in DB
        new TinyDB(getApplicationContext()).putListObject(
                PreferenceHelper.MY_CART_LIST_LOCAL, CenterRepository
                        .getCenterRepository().getListOfProductsInShoppingList());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Show Offline Error Message
        if (!Connectivity.isConnected(getApplicationContext())) {
            final Dialog dialog = new Dialog(ECartHomeActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.connection_dialog);
            Button dialogButton = (Button) dialog
                    .findViewById(R.id.dialogButtonOK);

            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

            dialog.show();
        }

        // Show Whats New Features If Requires
        new WhatsNewDialog(this);
    }

    /*
     * Toggles Between Offer Banner and Checkout Amount. If Cart is Empty SHow
     * Banner else display total amount and item count
     */


    public void toggleBannerVisibility() {
        if (itemCount == 0) {

            findViewById(R.id.checkout_item_root).setVisibility(View.GONE);
            findViewById(R.id.new_offers_banner).setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.checkout_item_root).setVisibility(View.VISIBLE);
            findViewById(R.id.new_offers_banner).setVisibility(View.GONE);
        }
    }

    /*
     * get total checkout amount
     */
    public Double getCheckoutAmount() {
        return checkoutAmount;
    }



    /*
     * Get Number of items in cart
     */
    public int getItemCount() {
        return itemCount;
    }

    /*
     * Get Navigation drawer
     */
    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }


    public void showPurchaseDialog() {

        AlertDialog.Builder exitScreenDialog = new AlertDialog.Builder(ECartHomeActivity.this, R.style.PauseDialog);

        exitScreenDialog.setTitle("Order Confirmation")
                .setMessage("Would you like to place this order ?");
        exitScreenDialog.setCancelable(true);

        exitScreenDialog.setPositiveButton(
                "Place Order",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();
                        dialog.cancel();

                        ArrayList<String> productId = new ArrayList<String>();

                        for (Product productFromShoppingList : CenterRepository.getCenterRepository().getListOfProductsInShoppingList()) {

                            //add product ids to array
                            productId.add(productFromShoppingList.getProductId());
                        }
                        ArrayList<Product> toprint = new ArrayList<Product>();
                        for (Product productFromShoppingList : CenterRepository.getCenterRepository().getListOfProductsInShoppingList()) {

                            //add product ids to array
                            toprint.add(productFromShoppingList);
                        }

                        String str = "";
                        for (Product pair : toprint) {
                            str += pair.getProductName()+" quantity(" +pair.getQuantity() +"), "+ ' ';
                        }

                        String am=checkoutAmount.toString();
                        HashMap<String, String> user = session.getUserDetails();
                        final String username = user.get(UserSessionManager.KEY_User);

                        String compID=CenterRepository.getCenterRepository()
                                .getListOfProductsInShoppingList().get(0).getCompanyID();

                        String ecocashNumber=dbHelper.getEcocash(compID);
                        String code="*151*1*1";
                        if(ecocashNumber.length()!=10){
                            code="*151*1*2";
                        }

                        int myAmount=checkoutAmount.intValue();
                        String processEcocash = code+"*"+ecocashNumber+"*"+myAmount+"#";
                        Intent i = new Intent(Intent.ACTION_CALL);
                        i.setData(Uri.parse("tel:" + Uri.encode(processEcocash)));

                        if (ActivityCompat.checkSelfPermission(ECartHomeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Toast.makeText(ECartHomeActivity.this,"Grant permission for calling in the application permissions",Toast.LENGTH_LONG).show();
                            return;
                        }


                        Toast.makeText(ECartHomeActivity.this,"Ecocash to "+processEcocash,Toast.LENGTH_LONG).show();

                        startActivity(i);

                        //send order to server
                        sendOrder(str,am,username);


                        //pass product id array to Apriori ALGO
                        CenterRepository.getCenterRepository()
                                .addToItemSetList(new HashSet<>(productId));

                        //Do Minning
                        FrequentItemsetData<String> data = generator.generate(
                                CenterRepository.getCenterRepository().getItemSetList()
                                , MINIMUM_SUPPORT);

                        for (Set<String> itemset : data.getFrequentItemsetList()) {
                            Log.e("Graph", "Item Set : " +
                                    itemset + "Support : " +
                                    data.getSupport(itemset));
                        }

                        //clear all list item
                        CenterRepository.getCenterRepository().getListOfProductsInShoppingList().clear();   //wen back pressed out of shop clear cart



                        itemCount = 0;
                        itemCountTextView.setText(String.valueOf(0));
                        checkoutAmount = new Double(Double.MIN_VALUE);
                        checkOutAmount.setText(Money.rupees(checkoutAmount).toString());   //removed the checkoutAmount here
                        toggleBannerVisibility();

                    }
                });


        exitScreenDialog.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        exitScreenDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Snackbar.make(ECartHomeActivity.this.getWindow().getDecorView().findViewById(android.R.id.content)
                        , "Ordering cancelled, continue shopping Happy Shopping !!", Snackbar.LENGTH_LONG)
                        .setAction("View Your Basket", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(ECartHomeActivity.this, APrioriResultActivity.class));
                            }
                        }).show();
            }
        });

        AlertDialog alert11 = exitScreenDialog.create();
        alert11.show();


    }


    public void  sendOrder(final String order, final String amount,final String username){

        request = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArry = new JSONArray(response);
                    for(int i=0 ; i< jsonArry.length();i++) {



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("me","Failed to connect to database");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("order",order);
                hashMap.put("amount",amount);
                hashMap.put("username",username);

                return hashMap;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }


}
