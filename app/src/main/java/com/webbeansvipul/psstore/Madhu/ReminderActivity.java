package com.webbeansvipul.psstore.Madhu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.webbeansvipul.psstore.R;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etTitle , etDescription ;
   // TextView tvStart, tvEnd ;
    Button btnReminder ;
    String title = "", description= "",time1="",time2="" ;
    ProgressDialog pd ;
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_layout);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Set Reminder");

        etTitle = (EditText)findViewById(R.id.etTitle);
        etDescription = (EditText)findViewById(R.id.etDescription);
      //  tvStart =(TextView)findViewById(R.id.tvStartTime);
      //  tvEnd=(TextView)findViewById(R.id.tvEndTime);
        btnReminder = (Button)findViewById(R.id.btnSubmitAskDoc);

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please wait...");

      //  tvEnd.setOnClickListener(this);
     //   tvStart.setOnClickListener(this);
        btnReminder.setOnClickListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSubmitAskDoc:
               title =  etTitle.getText().toString();
               description = etDescription.getText().toString();
             //  time1=tvStart.getText().toString();
             //  time2=tvEnd.getText().toString();

               if(title.equals("")){
                   etTitle.setError("Please Enter Tilte");
               }else if(description.equals("")){
                   etDescription.setError("Please Enter Description");
               }/*else if(time1.equals("")){
                   tvStart.setError("Please Select Start Time");
               }else if(time2.equals("")){
                   tvEnd.setError("Please Select End Time");
               }*/else{
                   setAlarm();
               }


                break;
           /* case R.id.tvStartTime:
              TimePickerDialog mTimePicker;
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                mTimePicker = new TimePickerDialog(ReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvStart.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.tvEndTime:
              TimePickerDialog mTimePicker1;
                Calendar mcurrentTime1 = Calendar.getInstance();
                int hour1 = mcurrentTime1.get(Calendar.HOUR_OF_DAY);
                int minute1 = mcurrentTime1.get(Calendar.MINUTE);
                mTimePicker = new TimePickerDialog(ReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvEnd.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour1, minute1, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;*/
            default:

                break;
        }
    }

    public void setAlarm(){
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);
        intent.putExtra("rrule", "FREQ=DAILY");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}
