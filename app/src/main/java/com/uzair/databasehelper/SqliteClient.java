package com.uzair.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.uzair.utils.Contracts;
import com.uzair.model.Items;
import com.uzair.model.Product;

import java.util.ArrayList;
import java.util.List;

public class SqliteClient extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "shop.db";
    public static final int DATABASE_VERSION = 17;
    private Context context;


    public SqliteClient(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table product
        db.execSQL("create table " + Contracts.Products.COL_TABLE_NAME +
                " ( " + Contracts.Products.COL_UID + " integer primary key, "
                + Contracts.Products.COL_PRODUCT_NAME + " text, " +
                Contracts.Products.COL_COMPANY_ID + " integer, " +
                Contracts.Products.COL_CATEGORY_ID + " integer, " +
                Contracts.Products.COL_STATUS + " text ); ");


        ///create table items
        db.execSQL(" CREATE TABLE " + Contracts.Items.COL_TABLE_NAME +
                " ( " + Contracts.Items.COL_UID + " integer primary key, "
                + Contracts.Items.COL_ITEM_NAME + " text, " +
                Contracts.Items.COL_SKU_CODE + " text, " +
                Contracts.Items.COL_BOX_SIZE + " integer, " +
                Contracts.Items.COL_CTN_SIZE + " integer, " +
                Contracts.Items.COL_IMAGE_URL + " text, " +
                Contracts.Items.COL_PRODUCT_ID + " integer, " +
                " FOREIGN KEY (" + Contracts.Items.COL_PRODUCT_ID + " ) REFERENCES " +
                Contracts.Products.COL_TABLE_NAME + " ( " + Contracts.Products.COL_UID + " ) ); ");


        // insert data in product table on start
        // createProduct();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /// upgrade tables here
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Products.COL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Items.COL_TABLE_NAME);
        onCreate(db);
    }

    //// insertion product table
    public void createProduct() {
        SQLiteDatabase db = this.getWritableDatabase();
        Product biscuit = new Product("Biscuit", "PUBLISHED", 1, 1, 1);
        insertProduct(biscuit, db);

        Product cake = new Product("Cake", "PUBLISHED", 2, 1, 1);
        insertProduct(cake, db);

        Product instant_noodles = new Product("Instant Noodles", "PUBLISHED", 3, 2, 1);
        insertProduct(instant_noodles, db);

        Product lotteSpout = new Product("Lotte Spout", "PUBLISHED", 4, 3, 1);
        insertProduct(lotteSpout, db);

        Product lotteBiscuit = new Product("Lotte  Biscuit", "PUBLISHED", 5, 4, 1);
        insertProduct(lotteBiscuit, db);

        Product pie = new Product("Pie", "PUBLISHED", 6, 4, 1);
        insertProduct(pie, db);

        Product pasta = new Product("Pasta", "PUBLISHED", 7, 5, 1);
        insertProduct(pasta, db);

        Product premiumPasta = new Product("Premium Pasta", "PUBLISHED", 8, 5, 1);
        insertProduct(premiumPasta, db);

        Product vermicelli = new Product("Vermicelli", "PUBLISHED", 9, 5, 1);
        insertProduct(vermicelli, db);

        Product snacks = new Product("Snacks", "PUBLISHED", 10, 6, 1);
        insertProduct(snacks, db);


    }

    ///insert
    public void insertProduct(Product product, SQLiteDatabase db) {


        ContentValues productCv = new ContentValues();
        productCv.put(Contracts.Products.COL_PRODUCT_NAME, product.getProductName());
        productCv.put(Contracts.Products.COL_UID, product.getUid());
        productCv.put(Contracts.Products.COL_CATEGORY_ID, product.getCategoryId());
        productCv.put(Contracts.Products.COL_COMPANY_ID, product.getCompanyId());
        productCv.put(Contracts.Products.COL_STATUS, product.getStatus());


        db.insert(Contracts.Products.COL_TABLE_NAME, null, productCv);

    }

    /// get product list here
    public List<Product> getProductList() {
        List<Product> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(" SELECT * FROM " + Contracts.Products.COL_TABLE_NAME, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Product productData = new Product();
                productData.setUid(cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.Products.COL_UID)));
                productData.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(Contracts.Products.COL_PRODUCT_NAME)));
                productData.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.Products.COL_CATEGORY_ID)));
                productData.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(Contracts.Products.COL_STATUS)));
                productData.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.Products.COL_COMPANY_ID)));
                // Adding product to list
                list.add(productData);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;

    }

    public String getProductNameById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String productName = "";
        Cursor productCursor = db.rawQuery("select * from " + Contracts.Products.COL_TABLE_NAME +
                " where " + Contracts.Products.COL_UID + "=?", new String[]{String.valueOf(productId)});

        if (productCursor.moveToFirst()) {

            do {
                productName = productCursor.getString(productCursor.getColumnIndexOrThrow(Contracts.Products.COL_PRODUCT_NAME));
            }
            while (productCursor.moveToNext());

        }
        productCursor.close();
        return productName;


    }


    /// insert data in second table
    public void createItem() {
        SQLiteDatabase db = this.getWritableDatabase();

        /// category first
        Items biscuitOne = new Items("ALP G72 - ALPHA S/P RS.10", "https://rahatmedia.com/kolson/uploads/sku/5.png", "G72", 1, 1, 0, 18);
        insertItems(biscuitOne, db);

        Items biscuitTwo = new Items("ALP G71 - ALPHA T/P RS.5", "https://rahatmedia.com/kolson/uploads/sku/5.png", "G71", 2, 1, 0, 18);
        insertItems(biscuitTwo, db);

        Items biscuitThree = new Items("BRV - G56 - BRAVO BP", "https://rahatmedia.com/kolson/uploads/sku/5.png", "G56", 3, 1, 0, 18);
        insertItems(biscuitThree, db);

        Items biscuitFourth = new Items("BRV - G6 - BRAVO FP", "https://rahatmedia.com/kolson/uploads/sku/5.png", "G6", 4, 1, 0, 96);
        insertItems(biscuitFourth, db);

        Items biscuitFive = new Items("BRV - G36 - BRAVO SP", "https://rahatmedia.com/kolson/uploads/sku/5.png", "G36", 5, 1, 0, 12);
        insertItems(biscuitFive, db);

        /// category second
        Items cakeOne = new Items("C.CK - CC11 - O.FRESH C.CAKE-STRW", "https://rahatmedia.com/kolson/uploads/sku/5.png", "CC11", 6, 2, 0, 24);
        insertItems(cakeOne, db);

        Items cakeTwo = new Items("C.CK - CC12 - O.FRESH C.CAKE-CHOC", "https://rahatmedia.com/kolson/uploads/sku/5.png", "CC12", 7, 2, 0, 24);
        insertItems(cakeTwo, db);

        Items cakeThree = new Items("C.CK - CC13 - O.FRESH C.CAKE-B.BER", "https://rahatmedia.com/kolson/uploads/sku/5.png", "CC13", 8, 2, 0, 24);
        insertItems(cakeThree, db);

        Items cakeFourth = new Items("C.CK - CC14 - O.FRESH C.CAKE-BANA", "https://rahatmedia.com/kolson/uploads/sku/5.png", "CC14", 9, 2, 0, 24);
        insertItems(cakeFourth, db);

        Items cakeFive = new Items("O.F - C4 - O.FRESH STRAW JAM", "https://rahatmedia.com/kolson/uploads/sku/5.png", "C4", 10, 2, 0, 24);
        insertItems(cakeFive, db);

        /// category third
        Items noodleFirst = new Items("N.D - NC3 -CUP NOODLE TIKKA", "https://rahatmedia.com/kolson/uploads/sku/5.png", "NC3", 11, 3, 0, 24);
        insertItems(noodleFirst, db);

        Items noodleSecond = new Items("N.D - N1CP PACK NOODLE CHICKEN FP", "https://rahatmedia.com/kolson/uploads/sku/5.png", "N1CP", 12, 3, 0, 18);
        insertItems(noodleSecond, db);

        Items noodleThird = new Items("N.D - N2CP PACK NOODLE CHATPATTA FP", "https://rahatmedia.com/kolson/uploads/sku/5.png", "N2CP", 13, 3, 0, 18);
        insertItems(noodleThird, db);

        Items noodleFourth = new Items("N.D - N1 - PACK NOODLE CHICKEN", "https://rahatmedia.com/kolson/uploads/sku/5.png", "N1", 14, 3, 0, 72);
        insertItems(noodleFourth, db);

        Items noodleFive = new Items("N.D - N2 - PACK NOODLE CHATPATTA", "https://rahatmedia.com/kolson/uploads/sku/5.png", "N2", 15, 3, 0, 72);
        insertItems(noodleFive, db);

        ///// category fourth
        Items lotteSpoutOne = new Items("L.S - LS04 - LOTTE SPOUT STRAWBERRY 23.8G", "https://rahatmedia.com/kolson/uploads/sku/5.png", "LS04", 16, 4, 0, 24);
        insertItems(lotteSpoutOne, db);

        Items lotteSpoutTwo = new Items("L.S - LS06 - LOTTE SPOUT (SPR-PEP-STR) 23.8G", "https://rahatmedia.com/kolson/uploads/sku/5.png", "LS06", 17, 4, 0, 24);
        insertItems(lotteSpoutTwo, db);

        Items lotteSpoutThird = new Items("L.S - LS11 - LOTTE SPOUT SPEARMINT", "https://rahatmedia.com/kolson/uploads/sku/5.png", "LS11", 18, 4, 0, 36);
        insertItems(lotteSpoutThird, db);

        Items lotteSpoutFourth = new Items("L.S - LS12 - LOTTE SPOUT PEPPERMINT", "https://rahatmedia.com/kolson/uploads/sku/5.png", "LS12", 19, 4, 0, 36);
        insertItems(lotteSpoutFourth, db);

        Items lotteSpoutFive = new Items("L.S - LS13 - LOTTE SPOUT CINNAMON", "https://rahatmedia.com/kolson/uploads/sku/5.png", "LS13", 20, 4, 0, 36);
        insertItems(lotteSpoutFive, db);


    }

    /// insert data into items
    public void insertItems(Items item, SQLiteDatabase db) {

        ContentValues itemContentValue = new ContentValues();
        itemContentValue.put(Contracts.Items.COL_UID, item.getUid());
        itemContentValue.put(Contracts.Items.COL_ITEM_NAME, item.getItemName());
        itemContentValue.put(Contracts.Items.COL_BOX_SIZE, item.getBoxSize());
        itemContentValue.put(Contracts.Items.COL_CTN_SIZE, item.getCtnSize());
        itemContentValue.put(Contracts.Items.COL_IMAGE_URL, item.getImageUrl());
        itemContentValue.put(Contracts.Items.COL_PRODUCT_ID, item.getProductId());
        itemContentValue.put(Contracts.Items.COL_SKU_CODE, item.getSkuCode());
        db.insert(Contracts.Items.COL_TABLE_NAME, null, itemContentValue);
    }

    /// get items list
    public List<Items> getAllItems(int currentPage , int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Items> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * from " + Contracts.Items.COL_TABLE_NAME + " limit "+ currentPage + "," +limit, null);

        if (cursor.moveToFirst()) {
            do {
                Items items = new Items();
                items.setItemName(cursor.getString(cursor.getColumnIndexOrThrow(Contracts.Items.COL_ITEM_NAME)));
                items.setUid(cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.Items.COL_UID)));
                items.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.Items.COL_PRODUCT_ID)));
                items.setBoxSize(cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.Items.COL_BOX_SIZE)));
                items.setCtnSize(cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.Items.COL_CTN_SIZE)));
                items.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(Contracts.Items.COL_IMAGE_URL)));
                items.setSkuCode(cursor.getString(cursor.getColumnIndexOrThrow(Contracts.Items.COL_SKU_CODE)));

                list.add(items);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }


}
