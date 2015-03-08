package bibimba.detaramediaary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2015/02/21.
 */
public class DiaryDatabaseDac extends SQLiteOpenHelper{

    static final String DB_NAME = "detaramediary.db";   // DB名
    static final int DB_VERSION = 1;                // DBのVersion

    // SQL文をStringに保持しておく
    static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS detaramediary" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT,youbi TEXT,hiduke TEXT,tenki TEXT," +
            "itu TEXT,doko TEXT,dare TEXT,donna TEXT,nani TEXT,dousita TEXT,matome TEXT" +
    ",tweetfg INTEGER)";
    static final String DROP_TABLE = "drop table detaramediary;";

    // コンストラクタ
    public DiaryDatabaseDac(Context mContext){
        super(mContext,DB_NAME,null,DB_VERSION);
    }

    //public DiaryDatabaseDac(Context context, String name,
    //                          SQLiteDatabase.CursorFactory factory, int version) {
    //    super(context, name, factory, version);
    //}

    // DBが存在しない状態でOpenすると、onCreateがコールされる
    // 新規作成されたDBのインスタンスが付与されるので、テーブルを作成する。
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    // コンストラクタで指定したバージョンと、参照先のDBのバージョンに差異があるときにコールされる
    // 今回バージョンは１固定のため、処理は行わない。
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
