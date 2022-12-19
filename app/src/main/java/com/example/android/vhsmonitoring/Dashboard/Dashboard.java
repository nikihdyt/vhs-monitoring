package com.example.android.vhsmonitoring.Dashboard;
import static com.example.android.vhsmonitoring.SplashScreeen.MainActivity.sessions_userRole;
import static com.example.android.vhsmonitoring.SplashScreeen.MainActivity.sharedpreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.android.vhsmonitoring.Admin.Model.ClaimContract;
import com.example.android.vhsmonitoring.Admin.Model.CustomerAddress;
import com.example.android.vhsmonitoring.Admin.Model.StockData;
import com.example.android.vhsmonitoring.Admin.Model.StockDistributions.DailyPickupData;
import com.example.android.vhsmonitoring.Admin.Model.StockDistributions.RestockData;
import com.example.android.vhsmonitoring.Admin.Model.StockDistributions.StockOpnameData;
import com.example.android.vhsmonitoring.Admin.Model.StockDistributions.StockRequest;
import com.example.android.vhsmonitoring.Admin.Model.StockDistributions.StockTransactions;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserCustomer;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserData;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserHandler;
import com.example.android.vhsmonitoring.Approval.Approval;
import com.example.android.vhsmonitoring.Dashboard.stockrequest.StockRequestAdapter;
import com.example.android.vhsmonitoring.Dashboard.stockrequest.StockRequestModel;
import com.example.android.vhsmonitoring.Encryption;
import com.example.android.vhsmonitoring.InputData.InputData;
import com.example.android.vhsmonitoring.Notifications.NotificationActivity;
import com.example.android.vhsmonitoring.R;
import com.example.android.vhsmonitoring.InputData.StockOpname;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    public List<StockTransactions> stockHistories = new ArrayList<>();
    public DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    public SharedPreferences sessions;
    public Gson gsonData = new Gson();
    public String jSonData;

    public UserHandler handler;
    public UserCustomer handlerCustomerData;
    public CustomerAddress handlerCustomerAddress;
    public StockData handlerCustomerStock;
    public ClaimContract claimContract;

    public UserCustomer customer;
    public CustomerAddress SelfCustomerAddress;
    public StockData selfStockData;

    public UserData pertamina;
    public StockData pertaminaCustomerStockData;
    public StockRequest handlerRestockRequest;
    public int approvalNeededAmount = 0;
    public int activeTransaction = 0;
    public int stockRequest = 0;
    public List<StockData> pertaminaCustomers = new ArrayList<>();
    public List<StockRequest> stockRequestsData = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get user data
        sessions = getSharedPreferences(sharedpreferences, Context.MODE_PRIVATE);
        jSonData = sessions.getString("userSessions", "");

        // check user role and display dashboard based on their role
        if ("handler".equals(sessions.getString(sessions_userRole, ""))) {
            handler = gsonData.fromJson(jSonData, UserHandler.class);
            // get handler claim contract
            getHandlerClaimContract();

            // view handler dashboard
            viewHandlerDashboard();

        } else if ("customer".equals(sessions.getString(sessions_userRole, ""))) {
            customer = gsonData.fromJson(jSonData, UserCustomer.class);

            // view customer dashboard
            viewCustomerDashboard();
        } else if ("pertamina".equals(sessions.getString(sessions_userRole, ""))) {
            pertamina = gsonData.fromJson(jSonData, UserData.class);
            viewPertaminaDashboard();
        }
    }

    // handler dashboard
    public void viewHandlerDashboard() {
        // set content view to handler dashboard
        setContentView(R.layout.activity_beranda);

        // get all needed views for handler dashboard
        ConstraintLayout inputCard = findViewById(R.id.input_expanded_view);
        CardView overviewCard = findViewById(R.id.card_overview_and_input);
        ExtendedFloatingActionButton btnInput = findViewById(R.id.btn_input);
        ImageView icTankRest = findViewById(R.id.ic_TANK_REST);
        TextView tvTankRest = findViewById(R.id.tv_TANK_REST);
        TextView tvTankRestTimeRemaining = findViewById(R.id.tv_tank_rest_time_remaining);
        TextView tvUserName = findViewById(R.id.tv_user_name);
        TextView tvUserId = findViewById(R.id.tv_user_id);
        TextView tvCalendar = findViewById(R.id.tv_calendar);
        ImageButton btnTankRest = findViewById(R.id.btn_add_tank_rest);
        ImageButton btnNotification = findViewById(R.id.btnNotification);
        ImageButton btnArrivedStock = findViewById(R.id.btn_add_arrived_stock);
        ImageButton btnDailyPickup = findViewById(R.id.btn_add_daily_pickup);

        // views for history
        RecyclerView recycler = findViewById(R.id.historyRecycler);

        // get customer's data
        fillCustomerOverviewCard();

        // prepare encryption
        Encryption encode = new Encryption();

        // get today date
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        // set dashboard basic information
        boolean isInputCardVisible = false;
        icTankRest.setImageResource(R.drawable.ic_progress_doing);
        tvUserName.setText(handler.getName());
        tvUserId.setText(encode.decrypt(handler.getCode()));
        tvCalendar.setText(dateFormat.format(date));
        tvTankRest.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_700));
        tvTankRestTimeRemaining.setVisibility(View.VISIBLE);
        inputCard.setVisibility(View.GONE);
        btnTankRest.setVisibility(View.VISIBLE);
        btnTankRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, StockOpname.class);
                startActivity(intent);
            }
        });

        // input button
        btnInput.bringToFront();
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputCard.getVisibility()==View.GONE) {
                    TransitionManager.beginDelayedTransition(overviewCard, new AutoTransition());
                    inputCard.setVisibility(View.VISIBLE);
                    // btnInput.setBackgroundColor(getResources().getColor(R.color.primary_700));
                    // btnInput.setTextColor(getResources().getColor(R.color.primary_200));
                } else {
                    TransitionManager.beginDelayedTransition(overviewCard, new AutoTransition());
                    inputCard.setVisibility(View.GONE);
                    // btnInput.setBackgroundColor(getResources().getColor(R.color.primary_200));
                    // btnInput.setTextColor(getResources().getColor(R.color.primary_700));
                }
            }
        });

        // notification button
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        // tank rest button
        btnTankRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, StockOpname.class);
                String json_handler = gsonData.toJson(handler);
                String json_handlerCustomerData = gsonData.toJson(handlerCustomerData);
                String json_handlerCustomerAddress = gsonData.toJson(handlerCustomerAddress);
                String json_handlerCustomerStock = gsonData.toJson(handlerCustomerStock);
                String json_claimContract = gsonData.toJson(claimContract);
                intent.putExtra("handler", json_handler);
                intent.putExtra("handlerCustomerData", json_handlerCustomerData);
                intent.putExtra("handlerCustomerAddress", json_handlerCustomerAddress);
                intent.putExtra("handlerCustomerStock", json_handlerCustomerStock);
                intent.putExtra("claimContract", json_claimContract);
                intent.putExtra("typeInput", "ARRIVED STOCK");
                startActivity(intent);
            }
        });

        // arrivedStock button
        btnArrivedStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, InputData.class);
                String json_handler = gsonData.toJson(handler);
                String json_handlerCustomerData = gsonData.toJson(handlerCustomerData);
                String json_handlerCustomerAddress = gsonData.toJson(handlerCustomerAddress);
                String json_handlerCustomerStock = gsonData.toJson(handlerCustomerStock);
                String json_claimContract = gsonData.toJson(claimContract);
                intent.putExtra("handler", json_handler);
                intent.putExtra("handlerCustomerData", json_handlerCustomerData);
                intent.putExtra("handlerCustomerAddress", json_handlerCustomerAddress);
                intent.putExtra("handlerCustomerStock", json_handlerCustomerStock);
                intent.putExtra("claimContract", json_claimContract);
                intent.putExtra("inputType", "DAILY PICKUP");
                startActivity(intent);
            }
        });

        // daily pickup
        btnDailyPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, InputData.class);
                String json_handler = gsonData.toJson(handler);
                String json_handlerCustomerData = gsonData.toJson(handlerCustomerData);
                String json_handlerCustomerAddress = gsonData.toJson(handlerCustomerAddress);
                String json_handlerCustomerStock = gsonData.toJson(handlerCustomerStock);
                String json_claimContract = gsonData.toJson(claimContract);
                intent.putExtra("handler", json_handler);
                intent.putExtra("handlerCustomerData", json_handlerCustomerData);
                intent.putExtra("handlerCustomerAddress", json_handlerCustomerAddress);
                intent.putExtra("handlerCustomerStock", json_handlerCustomerStock);
                intent.putExtra("claimContract", json_claimContract);
                startActivity(intent);
            }
        });
    }
    public void getHandlerClaimContract() {
        db.child("data_claim_contract").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    claimContract = item.getValue(ClaimContract.class);
                    if (claimContract.getHandlerId().equals(handler.getId())) {
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void fillCustomerOverviewCard() {
        ImageView ivLogoCustomer = findViewById(R.id.logo_customer);
        TextView tvCustomerName = findViewById(R.id.tv_customer_name);
        TextView tvCustomerAddress = findViewById(R.id.tv_customer_address);
        TextView tvCustomerStockType = findViewById(R.id.tv_customer_stok);
        TextView tvCurrentStock = findViewById(R.id.tv_current_stock);
        TextView tvSold = findViewById(R.id.tv_sold);
        TextView tvInputTitle = findViewById(R.id.tv_input_title);
        TextView tvOverviewStatusTime = findViewById(R.id.tv_overview_status_time);

        TextView tvStatus = findViewById(R.id.tv_status);

        // check customer data in database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference customerReference = database.getReference("data_user/" + handler.getCustomerId());
        customerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserCustomer dataCustomer = snapshot.getValue(UserCustomer.class);
                String jsonCustomerData = gsonData.toJson(dataCustomer);
                handlerCustomerData = gsonData.fromJson(jsonCustomerData, UserCustomer.class);

                if ("default".equals(handlerCustomerData.getIcons())) {
                    ivLogoCustomer.setBackgroundResource(R.drawable.logo_customer);
                }
                tvCustomerName.setText(handlerCustomerData.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerData");
            }
        });

        // get customer address
        db.child("customer_address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    handlerCustomerAddress = item.getValue(CustomerAddress.class);
                    if (handlerCustomerAddress.getCustomerId().equals(handlerCustomerData.getId())) {
                        tvCustomerAddress.setText(String.format("%s, %s %s", handlerCustomerAddress.getCity(), handlerCustomerAddress.getProvince(), handlerCustomerAddress.getPostalCode()));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerAddress");
            }
        });

        // get customer stock data
        db.child("data_stock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    handlerCustomerStock = item.getValue(StockData.class);
                    if (handlerCustomerStock.getBuyerId().equals(handlerCustomerData.getId())) {
                        String currentStock = handlerCustomerStock.getTank_rest() + "kL";
                        String stockSold = handlerCustomerStock.getSold() + "kL";

                        //set stock data
                        tvCustomerStockType.setText(String.format("stock: %s", handlerCustomerStock.getType()));
                        tvCurrentStock.setText(currentStock);
                        tvSold.setText(stockSold);

                        // get all unfinished stock distribution
                        db.child("data_stock/" + handlerCustomerData.getStockId() + "/stockDistributions").orderByChild("date_sent").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot item : snapshot.getChildren()) {
                                    StockTransactions stockDistribution = item.getValue(StockTransactions.class);
                                    if ("StockOpname".equals(stockDistribution.getType())) {
                                        StockOpnameData dataStock = item.getValue(StockOpnameData.class);
                                        assert dataStock != null;
                                        dataStock.checkStatus();
                                        tvInputTitle.setText(dataStock.getStatus());
                                        if (!"Approved".equals(dataStock.getStatus())) {
                                            tvOverviewStatusTime.setText(String.format("Requested on %s", dataStock.getDate_sent()));
                                            tvStatus.setText(R.string.approval_needed);
                                            tvStatus.setTextColor(getResources().getColor(R.color.tertiary_200));
                                        } else {
                                            tvStatus.setText(R.string.nothing_to_do);
                                        }
                                        break;
                                    } else if ("InputData".equals(stockDistribution.getType())) {
                                        DailyPickupData dataStock = item.getValue(DailyPickupData.class);
                                        assert dataStock != null;
                                        dataStock.checkStatus();
                                        tvInputTitle.setText(dataStock.getStatus());
                                        if (!"Approved".equals(dataStock.getStatus())) {
                                            tvOverviewStatusTime.setText(String.format("Requested on %s", dataStock.getDate_sent()));
                                            tvStatus.setText(R.string.approval_needed);
                                            tvStatus.setTextColor(getResources().getColor(R.color.tertiary_200));
                                        } else {
                                            tvStatus.setText(R.string.nothing_to_do);
                                        }
                                        break;
                                    } else if ("Restock".equals(stockDistribution.getType())) {
                                        RestockData dataStock = item.getValue(RestockData.class);
                                        assert dataStock != null;
                                        dataStock.checkStatus();
                                        tvInputTitle.setText(dataStock.getStatus());
                                        if (!"Approved".equals(dataStock.getStatus())) {
                                            tvOverviewStatusTime.setText(String.format("Requested on %s", dataStock.getDate_sent()));
                                            tvStatus.setText(R.string.approval_needed);
                                            tvStatus.setTextColor(getResources().getColor(R.color.tertiary_200));
                                        } else {
                                            tvStatus.setText(R.string.nothing_to_do);
                                        }
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("ERROR", "error while taking handlerCustomerStockData");
                            }
                        });

                        Log.d("dataStatus", "Here");
                        // get stock distributions histories
                        db.child("data_stock/" + handlerCustomerData.getStockId() + "/stockDistributions").orderByChild("date_sent").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot item : snapshot.getChildren()) {
                                    // save to StockTransactions object
                                    StockTransactions transactionsHistory = item.getValue(StockTransactions.class);
                                    Log.d("dataStatus", transactionsHistory.getId());
                                    // seperate the data based on their type and update the status
                                    if ("StockOpname".equals(transactionsHistory.getType())) {
                                        StockOpnameData stockHistory = snapshot.getValue(StockOpnameData.class);
                                        stockHistory.checkStatus();
                                        transactionsHistory.setStatus(stockHistory.getStatus());
                                    } else if ("InputData".equals(transactionsHistory.getType())) {
                                        DailyPickupData stockHistory = snapshot.getValue(DailyPickupData.class);
                                        stockHistory.checkStatus();
                                        transactionsHistory.setStatus(stockHistory.getStatus());
                                    } else if ("Restock".equals(transactionsHistory.getType())) {
                                        RestockData stockHistory = snapshot.getValue(RestockData.class);
                                        stockHistory.checkStatus();
                                        transactionsHistory.setStatus(stockHistory.getStatus());
                                    }

                                    // add processed data to an array
                                    // Log.d("dataStatus", transactionsHistory.getId());
                                    stockHistories.add(transactionsHistory);
                                }

                                // check array of data
                                Log.d("length", String.valueOf(stockHistories.size()));
                                if (stockHistories.size() > 0) {
                                    RecyclerView historyRecycler = findViewById(R.id.historyRecycler);
                                    HistoryAdapter adapter = new HistoryAdapter(Dashboard.this, stockHistories);
                                    historyRecycler.setAdapter(adapter);
                                    historyRecycler.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                                    historyRecycler.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("ERROR", "error while taking handlerCustomerStockData");
                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerStockData");
            }
        });
    }

    // customer dashboard
    public void viewCustomerDashboard() {
        // set layout for customer dashboard
        setContentView(R.layout.activity_beranda);

        // views for buttons
        ExtendedFloatingActionButton btnInput = findViewById(R.id.btn_input);
        ExtendedFloatingActionButton btnApproval = findViewById(R.id.btn_approval_customer);
        ImageButton btnNotification = findViewById(R.id.btnNotification);

        TextView tvHistoryHandler = findViewById(R.id.tv_history);
        TextView tvHistoryCustomer = findViewById(R.id.tv_history_customer);
        TextView tvTankRest = findViewById(R.id.tv_TANK_REST);
        TextView tvTankRestTimeRemaining = findViewById(R.id.tv_tank_rest_time_remaining);

        // views for filter
        ImageView icFilterHandler = findViewById(R.id.ic_filter);
        LinearLayout groupHistoryFilterHandler = findViewById(R.id.group_histories);

        // control vies visibilities
        btnInput.setVisibility(View.GONE);
        btnApproval.setVisibility(View.VISIBLE);
        tvHistoryHandler.setVisibility(View.GONE);
        tvHistoryCustomer.setVisibility(View.VISIBLE);
        icFilterHandler.setVisibility(View.GONE);
        groupHistoryFilterHandler.setVisibility(View.GONE);

        // add button listener
        btnApproval.bringToFront();
        btnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Approval.class);
                intent.putExtra("approvalType", "DAILY PICKUP");
                startActivity(intent);
            }
        });

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        // get user data
        fillCustomerSelfOverviewCard();
    }
    public void fillCustomerSelfOverviewCard() {
        // views for user data
        ImageView ivUserProfile = findViewById(R.id.img_user_profile);
        TextView tvUserName = findViewById(R.id.tv_user_name);
        TextView tvUserId = findViewById(R.id.tv_user_id);
        TextView tvCalendar = findViewById(R.id.tv_calendar);

        // views for user overview
        ImageView ivLogoCustomer = findViewById(R.id.logo_customer);
        TextView tvCustomerName = findViewById(R.id.tv_customer_name);
        TextView tvCustomerAddress = findViewById(R.id.tv_customer_address);
        TextView tvCustomerStockType = findViewById(R.id.tv_customer_stok);

        TextView tvCurrentStock = findViewById(R.id.tv_current_stock);
        TextView tvSold = findViewById(R.id.tv_sold);
        TextView tvInputTitle = findViewById(R.id.tv_input_title);
        TextView tvOverviewStatusTime = findViewById(R.id.tv_overview_status_time);

        Encryption encode = new Encryption();
        tvUserName.setText(customer.getName());
        tvUserId.setText(encode.decrypt(customer.getCode()));
        tvCustomerName.setText(customer.getName());

        if ("default".equals(customer.getIcons())) {
            ivLogoCustomer.setBackgroundResource(R.drawable.logo_customer);
        }

        // get customer address
        db.child("customer_address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    SelfCustomerAddress = item.getValue(CustomerAddress.class);
                    if (customer.getId().equals(SelfCustomerAddress.getCustomerId())) {
                        tvCustomerAddress.setText(String.format("%s, %s %s", SelfCustomerAddress.getCity(), SelfCustomerAddress.getProvince(), SelfCustomerAddress.getPostalCode()));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerAddress");
            }
        });

        // get customer stock data
        db.child("data_stock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    selfStockData = item.getValue(StockData.class);
                    if (selfStockData.getBuyerId().equals(customer.getId())) {
                        String currentStock = selfStockData.getTank_rest() + "kL";
                        String stockSold = selfStockData.getSold() + "kL";

                        //set stock data
                        tvCustomerStockType.setText(selfStockData.getType());
                        tvCurrentStock.setText(currentStock);
                        tvSold.setText(stockSold);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerStockData");
            }
        });

        tvInputTitle.setText(R.string.fresh_stock);
        tvOverviewStatusTime.setText(R.string.fresh_stock);
    }

    // pertamina dashboard
    public void viewPertaminaDashboard() {
        // set layout for pertamina dashboard
        setContentView(R.layout.activity_beranda_pusat);

        // get today date
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        // views for pertamina dashboard
        TextView tvUserName = findViewById(R.id.tv_user_name);
        TextView tvUserId = findViewById(R.id.tv_user_id);
        TextView tvCalendar = findViewById(R.id.tv_calendar);
        TextView tvApprovalNeeded = findViewById(R.id.tv_approval_needed_amount);
        TextView tvActiveTransactionAmount =findViewById(R.id.tv_active_transaction_amount);
        TextView tvStockRequestAmount = findViewById(R.id.tv_stock_request_amount);

        Encryption encode = new Encryption();

        tvUserName.setText(pertamina.getName());
        tvUserId.setText(encode.decrypt(pertamina.getCode()));
        tvCalendar.setText(dateFormat.format(date));

        // approval needed for pertamina
        db.child("data_stock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    pertaminaCustomerStockData = item.getValue(StockData.class);
                    assert pertaminaCustomerStockData != null;
                    if (pertaminaCustomerStockData.getManagerId().equals(pertamina.getId()) && "finished".equals(pertaminaCustomerStockData.getTank_status())) {
                        db.child("data_stock").child(pertaminaCustomerStockData.getId()).child("stockDistributions").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot item : snapshot.getChildren()) {
                                    StockTransactions stockData = item.getValue(StockTransactions.class);
                                    if ("StockOpname".equals(stockData.getType())) {
                                        StockOpnameData data = item.getValue(StockOpnameData.class);
                                        assert data != null;
                                        if (!data.isApproval_pertamina()) { approvalNeededAmount += 1; }
                                    } else if ("Restock".equals(stockData.getType())) {
                                        StockOpnameData data = item.getValue(StockOpnameData.class);
                                        assert data != null;
                                        if (!data.isApproval_pertamina()) { approvalNeededAmount += 1; }
                                    } else if ("DailyPickup".equals(stockData.getType())) {
                                        StockOpnameData data = item.getValue(StockOpnameData.class);
                                        assert data != null;
                                        if (!data.isApproval_pertamina()) { approvalNeededAmount += 1; }
                                    }
                                }

                                if (!"finished".equals(pertaminaCustomerStockData.getTank_status())) {
                                    activeTransaction += 1;
                                    pertaminaCustomers.add(pertaminaCustomerStockData);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("ERROR", "error while taking handlerCustomerStockData");
                            }
                        });
                    }
                }

                tvApprovalNeeded.setText(String.valueOf(approvalNeededAmount));
                tvActiveTransactionAmount.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerStockData");
            }
        });

        // stock request for pertamina
        db.child("data_stock_request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    handlerRestockRequest = item.getValue(StockRequest.class);
                    assert handlerRestockRequest != null;
                    if (!handlerRestockRequest.isStatus()) {
                        stockRequest += 1;
                        stockRequestsData.add(handlerRestockRequest);
                    }
                }

                tvStockRequestAmount.setText(String.valueOf(stockRequest));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerStockData");
            }
        });

        // configure restock stack
        stockRequestStack();

    }

    private void stockRequestStack() {
        ViewPager mStockRequestsViewPager = findViewById(R.id.vp_stock_request);
        ArrayList<StockRequestModel> mStcoRequestsList = new ArrayList<>();

        List<Integer> custLogo = new ArrayList<>();
        List<String> custName = new ArrayList<>();
        List<String> custStock = new ArrayList<>();

        // add data from stock request to be displayed in stack
        for (int i = 0; i < stockRequestsData.size(); i++) {
            if ("default".equals(stockRequestsData.get(i).getCustomerLogo())) {
                custLogo.add(R.drawable.logo_customer);
            }
            custName.add(stockRequestsData.get(i).getCustomerName());
            custStock.add(stockRequestsData.get(i).getCustomerStockType());
        }

        for(int j = 0; j < custLogo.size(); j++) {
            StockRequestModel stockRequestModel = new StockRequestModel();
            stockRequestModel.setCustLogo(custLogo.get(j));
            stockRequestModel.setCustName(custName.get(j));
            stockRequestModel.setStokType(custStock.get(j));
            mStcoRequestsList.add(stockRequestModel);
        }

        StockRequestAdapter mStockRequestAdapter = new StockRequestAdapter(mStcoRequestsList,this);
        mStockRequestsViewPager.setPageTransformer(true, new StockRequestViewPagerStack());
        mStockRequestsViewPager.setOffscreenPageLimit(2);
        mStockRequestsViewPager.setAdapter(mStockRequestAdapter);
    }
    private class StockRequestViewPagerStack implements ViewPager.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position >= 0) {
                page.setTranslationX(page.getWidth() * position);
                page.setTranslationY(30 * position);
                // page.setScaleX(0.7f - 0.05f * position);
                // page.setScaleY(0.7f);
            }
        }
    }
}




