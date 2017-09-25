package com.sagib.food2you;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sagib.food2you.models.Food;
import com.sagib.food2you.models.Product;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ProductsFragment extends Fragment {


    @BindView(R.id.rvProductsList)
    RecyclerView rvProductsList;
    Unbinder unbinder;
    Gson gson = new Gson();
    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_products, container, false);
        unbinder = ButterKnife.bind(this, v);
        prefs = getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
        ProductsAdapter adapter = new ProductsAdapter(getFoodList(), this);
        rvProductsList.setAdapter(adapter);
        rvProductsList.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ביצוע הזמנה - תפריט");
        super.onResume();
    }

    public ArrayList<Food> getFoodList() {
        ArrayList<Food> foodArrayList = new ArrayList<>();
        foodArrayList.add(new Food("ג׳חנון", "ג'חנון + ביצה + רסק + חריף", 19, null, true));
        foodArrayList.add(new Food("מלוואח", "מלוואח + ביצה + רסק + חריף", 19, null, true));
        foodArrayList.add(new Food("מלוואח מגולגל", "מלוואח מגולגל עם חומוס, טחינה, רסק וביצה", 22, null, true));
        foodArrayList.add(new Food("מלוואח גבינות מגולגל", "מלוואח מגולגל עם תערובת גבינות, רסק עגבניות וביצה קשה", 30, null, true));
        foodArrayList.add(new Food("חומוס/טחינה אישי", "", 7, null, false));
        foodArrayList.add(new Food("תוספת ביצה", "", 3, null, false));
        foodArrayList.add(new Food("בקבוק שתיה 1.5 ל׳", "ממשפחת קוקה קולה, יש לציין בהערות את המשקה המבוקש", 12, null, false));
        return foodArrayList;
    }


    public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

        ArrayList<Food> foodArrayList;
        Fragment fragment;
        LayoutInflater inflater;
        Context context;

        public ProductsAdapter(ArrayList<Food> foodArrayList, Fragment fragment) {
            this.foodArrayList = foodArrayList;
            this.fragment = fragment;
            this.context = fragment.getContext();
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.foodlist_item, parent, false);
            return new ProductsViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ProductsViewHolder holder, int position) {
            final Food food = foodArrayList.get(position);
            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");
            holder.btnAdd.setTypeface(font);
            switch (food.getName()) {
                case "ג׳חנון":
                    holder.ivFoodImg.setImageResource(R.drawable.food);
                    break;
                case "בקבוק שתיה 1.5 ל׳":
                    holder.ivFoodImg.setImageResource(R.drawable.cola);
                    break;
                case "תוספת ביצה":
                    holder.ivFoodImg.setImageResource(R.drawable.egg);
                    break;
                case "חומוס/טחינה אישי":
                    holder.ivFoodImg.setImageResource(R.drawable.hummus);
                    break;
                case "מלוואח":
                    holder.ivFoodImg.setImageResource(R.drawable.malawach);
                    break;
                case "מלוואח מגולגל":
                    holder.ivFoodImg.setImageResource(R.drawable.mwrap);
                    break;
                case "מלוואח גבינות מגולגל":
                    holder.ivFoodImg.setImageResource(R.drawable.mwrapwh);
                    break;
            }
            if (!food.getName().equals("בקבוק שתיה 1.5 ל׳")) {
                holder.sDrinks.setVisibility(View.GONE);
            }
            if (food.getName().equals("בקבוק שתיה 1.5 ל׳")) {
                holder.tvDescription.setVisibility(View.GONE);
            }
            holder.tvDescription.setText(food.getDescription());
            holder.tvFoodPrice.setText("₪" + String.valueOf(food.getPrice()));
            holder.tvFoodName.setText(food.getName());
            if (!food.isAddonAvailable()) {
                holder.cbAddon.setVisibility(View.GONE);
            }
            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean hasAddon = holder.cbAddon.isChecked();
                    int productPrice = food.getPrice();
                    if (hasAddon) {
                        productPrice += 3;
                    }
                    String notes = "";
                    if (food.getName().equals("בקבוק שתיה 1.5 ל׳")) {
                        notes = holder.sDrinks.getSelectedItem().toString();
                    }
                    int qty = Integer.valueOf(holder.tvQty.getText().toString());
                    Product product = new Product(food, hasAddon, productPrice, false, notes, qty);
                    OrderLandingFragment orderLandingFragment = new OrderLandingFragment();
                    Bundle args = new Bundle();
                    args.putParcelable("Product", product);
                    orderLandingFragment.setArguments(args);
                    Log.d("SagiB", product.toString());
                    fragment.getFragmentManager().beginTransaction().replace(R.id.content, orderLandingFragment).commit();
                }
            });
            final View.OnClickListener minusL = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentQty = Integer.valueOf(holder.tvQty.getText().toString());
                    int newQty = currentQty - 1;
                    holder.tvQty.setText(String.valueOf(newQty));
                    if (newQty > 1) {
                        holder.ivMinus.setOnClickListener(this);
                        holder.ivMinus.clearColorFilter();
                    }
                    if (newQty < 2) {
                        holder.ivMinus.setOnClickListener(null);
                        holder.ivMinus.setColorFilter(Color.LTGRAY);
                    }
                }
            };
            View.OnClickListener plusL = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentQty = Integer.valueOf(holder.tvQty.getText().toString());
                    int newQty = currentQty + 1;
                    holder.tvQty.setText(String.valueOf(newQty));
                    if (newQty > 1) {
                        holder.ivMinus.setOnClickListener(minusL);
                        holder.ivMinus.clearColorFilter();
                    }
                    if (newQty < 2) {
                        holder.ivMinus.setOnClickListener(null);
                        holder.ivMinus.setColorFilter(Color.LTGRAY);
                    }
                }
            };
            holder.ivPlus.setOnClickListener(plusL);
            holder.ivMinus.setOnClickListener(minusL);
            if (Integer.valueOf(holder.tvQty.getText().toString()) < 2) {
                holder.ivMinus.setOnClickListener(null);
                holder.ivMinus.setColorFilter(Color.LTGRAY);
            }
        }

        @Override
        public int getItemCount() {
            return foodArrayList.size();
        }

        public class ProductsViewHolder extends RecyclerView.ViewHolder {

            SimpleDraweeView ivFoodImg;
            TextView tvFoodName;
            TextView tvDescription;
            TextView tvFoodPrice;
            BootstrapButton btnAdd;
            CheckBox cbAddon;
            Spinner sDrinks;
            ImageView ivPlus;
            ImageView ivMinus;
            TextView tvQty;

            public ProductsViewHolder(View itemView) {
                super(itemView);
                ivFoodImg = (SimpleDraweeView) itemView.findViewById(R.id.ivFoodImg);
                tvFoodName = (TextView) itemView.findViewById(R.id.tvFoodName);
                tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
                tvFoodPrice = (TextView) itemView.findViewById(R.id.tvFoodPrice);
                btnAdd = (BootstrapButton) itemView.findViewById(R.id.btnAdd);
                cbAddon = (CheckBox) itemView.findViewById(R.id.cbAddon);
                sDrinks = (Spinner) itemView.findViewById(R.id.sDrinks);
                ivPlus = (ImageView) itemView.findViewById(R.id.ivPlus);
                ivMinus = (ImageView) itemView.findViewById(R.id.ivMinus);
                tvQty = (TextView) itemView.findViewById(R.id.tvQty);
            }
        }
    }
}

