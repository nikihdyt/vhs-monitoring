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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.android.vhsmonitoring.Admin.Model.NotificationsData;
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
import com.example.android.vhsmonitoring.Notifications.NotificationAdapter;
import com.example.android.vhsmonitoring.R;
import com.example.android.vhsmonitoring.InputData.StockOpname;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<DailyPickupData> dataDailyPickup = new ArrayList<>();
    public String idCurrentApproval;

    public UserData pertamina;
    public StockData pertaminaCustomerStockData;
    public StockData stockApprovalQueue;
    public StockRequest handlerRestockRequest;
    public UserCustomer pertaminaCustomerData;
    public CustomerAddress pertaminaCustomerAddress;
    public int approvalNeededAmount = 0;
    public int activeTransaction = 0;
    public int stockRequest = 0;
    public String approvalStockId;
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
                intent.putExtra("user", handler.getId());
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
                intent.putExtra("inputType", "ARRIVED STOCK");
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
                intent.putExtra("inputType", "DAILY PICKUP");
                startActivity(intent);
            }
        });
    }
    public void fillChoicesData() {
        // views from dropdown choices
        TextView tvArrivedStockAmount = findViewById(R.id.tv_ARRIVED_STOCK_amount);
        TextView tvArrivedStockTime = findViewById(R.id.tv_ARRIVED_STOCK_time);
        TextView tvDailyPickupAmount = findViewById(R.id.tv_DAILY_PICKUP_amount);
        TextView tvDailyPickupTime = findViewById(R.id.tv_DAILY_PICKUP_time);

        // fill daily pickup choice
        db.child("data_stock/" + handlerCustomerData.getStockId() + "/stock_distributions").orderByChild("date_sent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    StockTransactions stockDistribution = item.getValue(StockTransactions.class);
                    if ("Daily Pickup".equals(stockDistribution.getType()) && !"Approved".equals(stockDistribution.getStatus())) {
                        DailyPickupData dataStock = item.getValue(DailyPickupData.class);
                        assert dataStock != null;
                        tvDailyPickupAmount.setText(String.format("%skL", String.valueOf(dataStock.getAmount())));
                        tvDailyPickupTime.setText(String.format("Requested on%s", dataStock.getDate_sent()));
                        tvDailyPickupTime.setTextColor(getResources().getColor(R.color.tertiary_200));
                        break;
                    } else if ("Daily Pickup".equals(stockDistribution.getType()) && "Approved".equals(stockDistribution.getStatus())) {
                        DailyPickupData dataStock = item.getValue(DailyPickupData.class);
                        assert dataStock != null;
                        tvDailyPickupAmount.setText(String.format("%skL", String.valueOf(dataStock.getAmount())));
                        tvDailyPickupTime.setText(String.format("Approved on%s", dataStock.getDate_approved()));
                        tvDailyPickupTime.setTextColor(getResources().getColor(R.color.secondary_500));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerStockData");
            }
        });

        // fill arrived stock choices

    }
    public void displayHistories() {
        TextView tvInputTitle = findViewById(R.id.tv_input_title);
        TextView tvOverviewStatusTime = findViewById(R.id.tv_overview_status_time);
        TextView tvStatus = findViewById(R.id.tv_status);

        // clear data in stockHistories to prevent duplicate
        if (stockHistories.size() >= 1) {
            stockHistories.clear();
        }

        // get all unfinished stock distribution
        db.child("data_stock/" + handlerCustomerData.getStockId() + "/stock_distributions").orderByChild("date_sent").addValueEventListener(new ValueEventListener() {
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

        // get stock distributions histories
        db.child("data_stock/" + handlerCustomerData.getStockId() + "/stock_distributions").orderByChild("date_sent").addValueEventListener(new ValueEventListener() {
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

                    // fill dropdown choices data
                    fillChoicesData();

                    // add data to list of object
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

                        displayHistories();
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

        // control views visibilities
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
                // Approve daily pickup
                DatabaseReference referenceData = FirebaseDatabase.getInstance().getReference();
                Map<String, Object> updateApproval = new HashMap<>();
                updateApproval.put("approval_customer", true);
                referenceData.child("data_stock").child(selfStockData.getId()).child("stock_distributions").child(idCurrentApproval).updateChildren(updateApproval);

                // report data to users
                Intent intent = new Intent(Dashboard.this, Approval.class);
                String json_customer = gsonData.toJson(customer);
                String json_SelfCustomerAddress = gsonData.toJson(SelfCustomerAddress);
                String json_selfStockData = gsonData.toJson(selfStockData);
                intent.putExtra("customer", json_customer);
                intent.putExtra("SelfCustomerAddress", json_SelfCustomerAddress);
                intent.putExtra("selfStockData", json_selfStockData);
                intent.putExtra("approvalType", "DAILY PICKUP");
                startActivity(intent);
            }
        });

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, NotificationActivity.class);
                intent.putExtra("user", customer.getId());
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

        // get today date
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

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

                        displayDailyPickupHistories();
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
        tvCalendar.setText(dateFormat.format(date));
    }
    public void displayDailyPickupHistories() {
        TextView tvInputTitle = findViewById(R.id.tv_input_title);
        TextView tvOverviewStatusTime = findViewById(R.id.tv_overview_status_time);
        TextView tvStatus = findViewById(R.id.tv_status);

        // clear data in Daily Stock to prevent duplicate
        if (dataDailyPickup.size() >= 1) {
            dataDailyPickup.clear();
        }

        // get all daily stock data
        db.child("data_stock").child(selfStockData.getId()).child("stock_distributions").orderByChild("date_sent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    StockTransactions dataStock = item.getValue(StockTransactions.class);
                    if ("Daily Pickup".equals(dataStock.getType())) {
                        DailyPickupData data = item.getValue(DailyPickupData.class);

                        // Update data status
                        data.checkStatus();
                        DatabaseReference referenceData = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> updateStatus = new HashMap<>();
                        updateStatus.put("status", data.getStatus());
                        referenceData.child("data_stock").child(selfStockData.getId()).child("stock_distributions").child(data.getId()).updateChildren(updateStatus);

                        tvInputTitle.setText(data.getStatus());
                        if (!"Approved".equals(data.getStatus()) && !data.isApproval_customer()) {
                            tvOverviewStatusTime.setText(String.format("Requested on %s", dataStock.getDate_sent()));
                            tvStatus.setText(R.string.approval_needed);
                            tvStatus.setTextColor(getResources().getColor(R.color.tertiary_200));
                            idCurrentApproval = data.getId();
                        } else if (!"Approved".equals(data.getStatus()) && !data.isApproval_pertamina()) {
                            tvOverviewStatusTime.setText(String.format("Requested on %s", dataStock.getDate_sent()));
                            tvStatus.setText(data.getStatus());
                            tvStatus.setTextColor(getResources().getColor(R.color.tertiary_200));
                        } else if ("Approved".equals(data.getStatus())) {
                            tvOverviewStatusTime.setText(String.format("Approved on %s", dataStock.getDate_approved()));
                            tvStatus.setTextColor(getResources().getColor(R.color.secondary_500));
                            tvStatus.setText(R.string.nothing_to_do);
                        }
                        dataDailyPickup.add(data);
                    }
                }

                // check array of data
                Log.d("length", String.valueOf(stockHistories.size()));
                if (dataDailyPickup.size() > 0) {
                    RecyclerView DailyPickupHistories = findViewById(R.id.historyRecycler);
                    DailyPickupAdapter adapter = new DailyPickupAdapter(Dashboard.this, dataDailyPickup);
                    DailyPickupHistories.setAdapter(adapter);
                    DailyPickupHistories.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                    DailyPickupHistories.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerStockData");
            }
        });
    }

    // pertamina dashboard
    public void viewPertaminaDashboard() {
        // set layout for pertamina dashboard
        setContentView(R.layout.activity_beranda_pusat);

        // get today date
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        TextView tvCurrentStockAmountArrivedStock = findViewById(R.id.tv_current_stock_amount_arrived_stock);
        TextView tvApprovedBy = findViewById(R.id.tv_approved_by);

        TextView tvCurrentStock = findViewById(R.id.tv_current_stock);
        TextView tvSold = findViewById(R.id.tv_sold);

        // views for pertamina dashboard
        TextView tvUserName = findViewById(R.id.tv_user_name);
        TextView tvUserId = findViewById(R.id.tv_user_id);
        TextView tvCalendar = findViewById(R.id.tv_calendar);
        TextView tvApprovalNeeded = findViewById(R.id.tv_approval_needed_amount);
        TextView tvActiveTransactionAmount =findViewById(R.id.tv_active_transaction_amount);
        TextView tvStockRequestAmount = findViewById(R.id.tv_stock_request_amount);
        TextView tvApprovalDate = findViewById(R.id.tv_approval_date);

        ImageButton btn_btnNotification = findViewById(R.id.btnNotification);
        btn_btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, NotificationActivity.class);
                intent.putExtra("user", pertamina.getId());
                startActivity(intent);
            }
        });

        ExtendedFloatingActionButton btn_approve = findViewById(R.id.btn_approve);
        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("check", approvalStockId);
                if (!"".equals(approvalStockId)) {
                    // Approve stock
                    DatabaseReference referenceData = FirebaseDatabase.getInstance().getReference();
                    Map<String, Object> updateApproval = new HashMap<>();
                    updateApproval.put("approval_pertamina", true);
                    updateApproval.put("date_approved", dateFormat.format(date));
                    referenceData.child("data_stock").child(pertaminaCustomerStockData.getId()).child("stock_distributions").child(approvalStockId).updateChildren(updateApproval);

                    // update stock status
                    db.child("data_stock/" + pertaminaCustomerStockData.getId() + "/stock_distributions/" + approvalStockId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot item : snapshot.getChildren()) {
                                StockTransactions data = snapshot.getValue(StockTransactions.class);
                                assert data != null;
                                if ("StockOpname".equals(data.getType())) {
                                    StockOpnameData stock = snapshot.getValue(StockOpnameData.class);
                                    stock.checkStatus();

                                    // update stock status
                                    Map<String, Object> updateStatus = new HashMap<>();
                                    updateStatus.put("status", stock.getStatus());
                                    referenceData.child("data_stock").child(pertaminaCustomerStockData.getId()).child("stock_distributions").child(approvalStockId).updateChildren(updateStatus);

                                } else if ("Daily Pickup".equals(data.getType())) {
                                    DailyPickupData stock = snapshot.getValue(DailyPickupData.class);
                                    stock.checkStatus();

                                    // update stock status
                                    Map<String, Object> updateStatus = new HashMap<>();
                                    updateStatus.put("status", stock.getStatus());
                                    referenceData.child("data_stock").child(pertaminaCustomerStockData.getId()).child("stock_distributions").child(approvalStockId).updateChildren(updateStatus);

                                } else if ("Restock".equals(data.getType())) {
                                    RestockData stock = snapshot.getValue(RestockData.class);
                                    stock.checkStatus();

                                    // update stock status
                                    Map<String, Object> updateStatus = new HashMap<>();
                                    updateStatus.put("status", stock.getStatus());
                                    referenceData.child("data_stock").child(pertaminaCustomerStockData.getId()).child("stock_distributions").child(approvalStockId).updateChildren(updateStatus);

                                }
                                Toast.makeText(getApplicationContext(), "Data has been Approved!", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("ERROR", "error while taking handlerCustomerStockData");
                        }
                    });
                }
            }
        });

        Encryption encode = new Encryption();

        tvUserName.setText(pertamina.getName());
        tvUserId.setText(encode.decrypt(pertamina.getCode()));
        tvCalendar.setText(dateFormat.format(date));
        tvApprovalDate.setText(dateFormat.format(date));

        // get pertamina customer data
        db.child("data_user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    UserData user = item.getValue(UserData.class);
                    assert user != null;
                    if ("customer".equals(user.getRole())) {
                        pertaminaCustomerData = item.getValue(UserCustomer.class);
                        if (pertaminaCustomerData.getPertaminaId().equals(pertamina.getId())) {
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerStockData");
            }
        });

        // get pertamina customer address
        db.child("customer_address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    pertaminaCustomerAddress = item.getValue(CustomerAddress.class);
                    assert pertaminaCustomerAddress != null;
                    if (pertaminaCustomerData.getId().equals(pertaminaCustomerAddress.getCustomerId()) && pertaminaCustomerData.getPertaminaId().equals(pertamina.getId())) {
                        // views for pertamina's customer
                        TextView tvCustomerName = findViewById(R.id.tv_customer_name);
                        TextView tvCustomerAddress = findViewById(R.id.tv_customer_address);

                        // fill report data
                        tvCustomerName.setText(pertaminaCustomerData.getName());
                        tvCustomerAddress.setText(String.format("%s, %s %s", pertaminaCustomerAddress.getCity(), pertaminaCustomerAddress.getProvince(), pertaminaCustomerAddress.getPostalCode()));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerStockData");
            }
        });

        // get pertamina customer stock
        db.child("data_stock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    StockData data = item.getValue(StockData.class);
                    assert data != null;
                    if (data.getManagerId().equals(pertamina.getId())) {
                        TextView tvCurrentStock = findViewById(R.id.tv_current_stock);
                        TextView tvSold = findViewById(R.id.tv_sold);
                        TextView tvCustomerStockType = findViewById(R.id.tv_customer_stok);

                        String currentStock = data.getTank_rest() + "kL";
                        String stockSold = data.getSold() + "kL";

                        Log.d("dataTest", currentStock);
                        Log.d("dataTest1", stockSold);

                        tvCurrentStock.setText(currentStock);
                        tvSold.setText(stockSold);
                        tvCustomerStockType.setText(String.format("Stock: %s", data.getType()));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerStockData");
            }
        });

        // approval needed for pertamina
        db.child("data_stock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    pertaminaCustomerStockData = item.getValue(StockData.class);
                    assert pertaminaCustomerStockData != null;
                    if (!"finished".equals(pertaminaCustomerStockData.getTank_status())) {
                        activeTransaction += 1;
                        pertaminaCustomers.add(pertaminaCustomerStockData);
                    }
                    if (pertaminaCustomerStockData.getManagerId().equals(pertamina.getId()) && !"finished".equals(pertaminaCustomerStockData.getTank_status())) {
                        db.child("data_stock").child(pertaminaCustomerStockData.getId()).child("stock_distributions").addValueEventListener(new ValueEventListener() {
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
                                    } else if ("Daily Pickup".equals(stockData.getType())) {
                                        StockOpnameData data = item.getValue(StockOpnameData.class);
                                        assert data != null;
                                        if (!data.isApproval_pertamina()) { approvalNeededAmount += 1; }
                                    }
                                    tvApprovalNeeded.setText(String.valueOf(approvalNeededAmount));
                                    tvActiveTransactionAmount.setText(String.valueOf(activeTransaction));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("ERROR", "error while taking handlerCustomerStockData");
                            }
                        });
                    }

                }
                Log.d("test3", String.valueOf(approvalNeededAmount));
                tvApprovalNeeded.setText(String.valueOf(approvalNeededAmount));
                tvActiveTransactionAmount.setText(String.valueOf(activeTransaction));
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

        // pertamina's approval stock data
        db.child("data_stock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    pertaminaCustomerStockData = item.getValue(StockData.class);
                    assert pertaminaCustomerStockData != null;
                    if (pertaminaCustomerStockData.getManagerId().equals(pertamina.getId())) {
                        db.child("data_stock").child(pertaminaCustomerStockData.getId()).child("stock_distributions").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot item : snapshot.getChildren()) {
                                    StockTransactions stocks = item.getValue(StockTransactions.class);
                                    assert stocks != null;
                                    if (!"Approved".equals(stocks.getStatus())) {
                                        TextView tvInputTitle = findViewById(R.id.tv_input_title);
                                        TextView tvOverviewStatusTime = findViewById(R.id.tv_overview_status_time);

                                        if ("StockOpname".equals(stocks.getType())) {
                                            StockOpnameData data = item.getValue(StockOpnameData.class);
                                            tvInputTitle.setText(data.getStatus());
                                            tvOverviewStatusTime.setText(String.format("Requested on %s", data.getDate_sent()));
                                            approvalStockId = data.getId();
                                        } else if ("Daily Pickup".equals(stocks.getType())) {
                                            DailyPickupData data = item.getValue(DailyPickupData.class);
                                            tvInputTitle.setText(data.getStatus());
                                            tvOverviewStatusTime.setText(String.format("Requested on %s", data.getDate_sent()));
                                            approvalStockId = data.getId();
                                        } else if ("Restock".equals(stocks.getType())) {
                                            RestockData data = item.getValue(RestockData.class);
                                            tvInputTitle.setText(data.getStatus());
                                            tvOverviewStatusTime.setText(String.format("Requested on %s", data.getDate_sent()));
                                            approvalStockId = data.getId();
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
                    }
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerStockData");
            }
        });

        // configure restock stack
        TextView tvStockRequest = findViewById(R.id.tv_stock_request);
        ViewPager vpStockRequest = findViewById(R.id.vp_stock_request);
        tvStockRequest.setVisibility(View.GONE);
        vpStockRequest.setVisibility(View.GONE);

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




