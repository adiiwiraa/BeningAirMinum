package com.example.testing.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.testing.R;
import com.example.testing.model.Order;
import com.example.testing.model.Produk;
import com.example.testing.model.Staff;
import com.example.testing.model.User;
import com.example.testing.utils.DateUtils;
import com.example.testing.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "WaterDepot.db";
    public static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_PRODUK = "produk";
    public static final String TABLE_STAFF = "staff";

    // Users Table columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_PHONE = "phone";
    public static final String COLUMN_USER_ADDRESS = "address";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_PHOTO = "photo";
    public static final String COLUMN_USER_LEVEL = "level";
    public static final String COLUMN_USER_POSITION = "position";
    public static final String COLUMN_USER_TIME_CREATED = "time_created";
    public static final String COLUMN_USER_TIME_UPDATED = "time_updated";

    // Orders Table columns
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_USER_ID = "user_id";
    public static final String COLUMN_ORDER_NAME = "name";
    public static final String COLUMN_ORDER_EMAIL = "email";
    public static final String COLUMN_ORDER_PHONE = "phone";
    public static final String COLUMN_ORDER_ADDRESS = "address";
    public static final String COLUMN_ORDER_QUANTITY = "quantity";
    public static final String COLUMN_ORDER_PAYMENT_TYPE = "payment_type";
    public static final String COLUMN_ORDER_TOTAL = "total";
    public static final String COLUMN_ORDER_STATUS = "status";
    public static final String COLUMN_ORDER_DRIVER_NAME = "driver_name";
    public static final String COLUMN_ORDER_TRACKING_NUMBER = "tracking_number";
    public static final String COLUMN_ORDER_ORDER_NAME = "order_name";
    public static final String COLUMN_ORDER_TIME_CREATED = "time_created";
    public static final String COLUMN_ORDER_TIME_FINISHED = "time_finished";

    // Produk Table columns
    public static final String COLUMN_PRODUK_ID = "produk_id";
    public static final String COLUMN_PRODUK_NAMA = "nama_produk";
    public static final String COLUMN_PRODUK_STOK = "stok_produk";
    public static final String COLUMN_PRODUK_HARGA = "harga_produk";
    public static final String COLUMN_PRODUK_TANGGAL_MASUK = "tanggal_masuk";
    public static final String COLUMN_PRODUK_GAMBAR = "gambar_produk";
    public static final String COLUMN_PRODUK_TANGGAL_UPDATE = "tanggal_update";

    // Staff Table columns
    public static final String COLUMN_STAFF_ID = "staff_id";
    public static final String COLUMN_STAFF_NAMA = "nama_staff";
    public static final String COLUMN_STAFF_JABATAN = "jabatan";
    public static final String COLUMN_STAFF_TELEPON = "telepon";
    public static final String COLUMN_STAFF_EMAIL = "email";
    public static final String COLUMN_STAFF_PASSWORD = "password";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_EMAIL + " TEXT, " +
                COLUMN_USER_PASSWORD + " TEXT, " +
                COLUMN_USER_PHONE + " TEXT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_LEVEL + " TEXT, " +
                COLUMN_USER_POSITION + " INTEGER, " +
                COLUMN_USER_PHOTO + " BLOB, " +
                COLUMN_USER_TIME_CREATED + " TEXT, " +
                COLUMN_USER_TIME_UPDATED + " TEXT, " +
                COLUMN_USER_ADDRESS + " TEXT)";
        db.execSQL(createUsersTable);

        String createOrdersTable = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORDER_USER_ID + " INTEGER, " +
                COLUMN_ORDER_NAME + " TEXT, " +
                COLUMN_ORDER_EMAIL + " TEXT, " +
                COLUMN_ORDER_PHONE + " TEXT, " +
                COLUMN_ORDER_ADDRESS + " TEXT, " +
                COLUMN_ORDER_QUANTITY + " INTEGER, " +
                COLUMN_ORDER_PAYMENT_TYPE + " TEXT, " +
                COLUMN_ORDER_TOTAL + " REAL, " +
                COLUMN_ORDER_STATUS + " TEXT, " +
                COLUMN_ORDER_DRIVER_NAME + " TEXT, " +
                COLUMN_ORDER_ORDER_NAME + " TEXT, " +
                COLUMN_ORDER_TIME_CREATED + " TEXT, " +
                COLUMN_ORDER_TIME_FINISHED + " TEXT, " +
                COLUMN_ORDER_TRACKING_NUMBER + " TEXT)";
        db.execSQL(createOrdersTable);

        String createProdukTable = "CREATE TABLE " + TABLE_PRODUK + " (" +
                COLUMN_PRODUK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUK_NAMA + " TEXT, " +
                COLUMN_PRODUK_STOK + " INTEGER, " +
                COLUMN_PRODUK_HARGA + " REAL, " +
                COLUMN_PRODUK_GAMBAR + " BLOB, " +
                COLUMN_PRODUK_TANGGAL_UPDATE + " TEXT, " +
                COLUMN_PRODUK_TANGGAL_MASUK + " TEXT)";
        db.execSQL(createProdukTable);

        String createStaffTable = "CREATE TABLE " + TABLE_STAFF + " (" +
                COLUMN_STAFF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STAFF_NAMA + " TEXT, " +
                COLUMN_STAFF_JABATAN + " TEXT, " +
                COLUMN_STAFF_TELEPON + " TEXT, " +
                COLUMN_STAFF_EMAIL + " TEXT, " +
                COLUMN_STAFF_PASSWORD + " TEXT)";
        db.execSQL(createStaffTable);

        // Insert initial data
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAFF);
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {

        Produk produk1 = new Produk(
                1,
                "Air Mineral Gelas Dus (isi 48 pcs)",
                1000,
                21000,
                ImageUtils.convertDrawableToByteArray(context.getResources().getDrawable(R.drawable.mineral_gelas)),
                DateUtils.getCurrentTime(),
                DateUtils.getCurrentTime()
        );

        Produk produk2 = new Produk(
                1,
                "Air Mineral Botol Dus (isi 24 pcs)",
                500,
                53000,
                ImageUtils.convertDrawableToByteArray(context.getResources().getDrawable(R.drawable.mineral_botol)),
                DateUtils.getCurrentTime(),
                DateUtils.getCurrentTime()
        );

        Produk produk3 = new Produk(
                1,
                "Air Mineral Galon",
                200,
                15000,
                ImageUtils.convertDrawableToByteArray(context.getResources().getDrawable(R.drawable.mineral_galon)),
                DateUtils.getCurrentTime(),
                DateUtils.getCurrentTime()
        );

        List<Produk> produkAwal = new ArrayList<Produk>();
        produkAwal.add(produk1);
        produkAwal.add(produk2);
        produkAwal.add(produk3);

        for (Produk produk: produkAwal) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PRODUK_NAMA, produk.getNamaProduk());
            values.put(COLUMN_PRODUK_HARGA, produk.getHargaProduk());
            values.put(COLUMN_PRODUK_STOK, produk.getStokProduk());
            values.put(COLUMN_PRODUK_GAMBAR, produk.getFotoProduk());
            values.put(COLUMN_PRODUK_TANGGAL_MASUK, produk.getTanggalMasuk());
            values.put(COLUMN_PRODUK_TANGGAL_UPDATE, produk.getTanggalUpdate());
            db.insert(TABLE_PRODUK, null, values);
        }

        User staff1 = new User(
                1,
                "admin1@waterdepot.com",
                "admin1234",
                "081234567890",
                "Pondok Labu",
                null,
                2,
                "Admin",
                DateUtils.getCurrentTime(),
                DateUtils.getCurrentTime(),
                "Admin Satu"
        );

        User staff2 = new User(
                1,
                "admin2@waterdepot.com",
                "admin1234",
                "081234567890",
                "Pondok Labu",
                null,
                2,
                "Admin",
                DateUtils.getCurrentTime(),
                DateUtils.getCurrentTime(),
                "Admin Dua"
        );

        User staff3 = new User(
                1,
                "driver1@waterdepot.com",
                "driver1234",
                "081234567890",
                "Pondok Labu",
                null,
                2,
                "Driver",
                DateUtils.getCurrentTime(),
                DateUtils.getCurrentTime(),
                "Driver 1"
        );

        User staff4 = new User(
                1,
                "driver2@waterdepot.com",
                "admin1234",
                "081234567890",
                "Pondok Labu",
                null,
                2,
                "Driver",
                DateUtils.getCurrentTime(),
                DateUtils.getCurrentTime(),
                "Driver 2"
        );

        List<User> staffAwal = new ArrayList<User>();
        staffAwal.add(staff1);
        staffAwal.add(staff2);
        staffAwal.add(staff3);
        staffAwal.add(staff4);

        for (User staff: staffAwal) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_EMAIL, staff.getEmail());
            values.put(COLUMN_USER_PASSWORD, staff.getPassword());
            values.put(COLUMN_USER_PHONE, staff.getPhone());
            values.put(COLUMN_USER_ADDRESS, staff.getAddress());
            values.put(COLUMN_USER_PHOTO, staff.getPhoto());
            values.put(COLUMN_USER_LEVEL, staff.getLevel());
            values.put(COLUMN_USER_POSITION, staff.getPosition());
            values.put(COLUMN_USER_TIME_CREATED, staff.getCreatedAt());
            values.put(COLUMN_USER_TIME_UPDATED, staff.getUpdatedAt());
            values.put(COLUMN_USER_NAME, staff.getName());
            db.insert(TABLE_USERS, null, values);
        }

//        db.close();
    }

    // CRUD operations for Users
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_ADDRESS, user.getAddress());
        values.put(COLUMN_USER_PHOTO, user.getPhoto());
        values.put(COLUMN_USER_LEVEL, user.getLevel());
        values.put(COLUMN_USER_POSITION, user.getPosition());
        values.put(COLUMN_USER_TIME_CREATED, user.getCreatedAt());
        values.put(COLUMN_USER_TIME_UPDATED, user.getUpdatedAt());
        values.put(COLUMN_USER_NAME, user.getName());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD, COLUMN_USER_PHONE, COLUMN_USER_NAME, COLUMN_USER_ADDRESS, COLUMN_USER_PHOTO, COLUMN_USER_LEVEL, COLUMN_USER_POSITION, COLUMN_USER_TIME_CREATED, COLUMN_USER_TIME_UPDATED},
                null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            do {
                int idIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                int emailIndex = cursor.getColumnIndex(COLUMN_USER_EMAIL);
                int passIndex = cursor.getColumnIndex(COLUMN_USER_PASSWORD);
                int phoneIndex = cursor.getColumnIndex(COLUMN_USER_PHONE);
                int addressIndex = cursor.getColumnIndex(COLUMN_USER_ADDRESS);
                int nameIndex = cursor.getColumnIndex(COLUMN_USER_NAME);
                int photoIndex = cursor.getColumnIndex(COLUMN_USER_PHOTO);
                int levelIndex = cursor.getColumnIndex(COLUMN_USER_LEVEL);
                int positionIndex = cursor.getColumnIndex(COLUMN_USER_POSITION);
                int createdIndex = cursor.getColumnIndex(COLUMN_USER_TIME_CREATED);
                int updatedIndex = cursor.getColumnIndex(COLUMN_USER_TIME_UPDATED);

                if (idIndex != -1 && emailIndex != -1 && passIndex != -1 && phoneIndex != -1 && addressIndex != -1) {
                    User user = new User(
                            cursor.getInt(idIndex),
                            cursor.getString(emailIndex),
                            cursor.getString(passIndex),
                            cursor.getString(phoneIndex),
                            cursor.getString(addressIndex),
                            cursor.getBlob(photoIndex),
                            cursor.getInt(levelIndex),
                            cursor.getString(positionIndex),
                            cursor.getString(createdIndex),
                            cursor.getString(updatedIndex),
                            cursor.getString(nameIndex)
                    );
                    userList.add(user);
                } else {
                    Log.e("DBHelper", "Column not found in cursor");
                }
            } while (cursor.moveToNext());

        } else {
            Log.e("DBHelper", "Cursor is empty or null");
        }

        if (cursor != null) {
            cursor.close();
        }
        return userList;
    }

    public User getUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD, COLUMN_USER_PHONE, COLUMN_USER_NAME, COLUMN_USER_ADDRESS, COLUMN_USER_PHOTO, COLUMN_USER_LEVEL, COLUMN_USER_POSITION, COLUMN_USER_TIME_CREATED, COLUMN_USER_TIME_UPDATED},
                COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            int emailIndex = cursor.getColumnIndex(COLUMN_USER_EMAIL);
            int passIndex = cursor.getColumnIndex(COLUMN_USER_PASSWORD);
            int phoneIndex = cursor.getColumnIndex(COLUMN_USER_PHONE);
            int addressIndex = cursor.getColumnIndex(COLUMN_USER_ADDRESS);
            int nameIndex = cursor.getColumnIndex(COLUMN_USER_NAME);
            int photoIndex = cursor.getColumnIndex(COLUMN_USER_PHOTO);
            int levelIndex = cursor.getColumnIndex(COLUMN_USER_LEVEL);
            int positionIndex = cursor.getColumnIndex(COLUMN_USER_POSITION);
            int createdIndex = cursor.getColumnIndex(COLUMN_USER_TIME_CREATED);
            int updatedIndex = cursor.getColumnIndex(COLUMN_USER_TIME_UPDATED);

            if (idIndex != -1 && emailIndex != -1 && passIndex != -1 && phoneIndex != -1 && addressIndex != -1) {
                User user = new User(
                        cursor.getInt(idIndex),
                        cursor.getString(emailIndex),
                        cursor.getString(passIndex),
                        cursor.getString(phoneIndex),
                        cursor.getString(addressIndex),
                        cursor.getBlob(photoIndex),
                        cursor.getInt(levelIndex),
                        cursor.getString(positionIndex),
                        cursor.getString(createdIndex),
                        cursor.getString(updatedIndex),
                        cursor.getString(nameIndex)
                );
                cursor.close();
                return user;
            } else {
                Log.e("DBHelper", "Column not found in cursor");
            }
        } else {
            Log.e("DBHelper", "Cursor is empty or null");
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD, COLUMN_USER_PHONE, COLUMN_USER_NAME, COLUMN_USER_ADDRESS, COLUMN_USER_PHOTO, COLUMN_USER_LEVEL, COLUMN_USER_POSITION, COLUMN_USER_TIME_CREATED, COLUMN_USER_TIME_UPDATED},
                COLUMN_USER_EMAIL + "=? ",
                new String[]{email}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            int emailIndex = cursor.getColumnIndex(COLUMN_USER_EMAIL);
            int passIndex = cursor.getColumnIndex(COLUMN_USER_PASSWORD);
            int phoneIndex = cursor.getColumnIndex(COLUMN_USER_PHONE);
            int addressIndex = cursor.getColumnIndex(COLUMN_USER_ADDRESS);
            int nameIndex = cursor.getColumnIndex(COLUMN_USER_NAME);
            int photoIndex = cursor.getColumnIndex(COLUMN_USER_PHOTO);
            int levelIndex = cursor.getColumnIndex(COLUMN_USER_LEVEL);
            int positionIndex = cursor.getColumnIndex(COLUMN_USER_POSITION);
            int createdIndex = cursor.getColumnIndex(COLUMN_USER_TIME_CREATED);
            int updatedIndex = cursor.getColumnIndex(COLUMN_USER_TIME_UPDATED);

            if (idIndex != -1 && emailIndex != -1 && passIndex != -1 && phoneIndex != -1 && addressIndex != -1) {
                User user = new User(
                        cursor.getInt(idIndex),
                        cursor.getString(emailIndex),
                        cursor.getString(passIndex),
                        cursor.getString(phoneIndex),
                        cursor.getString(addressIndex),
                        cursor.getBlob(photoIndex),
                        cursor.getInt(levelIndex),
                        cursor.getString(positionIndex),
                        cursor.getString(createdIndex),
                        cursor.getString(updatedIndex),
                        cursor.getString(nameIndex)
                );
                cursor.close();
                return user;
            } else {
                Log.e("DBHelper", "Column not found in cursor");
            }
        } else {
            Log.e("DBHelper", "Cursor is empty or null");
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public User getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD, COLUMN_USER_PHONE, COLUMN_USER_NAME, COLUMN_USER_ADDRESS, COLUMN_USER_PHOTO, COLUMN_USER_LEVEL, COLUMN_USER_POSITION, COLUMN_USER_TIME_CREATED, COLUMN_USER_TIME_UPDATED},
                COLUMN_USER_ID + "=? ",
                new String[]{Integer.toString(id)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            int emailIndex = cursor.getColumnIndex(COLUMN_USER_EMAIL);
            int passIndex = cursor.getColumnIndex(COLUMN_USER_PASSWORD);
            int phoneIndex = cursor.getColumnIndex(COLUMN_USER_PHONE);
            int addressIndex = cursor.getColumnIndex(COLUMN_USER_ADDRESS);
            int nameIndex = cursor.getColumnIndex(COLUMN_USER_NAME);
            int photoIndex = cursor.getColumnIndex(COLUMN_USER_PHOTO);
            int levelIndex = cursor.getColumnIndex(COLUMN_USER_LEVEL);
            int positionIndex = cursor.getColumnIndex(COLUMN_USER_POSITION);
            int createdIndex = cursor.getColumnIndex(COLUMN_USER_TIME_CREATED);
            int updatedIndex = cursor.getColumnIndex(COLUMN_USER_TIME_UPDATED);

            if (idIndex != -1 && emailIndex != -1 && passIndex != -1 && phoneIndex != -1 && addressIndex != -1) {
                User user = new User(
                        cursor.getInt(idIndex),
                        cursor.getString(emailIndex),
                        cursor.getString(passIndex),
                        cursor.getString(phoneIndex),
                        cursor.getString(addressIndex),
                        cursor.getBlob(photoIndex),
                        cursor.getInt(levelIndex),
                        cursor.getString(positionIndex),
                        cursor.getString(createdIndex),
                        cursor.getString(updatedIndex),
                        cursor.getString(nameIndex)
                );
                cursor.close();
                return user;
            } else {
                Log.e("DBHelper", "Column not found in cursor");
            }
        } else {
            Log.e("DBHelper", "Cursor is empty or null");
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_ADDRESS, user.getAddress());
        values.put(COLUMN_USER_PHOTO, user.getPhoto());
        values.put(COLUMN_USER_LEVEL, user.getLevel());
        values.put(COLUMN_USER_POSITION, user.getPosition());
        values.put(COLUMN_USER_TIME_CREATED, user.getCreatedAt());
        values.put(COLUMN_USER_TIME_UPDATED, user.getUpdatedAt());
        values.put(COLUMN_USER_NAME, user.getName());
        db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user.getUserId())});
        db.close();
    }

    // CRUD operations for Orders
    public void addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_USER_ID, order.getUserId());
        values.put(COLUMN_ORDER_NAME, order.getName());
        values.put(COLUMN_ORDER_EMAIL, order.getEmail());
        values.put(COLUMN_ORDER_PHONE, order.getPhone());
        values.put(COLUMN_ORDER_ADDRESS, order.getAddress());
        values.put(COLUMN_ORDER_QUANTITY, order.getQuantity());
        values.put(COLUMN_ORDER_PAYMENT_TYPE, order.getPaymentType());
        values.put(COLUMN_ORDER_TOTAL, order.getTotal());
        values.put(COLUMN_ORDER_STATUS, order.getStatus());
        values.put(COLUMN_ORDER_DRIVER_NAME, order.getDriverName());
        values.put(COLUMN_ORDER_ORDER_NAME, order.getOrderName());
        values.put(COLUMN_ORDER_TRACKING_NUMBER, order.getTrackingNumber());
        values.put(COLUMN_ORDER_TIME_CREATED, order.getCreatedAt());
        values.put(COLUMN_ORDER_TIME_FINISHED, order.getFinishedAt());
        db.insert(TABLE_ORDERS, null, values);
        db.close();
    }

    public List<Order> getAllOrders(int userId) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, new String[]{COLUMN_ORDER_ID, COLUMN_ORDER_USER_ID, COLUMN_ORDER_NAME, COLUMN_ORDER_EMAIL, COLUMN_ORDER_PHONE, COLUMN_ORDER_ADDRESS, COLUMN_ORDER_ORDER_NAME, COLUMN_ORDER_QUANTITY, COLUMN_ORDER_PAYMENT_TYPE, COLUMN_ORDER_TOTAL, COLUMN_ORDER_STATUS, COLUMN_ORDER_DRIVER_NAME, COLUMN_ORDER_TRACKING_NUMBER, COLUMN_ORDER_TIME_CREATED, COLUMN_ORDER_TIME_FINISHED},
                COLUMN_ORDER_USER_ID + "=?", new String[]{Integer.toString(userId)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getString(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getString(8),
                        cursor.getDouble(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14)
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return orderList;
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, new String[]{COLUMN_ORDER_ID, COLUMN_ORDER_USER_ID, COLUMN_ORDER_NAME, COLUMN_ORDER_EMAIL, COLUMN_ORDER_PHONE, COLUMN_ORDER_ADDRESS, COLUMN_ORDER_ORDER_NAME, COLUMN_ORDER_QUANTITY, COLUMN_ORDER_PAYMENT_TYPE, COLUMN_ORDER_TOTAL, COLUMN_ORDER_STATUS, COLUMN_ORDER_DRIVER_NAME, COLUMN_ORDER_TRACKING_NUMBER, COLUMN_ORDER_TIME_CREATED, COLUMN_ORDER_TIME_FINISHED},
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getString(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getString(8),
                        cursor.getDouble(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14)
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return orderList;
    }

    public void updateOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_USER_ID, order.getUserId());
        values.put(COLUMN_ORDER_NAME, order.getName());
        values.put(COLUMN_ORDER_EMAIL, order.getEmail());
        values.put(COLUMN_ORDER_PHONE, order.getPhone());
        values.put(COLUMN_ORDER_ADDRESS, order.getAddress());
        values.put(COLUMN_ORDER_ORDER_NAME, order.getOrderName());
        values.put(COLUMN_ORDER_QUANTITY, order.getQuantity());
        values.put(COLUMN_ORDER_PAYMENT_TYPE, order.getPaymentType());
        values.put(COLUMN_ORDER_TOTAL, order.getTotal());
        values.put(COLUMN_ORDER_STATUS, order.getStatus());
        values.put(COLUMN_ORDER_DRIVER_NAME, order.getDriverName());
        values.put(COLUMN_ORDER_TRACKING_NUMBER, order.getTrackingNumber());
        values.put(COLUMN_ORDER_TIME_CREATED, order.getCreatedAt());
        values.put(COLUMN_ORDER_TIME_FINISHED, order.getFinishedAt());
        db.update(TABLE_ORDERS, values, COLUMN_ORDER_ID + " = ?", new String[]{String.valueOf(order.getOrderId())});
        db.close();
    }

    public Order getOrder(String orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, new String[]{COLUMN_ORDER_ID, COLUMN_ORDER_USER_ID, COLUMN_ORDER_NAME, COLUMN_ORDER_EMAIL, COLUMN_ORDER_PHONE, COLUMN_ORDER_ADDRESS, COLUMN_ORDER_ORDER_NAME, COLUMN_ORDER_QUANTITY, COLUMN_ORDER_PAYMENT_TYPE, COLUMN_ORDER_TOTAL, COLUMN_ORDER_STATUS, COLUMN_ORDER_DRIVER_NAME, COLUMN_ORDER_TRACKING_NUMBER, COLUMN_ORDER_TIME_CREATED, COLUMN_ORDER_TIME_FINISHED},
                COLUMN_ORDER_ID + "=?", new String[]{String.valueOf(orderId)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Order order = new Order(
                cursor.getString(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getInt(7),
                cursor.getString(8),
                cursor.getDouble(9),
                cursor.getString(10),
                cursor.getString(11),
                cursor.getString(12),
                cursor.getString(13),
                cursor.getString(14)
        );
        cursor.close();
        return order;
    }

    // CRUD operations for Produk
    public void addProduk(Produk produk) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUK_NAMA, produk.getNamaProduk());
        values.put(COLUMN_PRODUK_STOK, produk.getStokProduk());
        values.put(COLUMN_PRODUK_HARGA, produk.getHargaProduk());
        values.put(COLUMN_PRODUK_GAMBAR, produk.getFotoProduk());
        values.put(COLUMN_PRODUK_TANGGAL_MASUK, produk.getTanggalMasuk());
        values.put(COLUMN_PRODUK_TANGGAL_UPDATE, produk.getTanggalUpdate());
        db.insert(TABLE_PRODUK, null, values);
        db.close();
    }

    public List<Produk> getAllProduk() {
        List<Produk> produkList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUK, new String[]{COLUMN_PRODUK_ID, COLUMN_PRODUK_NAMA, COLUMN_PRODUK_STOK, COLUMN_PRODUK_HARGA, COLUMN_PRODUK_GAMBAR, COLUMN_PRODUK_TANGGAL_MASUK, COLUMN_PRODUK_TANGGAL_UPDATE},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Produk produk = new Produk(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getDouble(3),
                        cursor.getBlob(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                produkList.add(produk);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return produkList;
    }

    public Produk getProdukById(int produkId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUK, new String[]{COLUMN_PRODUK_ID, COLUMN_PRODUK_NAMA, COLUMN_PRODUK_STOK, COLUMN_PRODUK_HARGA, COLUMN_PRODUK_GAMBAR, COLUMN_PRODUK_TANGGAL_MASUK, COLUMN_PRODUK_TANGGAL_UPDATE},
                COLUMN_PRODUK_ID + "=?", new String[]{String.valueOf(produkId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Produk produk = new Produk(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getDouble(3),
                    cursor.getBlob(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
            cursor.close();
            return produk;
        }
        return null;
    }

    public void deleteProdukById(int produkId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PRODUK, COLUMN_PRODUK_ID + " = ?", new String[]{String.valueOf(produkId)});
        db.close();
    }

    public void updateProduk(Produk produk) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUK_NAMA, produk.getNamaProduk());
        values.put(COLUMN_PRODUK_STOK, produk.getStokProduk());
        values.put(COLUMN_PRODUK_HARGA, produk.getHargaProduk());
        values.put(COLUMN_PRODUK_GAMBAR, produk.getFotoProduk());
        values.put(COLUMN_PRODUK_TANGGAL_MASUK, produk.getTanggalMasuk());
        values.put(COLUMN_PRODUK_TANGGAL_UPDATE, produk.getTanggalUpdate());
        db.update(TABLE_PRODUK, values, COLUMN_PRODUK_ID + " = ?", new String[]{String.valueOf(produk.getProdukId())});
        db.close();
    }

    // CRUD operations for Staff
    public void addStaff(Staff staff) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STAFF_NAMA, staff.getNamaStaff());
        values.put(COLUMN_STAFF_JABATAN, staff.getJabatan());
        values.put(COLUMN_STAFF_TELEPON, staff.getTelepon());
        values.put(COLUMN_STAFF_EMAIL, staff.getEmail());
        values.put(COLUMN_STAFF_PASSWORD, staff.getPassword());
        db.insert(TABLE_STAFF, null, values);
        db.close();
    }

    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STAFF, new String[]{COLUMN_STAFF_ID, COLUMN_STAFF_NAMA, COLUMN_STAFF_JABATAN, COLUMN_STAFF_TELEPON, COLUMN_STAFF_EMAIL, COLUMN_STAFF_PASSWORD},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Staff staff = new Staff(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                staffList.add(staff);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return staffList;
    }

    public Staff getStaffById(int staffId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STAFF, new String[]{COLUMN_STAFF_ID, COLUMN_STAFF_NAMA, COLUMN_STAFF_JABATAN, COLUMN_STAFF_TELEPON, COLUMN_STAFF_EMAIL, COLUMN_STAFF_PASSWORD},
                COLUMN_STAFF_ID + "=?", new String[]{String.valueOf(staffId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Staff staff = new Staff(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            cursor.close();
            return staff;
        }
        return null;
    }

    public void updateStaff(Staff staff) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STAFF_NAMA, staff.getNamaStaff());
        values.put(COLUMN_STAFF_JABATAN, staff.getJabatan());
        values.put(COLUMN_STAFF_TELEPON, staff.getTelepon());
        values.put(COLUMN_STAFF_EMAIL, staff.getEmail());
        values.put(COLUMN_STAFF_PASSWORD, staff.getPassword());

        db.update(TABLE_STAFF, values, COLUMN_STAFF_ID + " = ?", new String[]{String.valueOf(staff.getStaffId())});
        db.close();
    }

    public void deleteStaffById(int staffId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_STAFF, COLUMN_STAFF_ID + " = ?", new String[]{String.valueOf(staffId)});
        db.close();
    }
}
