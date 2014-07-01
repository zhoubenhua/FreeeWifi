package com.zhephone.hotspot.app.db;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.zhephone.hotspot.app.common.Constant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
public class WifiDatabseAccess extends SQLiteOpenHelper {
    //private static final Logger LOG = LoggerFactory.getLogger(LianLianDatabseAccess.class);
    private static final String TAG = "WifiDatabseAccess";
    private static final String TABLE_TEMP = "_temp";
    public static final QueryResultHandler<String[]> QSTRINGS = new StringArrayQuery();

    /**
     * 创建一个新的实例 LianLianDatabseAccess.
     */
    public WifiDatabseAccess(Context context) {
    	super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }
    
    public void createTableIfNotExisted(SQLiteDatabase db,String tableName,String[] columns){
        String sql = String.format("create table if not exists %s(%s)", tableName,StringUtils.join(columns,","));
        db.execSQL(sql);
    }
    
    public interface QueryResultHandler<T> {
        T handle(Cursor cursor,int numOfCols);
    }
    
    public <T> List<T> queryList(String sql, QueryResultHandler<T> objq,String ...args){
        Cursor c = null;
        List<T> results = Collections.emptyList();
        try{
            c = this.getReadableDatabase().rawQuery(sql, args);
            if(c != null){
	            int n = c.getColumnCount();
	            c.moveToFirst();
	            results = new ArrayList<T>();
	            while(!c.isAfterLast()){
	                results.add(objq.handle(c, n));
	                c.moveToNext();
	            }
            }
        }finally {
            if(c != null && !c.isClosed()){
                c.close();
            }
        }
        return results;
    }

    /**
     * 在该方法中创建应用程序使用的表
     * @TIP ： 每个数据表的操作都需要在createDatabase、dropAllTempTables、renameAllToTemp、insertAllFromTemp 里面对应加上，
     * createDatabase是为了创建数据库，后面三个是为了数据库在后续版本差异化升级过程中保证数据不丢失。必须加，不然会造成不可预料的问题
     * @see com.luluyou.android.lib.db.SqliteDatabaseAccess#createDatabase(android.database.sqlite.SQLiteDatabase)
     */
    private void createDatabase(final SQLiteDatabase db) {
        Log.i(TAG, "将要创建该应用程序使用的表");
        //Log.i(TAG, "创建表为:"+NetworkFlowTable.TABLE_NAME);
        //super.createTableIfNotExisted(db, NetworkFlowTable.TABLE_NAME,NetworkFlowTable.NETWORK_FLOW_TABLE_SCHEMA);
        //super.createTableIfNotExisted(db, NaviTable.TABLE_NAME,NaviTable.NAVI_TABLE_SCHEMA);
        createTableIfNotExisted(db, Constant.ChatTableDef.TABLE_NAME, Constant.ChatTableDef.TABLE_SCHEMA);
    }
    
    private void dropAllTempTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + Constant.ChatTableDef.TABLE_NAME + TABLE_TEMP);
	}
	
	private void renameAllToTemp(SQLiteDatabase db) {
		db.execSQL("ALTER TABLE " + Constant.ChatTableDef.TABLE_NAME + " RENAME TO " + Constant.ChatTableDef.TABLE_NAME + TABLE_TEMP);
	}
	
	private void insertAllFromTemp(SQLiteDatabase db) {
		insertFromTemp(db, Constant.ChatTableDef.TABLE_NAME + TABLE_TEMP, Constant.ChatTableDef.TABLE_NAME);
	}
	
	 /**
     * 
     *insert(这里用一句话描述这个方法的作用）
     *（插入一条记录到数据库中）
     * @param tableName
     * @param values
     * @return
     *long
     * @exception
     * @since 1.0.0
     */
    public synchronized long insert(String tableName,ContentValues values){
        return this.getWritableDatabase().insertOrThrow(tableName, null, values);
    }
    
    public synchronized long delete(String tableName){
    	return delete(tableName, null, null);
    }
    
    public synchronized long delete(String tableName, String whereClause, String[] whereArgs){
    	return this.getWritableDatabase().delete(tableName, whereClause, whereArgs);
    }
    
    /**
     * 
     *queryStrings(这里用一句话描述这个方法的作用）
     *（得到一个字符串数据，其中的值为一条记录的值）
     * @param sql
     * @param args
     * @return
     *String[]
     * @exception
     * @since 1.0.0
     */
    public String[] queryStrings(String sql, String...args){
        return this.queryObject(sql, QSTRINGS, args);
    }
    
    public <T> T queryObject(String sql, QueryResultHandler<T> objq,String ... args){
        Cursor c = null;
        T obj = null;
        try{
            c = this.getReadableDatabase().rawQuery(sql, args);
            if(c != null){
	            if(c.getCount() == 0 || c.isAfterLast()){
	            	obj = null;
	            }else{
	            	c.moveToFirst();
	            	obj = objq.handle(c, c.getColumnCount());
	            }
            }
        }finally {
            if(c != null && !c.isClosed()){
                c.close();
            }
        }
        return obj;
    }
    
    public synchronized int update(String tableName,ContentValues values,String whereClause,String... whereArgs){
        return this.getWritableDatabase().update(tableName, values, whereClause, whereArgs);
    }
    
    public void execSql(String sql){
        this.getWritableDatabase().execSQL(sql);
    }
    
    public void beginTransaction(){
        this.getWritableDatabase().beginTransaction();
    }
    
    public void endTransaction(){
        this.getWritableDatabase().endTransaction();
    }
    
    public void setTransactionSuccessful(){
        this.getWritableDatabase().setTransactionSuccessful();
    }

    /**
	 * 该方法用于表升级时清理相关的表结构和数据
	 * 升级数据库
	 * 
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	private void upgradeWithoutDataChanged(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			// drop temp table
			dropAllTempTables(db);
			// create new table, if old table not exist then create a new one
			onCreate(db);
			// rename all tables
			renameAllToTemp(db);
			// create new table
			onCreate(db);
			// insert data from temp table to new table
			insertAllFromTemp(db);
			// drop temp table
			dropAllTempTables(db);
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * insert from temp table
	 * 
	 * @param db
	 */
	private void insertFromTemp(SQLiteDatabase db, String oldTableName, String newTableName) {
		String columns = getCommonColumns(db, oldTableName, newTableName);
		db.execSQL("INSERT INTO " + newTableName + " (" + columns + ") SELECT " + columns + " FROM " + oldTableName);
	}
	
	/**
	 * get all common columns from table old and new
	 * 
	 * @param db
	 * @param tableName
	 * @return
	 */
	private String getCommonColumns(SQLiteDatabase db, String oldTableName, String newTableName) {
		List<String> oldTableColumns = null;
		List<String> newTableColumns = null;

		oldTableColumns = getTableColumns(db, oldTableName);
		newTableColumns = getTableColumns(db, newTableName);

		Map<String, Integer> newColumns = new HashMap<String, Integer>();
		for (String oColumn : oldTableColumns) {
			newColumns.put(oColumn, 1);
		}

		for (String nColumn : newTableColumns) {
			if (newColumns.containsKey(nColumn)) {
				newColumns.put(nColumn, 2);
			}
		}

		//ID不参与数据插入
		newColumns.remove(BaseColumns._ID);

		List<String> columns = new ArrayList<String>();
		for (Iterator<String> iter = newColumns.keySet().iterator(); iter.hasNext();) {
			String c = iter.next();
			if (newColumns.get(c) == 2) {
				columns.add(c);
			}
		}

		String columnsSeperated = TextUtils.join(",", columns);
		return columnsSeperated;
	}
	
	private List<String> getTableColumns(SQLiteDatabase db, String tableName) {
		ArrayList<String> columns = new ArrayList<String>();
		Cursor c = null;
		try {
			c = db.rawQuery("select * from " + tableName + " limit 1", null);
			if (c != null) {
				columns = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
		return columns;
	}
    
    /* (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        super.close();
    }

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		createDatabase(arg0);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		upgradeWithoutDataChanged(db);
	}

}
