package bibimba.detaramediaary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    public static final String PREF_NAME = "access_token";
    public static final String TOKEN = "token";
    public static final String TOKEN_SECRET = "token_secret";

    private String token;
    private String tokenSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //日記作成Activity起動
                Intent inten = new Intent(MainActivity.this,DiaryAct.class);
                startActivity(inten);
            }
        });


        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //日記を読む
                Intent inten2 = new Intent(MainActivity.this,DiarySelect.class);
                startActivity(inten2);
            }
        });

        Button btn3 = (Button)findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //終わり
                finish();
            }
        });


        Button btn4 = (Button)findViewById(R.id.button4);

        SharedPreferences preferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        token = preferences.getString(TOKEN,null);
        tokenSecret = preferences.getString(TOKEN_SECRET,null);
        //ついったー認証済みかチェック
        if(token == null || tokenSecret == null) {
            btn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ツイッター認証
                    getpreferences();
                }
            });
        } else {
            btn4.setVisibility(View.INVISIBLE);
        }



    }

    private void getpreferences() {

        /* ConnectivityManagerの取得 */
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if (nInfo == null) {

            showShortToast("いんたーねっとがみつからないです");
            return;
        }

        if (nInfo.isConnected()) {
            Intent intent = new Intent(this,OAuthActivity.class);

            startActivity(intent);
            finish();
        } else {
            showShortToast("ねっとわーくせつぞくをしてください");
            return;
        }

    }

    private void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart(){
        super.onRestart();
    }

}
