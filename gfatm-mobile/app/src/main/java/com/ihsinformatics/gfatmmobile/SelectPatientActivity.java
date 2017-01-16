package com.ihsinformatics.gfatmmobile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.util.RegexUtil;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SelectPatientActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    Calendar dateOfBirthCalendar;
    DialogFragment dobFragment;

    TextView cancelButton;
    TextView createPatient;
    TextView createButton;

    LinearLayout selectLayout;
    LinearLayout createLayout;

    EditText selectPatientId;
    Button selectPatientScanButton;
    Button searchPatient;

    EditText firstName;
    EditText lastName;
    RadioGroup gender;
    RadioButton male;
    RadioButton female;
    EditText dob;
    EditText age;
    EditText createPatientId;
    Button createPatientScanButton;
    EditText externalId;

    ImageView selectButton;
    private ServerService serverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_patient);
        serverService = new ServerService(getApplicationContext());

        cancelButton = (TextView) findViewById(R.id.cancelButton);
        createButton = (TextView) findViewById(R.id.createPatientButton);
        createPatient = (TextView) findViewById(R.id.createPatient);
        selectLayout = (LinearLayout) findViewById(R.id.selectLayout);
        createLayout = (LinearLayout) findViewById(R.id.createLayout);

        selectPatientId = (EditText) findViewById(R.id.selectPatientId);
        selectPatientScanButton = (Button) findViewById(R.id.selectBarcodeScan);
        searchPatient = (Button) findViewById(R.id.searchPatientButton);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        gender = (RadioGroup) findViewById(R.id.gender);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        createPatientId = (EditText) findViewById(R.id.createPatientId);
        createPatientScanButton = (Button) findViewById(R.id.createBarcodeScan);
        externalId = (EditText) findViewById(R.id.externalId);
        dob = (EditText) findViewById(R.id.dob);
        dob.setKeyListener(null);

        dateOfBirthCalendar = Calendar.getInstance();
        dobFragment = new SelectDateFragment();

        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    dobFragment.show(getFragmentManager(), "DatePicker");
                }
            }
        });

        age = (EditText) findViewById(R.id.age);

        age.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                dob.setError(null);
                if(s.length() != 0){
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.YEAR, -Integer.parseInt(s.toString()));

                    dateOfBirthCalendar.set(cal.get(Calendar.YEAR), dateOfBirthCalendar.get(Calendar.MONTH), dateOfBirthCalendar.get(Calendar.DAY_OF_MONTH));
                    dob.setText(DateFormat.format("dd-MMM-yyyy", dateOfBirthCalendar).toString());

                }
                else{
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.YEAR, 0);

                    dateOfBirthCalendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    dob.setText("");
                }
            }
        });

        selectButton = (ImageView) findViewById(R.id.select);
        /*int color = App.getColor(this, R.attr.colorAccent);
        DrawableCompat.setTint(selectButton.getDrawable(), color);*/
        selectButton.setOnTouchListener(this);

        dob.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        createPatient.setOnClickListener(this);
        createButton.setOnClickListener(this);
        createPatientScanButton.setOnClickListener(this);
        selectPatientScanButton.setOnClickListener(this);
        searchPatient.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                //overlay is black with transparency of 0x77 (119)
                view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                view.invalidate();

                Boolean error = false;

                if (App.get(selectPatientId).isEmpty()) {
                    selectPatientId.setError(getString(R.string.empty_field));
                    selectPatientId.requestFocus();
                    error = true;
                } else if (!RegexUtil.isValidId(App.get(selectPatientId))) {
                    selectPatientId.setError(getString(R.string.invalid_id));
                    selectPatientId.requestFocus();
                    error = true;
                }

                if (!error) getPatient();

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                ImageView view = (ImageView) v;
                //clear the overlay
                view.getDrawable().clearColorFilter();
                view.invalidate();
                break;
            }
        }
        return true;
    }

    public void getPatient() {

    }

    @Override
    public void onClick(View v) {

        if (v == cancelButton) {

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            super.onBackPressed();

        } else if (v == searchPatient) {

            Boolean error = false;

            Intent intent = new Intent();
            intent.putExtra("key", "SEARCH");
            setResult(RESULT_OK, intent);
            finish();


        } else if (v == createPatient) {
            selectLayout.setVisibility(View.GONE);
            createLayout.setVisibility(View.VISIBLE);
            createButton.setVisibility(View.VISIBLE);

            setTitle(getResources().getString(R.string.title_create_new_patient));
        } else if (v == createButton) {
            if(createPatientValidate()){

            }
        }
        else if ( v == dob){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            dobFragment.show(getFragmentManager(), "DatePicker");
        }
        else if ( v == createPatientScanButton){
            try {
                Intent intent = new Intent(Barcode.BARCODE_INTENT);
                if (App.isCallable(SelectPatientActivity.this, intent)) {
                    intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
                    startActivityForResult(intent, Barcode.BARCODE_RESULT);
                } else {
                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            } catch (ActivityNotFoundException e) {
                //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
                alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                //DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_error));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
        else if( v == selectPatientScanButton){

            try {
                Intent intent = new Intent(Barcode.BARCODE_INTENT);
                if (App.isCallable(SelectPatientActivity.this, intent)) {
                    intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
                    startActivityForResult(intent, Barcode.BARCODE_RESULT_2);
                } else {
                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            } catch (ActivityNotFoundException e) {
                //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
                alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                //DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_error));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve barcode scan results
        if (requestCode == Barcode.BARCODE_RESULT) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(Barcode.SCAN_RESULT);
                // Check for valid Id
                if (RegexUtil.isValidId(str)) {
                    createPatientId.setText(str);
                    createPatientId.requestFocus();
                } else {

                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                    createPatientId.setText("");
                    createPatientId.requestFocus();

                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.invalid_scanned_id));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            } else if (resultCode == RESULT_CANCELED) {

                int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                createPatientId.setText("");
                createPatientId.requestFocus();

                /*final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this).create();
                alertDialog.setMessage(getString(R.string.warning_before_clear));
                Drawable clearIcon = getResources().getDrawable(R.drawable.ic_clear);
                DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_clear));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();*/

            }
            // Set the locale again, since the Barcode app restores system's
            // locale because of orientation
            Locale.setDefault(App.getCurrentLocale());
            Configuration config = new Configuration();
            config.locale = App.getCurrentLocale();
            SelectPatientActivity.this.getResources().updateConfiguration(config,null);
        }
        else if(requestCode == Barcode.BARCODE_RESULT_2){
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(Barcode.SCAN_RESULT);
                // Check for valid Id
                if (RegexUtil.isValidId(str)) {
                    selectPatientId.setText(str);
                    selectPatientId.requestFocus();
                } else {

                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                    selectPatientId.setText("");
                    selectPatientId.requestFocus();

                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.invalid_scanned_id));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            } else if (resultCode == RESULT_CANCELED) {

                int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                selectPatientId.setText("");
                selectPatientId.requestFocus();

                /*final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this).create();
                alertDialog.setMessage(getString(R.string.warning_before_clear));
                Drawable clearIcon = getResources().getDrawable(R.drawable.ic_clear);
                DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_clear));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();*/

            }
            // Set the locale again, since the Barcode app restores system's
            // locale because of orientation
            Locale.setDefault(App.getCurrentLocale());
            Configuration config = new Configuration();
            config.locale = App.getCurrentLocale();
            SelectPatientActivity.this.getResources().updateConfiguration(config,null);
        }
    }

    @Override
    public void onBackPressed() {

        if (createLayout.getVisibility() == View.VISIBLE) {

            selectLayout.setVisibility(View.VISIBLE);
            createLayout.setVisibility(View.GONE);
            createButton.setVisibility(View.GONE);

            firstName.setText("");
            lastName.setText("");
            male.setChecked(true);
            dob.setText("");
            age.setText("");
            createPatientId.setText("");
            externalId.setText("");

            setTitle(getResources().getString(R.string.title_select_patient));

        } else
            super.onBackPressed();
    }

    public boolean createPatientValidate(){
        boolean error = false;

        if (App.get(createPatientId).isEmpty()) {
            createPatientId.setError(getString(R.string.empty_field));
            createPatientId.requestFocus();
            error = true;
        }else if(!RegexUtil.isValidId(App.get(createPatientId))){
            createPatientId.setError(getString(R.string.invalid_id));
            createPatientId.requestFocus();
            error = true;
        }
        if (App.get(dob).isEmpty()) {
            dob.setError(getString(R.string.empty_field));
            error = true;
        }
        if (App.get(lastName).isEmpty()) {
            lastName.setError(getString(R.string.empty_field));
            lastName.requestFocus();
            error = true;
        }
        if (App.get(firstName).isEmpty()) {
            firstName.setError(getString(R.string.empty_field));
            firstName.requestFocus();
            error = true;
        }

        if (error)
            return false;

        return true;
    }

    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int yy = dateOfBirthCalendar.get(Calendar.YEAR);
            int mm = dateOfBirthCalendar.get(Calendar.MONTH);
            int dd = dateOfBirthCalendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            dateOfBirthCalendar.set(yy, mm, dd);
            age.setText(String.valueOf(App.getDiffYears(dateOfBirthCalendar.getTime(), new Date())));

        }
    }

}
