package com.y2m.bloodsugartwo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class EditActivity extends AppCompatActivity {
    private String type;
    private int value;
    private Long timeInMillsec;
    private String date,time;
    private int mYear,mMonth,mDay;
    private AlertDialog alertDialog;
    SugerDBHandler db;
    int typeIndex=0;
    int id;
    EditText typeEditText,valueEditText,dateEditText,timeEditText;
    Button save,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        db=new SugerDBHandler(this);

        typeIndex = getIntent().getExtras().getInt("typeIndex");
        value = getIntent().getExtras().getInt("value");
        id = getIntent().getExtras().getInt("id");
        int timeInSec= getIntent().getExtras().getInt("timeInSec");
        String timeText= String.valueOf(timeInSec);
        timeText+="000";
        Calendar cl = Calendar.getInstance();
        long timeInMillSec= Long.valueOf(timeText);
        Log.d("////////// =","timeInMillSec"+timeInMillSec);

        cl.setTimeInMillis(timeInMillSec);  //here your time in miliseconds
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = "" + cl.get(Calendar.YEAR) + "-" + (cl.get(Calendar.MONTH)+1) + "-" + cl.get(Calendar.DAY_OF_MONTH);
        String time = "" + cl.get(Calendar.HOUR_OF_DAY) + "-" + cl.get(Calendar.MINUTE);


        timeEditText=(EditText)findViewById(R.id.time);
        dateEditText=(EditText)findViewById(R.id.date);
        valueEditText=(EditText)findViewById(R.id.value);
        typeEditText=(EditText)findViewById(R.id.type);
        save=(Button) findViewById(R.id.save);
        delete=(Button) findViewById(R.id.delete);
        dateEditText.setText(date);
        timeEditText.setText(time);
        switch(typeIndex)
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
        valueEditText.setText(String.valueOf(value));

        typeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {getString(R.string.type1),getString(R.string.type2),getString(R.string.type3)};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
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
                Calendar mcurrentDate= Calendar.getInstance();
                mYear=mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog mDatePicker=new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener()
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
                mTimePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeEditText.setText( selectedHour + "-" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle(getResources().getString(R.string.time));
                mTimePicker.show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditActivity.this);
                alert.setTitle("هل تريد تأكيد الحذف");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db=new SugerDBHandler(EditActivity.this);
                        Log.d("////////// id =","id = "+id);
                        int i=db.deleteRow(id);
                        Log.d("////////// i =",""+i);
                        if (i>0)
                        {
                            Log.d("////////// i =",""+i);
                            finish();
                        }
                    }
                });
                alert.show();
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
                    Log.d("////////// =","id "+id);
                    Item item=new Item(id, Integer.valueOf(value),timeInSec,typeIndex);
                    db=new SugerDBHandler(EditActivity.this);
                    int i=db.updateRow(item);
                    Log.d("////////// i =",""+i);
                    Log.d("////////// i =",""+item.toString());
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
