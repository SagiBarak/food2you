package com.sagib.food2you;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sagib.food2you.models.Product;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.sagib.food2you.R.drawable.food;

public class OrderLandingFragment extends Fragment {


    @BindView(R.id.btnAddProduct)
    BootstrapButton btnAddProduct;
    Unbinder unbinder;
    SharedPreferences prefs;
    Gson gson = new Gson();
    @BindView(R.id.rvProducts)
    RecyclerView rvProducts;
    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.btnContinue)
    BootstrapButton btnContinue;
    ArrayList<Product> products = new ArrayList<>();
    int totalPrice = 0;
    @BindView(R.id.tvNoItems)
    TextView tvNoItems;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int index = intent.getIntExtra("Index", 0);
            products.remove(index);
            LandingPageAdapter land = new LandingPageAdapter(products, getContext());
            rvProducts.setAdapter(land);
            if (products.size() == 0) {
                tvNoItems.setVisibility(View.VISIBLE);
                btnContinue.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                btnContinue.setTextColor(Color.LTGRAY);
                btnContinue.setClickable(false);
            }
            if (products.size() > 0) {
                tvNoItems.setVisibility(View.GONE);
                btnContinue.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                btnContinue.setTextColor(Color.WHITE);
                btnContinue.setClickable(true);
            }
            totalPrice = 0;
            for (Product product : products) {
                totalPrice += product.getProductPrice();
            }
            prefs.edit().remove("Products").commit();
            prefs.edit().putString("Products", gson.toJson(products)).commit();
            if (totalPrice < 50 && totalPrice > 0) {
                tvTotalPrice.setText(String.format("סה״כ: ₪%d +\nדמי משלוח: ₪10", totalPrice));
            } else {
                tvTotalPrice.setText(String.format("סה״כ: ₪%d", totalPrice));
            }
        }
    };
    BroadcastReceiver updateQtyRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int qty = intent.getIntExtra("Qty", 1);
            int idx = intent.getIntExtra("Idx", 0);
            Product product = products.get(idx);
            int currentPrice = product.getProductPrice() / product.getQty();
            product.setQty(qty);
            product.setProductPrice(currentPrice * qty);
            products.set(idx, product);
            LandingPageAdapter land = new LandingPageAdapter(products, getContext());
            rvProducts.setAdapter(land);
            totalPrice = 0;
            for (Product thisProduct : products) {
                totalPrice += thisProduct.getProductPrice();
            }
            prefs.edit().remove("Products").commit();
            prefs.edit().putString("Products", gson.toJson(products)).commit();
            if (totalPrice < 50 && totalPrice > 0) {
                tvTotalPrice.setText(String.format("סה״כ: ₪%d +\nדמי משלוח: ₪10", totalPrice));
            } else {
                tvTotalPrice.setText(String.format("סה״כ: ₪%d", totalPrice));
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_landing, container, false);
        unbinder = ButterKnife.bind(this, v);
        prefs = getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
        Product newProduct = null;
        btnContinue.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
        btnContinue.setTextColor(Color.LTGRAY);
        btnContinue.setClickable(false);
        String productsJson = prefs.getString("Products", null);
        if (productsJson != null) {
            products = gson.fromJson(productsJson, new TypeToken<ArrayList<Product>>() {
            }.getType());
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            boolean isNew = true;
            newProduct = getArguments().getParcelable("Product");
            int size = products.size();
            int count = 0;
            for (Product product : products) {
                if (product.getFood().getName().equals(newProduct.getFood().getName()) && product.getNotes().equals(newProduct.getNotes()) && product.isHasAddon() == newProduct.isHasAddon()) {
                    int idx = products.indexOf(product);
                    int newQty = product.getQty() + newProduct.getQty();
                    product.setQty(newQty);
                    int newPrice = product.getFood().getPrice();
                    if (product.isHasAddon()) {
                        newPrice += 3;
                    }
                    newPrice = newPrice * newQty;
                    product.setProductPrice(newPrice);
                    products.set(idx, product);
                    isNew = false;
                } else {
                    count++;
                }
            }
            if (count == size) {
                isNew = true;
            }
            if (isNew) {
                products.add(newProduct);
            }
        }
        if (products.size() > 0) {
            tvNoItems.setVisibility(View.GONE);
            btnContinue.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
            btnContinue.setClickable(true);
        }
        LandingPageAdapter adapter = new LandingPageAdapter(products, getContext());
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        if (products != null) {
            for (Product product : products) {
                totalPrice += product.getProductPrice();
            }
            prefs.edit().remove("Products").commit();
            prefs.edit().putString("Products", gson.toJson(products)).commit();
        }
        if (totalPrice < 50 && totalPrice > 0) {
            tvTotalPrice.setText(String.format("סה״כ: ₪%d +\nדמי משלוח: ₪10", totalPrice));
        } else {
            tvTotalPrice.setText(String.format("סה״כ: ₪%d", totalPrice));
        }
        IntentFilter filter = new IntentFilter("ItemRemoved");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
        IntentFilter updateQtyF = new IntentFilter("UpdateQty");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(updateQtyRec, updateQtyF);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("OrderFragment"));
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(updateQtyRec);
        super.onPause();
    }

    @Override
    public void onResume() {
        setTitle("ביצוע הזמנה - פריטים");
        super.onResume();
    }

    public void setTitle(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(getActivity());
        textView.setText(title);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(textView);
    }

    @OnClick(R.id.btnAddProduct)
    public void onBtnAddProductClicked() {
        getFragmentManager().beginTransaction().replace(R.id.content, new ProductsFragment()).commit();
    }

    @OnClick(R.id.btnContinue)
    public void onBtnContinueClicked() {
        if (totalPrice == 0) {
            Toast.makeText(getContext(), "לא נבחרו פריטים!", Toast.LENGTH_SHORT).show();
        } else {
            FinalFragment finalFragment = new FinalFragment();
            Bundle args = new Bundle();
            String fullOrderString = gson.toJson(products);
            args.putString("FullOrder", fullOrderString);
            finalFragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.content, finalFragment, "Final").commit();
        }
    }

    public class LandingPageAdapter extends RecyclerView.Adapter<LandingPageAdapter.LandingPageViewHolder> {

        ArrayList<Product> productArrayList;
        LayoutInflater inflater;
        Context context;
        int productPrice;

        public LandingPageAdapter(ArrayList<Product> productArrayList, Context context) {
            this.productArrayList = productArrayList;
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public LandingPageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.product_item, parent, false);
            return new LandingPageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final LandingPageViewHolder holder, int position) {
            final Product product = productArrayList.get(position);
            productPrice = product.getFood().getPrice();
            if (product.isHasAddon()) {
                productPrice += 3;
            }
            holder.tvFoodName.setText(product.getFood().getName());
            int totalPrice = product.getProductPrice();
            holder.tvPrice.setText("₪" + String.valueOf(totalPrice));
            if (product.isHasAddon()) {
                holder.tvAddon.setText("+ תוספת ביצה");
            } else if (!product.isHasAddon() && product.getFood().isAddonAvailable()) {
                holder.tvAddon.setText("לא נבחרה תוספת");
            } else if (product.getFood().getName().equals("בקבוק שתיה 1.5 ל׳")) {
                holder.tvAddon.setText(product.getNotes());
            } else {
                holder.tvAddon.setText("");
            }
            holder.tvQty.setText(String.valueOf(product.getQty()));
            holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("ItemRemoved");
                    intent.putExtra("Index", holder.getAdapterPosition());
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
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
                    }
                    if (newQty < 2) {
                        holder.ivMinus.setOnClickListener(null);
                    }
                    int newPrice = productPrice * newQty;
                    holder.tvPrice.setText("₪" + String.valueOf(newPrice));
                    Intent intent = new Intent("UpdateQty");
                    intent.putExtra("Qty", newQty);
                    intent.putExtra("Idx", holder.getAdapterPosition());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
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
                    }
                    if (newQty < 2) {
                        holder.ivMinus.setOnClickListener(null);
                    }
                    int newPrice = productPrice * newQty;
                    holder.tvPrice.setText("₪" + String.valueOf(newPrice));
                    Intent intent = new Intent("UpdateQty");
                    intent.putExtra("Qty", newQty);
                    intent.putExtra("Idx", holder.getAdapterPosition());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            };
            holder.ivPlus.setOnClickListener(plusL);
            holder.ivMinus.setOnClickListener(minusL);
            if (product.getQty() < 2) {
                holder.ivMinus.setOnClickListener(null);
            }
            switch (product.getFood().getName()) {
                case "ג׳חנון":
                    holder.ivFoodImg.setImageResource(food);
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
        }

        @Override
        public int getItemCount() {
            return productArrayList.size();
        }

        public class LandingPageViewHolder extends RecyclerView.ViewHolder {

            SimpleDraweeView ivFoodImg;
            TextView tvFoodName;
            TextView tvAddon;
            TextView tvPrice;
            BootstrapButton btnRemove;
            ImageView ivPlus;
            ImageView ivMinus;
            TextView tvQty;

            public LandingPageViewHolder(View itemView) {
                super(itemView);
                ivFoodImg = (SimpleDraweeView) itemView.findViewById(R.id.ivFoodImg);
                tvFoodName = (TextView) itemView.findViewById(R.id.tvFoodName);
                tvAddon = (TextView) itemView.findViewById(R.id.tvAddon);
                tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
                btnRemove = (BootstrapButton) itemView.findViewById(R.id.btnRemove);
                ivMinus = (ImageView) itemView.findViewById(R.id.ivMinus);
                ivPlus = (ImageView) itemView.findViewById(R.id.ivPlus);
                tvQty = (TextView) itemView.findViewById(R.id.tvQty);
            }
        }
    }
}
