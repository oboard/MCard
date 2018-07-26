package oboard.mcard;

import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.graphics.Color;
import android.widget.TextView;

public class MainActivity extends Activity 
{
    ArrayList<String> s = new ArrayList<String>();
    ArrayList<CardView> sc = new ArrayList<CardView>();
    
    LinearLayout linearlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        linearlayout = (LinearLayout)findViewById(R.id.mainLinearLayout);
        
        
        if (S.init(this, "mcard").get("f",false) == true) {
            S.addIndex("tm","t","欢迎使用便签卡，长按删除这条信息").ok();
            
        }
        S.addIndex("tm","t","欢迎使用记录卡，长按删除这条信息").ok();
        //读取信息
        for (int i = 0; i < S.get("tm", 0); i++) {
            s.add(S.get("t" + i, "散落在地上的卡片"));
        }
        freshList();
    }
    
    public void freshList() {
        for (int i = 0; i < s.size(); i++) {
            CardView c = new CardView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10,10,10,10);
            c.setLayoutParams(lp);
            c.setPadding(20,100,20,100);
            c.setColor(Color.WHITE);
            c.setRound(2);
            
            TextView t = new TextView(this);
            t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            t.setText(s.get(i));
            t.setTextColor(Color.BLACK);
            t.setShadowLayer(5,1,1,Color.GRAY);
            c.addView(t);
            linearlayout.addView(c);
        }
        
    }
    
    
}
