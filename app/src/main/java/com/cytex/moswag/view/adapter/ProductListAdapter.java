/*
 * Copyright (c) 2018. http://moswag@cytex.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Webster Moswa <webstermoswa11@Gmail.com>, 2018.
 */

package com.cytex.moswag.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cytex.moswag.R;
import com.cytex.moswag.model.CenterRepository;
import com.cytex.moswag.model.entities.Money;
import com.cytex.moswag.model.entities.Product;
import com.cytex.moswag.util.ColorGenerator;
import com.cytex.moswag.util.Utils;
import com.cytex.moswag.view.activities.ECartHomeActivity;
import com.cytex.moswag.view.customview.TextDrawable;
import com.cytex.moswag.view.customview.TextDrawable.IBuilder;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Webster Moswa (moswag@cytex.com)
 */
public class ProductListAdapter extends
        RecyclerView.Adapter<ProductListAdapter.VersionViewHolder> implements
        ItemTouchHelperAdapter {

    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    private IBuilder mDrawableBuilder;

    private TextDrawable drawable;

    private String ImageUrl;

    private List<Product> productList = new ArrayList<Product>();
    private OnItemClickListener clickListener;

    private Context context;

    public ProductListAdapter(String subcategoryKey, Context context,
                              boolean isCartlist) {

        if (isCartlist) {

            productList = CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList();

        } else {

            productList = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                    .get(subcategoryKey);
        }

        this.context = context;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_product_list, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder holder,
                                 final int position) {

        holder.itemName.setText(productList.get(position)
                .getProductName());

        holder.itemDesc.setText(productList.get(position)
                .getDescription());

        String sellCostString = Money.rupees(
                Double.valueOf(productList.get(position)
                        .getSalePrice())).toString()
                + "  ";

        String buyMRP = Money.rupees(
                Double.valueOf(productList.get(position)
                        .getSalePrice())).toString();

        String costString = sellCostString + buyMRP;

        holder.itemCost.setText(buyMRP);

       // holder.itemCost.setText(costString, BufferType.SPANNABLE);   //to set the cancelled price and new price

//        Spannable spannable = (Spannable) holder.itemCost.getText();
//
//        spannable.setSpan(new StrikethroughSpan(), sellCostString.length(),
//                costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4)
                .endConfig().roundRect(10);

        drawable = mDrawableBuilder.build(String.valueOf(productList
                .get(position).getProductName().charAt(0)), mColorGenerator
                .getColor(productList.get(position).getProductName()));

        ImageUrl = productList.get(position).getImageUrl();


        Glide.with(context).load(ImageUrl).placeholder(drawable)
                .error(drawable).animate(R.anim.base_slide_right_in)
                .centerCrop().into(holder.imagView);


        holder.addItem.findViewById(R.id.add_item).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        //current object
                        Product tempObj = productList.get(position);


                        //check the products company in the cart
//                        List<Product> getProductsInCart = CenterRepository.getCenterRepository()
//                                .getListOfProductsInShoppingList();
//                        String companyID = null;
//                        boolean cmID=false;
//                        if(getProductsInCart!=null){
//                            Toast.makeText(context,"The cart is not empty",Toast.LENGTH_SHORT).show();
//                            for (Product prod : getProductsInCart) {
//                                companyID = prod.getCompanyID();
//                                Toast.makeText(context,"Products in cart have company id "+companyID,Toast.LENGTH_SHORT).show();
//                            }
//                            // cmID=companyID.equals(tempObj.getCompanyID())? true: false;
//                        }
//
//
//                        if (cmID==true || getProductsInCart==null) {


                            //if current object is already in shopping list
                            if (CenterRepository.getCenterRepository()
                                    .getListOfProductsInShoppingList().contains(tempObj)) {


                                //get position of current item in shopping list
                                int indexOfTempInShopingList = CenterRepository
                                        .getCenterRepository().getListOfProductsInShoppingList()
                                        .indexOf(tempObj);

                                // increase quantity of current item in shopping list
                                if (Integer.parseInt(tempObj.getQuantity()) == 0) {

                                    ((ECartHomeActivity) getContext())
                                            .updateItemCount(true);

                                }


                                //if cart contains the company id
                                if(CenterRepository.getCenterRepository()
                                        .getListOfProductsInShoppingList().get(0).getCompanyID().equals(tempObj.getCompanyID())) {
                                    // update quantity in shopping list
                                    CenterRepository
                                            .getCenterRepository()
                                            .getListOfProductsInShoppingList()
                                            .get(indexOfTempInShopingList)
                                            .setQuantity(
                                                    String.valueOf(Integer
                                                            .valueOf(tempObj
                                                                    .getQuantity()) + 1));


                                    //update checkout amount
                                    ((ECartHomeActivity) getContext()).updateCheckOutAmount(
                                            Double
                                                    .valueOf(productList
                                                            .get(position)
                                                            .getSalePrice()),
                                            true);

                                    // update current item quanitity
                                    holder.quanitity.setText(tempObj.getQuantity());
                                }
                                else{
                                    Toast.makeText(context,"The company id do not match",Toast.LENGTH_SHORT).show();
                                }

                            } else if(CenterRepository.getCenterRepository()
                                    .getListOfProductsInShoppingList().size()!=0) {
                                if(CenterRepository.getCenterRepository()
                                        .getListOfProductsInShoppingList().get(0).getCompanyID().equals(tempObj.getCompanyID()) ) {

                                    ((ECartHomeActivity) getContext()).updateItemCount(true);

                                    tempObj.setQuantity(String.valueOf(1));

                                    holder.quanitity.setText(tempObj.getQuantity());

                                    CenterRepository.getCenterRepository()
                                            .getListOfProductsInShoppingList().add(tempObj);

                                    ((ECartHomeActivity) getContext()).updateCheckOutAmount(
                                            Double.valueOf(productList.get(position).getSalePrice()),
                                            true);
                                }
                                else{
                                    Toast.makeText(context,"Clearing cart for the previous company",Toast.LENGTH_SHORT).show();
                                    CenterRepository.getCenterRepository()
                                            .getListOfProductsInShoppingList().clear();
                                    //update ui for amounbt and itemcount
                                    ((ECartHomeActivity) getContext()).updateCheckOutAmountAndItemCount(
                                            Double.valueOf("0.0"),
                                            0);
                                }

                            }
                            else{
                                ((ECartHomeActivity) getContext()).updateItemCount(true);

                                tempObj.setQuantity(String.valueOf(1));

                                holder.quanitity.setText(tempObj.getQuantity());

                                CenterRepository.getCenterRepository()
                                        .getListOfProductsInShoppingList().add(tempObj);

                                ((ECartHomeActivity) getContext()).updateCheckOutAmount(
                                        Double.valueOf(productList.get(position).getSalePrice()),
                                        true);
                            }

                            Utils.vibrate(getContext());


                    }
                });

        holder.removeItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Product tempObj = (productList).get(position);

                if (CenterRepository.getCenterRepository().getListOfProductsInShoppingList()
                        .contains(tempObj)) {

                    int indexOfTempInShopingList = CenterRepository
                            .getCenterRepository().getListOfProductsInShoppingList()
                            .indexOf(tempObj);

                    if (Integer.valueOf(tempObj.getQuantity()) != 0) {

                        CenterRepository
                                .getCenterRepository()
                                .getListOfProductsInShoppingList()
                                .get(indexOfTempInShopingList)
                                .setQuantity(
                                        String.valueOf(Integer.valueOf(tempObj
                                                .getQuantity()) - 1));

                        ((ECartHomeActivity) getContext()).updateCheckOutAmount(
                                Double.valueOf(productList
                                        .get(position).getSalePrice()),
                                false);

                        holder.quanitity.setText(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
                                .get(indexOfTempInShopingList).getQuantity());

                        Utils.vibrate(getContext());

                        if (Integer.valueOf(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
                                .get(indexOfTempInShopingList).getQuantity()) == 0) {

                            CenterRepository.getCenterRepository()
                                    .getListOfProductsInShoppingList()
                                    .remove(indexOfTempInShopingList);

                            notifyDataSetChanged();

                            ((ECartHomeActivity) getContext())
                                    .updateItemCount(false);

                        }

                    }

                } else {

                }

            }

        });

    }


    private ECartHomeActivity getContext() {
        // TODO Auto-generated method stub
        return (ECartHomeActivity) context;
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onItemDismiss(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(productList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(productList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    class VersionViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ //removed the onclick on product
              {
        TextView itemName, itemDesc, itemCost, availability, quanitity,
                addItem, removeItem;
        ImageView imagView;

        public VersionViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.item_name);

            itemDesc = (TextView) itemView.findViewById(R.id.item_short_desc);

            itemCost = (TextView) itemView.findViewById(R.id.item_price);

            availability = (TextView) itemView
                    .findViewById(R.id.iteam_avilable);

            quanitity = (TextView) itemView.findViewById(R.id.iteam_amount);

            itemName.setSelected(true);

            imagView = ((ImageView) itemView.findViewById(R.id.product_thumb));

            addItem = ((TextView) itemView.findViewById(R.id.add_item));

            removeItem = ((TextView) itemView.findViewById(R.id.remove_item));

           // itemView.setOnClickListener(this);


        }

//        @Override
//        public void onClick(View v) {
//            clickListener.onItemClick(v, getPosition());
//        }
    }

}
