package oboard.mcard;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.animation.ValueAnimator;
import android.view.animation.BounceInterpolator;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ClipData;
import android.animation.Animator;

public class MainActivity extends Activity {
    ArrayList<String> s = new ArrayList<String>();
    //ArrayList<CardView> sc = new ArrayList<CardView>();
    Bitmap cacheUI;//界面模糊缓存;
    LinearLayout linearlayout, menu;
    ScrollView scrollview;
    FrameLayout framelayout;
    ImageView imageview;
    WallpaperManager mWallpaperManager;
    Button plus;

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
        imageview = (ImageView)findViewById(R.id.mainImageView);
        menu = (LinearLayout)findViewById(R.id.mainMenu);
        plus = (Button)findViewById(R.id.plus);

        S.init(this, "mcard");
        if (S.get("tm", 0) == 0) {
            S.addIndex("tm", "t", "欢迎使用便签卡\n长按删除这条信息\n点击编辑这条信息\n开发者@一块小板子")
                .addIndex("tim", "ti", "｡◕‿◕｡")
                .ok();
        }

        mWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        ((FrameLayout)scrollview.getParent()).setBackgroundDrawable(mWallpaperManager.getDrawable());


        freshList();
        //设置滚动试图
        scrollview.setOnScrollChangeListener(new ScrollView.OnScrollChangeListener() {
                public void onScrollChange(View view, int a, int b, int c, int d) {
                    if (cacheUI != null) {
                        if (Math.abs(scrollview.getScrollY()) + framelayout.getHeight() <= cacheUI.getHeight()) {
                            BitmapDrawable bd = 
                                new BitmapDrawable(
                                Bitmap.createBitmap(
                                    cacheUI,
                                    0,
                                    Math.abs(scrollview.getScrollY()),
                                    cacheUI.getWidth(), 
                                    framelayout.getHeight()));

                            if (bd != null) {
                                framelayout.setBackground(bd);
                            }
                        }
                    }

                }
            });

        imageview.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ValueAnimator v = ValueAnimator.ofFloat(1.0f, 0f).setDuration(450);
                    v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator v) {
                                float i = v.getAnimatedValue();
                                menu.setScaleX(i);
                                menu.setScaleY(i);
                                menu.setAlpha(i);
                            }
                        });
                    v.addListener(new ValueAnimator.AnimatorListener() {
                            public void onAnimationCancel(Animator a) {

                            }
                            public void onAnimationStart(Animator a) {

                            }
                            public void onAnimationRepeat(Animator a) {

                            }
                            public void onAnimationEnd(Animator a) {
                                menu.setVisibility(View.GONE);
                            }
                        });
                    v.setInterpolator(new BounceInterpolator());
                    v.start();
                    ValueAnimator v2 = ValueAnimator.ofFloat(0, 1).setDuration(225);
                    v2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator v) {
                                imageview.setAlpha((float)v.getAnimatedValue());
                            }
                        });
                    v2.addListener(new ValueAnimator.AnimatorListener() {
                            public void onAnimationCancel(Animator a) {

                            }
                            public void onAnimationStart(Animator a) {

                            }
                            public void onAnimationRepeat(Animator a) {

                            }
                            public void onAnimationEnd(Animator a) {
                                imageview.setVisibility(View.GONE);
                            }
                        });
                    v2.start();
             
                    ((ViewGroup)scrollview.getParent()).removeView((View)view.getTag());
                   // freshList();
                }
            });

        plus.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int id = S.get("tm", -1) + 1;
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);

                    intent.putExtra("id", id);
                    intent.putExtra("data", "");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    }
                    startActivity(intent);
                }
            });

        EditActivity.main = this;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            updateCache();
        }
    }
    
    public void deleteCard(View view) {
        S.delIndex("tm", "t", ((int)((CardView)imageview.getTag()).getTag()))
        .ok();
        imageview.performClick();
        freshList();
    }
    
    public void copyCard(View view) {
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(null, S.get("t" + ((int)((CardView)imageview.getTag()).getTag()), "")));
        imageview.performClick();
    }
    
    
    public void shareCard(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, S.get("t" + ((int)((CardView)imageview.getTag()).getTag()), ""));
        startActivity(Intent.createChooser(i, "Share"));
        imageview.performClick();
    }
    

    public void updateCache() {
        cacheUI = null;
        cacheUI = loadBitmapFromView(linearlayout);
        cacheUI = FastBlur.rsBlur(MainActivity.this, cacheUI, 25);
    }

    public void freshList() {
        s.clear();
        //读取信息
        for (int i = 0; i < S.get("tm", 0); i++) {
            s.add(S.get("t" + i, "散落在地上的卡片"));
        }
        linearlayout.removeAllViews();
        //sc.clear();
        for (int i = 0; i < s.size(); i++) {
            CardView c = new CardView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20, 20, 20, 20);
            c.setLayoutParams(lp);
            c.setPadding(50, 100, 20, 100);
            c.setRound(10);
            c.setTag(i);
            //c.setColor(Color.argb(250,255,255,255));
            final TextView t = new TextView(this);
            t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            t.setText(s.get(i));
            t.setTextColor(Color.BLACK);
            t.setShadowLayer(5, 1, 1, Color.GRAY);
            c.addView(t);
            final int j = i;//final的i
            c.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        //  t.setTransitionName("text");
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        //intent.setAction(Intent.ACTION_VIEW);
                        intent.putExtra("id", j);
                        intent.putExtra("data", S.get("t" + j, t.getText().toString()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        }//, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, t, "text").toBundle());

                        startActivity(intent);

                    }
                });
            c.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        int h = (scrollview.getHeight() > cacheUI.getHeight()) ? cacheUI.getHeight() : scrollview.getHeight();
                        if (cacheUI != null) {
                            Bitmap cacheB = Bitmap.createBitmap(cacheUI);
                            Canvas c = new Canvas(cacheB);
                            c.drawColor(Color.BLACK);
                            c.drawBitmap(cacheUI, 0, 0, null);
                            imageview.setBackgroundColor(Color.BLACK);
                            imageview.setImageBitmap(Bitmap.createBitmap(
                                                                           cacheB,
                                                                           0,
                                                                           Math.abs(scrollview.getScrollY()),
                                                                           cacheUI.getWidth(), 
                                                                           h));
                        }
                        linearlayout.removeView(view);
                        ((ViewGroup)scrollview.getParent()).addView(view);
                        freshList();
                        view.setElevation(20f);
                        view.setZ(20f);
                        view.setTranslationY(framelayout.getHeight());
                        final View vv = view;
                        ValueAnimator v = ValueAnimator.ofFloat(0.5f, 1.0f).setDuration(450);
                        v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator v) {
                                    float i = v.getAnimatedValue();
                                    menu.setScaleX(i);
                                    menu.setScaleY(i);
                                    vv.setScaleX(i);
                                    vv.setScaleY(i);
                                    menu.setAlpha(i);
                                }
                            });
                        
                        v.setInterpolator(new BounceInterpolator());
                        v.start();
                        ValueAnimator v2 = ValueAnimator.ofFloat(0, 1).setDuration(225);
                        v2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator v) {
                                    imageview.setAlpha((float)v.getAnimatedValue());
                                }
                            });
                        v2.start();
                        imageview.setTag(view);
                        imageview.setVisibility(View.VISIBLE);
                        menu.setVisibility(View.VISIBLE);
                        return true;
                    }
                });
            linearlayout.addView(c);
        }
    }


    public Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        //c.drawColor(Color.BLACK);
        v.draw(c);
        return screenshot;
    }

}
