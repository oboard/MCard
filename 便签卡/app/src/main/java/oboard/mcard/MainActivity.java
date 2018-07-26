package oboard.mcard;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends Activity {
    ArrayList<String> s = new ArrayList<String>();
    ArrayList<CardView> sc = new ArrayList<CardView>();
    Bitmap cacheUI;//界面模糊缓存;
    LinearLayout linearlayout;
    ScrollView scrollview;
    FrameLayout framelayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (Build.VERSION.SDK_INT >= 19) {
            WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
            layoutParams.flags = 67108864 | layoutParams.flags;
        }
        linearlayout = (LinearLayout)findViewById(R.id.mainLinearLayout);
        framelayout = (FrameLayout)findViewById(R.id.mainFrameLayout);
        scrollview = (ScrollView)linearlayout.getParent();

        if (S.init(this, "mcard").get("f", false) == true) {
            S.addIndex("tm", "t", "欢迎使用便签卡，长按删除这条信息").ok();

        }
        S.addIndex("tm", "t", "欢迎使用记录卡，长按删除这条信息").ok();
        //读取信息
        for (int i = 0; i < S.get("tm", 0); i++) {
            s.add(S.get("t" + i, "散落在地上的卡片"));
        }
        freshList();
        linearlayout.setDrawingCacheEnabled(true);
        //设置滚动试图
        scrollview.setOnScrollChangeListener(new ScrollView.OnScrollChangeListener() {
                public void onScrollChange(View view, int a, int b, int c, int d) {
                    if (cacheUI == null) {

                        cacheUI = loadBitmapFromView(linearlayout);
                        //linearlayout.setDrawingCacheEnabled(false);
                        if (cacheUI != null)
                            cacheUI = FastBlur.rsBlur(MainActivity.this, cacheUI, 25);
                    }
                    if (cacheUI != null) {
                        BitmapDrawable bd = 
                            new BitmapDrawable(
                            Bitmap.createBitmap(
                                cacheUI,
                                0,
                                Math.abs(scrollview.getScrollY()),
                                cacheUI.getWidth(), 
                                framelayout.getHeight()));

                        framelayout.setBackgroundDrawable(bd);
                    }
                }
            });
    }

    public void freshList() {
        for (int i = 0; i < s.size(); i++) {
            CardView c = new CardView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 10, 10, 10);
            c.setLayoutParams(lp);
            c.setPadding(20, 100, 20, 100);
            c.setColor(Color.WHITE);
            TextView t = new TextView(this);
            t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            t.setText(s.get(i));
            t.setTextColor(Color.BLACK);
            t.setShadowLayer(5, 1, 1, Color.GRAY);
            c.addView(t);
            linearlayout.addView(c);

        }

    }

    public static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        //c.translate(-v.getScrollX(), -v.getScrollY());
        v.draw(c);
        return screenshot;
    }


}
