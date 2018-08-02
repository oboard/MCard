package oboard.mcard;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;
import oboard.mcard.R;

public class AppWidget extends AppWidgetProvider {  
    public static final String BTNACTION = "com.xinxue.action.TYPE_BTN";  

    @Override  
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {  
        super.onUpdate(context, appWidgetManager, appWidgetIds);  
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);  
        //创建一个广播，点击按钮发送该广播  
        //Intent intent = new Intent(BTNACTION);  
       // PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);  
       // remoteViews.setOnClickPendingIntent(R.id.widget_btn, pendingIntent);  
        //如果你添加了多个实例的情况下需要下面的处理  
        for (int i = 0; i < appWidgetIds.length; i++) {  
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);  
        }  
    }  

    @Override  
    public void onReceive(Context context, Intent intent) {  
        switch (intent.getAction()) {  
            case BTNACTION:  
                Toast.makeText(context, "点到我啦！", Toast.LENGTH_SHORT).show();  
                break;  


        }  
        super.onReceive(context, intent);  
    }  
}  
