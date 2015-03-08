package bibimba.detaramediaary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.os.Handler;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import java.util.ArrayList;


/**
 * Created by user on 2015/02/11.
 */
public class DiaryAct extends ActionBarActivity implements DiaryView.Callback{
    //public class DiaryAct extends Activity{

    private DiaryView SView;

    private Textdataread dtrd;

    private int susumiguai = 0;

    private final int MAX_KAKUKOTO = 6;

    private double kurukurucount = 0;
    private int stopcount = 0;
    private boolean isStoped;

    private Handler kurukuruhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.diarycreate);
        SView = new DiaryView(this);
        SView.setCallback(this);
        setContentView(SView);


        //ボタンのリスナーセット
 /*       Button btn = (Button)findViewById(R.id.kaku);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnclick();
            }
        });

        btn.setText(dtrd.kakukotoList.get(susumiguai));
*/
    }




    private void btnclick() {

        TextView txt;
        Button btn =(Button)findViewById(R.id.kaku);

        if (susumiguai <=  MAX_KAKUKOTO) {
            susumiguai++;
            btn.setText(dtrd.kakukotoList.get(susumiguai));
        } else {
            this.finish();
        }
    }

    @Override
    public void onDiaryOver() {
        SView.diarydb.close();

        SView = null;

        this.finish();

    }

}
