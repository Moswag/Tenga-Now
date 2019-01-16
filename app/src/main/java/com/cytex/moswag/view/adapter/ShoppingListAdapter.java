/*
 * Copyright (c) 2018. http://moswag@cytex.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Webster Moswa <webstermoswa11@Gmail.com>, 2018.
 */

package com.cytex.moswag.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.euicc.DownloadableSubscription;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.bumptech.glide.Glide;
import com.cytex.moswag.R;
import com.cytex.moswag.model.CenterRepository;
import com.cytex.moswag.model.entities.Money;
import com.cytex.moswag.model.entities.Product;
import com.cytex.moswag.util.ColorGenerator;
import com.cytex.moswag.util.Utils;
import com.cytex.moswag.view.activities.ECartHomeActivity;
import com.cytex.moswag.view.customview.ItemTouchHelperAdapter;
import com.cytex.moswag.view.customview.ItemTouchHelperViewHolder;
import com.cytex.moswag.view.customview.OnStartDragListener;
import com.cytex.moswag.view.customview.TextDrawable;
import com.cytex.moswag.view.customview.TextDrawable.IBuilder;
import com.cytex.moswag.view.fragment.MyCartFragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to
 * respond to move and dismiss events from a
 * {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Webster Moswa (moswag@cytex.com)
 */
public class ShoppingListAdapter extends
        RecyclerView.Adapter<ShoppingListAdapter.ItemViewHolder> implements
        ItemTouchHelperAdapter {

    private static OnItemClickListener clickListener;
    private final OnStartDragListener mDragStartListener;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    private String ImageUrl;
    private List<Product> productList = new ArrayList<Product>();
    private Context context;

    public ShoppingListAdapter(Context context,
                               OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;

        this.context = context;

        productList = CenterRepository.getCenterRepository().getListOfProductsInShoppingList();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_product_list, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.itemName.setText(productList.get(position).getProductName());

        holder.itemDesc.setText(productList.get(position).getDescription());

        String sellCostString = Money.rupees(
                Double.valueOf(productList.get(position)
                        .getSalePrice())).toString()
                + "  ";

        String buyMRP = Money.rupees(
                Double.valueOf(productList.get(position)
                        .getSalePrice())).toString();

        String costString = sellCostString + buyMRP;

        holder.itemCost.setText(costString, BufferType.SPANNABLE);

        Spannable spannable = (Spannable) holder.itemCost.getText();

        spannable.setSpan(new StrikethroughSpan(), sellCostString.length(),
                costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4)
                .endConfig().roundRect(10);

        drawable = mDrawableBuilder.build(String.valueOf(productList
                .get(position).getProductName().charAt(0)), mColorGenerator
                .getColor(productList.get(position).getProductName()));

        ImageUrl = productList.get(position).getImageUrl();

        holder.quanitity.setText(CenterRepository.getCenterRepository()
                .getListOfProductsInShoppingList().get(position).getQuantity());

        Glide.with(context).load(ImageUrl).placeholder(drawable)
                .error(drawable).animate(R.anim.base_slide_right_in)
                .centerCrop().into(holder.imagView);

        // Start a drag whenever the handle view it touched
        holder.imagView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });

        holder.addItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                CenterRepository
                        .getCenterRepository()
                        .getListOfProductsInShoppingList()
                        .get(position)
                        .setQuantity(
                                String.valueOf(

                                        Integer.valueOf(CenterRepository
                                                .getCenterRepository().getListOfProductsInShoppingList()
                                                .get(position).getQuantity()) + 1));

                holder.quanitity.setText(CenterRepository.getCenterRepository()
                        .getListOfProductsInShoppingList().get(position).getQuantity());

                Utils.vibrate(context);

                ((ECartHomeActivity) context).updateCheckOutAmount(
                        Double.valueOf(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
                                .get(position).getSalePrice()), true);

            }
        });

        holder.removeItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Integer.valueOf(CenterRepository.getCenterRepository()
                        .getListOfProductsInShoppingList().get(position).getQuantity()) > 2) {

                    CenterRepository
                            .getCenterRepository()
                            .getListOfProductsInShoppingList()
                            .get(position)
                            .setQuantity(
                                    String.valueOf(

                                            Integer.valueOf(CenterRepository
                                                    .getCenterRepository()
                                                    .getListOfProductsInShoppingList().get(position)
                                                    .getQuantity()) - 1));

                    holder.quanitity.setText(CenterRepository
                            .getCenterRepository().getListOfProductsInShoppingList()
                            .get(position).getQuantity());

                    ((ECartHomeActivity) context).updateCheckOutAmount(
                            Double.valueOf(CenterRepository
                                    .getCenterRepository().getListOfProductsInShoppingList()
                                    .get(position).getSalePrice()), false);

                    Utils.vibrate(context);
                } else if (Integer.valueOf(CenterRepository.getCenterRepository()
                        .getListOfProductsInShoppingList().get(position).getQuantity()) == 1) {
                    ((ECartHomeActivity) context).updateItemCount(false);

                    ((ECartHomeActivity) context).updateCheckOutAmount(
                            Double.valueOf(CenterRepository
                                    .getCenterRepository().getListOfProductsInShoppingList()
                                    .get(position).getSalePrice()), false);

                    CenterRepository.getCenterRepository().getListOfProductsInShoppingList()
                            .remove(position);

                    if (Integer.valueOf(((ECartHomeActivity) context)
                            .getItemCount()) == 0) {

                        MyCartFragment.updateMyCartFragment(false);

                    }

                    Utils.vibrate(context);

                }

            }
        });
    }

    @Override
    public void onItemDismiss(int position) {

        ((ECartHomeActivity) context).updateItemCount(false);

        ((ECartHomeActivity) context).updateCheckOutAmount(
                Double.valueOf(CenterRepository
                        .getCenterRepository().getListOfProductsInShoppingList().get(position)
                        .getSalePrice()), false);

        CenterRepository.getCenterRepository().getListOfProductsInShoppingList().remove(position);

        if (Integer.valueOf(((ECartHomeActivity) context).getItemCount()) == 0) {

            MyCartFragment.updateMyCartFragment(false);

        }

        Utils.vibrate(context);

        // productList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        Collections.swap(productList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return productList.size();

    }

    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    /**
     * Simple example of a view holder that implements
     * {@link ItemTouchHelperViewHolder} and has a "handle" view that initiates
     * a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder, View.OnClickListener {

        // public final ImageView handleView;

        TextView itemName, itemDesc, itemCost, availability, quanitity,
                addItem, removeItem;
        ImageView imagView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            // handleView = (ImageView) itemView.findViewById(R.id.handle);

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

            itemView.setOnClickListener(this);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public void onClick(View v) {

            clickListener.onItemClick(v, getPosition());
        }
    }
}
