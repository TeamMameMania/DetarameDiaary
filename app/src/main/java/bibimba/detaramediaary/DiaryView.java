package bibimba.detaramediaary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import android.view.MotionEvent;

/**
 * Created by user on 2015/02/11.
 */
public class DiaryView extends SurfaceView implements SurfaceHolder.Callback{

    static SQLiteDatabase diarydb;
    private final String TBL_NAME = "detaramediary";

    private static final long waitt = 15;
    private Textdataread dtrd;
    private int susumiguai = 0;
    private String txtnengappi;

    //private TextView hiduketxt;

    private int textSize = 22;
    private int linePointY =0;

    private final int textlefts[] = {225,0,0,0,0,0,0,0,0} ;
    private final int texttops[] = {10,50,90,130,170,210,250,290,330} ;
    private int rotatestopcount[] = new int[9];
    private int kakukotolistsize[] = new int[9];

    private int textheightsize;

    private double rotationcount = 0;
    private final int MAX_KAKUKOTO = 8;

    private int w;
    private int h;

    private final Handler handler;

    public interface Callback {
        public void onDiaryOver();
    }

    private Callback callback;
    public void setCallback(Callback callback) {
        this.callback =callback;
    }



    private Bitmap textbitmaps[] = new Bitmap[9];

    /*
    private Bitmap tenkilistbitmap;
    private Bitmap itulistbitmap;
    private Bitmap dokolistbitmap;
    private Bitmap darelistbitmap;
    private Bitmap nanilistbitmap;
    private Bitmap dousitalistbitmap;
    private Bitmap matomelistbitmap;

    private ArrayList<String> tenkiList = new ArrayList<>();
    private ArrayList<String> ituList = new ArrayList<>();
    private ArrayList<String> dokoList = new ArrayList<>();
    private ArrayList<String> dareList = new ArrayList<>();
    private ArrayList<String> naniList = new ArrayList<>();
    private ArrayList<String> dousitaList = new ArrayList<>();
    private ArrayList<String> matomeList = new ArrayList<>();
*/
    private class DrawThread extends Thread {
        boolean isFinished;
        @Override
        public void run() {
            SurfaceHolder holdes = getHolder();

            while (!isFinished) {
                Canvas canvas = holdes.lockCanvas();
                if (canvas != null ) {
                    drawDiary(canvas);
                    holdes.unlockCanvasAndPost(canvas);
                }

                try {
                    sleep(waitt);
                } catch (InterruptedException e) {

                }
            }
        }
    }

    private DrawThread drawThread;

    public void startDrawThread() {
        stopDrawThread();
        drawThread = new DrawThread();
        drawThread.start();
    }

    public boolean  stopDrawThread() {
        if (drawThread == null) {
            return false;
        }
        drawThread.isFinished = true;
        drawThread =null;
        return true;
    }

    private void diaryOver() {
        //if (isGameOver) {
        //    return;
        //}

        //isGameOver = true;
        ContentValues values = new ContentValues();

        String[] columns = {"hiduke"};
        String where = "hiduke = ?";
        String[] where_args = {txtnengappi};

        Cursor cursor = diarydb.query(TBL_NAME,columns,where,where_args,null,null,null);

        values.put("youbi", dtrd.youbiList.get(rotatestopcount[0]));
        values.put("tenki", dtrd.tenkiList.get(rotatestopcount[1]));
        values.put("itu", dtrd.ituList.get(rotatestopcount[2]));
        values.put("doko", dtrd.dokoList.get(rotatestopcount[3]));
        values.put("dare", dtrd.dareList.get(rotatestopcount[4]));
        values.put("donna", dtrd.donnaList.get(rotatestopcount[5]));
        values.put("nani", dtrd.naniList.get(rotatestopcount[6]));
        values.put("dousita", dtrd.dousitaList.get(rotatestopcount[7]));
        values.put("matome", dtrd.matomeList.get(rotatestopcount[8]));

        //ツイート状態は0（ツイートしてない）で登録する
        values.put("tweetfg",0);


        if (cursor.getCount() > 0 ) {
            diarydb.update(TBL_NAME,values,where,where_args);
        } else {
            values.put("hiduke", txtnengappi);
            diarydb.insert(TBL_NAME, null, values);
        }


        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onDiaryOver();
            }
        });
    }

    public void drawDiary(Canvas canvas) {
        //int width = canvas.getWidth();
        //int height = canvas.getHeight();

        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        //年月日を出力
        canvas.drawText(txtnengappi,0,30,paint);

        //日記本文を出力
        drawtextbitmap(canvas);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDrawThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,int format,int width,int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDrawThread();
    }

    public DiaryView(Context context) {
        super(context);

        //終了時にpostをかけるハンドラ
        handler = new Handler();

        getHolder().addCallback(this);
        //文字列データ準備
        textdataprepare();



        //テキストのビットマップ作成
        textbitmapcreate();

        //データベースのオープン
        DiaryDatabaseDac hlpr = new DiaryDatabaseDac(context);
        diarydb = hlpr.getWritableDatabase();

    }

    private void textdataprepare() {
        dtrd = new Textdataread(this.getContext());

        //日付はその日を取得するのでカレンダーを使ってセット
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        txtnengappi = year + "ねん" + (month + 1) + "がつ" + day + "にち";
    }

    private void textbitmapcreate() {
        /*
        tenkilistbitmap = textarraytobitmap(dtrd.tenkiList);
        itulistbitmap = textarraytobitmap(dtrd.ituList);
        dokolistbitmap = textarraytobitmap(dtrd.dokoList);
        darelistbitmap = textarraytobitmap(dtrd.dareList);
        nanilistbitmap = textarraytobitmap(dtrd.naniList);
        dousitalistbitmap = textarraytobitmap(dtrd.dousitaList);
        matomelistbitmap = textarraytobitmap(dtrd.matomeList);
        */
        textbitmaps[0] =textarraytobitmap(dtrd.youbiList);
        textbitmaps[1] =textarraytobitmap(dtrd.tenkiList);
        textbitmaps[2] =textarraytobitmap(dtrd.ituList);
        textbitmaps[3] =textarraytobitmap(dtrd.dokoList);
        textbitmaps[4] =textarraytobitmap(dtrd.dareList);
        textbitmaps[5] =textarraytobitmap(dtrd.donnaList);
        textbitmaps[6] =textarraytobitmap(dtrd.naniList);
        textbitmaps[7] =textarraytobitmap(dtrd.dousitaList);
        textbitmaps[8] =textarraytobitmap(dtrd.matomeList);

        kakukotolistsize[0] = dtrd.youbiList.size()-1;
        kakukotolistsize[1] = dtrd.tenkiList.size()-1;
        kakukotolistsize[2] = dtrd.ituList.size()-1;
        kakukotolistsize[3] = dtrd.dokoList.size()-1;
        kakukotolistsize[4] = dtrd.dareList.size()-1;
        kakukotolistsize[5] = dtrd.donnaList.size()-1;
        kakukotolistsize[6] = dtrd.naniList.size()-1;
        kakukotolistsize[7] = dtrd.dousitaList.size()-1;
        kakukotolistsize[8] = dtrd.matomeList.size()-1;

    }

    private Bitmap textarraytobitmap(ArrayList<String> arr) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        linePointY = 0;

        String tempstr;
        String maxtext="";

        for (int i = 0;i < arr.size(); i++) {
            tempstr = arr.get(i);
            //今のインデックスの方が文字数多い場合
            if (maxtext.length() < tempstr.length()) {
                maxtext = tempstr;
            }
        }

        //文字テキストの長方形サイズを取得
        paint.getTextBounds(maxtext,0,maxtext.length(),new Rect());

        //文字テキストの幅と高さを取得。
        Paint.FontMetrics fm = paint.getFontMetrics();
        int mtw = (int) paint.measureText(maxtext);//幅
        int fmHeight = (int) ( arr.size() * (Math.abs(fm.top) + fm.bottom));//高さ

        textheightsize = (int)(Math.abs(fm.top) + fm.bottom);

        //ビットマップ作成
        Bitmap bit = Bitmap.createBitmap(mtw + 1 * 2, fmHeight + 1 * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bit);

        for (int i = 0;i < arr.size(); i++) {
            tempstr = arr.get(i);
            //+ System.getProperty("line.separator")
            canvas.drawText(tempstr,1,Math.abs(fm.ascent) + 1 + linePointY,paint);
            linePointY += textheightsize;
            //linePointY += textSize;
        }

        return bit;

    }

    private void drawtextbitmap(Canvas canvas) {




/*        switch (susumiguai) {

            //case -1: //書き始め
            //    kurukurustart();

            case 0: //天気

                if (rotationcount >= dtrd.tenkiList.size() -1 ) {
                    rotationcount = 0;
                }

                break;

            case 1: //いつ

                if (rotationcount >= dtrd.ituList.size() -1) {
                    rotationcount = 0;
                }

                break;

            case 2: //どこで

                if (rotationcount >= dtrd.dokoList.size() -1) {
                    rotationcount = 0;
                }

                break;

            case 3: //誰と

                if (rotationcount >= dtrd.dareList.size() -1) {
                    rotationcount = 0;
                }
                break;

            case 4: //何を

                if (rotationcount >= dtrd.naniList.size() -1) {
                    rotationcount = 0;
                }
                break;

            case 5: //どうした

                if (rotationcount >= dtrd.dousitaList.size() -1) {
                    rotationcount = 0;
                }
                break;

            case 6: //かんそう

                if (rotationcount >= dtrd.matomeList.size() -1) {
                    rotationcount = 0;
                }
                break;

        }
*/
        drawtextbitmap2(canvas);

        if (susumiguai <=  MAX_KAKUKOTO) {
            if (rotationcount >= kakukotolistsize[susumiguai]) {
                rotationcount = 0;
            }
            rotationcount = rotationcount + 0.25;
            //btn.setText(dtrd.kakukotoList.get(susumiguai));
        }

    }

    private void drawtextbitmap2(Canvas canvas) {
        //textbitmaps[susumiguai]

        if (susumiguai > 0) {
            for (int i = 0; i < susumiguai; i++) {
                w = textbitmaps[i].getWidth();
                // 描画元の矩形イメージ（bitmap内の位置）
                Rect src = new Rect(0,rotatestopcount[i] * textheightsize,w,(rotatestopcount[i] + 1) * textheightsize);
                // 描画先の矩形イメージ（画面表示位置・サイズ）
                Rect dst = new Rect(textlefts[i],texttops[i], w + textlefts[i], texttops[i] + textheightsize);

                canvas.drawBitmap(textbitmaps[i], src, dst, null);
            }
        }

        if (susumiguai <= MAX_KAKUKOTO) {
            w = textbitmaps[susumiguai].getWidth();
            //h = tenkilistbitmap.getHeight();
            //Log.v("おおきさ","width:" + w + "height:" + h + "textheight" + textheightsize);

            // 描画元の矩形イメージ（bitmap内の位置）
            //Rect src = new Rect(0, rotationcount * textheightsize , w,   (rotationcount + 1) * textheightsize );
            Rect src = new Rect(0,(int)(rotationcount * textheightsize),w,(int)((rotationcount + 1) * textheightsize) );
            // 描画先の矩形イメージ（画面表示位置・サイズ）
            Rect dst = new Rect(textlefts[susumiguai], texttops[susumiguai], w + textlefts[susumiguai],
                                texttops[susumiguai] + textheightsize);

            canvas.drawBitmap(textbitmaps[susumiguai], src, dst, null);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (susumiguai <= MAX_KAKUKOTO) {
                    //画面をタッチした時点のポインタを保持
                    if (rotationcount >= kakukotolistsize[susumiguai]) {
                        rotationcount = 0;
                    }
                    rotatestopcount[susumiguai] = (int)rotationcount;

                    //次の項目にいくのでカウントを0にする
                    rotationcount = 0;
                    susumiguai++;
                    return true;
                } else {
                    diaryOver();
                }

        }
        return super.onTouchEvent(event);
    }

}
