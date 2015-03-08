package bibimba.detaramediaary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBarActivity;




/**
 * Created by user on 2015/02/22.
 */
public class DiarySelect extends ActionBarActivity{

    private static SQLiteDatabase diarydb;
    private final String TBL_NAME = "detaramediary";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diaryselect);

        dataset();

        Button btn = (Button)findViewById(R.id.titlemodoru);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modoru();
            }
        });




    }



    private void modoru() {
        this.finish();
    }

    private void dataset() {

        //データベースのオープン
        DiaryDatabaseDac hlpr = new DiaryDatabaseDac(this.getApplicationContext());
        diarydb = hlpr.getReadableDatabase();

        /*
        //日付はその日を取得するのでカレンダーを使ってセット
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String txtnengappi = year + "ねん" + (month + 1) + "がつ" + day + "にち";

        //ContentValues values = new ContentValues();


        String where = "hiduke = ?";
        String[] where_args = {txtnengappi};
*/
        String[] columns = {"hiduke","tenki","youbi"};
        Cursor cursor = diarydb.query(TBL_NAME,null,null,null,null,null,null);

        int[] to = {R.id.rowhiduke,R.id.rowtenki,R.id.rowyoubi};

        SimpleCursorAdapter sadapt = new SimpleCursorAdapter(this.getApplicationContext(),
                R.layout.diaryselectrow,
                cursor,
                columns,
                to,
                0);

        ListView diarylist =(ListView)findViewById(R.id.list_view);
        diarylist.setAdapter(sadapt);

        diarydb.close();

        //if (cursor.getCount()==0) {
        //    Toast.makeText(getApplicationContext(), "まだいちにちもにっきかいてません", Toast.LENGTH_SHORT);
        //    this.finish();
        //}

        //アイテムクリック
        diarylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                ListView listView = (ListView)parent;

                Intent intent = new Intent(DiarySelect.this, DiaryRead.class);


                String itm =((TextView)view.findViewById(R.id.rowhiduke)).getText().toString();

                intent.putExtra("ITEM", itm);

                startActivity(intent);
            }
        });
    }

}

