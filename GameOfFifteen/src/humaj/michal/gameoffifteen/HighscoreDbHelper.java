package humaj.michal.gameoffifteen;

import humaj.michal.gameoffifteen.HighscoreContract.HighscoreEntry;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HighscoreDbHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Highscore.db";

	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ HighscoreEntry.TABLE_NAME + " (" + HighscoreEntry._ID
			+ " INTEGER PRIMARY KEY,"
			+ HighscoreEntry.COLUMN_NAME_IS_GALLERY_PIC + INTEGER_TYPE
			+ COMMA_SEP + HighscoreEntry.COLUMN_NAME_PIC_RES_ID + INTEGER_TYPE
			+ COMMA_SEP + HighscoreEntry.COLUMN_NAME_PIC_FILENAME + TEXT_TYPE
			+ COMMA_SEP + HighscoreEntry.COLUMN_NAME_3x3_TIME + TEXT_TYPE
			+ COMMA_SEP + HighscoreEntry.COLUMN_NAME_4x4_TIME + TEXT_TYPE
			+ COMMA_SEP + HighscoreEntry.COLUMN_NAME_5x5_TIME + TEXT_TYPE
			+ COMMA_SEP + HighscoreEntry.COLUMN_NAME_6x6_TIME + TEXT_TYPE
			+ COMMA_SEP + HighscoreEntry.COLUMN_NAME_3x3_MOVES + INTEGER_TYPE
			+ COMMA_SEP + HighscoreEntry.COLUMN_NAME_4x4_MOVES + INTEGER_TYPE
			+ COMMA_SEP + HighscoreEntry.COLUMN_NAME_5x5_MOVES + INTEGER_TYPE
			+ COMMA_SEP + HighscoreEntry.COLUMN_NAME_6x6_MOVES + INTEGER_TYPE
			+ " )";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ HighscoreEntry.TABLE_NAME;

	public HighscoreDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}
