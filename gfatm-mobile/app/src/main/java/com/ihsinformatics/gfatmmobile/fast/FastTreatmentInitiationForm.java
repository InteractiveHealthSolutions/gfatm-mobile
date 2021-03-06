package com.ihsinformatics.gfatmmobile.fast;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyEditText;
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
 * Created by Haris on 2/15/2017.
 */

public class FastTreatmentInitiationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    public static final int THIRD_DATE_DIALOG_ID = 3;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;
    Boolean dateChoose = false;
    boolean refillFlag = false;
    Boolean emptyError = false;
    Context context;

    // Views...

    TitledRadioGroup tbPatient;
    TitledRadioGroup antibiotic;


    TitledButton regDate;
    LinearLayout cnicLinearLayout;
    MyEditText cnic1;
    MyEditText cnic2;
    MyEditText cnic3;
    TitledSpinner cnicOwner;
    TitledEditText cnicOwnerOther;

    TitledEditText tbRegisterationNumber;
    TitledCheckBoxes diagonosisType;
    TitledRadioGroup tbType;
    TitledSpinner extraPulmonarySite;
    TitledEditText extraPulmonarySiteOther;
    TitledSpinner patientType;
    TitledRadioGroup treatmentInitiated;
    TitledSpinner reasonTreatmentNotIniated;
    TitledEditText reasonTreatmentNotInitiatedOther;
    TitledRadioGroup tbCategory;
    TitledRadioGroup historyCategory;
    TitledSpinner outcomePreviousCategory;
    TitledEditText weight;
    TitledButton returnVisitDate;
    MyTextView treatInitInstruction;

    Snackbar snackbar;

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
        formName = Forms.FAST_TREATMENT_INITIATION_FORM;
        form = Forms.fastTreatmentInitiationForm;

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

        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        regDate = new TitledButton(context, null, getResources().getString(R.string.fast_registeration_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);

        tbPatient = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_does_the_patient_have_tb), getResources().getStringArray(R.array.fast_tb_patient_list), "", App.VERTICAL, App.VERTICAL, true, "PATIENT HAVE TB", new String[]{"YES", "INCONCLUSIVE"});
        antibiotic = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_are_you_prescribing_an_antibiotic_trial), getResources().getStringArray(R.array.fast_yes_no_list), "", App.VERTICAL, App.VERTICAL, true, "ANTIBIOTIC GIVEN", getResources().getStringArray(R.array.yes_no_list_concept));

        cnicLinearLayout = new LinearLayout(context);
        cnicLinearLayout.setOrientation(LinearLayout.VERTICAL);
        MyTextView cnic = new MyTextView(context, getResources().getString(R.string.fast_nic_number));
        cnicLinearLayout.addView(cnic);
        LinearLayout cnicPartLayout = new LinearLayout(context);
        cnicPartLayout.setOrientation(LinearLayout.HORIZONTAL);
        cnic1 = new MyEditText(context, "", 5, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE);
        cnic1.setHint("XXXXX");
        cnicPartLayout.addView(cnic1);
        MyTextView cnicDash = new MyTextView(context, " - ");
        cnicPartLayout.addView(cnicDash);
        cnic2 = new MyEditText(context, "", 7, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE);
        cnic2.setHint("XXXXXXX");
        cnicPartLayout.addView(cnic2);
        MyTextView cnicDash2 = new MyTextView(context, " - ");
        cnicPartLayout.addView(cnicDash2);
        cnic3 = new MyEditText(context, "", 1, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE);
        cnic3.setHint("X");
        cnicPartLayout.addView(cnic3);
        cnicLinearLayout.addView(cnicPartLayout);

        cnicOwner = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_cnic), getResources().getStringArray(R.array.fast_whose_nic_is_this_list), getResources().getString(R.string.fast_self), App.VERTICAL, false, "COMPUTERIZED NATIONAL IDENTIFICATION OWNER", new String[]{"SELF", "MOTHER", "FATHER", "SISTER", "BROTHER", "SPOUSE", "PATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "MATERNAL GRANDMOTHER", "UNCLE", "AUNT", "SON", "DAUGHTER", "OTHER FAMILY MEMBER"});
        cnicOwnerOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER");
        tbRegisterationNumber = new TitledEditText(context, null, getResources().getString(R.string.fast_tb_registeration_no), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false,"TB REGISTRATION NUMBER");
        diagonosisType = new TitledCheckBoxes(context, null, getResources().getString(R.string.fast_type_of_diagnosis), getResources().getStringArray(R.array.fast_diagonosis_type_list), new Boolean[]{true, false}, App.VERTICAL, App.VERTICAL, true,"TUBERCULOSIS DIAGNOSIS METHOD", new String[]{"PRIMARY RESPIRATORY TUBERCULOSIS, CONFIRMED BACTERIOLOGICALLY","CLINICAL SUSPICION"});
        tbType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_what_type_of_tb), getResources().getStringArray(R.array.fast_tb_type_list), getResources().getString(R.string.fast_pulmonary), App.VERTICAL, App.VERTICAL,false,"SITE OF TUBERCULOSIS DISEASE", new String[]{"PULMONARY TUBERCULOSIS","EXTRA-PULMONARY TUBERCULOSIS"});
        extraPulmonarySite = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_site_of_extra_pulmonary), getResources().getStringArray(R.array.fast_extra_pulmonary_site_list), getResources().getString(R.string.fast_lymph_node), App.VERTICAL,true,"EXTRA PULMONARY SITE", new String[]{"LYMPH NODE SARCOIDOSIS", "ABDOMEN", "ACUTE LYMPHOBLASTIC LEUKEMIA WITH CENTRAL NERVOUS SYSTEM INVOLVEMENT", "RENAL DISEASE", "TUBERCULOSIS OF BONES AND JOINTS", "GENITOURINARY TUBERCULOSIS", "PLEURAL EFFUSION", "OTHER EXTRA PULMONARY SITE"});
        extraPulmonarySiteOther = new TitledEditText(context, null, getResources().getString(R.string.other_extra_pulmonary_tb_site), "", "", 100, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER EXTRA PULMONARY SITE");
        patientType = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_patient_type), getResources().getStringArray(R.array.fast_patient_type_list), getResources().getString(R.string.fast_new), App.VERTICAL,false,"TB PATIENT TYPE", new String[]{"NEW TB PATIENT", "RELAPSE", "PATIENT REFERRED", "LOST TO FOLLOW-UP", "TUBERCULOSIS TREATMENT FAILURE", "OTHER PATIENT TYPE"});
        treatmentInitiated = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_was_treatment_initiated), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL,true,"TREATMENT INITIATED",getResources().getStringArray(R.array.yes_no_list_concept));
        reasonTreatmentNotIniated = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_reason_the_treatment_was_not_iniated), getResources().getStringArray(R.array.fast_reason_treatment_notinitiated_list), getResources().getString(R.string.fast_patient_refused_treatment), App.VERTICAL,true,"TREATMENT NOT STARTED", new String[]{"REFUSAL OF TREATMENT BY PATIENT", "LOST TO FOLLOW-UP", "DIED", "PATIENT REFERRED", "TREATMENT NOT INITIATED OTHER REASON"});
        reasonTreatmentNotInitiatedOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"TREATMENT NOT INITIATED OTHER REASON");
        tbCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_category), getResources().getStringArray(R.array.fast_tb_category_list), getResources().getString(R.string.fast_category1), App.VERTICAL, App.VERTICAL,false,"TB CATEGORY", new String[]{"CATEGORY I TUBERCULOSIS", "CATEGORY II TUBERCULOSIS"});
        historyCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_if_category_2_history_of_previous), getResources().getStringArray(R.array.fast_history_category_2_list), getResources().getString(R.string.fast_cat_1), App.VERTICAL, App.VERTICAL,false,"HISTORY OF PREVIOUSLY ANTI TUBERCULOSIS TREATMENT", new String[]{"CATEGORY I TUBERCULOSIS","CATEGORY II TUBERCULOSIS","CAT I & II TUBERCULOSIS" , "OTHER TUBERCULOSIS CATEGORY"});
        outcomePreviousCategory = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_if_category_2_outcome_of_previous), getResources().getStringArray(R.array.fast_outcome_previous_category_list), getResources().getString(R.string.fast_cured), App.VERTICAL,false,"OUTCOME OF PREVIOUS CAT II TUBERCULOSIS TREATMENT", new String[]{"CURE, OUTCOME", "TREATMENT COMPLETE", "TUBERCULOSIS TREATMENT FAILURE", "TRANSFERRED OUT", "LOST TO FOLLOW-UP", "OTHER TREATMENT OUTCOME"});
        weight = new TitledEditText(context, null, getResources().getString(R.string.fast_patient_weight), "", "", 3, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, false,"WEIGHT (KG)");
        thirdDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
        thirdDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
        thirdDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
        thirdDateCalendar.add(Calendar.DAY_OF_MONTH, 30);
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.fast_next_appointment_date), DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        treatInitInstruction = new MyTextView(context, getResources().getString(R.string.fast_treatment_init_instruction));
        treatInitInstruction.setTextColor(Color.BLACK);
        treatInitInstruction.setTypeface(null, Typeface.NORMAL);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), regDate.getButton(), cnic1, cnic2, cnic3,
                cnicOwner.getSpinner(), cnicOwnerOther.getEditText(), tbRegisterationNumber.getEditText(), diagonosisType, tbType.getRadioGroup(),
                extraPulmonarySite.getSpinner(), extraPulmonarySiteOther.getEditText(), patientType.getSpinner(), treatmentInitiated.getRadioGroup(),
                reasonTreatmentNotIniated.getSpinner(), reasonTreatmentNotInitiatedOther.getEditText(), tbCategory.getRadioGroup(),
                historyCategory.getRadioGroup(), outcomePreviousCategory.getSpinner(), weight.getEditText(), returnVisitDate.getButton(), tbPatient.getRadioGroup(), antibiotic.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, tbPatient, antibiotic, diagonosisType, treatmentInitiated, reasonTreatmentNotIniated, reasonTreatmentNotInitiatedOther,
                        cnicLinearLayout, cnicOwner, cnicOwnerOther, tbType, extraPulmonarySite, extraPulmonarySiteOther, patientType,
                        tbCategory, historyCategory, outcomePreviousCategory, regDate, tbRegisterationNumber, weight, returnVisitDate, treatInitInstruction}};

        formDate.getButton().setOnClickListener(this);
        regDate.getButton().setOnClickListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        extraPulmonarySite.getSpinner().setOnItemSelectedListener(this);
        tbType.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentInitiated.getRadioGroup().setOnCheckedChangeListener(this);
        reasonTreatmentNotIniated.getSpinner().setOnItemSelectedListener(this);
        tbCategory.getRadioGroup().setOnCheckedChangeListener(this);
        cnicOwner.getSpinner().setOnItemSelectedListener(this);
        tbPatient.getRadioGroup().setOnCheckedChangeListener(this);
        antibiotic.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();


        cnic1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 5) {
                    cnic2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cnic2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 7) {
                    cnic3.requestFocus();
                }

                if (s.length() == 0) {
                    cnic1.requestFocus();
                    cnic1.setSelection(cnic1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cnic3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    cnic2.requestFocus();
                    cnic2.setSelection(cnic2.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void updateDisplay() {

        if (refillFlag) {
            refillFlag = false;
            return;
        }

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
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }
        if (!(regDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = regDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                regDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                regDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                regDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }

        if (!dateChoose) {
            Calendar requiredDate = formDateCalendar.getInstance();
            requiredDate.setTime(formDateCalendar.getTime());
            requiredDate.add(Calendar.DATE, 30);

            if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                thirdDateCalendar.setTime(requiredDate.getTime());
            } else {
                requiredDate.add(Calendar.DATE, -1);
                thirdDateCalendar.setTime(requiredDate.getTime());
            }
        }


        String nextAppointmentDateString = App.getSqlDate(thirdDateCalendar);
        Date nextAppointmentDate = App.stringToDate(nextAppointmentDateString, "yyyy-MM-dd");

        String treatStartDateString = App.getSqlDate(secondDateCalendar);
        Date treatmentStDate = App.stringToDate(treatStartDateString, "yyyy-MM-dd");


        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString()))) {

            String formDa = returnVisitDate.getButton().getText().toString();

            //Date date = new Date();
            if (thirdDateCalendar.before(formDateCalendar)) {

                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

            } else if (thirdDateCalendar.before(secondDateCalendar)) {

                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_next_appointment_date_cant_be_before_registeration_date), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            } else if (nextAppointmentDate.compareTo(treatmentStDate) == 0) {
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_registeration_date_and_next_visit_date_cant_be_same), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            } else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
        }
        dateChoose = false;
        formDate.getButton().setEnabled(true);
        regDate.getButton().setEnabled(true);
        returnVisitDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = super.validate();

        if (tbPatient.getVisibility() == View.VISIBLE && App.get(tbPatient).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            error = true;
        }

        boolean check = false;
        for (CheckBox cb : diagonosisType.getCheckedBoxes()) {
            if (cb.isChecked()) {
                check = true;
                break;
            }
        }

        if (diagonosisType.getVisibility() == View.VISIBLE && check == false) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            diagonosisType.getQuestionView().setError(getString(R.string.empty_field));
            diagonosisType.requestFocus();
            error = true;
        }

        if (antibiotic.getVisibility() == View.VISIBLE && App.get(antibiotic).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            error = true;
        }

        if (cnicLinearLayout.getVisibility() == View.VISIBLE && cnic1.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic1.setError(getString(R.string.empty_field));
            cnic1.requestFocus();
            error = true;
        }

        if (cnicLinearLayout.getVisibility() == View.VISIBLE && cnic2.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic2.setError(getString(R.string.empty_field));
            cnic2.requestFocus();
            error = true;
        }

        if (cnicLinearLayout.getVisibility() == View.VISIBLE && cnic3.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic3.setError(getString(R.string.empty_field));
            cnic3.requestFocus();
            error = true;
        }

        if (cnicLinearLayout.getVisibility() == View.VISIBLE && App.get(cnic1).length() != 5) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic1.setError(getString(R.string.length_message));
            cnic1.requestFocus();
            error = true;
        }

        if (cnicLinearLayout.getVisibility() == View.VISIBLE && App.get(cnic2).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic2.setError(getString(R.string.length_message));
            cnic2.requestFocus();
            error = true;
        }

        if (cnicLinearLayout.getVisibility() == View.VISIBLE && App.get(cnic3).length() != 1) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic3.setError(getString(R.string.length_message));
            cnic3.requestFocus();
            error = true;
        }

        if (cnicOwnerOther.getVisibility() == View.VISIBLE && cnicOwnerOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnicOwnerOther.getEditText().setError(getString(R.string.empty_field));
            cnicOwnerOther.getEditText().requestFocus();
            error = true;
        }


      /*  if (tbRegisterationNumber.getVisibility() == View.VISIBLE && tbRegisterationNumber.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tbRegisterationNumber.getEditText().setError(getString(R.string.empty_field));
            tbRegisterationNumber.getEditText().requestFocus();
            error = true;
        }*/

        if (tbRegisterationNumber.getEditText().getText().toString().length() > 0 && tbRegisterationNumber.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tbRegisterationNumber.getEditText().setError(getString(R.string.invalid_value));
            tbRegisterationNumber.getEditText().requestFocus();
            error = true;
        }


        if (extraPulmonarySiteOther.getVisibility() == View.VISIBLE && extraPulmonarySiteOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            extraPulmonarySiteOther.getEditText().setError(getString(R.string.empty_field));
            extraPulmonarySiteOther.getEditText().requestFocus();
            error = true;
        }


        if (reasonTreatmentNotInitiatedOther.getVisibility() == View.VISIBLE && reasonTreatmentNotInitiatedOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reasonTreatmentNotInitiatedOther.getEditText().setError(getString(R.string.empty_field));
            reasonTreatmentNotInitiatedOther.getEditText().requestFocus();
            error = true;
        }


        if (weight.getVisibility() == View.VISIBLE && weight.getEditText().getText().toString().length() > 0 && weight.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            weight.getEditText().setError(getString(R.string.empty_field));
            weight.getEditText().requestFocus();
            error = true;
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.form_error));
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

    @Override
    public boolean submit() {
        final HashMap<String, String> personAttribute = new HashMap<String, String>();
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


        String cnicNumber = cnic1.getText().toString() + "-" + cnic2.getText().toString() + "-" + cnic3.getText().toString();


        if (regDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REGISTRATION DATE", App.getSqlDateTime(secondDateCalendar)});

        if (cnicNumber.length() == 15) {
            observations.add(new String[]{"NATIONAL IDENTIFICATION NUMBER", cnicNumber});
            personAttribute.put("National ID", cnicNumber);
        }

        String ownerString = "";
        if (cnicOwner.getVisibility() == View.VISIBLE) {
            ownerString = App.get(cnicOwner).equals(getResources().getString(R.string.pet_self)) ? "SELF" :
                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_mother)) ? "MOTHER" :
                            (App.get(cnicOwner).equals(getResources().getString(R.string.pet_father)) ? "FATHER" :
                                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_sister)) ? "SISTER" :
                                            (App.get(cnicOwner).equals(getResources().getString(R.string.pet_brother)) ? "BROTHER" :
                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_spouse)) ? "SPOUSE" :
                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.pet_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.pet_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.pet_uncle)) ? "UNCLE" :
                                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_aunt)) ? "AUNT" :
                                                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.pet_son)) ? "SON" :
                                                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_daughter)) ? "DAUGHTER" :

                                                                                                                            "OTHER FAMILY MEMBER")))))))))))));

            String[][] cnicOwnerConcept = serverService.getConceptUuidAndDataType(ownerString);
            personAttribute.put("National ID Owner", cnicOwnerConcept[0][0]);
        }




        if (returnVisitDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(thirdDateCalendar)});

        personAttribute.put("Health Center", serverService.getLocationUuid(App.getLocation()));

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

                String id = null;
                if (App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocally("FAST-Treatment Initiation", form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveMultiplePersonAttribute(personAttribute, id);
                if (!result.contains("SUCCESS"))
                    return result;

                if (!App.get(tbRegisterationNumber).isEmpty() && tbRegisterationNumber.getVisibility() == View.VISIBLE) {
                    result = serverService.saveIdentifier("District TB Number", App.get(tbRegisterationNumber), id);
                    if (!result.contains("SUCCESS"))
                        return result;
                }

                result = serverService.saveEncounterAndObservationTesting("FAST-Treatment Initiation", form, formDateCalendar, observations.toArray(new String[][]{}), id);
                if (!result.contains("SUCCESS"))
                    return result;

                return "SUCCESS";

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

    @Override
    public void refill(int formId) {
        refillFlag = true;
        super.refill(formId);

        OfflineForm fo = serverService.getSavedFormById(formId);

        ArrayList<String[][]> obsValue = fo.getObsValue();


        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
           if (obs[0][0].equals("REGISTRATION DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                regDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                regDate.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("NATIONAL IDENTIFICATION NUMBER")) {
                String data = obs[0][1];
                cnic1.setText(data.substring(0, 5));
                cnic2.setText(data.substring(6, 13));
                cnic3.setText(data.substring(14));
            }
            else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                thirdDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
                returnVisitDate.setVisibility(View.VISIBLE);

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

        if (view == regDate.getButton()) {
            regDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, false, true, true);

            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);*/
        }

        if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            showDateDialog(thirdDateCalendar, true, false, true);

            /*Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;

        if (spinner == extraPulmonarySite.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                extraPulmonarySiteOther.setVisibility(View.VISIBLE);
            } else {
                extraPulmonarySiteOther.setVisibility(View.GONE);
            }
        } else if (spinner == reasonTreatmentNotIniated.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                reasonTreatmentNotInitiatedOther.setVisibility(View.VISIBLE);
            } else {
                reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_patient_died)) || parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_patient_refused_treatment))) {
                returnVisitDate.setVisibility(View.GONE);
                treatInitInstruction.setVisibility(View.GONE);
            } else {
                returnVisitDate.setVisibility(View.VISIBLE);
                treatInitInstruction.setVisibility(View.VISIBLE);
            }
        } else if (spinner == cnicOwner.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                cnicOwnerOther.setVisibility(View.VISIBLE);
            } else {
                cnicOwnerOther.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == tbType.getRadioGroup()) {
            if (tbType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                extraPulmonarySite.setVisibility(View.VISIBLE);
                if (extraPulmonarySite.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    extraPulmonarySiteOther.setVisibility(View.VISIBLE);
                } else {
                    extraPulmonarySiteOther.setVisibility(View.GONE);
                }
            } else {
                extraPulmonarySite.setVisibility(View.GONE);
                extraPulmonarySiteOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == treatmentInitiated.getRadioGroup()) {
            if (treatmentInitiated.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                reasonTreatmentNotIniated.setVisibility(View.VISIBLE);
                if (reasonTreatmentNotIniated.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    reasonTreatmentNotInitiatedOther.setVisibility(View.VISIBLE);
                } else {
                    reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
                }

                cnicLinearLayout.setVisibility(View.GONE);
                cnicOwner.setVisibility(View.GONE);
                cnicOwnerOther.setVisibility(View.GONE);
                tbType.setVisibility(View.GONE);
                extraPulmonarySite.setVisibility(View.GONE);
                extraPulmonarySiteOther.setVisibility(View.GONE);
                patientType.setVisibility(View.GONE);
                tbCategory.setVisibility(View.GONE);
                historyCategory.setVisibility(View.GONE);
                regDate.setVisibility(View.GONE);
                outcomePreviousCategory.setVisibility(View.GONE);
                tbRegisterationNumber.setVisibility(View.GONE);
                weight.setVisibility(View.GONE);
            } else {
                reasonTreatmentNotIniated.setVisibility(View.GONE);
                reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);

                cnicLinearLayout.setVisibility(View.VISIBLE);
                cnicOwner.setVisibility(View.VISIBLE);
                if (cnicOwner.getSpinner().getSelectedItem().toString().equals(getResources().getString(R.string.fast_other_title))) {
                    cnicOwnerOther.setVisibility(View.VISIBLE);
                } else {
                    cnicOwnerOther.setVisibility(View.GONE);
                }
                tbType.setVisibility(View.VISIBLE);
                if (tbType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                    extraPulmonarySite.setVisibility(View.VISIBLE);
                    if (extraPulmonarySite.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                        extraPulmonarySiteOther.setVisibility(View.VISIBLE);
                    } else {
                        extraPulmonarySiteOther.setVisibility(View.GONE);
                    }
                } else {
                    extraPulmonarySite.setVisibility(View.GONE);
                    extraPulmonarySiteOther.setVisibility(View.GONE);
                }

                if (tbPatient.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                    diagonosisType.setVisibility(View.VISIBLE);
                    treatmentInitiated.setVisibility(View.VISIBLE);
                    patientType.setVisibility(View.VISIBLE);
                    tbCategory.setVisibility(View.VISIBLE);

                    if (tbCategory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_category2))) {
                        historyCategory.setVisibility(View.VISIBLE);
                        outcomePreviousCategory.setVisibility(View.VISIBLE);
                    } else {
                        historyCategory.setVisibility(View.GONE);
                        outcomePreviousCategory.setVisibility(View.GONE);
                    }
                    regDate.setVisibility(View.VISIBLE);
                    tbRegisterationNumber.setVisibility(View.VISIBLE);
                } else {
                    diagonosisType.setVisibility(View.GONE);
                    treatmentInitiated.setVisibility(View.GONE);
                    patientType.setVisibility(View.GONE);
                    tbCategory.setVisibility(View.GONE);
                    historyCategory.setVisibility(View.GONE);
                    outcomePreviousCategory.setVisibility(View.GONE);
                    regDate.setVisibility(View.GONE);
                    tbRegisterationNumber.setVisibility(View.GONE);
                }
                returnVisitDate.setVisibility(View.VISIBLE);
                treatInitInstruction.setVisibility(View.VISIBLE);
                weight.setVisibility(View.VISIBLE);
            }
        } else if (radioGroup == tbCategory.getRadioGroup()) {
            if (tbCategory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_category2))) {
                historyCategory.setVisibility(View.VISIBLE);
                outcomePreviousCategory.setVisibility(View.VISIBLE);
            } else {
                historyCategory.setVisibility(View.GONE);
                outcomePreviousCategory.setVisibility(View.GONE);
            }
        } else if (radioGroup == tbPatient.getRadioGroup()) {
            if (tbPatient.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                antibiotic.setVisibility(View.GONE);
                diagonosisType.setVisibility(View.VISIBLE);
                treatmentInitiated.setVisibility(View.VISIBLE);

                if (treatmentInitiated.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                    reasonTreatmentNotIniated.setVisibility(View.VISIBLE);
                    if (reasonTreatmentNotIniated.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                        reasonTreatmentNotInitiatedOther.setVisibility(View.VISIBLE);
                    } else {
                        reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
                    }

                    cnicLinearLayout.setVisibility(View.GONE);
                    cnicOwner.setVisibility(View.GONE);
                    cnicOwnerOther.setVisibility(View.GONE);
                    tbType.setVisibility(View.GONE);
                    extraPulmonarySite.setVisibility(View.GONE);
                    extraPulmonarySiteOther.setVisibility(View.GONE);
                    patientType.setVisibility(View.GONE);
                    tbCategory.setVisibility(View.GONE);
                    historyCategory.setVisibility(View.GONE);
                    outcomePreviousCategory.setVisibility(View.GONE);
                    regDate.setVisibility(View.GONE);
                    tbRegisterationNumber.setVisibility(View.GONE);
                    weight.setVisibility(View.GONE);
                } else {
                    reasonTreatmentNotIniated.setVisibility(View.GONE);
                    reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);

                    cnicLinearLayout.setVisibility(View.VISIBLE);
                    cnicOwner.setVisibility(View.VISIBLE);
                    if (cnicOwner.getSpinner().getSelectedItem().toString().equals(getResources().getString(R.string.fast_other_title))) {
                        cnicOwnerOther.setVisibility(View.VISIBLE);
                    } else {
                        cnicOwnerOther.setVisibility(View.GONE);
                    }
                    tbType.setVisibility(View.VISIBLE);
                    if (tbType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                        extraPulmonarySite.setVisibility(View.VISIBLE);
                        if (extraPulmonarySite.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                            extraPulmonarySiteOther.setVisibility(View.VISIBLE);
                        } else {
                            extraPulmonarySiteOther.setVisibility(View.GONE);
                        }
                    } else {
                        extraPulmonarySite.setVisibility(View.GONE);
                        extraPulmonarySiteOther.setVisibility(View.GONE);
                    }

                    if (tbPatient.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                        diagonosisType.setVisibility(View.VISIBLE);
                        treatmentInitiated.setVisibility(View.VISIBLE);
                        patientType.setVisibility(View.VISIBLE);
                        tbCategory.setVisibility(View.VISIBLE);

                        if (tbCategory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_category2))) {
                            historyCategory.setVisibility(View.VISIBLE);
                            outcomePreviousCategory.setVisibility(View.VISIBLE);
                        } else {
                            historyCategory.setVisibility(View.GONE);
                            outcomePreviousCategory.setVisibility(View.GONE);
                        }
                        regDate.setVisibility(View.VISIBLE);
                        tbRegisterationNumber.setVisibility(View.VISIBLE);
                    } else {
                        diagonosisType.setVisibility(View.GONE);
                        treatmentInitiated.setVisibility(View.GONE);
                        patientType.setVisibility(View.GONE);
                        tbCategory.setVisibility(View.GONE);
                        historyCategory.setVisibility(View.GONE);
                        outcomePreviousCategory.setVisibility(View.GONE);
                        regDate.setVisibility(View.GONE);
                        tbRegisterationNumber.setVisibility(View.GONE);
                    }
                    returnVisitDate.setVisibility(View.VISIBLE);
                    treatInitInstruction.setVisibility(View.VISIBLE);
                    weight.setVisibility(View.VISIBLE);
                }

            } else if (tbPatient.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_inconclusive))) {

                diagonosisType.setVisibility(View.GONE);
                treatmentInitiated.setVisibility(View.GONE);
                reasonTreatmentNotIniated.setVisibility(View.GONE);
                reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
                returnVisitDate.setVisibility(View.VISIBLE);
                treatInitInstruction.setVisibility(View.VISIBLE);
                cnicLinearLayout.setVisibility(View.VISIBLE);
                cnicOwner.setVisibility(View.VISIBLE);
                if (cnicOwner.getSpinner().getSelectedItem().toString().equals(getResources().getString(R.string.fast_other_title))) {
                    cnicOwnerOther.setVisibility(View.VISIBLE);
                } else {
                    cnicOwnerOther.setVisibility(View.GONE);
                }
                tbType.setVisibility(View.VISIBLE);
                if (tbType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                    extraPulmonarySite.setVisibility(View.VISIBLE);
                    if (extraPulmonarySite.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                        extraPulmonarySiteOther.setVisibility(View.VISIBLE);
                    } else {
                        extraPulmonarySiteOther.setVisibility(View.GONE);
                    }
                } else {
                    extraPulmonarySite.setVisibility(View.GONE);
                    extraPulmonarySiteOther.setVisibility(View.GONE);
                }

                if (tbPatient.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                    diagonosisType.setVisibility(View.VISIBLE);
                    treatmentInitiated.setVisibility(View.VISIBLE);
                    patientType.setVisibility(View.VISIBLE);
                    tbCategory.setVisibility(View.VISIBLE);

                    if (tbCategory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_category2))) {
                        historyCategory.setVisibility(View.VISIBLE);
                        outcomePreviousCategory.setVisibility(View.VISIBLE);
                    } else {
                        historyCategory.setVisibility(View.GONE);
                        outcomePreviousCategory.setVisibility(View.GONE);
                    }
                    regDate.setVisibility(View.VISIBLE);
                    tbRegisterationNumber.setVisibility(View.VISIBLE);
                } else {
                    diagonosisType.setVisibility(View.GONE);
                    treatmentInitiated.setVisibility(View.GONE);
                    patientType.setVisibility(View.GONE);
                    tbCategory.setVisibility(View.GONE);
                    historyCategory.setVisibility(View.GONE);
                    outcomePreviousCategory.setVisibility(View.GONE);
                    regDate.setVisibility(View.GONE);
                    tbRegisterationNumber.setVisibility(View.GONE);
                }

                weight.setVisibility(View.VISIBLE);


                patientType.setVisibility(View.GONE);
                tbCategory.setVisibility(View.GONE);
                historyCategory.setVisibility(View.GONE);
                outcomePreviousCategory.setVisibility(View.GONE);
                regDate.setVisibility(View.GONE);
                tbRegisterationNumber.setVisibility(View.GONE);


                antibiotic.setVisibility(View.VISIBLE);
                if (antibiotic.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                    cnicLinearLayout.setVisibility(View.GONE);
                    cnicOwner.setVisibility(View.GONE);
                    cnicOwnerOther.setVisibility(View.GONE);
                    tbType.setVisibility(View.GONE);
                    extraPulmonarySite.setVisibility(View.GONE);
                    extraPulmonarySiteOther.setVisibility(View.GONE);
                    patientType.setVisibility(View.GONE);
                    tbCategory.setVisibility(View.GONE);
                    historyCategory.setVisibility(View.GONE);
                    outcomePreviousCategory.setVisibility(View.GONE);
                    regDate.setVisibility(View.GONE);
                    tbRegisterationNumber.setVisibility(View.GONE);
                    weight.setVisibility(View.GONE);
                    returnVisitDate.setVisibility(View.VISIBLE);
                    treatInitInstruction.setVisibility(View.VISIBLE);
                } else {
                   /* cnicLinearLayout.setVisibility(View.VISIBLE);
                    cnicOwner.setVisibility(View.VISIBLE);
                    if (cnicOwner.getSpinner().getSelectedItem().toString().equals(getResources().getString(R.string.fast_other_title))) {
                        cnicOwnerOther.setVisibility(View.VISIBLE);
                    } else {
                        cnicOwnerOther.setVisibility(View.GONE);
                    }
                    tbType.setVisibility(View.VISIBLE);
                    if (tbType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                        extraPulmonarySite.setVisibility(View.VISIBLE);
                        if (extraPulmonarySite.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                            extraPulmonarySiteOther.setVisibility(View.VISIBLE);
                        } else {
                            extraPulmonarySiteOther.setVisibility(View.GONE);
                        }
                    } else {
                        extraPulmonarySite.setVisibility(View.GONE);
                        extraPulmonarySiteOther.setVisibility(View.GONE);
                    }

                    if (tbPatient.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                        diagonosisType.setVisibility(View.VISIBLE);
                        treatmentInitiated.setVisibility(View.VISIBLE);
                        patientType.setVisibility(View.VISIBLE);
                        tbCategory.setVisibility(View.VISIBLE);

                        if (tbCategory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_category2))) {
                            historyCategory.setVisibility(View.VISIBLE);
                            outcomePreviousCategory.setVisibility(View.VISIBLE);
                        } else {
                            historyCategory.setVisibility(View.GONE);
                            outcomePreviousCategory.setVisibility(View.GONE);
                        }
                        regDate.setVisibility(View.VISIBLE);
                        tbRegisterationNumber.setVisibility(View.VISIBLE);
                    } else {
                        diagonosisType.setVisibility(View.GONE);
                        treatmentInitiated.setVisibility(View.GONE);
                        patientType.setVisibility(View.GONE);
                        tbCategory.setVisibility(View.GONE);
                        historyCategory.setVisibility(View.GONE);
                        outcomePreviousCategory.setVisibility(View.GONE);
                        regDate.setVisibility(View.GONE);
                        tbRegisterationNumber.setVisibility(View.GONE);
                    }

                    weight.setVisibility(View.VISIBLE);*/

                    cnicLinearLayout.setVisibility(View.GONE);
                    cnicOwner.setVisibility(View.GONE);
                    cnicOwnerOther.setVisibility(View.GONE);
                    tbType.setVisibility(View.GONE);
                    extraPulmonarySite.setVisibility(View.GONE);
                    extraPulmonarySiteOther.setVisibility(View.GONE);
                    patientType.setVisibility(View.GONE);
                    tbCategory.setVisibility(View.GONE);
                    historyCategory.setVisibility(View.GONE);
                    outcomePreviousCategory.setVisibility(View.GONE);
                    regDate.setVisibility(View.GONE);
                    tbRegisterationNumber.setVisibility(View.GONE);
                    weight.setVisibility(View.GONE);
                    returnVisitDate.setVisibility(View.GONE);
                    treatInitInstruction.setVisibility(View.GONE);
                }

            } else if (tbPatient.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                antibiotic.setVisibility(View.GONE);
                diagonosisType.setVisibility(View.GONE);
                treatmentInitiated.setVisibility(View.GONE);
                reasonTreatmentNotIniated.setVisibility(View.GONE);
                reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
                patientType.setVisibility(View.GONE);
                cnicLinearLayout.setVisibility(View.VISIBLE);
                cnicOwner.setVisibility(View.VISIBLE);
                tbCategory.setVisibility(View.GONE);
                historyCategory.setVisibility(View.GONE);
                outcomePreviousCategory.setVisibility(View.GONE);
                tbRegisterationNumber.setVisibility(View.GONE);
                regDate.setVisibility(View.GONE);

                if (cnicOwner.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    cnicOwnerOther.setVisibility(View.VISIBLE);
                } else {
                    cnicOwnerOther.setVisibility(View.GONE);
                }

                tbType.setVisibility(View.VISIBLE);
                if (tbType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                    extraPulmonarySite.setVisibility(View.VISIBLE);
                    if (extraPulmonarySite.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                        extraPulmonarySiteOther.setVisibility(View.VISIBLE);
                    } else {
                        extraPulmonarySiteOther.setVisibility(View.GONE);
                    }
                } else {
                    extraPulmonarySite.setVisibility(View.GONE);
                    extraPulmonarySiteOther.setVisibility(View.GONE);
                }

                weight.setVisibility(View.VISIBLE);
                returnVisitDate.setVisibility(View.VISIBLE);
                treatInitInstruction.setVisibility(View.VISIBLE);
            }

        } else if (radioGroup == antibiotic.getRadioGroup()) {
            if (antibiotic.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                cnicLinearLayout.setVisibility(View.GONE);
                cnicOwner.setVisibility(View.GONE);
                cnicOwnerOther.setVisibility(View.GONE);
                tbType.setVisibility(View.GONE);
                extraPulmonarySite.setVisibility(View.GONE);
                extraPulmonarySiteOther.setVisibility(View.GONE);
                patientType.setVisibility(View.GONE);
                tbCategory.setVisibility(View.GONE);
                historyCategory.setVisibility(View.GONE);
                outcomePreviousCategory.setVisibility(View.GONE);
                regDate.setVisibility(View.GONE);
                tbRegisterationNumber.setVisibility(View.GONE);
                weight.setVisibility(View.GONE);
                returnVisitDate.setVisibility(View.VISIBLE);
                treatInitInstruction.setVisibility(View.VISIBLE);

            } else {
              /*  cnicLinearLayout.setVisibility(View.VISIBLE);
                cnicOwner.setVisibility(View.VISIBLE);
                if (cnicOwner.getSpinner().getSelectedItem().toString().equals(getResources().getString(R.string.fast_other_title))) {
                    cnicOwnerOther.setVisibility(View.VISIBLE);
                } else {
                    cnicOwnerOther.setVisibility(View.GONE);
                }
                tbType.setVisibility(View.VISIBLE);
                if (tbType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                    extraPulmonarySite.setVisibility(View.VISIBLE);
                    if (extraPulmonarySite.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                        extraPulmonarySiteOther.setVisibility(View.VISIBLE);
                    } else {
                        extraPulmonarySiteOther.setVisibility(View.GONE);
                    }
                } else {
                    extraPulmonarySite.setVisibility(View.GONE);
                    extraPulmonarySiteOther.setVisibility(View.GONE);
                }

                if (tbPatient.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                    diagonosisType.setVisibility(View.VISIBLE);
                    treatmentInitiated.setVisibility(View.VISIBLE);
                    patientType.setVisibility(View.VISIBLE);
                    tbCategory.setVisibility(View.VISIBLE);

                    if (tbCategory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_category2))) {
                        historyCategory.setVisibility(View.VISIBLE);
                        outcomePreviousCategory.setVisibility(View.VISIBLE);
                    } else {
                        historyCategory.setVisibility(View.GONE);
                        outcomePreviousCategory.setVisibility(View.GONE);
                    }
                    regDate.setVisibility(View.VISIBLE);
                    tbRegisterationNumber.setVisibility(View.VISIBLE);
                } else {
                    diagonosisType.setVisibility(View.GONE);
                    treatmentInitiated.setVisibility(View.GONE);
                    patientType.setVisibility(View.GONE);
                    tbCategory.setVisibility(View.GONE);
                    historyCategory.setVisibility(View.GONE);
                    outcomePreviousCategory.setVisibility(View.GONE);
                    regDate.setVisibility(View.GONE);
                    tbRegisterationNumber.setVisibility(View.GONE);
                }

                weight.setVisibility(View.VISIBLE);*/

                cnicLinearLayout.setVisibility(View.GONE);
                cnicOwner.setVisibility(View.GONE);
                cnicOwnerOther.setVisibility(View.GONE);
                tbType.setVisibility(View.GONE);
                extraPulmonarySite.setVisibility(View.GONE);
                extraPulmonarySiteOther.setVisibility(View.GONE);
                patientType.setVisibility(View.GONE);
                tbCategory.setVisibility(View.GONE);
                historyCategory.setVisibility(View.GONE);
                outcomePreviousCategory.setVisibility(View.GONE);
                regDate.setVisibility(View.GONE);
                tbRegisterationNumber.setVisibility(View.GONE);
                weight.setVisibility(View.GONE);
                returnVisitDate.setVisibility(View.GONE);
                treatInitInstruction.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        diagonosisType.getQuestionView().setError(null);
    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDateCalendar = Calendar.getInstance();
        secondDateCalendar = Calendar.getInstance();
        thirdDateCalendar = Calendar.getInstance();


        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        //regDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

        antibiotic.setVisibility(View.GONE);
        diagonosisType.setVisibility(View.GONE);
        treatmentInitiated.setVisibility(View.GONE);
        reasonTreatmentNotIniated.setVisibility(View.GONE);
        reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
        cnicOwnerOther.setVisibility(View.GONE);
        extraPulmonarySite.setVisibility(View.GONE);
        extraPulmonarySiteOther.setVisibility(View.GONE);
        patientType.setVisibility(View.GONE);
        tbCategory.setVisibility(View.GONE);
        historyCategory.setVisibility(View.GONE);
        outcomePreviousCategory.setVisibility(View.GONE);
        regDate.setVisibility(View.GONE);
        tbRegisterationNumber.setVisibility(View.GONE);
        returnVisitDate.setVisibility(View.GONE);

        boolean flag = true;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);
                flag = false;

            } else bundle.putBoolean("save", false);

        }

        if (flag) {
            final AsyncTask<String, String, HashMap<String, String>> autopopulateFormTask = new AsyncTask<String, String, HashMap<String, String>>() {
                @Override
                protected HashMap<String, String> doInBackground(String... params) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.setInverseBackgroundForced(true);
                            loading.setIndeterminate(true);
                            loading.setCancelable(false);
                            loading.setMessage(getResources().getString(R.string.fetching_data));
                            loading.show();
                        }
                    });

                    HashMap<String, String> result = new HashMap<String, String>();
                    String cnic1 = serverService.getLatestObsValue(App.getPatientId(), "Patient Information", "NATIONAL IDENTIFICATION NUMBER");
                    String cnicowner1 = serverService.getLatestObsValue(App.getPatientId(), "Patient Information", "COMPUTERIZED NATIONAL IDENTIFICATION OWNER");
                    String cnicownerother1 = serverService.getLatestObsValue(App.getPatientId(), "Patient Information", "OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER");
                    //String regDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Patient Information");

                    if (cnic1 != null)
                        result.put("NATIONAL IDENTIFICATION NUMBER", cnic1);
                    if (cnicowner1 != null) {
                        result.put("COMPUTERIZED NATIONAL IDENTIFICATION OWNER", cnicowner1.equals("SELF") ? getResources().getString(R.string.fast_self) :
                                (cnicowner1.equals("MOTHER") ? getResources().getString(R.string.fast_mother) :
                                        (cnicowner1.equals("FATHER") ? getResources().getString(R.string.fast_father) :
                                                (cnicowner1.equals("SISTER") ? getResources().getString(R.string.fast_sister) :
                                                        (cnicowner1.equals("BROTHER") ? getResources().getString(R.string.fast_brother) :
                                                                (cnicowner1.equals("SPOUSE") ? getResources().getString(R.string.fast_spouse) :
                                                                        (cnicowner1.equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.fast_paternal_grandfather) :
                                                                                (cnicowner1.equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.fast_paternal_grandmother) :
                                                                                        (cnicowner1.equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.fast_maternal_grandfather) :
                                                                                                (cnicowner1.equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.fast_maternal_grandmother) :
                                                                                                        (cnicowner1.equals("UNCLE") ? getResources().getString(R.string.fast_uncle) :
                                                                                                                (cnicowner1.equals("AUNT") ? getResources().getString(R.string.fast_aunt) :
                                                                                                                        (cnicowner1.equals("SON") ? getResources().getString(R.string.fast_son) :
                                                                                                                                (cnicowner1.equals("DAUGHTER") ? getResources().getString(R.string.fast_daughter) : getResources().getString(R.string.fast_other_title)))))))))))))));

                    }

                    if (cnicownerother1 != null)
                        result.put("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", cnicownerother1);

                    /*if (regDate != null)
                        result.put("form DATE", regDate);*/

                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }


                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();

                    if (result.get("NATIONAL IDENTIFICATION NUMBER") != null) {
                        String value = result.get("NATIONAL IDENTIFICATION NUMBER");
                        if (value.length() == 15) {
                            cnic1.setText(value.substring(0, 5));
                            cnic2.setText(value.substring(6, 13));
                            cnic3.setText(value.substring(14));
                        }
                    }

                    if (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER") != null) {
                        cnicOwner.getSpinner().selectValue(result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER"));
                    }

                    if (result.get("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER") != null) {
                        cnicOwnerOther.getEditText().setText(result.get("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER"));
                    }

                    /*if (result.get("form DATE") != null) {
                        String format = "";
                        String registerationDate = result.get("form DATE");
                        if (registerationDate.contains("/")) {
                            format = "dd/MM/yyyy";
                        } else {
                            format = "yyyy-MM-dd";
                        }
                        secondDateCalendar.setTime(App.stringToDate(registerationDate, format));
                        updateDisplay();
                        regDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                    }*/
                }
            };
            autopopulateFormTask.execute("");
        }

    }


    @SuppressLint("ValidFragment")
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
            if (!getArguments().getBoolean("allowFutureDate", false))
                dialog.getDatePicker().setMaxDate(new Date().getTime());
            if (!getArguments().getBoolean("allowPastDate", false))
                dialog.getDatePicker().setMinDate(new Date().getTime());
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

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            updateDisplay();
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
