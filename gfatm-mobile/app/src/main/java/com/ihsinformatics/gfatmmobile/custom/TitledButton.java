package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.App;

/**
 * Created by Rabbia on 12/1/2016.
 */

public class TitledButton extends LinearLayout {

    MyTextView questionView;
    Button button;
    String defaultValue = "";
    Boolean mandatory = false;
    String tag = "";

    public TitledButton(Context context, String title, String ques, String defaultValue, int layoutOrientation) {
        super(context);
        this.defaultValue = defaultValue;
        if(!App.isTabletDevice(context)){
            layoutOrientation = App.VERTICAL;
        }
        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);

        questionView = new MyTextView(context, ques);
        linearLayout.addView(questionView);

        button = new Button(context);
        setDefaultValue();

        if (layoutOrientation == App.HORIZONTAL) {
            LayoutParams layoutParams1 = new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(20, 0, 0, 0);
            button.setLayoutParams(layoutParams1);
        }

        linearLayout.addView(button);

        addView(linearLayout);
    }

    public TitledButton(Context context, String title, String ques, String defaultValue, int layoutOrientation, Boolean mandatory) {
        super(context);
        this.defaultValue = defaultValue;
        if(!App.isTabletDevice(context)){
            layoutOrientation = App.VERTICAL;
        }
        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);
        this.mandatory = mandatory;

        LinearLayout hLayout = new LinearLayout(context);
        hLayout.setOrientation(HORIZONTAL);

        if (mandatory) {
            TextView mandatorySign = new TextView(context);
            mandatorySign.setText(" *");
            mandatorySign.setTextColor(Color.parseColor("#ff0000"));
            hLayout.addView(mandatorySign);
        }

        questionView = new MyTextView(context, ques);
        hLayout.addView(questionView);

        linearLayout.addView(hLayout);

        button = new Button(context);
        setDefaultValue();

        if (layoutOrientation == App.HORIZONTAL) {
            LayoutParams layoutParams1 = new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(20, 0, 0, 0);
            button.setLayoutParams(layoutParams1);
        }

        linearLayout.addView(button);

        addView(linearLayout);
    }


    public MyTextView getQuestionView() {
        return questionView;
    }

    public Button getButton() {
        return button;
    }

    public String getButtonText() {
        return button.getText().toString();
    }

    public void setDefaultValue() {

        if (defaultValue != null)
            button.setText(defaultValue);
        else
            defaultValue = "";

    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public Boolean getMandatory() {
        return mandatory;
    }
}
