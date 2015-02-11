package com.example.amado.taskit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class TaskActivity extends ActionBarActivity{
    public static final String EXTRA ="TaskExtra";
    private static final String TAG = "TaskActivity";
    private CheckBox mDoneBox;
    private Task mTask;
    private Button mDateButton;
    private EditText mTaskNameInput;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        mTask =(Task)getIntent().getSerializableExtra(EXTRA);

        if(mTask == null){
            mTask = new Task();
        }

        mCalendar = Calendar.getInstance();


        mTaskNameInput = (EditText)findViewById(R.id.taskName);
        mDateButton = (Button)findViewById(R.id.taskDate);
        mDoneBox = (CheckBox)findViewById(R.id.taskDone);
        Button saveButton = (Button)findViewById(R.id.saveButton);

        mTaskNameInput.setText(mTask.getName());
        if(mTask.getDueDate()== null){
            mCalendar.setTime(new Date() );
            mDateButton.setText(getResources().getString(R.string.noDate));
        }else{
            mCalendar.setTime(mTask.getDueDate());
        updateButton();
        }

        mDoneBox.setChecked(mTask.isDone());

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date = new DatePickerDialog(TaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        mCalendar.set(Calendar.MONTH, monthOfYear);
                        mCalendar.set(Calendar.YEAR, year);
                        updateButton();
                    }
                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                date.show();
            }

        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mTask.setName(mTaskNameInput.getText().toString());
            mTask.setDone(mDoneBox.isChecked());
            mTask.setDueDate(mCalendar.getTime());
                Intent i = new Intent();
                i.putExtra(EXTRA, mTask);
                setResult(RESULT_OK, i);
            finish();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void updateButton(){
        DateFormat df = DateFormat.getDateInstance();
        mDateButton.setText(df.format(mCalendar.getTime()));
    }
}
