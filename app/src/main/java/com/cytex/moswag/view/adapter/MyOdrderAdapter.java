package com.cytex.moswag.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cytex.moswag.R;
import com.cytex.moswag.model.entities.Order;
import com.cytex.moswag.view.activities.UpdateOrderStatus;
import com.cytex.moswag.view.activities.ViewMyOrder;

import java.util.ArrayList;
import java.util.List;

public class MyOdrderAdapter extends RecyclerView.Adapter<MyOdrderAdapter.Holderview> {
 private List<Order> productlist;
 private Context context;

    public MyOdrderAdapter(List<Order> productlist, Context context) {
        this.productlist = productlist;
        this.context = context;
    }

    @NonNull
    @Override
    public Holderview onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View layout= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myorderitem,viewGroup,false);

        return new Holderview(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull Holderview holderview, final int position) {

        holderview.tv_description.setText(productlist.get(position).getDescription());
        holderview.tv_amount.setText(productlist.get(position).getAmount());
        holderview.tv_status.setText(productlist.get(position).getStatus());
        holderview.tv_date.setText(productlist.get(position).getDate());

        if(productlist.get(position).getStatus().equals("Transporting")){
            holderview.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"click on"+ productlist.get(position).getDescription(),Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(context, ViewMyOrder.class);
                    intent.putExtra("date",productlist.get(position).getDate());
                    intent.putExtra("order",productlist.get(position).getDescription());
                    context.startActivity(intent);
                }
            });

        }



    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public void setfilter(List<Order> listitem){
        productlist=new ArrayList<>();
        productlist.addAll(listitem);
        notifyDataSetChanged();
    }

    class Holderview extends RecyclerView.ViewHolder{

        TextView tv_description,tv_amount,tv_date,tv_status;

        Holderview(View itemview){
          super(itemview);
            tv_description=(TextView) itemview.findViewById(R.id.description);
            tv_amount=(TextView) itemview.findViewById(R.id.amount);
            tv_status=(TextView) itemview.findViewById(R.id.status);
            tv_date=(TextView) itemview.findViewById(R.id.date);
        }

    }
}
