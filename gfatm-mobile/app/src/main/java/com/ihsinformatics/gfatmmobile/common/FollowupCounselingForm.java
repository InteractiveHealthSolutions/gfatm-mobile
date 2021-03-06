package com.ihsinformatics.gfatmmobile.common;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
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
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 2/21/2017.
 */

public class FollowupCounselingForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...

    TitledRadioGroup followup_visit_type;
    TitledEditText followup_reason_other;
    TitledEditText followup_month;
    TitledRadioGroup treatment_outcome;
    TitledRadioGroup tb_treatment_phase;
    TitledRadioGroup counselling;
    TitledEditText counselling_other;
    TextView heading_disease_info;
    TitledRadioGroup reason_followup_counseling;
    TitledEditText referral_complaint_by_field_team;
    TitledEditText other_referral_complaint_by_field_team;
    TitledEditText akuads_score;
    TitledRadioGroup adverse_event_last_visit;
    TitledCheckBoxes adverse_events;
    TitledEditText adverse_event_other;
    TitledCheckBoxes occupational_problem;
    TitledCheckBoxes relation_problem;
    TitledEditText relation_problem_other;
    TitledCheckBoxes psych_environmental_problem;
    TitledEditText psych_environmental_problem_other;
    TitledCheckBoxes patient_behaviour;
    TitledEditText counselor_comments;
    TitledRadioGroup patientReferred;
    TitledCheckBoxes referredTo;
    TitledCheckBoxes referalReasonPsychologist;
    TitledEditText otherReferalReasonPsychologist;
    TitledCheckBoxes referalReasonSupervisor;
    TitledEditText otherReferalReasonSupervisor;
    TitledCheckBoxes referalReasonCallCenter;
    TitledEditText otherReferalReasonCallCenter;
    TitledCheckBoxes referalReasonClinician;
    TitledEditText otherReferalReasonClinician;
    private TitledRadioGroup familyPlaning;
    private TitledRadioGroup familyPlaningMethod;


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
        formName = Forms.FOLLOWUP_COUNSELING;
        form = Forms.followupCounseling;

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
        String columnName = "";
        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length + 2];
        locationArray[0] = "";
        int j = 1;
        for (int i = 0; i < locations.length; i++) {
            locationArray[j] = String.valueOf(locations[i][16]);
            j++;
        }

        followup_visit_type = new TitledRadioGroup(context, null, getResources().getString(R.string.common_followup_visit_type), getResources().getStringArray(R.array.common_followup_visit_type_options), getResources().getString(R.string.common_followup_visit_type_planned), App.VERTICAL, App.VERTICAL, true, "FOLLOW-UP VISIT TYPE", new String[]{"TWO WEEK FOLLOW UP", "PLANNED MONTHLY FOLLOW UP", "END OF TREATMENT FOLLOW UP", "POST-TREATMENT FOLLOW UP", "OTHER FOLLOW UP REASON"});
        followup_reason_other = new TitledEditText(context, null, getResources().getString(R.string.common_followup_visit_type_specify_other), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER FOLLOW UP REASON");
        followup_month = new TitledEditText(context, null, getResources().getString(R.string.common_followup_month), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true, "FOLLOW-UP MONTH");
        treatment_outcome = new TitledRadioGroup(context, null, getResources().getString(R.string.common_treatment_outcome), getResources().getStringArray(R.array.common_treatment_outcome_options), "", App.VERTICAL, App.VERTICAL, true, "TREATMENT OUTCOME", new String[]{"CURE, OUTCOME", "TREATMENT COMPLETE", "TUBERCULOSIS TREATMENT FAILURE", "DIED", "TRANSFERRED OUT", "PATIENT REFERRED", "LOST TO FOLLOW-UP", "CLINICALLY EVALUATED, NO TB", "ANTIBIOTIC COMPLETE - NO TB", "NOT EVALUATED", "TREATMENT ADAPTED", "CONTACT DIAGNOSED WITH TB", "REFUSAL OF TREATMENT BY PATIENT", "PATIENT REFUSED TREATMENT AFTER STARTING", "REFUSED SCREENING", "NOT EVALUATED (SHIFT TO LTR)", "OTHER TREATMENT OUTCOME"});
        tb_treatment_phase = new TitledRadioGroup(context, null, getResources().getString(R.string.common_tb_treatment_phase), getResources().getStringArray(R.array.common_tb_treatment_phase_options), "", App.VERTICAL, App.VERTICAL, false, "TUBERCULOSIS TREATMENT PHASE", new String[]{"INTENSIVE PHASE", "CONTINUATION PHASE, TUBERCULOSIS TREATMENT", "NOT APPLICABLE"});
        counselling = new TitledRadioGroup(context, null, getResources().getString(R.string.common_counselling), getResources().getStringArray(R.array.common_counselling_options), getResources().getString(R.string.common_counselling_self), App.VERTICAL, App.VERTICAL, true, "FAMILY MEMBERS COUNSELLED", new String[]{"SELF", "PARENT", "GUARDIAN", "MOTHER", "FATHER", "MATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "PATERNAL GRANDFATHER", "BROTHER", "SISTER", "SON", "DAUGHTER", "SPOUSE", "AUNT", "NEIGHBOR", "UNCLE", "FRIEND", "COUSIN", "IN-LAWS"});
        counselling_other = new TitledEditText(context, null, getResources().getString(R.string.common_counselling_specify_other), "", "", 25, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        reason_followup_counseling = new TitledRadioGroup(context, null, getResources().getString(R.string.common_reason_followup_counseling), getResources().getStringArray(R.array.common_reason_followup_counseling_options), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR COUNSELING", new String[]{"SCHEDULED COUNSELING", "PATIENT REFERRED FOR COUNSELING"});
        referral_complaint_by_field_team = new TitledEditText(context, null, getResources().getString(R.string.common_referral_complaint_by_field_team), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        other_referral_complaint_by_field_team = new TitledEditText(context, null, getResources().getString(R.string.common_other_referral_complaint_by_field_team), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        akuads_score = new TitledEditText(context, null, getResources().getString(R.string.common_akuads_score), "", "", 2, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false, "AKUADS SCORE");
        adverse_event_last_visit = new TitledRadioGroup(context, null, getResources().getString(R.string.common_adverse_event_last_visit), getResources().getStringArray(R.array.common_adverse_event_last_visit_options), null, App.VERTICAL, App.VERTICAL, false, "ADVERSE EVENT REPORTED", getResources().getStringArray(R.array.yes_no_list_concept));
        adverse_events = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_adverse_events_2), getResources().getStringArray(R.array.common_adverse_events_2_options), new Boolean[]{true}, App.VERTICAL, App.VERTICAL, true, "ADVERSE EVENTS", new String[]{"VOMITING", "HEADACHE", "DIARRHEA", "BEHAVIOURAL CHANGE", "JOINT PAIN", "DELUSION", "HEARING DISORDER", "LOSS OF APPETITE", "ALLERGIC DERMATITIS", "NAUSEA", "TINNITUS", "HALLUCINATION", "WEAKNESS", "DEPRESSION", "INSOMNIA", "COMPLETE DEAFNESS", "FEVER", "VERTIGO", "ANXIETY", "WEIGHT LOSS", "LETHARGY", "VISUAL IMPAIRMENT", "BLURRED VISION", "JAUNDICE", "PERIORAL NUMBNESS", "OTHER ADVERSE EVENT"});
        adverse_event_other = new TitledEditText(context, null, getResources().getString(R.string.common_adverse_events_2_specify_other), "", "", 25, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER ADVERSE EVENT");
        occupational_problem = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_occupational_problem), getResources().getStringArray(R.array.common_occupational_problem_options), null, App.VERTICAL, App.VERTICAL, false, "OCCUPATIONAL PROBLEMS", new String[]{"NONE", "LOSS OF JOB DUE TO CONTINUOUS SICKNESS AND ABSENCE", "TERMINATION OF JOB DUE TO TB", "DIFFICULTY USING MASK AT WORK", "SOCIAL STIGMA", "OTHER"});
        relation_problem = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_relation_problem), getResources().getStringArray(R.array.common_relation_problem_options), null, App.VERTICAL, App.VERTICAL, false, "RELATIONSHIP PROBLEMS", new String[]{"NONE", "MARITAL PROBLEMS", "ISSUES WITH IN-LAWS", "DIVORCE", "SEPARATION", "OTHER RELATION PROBLEM"});
        relation_problem_other = new TitledEditText(context, null, getResources().getString(R.string.common_relation_problem_specify_others), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER RELATION PROBLEM");

        familyPlaning = new TitledRadioGroup(context, null, getResources().getString(R.string.common_family_planing), getResources().getStringArray(R.array.pmdt_yes_no_not_applicable), null, App.VERTICAL, App.VERTICAL, false, "FAMILY PLANNING STATUS", new String[]{"YES", "NO", "NOT APPLICABLE"});
        familyPlaningMethod = new TitledRadioGroup(context, null, getResources().getString(R.string.common_family_planing_method), getResources().getStringArray(R.array.common_family_planing_method), null, App.VERTICAL, App.VERTICAL, true, "METHOD OF FAMILY PLANNING", new String[]{"IMPLANTABLE CONTRACEPTIVE (UNSPECIFIED TYPE)", "ORAL CONTRACEPTION", "CONDOMS", "NATURAL FAMILY PLANNING", "NATURAL FAMILY PLANNING"});


        psych_environmental_problem = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_psych_environmental_problem), getResources().getStringArray(R.array.common_psych_environmental_problem_options), null, App.VERTICAL, App.VERTICAL, false, "PSYCHOSOCIAL AND ENVIROMENT PROBLEMS", new String[]{"NONE", "SELF NEGLIGENCE TOWARDS HEALTH", "FAMILY NEGLIGENCE", "PARENTAL CONFLICTS", "SEXUAL, PHYSICAL, VERBAL ABUSE", "VERBAL ABUSE", "ACADEMIC PROBLEM", "ECONOMIC PROBLEM", "OTHER PSYCHOSOCIAL AND ENVIRONMENT PROBLEMS"});
        psych_environmental_problem_other = new TitledEditText(context, null, getResources().getString(R.string.common_psych_environmental_problem_specify_other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER PSYCHOSOCIAL AND ENVIRONMENT PROBLEMS");
        patient_behaviour = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_patient_behaviour), getResources().getStringArray(R.array.common_patient_behaviour_options), null, App.VERTICAL, App.VERTICAL, true, "BEHAVIOUR", new String[]{"NORMAL", "IRRITABILITY", "STUBBORN BEHAVIOUR", "INTROVERTED PERSONALITY", "AGGRESSIVE BEHAVIOUR", "ARGUMENTATIVE BEHAVIOUR", "NON COMPLIANT BEHAVIOUR", "COMPLIANT BEHAVIOUR", "COOPERATIVE BEHAVIOUR", "NON-COOPERATIVE BEHAVIOUR"});
        counselor_comments = new TitledEditText(context, null, getResources().getString(R.string.common_counselor_comments), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "COUNSELOR COMMENTS");
        patientReferred = new TitledRadioGroup(context, null, getResources().getString(R.string.refer_patient), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true, "PATIENT REFERRED", getResources().getStringArray(R.array.yes_no_list_concept));
        referredTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.refer_patient_to), getResources().getStringArray(R.array.refer_patient_to_option), null, App.VERTICAL, App.VERTICAL, true, "PATIENT REFERRED TO", new String[]{"COUNSELOR", "PSYCHOLOGIST", "CLINICAL OFFICER/DOCTOR", "CALL CENTER", "FIELD SUPERVISOR", "SITE SUPERVISOR"});
        referalReasonPsychologist = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_psychologist), getResources().getStringArray(R.array.referral_reason_for_psychologist_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR PSYCHOLOGIST/COUNSELOR REFERRAL", new String[]{"CHECK FOR TREATMENT ADHERENCE", "PSYCHOLOGICAL EVALUATION", "BEHAVIORAL ISSUES", "REFUSAL OF TREATMENT BY PATIENT", "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR"});
        otherReferalReasonPsychologist = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR");
        referalReasonSupervisor = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_supervisor), getResources().getStringArray(R.array.referral_reason_for_supervisor_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR SUPERVISOR REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO SUPERVISOR"});
        otherReferalReasonSupervisor = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO SUPERVISOR");
        referalReasonCallCenter = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_center), getResources().getStringArray(R.array.referral_reason_for_call_center_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CALL CENTER REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO CALL CENTER"});
        otherReferalReasonCallCenter = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CALL CENTER");
        referalReasonClinician = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_clinician), getResources().getStringArray(R.array.referral_reason_for_clinician_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CLINICIAN REFERRAL", new String[]{"EXPERT OPINION", "ADVERSE EVENTS", "OTHER REFERRAL REASON TO CLINICIAN"});
        otherReferalReasonClinician = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CLINICIAN");

        heading_disease_info = new TextView(context);
        heading_disease_info.setText("Disease Information");
        heading_disease_info.setTypeface(null, Typeface.BOLD);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), followup_visit_type.getRadioGroup(), followup_reason_other.getEditText(), followup_month.getEditText(),
                treatment_outcome.getRadioGroup(), tb_treatment_phase.getRadioGroup(), counselling.getRadioGroup(), counselling_other.getEditText(), reason_followup_counseling.getRadioGroup(),
                referral_complaint_by_field_team.getEditText(), other_referral_complaint_by_field_team.getEditText(), akuads_score.getEditText(), adverse_event_last_visit.getRadioGroup(),
                adverse_events, adverse_event_other.getEditText(), occupational_problem, relation_problem, relation_problem_other.getEditText(), psych_environmental_problem, psych_environmental_problem_other.getEditText(),
                patient_behaviour, counselor_comments.getEditText(), patientReferred.getRadioGroup(), referredTo, referalReasonPsychologist, otherReferalReasonPsychologist.getEditText(), referalReasonSupervisor, otherReferalReasonSupervisor.getEditText(),
                referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText(), familyPlaning.getRadioGroup(), familyPlaningMethod.getRadioGroup()
        };

        // Array used to display views accordingly...
        viewGroups = new View[][]{{formDate, followup_visit_type, followup_reason_other, followup_month, treatment_outcome, tb_treatment_phase,
                counselling, counselling_other, patient_behaviour, heading_disease_info, reason_followup_counseling, referral_complaint_by_field_team, other_referral_complaint_by_field_team, akuads_score,
                adverse_event_last_visit, adverse_events, adverse_event_other, occupational_problem, relation_problem, relation_problem_other, familyPlaning, familyPlaningMethod, psych_environmental_problem, psych_environmental_problem_other,
                counselor_comments, patientReferred, referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
                referalReasonCallCenter, otherReferalReasonCallCenter, referalReasonClinician, otherReferalReasonClinician
        },};

        formDate.getButton().setOnClickListener(this);
        followup_visit_type.getRadioGroup().setOnCheckedChangeListener(this);
        counselling.getRadioGroup().setOnCheckedChangeListener(this);
        reason_followup_counseling.getRadioGroup().setOnCheckedChangeListener(this);
        adverse_event_last_visit.getRadioGroup().setOnCheckedChangeListener(this);
        familyPlaning.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : adverse_events.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : relation_problem.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : psych_environmental_problem.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        patientReferred.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : referredTo.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : referalReasonPsychologist.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : referalReasonSupervisor.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : referalReasonClinician.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : referalReasonCallCenter.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        resetViews();
    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        String formDa = formDate.getButton().getText().toString();
        String personDOB = App.getPatient().getPerson().getBirthdate();


        Date date = new Date();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {


            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());


        }


        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = super.validate();

        try {
            if (akuads_score.getVisibility() == View.VISIBLE && Integer.parseInt(akuads_score.getEditText().getText().toString().trim()) > 75) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                akuads_score.getEditText().setError("Value should be 0-75");
                error = true;
            }
        } catch (Exception e) {
        }


        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
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

        final ArrayList<String[]> observations = getObservations();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
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



       /* if (followup_visit_type.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FOLLOW-UP VISIT TYPE", App.get(followup_visit_type).equals(getResources().getString(R.string.common_followup_visit_type_two_weeks)) ? "TWO WEEK FOLLOW UP" :
                    App.get(followup_visit_type).equals(getResources().getString(R.string.common_followup_visit_type_planned)) ? "PLANNED MONTHLY FOLLOW UP" :
                            App.get(followup_visit_type).equals(getResources().getString(R.string.common_followup_visit_type_end)) ? "END OF TREATMENT FOLLOW UP" :
                                    App.get(followup_visit_type).equals(getResources().getString(R.string.common_followup_visit_type_post)) ? "POST-TREATMENT FOLLOW UP" : "OTHER FOLLOW UP REASON"});

*/     /*   if (followup_reason_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FOLLOW UP REASON", App.get(followup_reason_other)});

        if (followup_month.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FOLLOW-UP MONTH", App.get(followup_month)});

        if (treatment_outcome.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT OUTCOME", (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_cured))) ? "CURE, OUTCOME" :
                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_treatment_completed)) ? "TREATMENT COMPLETE" :
                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" :
                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_died)) ? "DIED" :
                                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_transferout)) ? "TRANSFERRED OUT" :
                                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_referral)) ? "PATIENT REFERRED" :
                                                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_lost_to_follow)) ? "LOST TO FOLLOW-UP" :
                                                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_clinically_evaluated)) ? "CLINICALLY EVALUATED, NO TB" :
                                                                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_antibiotic)) ? "ANTIBIOTIC COMPLETE - NO TB" :
                                                                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_not_evaluated)) ? "NOT EVALUATED" :
                                                                                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_treatment_adapted)) ? "TREATMENT ADAPTED" :
                                                                                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_contact_diagnosed)) ? "CONTACT DIAGNOSED WITH TB" :
                                                                                                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_refused_to_take_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                                                                                                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_refused_after_starting)) ? "PATIENT REFUSED TREATMENT AFTER STARTING" :
                                                                                                                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_refused_screening)) ? "REFUSED SCREENING" :
                                                                                                                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_not_evaluated_shift_ltr)) ? "NOT EVALUATED (SHIFT TO LTR)" : "OTHER TREATMENT OUTCOME")))))))))))))))});
*/

       /* if (tb_treatment_phase.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS TREATMENT PHASE", App.get(tb_treatment_phase).equals(getResources().getString(R.string.common_tb_treatment_phase_intensive)) ? "INTENSIVE PHASE" :
                    App.get(tb_treatment_phase).equals(getResources().getString(R.string.common_tb_treatment_phase_continuation)) ? "CONTINUATION PHASE, TUBERCULOSIS TREATMENT" : "NOT APPLICABLE"});

        if (counselling.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FAMILY MEMBERS COUNSELLED", (App.get(counselling).equals(getResources().getString(R.string.common_counselling_self))) ? "SELF" :
                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_parent)) ? "PARENT" :
                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_guardian)) ? "GUARDIAN" :
                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_mother)) ? "MOTHER" :
                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_father)) ? "FATHER" :
                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_maternal_grand_mother)) ? "MATERNAL GRANDMOTHER" :
                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_maternal_grand_father)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_paternal_grand_mother)) ? "PATERNAL GRANDMOTHER" :
                                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_maternal_grand_father)) ? "PATERNAL GRANDFATHER" :
                                                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_brother)) ? "BROTHER" :
                                                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_sister)) ? "SISTER" :
                                                                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_son)) ? "SON" :
                                                                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_daughter)) ? "DAUGHTER" :
                                                                                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_spouse)) ? "SPOUSE" :
                                                                                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_aunt)) ? "AUNT" :
                                                                                                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_uncle)) ? "UNCLE" :
                                                                                                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_neighbor)) ? "NEIGHBOR" :
                                                                                                                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_friend)) ? "FRIEND" :
                                                                                                                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_cousin)) ? "COUSIN" : "IN-LAWS"))))))))))))))))))});


*/       /* if (reason_followup_counseling.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR COUNSELING", App.get(reason_followup_counseling).equals(getResources().getString(R.string.common_reason_followup_counseling_monthly)) ? "SCHEDULED COUNSELING" : "PATIENT REFERRED FOR COUNSELING"});

        if (akuads_score.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"AKUADS SCORE", App.get(akuads_score)});

        if (adverse_event_last_visit.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ADVERSE EVENT REPORTED", App.get(adverse_event_last_visit).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
*/
     /*   if (adverse_events.getVisibility() == View.VISIBLE) {

            String referredToString = "";
            for (CheckBox cb : adverse_events.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_vomit)))
                    referredToString = referredToString + "VOMITING" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_headache)))
                    referredToString = referredToString + "HEADACHE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_diarhea)))
                    referredToString = referredToString + "DIARRHEA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_behavioural)))
                    referredToString = referredToString + "BEHAVIOURAL CHANGE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_arthralgia)))
                    referredToString = referredToString + "JOINT PAIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_delusion)))
                    referredToString = referredToString + "DELUSION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_hearing)))
                    referredToString = referredToString + "HEARING DISORDER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_loss)))
                    referredToString = referredToString + "LOSS OF APPETITE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_skin)))
                    referredToString = referredToString + "ALLERGIC DERMATITIS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_nausea)))
                    referredToString = referredToString + "NAUSEA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_tinnitus)))
                    referredToString = referredToString + "TINNITUS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_hallucination)))
                    referredToString = referredToString + "HALLUCINATION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_weakness)))
                    referredToString = referredToString + "WEAKNESS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_depression)))
                    referredToString = referredToString + "DEPRESSION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_insomnia)))
                    referredToString = referredToString + "INSOMNIA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_deafness)))
                    referredToString = referredToString + "COMPLETE DEAFNESS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_fever)))
                    referredToString = referredToString + "FEVER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_vertigo)))
                    referredToString = referredToString + "VERTIGO" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_anxiety)))
                    referredToString = referredToString + "ANXIETY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_weight)))
                    referredToString = referredToString + "WEIGHT LOSS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_lethargy)))
                    referredToString = referredToString + "LETHARGY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_visual)))
                    referredToString = referredToString + "VISUAL IMPAIRMENT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_blur)))
                    referredToString = referredToString + "BLURRED VISION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_jaundice)))
                    referredToString = referredToString + "JAUNDICE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_numbness)))
                    referredToString = referredToString + "PERIORAL NUMBNESS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_other)))
                    referredToString = referredToString + "OTHER ADVERSE EVENT" + " ; ";


            }
            observations.add(new String[]{"ADVERSE EVENTS", referredToString});
        }
*/
/*
        if (adverse_event_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER ADVERSE EVENT", App.get(adverse_event_other)});

        if (occupational_problem.getVisibility() == View.VISIBLE) {

            String referredToString = "";
            for (CheckBox cb : occupational_problem.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_occupational_problem_none)))
                    referredToString = referredToString + "NONE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_occupational_problem_loss)))
                    referredToString = referredToString + "LOSS OF JOB DUE TO CONTINUOUS SICKNESS AND ABSENCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_occupational_problem_termination)))
                    referredToString = referredToString + "TERMINATION OF JOB DUE TO TB" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_occupational_problem_difficulty)))
                    referredToString = referredToString + "DIFFICULTY USING MASK AT WORK" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_occupational_problem_social)))
                    referredToString = referredToString + "SOCIAL STIGMA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_occupational_problem_other)))
                    referredToString = referredToString + "OTHER" + " ; ";
            }
            observations.add(new String[]{"OCCUPATIONAL PROBLEMS", referredToString});

        }
*/

/*        if (relation_problem.getVisibility() == View.VISIBLE) {

            String referredToString = "";
            for (CheckBox cb : relation_problem.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_relation_problem_none)))
                    referredToString = referredToString + "NONE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_relation_problem_marital)))
                    referredToString = referredToString + "MARITAL PROBLEMS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_relation_problem_issues)))
                    referredToString = referredToString + "ISSUES WITH IN-LAWS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_relation_problem_divorce)))
                    referredToString = referredToString + "DIVORCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_relation_problem_separation)))
                    referredToString = referredToString + "SEPARATION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_relation_problem_others)))
                    referredToString = referredToString + "OTHER RELATION PROBLEM" + " ; ";
            }
            observations.add(new String[]{"RELATIONSHIP PROBLEMS", referredToString});

        }
        if (relation_problem_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER RELATION PROBLEM", App.get(relation_problem_other)});*/

/*
        if (psych_environmental_problem.getVisibility() == View.VISIBLE) {

            String referredToString = "";
            for (CheckBox cb : psych_environmental_problem.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_none)))
                    referredToString = referredToString + "NONE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_self)))
                    referredToString = referredToString + "SELF NEGLIGENCE TOWARDS HEALTH" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_family)))
                    referredToString = referredToString + "FAMILY NEGLIGENCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_parental)))
                    referredToString = referredToString + "PARENTAL CONFLICTS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_sexual)))
                    referredToString = referredToString + "SEXUAL, PHYSICAL, VERBAL ABUSE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_verbal)))
                    referredToString = referredToString + "VERBAL ABUSE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_academic)))
                    referredToString = referredToString + "ACADEMIC PROBLEM" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_financial)))
                    referredToString = referredToString + "ECONOMIC PROBLEM" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_other)))
                    referredToString = referredToString + "OTHER PSYCHOSOCIAL AND ENVIRONMENT PROBLEMS" + " ; ";
            }
            observations.add(new String[]{"PSYCHOSOCIAL AND ENVIROMENT PROBLEMS", referredToString});

        }
        if (psych_environmental_problem_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER PSYCHOSOCIAL AND ENVIRONMENT PROBLEMS", App.get(psych_environmental_problem_other)});

*/

/*        if (patient_behaviour.getVisibility() == View.VISIBLE) {

            String referredToString = "";
            for (CheckBox cb : patient_behaviour.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_normal)))
                    referredToString = referredToString + "NORMAL" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_irritable)))
                    referredToString = referredToString + "IRRITABILITY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_stubborn)))
                    referredToString = referredToString + "STUBBORN BEHAVIOUR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_shy)))
                    referredToString = referredToString + "INTROVERTED PERSONALITY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_aggressive)))
                    referredToString = referredToString + "AGGRESSIVE BEHAVIOUR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_argument)))
                    referredToString = referredToString + "ARGUMENTATIVE BEHAVIOUR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_non_compliant)))
                    referredToString = referredToString + "NON COMPLIANT BEHAVIOUR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_compliant)))
                    referredToString = referredToString + "COMPLIANT BEHAVIOUR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_cooperative)))
                    referredToString = referredToString + "COOPERATIVE BEHAVIOUR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_non_cooprerative)))
                    referredToString = referredToString + "NON-COOPERATIVE BEHAVIOUR" + " ; ";
            }
            observations.add(new String[]{"BEHAVIOUR", referredToString});

        }*/

/*        if (counselor_comments.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELOR COMMENTS", App.get(counselor_comments)});

        observations.add(new String[]{"PATIENT REFERRED", App.get(patientReferred).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (referredTo.getVisibility() == View.VISIBLE) {

            String referredToString = "";
            for (CheckBox cb : referredTo.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counselor)))
                    referredToString = referredToString + "COUNSELOR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.psychologist)))
                    referredToString = referredToString + "PSYCHOLOGIST" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.clinician)))
                    referredToString = referredToString + "CLINICAL OFFICER/DOCTOR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.call_center)))
                    referredToString = referredToString + "CALL CENTER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.field_supervisor)))
                    referredToString = referredToString + "FIELD SUPERVISOR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.site_supervisor)))
                    referredToString = referredToString + "SITE SUPERVISOR" + " ; ";
            }
            observations.add(new String[]{"PATIENT REFERRED TO", referredToString});

        }*/
/*
        if (referalReasonPsychologist.getVisibility() == View.VISIBLE) {

            String string = "";
            for (CheckBox cb : referalReasonPsychologist.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.check_treatment_adherence)))
                    string = string + "CHECK FOR TREATMENT ADHERENCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.psychological_issue)))
                    string = string + "PSYCHOLOGICAL EVALUATION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.behavioral_issue)))
                    string = string + "BEHAVIORAL ISSUES" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.refusal)))
                    string = string + "REFUSAL OF TREATMENT BY PATIENT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.other)))
                    string = string + "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR" + " ; ";
            }
            observations.add(new String[]{"REASON FOR PSYCHOLOGIST/COUNSELOR REFERRAL", string});

        }
        if (otherReferalReasonPsychologist.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR", App.get(otherReferalReasonPsychologist)});

        if (referalReasonSupervisor.getVisibility() == View.VISIBLE) {

            String string = "";
            for (CheckBox cb : referalReasonSupervisor.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.contact_screening_reminder)))
                    string = string + "CONTACT SCREENING REMINDER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.treatment_followup_reminder)))
                    string = string + "TREATMENT FOLLOWUP REMINDER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.check_treatment_adherence)))
                    string = string + "CHECK FOR TREATMENT ADHERENCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.investigation_report_collection)))
                    string = string + "INVESTIGATION OF REPORT COLLECTION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.adverse_events)))
                    string = string + "ADVERSE EVENTS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.medicine_collection)))
                    string = string + "MEDICINE COLLECTION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.other)))
                    string = string + "OTHER REFERRAL REASON TO SUPERVISOR" + " ; ";
            }
            observations.add(new String[]{"REASON FOR SUPERVISOR REFERRAL", string});

        }
        if (otherReferalReasonSupervisor.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REFERRAL REASON TO SUPERVISOR", App.get(otherReferalReasonSupervisor)});
*/

   /*     if (referalReasonCallCenter.getVisibility() == View.VISIBLE) {

            String string = "";
            for (CheckBox cb : referalReasonCallCenter.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.contact_screening_reminder)))
                    string = string + "CONTACT SCREENING REMINDER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.treatment_followup_reminder)))
                    string = string + "TREATMENT FOLLOWUP REMINDER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.check_treatment_adherence)))
                    string = string + "CHECK FOR TREATMENT ADHERENCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.investigation_report_collection)))
                    string = string + "INVESTIGATION OF REPORT COLLECTION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.adverse_events)))
                    string = string + "ADVERSE EVENTS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.medicine_collection)))
                    string = string + "MEDICINE COLLECTION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.other)))
                    string = string + "OTHER REFERRAL REASON TO CALL CENTER" + " ; ";
            }
            observations.add(new String[]{"REASON FOR CALL CENTER REFERRAL", string});

        }
        if (otherReferalReasonCallCenter.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REFERRAL REASON TO CALL CENTER", App.get(otherReferalReasonCallCenter)});

        if (referalReasonClinician.getVisibility() == View.VISIBLE) {

            String string = "";
            for (CheckBox cb : referalReasonClinician.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.expert_opinion)))
                    string = string + "EXPERT OPINION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.adverse_events)))
                    string = string + "ADVERSE EVENTS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.other)))
                    string = string + "OTHER REFERRAL REASON TO CLINICIAN" + " ; ";
            }
            observations.add(new String[]{"REASON FOR CLINICIAN REFERRAL", string});

        }
        if (otherReferalReasonClinician.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REFERRAL REASON TO CLINICIAN", App.get(otherReferalReasonClinician)});*/


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
                    id = serverService.saveFormLocally(Forms.FOLLOWUP_COUNSELING, form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";


                result = serverService.saveEncounterAndObservationTesting(Forms.FOLLOWUP_COUNSELING, form, formDateCalendar, observations.toArray(new String[][]{}), id);
                if (result != null)
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

        super.refill(formId);

        updateFollowUpMonth();

    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {

            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);

            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;


    }

    @Override
    public void onCheckedChanged(CompoundButton group, boolean isChecked) {
        if (adverse_events.getVisibility() == View.VISIBLE)
            for (CheckBox cb : adverse_events.getCheckedBoxes()) {
                if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_other)) && cb.isChecked()) {
                    adverse_event_other.setVisibility(View.VISIBLE);
                    break;
                } else {
                    adverse_event_other.setVisibility(View.GONE);
                }
            }
        if (relation_problem.getVisibility() == View.VISIBLE)
            for (CheckBox cb : relation_problem.getCheckedBoxes()) {
                if (cb.getText().equals(getResources().getString(R.string.common_relation_problem_others)) && cb.isChecked()) {
                    relation_problem_other.setVisibility(View.VISIBLE);
                    break;
                } else {
                    relation_problem_other.setVisibility(View.GONE);
                }
            }
        if (psych_environmental_problem.getVisibility() == View.VISIBLE)
            for (CheckBox cb : psych_environmental_problem.getCheckedBoxes()) {
                if (cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_other)) && cb.isChecked()) {
                    psych_environmental_problem_other.setVisibility(View.VISIBLE);
                    break;
                } else {
                    psych_environmental_problem_other.setVisibility(View.GONE);
                }
            }
        if (App.get(patientReferred).equals(getResources().getString(R.string.yes))) {
            for (CheckBox cb : referredTo.getCheckedBoxes()) {
                setReferralViews();
            }
            for (CheckBox cb : referalReasonPsychologist.getCheckedBoxes()) {
                setReferralViews();
            }
            for (CheckBox cb : referalReasonSupervisor.getCheckedBoxes()) {
                if (referalReasonCallCenter.getQuestionView().getError() != null)
                    setReferralViews();
            }
            for (CheckBox cb : referalReasonCallCenter.getCheckedBoxes()) {
                setReferralViews();
            }
            for (CheckBox cb : referalReasonClinician.getCheckedBoxes()) {
                setReferralViews();
            }
        }


    }


    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        referredTo.setVisibility(View.GONE);
        referalReasonPsychologist.setVisibility(View.GONE);
        otherReferalReasonPsychologist.setVisibility(View.GONE);
        referalReasonSupervisor.setVisibility(View.GONE);
        otherReferalReasonSupervisor.setVisibility(View.GONE);
        referalReasonCallCenter.setVisibility(View.GONE);
        otherReferalReasonCallCenter.setVisibility(View.GONE);
        referalReasonClinician.setVisibility(View.GONE);
        otherReferalReasonClinician.setVisibility(View.GONE);
        adverse_event_other.setVisibility(View.GONE);
        relation_problem_other.setVisibility(View.GONE);
        psych_environmental_problem_other.setVisibility(View.GONE);
        familyPlaningMethod.setVisibility(View.GONE);


        referral_complaint_by_field_team.getEditText().setEnabled(false);
        other_referral_complaint_by_field_team.getEditText().setEnabled(false);
//        akuads_score.getEditText().setEnabled(false);

        Boolean flag = true;

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
            updateFollowUpMonth();
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

                    String s_tb_treatment_outcome = serverService.getLatestObsValue(App.getPatientId(), "TREATMENT OUTCOME");
                    String s_akuads_score = serverService.getLatestObsValue(App.getPatientId(), "AKUADS SCORE");

                    result.put("s_tb_treatment_outcome", s_tb_treatment_outcome);
                    result.put("s_akuads_score", s_akuads_score);

                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }


                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();
                    if (result.get("s_tb_treatment_outcome") != null) {
                        if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("CURE, OUTCOME")) {
                            treatment_outcome.getRadioGroup().getButtons().get(0).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("TREATMENT COMPLETE")) {
                            treatment_outcome.getRadioGroup().getButtons().get(1).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("TUBERCULOSIS TREATMENT FAILURE")) {
                            treatment_outcome.getRadioGroup().getButtons().get(2).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("DIED")) {
                            treatment_outcome.getRadioGroup().getButtons().get(3).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("TRANSFERRED OUT")) {
                            treatment_outcome.getRadioGroup().getButtons().get(4).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("PATIENT REFERRED")) {
                            treatment_outcome.getRadioGroup().getButtons().get(5).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("LOST TO FOLLOW-UP")) {
                            treatment_outcome.getRadioGroup().getButtons().get(6).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("CLINICALLY EVALUATED, NO TB")) {
                            treatment_outcome.getRadioGroup().getButtons().get(7).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("ANTIBIOTIC COMPLETE - NO TB")) {
                            treatment_outcome.getRadioGroup().getButtons().get(8).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("NOT EVALUATED")) {
                            treatment_outcome.getRadioGroup().getButtons().get(9).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("TREATMENT ADAPTED")) {
                            treatment_outcome.getRadioGroup().getButtons().get(10).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("CONTACT DIAGNOSED WITH TB")) {
                            treatment_outcome.getRadioGroup().getButtons().get(11).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("REFUSAL OF TREATMENT BY PATIENT")) {
                            treatment_outcome.getRadioGroup().getButtons().get(12).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("PATIENT REFUSED TREATMENT AFTER STARTING")) {
                            treatment_outcome.getRadioGroup().getButtons().get(13).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("REFUSED SCREENING")) {
                            treatment_outcome.getRadioGroup().getButtons().get(14).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("STILL ON TREATMENT")) {
                            treatment_outcome.getRadioGroup().getButtons().get(15).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("NOT EVALUATED (SHIFT TO LTR)")) {
                            treatment_outcome.getRadioGroup().getButtons().get(16).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("OTHER TREATMENT OUTCOME")) {
                            treatment_outcome.getRadioGroup().getButtons().get(17).setChecked(true);
                        }
                    }

                    if (result.get("s_akuads_score") != null) {
                        akuads_score.getEditText().setText(result.get("s_akuads_score").toString());
                        akuads_score.getEditText().setEnabled(false);
                    } else {
                        akuads_score.getEditText().setEnabled(true);
                    }


                }
            };
            autopopulateFormTask.execute("");
        }
    }

    public void setReferralViews() {

        referalReasonPsychologist.setVisibility(View.GONE);
        otherReferalReasonPsychologist.setVisibility(View.GONE);
        referalReasonSupervisor.setVisibility(View.GONE);
        otherReferalReasonSupervisor.setVisibility(View.GONE);
        referalReasonCallCenter.setVisibility(View.GONE);
        otherReferalReasonCallCenter.setVisibility(View.GONE);
        referalReasonClinician.setVisibility(View.GONE);
        otherReferalReasonClinician.setVisibility(View.GONE);

        for (CheckBox cb : referredTo.getCheckedBoxes()) {

            if (cb.getText().equals(getString(R.string.counselor)) || cb.getText().equals(getString(R.string.psychologist))) {
                if (cb.isChecked()) {
                    referalReasonPsychologist.setVisibility(View.VISIBLE);
                    for (CheckBox cb1 : referalReasonPsychologist.getCheckedBoxes()) {
                        if (cb1.isChecked() && cb1.getText().equals(getString(R.string.other)))
                            otherReferalReasonPsychologist.setVisibility(View.VISIBLE);
                        otherReferalReasonPsychologist.getEditText().requestFocus();
                    }
                }
            } else if (cb.getText().equals(getString(R.string.site_supervisor)) || cb.getText().equals(getString(R.string.field_supervisor))) {
                if (cb.isChecked()) {
                    referalReasonSupervisor.setVisibility(View.VISIBLE);
                    for (CheckBox cb1 : referalReasonSupervisor.getCheckedBoxes()) {
                        if (cb1.isChecked() && cb1.getText().equals(getString(R.string.other))) {
                            otherReferalReasonSupervisor.setVisibility(View.VISIBLE);
                            otherReferalReasonSupervisor.getEditText().requestFocus();
                        }
                    }
                }
            } else if (cb.getText().equals(getString(R.string.call_center))) {
                if (cb.isChecked()) {
                    referalReasonCallCenter.setVisibility(View.VISIBLE);
                    for (CheckBox cb1 : referalReasonCallCenter.getCheckedBoxes()) {
                        if (cb1.isChecked() && cb1.getText().equals(getString(R.string.other))) {
                            otherReferalReasonCallCenter.setVisibility(View.VISIBLE);
                            otherReferalReasonCallCenter.getEditText().requestFocus();
                        }
                    }
                }
            } else if (cb.getText().equals(getString(R.string.clinician))) {
                if (cb.isChecked()) {
                    referalReasonClinician.setVisibility(View.VISIBLE);
                    for (CheckBox cb1 : referalReasonClinician.getCheckedBoxes()) {
                        if (cb1.isChecked() && cb1.getText().equals(getString(R.string.other))) {
                            otherReferalReasonClinician.setVisibility(View.VISIBLE);
                            otherReferalReasonClinician.getEditText().requestFocus();
                        }
                    }
                }
            }

        }

    }

    public void updateFollowUpMonth() {


        /*String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + "Treatment Initiation", "REGISTRATION DATE");

        if (treatmentDate == null) {
            treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-" + "Treatment Initiation", "REGISTRATION DATE");
            if (treatmentDate == null)
                treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "PET-" + "Treatment Initiation", "TREATMENT START DATE");
        }*/
        String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "REGISTRATION DATE");
        if (treatmentDate == null) {
            treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "PET-" + "Treatment Initiation", "TREATMENT START DATE");
        }

        String format = "";
        String[] monthArray;

        if (treatmentDate == null) {
            monthArray = new String[1];
            monthArray[0] = "0";
            followup_month.getEditText().setText("" + monthArray[0]);
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
            followup_month.getEditText().setText("" + diffMonth);


        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {


        if (radioGroup == followup_visit_type.getRadioGroup()) {
            followup_reason_other.setVisibility(View.GONE);
            followup_month.setVisibility(View.GONE);
            treatment_outcome.setVisibility(View.GONE);
            tb_treatment_phase.setVisibility(View.GONE);

            if (followup_visit_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_followup_visit_type_other))) {
                followup_reason_other.setVisibility(View.VISIBLE);
            } else if (followup_visit_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_followup_visit_type_planned))) {
                tb_treatment_phase.setVisibility(View.VISIBLE);
                followup_month.setVisibility(View.VISIBLE);
            } else if (followup_visit_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_followup_visit_type_end))) {
                treatment_outcome.setVisibility(View.VISIBLE);
            }
        } else if (radioGroup == counselling.getRadioGroup()) {
            counselling_other.setVisibility(View.GONE);

            if (counselling.getRadioGroup().getSelectedValue().equals(getString(R.string.common_counselling_other))) {
                counselling_other.setVisibility(View.VISIBLE);
            }
        } else if (radioGroup == adverse_event_last_visit.getRadioGroup()) {
            adverse_events.setVisibility(View.GONE);

            if (adverse_event_last_visit.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                adverse_events.setVisibility(View.VISIBLE);
                for (CheckBox cb : adverse_events.getCheckedBoxes()) {
                    cb.setChecked(false);
                }

            } else {
                adverse_event_other.setVisibility(View.GONE);
            }
        } else if (radioGroup == patientReferred.getRadioGroup()) {
            patientReferred.getQuestionView().setError(null);
            if (App.get(patientReferred).equals(getResources().getString(R.string.yes))) {
                referredTo.setVisibility(View.VISIBLE);
                setReferralViews();
            } else {
                referredTo.setVisibility(View.GONE);
                referalReasonPsychologist.setVisibility(View.GONE);
                otherReferalReasonPsychologist.setVisibility(View.GONE);
                referalReasonSupervisor.setVisibility(View.GONE);
                otherReferalReasonSupervisor.setVisibility(View.GONE);
                referalReasonCallCenter.setVisibility(View.GONE);
                otherReferalReasonCallCenter.setVisibility(View.GONE);
                referalReasonClinician.setVisibility(View.GONE);
                otherReferalReasonClinician.setVisibility(View.GONE);
            }
        }else if (radioGroup == familyPlaning.getRadioGroup()) {

            if (familyPlaning.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                familyPlaningMethod.setVisibility(View.VISIBLE);
            }else
                familyPlaningMethod.setVisibility(View.GONE);
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
