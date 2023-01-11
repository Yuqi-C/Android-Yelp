package com.yuqiC.hw9;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class ReservationDialog extends Dialog implements View.OnClickListener{

    private TextView tvTitle;
    private EditText edEmail, edDate, edTime;
    private Button mSubmit;
    private Button mCancel;
    private String title;
    private IOnCancelListener cancelListener;
    private IOnSubmitListener submitListener;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setmCancel(IOnCancelListener listener) {
        this.cancelListener = listener;
    }

    public void setmSubmit(IOnSubmitListener listener) {
        this.submitListener = listener;
    }

    public ReservationDialog(@NonNull Context context) {
        super(context);
    }

    public ReservationDialog(@NonNull Context context, int themeId) {
        super(context, themeId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reservation);
        tvTitle = findViewById(R.id.res_title);
        edEmail = findViewById(R.id.res_email);
        edDate = findViewById(R.id.res_date);
        edTime = findViewById(R.id.res_time);
        mSubmit = findViewById(R.id.res_submit);
        mCancel = findViewById(R.id.res_cancel);

        if(!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }

        edTime.setOnClickListener(this);
        edDate.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.res_submit:
                if(submitListener != null){
                    submitListener.onSubmit(this);
                }
                dismiss();

                if(!isEmailValid(edEmail.getText().toString())){
                    Toast myToast = new Toast(view.getContext());
                    LayoutInflater inflater = LayoutInflater.from(view.getContext());
                    View myView = inflater.inflate(R.layout.dialog_toast, null);
                    TextView info = myView.findViewById(R.id.tv_toast);
                    info.setText("InValid Email Address");
                    myToast.setView(myView);
                    myToast.setDuration(Toast.LENGTH_LONG);
                    myToast.show();
                    break;
                }

                String[] reserve_time = edTime.getText().toString().split(":");
                if( Double.valueOf(reserve_time[0])<10 || Double.valueOf(reserve_time[0])>=17){
                    Toast myToast = new Toast(view.getContext());
                    LayoutInflater inflater = LayoutInflater.from(view.getContext());
                    View myView = inflater.inflate(R.layout.dialog_toast, null);
                    TextView info = myView.findViewById(R.id.tv_toast);
                    info.setText("Time should be between 10AM AND 5PM");
                    myToast.setView(myView);
                    myToast.setDuration(Toast.LENGTH_LONG);
                    myToast.show();
                    break;
                }
                SharedPreferences pref = view.getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                Set<String> all_titles;
                if(pref.contains("all_titles")){
                    all_titles = pref.getStringSet("all_titles", null);
                    if(!all_titles.contains(title)){
                        all_titles.add(title);
                    }
                }else{
                    all_titles = new HashSet<>();
                    all_titles.add(title);
                }

                String reservation_info = edDate.getText().toString() + "," + edTime.getText().toString() + "," + edEmail.getText().toString();
                editor.putString(title, reservation_info);
                editor.putStringSet("all_titles", all_titles);
                editor.commit();

                Toast myToast = new Toast(view.getContext());
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                View myView = inflater.inflate(R.layout.dialog_toast, null);
                TextView info = myView.findViewById(R.id.tv_toast);
                info.setText("Reservation Booked");
                myToast.setView(myView);
                myToast.setDuration(Toast.LENGTH_LONG);
                myToast.show();
                dismiss();
                break;
            case R.id.res_cancel:
                if(cancelListener != null){
                    cancelListener.onCancel(this);
                }
                dismiss();
                break;
            case R.id.res_date:
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                edDate.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
                break;
            case R.id.res_time:
                final Calendar cT = Calendar.getInstance();

                int hour = cT.get(Calendar.HOUR_OF_DAY);
                int minute = cT.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                edTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
                break;
        }
    }

    public interface IOnCancelListener {
        void onCancel(ReservationDialog dialog);
    }

    public interface IOnSubmitListener {
        void onSubmit(ReservationDialog dialog);
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
