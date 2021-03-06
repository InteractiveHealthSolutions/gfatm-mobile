package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;

/**
 * Created by Rabbia on 11/30/2016.
 */

public class TitledEditText extends LinearLayout {

    MyTextView questionView;
    MyEditText editText;
    Boolean mandatory;
    String tag = "";
    String concept = "";

    @Deprecated
    public TitledEditText(Context context, String title, String ques, String defaultValue, String hint, int length, InputFilter filter, int inputType, int layoutOrientation, Boolean mandatory) {
        super(context);
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

        editText = new MyEditText(context, defaultValue, length, filter, inputType);
        linearLayout.addView(editText);

        if (layoutOrientation == App.HORIZONTAL) {

            LayoutParams layoutParams1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
            hLayout.setLayoutParams(layoutParams1);

            questionView.setGravity(Gravity.CENTER);

            LayoutParams layoutParams2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
            questionView.setLayoutParams(layoutParams2);

            LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            layoutParams.setMargins(20, 0, 0, 0);
            editText.setLayoutParams(layoutParams);

            editText.setGravity(Gravity.BOTTOM);
        }

        if (!(hint == null || hint.equals("")))
            editText.setHint(hint);

        addView(linearLayout);
    }

    public TitledEditText(Context context, String title, String ques, String defaultValue, String hint, int length, InputFilter filter, int inputType, int layoutOrientation, Boolean mandatory, String concept) {
        super(context);

        if (!App.isTabletDevice(context)) {
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

        editText = new MyEditText(context, defaultValue, length, filter, inputType);
        linearLayout.addView(editText);

        if (layoutOrientation == App.HORIZONTAL) {

            LayoutParams layoutParams1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
            hLayout.setLayoutParams(layoutParams1);

            questionView.setGravity(Gravity.CENTER);

            LayoutParams layoutParams2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
            questionView.setLayoutParams(layoutParams2);

            LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            layoutParams.setMargins(20, 0, 0, 0);
            editText.setLayoutParams(layoutParams);

            editText.setGravity(Gravity.BOTTOM);
        }

        if (!(hint == null || hint.equals("")))
            editText.setHint(hint);

        addView(linearLayout);

        this.concept = concept;
    }

    public MyTextView getQuestionView() {
        return questionView;
    }

    public MyEditText getEditText() {
        return editText;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public String getConcept() {
        return concept;
    }
}
