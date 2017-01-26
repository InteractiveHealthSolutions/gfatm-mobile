package com.ihsinformatics.gfatmmobile.pet;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class ClinicianContactScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledButton formDate;
    TitledEditText weight;
    TitledEditText height;
    TitledEditText bmi;
    TitledEditText muac;
    TitledEditText weightPercentile;

    TitledRadioGroup cough;
    TitledRadioGroup coughDuration;
    TitledRadioGroup haemoptysis;
    TitledRadioGroup difficultyBreathing;
    TitledRadioGroup fever;
    TitledRadioGroup feverDuration;
    TitledRadioGroup weightLoss;
    TitledRadioGroup nightSweats;
    TitledRadioGroup lethargy;
    TitledRadioGroup swollenJoints;
    TitledRadioGroup backPain;
    TitledRadioGroup adenopathy;
    TitledRadioGroup vomiting;
    TitledRadioGroup giSymptoms;
    TitledRadioGroup lossInterestInActivity;
    TitledRadioGroup exposurePoint1;
    TitledRadioGroup exposurePoint2;
    TitledRadioGroup exposurePoint3;
    TitledRadioGroup exposurePoint4;
    TitledRadioGroup exposurePoint5;
    TitledRadioGroup exposurePoint6;
    TitledRadioGroup exposurePoint7;
    TitledRadioGroup exposurePoint8;
    TitledRadioGroup exposurePoint9;
    TitledRadioGroup exposurePoint10;
    TitledEditText exposureScore;

    TitledRadioGroup generalAppearence;
    TitledEditText generalAppearenceExplanation;
    TitledRadioGroup heent;
    TitledEditText heentExplanation;
    TitledRadioGroup lymphnode;
    TitledEditText lymphnodeExplanation;
    TitledRadioGroup spine;
    TitledEditText spineExplanation;
    TitledRadioGroup joints;
    TitledEditText jointsExplanation;
    TitledRadioGroup skin;
    TitledEditText skinExplanation;
    TitledRadioGroup chest;
    TitledEditText chestExplanation;
    TitledRadioGroup abdominal;
    TitledEditText abdominalExplanation;
    TitledEditText others;
    TitledSpinner examOutcome;
    TitledCheckBoxes comorbidCondition;
    CheckBox otherComorbidCondition;
    TitledEditText otherCondition;
    TitledRadioGroup referral;
    TitledEditText clincianNote;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 4;
        FORM_NAME = Forms.PET_CLINICIAN_CONTACT_SCREENING;
        FORM = Forms.pet_clinicianContactScreening;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(PAGE_COUNT - 1);
        formName.setText(FORM_NAME);

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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }

        gotoFirstPage();

        return mainContent;
    }


    @Override
    public void initViews() {

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 3, RegexUtil.numericFilter, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        height = new TitledEditText(context, null, getResources().getString(R.string.pet_height), "", "", 3, RegexUtil.numericFilter, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        bmi = new TitledEditText(context, null, getResources().getString(R.string.pet_bmi), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        muac = new TitledEditText(context, null, getResources().getString(R.string.pet_muac), "", "", 3, RegexUtil.numericFilter, InputType.TYPE_NUMBER_FLAG_DECIMAL, App.HORIZONTAL, true);
        weightPercentile = new TitledEditText(context, null, getResources().getString(R.string.pet_weight_percentile), "", "", 10, RegexUtil.numericFilter, InputType.TYPE_NUMBER_FLAG_DECIMAL, App.HORIZONTAL, true);

        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pet_contact_symptom_screen), App.VERTICAL);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_has_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        coughDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.no), App.VERTICAL, App.VERTICAL, true);
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_haemoptysis), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        difficultyBreathing = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_difficulty_breathing), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_fever), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        feverDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_fever_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.no), App.VERTICAL, App.VERTICAL, true);
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        lethargy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lethargy), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        swollenJoints = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_swollen_joints), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        backPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_back_pain), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        adenopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adenopathy), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        vomiting = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_vomiting_without_gi_symptoms), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        giSymptoms = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_gi_symptoms), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        lossInterestInActivity = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lost_activity_interest), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);

        linearLayout1.addView(cough);
        linearLayout1.addView(coughDuration);
        linearLayout1.addView(haemoptysis);
        linearLayout1.addView(difficultyBreathing);
        linearLayout1.addView(fever);
        linearLayout1.addView(feverDuration);
        linearLayout1.addView(weightLoss);
        linearLayout1.addView(nightSweats);
        linearLayout1.addView(lethargy);
        linearLayout1.addView(swollenJoints);
        linearLayout1.addView(backPain);
        linearLayout1.addView(adenopathy);
        linearLayout1.addView(vomiting);
        linearLayout1.addView(giSymptoms);
        linearLayout1.addView(lossInterestInActivity);

        MyLinearLayout linearLayout2 = new MyLinearLayout(context, "", App.VERTICAL);
        exposurePoint1 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_1), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint2 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_2), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint3 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_3), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint4 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_4), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint5 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_5), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint6 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_6), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint7 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_7), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint8 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_8), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint9 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_9), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint10 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_10), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposureScore = new TitledEditText(context, null, getResources().getString(R.string.pet_exposure_score), "0", "", 3, RegexUtil.numericFilter, InputType.TYPE_NUMBER_FLAG_DECIMAL, App.HORIZONTAL, true);
        linearLayout2.addView(exposurePoint1);
        linearLayout2.addView(exposurePoint2);
        linearLayout2.addView(exposurePoint3);
        linearLayout2.addView(exposurePoint4);
        linearLayout2.addView(exposurePoint5);
        linearLayout2.addView(exposurePoint6);
        linearLayout2.addView(exposurePoint7);
        linearLayout2.addView(exposurePoint8);
        linearLayout2.addView(exposurePoint9);
        linearLayout2.addView(exposurePoint10);
        linearLayout2.addView(exposureScore);

        MyLinearLayout linearLayout3 = new MyLinearLayout(context, getResources().getString(R.string.pet_physical_examination), App.VERTICAL);
        generalAppearence = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_general_appearance), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        generalAppearenceExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        generalAppearenceExplanation.getEditText().setSingleLine(false);
        generalAppearenceExplanation.getEditText().setMinimumHeight(150);
        heent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_heent), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        heentExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        heentExplanation.getEditText().setSingleLine(false);
        heentExplanation.getEditText().setMinimumHeight(150);
        lymphnode = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lymphnode), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        lymphnodeExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        lymphnodeExplanation.getEditText().setSingleLine(false);
        lymphnodeExplanation.getEditText().setMinimumHeight(150);
        spine = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_spine), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        spineExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        spineExplanation.getEditText().setSingleLine(false);
        spineExplanation.getEditText().setMinimumHeight(150);
        joints = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_joints), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        jointsExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        jointsExplanation.getEditText().setSingleLine(false);
        jointsExplanation.getEditText().setMinimumHeight(150);
        skin = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_skin), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        skinExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        skinExplanation.getEditText().setSingleLine(false);
        skinExplanation.getEditText().setMinimumHeight(150);
        chest = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_chest_examination), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        chestExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        chestExplanation.getEditText().setSingleLine(false);
        chestExplanation.getEditText().setMinimumHeight(150);
        abdominal = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_abdominal_examination), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        abdominalExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        abdominalExplanation.getEditText().setSingleLine(false);
        abdominalExplanation.getEditText().setMinimumHeight(150);
        others = new TitledEditText(context, null, getResources().getString(R.string.other_title), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        others.getEditText().setSingleLine(false);
        others.getEditText().setMinimumHeight(150);
        examOutcome = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_tb_suggestive), getResources().getStringArray(R.array.pet_tb_suggestives), getResources().getString(R.string.pet_not_tb_suggestive), App.VERTICAL, true);
        comorbidCondition = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_comorbid_condition), getResources().getStringArray(R.array.pet_comorbid_conditions), null, App.VERTICAL, App.VERTICAL);
        otherCondition = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 15, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        referral = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_referral_needed), getResources().getStringArray(R.array.yes_no_unknown_options), getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

        linearLayout3.addView(generalAppearence);
        linearLayout3.addView(generalAppearenceExplanation);
        linearLayout3.addView(heent);
        linearLayout3.addView(heentExplanation);
        linearLayout3.addView(lymphnode);
        linearLayout3.addView(lymphnodeExplanation);
        linearLayout3.addView(spine);
        linearLayout3.addView(spineExplanation);
        linearLayout3.addView(joints);
        linearLayout3.addView(jointsExplanation);
        linearLayout3.addView(skin);
        linearLayout3.addView(skinExplanation);
        linearLayout3.addView(chest);
        linearLayout3.addView(chestExplanation);
        linearLayout3.addView(abdominal);
        linearLayout3.addView(abdominalExplanation);
        linearLayout3.addView(others);
        linearLayout3.addView(examOutcome);
        linearLayout3.addView(comorbidCondition);
        linearLayout3.addView(otherCondition);
        linearLayout3.addView(referral);
        linearLayout3.addView(clincianNote);

        views = new View[]{formDate.getButton(), weight.getEditText(), height.getEditText(), bmi.getEditText(), muac.getEditText(), weightPercentile.getEditText(),
                cough.getRadioGroup(), coughDuration.getRadioGroup(), haemoptysis.getRadioGroup(), difficultyBreathing.getRadioGroup(), fever.getRadioGroup(), feverDuration.getRadioGroup(),
                weightLoss.getRadioGroup(), nightSweats.getRadioGroup(), lethargy.getRadioGroup(), swollenJoints.getRadioGroup(), backPain.getRadioGroup(), adenopathy.getRadioGroup(),
                vomiting.getRadioGroup(), giSymptoms.getRadioGroup(), lossInterestInActivity.getRadioGroup(), exposurePoint1.getRadioGroup(), exposurePoint2.getRadioGroup(), exposurePoint3.getRadioGroup(),
                exposurePoint4.getRadioGroup(), exposurePoint5.getRadioGroup(), exposurePoint6.getRadioGroup(), exposurePoint7.getRadioGroup(), exposurePoint8.getRadioGroup(), exposurePoint9.getRadioGroup(),
                exposurePoint10.getRadioGroup(), exposureScore.getEditText(),
                generalAppearence.getRadioGroup(), generalAppearenceExplanation.getEditText(), heent.getRadioGroup(), heentExplanation.getEditText(), lymphnode.getRadioGroup(), lymphnodeExplanation.getEditText(),
                spine.getRadioGroup(), spineExplanation.getEditText(), joints.getRadioGroup(), jointsExplanation.getEditText(), skin.getRadioGroup(), jointsExplanation.getEditText(),
                chest.getRadioGroup(), chestExplanation.getEditText(), abdominal.getRadioGroup(), abdominal.getRadioGroup(), examOutcome.getSpinner(), comorbidCondition,
                otherCondition.getEditText(), referral.getRadioGroup(), clincianNote.getEditText()};

        viewGroups = new View[][]{{formDate, weight, height, bmi, muac, weightPercentile},
                {linearLayout1},
                {linearLayout2},
                {linearLayout3}};

        weight.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (App.get(weight).equals("") || App.get(height).equals(""))
                    bmi.getEditText().setText("");
                else {

                    int w = Integer.parseInt(App.get(weight));
                    int h = Integer.parseInt(App.get(height));

                    Double heightInMeter = h * 1.0 / 100;

                    Double BMI = w * 1.0 / (heightInMeter * heightInMeter);
                    String result = String.format("%.2f", BMI);

                    String bmiCategory = "";

                    if (BMI < 18.5)
                        bmiCategory = getResources().getString(R.string.pet_underweight);
                    else if (BMI >= 18.5 && BMI <= 24.9)
                        bmiCategory = getResources().getString(R.string.pet_normal);
                    else if (BMI >= 25 && BMI <= 29.9)
                        bmiCategory = getResources().getString(R.string.pet_overweight);
                    else if (BMI >= 30 && BMI <= 39.9)
                        bmiCategory = getResources().getString(R.string.pet_obese);
                    else if (BMI >= 40)
                        bmiCategory = getResources().getString(R.string.pet_very_obese);

                    bmi.getEditText().setText(result + "   -   " + bmiCategory);

                }


            }
        });
        height.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (App.get(weight).equals("") || App.get(height).equals(""))
                    bmi.getEditText().setText("");
                else {

                    int w = Integer.parseInt(App.get(weight));
                    int h = Integer.parseInt(App.get(height));

                    Double heightInMeter = h * 1.0 / 100;

                    Double BMI = w * 1.0 / (heightInMeter * heightInMeter);
                    String result = String.format("%.2f", BMI);

                    String bmiCategory = "";

                    if (BMI < 18.5)
                        bmiCategory = getResources().getString(R.string.pet_underweight);
                    else if (BMI >= 18.5 && BMI <= 24.9)
                        bmiCategory = getResources().getString(R.string.pet_normal);
                    else if (BMI >= 25 && BMI <= 29.9)
                        bmiCategory = getResources().getString(R.string.pet_overweight);
                    else if (BMI >= 30 && BMI <= 39.9)
                        bmiCategory = getResources().getString(R.string.pet_obese);
                    else if (BMI >= 40)
                        bmiCategory = getResources().getString(R.string.pet_very_obese);

                    bmi.getEditText().setText(result + "   -   " + bmiCategory);
                }


            }
        });
        bmi.getEditText().setKeyListener(null);
        exposureScore.getEditText().setKeyListener(null);

        View listenerViewer[] = new View[]{formDate, cough, fever, exposurePoint1, exposurePoint2, exposurePoint3, exposurePoint4, exposurePoint5,
                exposurePoint6, exposurePoint7, exposurePoint8, exposurePoint9, exposurePoint10, abdominal, chest,
                skin, joints, spine, lymphnode, heent, generalAppearence};
        for (View v : listenerViewer) {

            if (v instanceof TitledButton)
                ((TitledButton) v).getButton().setOnClickListener(this);
            else if (v instanceof TitledRadioGroup)
                ((TitledRadioGroup) v).getRadioGroup().setOnCheckedChangeListener(this);

        }

        for (CheckBox checkBox : comorbidCondition.getCheckedBoxes()) {

            if (checkBox.getText().equals(getResources().getString(R.string.pet_other))) {
                otherComorbidCondition = checkBox;
                otherComorbidCondition.setOnCheckedChangeListener(this);
            }

        }

        coughDuration.setVisibility(View.GONE);
        feverDuration.setVisibility(View.GONE);
        generalAppearenceExplanation.setVisibility(View.GONE);
        heentExplanation.setVisibility(View.GONE);
        lymphnodeExplanation.setVisibility(View.GONE);
        spineExplanation.setVisibility(View.GONE);
        jointsExplanation.setVisibility(View.GONE);
        skinExplanation.setVisibility(View.GONE);
        chestExplanation.setVisibility(View.GONE);
        abdominalExplanation.setVisibility(View.GONE);
        otherCondition.setVisibility(View.GONE);
    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        coughDuration.setVisibility(View.GONE);
        feverDuration.setVisibility(View.GONE);
        generalAppearenceExplanation.setVisibility(View.GONE);
        heentExplanation.setVisibility(View.GONE);
        lymphnodeExplanation.setVisibility(View.GONE);
        spineExplanation.setVisibility(View.GONE);
        jointsExplanation.setVisibility(View.GONE);
        skinExplanation.setVisibility(View.GONE);
        chestExplanation.setVisibility(View.GONE);
        abdominalExplanation.setVisibility(View.GONE);
        otherCondition.setVisibility(View.GONE);

    }

    @Override
    public void updateDisplay() {
        if (!formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString())) {

            if (formDateCalendar.after(new Date())) {

                Date date = App.stringToDate(formDate.getButton().getText().toString(), "dd-MMM-yyyy");
                formDateCalendar = App.getCalendar(date);

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }
    }


    @Override
    public boolean validate() {

        Boolean error = false;

        if (App.get(abdominalExplanation).isEmpty() && abdominalExplanation.getVisibility() == View.VISIBLE) {
            abdominalExplanation.getEditText().setError(getString(R.string.empty_field));
            abdominalExplanation.getEditText().requestFocus();
            error = true;
        }

        if (App.get(chestExplanation).isEmpty() && chestExplanation.getVisibility() == View.VISIBLE) {
            chestExplanation.getEditText().setError(getString(R.string.empty_field));
            chestExplanation.getEditText().requestFocus();
            error = true;
        }

        if (App.get(skinExplanation).isEmpty() && skinExplanation.getVisibility() == View.VISIBLE) {
            skinExplanation.getEditText().setError(getString(R.string.empty_field));
            skinExplanation.getEditText().requestFocus();
            error = true;
        }

        if (App.get(spineExplanation).isEmpty() && spineExplanation.getVisibility() == View.VISIBLE) {
            spineExplanation.getEditText().setError(getString(R.string.empty_field));
            spineExplanation.getEditText().requestFocus();
            error = true;
        }

        if (App.get(lymphnodeExplanation).isEmpty() && lymphnodeExplanation.getVisibility() == View.VISIBLE) {
            lymphnodeExplanation.getEditText().setError(getString(R.string.empty_field));
            lymphnodeExplanation.getEditText().requestFocus();
            error = true;
        }

        if (App.get(heentExplanation).isEmpty() && heentExplanation.getVisibility() == View.VISIBLE) {
            heentExplanation.getEditText().setError(getString(R.string.empty_field));
            heentExplanation.getEditText().requestFocus();
            error = true;
        }

        if (App.get(generalAppearenceExplanation).isEmpty() && generalAppearenceExplanation.getVisibility() == View.VISIBLE) {
            generalAppearenceExplanation.getEditText().setError(getString(R.string.empty_field));
            generalAppearenceExplanation.getEditText().requestFocus();
            error = true;
        }

        if (App.get(weightPercentile).isEmpty() && weightPercentile.getVisibility() == View.VISIBLE) {
            weightPercentile.getEditText().setError(getString(R.string.empty_field));
            weightPercentile.getEditText().requestFocus();
            error = true;
        }

        if (App.get(muac).isEmpty() && muac.getVisibility() == View.VISIBLE) {
            muac.getEditText().setError(getString(R.string.empty_field));
            muac.getEditText().requestFocus();
            error = true;
        }

        if (App.get(height).isEmpty()) {
            height.getEditText().setError(getString(R.string.empty_field));
            height.getEditText().requestFocus();
            error = true;
        }

        if (App.get(weight).isEmpty()) {
            weight.getEditText().setError(getString(R.string.empty_field));
            weight.getEditText().requestFocus();
            error = true;
        }

        if (error) {

            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
            alertDialog.setMessage(getResources().getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            // DrawableCompat.setTint(clearIcon, color);
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

        return true;
    }

    @Override
    public boolean submit() {
        resetViews();
        return false;
    }

    @Override
    public boolean save() {
        return false;
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
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

        if (buttonView == otherComorbidCondition) {
            if (otherComorbidCondition.isChecked())
                otherCondition.setVisibility(View.VISIBLE);
            else
                otherCondition.setVisibility(View.GONE);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == cough.getRadioGroup()) {
            if (App.get(cough).equals(getResources().getString(R.string.yes)))
                coughDuration.setVisibility(View.VISIBLE);
            else
                coughDuration.setVisibility(View.GONE);
        } else if (group == fever.getRadioGroup()) {
            if (App.get(fever).equals(getResources().getString(R.string.yes)))
                feverDuration.setVisibility(View.VISIBLE);
            else
                feverDuration.setVisibility(View.GONE);
        } else if (group == exposurePoint1.getRadioGroup() || group == exposurePoint2.getRadioGroup() || group == exposurePoint3.getRadioGroup()
                || group == exposurePoint4.getRadioGroup() || group == exposurePoint5.getRadioGroup() || group == exposurePoint6.getRadioGroup()
                || group == exposurePoint7.getRadioGroup() || group == exposurePoint8.getRadioGroup() || group == exposurePoint9.getRadioGroup()
                || group == exposurePoint10.getRadioGroup()) {

            int score = 0;

            if (App.get(exposurePoint1).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint2).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint3).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint4).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint5).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint6).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint7).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint8).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint9).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint10).equals(getResources().getString(R.string.yes)))
                score++;

            exposureScore.getEditText().setText(String.valueOf(score));

        } else if (group == generalAppearence.getRadioGroup()) {

            if (App.get(generalAppearence).equals(getResources().getString(R.string.pet_remarkable)))
                generalAppearenceExplanation.setVisibility(View.VISIBLE);
            else
                generalAppearenceExplanation.setVisibility(View.GONE);
        } else if (group == heent.getRadioGroup()) {

            if (App.get(heent).equals(getResources().getString(R.string.pet_remarkable)))
                heentExplanation.setVisibility(View.VISIBLE);
            else
                heentExplanation.setVisibility(View.GONE);
        } else if (group == lymphnode.getRadioGroup()) {

            if (App.get(lymphnode).equals(getResources().getString(R.string.pet_remarkable)))
                lymphnodeExplanation.setVisibility(View.VISIBLE);
            else
                lymphnodeExplanation.setVisibility(View.GONE);
        } else if (group == spine.getRadioGroup()) {

            if (App.get(spine).equals(getResources().getString(R.string.pet_remarkable)))
                spineExplanation.setVisibility(View.VISIBLE);
            else
                spineExplanation.setVisibility(View.GONE);
        } else if (group == joints.getRadioGroup()) {

            if (App.get(joints).equals(getResources().getString(R.string.pet_remarkable)))
                jointsExplanation.setVisibility(View.VISIBLE);
            else
                jointsExplanation.setVisibility(View.GONE);
        } else if (group == skin.getRadioGroup()) {

            if (App.get(skin).equals(getResources().getString(R.string.pet_remarkable)))
                skinExplanation.setVisibility(View.VISIBLE);
            else
                skinExplanation.setVisibility(View.GONE);
        } else if (group == chest.getRadioGroup()) {

            if (App.get(chest).equals(getResources().getString(R.string.pet_remarkable)))
                chestExplanation.setVisibility(View.VISIBLE);
            else
                chestExplanation.setVisibility(View.GONE);
        } else if (group == abdominal.getRadioGroup()) {

            if (App.get(abdominal).equals(getResources().getString(R.string.pet_remarkable)))
                abdominalExplanation.setVisibility(View.VISIBLE);
            else
                abdominalExplanation.setVisibility(View.GONE);
        }

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

}