package com.example.android.vhsmonitoring.Admin.Controller;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.vhsmonitoring.Admin.Model.ClaimContract;
import com.example.android.vhsmonitoring.Admin.Model.CustomerAddress;
import com.example.android.vhsmonitoring.Admin.Model.StockData;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserCustomer;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserData;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserHandler;
import com.example.android.vhsmonitoring.Encryption;
import com.example.android.vhsmonitoring.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

// this class is only for debugging and developing purposes!
public class AdminController extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        Button btn_addStock = findViewById(R.id.addStockDataButton);
        Button btn_addUser = findViewById(R.id.addUsersButton);
        Button btn_addClaimContract = findViewById(R.id.addClaimContractButton);
        Button btn_addAddress = findViewById(R.id.addAddressButton);

        btn_addStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStockData();
            }
        });
        btn_addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addUserData(); }
        });
        btn_addClaimContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addClaimContract(); }
        });
        btn_addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCustomerAddress();
            }
        });
    }

    public void addUserData() {
        EditText et_userName = findViewById(R.id.addUserName);
        EditText et_userPassword = findViewById(R.id.addUserPassword);
        EditText et_userCode = findViewById(R.id.addUserCode);
        EditText et_userIcons = findViewById(R.id.addUserIcons);

        EditText et_affiliatedHandler = findViewById(R.id.addAffiliatedHandler);
        EditText et_affiliatedPertamina = findViewById(R.id.addUserPertaminaManager);
        EditText et_affiliatedCustomer = findViewById(R.id.addUserAffiliatedCustomer);
        EditText et_affiliatedStock = findViewById(R.id.addAffiliatedStock);

        RadioGroup rg_groupSelection = findViewById(R.id.addRoleRadioGroup);
        RadioButton rb_userRole = findViewById(rg_groupSelection.getCheckedRadioButtonId());

        Encryption encode = new Encryption();

        String name = et_userName.getText().toString();
        String password = encode.encrypt(et_userPassword.getText().toString());
        String code = encode.encrypt(et_userCode.getText().toString());
        String icon = et_userIcons.getText().toString();
        String role = rb_userRole.getText().toString();
        String id = db.child("userData").push().getKey();

        String handlerId = et_affiliatedHandler.getText().toString();
        String pertaminaId = et_affiliatedPertamina.getText().toString();
        String customerId = et_affiliatedCustomer.getText().toString();
        String stockId = et_affiliatedStock.getText().toString();

        switch (role) {
            case "handler":
                UserHandler handler = new UserHandler(id, name, password, code, role, icon, customerId, pertaminaId);
                if (id != null) {
                    db.child("data_user").child(id).setValue(handler).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("SUCCESS", id);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ERROR", "Error adding data!");
                        }
                    });
                }
                break;
            case "customer":
                UserCustomer customer = new UserCustomer(id, name, password, code, role, icon, stockId, handlerId, pertaminaId);
                if (id != null) {
                    db.child("data_user").child(id).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("SUCCESS", id);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ERROR", "Error adding data!");
                        }
                    });
                }
                break;
            case "pertamina":
                // need to be revised
                UserData user = new UserData(id, name, password, code, role, icon);
                if (id != null) {
                    db.child("data_user").child(id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("SUCCESS", id);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ERROR", "Error adding data!");
                        }
                    });
                }
                break;
        }
    }

    public void addStockData() {
        EditText et_stockType = findViewById(R.id.addStockType);
        EditText et_stockBuyer = findViewById(R.id.addStockBuyerId);
        EditText et_stockHandler = findViewById(R.id.addStockHandlerId);
        EditText et_stockManager = findViewById(R.id.addStockManagerId);
        EditText et_tankStatus = findViewById(R.id.addStockTankStatus);
        EditText et_tankRest = findViewById(R.id.addStockPriceTankRest);
        EditText et_price = findViewById(R.id.addStockPrice);

        String type = et_stockType.getText().toString();
        String buyer = et_stockBuyer.getText().toString();
        String handler = et_stockHandler.getText().toString();
        String manager = et_stockManager.getText().toString();
        String status = et_tankStatus.getText().toString();
        int rest = Integer.parseInt(et_tankRest.getText().toString());
        int price = Integer.parseInt(et_price.getText().toString());
        int sold = 0;
        String id = db.child("data_stock").push().getKey();

        StockData stock = new StockData(id, type, buyer, handler, manager, status, price, rest, sold);
        if (id != null) {
            db.child("data_stock").child(id).setValue(stock).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("SUCCESS", id);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ERROR", "Error adding data!");
                }
            });
        }
    }

    public void addClaimContract() {
        EditText et_claimAmount = findViewById(R.id.addClaimAmount);
        EditText et_claimHandlerId = findViewById(R.id.addClaimContractHandler);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Date today_date = new Date();

        boolean approval = false;
        String amount = et_claimAmount.getText().toString();
        String handlerId = et_claimHandlerId.getText().toString();
        String sent_date = date.format(today_date);
        String id = db.child("data_claim_contract").push().getKey();

        ClaimContract contract = new ClaimContract(id, amount, sent_date, approval, handlerId);
        if (id != null) {
            db.child("data_claim_contract").child(id).setValue(contract).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("SUCCESS", id);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ERROR", "Error adding data!");
                }
            });
        }
    }

    // public void addNotifications() {}
    public void addCustomerAddress() {
        EditText et_AddressOwner = findViewById(R.id.addAddressOwnerId);
        EditText et_city = findViewById(R.id.addCityAddress);
        EditText et_province = findViewById(R.id.addProvinceAddress);
        EditText et_postalCode = findViewById(R.id.addPostalCode);

        String customerId = et_AddressOwner.getText().toString();
        String city = et_city.getText().toString();
        String province = et_province.getText().toString();
        String postalCode = et_postalCode.getText().toString();
        String id = db.child("customer_address").push().getKey();

        CustomerAddress contract = new CustomerAddress(id, customerId, city, province, postalCode);
        if (id != null) {
            db.child("customer_address").child(id).setValue(contract).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("SUCCESS", id);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ERROR", "Error adding data!");
                }
            });
        }
    }
}