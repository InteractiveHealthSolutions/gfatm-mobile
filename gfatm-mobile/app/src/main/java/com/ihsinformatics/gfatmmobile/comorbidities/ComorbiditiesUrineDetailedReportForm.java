package com.ihsinformatics.gfatmmobile.comorbidities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 12-Jan-17.
 */

public class ComorbiditiesUrineDetailedReportForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener, View.OnTouchListener {

    public static final int THIRD_DATE_DIALOG_ID = 3;
    // Extra Views for date ...
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;
    protected Calendar fourthDateCalendar;
    Context context;
    // Views...
    TitledButton formDate;
    TitledRadioGroup formType;
    TitledEditText urineDRTestID;
    //Views for Test Order
    MyTextView testOrderUrineDR;
    TitledSpinner urineDRMonthOfVisit;
    TitledButton urineDRTestOrderDate;
    ImageView testIdView;
    //Views for Test Result
    MyTextView testResultUrineDR;
    TitledButton urineDRTestResultDate;
    TitledEditText urineDRQuantity;
    //TitledEditText urineDRColor;
    TitledSpinner urineDRColor;
    TitledEditText urineDRSpecificGravity;
    TitledEditText urineDRPH;
    TitledEditText urineDRAlbumin;
    TitledEditText urineDRSugar;
    TitledEditText urineDRKetones;
    TitledEditText urineDRBilirubin;
    //TitledEditText urineDRBlood;
    TitledRadioGroup urineDRBlood;
    //TitledEditText urineDRNitrite;
    TitledRadioGroup urineDRNitrite;
    TitledEditText urineDRUrobilinogen;
    /*TitledEditText urineDRRedCells;
    TitledEditText urineDRPusCells;
    TitledEditText urineDREpithelialCells;
    TitledEditText urineDRHyalineCasts;
    TitledEditText urineDRCrystals;*/
    TitledRadioGroup urineDRRedCells;
    TitledRadioGroup urineDRPusCells;
    TitledRadioGroup urineDREpithelialCells;
    TitledRadioGroup urineDRHyalineCasts;
    TitledRadioGroup urineDRCrystals;
    TitledButton nextUrineDRTestDate;

    ScrollView scrollView;

    /**
     * CHANGE PAGE_COUNT and FORM_NAME Variable only...
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 1;
        FORM_NAME = Forms.COMORBIDITIES_URINE_DR_FORM;
        FORM = Forms.comorbidities_urineDRForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesUrineDetailedReportForm.MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(PAGE_COUNT - 1);
        formName.setText(FORM_NAME);

        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new ComorbiditiesUrineDetailedReportForm.SelectDateFragment();

        fourthDateCalendar = Calendar.getInstance();

        initViews();

        groups = new ArrayList<ViewGroup>();

        if (App.isLanguageRTL()) {
            for (int i = PAGE_COUNT - 1; i >= 0; i--) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        } else {
            for (int i = 0; i < PAGE_COUNT; i++) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }

        gotoFirstPage();

        return mainContent;
    }

    /**
     * Initializes all views and ArrayList and Views Array
     */
    public void initViews() {

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_testorder_testresult_form_type), getResources().getStringArray(R.array.comorbidities_testorder_testresult_form_type_options), "", App.HORIZONTAL, App.VERTICAL);
        testOrderUrineDR = new MyTextView(context, getResources().getString(R.string.comorbidities_urinedr_test_order));
        testOrderUrineDR.setTypeface(null, Typeface.BOLD);
        urineDRMonthOfVisit = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_urinedr_month_of_treatment), getResources().getStringArray(R.array.comorbidities_followup_month), "1", App.HORIZONTAL);
        urineDRTestOrderDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_hba1cdate_test_order), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        //urineDRTestID = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_hhba1c_testid), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        LinearLayout linearLayout = new LinearLayout(context);
        urineDRTestID = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_hhba1c_testid), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.9f
        );
        urineDRTestID.setLayoutParams(param);
        linearLayout.addView(urineDRTestID);
        testIdView = new ImageView(context);
        testIdView.setImageResource(R.drawable.ic_checked);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.1f
        );
        testIdView.setLayoutParams(param1);
        testIdView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        testIdView.setPadding(0, 5, 0, 0);

        linearLayout.addView(testIdView);

        //second page views...
        testResultUrineDR = new MyTextView(context, getResources().getString(R.string.comorbidities_urinedr_test_result));
        testResultUrineDR.setTypeface(null, Typeface.BOLD);
        urineDRTestResultDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_hba1c_resultdate), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        urineDRQuantity = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_quantity), "", getResources().getString(R.string.comorbidities_vitals_bmi_range), 6, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        urineDRColor = new TitledSpinner(context, null, getResources().getString(R.string.comorbidities_urinedr_color), getResources().getStringArray(R.array.comorbidities_urinedr_test_color_options), getResources().getString(R.string.comorbidities_urinedr_test_color_light_yellow), App.HORIZONTAL);
        //urineDRColor = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_color), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        urineDRSpecificGravity = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_spgravity), "", getResources().getString(R.string.comorbidities_urinedr_spgravity_ph_range), 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        urineDRPH = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_ph), "", getResources().getString(R.string.comorbidities_urinedr_spgravity_ph_range), 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        urineDRAlbumin = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_albumin), "", getResources().getString(R.string.comorbidities_vitals_waist_hip_whr_range), 6, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        urineDRSugar = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_sugar), "", getResources().getString(R.string.comorbidities_urinedr_sugar_ketones_uro_range), 7, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        urineDRKetones = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_ketones), "", getResources().getString(R.string.comorbidities_urinedr_sugar_ketones_uro_range), 7, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        urineDRBilirubin = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_bilirubin), "", getResources().getString(R.string.comorbidities_vitals_bmi_range), 6, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        //urineDRBlood = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_blood), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        urineDRBlood = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_urinedr_blood), getResources().getStringArray(R.array.comorbidities_urinedr_test_some_options), getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg), App.HORIZONTAL, App.VERTICAL);
        //urineDRNitrite = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_nitrite), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        urineDRNitrite = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_urinedr_nitrite), getResources().getStringArray(R.array.comorbidities_urinedr_test_some_options), getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg), App.HORIZONTAL, App.VERTICAL);
        urineDRUrobilinogen = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_urobilinogen), "", getResources().getString(R.string.comorbidities_urinedr_sugar_ketones_uro_range), 7, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        //urineDRRedCells = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_red_cells), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        urineDRRedCells = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_urinedr_red_cells), getResources().getStringArray(R.array.comorbidities_urinedr_test_some_options), getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg), App.HORIZONTAL, App.VERTICAL);
        //urineDRPusCells = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_pus_cells), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        urineDRPusCells = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_urinedr_pus_cells), getResources().getStringArray(R.array.comorbidities_urinedr_test_some_options), getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg), App.HORIZONTAL, App.VERTICAL);
        //urineDREpithelialCells = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_epithelial_cells), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        urineDREpithelialCells = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_urinedr_epithelial_cells), getResources().getStringArray(R.array.comorbidities_urinedr_test_some_options), getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg), App.HORIZONTAL, App.VERTICAL);
        //urineDRHyalineCasts = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_hyaline_casts), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        urineDRHyalineCasts = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_urinedr_hyaline_casts), getResources().getStringArray(R.array.comorbidities_urinedr_test_some_options), getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg), App.HORIZONTAL, App.VERTICAL);
        //urineDRCrystals = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_crystals), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        urineDRCrystals = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_urinedr_crystals), getResources().getStringArray(R.array.comorbidities_urinedr_test_some_options), getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg), App.HORIZONTAL, App.VERTICAL);
        nextUrineDRTestDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_urinedr_nexttestdate), DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString(), App.HORIZONTAL);
        //nextUrineDRTestDate.setVisibility(View.GONE);
        goneVisibility();


        // Used for reset fields...
        views = new View[]{formDate.getButton(), urineDRTestID.getEditText(), formType.getRadioGroup(), testOrderUrineDR, urineDRMonthOfVisit.getSpinner(), urineDRTestOrderDate.getButton(), testResultUrineDR, urineDRTestResultDate.getButton(), urineDRQuantity.getEditText(),
                urineDRColor.getSpinner(), urineDRSpecificGravity.getEditText(), urineDRPH.getEditText(), urineDRAlbumin.getEditText(), urineDRSugar.getEditText(), urineDRKetones.getEditText(), urineDRBilirubin.getEditText(), urineDRBlood.getRadioGroup(), urineDRNitrite.getRadioGroup(), urineDRUrobilinogen.getEditText(),
                urineDRRedCells.getRadioGroup(), urineDRPusCells.getRadioGroup(), urineDREpithelialCells.getRadioGroup(), urineDRHyalineCasts.getRadioGroup(), urineDRCrystals.getRadioGroup(), nextUrineDRTestDate.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, formType, linearLayout, testOrderUrineDR, urineDRMonthOfVisit, urineDRTestOrderDate, testResultUrineDR, urineDRTestResultDate, urineDRQuantity,
                        urineDRColor, urineDRSpecificGravity, urineDRPH, urineDRAlbumin, urineDRSugar, urineDRKetones, urineDRBilirubin, urineDRBlood, urineDRNitrite, urineDRUrobilinogen,
                        urineDRRedCells, urineDRPusCells, urineDREpithelialCells, urineDRHyalineCasts, urineDRCrystals, nextUrineDRTestDate}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);

        urineDRTestOrderDate.getButton().setOnClickListener(this);
        urineDRTestResultDate.getButton().setOnClickListener(this);
        nextUrineDRTestDate.getButton().setOnClickListener(this);

        urineDRQuantity.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (urineDRQuantity.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(urineDRQuantity.getEditText().getText().toString());
                        if (num < 0 || num > 50) {
                            urineDRQuantity.getEditText().setError(getString(R.string.comorbidities_vitals_bmi_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        urineDRSpecificGravity.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (urineDRSpecificGravity.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(urineDRSpecificGravity.getEditText().getText().toString());
                        if (num < 0 || num > 10) {
                            urineDRSpecificGravity.getEditText().setError(getString(R.string.comorbidities_urinedr_spgravity_ph_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        urineDRPH.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (urineDRPH.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(urineDRPH.getEditText().getText().toString());
                        if (num < 0 || num > 10) {
                            urineDRPH.getEditText().setError(getString(R.string.comorbidities_urinedr_spgravity_ph_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        urineDRAlbumin.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (urineDRAlbumin.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(urineDRAlbumin.getEditText().getText().toString());
                        if (num < 0 || num > 100) {
                            urineDRAlbumin.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        urineDRSugar.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (urineDRSugar.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(urineDRSugar.getEditText().getText().toString());
                        if (num < 0 || num > 400) {
                            urineDRSugar.getEditText().setError(getString(R.string.comorbidities_urinedr_sugar_ketones_uro_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        urineDRKetones.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (urineDRKetones.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(urineDRKetones.getEditText().getText().toString());
                        if (num < 0 || num > 400) {
                            urineDRKetones.getEditText().setError(getString(R.string.comorbidities_urinedr_sugar_ketones_uro_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        urineDRBilirubin.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (urineDRBilirubin.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(urineDRBilirubin.getEditText().getText().toString());
                        if (num < 0 || num > 50) {
                            urineDRBilirubin.getEditText().setError(getString(R.string.comorbidities_vitals_bmi_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        urineDRUrobilinogen.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (urineDRUrobilinogen.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(urineDRUrobilinogen.getEditText().getText().toString());
                        if (num < 0 || num > 400) {
                            urineDRUrobilinogen.getEditText().setError(getString(R.string.comorbidities_urinedr_sugar_ketones_uro_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        urineDRTestID.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (urineDRTestID.getEditText().getText().length() > 0) {
                        testIdView.setVisibility(View.VISIBLE);
                        testIdView.setImageResource(R.drawable.ic_checked);
                    } else {
                        testIdView.setVisibility(View.INVISIBLE);
                    }
                    goneVisibility();
                    submitButton.setEnabled(false);

                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        testIdView.setOnTouchListener(this);
        resetViews();
        /*urineDRTestID.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (urineDRTestID.getEditText().getText().length() > 0) {
                        if (urineDRTestID.getEditText().getText().length() < 11) {
                            urineDRTestID.getEditText().setError(getString(R.string.comorbidities_blood_sugar_testid_format_error));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });*/
    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        urineDRTestOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        urineDRTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        //fourthDateCalendar = thirdDateCalendar;
        fourthDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
        fourthDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
        fourthDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
        fourthDateCalendar.add(Calendar.MONTH, 2);
        fourthDateCalendar.add(Calendar.DAY_OF_MONTH, 20);
        nextUrineDRTestDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString());
    }

    @Override
    public boolean validate() {

        Boolean error = false;
        View view = null;

        /*if (urineDRCrystals.getVisibility() == View.VISIBLE && App.get(urineDRCrystals).isEmpty()) {
            gotoFirstPage();
            urineDRCrystals.getEditText().setError(getString(R.string.empty_field));
            urineDRCrystals.getEditText().requestFocus();
            error = true;
        }

        if (urineDRHyalineCasts.getVisibility() == View.VISIBLE && App.get(urineDRHyalineCasts).isEmpty()) {
            gotoFirstPage();
            urineDRHyalineCasts.getEditText().setError(getString(R.string.empty_field));
            urineDRHyalineCasts.getEditText().requestFocus();
            error = true;
        }

        if (urineDREpithelialCells.getVisibility() == View.VISIBLE && App.get(urineDREpithelialCells).isEmpty()) {
            gotoFirstPage();
            urineDREpithelialCells.getEditText().setError(getString(R.string.empty_field));
            urineDREpithelialCells.getEditText().requestFocus();
            error = true;
        }

        if (urineDRPusCells.getVisibility() == View.VISIBLE && App.get(urineDRPusCells).isEmpty()) {
            gotoFirstPage();
            urineDRPusCells.getEditText().setError(getString(R.string.empty_field));
            urineDRPusCells.getEditText().requestFocus();
            error = true;
        }

        if (urineDRRedCells.getVisibility() == View.VISIBLE && App.get(urineDRRedCells).isEmpty()) {
            gotoFirstPage();
            urineDRRedCells.getEditText().setError(getString(R.string.empty_field));
            urineDRRedCells.getEditText().requestFocus();
            error = true;
        }*/

        if (urineDRUrobilinogen.getVisibility() == View.VISIBLE && App.get(urineDRUrobilinogen).isEmpty()) {
            gotoLastPage();
            urineDRUrobilinogen.getEditText().setError(getString(R.string.empty_field));
            urineDRUrobilinogen.getEditText().requestFocus();
            error = true;
        } else if (urineDRUrobilinogen.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(urineDRUrobilinogen), true)) {
            gotoLastPage();
            urineDRUrobilinogen.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            urineDRUrobilinogen.getEditText().requestFocus();
            error = true;
        } else if (urineDRUrobilinogen.getVisibility() == View.VISIBLE && !App.get(urineDRUrobilinogen).isEmpty() && Double.parseDouble(App.get(urineDRUrobilinogen)) > 400) {
            gotoLastPage();
            urineDRUrobilinogen.getEditText().setError(getString(R.string.comorbidities_urinedr_sugar_ketones_uro_limit));
            urineDRUrobilinogen.getEditText().requestFocus();
            error = true;
        }

        /*if (urineDRNitrite.getVisibility() == View.VISIBLE && App.get(urineDRNitrite).isEmpty()) {
            gotoFirstPage();
            urineDRNitrite.getEditText().setError(getString(R.string.empty_field));
            urineDRNitrite.getEditText().requestFocus();
            error = true;
        }

        if (urineDRBlood.getVisibility() == View.VISIBLE && App.get(urineDRBlood).isEmpty()) {
            gotoFirstPage();
            urineDRBlood.getEditText().setError(getString(R.string.empty_field));
            urineDRBlood.getEditText().requestFocus();
            error = true;
        }*/

        if (urineDRBilirubin.getVisibility() == View.VISIBLE && App.get(urineDRBilirubin).isEmpty()) {
            gotoLastPage();
            urineDRBilirubin.getEditText().setError(getString(R.string.empty_field));
            urineDRBilirubin.getEditText().requestFocus();
            error = true;
        } else if (urineDRBilirubin.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(urineDRBilirubin), true)) {
            gotoLastPage();
            urineDRBilirubin.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            urineDRBilirubin.getEditText().requestFocus();
            error = true;
        } else if (urineDRBilirubin.getVisibility() == View.VISIBLE && !App.get(urineDRBilirubin).isEmpty() && Double.parseDouble(App.get(urineDRBilirubin)) > 50) {
            gotoLastPage();
            urineDRBilirubin.getEditText().setError(getString(R.string.comorbidities_vitals_bmi_limit));
            urineDRBilirubin.getEditText().requestFocus();
            error = true;
        }

        if (urineDRKetones.getVisibility() == View.VISIBLE && App.get(urineDRKetones).isEmpty()) {
            gotoLastPage();
            urineDRKetones.getEditText().setError(getString(R.string.empty_field));
            urineDRKetones.getEditText().requestFocus();
            error = true;
        } else if (urineDRKetones.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(urineDRKetones), true)) {
            gotoLastPage();
            urineDRKetones.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            urineDRKetones.getEditText().requestFocus();
            error = true;
        } else if (urineDRKetones.getVisibility() == View.VISIBLE && !App.get(urineDRKetones).isEmpty() && Double.parseDouble(App.get(urineDRKetones)) > 400) {
            gotoLastPage();
            urineDRKetones.getEditText().setError(getString(R.string.comorbidities_urinedr_sugar_ketones_uro_limit));
            urineDRKetones.getEditText().requestFocus();
            error = true;
        }

        if (urineDRSugar.getVisibility() == View.VISIBLE && App.get(urineDRSugar).isEmpty()) {
            gotoLastPage();
            urineDRSugar.getEditText().setError(getString(R.string.empty_field));
            urineDRSugar.getEditText().requestFocus();
            error = true;
        } else if (urineDRSugar.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(urineDRSugar), true)) {
            gotoLastPage();
            urineDRSugar.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            urineDRSugar.getEditText().requestFocus();
            error = true;
        } else if (urineDRSugar.getVisibility() == View.VISIBLE && !App.get(urineDRSugar).isEmpty() && Double.parseDouble(App.get(urineDRSugar)) > 400) {
            gotoLastPage();
            urineDRSugar.getEditText().setError(getString(R.string.comorbidities_urinedr_sugar_ketones_uro_limit));
            urineDRSugar.getEditText().requestFocus();
            error = true;
        }

        if (urineDRAlbumin.getVisibility() == View.VISIBLE && App.get(urineDRAlbumin).isEmpty()) {
            gotoLastPage();
            urineDRAlbumin.getEditText().setError(getString(R.string.empty_field));
            urineDRAlbumin.getEditText().requestFocus();
            error = true;
        } else if (urineDRAlbumin.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(urineDRAlbumin), true)) {
            gotoLastPage();
            urineDRAlbumin.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            urineDRAlbumin.getEditText().requestFocus();
            error = true;
        } else if (urineDRAlbumin.getVisibility() == View.VISIBLE && !App.get(urineDRAlbumin).isEmpty() && Double.parseDouble(App.get(urineDRAlbumin)) > 100) {
            gotoLastPage();
            urineDRAlbumin.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
            urineDRAlbumin.getEditText().requestFocus();
            error = true;
        }

        if (urineDRPH.getVisibility() == View.VISIBLE && App.get(urineDRPH).isEmpty()) {
            gotoLastPage();
            urineDRPH.getEditText().setError(getString(R.string.empty_field));
            urineDRPH.getEditText().requestFocus();
            error = true;
        } else if (urineDRPH.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(urineDRPH), true)) {
            gotoLastPage();
            urineDRPH.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            urineDRPH.getEditText().requestFocus();
            error = true;
        } else if (urineDRPH.getVisibility() == View.VISIBLE && !App.get(urineDRAlbumin).isEmpty() && Double.parseDouble(App.get(urineDRPH)) > 10) {
            gotoLastPage();
            urineDRPH.getEditText().setError(getString(R.string.comorbidities_urinedr_spgravity_ph_limit));
            urineDRPH.getEditText().requestFocus();
            error = true;
        }

        if (urineDRSpecificGravity.getVisibility() == View.VISIBLE && App.get(urineDRSpecificGravity).isEmpty()) {
            gotoLastPage();
            urineDRSpecificGravity.getEditText().setError(getString(R.string.empty_field));
            urineDRSpecificGravity.getEditText().requestFocus();
            error = true;
        } else if (urineDRSpecificGravity.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(urineDRSpecificGravity), true)) {
            gotoLastPage();
            urineDRSpecificGravity.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            urineDRSpecificGravity.getEditText().requestFocus();
            error = true;
        } else if (urineDRSpecificGravity.getVisibility() == View.VISIBLE && !App.get(urineDRSpecificGravity).isEmpty() && Double.parseDouble(App.get(urineDRSpecificGravity)) > 10) {
            gotoLastPage();
            urineDRSpecificGravity.getEditText().setError(getString(R.string.comorbidities_urinedr_spgravity_ph_limit));
            urineDRSpecificGravity.getEditText().requestFocus();
            error = true;
        }

        /*if (urineDRColor.getVisibility() == View.VISIBLE && App.get(urineDRColor).isEmpty()) {
            gotoFirstPage();
            urineDRColor.getEditText().setError(getString(R.string.empty_field));
            urineDRColor.getEditText().requestFocus();
            error = true;
        }*/

        if (urineDRQuantity.getVisibility() == View.VISIBLE && App.get(urineDRQuantity).isEmpty()) {
            gotoLastPage();
            urineDRQuantity.getEditText().setError(getString(R.string.empty_field));
            urineDRQuantity.getEditText().requestFocus();
            error = true;
        } else if (urineDRQuantity.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(urineDRQuantity), true)) {
            gotoLastPage();
            urineDRQuantity.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            urineDRQuantity.getEditText().requestFocus();
            error = true;
        } else if (urineDRQuantity.getVisibility() == View.VISIBLE && !App.get(urineDRQuantity).isEmpty() && Double.parseDouble(App.get(urineDRQuantity)) > 50) {
            gotoLastPage();
            urineDRQuantity.getEditText().setError(getString(R.string.comorbidities_vitals_bmi_limit));
            urineDRQuantity.getEditText().requestFocus();
            error = true;
        }

        Boolean flag = false;
        if (!App.get(formType).equalsIgnoreCase("")) {
            flag = true;
        }
        if (!flag) {
            formType.getQuestionView().setError(getString(R.string.empty_field));
            formType.getQuestionView().requestFocus();
            view = formType;
            error = true;
        }

        if (App.get(urineDRTestID).isEmpty()) {
            gotoFirstPage();
            urineDRTestID.getEditText().setError(getString(R.string.empty_field));
            urineDRTestID.getEditText().requestFocus();
            error = true;
        } /*else if (!App.get(urineDRTestID).isEmpty() && App.get(urineDRTestID).length() < 11) {
            gotoFirstPage();
            urineDRTestID.getEditText().setError(getString(R.string.comorbidities_blood_sugar_testid_format_error));
            urineDRTestID.getEditText().requestFocus();
            error = true;
        }*/

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            DrawableCompat.setTint(clearIcon, color);
            alertDialog.setIcon(clearIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_error));
            final View finalView = view;
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            scrollView.post(new Runnable() {
                                public void run() {
                                    if (finalView != null) {
                                        scrollView.scrollTo(0, finalView.getTop());
                                        urineDRTestID.clearFocus();
                                        urineDRQuantity.clearFocus();
                                        urineDRSpecificGravity.clearFocus();
                                        urineDRPH.clearFocus();
                                        urineDRAlbumin.clearFocus();
                                        urineDRSugar.clearFocus();
                                        urineDRKetones.clearFocus();
                                        urineDRBilirubin.clearFocus();
                                        urineDRBlood.clearFocus();
                                        urineDRNitrite.clearFocus();
                                        urineDRUrobilinogen.clearFocus();
                                        urineDRRedCells.clearFocus();
                                        urineDRPusCells.clearFocus();
                                        urineDREpithelialCells.clearFocus();
                                        urineDRHyalineCasts.clearFocus();
                                        urineDRCrystals.clearFocus();
                                    }
                                }
                            });
                            try {
                                InputMethodManager imm = (InputMethodManager) mainContent.getContext().getSystemService(mainContent.getContext().INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            return false;
        }

        return true;
    }

    @Override
    public boolean submit() {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
            }
            bundle.putBoolean("save", false);
        }

        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
        //observations.add(new String[]{"FOLLOW-UP MONTH", App.get(urineDRMonthOfVisit)});
        //observations.add(new String[]{"DATE TEST ORDERED", App.getSqlDateTime(secondDateCalendar)});
        observations.add(new String[]{"TEST ID", App.get(urineDRTestID)});

        if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
            observations.add(new String[]{"FOLLOW-UP MONTH", App.get(urineDRMonthOfVisit)});
            observations.add(new String[]{"DATE TEST ORDERED", App.getSqlDateTime(secondDateCalendar)});
        } else if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testresult))) {
            observations.add(new String[]{"TEST RESULT DATE", App.getSqlDateTime(thirdDateCalendar)});
            final String ownerString = App.get(urineDRColor).equals(getResources().getString(R.string.comorbidities_urinedr_test_color_options_colourless)) ? "COLOURLESS" :
                    (App.get(urineDRColor).equals(getResources().getString(R.string.comorbidities_urinedr_test_color_options_red)) ? "RED COLOR" :
                            (App.get(urineDRColor).equals(getResources().getString(R.string.comorbidities_urinedr_test_color_light_yellow)) ? "LIGHT YELLOW COLOUR" :
                                    (App.get(urineDRColor).equals(getResources().getString(R.string.comorbidities_urinedr_test_color_options_yellow_green)) ? "YELLOW-GREEN COLOUR" :
                                            (App.get(urineDRColor).equals(getResources().getString(R.string.comorbidities_urinedr_test_color_options_dark_yellow)) ? "DARK YELLOW COLOUR" :
                                                    (App.get(urineDRColor).equals(getResources().getString(R.string.comorbidities_urinedr_test_color_options_orange)) ? "ORANGE COLOR" :
                                                            (App.get(urineDRColor).equals(getResources().getString(R.string.comorbidities_urinedr_test_color_options_brown)) ? "BROWN COLOR" :
                                                                    (App.get(urineDRColor).equals(getResources().getString(R.string.comorbidities_urinedr_test_color_options_white)) ? "WHITE COLOR" : "GREY COLOR")))))));
            observations.add(new String[]{"URINE COLOR", ownerString});
            observations.add(new String[]{"URINE SPECIFIC GRAVITY", App.get(urineDRSpecificGravity)});
            observations.add(new String[]{"URINE PH", App.get(urineDRPH)});
            observations.add(new String[]{"URINARY ALBUMIN", App.get(urineDRAlbumin)});
            observations.add(new String[]{"URINE GLUCOSE", App.get(urineDRSugar)});
            observations.add(new String[]{"URINE KETONES, QUANTITATIVE", App.get(urineDRKetones)});
            observations.add(new String[]{"URINE BILIRUBIN", App.get(urineDRBilirubin)});
            observations.add(new String[]{"HEMATURIA", App.get(urineDRBlood).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) ? "NEGATIVE" :
                    (App.get(urineDRBlood).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) ? "POSITIVE" : "INDETERMINATE")});
            observations.add(new String[]{"URINE NITRITE TEST", App.get(urineDRNitrite).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) ? "NEGATIVE" :
                    (App.get(urineDRNitrite).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) ? "POSITIVE" : "INDETERMINATE")});
            observations.add(new String[]{"UROBILINOGEN IN URINE, QUNATITATIVE", App.get(urineDRUrobilinogen)});
            observations.add(new String[]{"ERYTHROCYTES PRESENCE IN URINE SEDIMENT BY LIGHT MICROSCOPY TEST", App.get(urineDRRedCells).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) ? "NEGATIVE" :
                    (App.get(urineDRRedCells).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) ? "POSITIVE" : "INDETERMINATE")});
            observations.add(new String[]{"URINE PUS CELLS", App.get(urineDRPusCells).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) ? "NEGATIVE" :
                    (App.get(urineDRPusCells).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) ? "POSITIVE" : "INDETERMINATE")});
            observations.add(new String[]{"EPITHELIAL CELLS", App.get(urineDREpithelialCells).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) ? "NEGATIVE" :
                    (App.get(urineDREpithelialCells).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) ? "POSITIVE" : "INDETERMINATE")});
            observations.add(new String[]{"URINARY CAST, HYALINE", App.get(urineDRHyalineCasts).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) ? "NEGATIVE" :
                    (App.get(urineDRHyalineCasts).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) ? "POSITIVE" : "INDETERMINATE")});
            observations.add(new String[]{"CRYSTALS IN URINE", App.get(urineDRCrystals).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) ? "NEGATIVE" :
                    (App.get(urineDRCrystals).equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) ? "POSITIVE" : "INDETERMINATE")});
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(fourthDateCalendar)});
        }

        AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.submitting_form));
                        loading.show();
                    }
                });

                String result = "";
                /*result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}));
                if (result.contains("SUCCESS"))
                    return "SUCCESS";*/

                if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
                    result = serverService.saveEncounterAndObservation("Urine Detailed Report Order", FORM, formDateCalendar, observations.toArray(new String[][]{}));
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testresult))) {
                    result = serverService.saveEncounterAndObservation("Urine Detailed Report Result", FORM, formDateCalendar, observations.toArray(new String[][]{}));
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                }

                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.equals("SUCCESS")) {
                    resetViews();

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.form_submitted));
                    Drawable submitIcon = getResources().getDrawable(R.drawable.ic_submit);
                    alertDialog.setIcon(submitIcon);
                    int color = App.getColor(context, R.attr.colorAccent);
                    DrawableCompat.setTint(submitIcon, color);
                    alertDialog.setTitle(getResources().getString(R.string.title_completed));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else if (result.equals("CONNECTION_ERROR")) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.data_connection_error) + "\n\n (" + result + ")");
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    String message = getResources().getString(R.string.insert_error) + "\n\n (" + result + ")";
                    alertDialog.setMessage(getResources().getString(R.string.insert_error));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }


            }
        };
        submissionFormTask.execute("");

        return false;
    }

    @Override
    public boolean save() {

        HashMap<String, String> formValues = new HashMap<String, String>();

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        return true;
    }

    @Override
    public void refill(int encounterId) {
        OfflineForm fo = serverService.getOfflineFormById(encounterId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (fo.getFormName().contains("Order")) {
                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);
                if (obs[0][0].equals("TEST ID")) {
                    urineDRTestID.getEditText().setText(obs[0][1]);
                    urineDRTestID.getEditText().setEnabled(false);
                    testIdView.setEnabled(false);
                    testIdView.setImageResource(R.drawable.ic_checked_green);
                    //checkTestId();
                } else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                    urineDRMonthOfVisit.getSpinner().selectValue(obs[0][1]);
                } else if (obs[0][0].equals("DATE TEST ORDERED")) {
                    String secondDate = obs[0][1];
                    secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    urineDRTestOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                }
                submitButton.setEnabled(true);
            } else {
                formType.getRadioGroup().getButtons().get(1).setChecked(true);
                formType.getRadioGroup().getButtons().get(0).setEnabled(false);
                if (obs[0][0].equals("TEST ID")) {
                    urineDRTestID.getEditText().setText(obs[0][1]);
                    checkTestId();
                    urineDRTestID.getEditText().setEnabled(false);
                    testIdView.setEnabled(false);
                } else if (obs[0][0].equals("TEST RESULT DATE")) {
                    String secondDate = obs[0][1];
                    thirdDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    urineDRTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
                } else if (obs[0][0].equals("URINE COLOR")) {
                    String value = obs[0][1].equals("COLOURLESS") ? getResources().getString(R.string.comorbidities_urinedr_test_color_options_colourless) :
                            (obs[0][1].equals("RED COLOR") ? getResources().getString(R.string.comorbidities_urinedr_test_color_options_red) :
                                    (obs[0][1].equals("LIGHT YELLOW COLOUR") ? getResources().getString(R.string.comorbidities_urinedr_test_color_light_yellow) :
                                            (obs[0][1].equals("YELLOW-GREEN COLOUR") ? getResources().getString(R.string.comorbidities_urinedr_test_color_options_yellow_green) :
                                                    (obs[0][1].equals("DARK YELLOW COLOUR") ? getResources().getString(R.string.comorbidities_urinedr_test_color_options_dark_yellow) :
                                                            (obs[0][1].equals("ORANGE COLOR") ? getResources().getString(R.string.comorbidities_urinedr_test_color_options_orange) :
                                                                    (obs[0][1].equals("BROWN COLOR") ? getResources().getString(R.string.comorbidities_urinedr_test_color_options_brown) :
                                                                            (obs[0][1].equals("WHITE COLOR") ? getResources().getString(R.string.comorbidities_urinedr_test_color_options_white) : getResources().getString(R.string.comorbidities_urinedr_test_color_options_grey))))))));
                    urineDRColor.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("URINE SPECIFIC GRAVITY")) {
                    urineDRSpecificGravity.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("URINE PH")) {
                    urineDRPH.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("URINARY ALBUMIN")) {
                    urineDRAlbumin.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("URINE GLUCOSE")) {
                    urineDRSugar.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("URINE KETONES, QUANTITATIVE")) {
                    urineDRKetones.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("URINE BILIRUBIN")) {
                    urineDRBilirubin.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("HEMATURIA")) {
                    for (RadioButton rb : urineDRBlood.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) && obs[0][1].equals("NEGATIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) && obs[0][1].equals("POSITIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_ind)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                } else if (obs[0][0].equals("URINE NITRITE TEST")) {
                    for (RadioButton rb : urineDRNitrite.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) && obs[0][1].equals("NEGATIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) && obs[0][1].equals("POSITIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_ind)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                } else if (obs[0][0].equals("UROBILINOGEN IN URINE, QUNATITATIV")) {
                    urineDRUrobilinogen.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("ERYTHROCYTES PRESENCE IN URINE SEDIMENT BY LIGHT MICROSCOPY TEST")) {
                    for (RadioButton rb : urineDRRedCells.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) && obs[0][1].equals("NEGATIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) && obs[0][1].equals("POSITIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_ind)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                } else if (obs[0][0].equals("URINE PUS CELLS")) {
                    for (RadioButton rb : urineDRPusCells.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) && obs[0][1].equals("NEGATIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) && obs[0][1].equals("POSITIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_ind)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                } else if (obs[0][0].equals("EPITHELIAL CELLS")) {
                    for (RadioButton rb : urineDREpithelialCells.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) && obs[0][1].equals("NEGATIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) && obs[0][1].equals("POSITIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_ind)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                } else if (obs[0][0].equals("URINARY CAST, HYALINE")) {
                    for (RadioButton rb : urineDRHyalineCasts.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) && obs[0][1].equals("NEGATIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) && obs[0][1].equals("POSITIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_ind)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                } else if (obs[0][0].equals("CRYSTALS IN URINE")) {
                    for (RadioButton rb : urineDRCrystals.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_neg)) && obs[0][1].equals("NEGATIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_pos)) && obs[0][1].equals("POSITIVE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_urinedr_test_some_options_ind)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                    String secondDate = obs[0][1];
                    fourthDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    urineDRTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString());
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == urineDRTestOrderDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == urineDRTestResultDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        urineDRTestID.getEditText().setEnabled(true);
        testIdView.setEnabled(true);
        formType.getRadioGroup().getButtons().get(0).setEnabled(true);
        formType.getRadioGroup().getButtons().get(1).setEnabled(true);

        thirdDateCalendar = Calendar.getInstance();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        urineDRTestOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        urineDRTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        fourthDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
        fourthDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
        fourthDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
        fourthDateCalendar.add(Calendar.MONTH, 2);
        fourthDateCalendar.add(Calendar.DAY_OF_MONTH, 20);
        nextUrineDRTestDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString());

        submitButton.setEnabled(false);

        testIdView.setVisibility(View.GONE);
        urineDRTestID.setVisibility(View.GONE);
        testIdView.setImageResource(R.drawable.ic_checked);

        goneVisibility();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);

            } else bundle.putBoolean("save", false);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == formType.getRadioGroup()) {
            //showTestOrderOrTestResult();
            //formType.getQuestionView().setError(null);

            formDate.setVisibility(View.VISIBLE);
            urineDRTestID.setVisibility(View.VISIBLE);
            urineDRTestID.getEditText().setText("");
            urineDRTestID.getEditText().setError(null);
            goneVisibility();
            submitButton.setEnabled(false);
        }
    }

    void goneVisibility() {
        testOrderUrineDR.setVisibility(View.GONE);
        urineDRMonthOfVisit.setVisibility(View.GONE);
        urineDRTestOrderDate.setVisibility(View.GONE);

        //second page views...
        testResultUrineDR.setVisibility(View.GONE);
        urineDRTestResultDate.setVisibility(View.GONE);
        urineDRQuantity.setVisibility(View.GONE);
        urineDRColor.setVisibility(View.GONE);
        urineDRSpecificGravity.setVisibility(View.GONE);
        urineDRPH.setVisibility(View.GONE);
        urineDRAlbumin.setVisibility(View.GONE);
        urineDRSugar.setVisibility(View.GONE);
        urineDRKetones.setVisibility(View.GONE);
        urineDRBilirubin.setVisibility(View.GONE);
        urineDRBlood.setVisibility(View.GONE);
        urineDRNitrite.setVisibility(View.GONE);
        urineDRUrobilinogen.setVisibility(View.GONE);
        urineDRRedCells.setVisibility(View.GONE);
        urineDRPusCells.setVisibility(View.GONE);
        urineDREpithelialCells.setVisibility(View.GONE);
        urineDRHyalineCasts.setVisibility(View.GONE);
        urineDRCrystals.setVisibility(View.GONE);
        nextUrineDRTestDate.setVisibility(View.GONE);

    }

    void showTestOrderOrTestResult() {
        if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
            testOrderUrineDR.setVisibility(View.VISIBLE);
            urineDRMonthOfVisit.setVisibility(View.VISIBLE);
            urineDRTestOrderDate.setVisibility(View.VISIBLE);

            //second page views...
            testResultUrineDR.setVisibility(View.GONE);
            urineDRTestResultDate.setVisibility(View.GONE);
            urineDRQuantity.setVisibility(View.GONE);
            urineDRColor.setVisibility(View.GONE);
            urineDRSpecificGravity.setVisibility(View.GONE);
            urineDRPH.setVisibility(View.GONE);
            urineDRAlbumin.setVisibility(View.GONE);
            urineDRSugar.setVisibility(View.GONE);
            urineDRKetones.setVisibility(View.GONE);
            urineDRBilirubin.setVisibility(View.GONE);
            urineDRBlood.setVisibility(View.GONE);
            urineDRNitrite.setVisibility(View.GONE);
            urineDRUrobilinogen.setVisibility(View.GONE);
            urineDRRedCells.setVisibility(View.GONE);
            urineDRPusCells.setVisibility(View.GONE);
            urineDREpithelialCells.setVisibility(View.GONE);
            urineDRHyalineCasts.setVisibility(View.GONE);
            urineDRCrystals.setVisibility(View.GONE);
            nextUrineDRTestDate.setVisibility(View.GONE);

        } else {
            testOrderUrineDR.setVisibility(View.GONE);
            urineDRMonthOfVisit.setVisibility(View.GONE);
            urineDRTestOrderDate.setVisibility(View.GONE);

            //second page views...
            testResultUrineDR.setVisibility(View.VISIBLE);
            urineDRTestResultDate.setVisibility(View.VISIBLE);
            urineDRQuantity.setVisibility(View.VISIBLE);
            urineDRColor.setVisibility(View.VISIBLE);
            urineDRSpecificGravity.setVisibility(View.VISIBLE);
            urineDRPH.setVisibility(View.VISIBLE);
            urineDRAlbumin.setVisibility(View.VISIBLE);
            urineDRSugar.setVisibility(View.VISIBLE);
            urineDRKetones.setVisibility(View.VISIBLE);
            urineDRBilirubin.setVisibility(View.VISIBLE);
            urineDRBlood.setVisibility(View.VISIBLE);
            urineDRNitrite.setVisibility(View.VISIBLE);
            urineDRUrobilinogen.setVisibility(View.VISIBLE);
            urineDRRedCells.setVisibility(View.VISIBLE);
            urineDRPusCells.setVisibility(View.VISIBLE);
            urineDREpithelialCells.setVisibility(View.VISIBLE);
            urineDRHyalineCasts.setVisibility(View.VISIBLE);
            urineDRCrystals.setVisibility(View.VISIBLE);
            nextUrineDRTestDate.setVisibility(View.VISIBLE);
        }
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

                checkTestId();

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

    private void checkTestId() {
        AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.verifying_test_id));
                        loading.show();
                    }
                });

                String result = "";

                Object[][] testIds = serverService.getTestIdByPatientAndEncounterType(App.getPatientId(), "Comorbidities-Urine Detailed Report Order");

                Log.d("TEST_IDS_U", Arrays.deepToString(testIds));

                if (testIds == null || testIds.length < 1) {
                    if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder)))
                        return "SUCCESS";
                    else
                        return "";
                }

                if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
                    result = "SUCCESS";
                    for (int i = 0; i < testIds.length; i++) {
                        if (String.valueOf(testIds[i][0]).equals(App.get(urineDRTestID))) {
                            return "";
                        }
                    }
                }

                if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testresult))) {
                    result = "";
                    for (int i = 0; i < testIds.length; i++) {
                        if (String.valueOf(testIds[i][0]).equals(App.get(urineDRTestID))) {
                            return "SUCCESS";
                        }
                    }
                }

                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            ;

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.equals("SUCCESS")) {

                    testIdView.setImageResource(R.drawable.ic_checked_green);
                    showTestOrderOrTestResult();
                    submitButton.setEnabled(true);

                } else {

                    if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
                        urineDRTestID.getEditText().setError("Test Id already used.");
                    } else {
                        urineDRTestID.getEditText().setError("No order form found for the test id for patient");
                    }

                }

                try {
                    InputMethodManager imm = (InputMethodManager) mainContent.getContext().getSystemService(mainContent.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        };
        submissionFormTask.execute("");

    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Object instantiateItem(View container, int position) {

            ViewGroup viewGroup = groups.get(position);
            ((ViewPager) container).addView(viewGroup, 0);

            return viewGroup;
        }

        @Override
        public void destroyItem(View container, int position, Object obj) {
            ((ViewPager) container).removeView((View) obj);
        }

        @Override
        public boolean isViewFromObject(View container, Object obj) {
            return container == obj;
        }

    }

    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar;
            if (getArguments().getInt("type") == DATE_DIALOG_ID)
                calendar = formDateCalendar;
            else if (getArguments().getInt("type") == SECOND_DATE_DIALOG_ID)
                calendar = secondDateCalendar;
            else if (getArguments().getInt("type") == THIRD_DATE_DIALOG_ID)
                calendar = thirdDateCalendar;
            else
                return null;

            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setTag(getArguments().getInt("type"));
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            if (((int) view.getTag()) == DATE_DIALOG_ID)
                formDateCalendar.set(yy, mm, dd);
            else if (((int) view.getTag()) == SECOND_DATE_DIALOG_ID)
                secondDateCalendar.set(yy, mm, dd);
            else if (((int) view.getTag()) == THIRD_DATE_DIALOG_ID)
                thirdDateCalendar.set(yy, mm, dd);

            updateDisplay();
        }
    }

}



