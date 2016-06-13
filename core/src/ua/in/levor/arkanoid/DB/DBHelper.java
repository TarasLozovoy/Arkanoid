package ua.in.levor.arkanoid.DB;


import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBHelper {
    private static DBHelper instance;
    private static final String DATABASE_NAME = "arkanoid.db";
    private static final int DATABASE_VERSION = 1;

    private ExecutorService executor;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table if not exists "
            + CurrencyTable.TABLE_NAME
            + "(_id integer primary key autoincrement, "
            + CurrencyTable.Cols.GOLD + " integer default 300, "
            + CurrencyTable.Cols.GEMS + " integer default 10);";

    // Database update sql statement
    private static final String DATABASE_UPDATE = null;

    public static DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper();
        }
        return instance;
    }

    private Database database;
    private boolean DBopened;

    private DBHelper() {
        initDB();
    }

    public void initDB() {
        database = DatabaseFactory.getNewDatabase(DATABASE_NAME,
                DATABASE_VERSION, DATABASE_CREATE, DATABASE_UPDATE);
        database.setupDatabase();

        try {
            database.openOrCreateDatabase();
            DBopened = true;
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        executor = Executors.newSingleThreadExecutor();
        try {
            DatabaseCursor cursor = database.rawQuery("SELECT * FROM " + CurrencyTable.TABLE_NAME);
            if (cursor.getCount() < 1) {
                database.execSQL("INSERT INTO " + CurrencyTable.TABLE_NAME + " DEFAULT VALUES;");

                System.out.println(getGoldFromDB());
                System.out.println(getGemsFromDB());
            }
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    public long getGoldFromDB() {
        try {
            DatabaseCursor cursor = database.rawQuery("SELECT * FROM " + CurrencyTable.TABLE_NAME);
            while (cursor.next()) {
                return cursor.getLong(CurrencyTable.Cols.GOLD);
            }
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getGemsFromDB() {
        try {
            DatabaseCursor cursor = database.rawQuery("SELECT * FROM " + CurrencyTable.TABLE_NAME);
            if (cursor.next()) {
                return cursor.getInt(CurrencyTable.Cols.GEMS);
            }
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateGold(long value) {
        executor.execute(new UpdateGoldTask(value));
    }

    public void updateGems(long value) {
        executor.execute(new UpdateGemsTask(value));
    }

    public void pause() {
        try {
            database.closeDatabase();
            DBopened = false;
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        initDB();
    }

    public void dispose() {
        try {
            database.closeDatabase();
            DBopened = false;
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    private class UpdateGoldTask implements Runnable {
        private long value;

        public UpdateGoldTask(long value) {
            this.value = value;
        }

        @Override
        public void run() {
            if (!DBopened) initDB();
            try {
                database.beginTransaction();
                database.execSQL("UPDATE " + CurrencyTable.TABLE_NAME + " SET " + CurrencyTable.Cols.GOLD + " = " + value);
                database.setTransactionSuccessful();
                database.endTransaction();

            } catch (SQLiteGdxException e) {
                e.printStackTrace();
            }
        }
    }

    private class UpdateGemsTask implements Runnable {
        private long value;

        public UpdateGemsTask(long value) {
            this.value = value;
        }

        @Override
        public void run() {
            try {
                if (!DBopened) initDB();
                database.beginTransaction();
                database.execSQL("UPDATE " + CurrencyTable.TABLE_NAME + " SET " + CurrencyTable.Cols.GEMS + " = " + value);
                database.setTransactionSuccessful();
                database.endTransaction();

            } catch (SQLiteGdxException e) {
                e.printStackTrace();
            }
        }
    }




    //DB layout
    public class CurrencyTable {
        public static final String TABLE_NAME = "currency_table";
        public class Cols {
            public static final String GOLD = "gold";
            public static final String GEMS = "gems";
        }
    }
}
