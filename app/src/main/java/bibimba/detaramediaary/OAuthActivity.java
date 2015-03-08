package bibimba.detaramediaary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import android.os.AsyncTask;


/**
 * Created by user on 2015/03/07.
 */
public class OAuthActivity extends Activity{

    private OAuthAuthorization mOAuth = null;
    private RequestToken mReq = null;
    public static final String CALLBACK_URL = "callback://OAuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_oauth);
        new OAuthTask().execute();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        new TokenTask().execute(intent);

    }

    public class OAuthTask extends AsyncTask <Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            Configuration conf = ConfigurationContext.getInstance();
            mOAuth = new OAuthAuthorization(conf);
            mOAuth.setOAuthConsumer(getString(R.string.consumer_key), getString(R.string.consumer_secret));
            try {
                mReq = mOAuth.getOAuthRequestToken(CALLBACK_URL);
            } catch (TwitterException e) {
                e.printStackTrace();
                return null;
            }

            String uri;
            uri = mReq.getAuthenticationURL();
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(uri)));
            return null;
        }
    }

    public class TokenTask extends AsyncTask<Intent,Void,AccessToken> {

        @Override
        protected AccessToken doInBackground(Intent... params) {
            Intent intent = params[0];
            Uri uri = intent.getData();
            AccessToken token = null;
            if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
                String verifier = uri.getQueryParameter("oauth_verifier");
                try {
                    token = mOAuth.getOAuthAccessToken(mReq,verifier);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return token;
        }


        @Override
        protected void onPostExecute(AccessToken result) {
            if (result != null) {
                SharedPreferences preferences = getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(MainActivity.TOKEN,result.getToken());
                editor.putString(MainActivity.TOKEN_SECRET,result.getTokenSecret());
                editor.commit();

                Intent sIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(sIntent);
            }
            finish();
        }

    }
}
