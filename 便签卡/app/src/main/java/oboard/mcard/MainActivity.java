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
import android.content.Intent;
import android.app.ActivityOptions;

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
        //读取信息
        for (int i = 0; i < S.get("tm", 0); i++) {
            s.add(S.get("t" + i, "散落在地上的卡片"));
        }
        freshList();
        //设置滚动试图
        scrollview.setOnScrollChangeListener(new ScrollView.OnScrollChangeListener() {
                public void onScrollChange(View view, int a, int b, int c, int d) {
                    view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    
                    if (cacheUI == null) {

                        cacheUI = loadBitmapFromView(linearlayout);
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
                    view.setLayerType(View.LAYER_TYPE_NONE, null);
                    
                }
            });
    }

    public void freshList() {
        for (int i = 0; i < s.size(); i++) {
            CardView c = new CardView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20, 20, 20, 20);
            c.setLayoutParams(lp);
            c.setPadding(50, 100, 20, 100);
            c.setRound(10);
            //c.setColor(Color.argb(200,255,255,255));
            final TextView t = new TextView(this);
            t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            t.setText(s.get(i));
            t.setTextColor(Color.BLACK);
            t.setShadowLayer(5, 1, 1, Color.GRAY);
            c.addView(t);
            c.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    t.setTransitionName("text");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(new Intent(MainActivity.this, EditActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, t, "text").toBundle());
                    }
                }
            });
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
