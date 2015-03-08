package bibimba.detaramediaary;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by user on 2015/02/11.
 */
public class Textdataread {

    public static ArrayList<String> kakukotoList = new ArrayList<String>();

    public ArrayList<String> tenkiList = new ArrayList<String>();
    public ArrayList<String> ituList = new ArrayList<String>();
    public ArrayList<String> dokoList = new ArrayList<String>();
    public ArrayList<String> dareList = new ArrayList<String>();

    public ArrayList<String> donnaList = new ArrayList<String>();

    public ArrayList<String> naniList = new ArrayList<String>();
    public ArrayList<String> dousitaList = new ArrayList<String>();
    public ArrayList<String> matomeList = new ArrayList<String>();

    public ArrayList<String> youbiList = new ArrayList<String>();

    public Textdataread(Context context) {

        //曜日
        //SUNDAY(1)MONDAY(2)TUESDAY(3)WEDNESDAY(4)THURSDAY(5)FRIDAY(6) SATURDAY(7)
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);

        switch (week) {
            case 1:
                youbiList.add("(にちようび)");
                youbiList.add("(日)");
                youbiList.add("(白)");
                youbiList.add("(目)");
                youbiList.add("(サンダイ)");
                youbiList.add("(Ｓｕｎｄａｙ)");
                break;
            case 2:
                youbiList.add("(げつようび)");
                youbiList.add("(月)");
                youbiList.add("(且)");
                youbiList.add("(マンダイ)");
                youbiList.add("(Ｍｏｎｄａｙ)");
                break;
            case 3:
                youbiList.add("(かようび)");
                youbiList.add("(火)");
                youbiList.add("(炎)");
                youbiList.add("(チューズダイ)");
                youbiList.add("(Ｔｕｅｓｄａｙ)");
                break;
            case 4:
                youbiList.add("(すいようび)");
                youbiList.add("(水)");
                youbiList.add("(ウェンズダイ)");
                youbiList.add("(Ｗｅｄｎｅｓｄａｙ)");
                break;
            case 5:
                youbiList.add("(もくようび)");
                youbiList.add("(木)");
                youbiList.add("(本)");
                youbiList.add("(サーズダイ)");
                youbiList.add("(Ｔｈｕｒｓｄａｙ)");
                break;
            case 6:
                youbiList.add("(きんようび)");
                youbiList.add("(金)");
                youbiList.add("(全)");
                youbiList.add("(フライダイ)");
                youbiList.add("(Ｆｒｉｄａｙ)");
                break;
            case 7:
                youbiList.add("(どようび)");
                youbiList.add("(土)");
                youbiList.add("(士)");
                youbiList.add("(サタダイ)");
                youbiList.add("(Ｓａｔｕｒｄａｙ)");
                break;
        }
        Collections.shuffle(youbiList);
        youbiList.add(youbiList.get(0));


        String[] aaa;

        //次書くこと
        aaa = context.getResources().getStringArray(R.array.tugikakukoto);
        for (int i = 0; i < aaa.length; i++) {
            kakukotoList.add(aaa[i]);

            //Log.v("次書くことリスト", "i:" + i + " text:" + aaa[i]);
        }

        //天気
        aaa = context.getResources().getStringArray(R.array.tenki);
        for (int i = 0; i < aaa.length; i++) {
            tenkiList.add(aaa[i]);

            //Log.v("天気リスト", "i:" + i + " text:" + aaa[i]);
        }
        Collections.shuffle(tenkiList);

        //最初のリストを回転時ののりしろとして追加しておく
        tenkiList.add(tenkiList.get(0));

        //いつ
        aaa = context.getResources().getStringArray(R.array.itu);
        for (int i = 0; i < aaa.length; i++) {
            ituList.add(aaa[i]);

            //Log.v("いつリスト","i:" + i + " text:" + aaa[i]);
        }
        Collections.shuffle(ituList);

        ituList.add(ituList.get(0));


        //どこで
        aaa = context.getResources().getStringArray(R.array.doko);
        for (int i = 0; i < aaa.length; i++) {
            dokoList.add(aaa[i]);

            //Log.v("どこリスト","i:" + i + " text:" + aaa[i]);
        }
        Collections.shuffle(dokoList);

        dokoList.add(dokoList.get(0));

        //誰と
        aaa = context.getResources().getStringArray(R.array.dare);
        for (int i = 0; i < aaa.length; i++) {
            dareList.add(aaa[i]);

            //Log.v("誰とリスト","i:" + i + " text:" + aaa[i]);
        }
        Collections.shuffle(dareList);

        dareList.add(dareList.get(0));

        //どんな
        aaa = context.getResources().getStringArray(R.array.donna);
        for (int i = 0; i < aaa.length; i++) {
            donnaList.add(aaa[i]);

            //Log.v("何リスト","i:" + i + " text:" + aaa[i]);
        }
        Collections.shuffle(donnaList);

        donnaList.add(donnaList.get(0));

        //何を
        aaa = context.getResources().getStringArray(R.array.nani);
        for (int i = 0; i < aaa.length; i++) {
            naniList.add(aaa[i]);

            //Log.v("何リスト","i:" + i + " text:" + aaa[i]);
        }
        Collections.shuffle(naniList);

        naniList.add(naniList.get(0));

        //どうした
        aaa = context.getResources().getStringArray(R.array.dousita);
        for (int i = 0; i < aaa.length; i++) {
            dousitaList.add(aaa[i]);

            //Log.v("どうしたリスト","i:" + i + " text:" + aaa[i]);
        }
        Collections.shuffle(dousitaList);

        dousitaList.add(dousitaList.get(0));

        //まとめ
        aaa = context.getResources().getStringArray(R.array.matome);
        for (int i = 0; i < aaa.length; i++) {
            matomeList.add(aaa[i]);

            //Log.v("まとめリスト","i:" + i + " text:" + aaa[i]);
        }
        Collections.shuffle(matomeList);

        matomeList.add(matomeList.get(0));

    }
}
