/**
 * NAME: ANKIT SARRAF
 * EMAIL: sarrafan@buffalo.edu
 * ABOUT: This class receives the requests from the main activity
 *        and adds data to the SQLite Table which is maintained
 * PURPOSE: There is no specific usage of the content provider for
 *          the project. But Prof. Steve Ko had asked to Implement it
 *          so that students could understand how to use it.
 * @author sarrafan
 */

package edu.buffalo.cse.cse486586.groupmessenger;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

/**
 * Notes By Prof. Steve Ko:
 * GroupMessengerProvider is a key-value table. Once again, please note that we do not implement
 * full support for SQL as a usual ContentProvider does. We re-purpose ContentProvider's interface
 * to use it as a key-value table.
 * 
 * There are two methods you need to implement---insert() and query(). Others are optional and
 * will not be tested.
 * 
 */
public class GroupMessengerProvider extends ContentProvider {
	//Tag for Logging Activity
	static final String TAG = GroupMessengerActivity.class.getSimpleName();

	DatabaseHelper databaseHelper;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		//Retrieve the database name to work on
		SQLiteDatabase myDB = databaseHelper.getWritableDatabase();

		//Log Entries for the Various attributes to the delete function
		/**Log.d(TAG, "Table Name => " + DatabaseHelper.TABLE_NAME);
		Log.d(TAG, "Where Clause => " + selection);
		for(String s: selectionArgs) {
			Log.d(TAG, "Key => " + s);
		}*/

		//Delete the particular column which has the key
		int deletedRows = myDB.delete(DatabaseHelper.TABLE_NAME, selection + "=?", selectionArgs);

		//To indicate how many rows (row with key value as selectionArgs[0]
		/*Log.d(TAG, 
		  "Inside delete. Deleted # " + deletedRows + " Row with Key - " + selectionArgs[0]);*/

		//Return 0 if no row deleted
		return deletedRows;
	}

	@Override
	public String getType(Uri uri) {
		// You do not need to implement this.
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		/*
		 * values will have two columns (a key column and a value column) 
		 * and one row that contains the actual (key, value) pair to be inserted.
		 * 
		 * For actual storage, you can use any of the two options:
		 * * Using SQLite to store data in tabular form
		 * * Using files to store data
		 * * Using Hash Tables to store data
		 * * Some other not mentioned here
		 * In this project SQLite is used to meet the purpose.
		 */

		//Retrieve the Database to work on
		SQLiteDatabase myDB = databaseHelper.getWritableDatabase();

		try {
			//Get the key Value from the contentValues
			String key = values.get("key").toString();

			//If the key already exists in the table, delete the respective row
			delete(uri, DatabaseHelper.KEY, new String[] {key});

			//Log the attributes for the insert function
			/**Log.d(TAG, "Table Name => " + DatabaseHelper.TABLE_NAME);
			Log.d(TAG, "ContentValues.key => " + values.get("key").toString());
			Log.d(TAG, "ContentValues.value => " + values.get("value"));*/

			//id variable stores the result of insert
			//If id is -1 there was some error while inserting the row. So no row inserted
			//If some positive value represents the row where insertion was done
			long id = 0;

			id = myDB.insert(DatabaseHelper.TABLE_NAME, null, values);

			if(id == -1) {
				Log.e(TAG, "Error while inserting => " + key);
			} else {
				Log.d(TAG, "Insertion Successful at Row => " + id);
			}

			uri = ContentUris.withAppendedId(uri, id);
			//Log.d(TAG, "URI => " + uri);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			myDB.close();
		}
		//</MY CODE>

		Log.v("insert", values.toString());
		return uri;
	}

	@Override
	public boolean onCreate() {
		// If you need to perform any one-time initialization task, please do it here.

		//Create the instance of the DatabaseHelper class
		databaseHelper = new DatabaseHelper(getContext());

		//Retrieve the Database which this code will work on
		SQLiteDatabase myDB = databaseHelper.getWritableDatabase();

		//Log the name of the Database
		Log.d(TAG, "Fetch myDB => " + myDB.toString());

		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		/*
		 * Return a Cursor object
		 * with the right format. If the formatting is not correct, then it is not going to work.
		 * 
		 * If you use SQLite, whatever is returned from SQLite is a Cursor object. However, you
		 * still need to be careful because the formatting might still be incorrect.
		 * 
		 * If you use a file storage option, then it is your job to build a Cursor * object. I
		 * recommend building a MatrixCursor described at:
		 * http://developer.android.com/reference/android/database/MatrixCursor.html
		 */

		//Retrieve the Database to work on
		SQLiteDatabase myDB = databaseHelper.getWritableDatabase();
		Cursor cursor;

		try {
			String [] columns = {DatabaseHelper.KEY, DatabaseHelper.VALUE};

			//Log.d(TAG, "Column Names => <" + columns[0] + "," + columns[1] + ">");
			//Log.d(TAG, "TableName => " + DatabaseHelper.TABLE_NAME);
			//Log.d(TAG, "Selection => " + selection);

			selectionArgs = new String [] {selection};

			cursor = myDB.query(DatabaseHelper.TABLE_NAME, columns, columns[0] + "=?", 
					selectionArgs, null, null, null);

			Log.e(TAG, "Number of Rows Retrieved => " + cursor.getCount());
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			myDB.close();
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}

	protected final class DatabaseHelper extends SQLiteOpenHelper {
		//Initialize the Database Name
		private static final String DATABASE_NAME = "keyvaluedatabase.db";

		//Initialize the Table Name
		private static final String TABLE_NAME = "KEYVALUETABLE";

		//Initialize the Database Version
		private static final int DATABASE_VERSION = 1;

		//Columns in KEYVALUETABLE
		//Column Key
		private static final String KEY = "key";
		//Column Value
		private static final String VALUE = "value";

		//Query String for Creating KEYVALUETABLE
		private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
				KEY + " VARCHAR(255) PRIMARY KEY, " +
				VALUE + " VARCHAR(255));";

		//Query string if we require to drop the table before upgrading it
		private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

		Context context;

		DatabaseHelper(Context context) {
			//Call the super() class contructor
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

			//Log to indicate the presence in the DatabaseHelper constructor
			Log.d(TAG, "Inside DatabaseHelper Constructor");

			//Setting the context data-member
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase myDB) {
			//Go ahead and create the table
			myDB.execSQL(CREATE_TABLE);

			//Log to indicate creation of the table
			Log.d(TAG, "Table created => " + CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase myDB, int arg1, int arg2) {
			//If the table already exists and it needs to be modified, drop the table first
			myDB.execSQL(DROP_TABLE);

			//Log to indicate Table was Dropped
			Log.d(TAG, "Table Dropped => " + TABLE_NAME);

			//call the onCreate() to create the new modified table
			onCreate(myDB);
		}
	}
}