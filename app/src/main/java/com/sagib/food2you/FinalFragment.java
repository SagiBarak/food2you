package com.sagib.food2you;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sagib.food2you.models.Order;
import com.sagib.food2you.models.Product;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class FinalFragment extends Fragment {

    Gson gson = new Gson();
    @BindView(R.id.btnContinue)
    BootstrapButton btnContinue;
    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.etFullName)
    EditText etFullName;
    @BindView(R.id.tilFullName)
    TextInputLayout tilFullName;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.tilPhoneNumber)
    TextInputLayout tilPhoneNumber;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.tilAddress)
    TextInputLayout tilAddress;
    @BindView(R.id.etCity)
    EditText etCity;
    @BindView(R.id.tilCity)
    TextInputLayout tilCity;
    @BindView(R.id.etHouseNumber)
    EditText etHouseNumber;
    @BindView(R.id.tilHouseNumber)
    TextInputLayout tilHouseNumber;
    @BindView(R.id.etFloorNumber)
    EditText etFloorNumber;
    @BindView(R.id.tilFloorNumber)
    TextInputLayout tilFloorNumber;
    @BindView(R.id.etAptNumber)
    EditText etAptNumber;
    @BindView(R.id.tilAptNumber)
    TextInputLayout tilAptNumber;
    @BindView(R.id.etNotes)
    EditText etNotes;
    @BindView(R.id.tilNotes)
    TextInputLayout tilNotes;
    int totalPrice;
    ArrayList<Product> products = new ArrayList<>();
    Unbinder unbinder;
    @BindView(R.id.tvShipDate)
    TextView tvShipDate;
    @BindView(R.id.sMinutes)
    Spinner sMinutes;
    @BindView(R.id.sHour)
    Spinner sHour;
    FirebaseDatabase mDatabase;
    String orderNumber;
    DatabaseReference myRef;
    String orderUID;
    String hour;
    String futureDate;
    AlertDialog show;
    String dateString;
    @BindView(R.id.btnBack)
    BootstrapButton btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_final, container, false);
        unbinder = ButterKnife.bind(this, v);
        mDatabase = FirebaseDatabase.getInstance();
        products = gson.fromJson(getArguments().getString("FullOrder"), new TypeToken<ArrayList<Product>>() {
        }.getType());
        for (Product product : products) {
            totalPrice += product.getProductPrice();
        }
        if (totalPrice < 50 && totalPrice > 0) {
            tvTotalPrice.setText(String.format("סה״כ: ₪%d +\nדמי משלוח: ₪10", totalPrice));
        } else {
            tvTotalPrice.setText(String.format("סה״כ: ₪%d", totalPrice));
        }
        Calendar c = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (today.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH) && today.get(Calendar.MONTH) == c.get(Calendar.MONTH) && today.get(Calendar.YEAR) == c.get(Calendar.YEAR) && today.get(Calendar.HOUR_OF_DAY) >= 17) {
            c.add(Calendar.DAY_OF_MONTH, 1);
            sHour.setAdapter(getHoursSpinner());
            while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        String testString = String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(c.get(Calendar.MONTH) + 1) + "/" + String.valueOf(c.get(Calendar.YEAR));
        if (testString.equals("30/9/2017")) {
            c.add(Calendar.DAY_OF_MONTH, 1);
            while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        if (today.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH) && today.get(Calendar.MONTH) == c.get(Calendar.MONTH) && today.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
            sHour.setAdapter(getHoursSpinner());
        }
        dateString = String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(c.get(Calendar.MONTH) + 1) + "/" + String.valueOf(c.get(Calendar.YEAR));
        tvShipDate.setText(dateString);
        if (dateString.equals("7/10/2017")) {
            Double newPrice = totalPrice * 0.90;
            if (newPrice < 50 && newPrice > 0) {
                tvTotalPrice.setText(String.format("סה״כ: ₪%d +\nדמי משלוח: ₪10", newPrice.intValue()));
            } else {
                tvTotalPrice.setText(String.format("סה״כ: ₪%d", newPrice.intValue()));
            }
            totalPrice = newPrice.intValue();
            tvShipDate.setText(dateString + " - *מבצע: 10% הנחה!*");
        }
        btnBack.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.content, new OrderLandingFragment()).commit();
            }
        });
        return v;
    }

    private ArrayAdapter<String> getHoursSpinner() {
        ArrayList<String> spinnerArray = new ArrayList<String>();
        int i = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (i <= 16 && i >= 7) {
            for (int j = i + 1; j < 18; j++) {
                if (j < 10) {
                    spinnerArray.add("0" + String.valueOf(j));
                } else {
                    spinnerArray.add(String.valueOf(j));
                }
            }
        } else {
            for (int j = 8; j < 18; j++) {
                if (j < 10) {
                    spinnerArray.add("0" + String.valueOf(j));
                } else {
                    spinnerArray.add(String.valueOf(j));
                }
            }
        }
        return new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
    }

    @Override
    public void onResume() {
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ג׳חנון ביתי - עד הבית");
        setTitle("ביצוע הזמנה - משלוח");
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnContinue)
    public void onBtnContinueClicked() {
        AlertDialog.Builder pleaseWait = new AlertDialog.Builder(getContext());
        show = pleaseWait.setMessage("אנא המתן...").setCancelable(false).show();
        boolean isValidated = true;
        if (etAddress.getText().toString().length() < 1 || etCity.getText().toString().length() < 1 || etFullName.getText().toString().length() < 1 || etPhoneNumber.getText().toString().length() < 1 || etHouseNumber.getText().toString().length() < 1) {
            isValidated = false;
            show.dismiss();
            if (etAddress.getText().toString().length() < 1) {
                tilAddress.setError("חובה למלא כתובת!");
            }
            if (etCity.getText().toString().length() < 1) {
                tilCity.setError("חובה למלא עיר!");
            }
            if (etFullName.getText().toString().length() < 1) {
                tilFullName.setError("חובה למלא שם מלא!");
            }
            if (etPhoneNumber.getText().toString().length() < 1) {
                tilPhoneNumber.setError("חובה למלא מספר טלפון!");
            }
            if (etHouseNumber.getText().toString().length() < 1) {
                tilHouseNumber.setError("חובה למלא מספר בית!");
            }
        }
        if (isValidated) {
            if (totalPrice < 50) {
                totalPrice += 10;
            }
            hour = sHour.getSelectedItem().toString() + ":" + sMinutes.getSelectedItem().toString();
            futureDate = dateString;
            myRef = mDatabase.getReference().child("Orders").child("New").push();
            orderUID = myRef.getKey();
            FirebaseDatabase.getInstance().getReference("currentOrderUID").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        long num = dataSnapshot.getValue(Long.class);
                        orderNumber = String.valueOf(num);
                        num += 1;
                        FirebaseDatabase.getInstance().getReference("currentOrderUID").setValue(num);
                        Order order = new Order(etFullName.getText().toString(), etPhoneNumber.getText().toString(), etAddress.getText().toString(), etHouseNumber.getText().toString(), etAptNumber.getText().toString(), etFloorNumber.getText().toString(), etCity.getText().toString(), hour, futureDate, LocalDateTime.now().toString(), etNotes.getText().toString(), products, totalPrice, orderUID, orderNumber);
                        myRef.setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                show.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("ההזמנה נשלחה בהצלחה!" + "\nתודה שבחרת בג׳חנון ביתי - עד הבית!\nבתאבון!");
                                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        BottomNavigationView navigation = (BottomNavigationView) ((AppCompatActivity) getActivity()).findViewById(R.id.navigation);
                                        navigation.setSelectedItemId(R.id.navigation_info);
                                    }
                                });
                                builder.show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                show.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("לצערנו, ארעה תקלה במהלך הפעולה.\nאנא נסה שנית.");
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
