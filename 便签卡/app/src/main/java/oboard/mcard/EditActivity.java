package oboard.mcard;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends Activity {

    static MainActivity main;
    EditText edittext, textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        edittext = (EditText)findViewById(R.id.editEditText);
        textview = (EditText)findViewById(R.id.editTextView1);
        edittext.setText(getIntent().getStringExtra("data").toString());
        textview.setText(S.get("ti" + getIntent().getIntExtra("id", -1), "无标题"));
    }

    public void Finish(View view) {
        Finish();
    }

    public void Finish() {
        int id = getIntent().getIntExtra("id", -1);
        if (S.get("tm", 0) < id) {
            S.addIndex("tm", "t", edittext.getText().toString())
                .addIndex("tim", "ti", textview.getText().toString())
                .ok();
        } else {
            S.put("t" + id, edittext.getText().toString())
                .put("ti" + id, textview.getText().toString())
                .ok();
        }
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
