package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.text.InputFilter;
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

    public TitledEditText(Context context, String title, String ques, String defaultValue, String hint, int length, InputFilter filter, int inputType, int layoutOrientation, Boolean mandatory) {
        super(context);

        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);
        this.mandatory = mandatory;

        LinearLayout hLayout = new LinearLayout(context);
        hLayout.setOrientation(HORIZONTAL);

        if (mandatory) {
            int color = App.getColor(context, R.attr.colorAccent);
            TextView mandatorySign = new TextView(context);
            mandatorySign.setText("*");
            mandatorySign.setTextColor(color);
            hLayout.addView(mandatorySign);
        }

        questionView = new MyTextView(context, ques);
        hLayout.addView(questionView);
        linearLayout.addView(hLayout);

        editText = new MyEditText(context, defaultValue, length, filter, inputType);
        linearLayout.addView(editText);

        if (layoutOrientation == App.HORIZONTAL) {
            LayoutParams layoutParams1 = new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(20, 0, 0, 0);
            editText.setLayoutParams(layoutParams1);
        }

        if (!(hint == null || hint.equals("")))
            editText.setHint(hint);

        addView(linearLayout);
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
}
