package bibimba.detaramediaary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBarActivity;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by user on 2015/02/22.
 */
public class DiaryRead extends ActionBarActivity{

    static SQLiteDatabase diarydb;
    private final String TBL_NAME = "detaramediary";



    public static final String PREF_NAME = "access_token";
    public static final String TOKEN = "token";
    public static final String TOKEN_SECRET = "token_secret";

    private String token;
    private String tokenSecret;

    private static String txtnengappi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diaryread);

        //日記選択画面で渡された日付を拾う
        Bundle extras = getIntent().getExtras();
        String hidukecont = extras.getString("ITEM");

        dataset(hidukecont);

        Button btn = (Button)findViewById(R.id.modorubutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //セレクト画面に戻るん
                modoru();
            }
        });


    }

    private void doTweet() {

        // 改行を文字列に含める
        StringBuffer str = new StringBuffer();
        str.append("#でたらめにっき");
        // プラットフォームに適した改行コードを取得\
        str.append(System.getProperty("line.separator"));

        TextView txt;

        txt = (TextView)findViewById(R.id.textnengappi);
        str.append(txt.getText());

        txt = (TextView)findViewById(R.id.textyoubi);
        str.append(txt.getText());
        str.append(System.getProperty("line.separator"));

        txt = (TextView)findViewById(R.id.texttenki);
        str.append(txt.getText());
        str.append(System.getProperty("line.separator"));

        txt = (TextView)findViewById(R.id.textitu);
        str.append(txt.getText());
        str.append(System.getProperty("line.separator"));

        txt = (TextView)findViewById(R.id.textdoko);
        str.append(txt.getText());
        str.append(System.getProperty("line.separator"));

        txt = (TextView)findViewById(R.id.textdare);
        str.append(txt.getText());
        str.append(System.getProperty("line.separator"));

        txt = (TextView)findViewById(R.id.textdonna);
        str.append(txt.getText());
        str.append(System.getProperty("line.separator"));

        txt = (TextView)findViewById(R.id.textnani);
        str.append(txt.getText());
        str.append(System.getProperty("line.separator"));

        txt = (TextView)findViewById(R.id.textdousita);
        str.append(txt.getText());
        str.append(System.getProperty("line.separator"));

        txt = (TextView)findViewById(R.id.textmatome);
        str.append(txt.getText());
        str.append(System.getProperty("line.separator"));

        String tweet = str.toString();

        new TweetTask().execute(tweet);


    }



    private void modoru() {
        this.finish();
    }

    private void dataset(String hidukestr) {

        //データベースのオープン
        DiaryDatabaseDac hlpr = new DiaryDatabaseDac(this.getApplicationContext());
        diarydb = hlpr.getReadableDatabase();


        if (hidukestr == "") {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            txtnengappi = year + "ねん" + (month + 1) + "がつ" + day + "にち";
        } else {
            txtnengappi = hidukestr;
        }



        //ContentValues values = new ContentValues();

        String[] columns = {"youbi","hiduke","tenki","itu","doko","dare","donna","nani","dousita","matome","tweetfg"};
        String where = "hiduke = ?";
        String[] where_args = {txtnengappi};

        Cursor cursor = diarydb.query(TBL_NAME,columns,where,where_args,null,null,null);

        if (cursor.moveToFirst()) {

            TextView txt;

            txt = (TextView)findViewById(R.id.textnengappi);
            txt.setText(txtnengappi);

            txt = (TextView)findViewById(R.id.textyoubi);
            txt.setText(cursor.getString(cursor.getColumnIndex("youbi")));

            txt = (TextView)findViewById(R.id.texttenki);
            txt.setText(cursor.getString(cursor.getColumnIndex("tenki")));

            txt = (TextView)findViewById(R.id.textitu);
            txt.setText(cursor.getString(cursor.getColumnIndex("itu")));

            txt = (TextView)findViewById(R.id.textdoko);
            txt.setText(cursor.getString(cursor.getColumnIndex("doko")));

            txt = (TextView)findViewById(R.id.textdare);
            txt.setText(cursor.getString(cursor.getColumnIndex("dare")));

            txt = (TextView)findViewById(R.id.textdonna);
            txt.setText(cursor.getString(cursor.getColumnIndex("donna")));

            txt = (TextView)findViewById(R.id.textnani);
            txt.setText(cursor.getString(cursor.getColumnIndex("nani")));

            txt = (TextView)findViewById(R.id.textdousita);
            txt.setText(cursor.getString(cursor.getColumnIndex("dousita")));

            txt = (TextView)findViewById(R.id.textmatome);
            txt.setText(cursor.getString(cursor.getColumnIndex("matome")));

            Button btn = (Button)findViewById(R.id.tweetbutton);
            Integer tfg = cursor.getInt(cursor.getColumnIndex("tweetfg"));



            SharedPreferences preferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
            token = preferences.getString(TOKEN,null);
            tokenSecret = preferences.getString(TOKEN_SECRET,null);
            //ついったー認証済みかチェック
            if(token == null || tokenSecret == null) {
                btn.setText("ついったーにんしょうされてません");
                btn.setEnabled(false);
            } else {
                //すでについーと済みだったら二重投稿できないようにする
                if (tfg == 1) {
                    btn.setText("この日記はついーとずみです");
                    btn.setEnabled(false);
                } else {
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //ツイートする
                            doTweet();
                        }
                    });
                }
            }

        } else {
            Toast.makeText(getApplicationContext(),"データ読み込み失敗",Toast.LENGTH_SHORT);

        }

        diarydb.close();

    }


    public class TweetTask extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... params) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(getString(R.string.consumer_key));
            builder.setOAuthConsumerSecret(getString(R.string.consumer_secret));
            builder.setOAuthAccessToken(token);
            builder.setOAuthAccessTokenSecret(tokenSecret);
            builder.setMediaProvider("TWITTER");

            twitter4j.conf.Configuration conf = builder.build();

            String tweet = params[0];

            Twitter twitter = new TwitterFactory(conf).getInstance();


            try {
                twitter.updateStatus(tweet);
            } catch (TwitterException e) {
                e.printStackTrace();
                showShortToast("にっきをついーとできませんでしたよ");
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            showShortToast("にっきをついーとしました");

            Button btn2 = (Button)findViewById(R.id.tweetbutton);
            //btn2.setVisibility(View.INVISIBLE);
            btn2.setText("この日記はついーとずみです");
            btn2.setEnabled(false);


            //ツイートフラグを1（ツイート済み）に更新する
            //データベースのオープン
            DiaryDatabaseDac hlpr = new DiaryDatabaseDac(getApplicationContext());
            diarydb = hlpr.getReadableDatabase();

            ContentValues values = new ContentValues();

            //String[] columns = {"hiduke"};
            String where = "hiduke = ?";
            String[] where_args = {txtnengappi};

            //ツイート状態は0（ツイートしてない）で登録する
            values.put("tweetfg",1);


            //if (cursor.getCount() > 0 ) {
            diarydb.update(TBL_NAME,values,where,where_args);
            //} else {
            //    values.put("hiduke", txtnengappi);
            //    diarydb.insert(TBL_NAME, null, values);
            //}

            diarydb.close();

        }
    }
    private void showShortToast(String text) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
}
