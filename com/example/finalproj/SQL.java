package com.example.finalproj;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
public class SQL extends SQLiteOpenHelper{

	final static int DB_VERSION = 1;
    final static String DB_NAME = "mydb.db";
    Context context;
	
	//public SQL(Context context, String name, CursorFactory factory, int version) {
	//	super(context, name, factory, version);
		 // Store the context for later use
   //     this.context = context;
        //check if db exists, if not it calls onCreate If it does exists then it checks 
        //the version. If the version is different then it calls the onUpgrade.
//	}
    public SQL (Context context)
    {
    	super(context, DB_NAME,null,DB_VERSION);
    	this.context = context;
    }
    
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE record ("
				+ " _id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "name TEXT NOT NULL, "
				+ "split_by INTEGER NOT NULL, "
			    + "time_spend INTEGER NOT NULL, "
			    + "self TEXT NOT NULL)"
			      );
		//better to create a txt file with all the scripts if they become complex
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//erase or upgrade database
	}

}



