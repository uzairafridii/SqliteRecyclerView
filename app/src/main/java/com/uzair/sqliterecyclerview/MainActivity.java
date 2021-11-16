package com.uzair.sqliterecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.uzair.adapter.MyAdapter;
import com.uzair.databasehelper.SqliteClient;
import com.uzair.model.Items;
import com.uzair.model.Product;
import com.uzair.utils.RecyclerSectionItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static int mTotalItemCount = 1;
    public static int currentPage = 1;
    public static int mLastVisibleItemPosition;
    public static boolean mIsLoading = false;
    public static int mPostsPerPage = 7;

    private SqliteClient client;
    private List<Product> productList;
    private List<Items> itemList;
    private RecyclerView rvProducts;
    private MyAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new SqliteClient(this);
        client.createProduct();
        client.createItem();

        initViews();

        /// check for scrolling of recycler view then load the new items
        rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalItemCount = layoutManager.getItemCount();
                mLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                if (!mIsLoading && mTotalItemCount <= (mLastVisibleItemPosition + mPostsPerPage)) {

                    itemList.addAll(client.getAllItems((currentPage * mPostsPerPage), mPostsPerPage));
                    currentPage++;
                    mIsLoading = false;
                }
            }
        });


    }

    private void initViews() {
        //productList = new ArrayList<>();

        rvProducts = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rvProducts.setLayoutManager(layoutManager);


        /// adapter for intruder shanky
        productList = client.getProductList();
        itemList = client.getAllItems(0, mPostsPerPage);
        adapter = new MyAdapter(itemList, this);
        //  adapterForSectionRecyclerView = new AdapterForSectionRecyclerView(MainActivity.this, sectionHeaderList);
        rvProducts.setAdapter(adapter);

        RecyclerSectionItemDecoration sectionItemDecoration =
                new RecyclerSectionItemDecoration(getResources().getDimensionPixelSize(R.dimen.header),
                        true,
                        getSectionCallback(itemList));
        rvProducts.addItemDecoration(sectionItemDecoration);

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