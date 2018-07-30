package oboard.mcard;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

public class EditActivity extends Activity {
    
    static MainActivity main;
    EditText edittext;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        edittext = (EditText)findViewById(R.id.editEditText);
        textview = (TextView)findViewById(R.id.editTextView1);
        edittext.setText(getIntent().getStringExtra("data").toString());
    }
    
    public void Finish(View view) {
        Finish();
    }
    
    public void Finish() {
        S.put("t" + getIntent().getIntExtra("id", -1), edittext.getText().toString()).ok();
        if (main != null)
        main.freshList();
        finishAndRemoveTask();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            Finish();
        return super.onOptionsItemSelected(item);
    }
    
    @Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            Finish();
        return super.onKeyDown(keyCode, event);
    } 

}
