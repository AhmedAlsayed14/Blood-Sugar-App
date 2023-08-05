package com.y2m.bloodsugartwo;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AverageActivity extends AppCompatActivity {
    private EditText fromEditText,toEditText;
    private Button calculateButton;
    private TextView type1TextView,type2TextView,type3TextView;
    private int fromYear,fromMonth,fromDay,toYear,toMonth,toDay;
    int fromTimeInSec=0,toTimeInSec=0;
    SugerDBHandler db;
    double type1Count=0,type1Value=0,type2Count=0,type2Value=0,type3Count=0,type3Value=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average);
        fromEditText=(EditText)findViewById(R.id.from);
        toEditText=(EditText)findViewById(R.id.to);
        calculateButton=(Button)findViewById(R.id.calculate);
        type1TextView=(TextView)findViewById(R.id.type1);
        type2TextView=(TextView)findViewById(R.id.type2);
        type3TextView=(TextView)findViewById(R.id.type3);


        String font = "ae_AlYermook.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), font);
        type1TextView.setTypeface(tf);
        type2TextView.setTypeface(tf);
        type3TextView.setTypeface(tf);
        db=new SugerDBHandler(this);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromTimeInSec==0)
                    Toast.makeText(AverageActivity.this,getResources().getString(R.string.averages_date_From_miss), Toast.LENGTH_LONG).show();
                else if (toTimeInSec==0)
                    Toast.makeText(AverageActivity.this,getResources().getString(R.string.averages_date_to_miss), Toast.LENGTH_LONG).show();
                else if (toTimeInSec<fromTimeInSec)
                    Toast.makeText(AverageActivity.this,getResources().getString(R.string.averages_invalid), Toast.LENGTH_LONG).show();
                else
                {
                    type1Count=0;
                    type1Value=0;
                    type2Count=0;
                    type2Value=0;
                    type3Count=0;
                    type3Value=0;
                    ArrayList<Item> selectedItems=db.getAllRowsInRange(fromTimeInSec,toTimeInSec);
                    Log.d("Selectd","//////////"+ selectedItems.size());
                    for (int i=0;i<selectedItems.size();i++)
                    {
                        if (selectedItems.get(i).getType()==0)
                        {
                            type1Count++;
                            type1Value+=selectedItems.get(i).getValue();
                        }
                        else if (selectedItems.get(i).getType()==1)
                        {
                            type2Count++;
                            type2Value+=selectedItems.get(i).getValue();
                        }
                        else if (selectedItems.get(i).getType()==2)
                        {
                            type3Count++;
                            type3Value+=selectedItems.get(i).getValue();
                        }
                    }
                    double avg1=0,avg2=0,avg3=0;
                    if (type1Count!=0)
                        avg1=(double)(type1Value/type1Count);
                    if (type2Count!=0)
                        avg2=(double)(type2Value/type2Count);
                    if (type3Count!=0)
                        avg3=(double)(type3Value/type3Count);
                    type1TextView.setText(getString(R.string.type1)+" Average is = [ "+avg1+" ] and count of recorded of this type during given time is [ " +type1Count+" ] .");
                    type2TextView.setText(getString(R.string.type2)+" Average is = [ "+avg2+" ] and count of recorded of this type during given time is [ " +type2Count+" ] .");
                    type3TextView.setText(getString(R.string.type3)+" Average is = [ "+avg3+" ] and count of recorded of this type during given time is [ " +type3Count+" ] .");
                    //255 260 2800
                }
            }
        });
        toEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate= Calendar.getInstance();
                toYear=mcurrentDate.get(Calendar.YEAR);
                toMonth=mcurrentDate.get(Calendar.MONTH);
                toDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog mDatePicker=new DatePickerDialog(AverageActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday)
                    {
                        toDay=selectedday;
                        toMonth=selectedmonth;
                        toYear=selectedyear;
                        toMonth+=1;
                        String dateString=new StringBuilder()
                                .append(toYear).append("-").append(toMonth).append("-").append(toDay).toString();
                        Log.d("dateString",dateString);
                        toEditText.setText(dateString);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1=null;
                        try {
                            date1 = dateFormat.parse(dateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Long timeInMillSec=  date1.getTime();
                        toTimeInSec= (int) (timeInMillSec/1000);
                    }
                },toYear, toMonth, toDay);
                mDatePicker.setTitle(getResources().getString(R.string.averages_date_to));
                mDatePicker.show();
            }
        });
        fromEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate= Calendar.getInstance();
                fromYear=mcurrentDate.get(Calendar.YEAR);
                fromMonth=mcurrentDate.get(Calendar.MONTH);
                fromDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog mDatePicker=new DatePickerDialog(AverageActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday)
                    {
                        fromDay=selectedday;
                        fromMonth=selectedmonth;
                        fromYear=selectedyear;
                        fromMonth+=1;
                        String dateString=new StringBuilder()
                                .append(fromYear).append("-").append(fromMonth).append("-").append(fromDay).toString();
                        Log.d("dateString",dateString);
                        fromEditText.setText(dateString);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1=null;
                        try {
                            date1 = dateFormat.parse(dateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Long timeInMillSec=  date1.getTime();
                        fromTimeInSec= (int) (timeInMillSec/1000);
                    }
                },fromYear, fromMonth, fromDay);
                mDatePicker.setTitle(getResources().getString(R.string.averages_date_From));
                mDatePicker.show();
            }
        });
    }
}
