package com.ihsinformatics.gfatmmobile.common;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 1/24/2017.
 */

public class ScreeningChestXrayOrderAndResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    public static final String VAN_DEFAULT_VALUE = "GHD-T";
    Context context;


    boolean emptyError = false;
    Boolean dateChoose = false;


    // Views...

    TitledRadioGroup pastXray;
    TitledRadioGroup pregnancyHistory;
    MyTextView cxrResultTitle;
    MyTextView cxrOrderTitle;
    TitledRadioGroup screenXrayType;
    TitledRadioGroup formType;
    TitledSpinner monthOfTreatment;
    //  TitledButton testDate;
    TitledEditText testId;
    TitledEditText cat4tbScore;
    TitledRadioGroup radiologicalDiagnosis;
    TitledCheckBoxes abnormalDetailedDiagnosis;
    TitledEditText abnormalDetailedDiagnosisOther;
    TitledRadioGroup extentOfDisease;
    TitledEditText radiologistRemarks;
    //  ImageView testIdView;
    //  LinearLayout linearLayout;

    TitledSpinner orderIds;
    TitledEditText orderId;

    TitledRadioGroup xrayDone;

    TitledSpinner reasonForXray;


    TitledRadioGroup compare_xray;
    TitledRadioGroup maximum_cavity;
    TitledRadioGroup fibrosis;
    TitledButton returnVisitDate;
    private TitledRadioGroup typeOfScreening;
    private TitledRadioGroup coughForTwoWeeks;
    private TitledEditText vanName;


    /**
     * CHANGE pageCount and formName Variable only...
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 1;
        formName = Forms.SCREENING_CHEST_XRAY_ORDER_AND_RESULT_FORM;
        form = Forms.screeningChestXrayOrderAndResultForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(pageCount - 1);
        formNameView.setText(formName);

        initViews();

        groups = new ArrayList<ViewGroup>();

        if (App.isLanguageRTL()) {
            for (int i = pageCount - 1; i >= 0; i--) {
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
            for (int i = 0; i < pageCount; i++) {
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
        }

        gotoFirstPage();

        return mainContent;
    }

    /**
     * Initializes all views and ArrayList and Views Array
     */
    public void initViews() {

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        pastXray = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_past_xray), getResources().getStringArray(R.array.fast_yes_no_list), "", App.VERTICAL, App.VERTICAL, true, "X RAY IN PAST 6 MONTHS", getResources().getStringArray(R.array.yes_no_list_concept));
        pregnancyHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_pregnancy_history), getResources().getStringArray(R.array.fast_yes_no_list), "", App.VERTICAL, App.VERTICAL, true, "PREGNANCY STATUS", getResources().getStringArray(R.array.yes_no_list_concept));


        testId = new TitledEditText(context, null, getResources().getString(R.string.fast_test_id), "", "", 40, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "TEST ID");
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_select_form_type), getResources().getStringArray(R.array.fast_order_and_result_list), "", App.HORIZONTAL, App.HORIZONTAL);
        cxrOrderTitle = new MyTextView(context, getResources().getString(R.string.fast_cxr_order_title));
        cxrOrderTitle.setTypeface(null, Typeface.BOLD);
        screenXrayType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_what_type_of_xray_is_this), getResources().getStringArray(R.array.fast_type_of_xray_is_this_list), null, App.VERTICAL, App.VERTICAL, true, "TYPE OF X RAY", new String[]{"CHEST XRAY", "X-RAY, OTHER"});
        monthOfTreatment = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_month_of_treatment), getResources().getStringArray(R.array.fast_number_list), "", App.VERTICAL, false, "FOLLOW-UP MONTH", getResources().getStringArray(R.array.fast_number_list));
        typeOfScreening = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_type_of_screen), getResources().getStringArray(R.array.fast_type_of_screening_list), "", App.HORIZONTAL, App.HORIZONTAL, true, "TYPE OF SCREENING", new String[]{"CXR Screening", "VERBAL SYMPTOM SCREENING"});
        coughForTwoWeeks = new TitledRadioGroup(context, null, getResources().getString(R.string.cough_for_two_weeks), getResources().getStringArray(R.array.fast_choice_list), "", App.HORIZONTAL, App.HORIZONTAL, true, "COUGH LASTING MORE THAN 2 WEEKS", getResources().getStringArray(R.array.fast_choice_list_concepts));

        vanName = new TitledEditText(context, null, getResources().getString(R.string.van_name), "", "", 8, RegexUtil.VAN_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, true, "VAN NAME");
        vanName.getEditText().setText(VAN_DEFAULT_VALUE);
        Selection.setSelection( vanName.getEditText().getText(),  vanName.getEditText().getText().length());

        updateFollowUpMonth();
        //   testDate = new TitledButton(context, null, getResources().getString(R.string.fast_test_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        cxrResultTitle = new MyTextView(context, getResources().getString(R.string.fast_cxr_result_title));
        cxrResultTitle.setTypeface(null, Typeface.BOLD);
        cat4tbScore = new TitledEditText(context, null, getResources().getString(R.string.fast_chest_xray_cad4tb_score), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true, "CHEST X-RAY SCORE");
        radiologicalDiagnosis = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_radiologica_diagnosis), getResources().getStringArray(R.array.fast_radiological_diagonosis_list), "", App.VERTICAL, App.VERTICAL, true, "RADIOLOGICAL DIAGNOSIS", new String[]{"NORMAL", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        abnormalDetailedDiagnosis = new TitledCheckBoxes(context, null, getResources().getString(R.string.fast_if_abnormal_detailed_diagnosis), getResources().getStringArray(R.array.fast_abnormal_detailed_diagnosis_list), new Boolean[]{true, false, false, false, false, false, false}, App.VERTICAL, App.VERTICAL, true, "ABNORMAL, DETAILED DIAGNOSIS", new String[]{"ADENOPATHY", "INFILTRATE", "CONSOLIDATION", "PLEURAL EFFUSION", "CAVIATION", "MILIARY", "OTHER ABNORMAL DETAILED DIAGNOSIS"});
        abnormalDetailedDiagnosisOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER ABNORMAL DETAILED DIAGNOSIS");
        extentOfDisease = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_extent_of_desease), getResources().getStringArray(R.array.fast_extent_of_disease_list), getResources().getString(R.string.fast_normal), App.VERTICAL, App.VERTICAL, true, "EXTENT OF DISEASE", new String[]{"NORMAL", "UNILATERAL", "BILATERAL", "ABNORMAL"});
        radiologistRemarks = new TitledEditText(context, null, getResources().getString(R.string.fast_radiologist_remarks), "", "", 500, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "CLINICIAN NOTES (TEXT)");
        orderId = new TitledEditText(context, null, getResources().getString(R.string.order_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "ORDER ID");
        // orderId.setLongClickable(false);
        orderIds = new TitledSpinner(context, "", getResources().getString(R.string.order_id), getResources().getStringArray(R.array.pet_empty_array), "", App.HORIZONTAL);

        reasonForXray = new TitledSpinner(context, "", getResources().getString(R.string.fast_reason_for_xray), getResources().getStringArray(R.array.fast_reason_for_xray_list_temp), "", App.VERTICAL, true, "REASON FOR X-RAY", new String[]{"IDENTIFIED PATIENT THROUGH SCREENING", "BASELINE DIAGNOSTIC", "FOLLOW UP"});
        xrayDone = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_xray_done), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "CXR DONE", getResources().getStringArray(R.array.yes_no_list_concept));


        compare_xray = new TitledRadioGroup(context, null, getResources().getString(R.string.compare_xray), getResources().getStringArray(R.array.compare_xray_option), getResources().getString(R.string.compare_xray_not), App.VERTICAL, App.VERTICAL, true, "COMPARISON WITH LAST X-RAY", new String[]{"IMPROVED", "WORSENING", "UNCHANGED", "NOT APPLICABLE"});
        maximum_cavity = new TitledRadioGroup(context, null, getResources().getString(R.string.maximum_cavity), getResources().getStringArray(R.array.maximum_cavity_option), getResources().getString(R.string.maximum_cavity_no_cav), App.VERTICAL, App.VERTICAL, true, "MAXIMUM CAVITY SIZE", new String[]{"NO CAVITIES", "&amp;lt;5 cm", ">=5 cm"});
        fibrosis = new TitledRadioGroup(context, null, getResources().getString(R.string.fibrosis), getResources().getStringArray(R.array.fibrosis_option), getResources().getString(R.string.fibrosis_none), App.VERTICAL, App.VERTICAL, true, "FIBROSIS", new String[]{"NONE", "ONE LOBE OR LESS", "MORE THAN ONE LOBE"});

        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.fast_next_appointment_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL, false);
        // Used for reset fields...
        views = new View[]{formDate.getButton(), formType.getRadioGroup(), testId.getEditText(),
                monthOfTreatment.getSpinner(), radiologicalDiagnosis.getRadioGroup(), cat4tbScore.getEditText(), abnormalDetailedDiagnosis,
                abnormalDetailedDiagnosisOther.getEditText(), extentOfDisease.getRadioGroup(), radiologistRemarks.getEditText(), typeOfScreening.getRadioGroup(), coughForTwoWeeks.getRadioGroup()
                , orderId.getEditText(), orderIds.getSpinner(), vanName.getEditText(), reasonForXray.getSpinner()
                , compare_xray.getRadioGroup(), maximum_cavity.getRadioGroup(), fibrosis.getRadioGroup(), returnVisitDate.getButton(), screenXrayType.getRadioGroup(), xrayDone.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType, formDate, cxrOrderTitle, pastXray, pregnancyHistory, reasonForXray, typeOfScreening, coughForTwoWeeks, orderId, screenXrayType, vanName, monthOfTreatment, cxrResultTitle, orderIds, xrayDone, testId, cat4tbScore, radiologicalDiagnosis,
                        abnormalDetailedDiagnosis, abnormalDetailedDiagnosisOther, extentOfDisease, radiologistRemarks/*, compare_xray, maximum_cavity, fibrosis*/, returnVisitDate}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);

        reasonForXray.getSpinner().setOnItemSelectedListener(this);
        radiologicalDiagnosis.getRadioGroup().setOnCheckedChangeListener(this);
        pastXray.getRadioGroup().setOnCheckedChangeListener(this);
        pregnancyHistory.getRadioGroup().setOnCheckedChangeListener(this);
        screenXrayType.getRadioGroup().setOnCheckedChangeListener(this);
        orderIds.getSpinner().setOnItemSelectedListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        xrayDone.getRadioGroup().setOnCheckedChangeListener(this);

        for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        cat4tbScore.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    int number = Integer.parseInt(s.toString());
                    if (number > 100 && number < 1) {
                        cat4tbScore.getEditText().setError(getResources().getString(R.string.fast_cad_score_value));
                    }
                }
            }
        });

        vanName.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith(VAN_DEFAULT_VALUE)){
                    vanName.getEditText().setText(VAN_DEFAULT_VALUE);
                    Selection.setSelection( vanName.getEditText().getText(),  vanName.getEditText().getText().length());
                }

            }
        });

        resetViews();
    }

    @Override
    public void updateDisplay() {
        Calendar treatDateCalender = null;
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            personDOB = personDOB.substring(0, 10);

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else {
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

                if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.fast_result))) {

                    if (!App.get(orderIds).equals("")) {
                        String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), "CXR Screening Test Order", "ORDER ID", App.get(orderIds));

                        String format = "";
                        if (encounterDateTime.contains("/")) {
                            format = "dd/MM/yyyy";
                        } else {
                            format = "yyyy-MM-dd";
                        }

                        Date orderDate = App.stringToDate(encounterDateTime, format);

                        if (formDateCalendar.before(App.getCalendar(orderDate))) {

                            Date dDate = App.stringToDate(formDa, "EEEE, MMM dd,yyyy");
                            if (dDate.before(orderDate)) {
                                formDateCalendar = Calendar.getInstance();
                                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                            } else {
                                formDateCalendar = App.getCalendar(dDate);
                                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                            }

                            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_result_date_cannot_be_before_order_date), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();

                        }

                    }
                } else if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.fast_order))) {
                    String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + "Treatment Initiation", "REGISTRATION DATE");

                    if (treatmentDate == null) {
                        treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-" + "Treatment Initiation", "REGISTRATION DATE");
                        if (treatmentDate == null)
                            treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "PET-" + "Treatment Initiation", "TREATMENT START DATE");
                    }
                    if (treatmentDate != null) {
                        treatDateCalender = App.getCalendar(App.stringToDate(treatmentDate, "yyyy-MM-dd"));
                        if (formDateCalendar.before(treatDateCalender)) {
                            formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_cannot_be_before_treatment_initiation_form), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();

                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        } else {
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        }
                    }

                }
            }

        } else {
            String formDa = formDate.getButton().getText().toString();

            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.fast_result))) {

                if (!App.get(orderIds).equals("")) {
                    String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), "CXR Screening Test Order", "ORDER ID", App.get(orderIds));

                    String format = "";
                    if (encounterDateTime.contains("/")) {
                        format = "dd/MM/yyyy";
                    } else {
                        format = "yyyy-MM-dd";
                    }

                    Date orderDate = App.stringToDate(encounterDateTime, format);

                    if (formDateCalendar.before(App.getCalendar(orderDate))) {

                        Date dDate = App.stringToDate(formDa, "EEEE, MMM dd,yyyy");
                        if (dDate.before(orderDate)) {
                            formDateCalendar = Calendar.getInstance();
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        } else {
                            formDateCalendar = App.getCalendar(dDate);
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        }

                        snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_result_date_cannot_be_before_order_date), Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();

                    }

                }
            }
        }
        if (!dateChoose) {
            Calendar requiredDate = formDateCalendar.getInstance();
            requiredDate.setTime(formDateCalendar.getTime());
            requiredDate.add(Calendar.DATE, 2);

            if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                secondDateCalendar.setTime(requiredDate.getTime());
            } else {
                requiredDate.add(Calendar.DATE, 1);
                secondDateCalendar.setTime(requiredDate.getTime());
            }
        }
        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = returnVisitDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            //Date date = new Date();
            if (secondDateCalendar.before(formDateCalendar)) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(personDOB)) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_next_appointment_date_cant_be_before_registeration_date), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }
        updateFollowUpMonth();
        dateChoose = false;
        formDate.getButton().setEnabled(true);
        returnVisitDate.getButton().setEnabled(true);
    }

    public void updateFollowUpMonth() {

        String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + "Treatment Initiation", "REGISTRATION DATE");

        if (treatmentDate == null) {
            treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-" + "TB Treatment Initiation", "REGISTRATION DATE");
            if (treatmentDate == null)
                treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "PET-" + "Treatment Initiation", "TREATMENT START DATE");
        }

        String format = "";
        String[] monthArray;

        if (treatmentDate == null) {
            monthArray = new String[1];
            monthArray[0] = "0";
            monthOfTreatment.getSpinner().setSpinnerData(monthArray);
        } else {
            if (treatmentDate.contains("/")) {
                format = "dd/MM/yyyy";
            } else {
                format = "yyyy-MM-dd";
            }
            Date convertedDate = App.stringToDate(treatmentDate, format);
            Calendar treatmentDateCalender = App.getCalendar(convertedDate);
            int diffYear = formDateCalendar.get(Calendar.YEAR) - treatmentDateCalender.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + formDateCalendar.get(Calendar.MONTH) - treatmentDateCalender.get(Calendar.MONTH);

            if (diffMonth == 0) {
                monthArray = new String[1];
                monthArray[0] = "1";
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
            } else if (diffMonth > 24) {
                monthArray = new String[24];
                for (int i = 0; i < 24; i++) {
                    monthArray[i] = String.valueOf(i + 1);
                }
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
            } else {
                monthArray = new String[diffMonth];
                for (int i = 0; i < diffMonth; i++) {
                    monthArray[i] = String.valueOf(i + 1);
                }
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
            }
        }
    }

    @Override
    public boolean validate() {
        Boolean error = super.validate();
        boolean isChecked = false;


        if (cat4tbScore.getVisibility() == View.VISIBLE) {
            if (!cat4tbScore.getEditText().getText().toString().trim().isEmpty()) {

                int v = Integer.parseInt(App.get(cat4tbScore));
                if (v < 0 || v > 100) {
                    cat4tbScore.getEditText().setError(getString(R.string.fast_cad_score_value));
                    cat4tbScore.getEditText().requestFocus();
                    error = true;
                }
            }
        }

        if (vanName.getVisibility() == View.VISIBLE) {
            if (!vanName.getEditText().getText().toString().trim().isEmpty() && !vanName.getEditText().getText().toString().equals(VAN_DEFAULT_VALUE)) {

                if (!vanName.getEditText().getText().toString().matches(RegexUtil.vanNumber)) {
                    vanName.getEditText().setError(getString(R.string.invalid_value));
                    vanName.getEditText().requestFocus();
                    error = true;
                }
            } else {
                vanName.getEditText().setError(getString(R.string.empty_field));
                vanName.getEditText().requestFocus();
                error = true;
            }
        }

        Boolean flag = true;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            if (saveFlag) {
                flag = false;
            } else {
                flag = true;
            }
        }


        if (orderIds.getVisibility() == View.VISIBLE && flag) {
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), "CXR Screening Test Result", "ORDER ID");
            if (resultTestIds != null) {
                for (String id : resultTestIds) {

                    if (id.equals(App.get(orderIds))) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.ctb_order_result_found_error) + App.get(orderIds));
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

                        return false;
                    }
                }
            }
        }

        if (testId.getVisibility() == View.VISIBLE && flag) {
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), "CXR Screening Test Result", "TEST ID");
            if (resultTestIds != null) {
                for (String id : resultTestIds) {
                    if (id.equals(App.get(testId))) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.ctb_test_result_found_error) + App.get(testId));
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

                        return false;
                    }

                }
            }

        }


        if (error) {
            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
            if (!emptyError)
                alertDialog.setMessage(getString(R.string.form_error));
            else
                alertDialog.setMessage(getString(R.string.fast_required_field_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            //  DrawableCompat.setTint(clearIcon, color);
            alertDialog.setIcon(clearIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_error));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
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

    public void setOrderId() {
        Date nowDate = new Date();
        orderId.getEditText().setText(App.getSqlDateTime(nowDate));
        orderId.getEditText().setKeyListener(null);
        orderId.getEditText().setFocusable(false);
    }

    @Override
    public boolean submit() {
        final ArrayList<String[]> observations = getObservations();

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if (!flag) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.form_does_not_exist));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    bundle.putBoolean("save", false);
                                    submit();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.backToMainMenu();
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
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

                    /*Toast.makeText(context, getString(R.string.form_does_not_exist),
                            Toast.LENGTH_LONG).show();*/

                    return false;
                }
                observations.add(new String[]{"TIME TAKEN TO FILL FORM", timeTakeToFill});
            } else {
                endTime = new Date();
                observations.add(new String[]{"TIME TAKEN TO FILL FORM", String.valueOf(App.getTimeDurationBetween(startTime, endTime))});
            }
            bundle.putBoolean("save", false);
        } else {
            endTime = new Date();
            observations.add(new String[]{"TIME TAKEN TO FILL FORM", String.valueOf(App.getTimeDurationBetween(startTime, endTime))});
        }


        if (!formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {

            observations.add(new String[]{"ORDER ID", App.get(orderIds)});

            if (returnVisitDate.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(secondDateCalendar)});
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

                if (App.get(formType).equals(getResources().getString(R.string.fast_order))) {
                    result = serverService.saveEncounterAndObservation("CXR Screening Test Order", form, formDateCalendar, observations.toArray(new String[][]{}), true);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.fast_result))) {
                    result = serverService.saveEncounterAndObservation("CXR Screening Test Result", form, formDateCalendar, observations.toArray(new String[][]{}), false);
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
                    MainActivity.backToMainMenu();
                    try {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

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
                    alertDialog.setMessage(message);
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

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(formName, "12345-5", formValues);*/

        return true;
    }


    void showTestOrderOrTestResult() {
        //formDate.setVisibility(View.VISIBLE);
        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {

            cxrOrderTitle.setVisibility(View.VISIBLE);
            // formDate.getQuestionView().setText(getResources().getString(R.string.fast_test_date));
            screenXrayType.setVisibility(View.VISIBLE);
            reasonForXray.setVisibility(View.VISIBLE);

            if (App.get(reasonForXray).equals(getResources().getString(R.string.fast_followup)))
                monthOfTreatment.setVisibility(View.VISIBLE);
            else
                monthOfTreatment.setVisibility(View.GONE);

            cxrResultTitle.setVisibility(View.GONE);
            cat4tbScore.setVisibility(View.GONE);
            radiologicalDiagnosis.setVisibility(View.GONE);
            abnormalDetailedDiagnosis.setVisibility(View.GONE);
            abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
            extentOfDisease.setVisibility(View.GONE);
            radiologistRemarks.setVisibility(View.GONE);

            maximum_cavity.setVisibility(View.GONE);
            compare_xray.setVisibility(View.GONE);
            fibrosis.setVisibility(View.GONE);
            xrayDone.setVisibility(View.GONE);

            returnVisitDate.setVisibility(View.GONE);

            orderId.setVisibility(View.VISIBLE);
            Date nowDate = new Date();
            orderId.getEditText().setText(App.getSqlDateTime(nowDate));
            orderIds.setVisibility(View.GONE);
            testId.setVisibility(View.GONE);
            orderId.setOnKeyListener(null);
            orderId.getEditText().setFocusable(false);

            monthOfTreatment.getSpinner().selectDefaultValue();
            updateFollowUpMonth();

        } else if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))) {
            cxrOrderTitle.setVisibility(View.GONE);
            screenXrayType.setVisibility(View.GONE);
            monthOfTreatment.setVisibility(View.GONE);
            pastXray.setVisibility(View.GONE);
            pregnancyHistory.setVisibility(View.GONE);
            typeOfScreening.setVisibility(View.GONE);
            coughForTwoWeeks.setVisibility(View.GONE);

            vanName.setVisibility(View.GONE);
            reasonForXray.setVisibility(View.GONE);
            cxrResultTitle.setVisibility(View.VISIBLE);
            returnVisitDate.setVisibility(View.VISIBLE);

            radiologistRemarks.setVisibility(View.VISIBLE);
            extentOfDisease.setVisibility(View.VISIBLE);

            if (radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_suggestive_of_tb)) || radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_not_suggestive_of_tb))) {
                abnormalDetailedDiagnosis.setVisibility(View.VISIBLE);
                for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()) {
                    if (App.get(cb).equals(getResources().getString(R.string.fast_others))) {
                        if (cb.isChecked()) {
                            abnormalDetailedDiagnosisOther.setVisibility(View.VISIBLE);
                        } else {
                            abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
                        }
                    }
                }
            }

            String value = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Order", "ORDER ID", App.get(orderIds), "TYPE OF X RAY");
            if (value != null) {
                if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))) {
                    if (value.contains("CHEST XRAY") && value.contains("X-RAY, OTHER") && App.get(xrayDone).equals(getString(R.string.yes))) {
                        cat4tbScore.setVisibility(View.VISIBLE);
                        extentOfDisease.setVisibility(View.VISIBLE);
                        radiologicalDiagnosis.setVisibility(View.VISIBLE);
                        if (radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_suggestive_of_tb)) || radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_not_suggestive_of_tb))) {
                            abnormalDetailedDiagnosis.setVisibility(View.VISIBLE);
                            for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()) {
                                if (App.get(cb).equals(getResources().getString(R.string.fast_others))) {
                                    if (cb.isChecked()) {
                                        abnormalDetailedDiagnosisOther.setVisibility(View.VISIBLE);
                                    } else {
                                        abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }


                    } else if (value.equals("CHEST XRAY") && App.get(xrayDone).equals(getString(R.string.yes))) {
                        cat4tbScore.setVisibility(View.VISIBLE);

                        radiologicalDiagnosis.setVisibility(View.GONE);
                        abnormalDetailedDiagnosis.setVisibility(View.GONE);
                        abnormalDetailedDiagnosisOther.setVisibility(View.GONE);

                        maximum_cavity.setVisibility(View.GONE);
                        fibrosis.setVisibility(View.GONE);
                        compare_xray.setVisibility(View.GONE);

                    } else if (value.equals("X-RAY, OTHER") && App.get(xrayDone).equals(getString(R.string.yes))) {
                        cat4tbScore.setVisibility(View.GONE);
                        extentOfDisease.setVisibility(View.VISIBLE);
                        radiologicalDiagnosis.setVisibility(View.VISIBLE);

                        maximum_cavity.setVisibility(View.VISIBLE);
                        fibrosis.setVisibility(View.VISIBLE);
                        compare_xray.setVisibility(View.VISIBLE);

                        if (radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_suggestive_of_tb)) || radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_not_suggestive_of_tb))) {
                            abnormalDetailedDiagnosis.setVisibility(View.VISIBLE);
                            for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()) {
                                if (App.get(cb).equals(getResources().getString(R.string.fast_others))) {
                                    if (cb.isChecked()) {
                                        abnormalDetailedDiagnosisOther.setVisibility(View.VISIBLE);
                                    } else {
                                        abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }

                    }
                }
            }


            orderId.setVisibility(View.GONE);
            orderIds.setVisibility(View.VISIBLE);
            testId.setVisibility(View.VISIBLE);

            testId.getEditText().setDefaultValue();
            radiologicalDiagnosis.getRadioGroup().selectDefaultValue();
            cat4tbScore.getEditText().setDefaultValue();
            extentOfDisease.getRadioGroup().selectDefaultValue();
            radiologistRemarks.getEditText().setDefaultValue();
            abnormalDetailedDiagnosis.selectDefaultValue();

            String[] testIds = serverService.getAllObsValues(App.getPatientId(), "CXR Screening Test Order", "ORDER ID");

            if (testIds == null || testIds.length == 0) {
                xrayDone.setVisibility(View.GONE);
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getResources().getString(R.string.fast_no_order_found_for_the_patient));
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
                submitButton.setEnabled(false);
                return;
            }

            if (testIds != null) {
                orderIds.getSpinner().setSpinnerData(testIds);
            }
        }
    }


    @Override
    public void refill(int encounterId) {
        super.refill(encounterId);

        OfflineForm fo = serverService.getSavedFormById(encounterId);

        ArrayList<String[][]> obsValue = fo.getObsValue();


        for (int i = 0; i < obsValue.size(); i++) {
            formDate.setVisibility(View.VISIBLE);
            String[][] obs = obsValue.get(i);

            if (fo.getFormName().contains("Order")) {
                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);

                submitButton.setEnabled(true);
            } else {
                formType.getRadioGroup().getButtons().get(1).setChecked(true);
                formType.getRadioGroup().getButtons().get(0).setEnabled(false);

                if (obs[0][0].equals("ORDER ID")) {
                    orderIds.getSpinner().selectValue(obs[0][1]);
                    orderIds.getSpinner().setEnabled(false);
                } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                    String secondDate = obs[0][1];
                    secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                    returnVisitDate.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);
            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);*/
        }

        if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, true, false, true);

            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", true);*/
            dateChoose = true;

        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        screenXrayType.getQuestionView().setError(null);
        abnormalDetailedDiagnosis.getQuestionView().setError(null);
        for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()) {
            if (App.get(cb).equals(getResources().getString(R.string.fast_others))) {
                if (cb.isChecked()) {
                    abnormalDetailedDiagnosisOther.setVisibility(View.VISIBLE);
                } else {
                    abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        formDate.setVisibility(View.GONE);
        pastXray.setVisibility(View.GONE);
        pregnancyHistory.setVisibility(View.GONE);
        typeOfScreening.setVisibility(View.GONE);
        coughForTwoWeeks.setVisibility(View.GONE);
        vanName.setVisibility(View.GONE);
        //  testIdView.setVisibility(View.GONE);
        testId.setVisibility(View.GONE);
        //   testIdView.setImageResource(R.drawable.ic_checked);
        goneVisibility();
        submitButton.setEnabled(false);

        compare_xray.setVisibility(View.GONE);
        maximum_cavity.setVisibility(View.GONE);
        fibrosis.setVisibility(View.GONE);

        secondDateCalendar.set(Calendar.YEAR, formDateCalendar.get(Calendar.YEAR));
        secondDateCalendar.set(Calendar.DAY_OF_MONTH, formDateCalendar.get(Calendar.DAY_OF_MONTH));
        secondDateCalendar.set(Calendar.MONTH, formDateCalendar.get(Calendar.MONTH));
        secondDateCalendar.add(Calendar.DAY_OF_MONTH, 30);

        Calendar requiredDate = formDateCalendar.getInstance();
        requiredDate.setTime(formDateCalendar.getTime());
        requiredDate.add(Calendar.DATE, 30);
        if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            secondDateCalendar.setTime(requiredDate.getTime());
        } else {
            requiredDate.add(Calendar.DATE, -1);
            secondDateCalendar.setTime(requiredDate.getTime());
        }

        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());


        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        String[] testIds = serverService.getAllObsValues(App.getPatientId(), "CXR Screening Test Order", "ORDER ID");
        if (testIds != null) {
            orderIds.getSpinner().setSpinnerData(testIds);
        }

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

    void goneVisibility() {
        // formDate.setVisibility(View.GONE);
        cxrOrderTitle.setVisibility(View.GONE);
        screenXrayType.setVisibility(View.GONE);
        monthOfTreatment.setVisibility(View.GONE);
        reasonForXray.setVisibility(View.GONE);
        // testDate.setVisibility(View.GONE);
        cxrResultTitle.setVisibility(View.GONE);
        cat4tbScore.setVisibility(View.GONE);
        radiologicalDiagnosis.setVisibility(View.GONE);
        abnormalDetailedDiagnosis.setVisibility(View.GONE);
        abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
        maximum_cavity.setVisibility(View.GONE);
        compare_xray.setVisibility(View.GONE);
        fibrosis.setVisibility(View.GONE);
        extentOfDisease.setVisibility(View.GONE);
        radiologistRemarks.setVisibility(View.GONE);
        xrayDone.setVisibility(View.GONE);


        orderIds.setVisibility(View.GONE);
        orderId.setVisibility(View.GONE);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == orderIds.getSpinner()) {

            xrayDone.getRadioGroup().clearCheck();
            xrayDone.setVisibility(View.VISIBLE);
            String cxrReason = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Order", "ORDER ID", App.get(orderIds), "REASON FOR X-RAY");


            String value = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Order", "ORDER ID", App.get(orderIds), "TYPE OF X RAY");
            if (value != null) {
                /**
                 *if(formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))){
                 if(value.contains("CHEST XRAY") && value.contains("X-RAY, OTHER")){
                 cat4tbScore.setVisibility(View.VISIBLE);
                 cadScoreRange.setVisibility(View.VISIBLE);

                 radiologicalDiagnosis.setVisibility(View.VISIBLE);
                 if (radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_suggestive_of_tb)) || radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_not_suggestive_of_tb))) {
                 abnormalDetailedDiagnosis.setVisibility(View.VISIBLE);
                 for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()) {
                 if (App.get(cb).equals(getResources().getString(R.string.fast_others))) {
                 if (cb.isChecked()) {
                 abnormalDetailedDiagnosisOther.setVisibility(View.VISIBLE);
                 } else {
                 abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
                 }
                 }
                 }
                 }
                 extentOfDisease.setVisibility(View.VISIBLE);

                 }
                 else
                 */
                if (value.equals("CHEST XRAY") && App.get(xrayDone).equals(getString(R.string.yes))) {
                    cat4tbScore.setVisibility(View.VISIBLE);


                    radiologicalDiagnosis.getRadioGroup().clearCheck();
                    radiologicalDiagnosis.setVisibility(View.GONE);
                    abnormalDetailedDiagnosis.setVisibility(View.GONE);
                    abnormalDetailedDiagnosisOther.getEditText().setText(null);
                    abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
                    compare_xray.setVisibility(View.GONE);
                    maximum_cavity.setVisibility(View.GONE);
                    fibrosis.setVisibility(View.GONE);
                } else if (value.equals("X-RAY, OTHER") && App.get(xrayDone).equals(getString(R.string.yes))) {
                    cat4tbScore.setVisibility(View.GONE);
                    cat4tbScore.getEditText().setText(null);

                    compare_xray.setVisibility(View.VISIBLE);
                    maximum_cavity.setVisibility(View.VISIBLE);
                    fibrosis.setVisibility(View.VISIBLE);
                    radiologicalDiagnosis.setVisibility(View.VISIBLE);
                    if (radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_suggestive_of_tb)) || radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_not_suggestive_of_tb))) {
                        abnormalDetailedDiagnosis.setVisibility(View.VISIBLE);
                        for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()) {
                            if (App.get(cb).equals(getResources().getString(R.string.fast_others))) {
                                if (cb.isChecked()) {
                                    abnormalDetailedDiagnosisOther.setVisibility(View.VISIBLE);
                                } else {
                                    abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                    extentOfDisease.setVisibility(View.VISIBLE);
                }

            }
            updateDisplay();
        } else if (spinner == reasonForXray.getSpinner()) {
            if (App.get(reasonForXray).equals(getResources().getString(R.string.fast_followup)))
                monthOfTreatment.setVisibility(View.VISIBLE);
            else
                monthOfTreatment.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (radioGroup == formType.getRadioGroup()) {
            if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {
                submitButton.setEnabled(true);
                formDate.setVisibility(View.VISIBLE);
                setOrderId();
                goneVisibility();
                testId.setVisibility(View.GONE);
                cxrOrderTitle.setVisibility(View.VISIBLE);
                pastXray.setVisibility(View.VISIBLE);
                typeOfScreening.setVisibility(View.VISIBLE);
                coughForTwoWeeks.setVisibility(View.VISIBLE);
                vanName.setVisibility(View.VISIBLE);


                pastXray.getRadioGroup().selectDefaultValue();
                pregnancyHistory.getRadioGroup().selectDefaultValue();
                monthOfTreatment.getSpinner().selectDefaultValue();

                if (App.getPatient().getPerson().getGender().equals("female") || App.getPatient().getPerson().getGender().equals("F")) {
                    pregnancyHistory.setVisibility(View.VISIBLE);
                }


                if (pastXray.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                    screenXrayType.setVisibility(View.VISIBLE);
                    if (App.getPatient().getPerson().getAge() < 15) {
                        screenXrayType.getRadioGroup().getButtons().get(1).setChecked(true);
                        screenXrayType.setRadioGroupEnabled(false);
                    } else {
                        screenXrayType.getRadioGroup().clearCheck();
                        screenXrayType.setRadioGroupEnabled(true);
                    }
                    if (App.get(reasonForXray).equals(getResources().getString(R.string.fast_followup)))
                        monthOfTreatment.setVisibility(View.VISIBLE);
                    else
                        monthOfTreatment.setVisibility(View.GONE);

                    orderId.setVisibility(View.VISIBLE);
                    reasonForXray.setVisibility(View.VISIBLE);

                }

                screenXrayType.setVisibility(View.VISIBLE);
                monthOfTreatment.setVisibility(View.VISIBLE);
                orderId.setVisibility(View.VISIBLE);
                reasonForXray.setVisibility(View.VISIBLE);
                typeOfScreening.setVisibility(View.VISIBLE);
                coughForTwoWeeks.setVisibility(View.VISIBLE);
                vanName.setVisibility(View.VISIBLE);

                returnVisitDate.setVisibility(View.GONE);
                //showTestOrderOrTestResult();
            } else {
                submitButton.setEnabled(true);
                formDate.setVisibility(View.VISIBLE);
                showTestOrderOrTestResult();
            }


        } else if (radioGroup == radiologicalDiagnosis.getRadioGroup()) {
            if (radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_suggestive_of_tb)) || radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_not_suggestive_of_tb))) {
                abnormalDetailedDiagnosis.setVisibility(View.VISIBLE);

                for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()) {
                    if (App.get(cb).equals(getResources().getString(R.string.fast_others))) {
                        if (cb.isChecked()) {
                            abnormalDetailedDiagnosisOther.setVisibility(View.VISIBLE);
                        } else {
                            abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
                        }
                    }
                }

            } else if (radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_normal))) {
                abnormalDetailedDiagnosis.setVisibility(View.GONE);
                abnormalDetailedDiagnosisOther.setVisibility(View.GONE);

            } else {
                abnormalDetailedDiagnosis.setVisibility(View.GONE);
                abnormalDetailedDiagnosisOther.setVisibility(View.GONE);

            }

        } else if (radioGroup == pregnancyHistory.getRadioGroup()) {
            if (pregnancyHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))
                    && pregnancyHistory.getVisibility() == View.VISIBLE) {
                formDate.setVisibility(View.VISIBLE);
                screenXrayType.setVisibility(View.VISIBLE);
                if (App.get(reasonForXray).equals(getResources().getString(R.string.fast_followup)))
                    monthOfTreatment.setVisibility(View.VISIBLE);
                else
                    monthOfTreatment.setVisibility(View.GONE);
                orderId.setVisibility(View.VISIBLE);
                reasonForXray.setVisibility(View.VISIBLE);
                typeOfScreening.setVisibility(View.VISIBLE);
                coughForTwoWeeks.setVisibility(View.VISIBLE);
                vanName.setVisibility(View.VISIBLE);

            } else if (pastXray.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title)) && pregnancyHistory.getVisibility() == View.GONE) {
                formDate.setVisibility(View.VISIBLE);
                screenXrayType.setVisibility(View.VISIBLE);
                if (App.get(reasonForXray).equals(getResources().getString(R.string.fast_followup)))
                    monthOfTreatment.setVisibility(View.VISIBLE);
                else
                    monthOfTreatment.setVisibility(View.GONE);
                orderId.setVisibility(View.VISIBLE);
                reasonForXray.setVisibility(View.VISIBLE);
                typeOfScreening.setVisibility(View.VISIBLE);
                coughForTwoWeeks.setVisibility(View.VISIBLE);
                vanName.setVisibility(View.VISIBLE);
            } else {
                screenXrayType.setVisibility(View.GONE);
                monthOfTreatment.setVisibility(View.GONE);
                orderId.setVisibility(View.GONE);
                reasonForXray.setVisibility(View.GONE);
                typeOfScreening.setVisibility(View.GONE);
                coughForTwoWeeks.setVisibility(View.GONE);
                vanName.setVisibility(View.GONE);
            }
              /*  linearLayout.setVisibility(View.VISIBLE);
                submitButton.setEnabled(false);
                Integer resource = (Integer)testIdView.getTag();
                if(resource == R.drawable.ic_checked_green){
                    cxrOrderTitle.setVisibility(View.VISIBLE);
                    screenXrayType.setVisibility(View.VISIBLE);
                    monthOfTreatment.setVisibility(View.VISIBLE);
                    submitButton.setEnabled(true);
                }
                else{
                    cxrOrderTitle.setVisibility(View.GONE);
                    screenXrayType.setVisibility(View.GONE);
                    monthOfTreatment.setVisibility(View.GONE);
                    submitButton.setEnabled(false);
                }
            }*/
        } else if (radioGroup == xrayDone.getRadioGroup()) {

            String value = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Order", "ORDER ID", App.get(orderIds), "TYPE OF X RAY");
            if (value != null) {

                cat4tbScore.setVisibility(View.GONE);

                radiologicalDiagnosis.setVisibility(View.GONE);
                abnormalDetailedDiagnosis.setVisibility(View.GONE);
                abnormalDetailedDiagnosisOther.setVisibility(View.GONE);

                extentOfDisease.setVisibility(View.GONE);
                radiologistRemarks.setVisibility(View.GONE);
                testId.setVisibility(View.GONE);
                compare_xray.setVisibility(View.GONE);
                maximum_cavity.setVisibility(View.GONE);
                fibrosis.setVisibility(View.GONE);

                if (App.get(xrayDone).equals(getString(R.string.yes))) {

                    testId.setVisibility(View.VISIBLE);
                    String cxrReason = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Order", "ORDER ID", App.get(orderIds), "REASON FOR X-RAY");

                    radiologistRemarks.setVisibility(View.VISIBLE);
                    extentOfDisease.setVisibility(View.VISIBLE);
                }

                if (value.equals("CHEST XRAY") && App.get(xrayDone).equals(getString(R.string.yes))) {
                    cat4tbScore.setVisibility(View.VISIBLE);


                    radiologicalDiagnosis.getRadioGroup().clearCheck();
                    radiologicalDiagnosis.setVisibility(View.GONE);
                    abnormalDetailedDiagnosis.setVisibility(View.GONE);
                    abnormalDetailedDiagnosisOther.getEditText().setText(null);
                    abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
                } else if (value.equals("X-RAY, OTHER") && App.get(xrayDone).equals(getString(R.string.yes))) {
                    cat4tbScore.setVisibility(View.GONE);
                    cat4tbScore.getEditText().setText(null);


                    compare_xray.setVisibility(View.VISIBLE);
                    maximum_cavity.setVisibility(View.VISIBLE);
                    fibrosis.setVisibility(View.VISIBLE);

                    radiologicalDiagnosis.setVisibility(View.VISIBLE);
                    if (radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_suggestive_of_tb)) || radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_not_suggestive_of_tb))) {
                        abnormalDetailedDiagnosis.setVisibility(View.VISIBLE);
                        for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()) {
                            if (App.get(cb).equals(getResources().getString(R.string.fast_others))) {
                                if (cb.isChecked()) {
                                    abnormalDetailedDiagnosisOther.setVisibility(View.VISIBLE);
                                } else {
                                    abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                    extentOfDisease.setVisibility(View.VISIBLE);
                }

            }

        }
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageCount;
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
}
