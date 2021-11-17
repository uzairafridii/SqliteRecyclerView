package com.uzair.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uzair.adapter.MyAdapter;
import com.uzair.databasehelper.SqliteClient;
import com.uzair.model.Items;
import com.uzair.model.Product;
import com.uzair.utils.RecyclerSectionItemDecoration;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static int startPoint = 0;
    public static int mPostsPerPage = 10;
    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    private SqliteClient client;
    private List<Items> itemList;
    private RecyclerView rvProducts;
    private MyAdapter adapter;
    private LinearLayoutManager layoutManager;
    private NestedScrollView mScrollView;

    /// firebase
    DatabaseReference dbRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new SqliteClient(this);

        initViews();

//        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged()
//            {
//                View view = (View)mScrollView.getChildAt(mScrollView.getChildCount() - 1);
//
//                int diff = (view.getBottom() - (mScrollView.getHeight() + mScrollView
//                        .getScrollY()));
//
//                if (diff == 0) {
//                    // your pagination code
//                    visibleItemCount = layoutManager.getChildCount();
//                    totalItemCount = layoutManager.getItemCount();
//                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
//
//                    if (loading) {
//                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
//                            startPoint++;
//                            Log.d("startpointofsqlite", "onScrolled: " + startPoint);
//                            loadMoreData();
//                            loading = true;
//                        }
//                    }
//                }
//            }
//        });



        /// check for scrolling of recycler view then load the new items
        rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            startPoint++;
                            Log.d("startpointofsqlite", "onScrolled: " + startPoint);
                            loadMoreData();
                            loading = true;
                        }
                    }
                }
            }
        });

    }

    private void loadMoreData() {
        itemList.addAll(client.getAllItems((startPoint * mPostsPerPage), mPostsPerPage));
        rvProducts.post(() -> adapter.notifyDataSetChanged());
        loading = false;
    }

    private void initViews() {

        ///// firebase
        dbRef  = FirebaseDatabase.getInstance().getReference();
        getAllProducts();
        getAllItems();

        ///recycler view setup
       // mScrollView  = findViewById(R.id.nestedScrollView);
        rvProducts = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setHasFixedSize(true);
      //  rvProducts.setNestedScrollingEnabled(false);

        itemList = client.getAllItems(startPoint, mPostsPerPage);
        adapter = new MyAdapter(itemList);
        rvProducts.setAdapter(adapter);

        /// set item decoration for recycler view
        RecyclerSectionItemDecoration sectionItemDecoration =
                new RecyclerSectionItemDecoration(getResources().getDimensionPixelSize(R.dimen.header),
                        true,
                        getSectionCallback(itemList));
        rvProducts.addItemDecoration(sectionItemDecoration);




    }


    /// get all products
    private void getAllProducts()
    {
        dbRef.child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot query : snapshot.getChildren())
                        {
                            Product product = new Product();
                            product.setProductName(String.valueOf(query.child("name").getValue()));
                            product.setStatus(String.valueOf(query.child("status").getValue()));
                            product.setUid(Integer.parseInt(String.valueOf(query.child("uid").getValue())));
                            product.setCompanyId(Integer.parseInt(String.valueOf(query.child("companyid").getValue())));
                            product.setCategoryId(Integer.parseInt(String.valueOf(query.child("category_id").getValue())));

                            client.insertProduct(product);
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void getAllItems()
    {
        dbRef.child("Items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot query : snapshot.getChildren())
                        {
                            Items items = new Items();
                            items.setItemName(String.valueOf(query.child("name").getValue()));
                            items.setProductId(Integer.parseInt(String.valueOf(query.child("productid").getValue())));
                            items.setSkuCode(String.valueOf(query.child("sku_code").getValue()));
                            items.setImageUrl(String.valueOf(query.child("image").getValue()));
                            items.setCtnSize(Integer.parseInt(String.valueOf(query.child("ctn_size").getValue())));
                            items.setBoxSize(Integer.parseInt(String.valueOf(query.child("box_size").getValue())));
                            items.setUid(Integer.parseInt(String.valueOf(query.child("uid").getValue())));

                            client.insertItems(items);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    /// item decoration
    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<Items> item) {
        return new RecyclerSectionItemDecoration.SectionCallback() {
            @Override
            public boolean isSection(int position) {
                return position == 0
                        || item.get(position).getProductId() !=
                        item.get(position - 1).getProductId();
            }

            @Override
            public String getSectionHeader(int position) {
                return client.getProductNameById(item.get(position).getProductId());
            }
        };
    }
}