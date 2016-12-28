package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Rabbia on 11/25/2016.
 */

public class MySpinner extends Spinner {

    String defaultValue;
    ArrayAdapter<String> spinnerArrayAdapter;

    public MySpinner(Context context, String[] options, String defaultValue) {
        super(context);

        this.defaultValue = defaultValue;

        List<String> spinnerList = new ArrayList<String>(Arrays.asList(options));

        if (App.isLanguageRTL()) {
            spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.custom_rtl_spinner, spinnerList);
            setAdapter(spinnerArrayAdapter);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_rtl_spinner);
            setGravity(Gravity.RIGHT);
        } else {
            spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerList);
            setAdapter(spinnerArrayAdapter);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            setGravity(Gravity.LEFT);
        }

        selectDefaultValue();

    }

    public void selectDefaultValue() {

        if (defaultValue == null || defaultValue.equals(""))
            setSelection(0);
        else
            setSelection(spinnerArrayAdapter.getPosition(defaultValue));

    }

    public void selectValue(String value) {
        setSelection(spinnerArrayAdapter.getPosition(value));
    }

    public String getValue() {
        return getSelectedItem().toString();
    }

}
