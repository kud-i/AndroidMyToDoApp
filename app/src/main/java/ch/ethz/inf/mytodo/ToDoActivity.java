package ch.ethz.inf.mytodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class ToDoActivity extends AppCompatActivity {
    private int mPosition = -1;
    private boolean mDone = false;
    private Calendar calendar;
    private TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();

        Intent intent = getIntent();

        dateView = (TextView) findViewById(R.id.editDateText);

        int i = intent.getIntExtra("position",-1);

        if (i >= 0) {
            //EDIT Mode
            setTitle(getString(R.string.ChangeToDo));
            mPosition = i;
            TextView title = (TextView) findViewById(R.id.editTitleText);

            title.setText(intent.getStringExtra("title"));
            dateView.setText(intent.getStringExtra("date"));

            mDone = intent.getBooleanExtra("done", false);
        }else{
            //New Mode
            setTitle(getString(R.string.NewToDo));
            showDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
        }

    }


    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
    @Override
    public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        // arg1 = year
        // arg2 = month
        // arg3 = day
        showDate(arg1, arg2+1, arg3);
    }
};

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append(".")
                .append(month).append(".").append(year));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            TextView titleTextView = (TextView) findViewById(R.id.editTitleText);
            TextView dateTextView = (TextView) findViewById(R.id.editDateText);

            String title = titleTextView.getText().toString();
            String date = dateTextView.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("date", date);
            resultIntent.putExtra("done", mDone);
            resultIntent.putExtra("position", mPosition);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();

            super.onBackPressed();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
