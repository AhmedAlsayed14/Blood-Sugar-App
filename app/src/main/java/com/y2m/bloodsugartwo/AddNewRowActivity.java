package com.y2m.bloodsugartwo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNewRowActivity extends AppCompatActivity {
    private String type;
    private int value;
    private Long timeInMillsec;
    private String date,time;
    private int mYear,mMonth,mDay;
    private AlertDialog alertDialog;
    SugerDBHandler db;
    int typeIndex=0;
    EditText typeEditText,valueEditText,dateEditText,timeEditText;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_row);
        db=new SugerDBHandler(this);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(Calendar.getInstance().getTime());
        df = new SimpleDateFormat("HH-mm");
        String currentTime= df.format(Calendar.getInstance().getTime());

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int missedDay=prefs.getInt("missedDay",0);
        missedDay=missedDay+1;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("missedDay", missedDay);
        editor.commit();

        timeEditText=(EditText)findViewById(R.id.time);
        dateEditText=(EditText)findViewById(R.id.date);
        valueEditText=(EditText)findViewById(R.id.value);
        typeEditText=(EditText)findViewById(R.id.type);
        save=(Button) findViewById(R.id.save);
        dateEditText.setText(currentDate);
        timeEditText.setText(currentTime);
        typeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {getString(R.string.type1),getString(R.string.type2),getString(R.string.type3)};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewRowActivity.this);
                builder.setTitle(getString(R.string.type));
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        typeIndex=item;
                        switch(item)
                        {
                            case 0:
                                typeEditText.setText(getString(R.string.type1));
                                break;
                            case 1:
                                typeEditText.setText(getString(R.string.type2));
                                break;
                            case 2:
                                typeEditText.setText(getString(R.string.type3));
                                break;
                        }
                        alertDialog.dismiss();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Dialog dialog = new Dialog(AddNewRowActivity.this);
//                dialog.setContentView(R.layout.time_dialog);
//                dialog.setTitle(getString(R.string.time));
//                DatePicker dp=(DatePicker)dialog.findViewById(R.id.datePicker1);
//                TimePicker tp = (TimePicker)dialog.findViewById(R.id.timePicker1);
//                Button save=(Button)dialog.findViewById(R.id.set_time);
//                save.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });

                Calendar mcurrentDate= Calendar.getInstance();
                mYear=mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog mDatePicker=new DatePickerDialog(AddNewRowActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday)
                    {
                        mDay=selectedday;
                        mMonth=selectedmonth;
                        mYear=selectedyear;
                        mMonth+=1;
                        String dateString=new StringBuilder()
                                .append(mYear).append("-").append(mMonth).append("-").append(mDay).toString();
                        Log.d("dateString",dateString);
                        dateEditText.setText(dateString);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle(getResources().getString(R.string.date));
                mDatePicker.show();
            }
        });
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddNewRowActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeEditText.setText( selectedHour + "-" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle(getResources().getString(R.string.time));
                mTimePicker.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value=valueEditText.getText().toString();
                String type=typeEditText.getText().toString();
                String date=dateEditText.getText().toString();
                String time=timeEditText.getText().toString();
                Log.d("value",value);
                Log.d("type",type);
                Log.d("date",date);
                Log.d("time",time);
                if (value.equals("")||type.equals("")||date.equals("")||time.equals(""))
                {
                    Toast.makeText(getApplicationContext(),""+getResources().getString(R.string.miss_input), Toast.LENGTH_LONG).show();
                }
                else
                {
                    String fullTime=date+"-"+time;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                    Date date1=null;
                    try {
                        date1 = dateFormat.parse(fullTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Long timeInMillSec=  date1.getTime();
                    int timeInSec= (int) (timeInMillSec/1000);
                    Log.d("////////// =","TimeInMillSec"+timeInSec);
                    Item item=new Item(Integer.valueOf(value),timeInSec,typeIndex);
                    db=new SugerDBHandler(AddNewRowActivity.this);
                    Long i=db.addNewRow(item);
                    Log.d("////////// i =",""+i);
                    if (i>0)
                    {
                        Log.d("////////// i =",""+i);
                        finish();
                    }
                }
            }
        });
    }
}
