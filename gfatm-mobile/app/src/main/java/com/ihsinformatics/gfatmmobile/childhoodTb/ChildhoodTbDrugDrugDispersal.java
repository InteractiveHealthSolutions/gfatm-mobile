package com.ihsinformatics.gfatmmobile.childhoodTb;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbDrugDrugDispersal extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    Snackbar snackbar;
    ScrollView scrollView;
    TitledButton formDate;

    TitledRadioGroup patientHaveTb;
    TitledRadioGroup treatmentPlan;
    TitledRadioGroup intensivePhaseRegimen;
    TitledSpinner typeFixedDosePrescribedIntensive;
    TitledRadioGroup currentTabletsofRHZ;
    TitledRadioGroup currentTabletsofE;
    TitledRadioGroup newTabletsofRHZ;
    TitledRadioGroup newTabletsofE;
    TitledRadioGroup adultFormulationofHRZE;

    TitledRadioGroup continuationPhaseRegimen;
    TitledSpinner typeFixedDosePrescribedContinuation;
    TitledRadioGroup currentTabletsOfContinuationRH;
    TitledRadioGroup currentTabletsOfContinuationE;
    TitledRadioGroup newTabletsOfContinuationRH;
    TitledRadioGroup newTabletsOfContinuationE;
    TitledRadioGroup adultFormulationOfContinuationRH;
    TitledRadioGroup adultFormulationOfContinuationRHE_E;
    TitledRadioGroup adultFormulationOfContinuationRHE_RHE;
    TitledCheckBoxes moAdditionalTreatment;
    TitledRadioGroup pediasureDispersed;
    TitledRadioGroup vitaminBComplexDispersed;
    TitledRadioGroup ironDispersed;
    TitledRadioGroup anthelminthicDispersed;
    TitledRadioGroup iptDose;
    TitledRadioGroup iptDrugDispersed;
    TitledCheckBoxes moInitiateTreatmentIpt;
    TitledRadioGroup ironDispersedIpt;
    TitledRadioGroup multivitaminsDispersed;
    TitledRadioGroup anthelminticAlbendazoleDispersed;
    TitledRadioGroup antibioticTrialDispersed;
    TitledCheckBoxes moInitiatingAdditionalTreatmentAntibiotic;
    TitledRadioGroup ironDispersedAntibiotic;
    TitledRadioGroup multivitaminsDispersedAntibiotic;
    TitledRadioGroup anthelminticAlbendazoleDispersedAntibiotic;
    TitledButton nextDateOfDrug;

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
        FORM_NAME = Forms.CHILDHOODTB_DRUG_DISPERSAL;
        FORM = Forms.childhoodTb_drug_dispersal;

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
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        } else {
            for (int i = 0; i < PAGE_COUNT; i++) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
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
        patientHaveTb = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_have_tb), getResources().getStringArray(R.array.ctb_patient_have_tb_list), null, App.VERTICAL, App.VERTICAL);
        treatmentPlan = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_treatment_plan), getResources().getStringArray(R.array.ctb_intensive_continuation), null, App.VERTICAL, App.VERTICAL);

        intensivePhaseRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_regimen), getResources().getStringArray(R.array.ctb_regimen_list), getResources().getString(R.string.ctb_rhz), App.HORIZONTAL, App.VERTICAL);
        typeFixedDosePrescribedIntensive = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_type_of_fixed_dose), getResources().getStringArray(R.array.ctb_type_of_fixed_dose_list), null, App.VERTICAL);
        currentTabletsofRHZ = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_intensive_current_rhz_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        currentTabletsofE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_intensive_current_e_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        newTabletsofRHZ = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_intensive_new_formulation_rhz_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        newTabletsofE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_intensive_new_formulation_e_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        adultFormulationofHRZE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_intensive_adult_hrze_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        continuationPhaseRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_continuation_phase_regimen), getResources().getStringArray(R.array.ctb_continuation_phase_regimen_list), null, App.HORIZONTAL, App.VERTICAL);
        typeFixedDosePrescribedContinuation = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_type_of_fixed_dose), getResources().getStringArray(R.array.ctb_type_of_dose_continuation_list), null, App.VERTICAL);
        currentTabletsOfContinuationRH = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_continuation_current_rh_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        currentTabletsOfContinuationE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_continuation_current_e_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        newTabletsOfContinuationRH = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_continuation_new_formulation_rh), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        newTabletsOfContinuationE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_continuation_new_formulation_e), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        adultFormulationOfContinuationRH = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_continuation_adult_formulation_rh_rh_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        adultFormulationOfContinuationRHE_E = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_continuation_adult_formulation_rhe_e_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        adultFormulationOfContinuationRHE_RHE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_continuation_adult_formulation_rhe_rhe_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        nextDateOfDrug = new TitledButton(context, null, getResources().getString(R.string.ctb_next_date_drug_dispersal), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        moAdditionalTreatment = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_mo_initiate_additional_treatment), getResources().getStringArray(R.array.ctb_pediasure_vitamin_iron_anthelminthic), null, App.VERTICAL, App.VERTICAL);
        pediasureDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_pediasure_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        vitaminBComplexDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_vitamin_b_complex_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        ironDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_iron_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        anthelminthicDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_anthelminthic_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        iptDose = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ipt_dose), getResources().getStringArray(R.array.ctb_dose_list), null, App.VERTICAL, App.VERTICAL,true);
        iptDrugDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ipt_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        moInitiateTreatmentIpt = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_mo_initiate_additional_treatment), getResources().getStringArray(R.array.ctb_iron_multivitamins_anthelmintic), null, App.VERTICAL, App.VERTICAL);
        ironDispersedIpt = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_iron_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        multivitaminsDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_multivitmins_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        anthelminticAlbendazoleDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_anthelminthic_albendazole_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        antibioticTrialDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_antibiotic_trial_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        moInitiatingAdditionalTreatmentAntibiotic = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_mo_initiate_additional_treatment), getResources().getStringArray(R.array.ctb_iron_multivitamins_anthelmintic_other_none), null, App.VERTICAL, App.VERTICAL);
        ironDispersedAntibiotic = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_iron_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        multivitaminsDispersedAntibiotic = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_multivitmins_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        anthelminticAlbendazoleDispersedAntibiotic = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_anthelminthic_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL);
        views = new View[]{
                formDate.getButton(), treatmentPlan.getRadioGroup(), intensivePhaseRegimen.getRadioGroup(), typeFixedDosePrescribedIntensive.getSpinner(), currentTabletsofRHZ.getRadioGroup(), currentTabletsofE.getRadioGroup(),
                newTabletsofRHZ.getRadioGroup(), newTabletsofE.getRadioGroup(), adultFormulationofHRZE.getRadioGroup(), continuationPhaseRegimen.getRadioGroup(), typeFixedDosePrescribedContinuation.getSpinner(),
                currentTabletsOfContinuationRH.getRadioGroup(), currentTabletsOfContinuationE.getRadioGroup(), newTabletsOfContinuationRH.getRadioGroup(), newTabletsOfContinuationE.getRadioGroup(),
                adultFormulationOfContinuationRH.getRadioGroup(), adultFormulationOfContinuationRHE_E.getRadioGroup(), adultFormulationOfContinuationRHE_RHE.getRadioGroup(),
                moAdditionalTreatment, pediasureDispersed.getRadioGroup(), vitaminBComplexDispersed.getRadioGroup(), ironDispersed.getRadioGroup(), anthelminthicDispersed.getRadioGroup(),
                iptDose.getRadioGroup(), iptDrugDispersed.getRadioGroup(), moInitiateTreatmentIpt, ironDispersedIpt.getRadioGroup(),multivitaminsDispersed.getRadioGroup(),
                anthelminticAlbendazoleDispersed.getRadioGroup(), antibioticTrialDispersed.getRadioGroup(), moInitiatingAdditionalTreatmentAntibiotic,
                ironDispersedAntibiotic.getRadioGroup(), multivitaminsDispersedAntibiotic.getRadioGroup(), anthelminticAlbendazoleDispersedAntibiotic.getRadioGroup(), nextDateOfDrug.getButton()};
        viewGroups = new View[][]
                {{
                        formDate,
                        patientHaveTb,
                        treatmentPlan,
                        intensivePhaseRegimen,
                        typeFixedDosePrescribedIntensive,
                        currentTabletsofRHZ,
                        currentTabletsofE,
                        newTabletsofRHZ,
                        newTabletsofE,
                        adultFormulationofHRZE,
                        continuationPhaseRegimen,
                        typeFixedDosePrescribedContinuation,
                        currentTabletsOfContinuationRH,
                        currentTabletsOfContinuationE,
                        newTabletsOfContinuationRH,
                        newTabletsOfContinuationE,
                        adultFormulationOfContinuationRH,
                        adultFormulationOfContinuationRHE_E,
                        adultFormulationOfContinuationRHE_RHE,
                        moAdditionalTreatment,
                        pediasureDispersed,
                        vitaminBComplexDispersed,
                        ironDispersed,
                        anthelminthicDispersed,
                        iptDose,
                        iptDrugDispersed,
                        moInitiateTreatmentIpt,
                        ironDispersedIpt,
                        multivitaminsDispersed,
                        anthelminticAlbendazoleDispersed,
                        antibioticTrialDispersed,
                        moInitiatingAdditionalTreatmentAntibiotic,
                        ironDispersedAntibiotic,
                        multivitaminsDispersedAntibiotic,
                        anthelminticAlbendazoleDispersedAntibiotic,
                        nextDateOfDrug,
                }};
        formDate.getButton().setOnClickListener(this);


        patientHaveTb.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentPlan.getRadioGroup().setOnCheckedChangeListener(this);
        intensivePhaseRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        typeFixedDosePrescribedIntensive.getSpinner().setOnItemSelectedListener(this);
        currentTabletsofRHZ.getRadioGroup().setOnCheckedChangeListener(this);
        currentTabletsofE.getRadioGroup().setOnCheckedChangeListener(this);
        newTabletsofRHZ.getRadioGroup().setOnCheckedChangeListener(this);
        newTabletsofE.getRadioGroup().setOnCheckedChangeListener(this);
        adultFormulationofHRZE.getRadioGroup().setOnCheckedChangeListener(this);
        continuationPhaseRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        typeFixedDosePrescribedContinuation.getSpinner().setOnItemSelectedListener(this);
        currentTabletsOfContinuationRH.getRadioGroup().setOnCheckedChangeListener(this);
        currentTabletsOfContinuationE.getRadioGroup().setOnCheckedChangeListener(this);
        newTabletsOfContinuationRH.getRadioGroup().setOnCheckedChangeListener(this);
        newTabletsOfContinuationE.getRadioGroup().setOnCheckedChangeListener(this);
        adultFormulationOfContinuationRH.getRadioGroup().setOnCheckedChangeListener(this);
        adultFormulationOfContinuationRHE_E.getRadioGroup().setOnCheckedChangeListener(this);
        adultFormulationOfContinuationRHE_RHE.getRadioGroup().setOnCheckedChangeListener(this);

        ArrayList<MyCheckBox> checkBoxList2 = moAdditionalTreatment.getCheckedBoxes();
        for (CheckBox cb : moAdditionalTreatment.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        pediasureDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        vitaminBComplexDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        ironDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        anthelminthicDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        iptDose.getRadioGroup().setOnCheckedChangeListener(this);
        iptDrugDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        ArrayList<MyCheckBox> checkBoxList = moInitiateTreatmentIpt.getCheckedBoxes();
        for (CheckBox cb : moInitiateTreatmentIpt.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        ironDispersedIpt.getRadioGroup().setOnCheckedChangeListener(this);
        multivitaminsDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        anthelminticAlbendazoleDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        antibioticTrialDispersed.getRadioGroup().setOnCheckedChangeListener(this);

        ArrayList<MyCheckBox> checkBoxList1 = moInitiatingAdditionalTreatmentAntibiotic.getCheckedBoxes();
        for (CheckBox cb : moInitiatingAdditionalTreatmentAntibiotic.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);


        ironDispersedAntibiotic.getRadioGroup().setOnCheckedChangeListener(this);
        multivitaminsDispersedAntibiotic.getRadioGroup().setOnCheckedChangeListener(this);
        anthelminticAlbendazoleDispersedAntibiotic.getRadioGroup().setOnCheckedChangeListener(this);
        nextDateOfDrug.getButton().setOnClickListener(this);

        antibioticTrialDispersed.getRadioGroup().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moInitiatingAdditionalTreatmentAntibiotic.setVisibility(View.VISIBLE);
            }
        });


        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();
        Date date = new Date();
        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();

            String personDOB = App.getPatient().getPerson().getBirthdate();


            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.ctb_form_date_less_than_patient_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }
        if (!nextDateOfDrug.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString())) {

            //
            // +Date date = App.stringToDate(sampleSubmitDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (secondDateCalendar.after(date)) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else {
                nextDateOfDrug.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            }
        }
    }

    @Override
    public boolean validate() {
        Boolean error = false;
        if (App.get(patientHaveTb).equalsIgnoreCase(getResources().getString(R.string.yes)) && App.get(treatmentPlan).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            treatmentPlan.getRadioGroup().getButtons().get(2).setError(getString(R.string.empty_field));
            treatmentPlan.getRadioGroup().requestFocus();
            error = true;
        }
        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            DrawableCompat.setTint(clearIcon, color);
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
        final ArrayList<String[]> observations = new ArrayList<String[]>();

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

        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
        observations.add(new String[]{"PATIENT HAVE TB", App.get(patientHaveTb).toUpperCase()});

        if(treatmentPlan.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"TREATMENT PLAN", App.get(treatmentPlan).equals(getResources().getString(R.string.ctb_intensive_phase)) ? "INTENSIVE PHASE" :
                    App.get(treatmentPlan).equals(getResources().getString(R.string.ctb_continuation_phase)) ? "CONTINUE REGIMEN"
                            : "TREATMENT COMPLETE"});
        }

        if (intensivePhaseRegimen.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"REGIMEN", App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rhze)) ? "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS" : "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE"});
        }

        if (typeFixedDosePrescribedIntensive.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"PAEDIATRIC DOSE COMBINATION", App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_current_formulation)) ? "CURRENT FORULATION" :
                    App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_new_formulation)) ? "NEW FORMULATION"
                            : "ADULT FORMULATION"});
        }
        if (currentTabletsofRHZ.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"IF CURRENT FORMULATION, RHZ TABLETS DISPERSED", App.get(currentTabletsofRHZ).toUpperCase()});
        }
        if (currentTabletsofE.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"IF CURRENT FORMULATION, E TABLETS DISPERSED", App.get(currentTabletsofE).toUpperCase()});
        }
        if (newTabletsofE.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"IF NEW FORMULATION, E TABLETS DISPERSED", App.get(newTabletsofRHZ).toUpperCase()});
        }
        if (newTabletsofRHZ.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"IF NEW FORMULATION, RHZ TABLETS DISPERSED", App.get(newTabletsofE).toUpperCase()});
        }
        if (adultFormulationofHRZE.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"IF ADULT FORMULATION, HRZE TABLETS DISPERSED", App.get(adultFormulationofHRZE).toUpperCase()});
        }
        if (continuationPhaseRegimen.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"REGIMEN", App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rh)) ? "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS" : "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE"});
        }
        if (typeFixedDosePrescribedContinuation.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE", App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_current_formulation_continuation)) ? "CURRENT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE" :
                    App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_new_formulation_continuation)) ? "NEW FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE" :
                            App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_adult_formulation_continuation_rhe)) ? "ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE"
                                    : "ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE"});
        }
        if (currentTabletsOfContinuationRH.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"IF CURRENT FORMULATION, RH TABLETS DISPERSED", App.get(currentTabletsOfContinuationRH).toUpperCase()});
        }
        if (currentTabletsOfContinuationE.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"IF CURRENT FORMULATION, E TABLETS DISPERSED", App.get(currentTabletsOfContinuationE).toUpperCase()});
        }
        if (newTabletsOfContinuationRH.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"IF NEW FORMULATION, RHZ TABLETS DISPERSED", App.get(newTabletsOfContinuationRH).toUpperCase()});
        }
        if (newTabletsOfContinuationE.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"IF NEW FORMULATION, E TABLETS DISPERSED", App.get(newTabletsOfContinuationE).toUpperCase()});
        }
        if (adultFormulationOfContinuationRH.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"IF ADULT FORMULATION, RH TABLETS DISPERSED", App.get(adultFormulationOfContinuationRH).toUpperCase()});
        }
        if (adultFormulationOfContinuationRHE_E.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"IF ADULT FORMULATION OF RHE, E TABLETS DISPERSED", App.get(adultFormulationOfContinuationRHE_E).toUpperCase()});
        }
        if (adultFormulationOfContinuationRHE_RHE.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"IF ADULT FORMULATION OF RHE, RHE TABLETS DISPERSED", App.get(adultFormulationOfContinuationRHE_RHE).toUpperCase()});
        }
        if(moAdditionalTreatment.getVisibility()==View.VISIBLE){
            String additionalTreatmentString = "";
            for (CheckBox cb : moAdditionalTreatment.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_pediasure)))
                    additionalTreatmentString = additionalTreatmentString + "PEDIASURE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_iron)))
                    additionalTreatmentString = additionalTreatmentString + "IRON" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_vitamin_B_complex)))
                    additionalTreatmentString = additionalTreatmentString + "VITAMIN B COMPLEX" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_anthelminthic)))
                    additionalTreatmentString = additionalTreatmentString + "ANTHELMINTHIC" + " ; ";
            }
            observations.add(new String[]{"ADDITIONAL TREATMENT TO TB PATIENT", additionalTreatmentString});
        }
        if(pediasureDispersed.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"PEDIASURE DISPERSED", App.get(pediasureDispersed).toUpperCase()});
        }
        if(vitaminBComplexDispersed.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"VITAMIN B-COMPLEX DISPERSED", App.get(vitaminBComplexDispersed).toUpperCase()});
        }
        if(ironDispersed.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"IRON DISPERSED", App.get(ironDispersed).toUpperCase()});
        }
        if(anthelminthicDispersed.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"ANTHELMINTIC DISPERSED", App.get(anthelminthicDispersed).toUpperCase()});
        }
        if(iptDose.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"IPT DOSE", App.get(iptDose).equals(getResources().getString(R.string.ctb_quater_per_day)) ? "1/4 TAB ONCE ADAY" :
                    (App.get(iptDose).equals(getResources().getString(R.string.ctb_half_per_day)) ? "1/2 TAB ONCE A DAY" :
                            "1 TAB ONCE A DAY")});
        }
        if(iptDrugDispersed.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"IPT DRUG DISPERSED", App.get(iptDrugDispersed).toUpperCase()});
        }
        if(moInitiateTreatmentIpt.getVisibility()==View.VISIBLE){
            String additionalTreatmentString = "";
            for (CheckBox cb : moInitiateTreatmentIpt.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_iron)))
                    additionalTreatmentString = additionalTreatmentString + "IRON" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_multivitamins)))
                    additionalTreatmentString = additionalTreatmentString + "MULTIVITAMIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_anthelmintic_albendazole)))
                    additionalTreatmentString = additionalTreatmentString + "ANTHELMINTHIC" + " ; ";
            }
            observations.add(new String[]{"ADDITIONAL TREATMENT IPT PATIENT", additionalTreatmentString});
        }
        if(ironDispersedIpt.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"IRON DISPERSED", App.get(ironDispersedIpt).toUpperCase()});
        }
        if(multivitaminsDispersed.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"MULTIVITAMINS DISPERSED", App.get(multivitaminsDispersed).toUpperCase()});
        }
        if(anthelminticAlbendazoleDispersed.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"ANTHELMINTIC DISPERSED", App.get(anthelminticAlbendazoleDispersed).toUpperCase()});
        }

        if(antibioticTrialDispersed.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"ANTIBIOTIC DISPERSED", App.get(antibioticTrialDispersed).toUpperCase()});
        }
        if(moInitiatingAdditionalTreatmentAntibiotic.getVisibility()==View.VISIBLE){
            String additionalTreatmentString = "";
            for (CheckBox cb : moInitiatingAdditionalTreatmentAntibiotic.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_iron)))
                    additionalTreatmentString = additionalTreatmentString + "IRON" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_multivitamins)))
                    additionalTreatmentString = additionalTreatmentString + "MULTIVITAMIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_anthelminthic)))
                    additionalTreatmentString = additionalTreatmentString + "ANTHELMINTHIC" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_other_title)))
                    additionalTreatmentString = additionalTreatmentString + "OTHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_none)))
                    additionalTreatmentString = additionalTreatmentString + "NONE" + " ; ";
            }
            observations.add(new String[]{"ADDITIONAL TREATMENT FOR INCONCLUSIVE PATIENT", additionalTreatmentString});
        }
        if(ironDispersedAntibiotic.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"IRON DISPERSED", App.get(ironDispersedAntibiotic).toUpperCase()});
        }
        if(multivitaminsDispersedAntibiotic.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"MULTIVITAMINS DISPERSED", App.get(multivitaminsDispersedAntibiotic).toUpperCase()});
        }
        if(anthelminticAlbendazoleDispersedAntibiotic.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"ANTHELMINTIC DISPERSED", App.get(anthelminticAlbendazoleDispersedAntibiotic).toUpperCase()});
        }

        observations.add(new String[]{"NEXT DATE OF DRUG DISPERSAL", App.getSqlDateTime(secondDateCalendar)});




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

                String result = serverService.saveEncounterAndObservation("Drug Dispersal Form", FORM, formDateCalendar, observations.toArray(new String[][]{}),true);
                if (result.contains("SUCCESS"))
                    return "SUCCESS";

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

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {
        OfflineForm fo = serverService.getOfflineFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("PATIENT HAVE TB")) {
                for (RadioButton rb : patientHaveTb.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_inconclusive)) && obs[0][1].equals("INCONCLUSIVE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                patientHaveTb.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT PLAN")) {
                for (RadioButton rb : treatmentPlan.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_intensive_phase)) && obs[0][1].equals("INTENSIVE PHASE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_continuation_phase)) && obs[0][1].equals("CONTINUE REGIMEN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                treatmentPlan.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REGIMEN")) {
                if (App.get(treatmentPlan).equals(getResources().getString(R.string.ctb_intensive_phase))) {
                    for (RadioButton rb : intensivePhaseRegimen.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_rhze)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhz)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    intensivePhaseRegimen.setVisibility(View.VISIBLE);
                }
            } else if (obs[0][0].equals("PAEDIATRIC DOSE COMBINATION")) {
                String value = obs[0][1].equals("CURRENT FORULATION") ? getResources().getString(R.string.ctb_current_formulation) :
                        (obs[0][1].equals("NEW FORMULATION") ? getResources().getString(R.string.ctb_new_formulation) :
                                getResources().getString(R.string.ctb_adult_formulation));

                typeFixedDosePrescribedIntensive.getSpinner().selectValue(value);
                typeFixedDosePrescribedIntensive.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IF CURRENT FORMULATION, RHZ TABLETS DISPERSED")) {
                for (RadioButton rb : currentTabletsofRHZ.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                currentTabletsofRHZ.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IF CURRENT FORMULATION, E TABLETS DISPERSED")) {
                for (RadioButton rb : currentTabletsofE.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                currentTabletsofE.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IF NEW FORMULATION, E TABLETS DISPERSED")) {
                for (RadioButton rb : newTabletsofE.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                newTabletsofE.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IF NEW FORMULATION, RHZ TABLETS DISPERSED")) {
                for (RadioButton rb : newTabletsofRHZ.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                newTabletsofRHZ.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IF ADULT FORMULATION, HRZE TABLETS DISPERSED")) {
                for (RadioButton rb : adultFormulationofHRZE.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                adultFormulationofHRZE.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REGIMEN")) {
                if (App.get(treatmentPlan).equals(getResources().getString(R.string.ctb_continuation_phase))) {
                    for (RadioButton rb : continuationPhaseRegimen.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_rh)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhe)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    continuationPhaseRegimen.setVisibility(View.VISIBLE);
                }
            } else if (obs[0][0].equals("PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE")) {
                String value = obs[0][1].equals("CURRENT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_current_formulation_continuation) :
                        (obs[0][1].equals("NEW FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_new_formulation_continuation) :
                                (obs[0][1].equals("ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_adult_formulation_continuation_rhe) :
                                        getResources().getString(R.string.ctb_adult_formulation_continuation_rh)));

                typeFixedDosePrescribedContinuation.getSpinner().selectValue(value);
                typeFixedDosePrescribedContinuation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IF CURRENT FORMULATION, RH TABLETS DISPERSED")) {
                for (RadioButton rb : currentTabletsOfContinuationRH.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                currentTabletsOfContinuationRH.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IF CURRENT FORMULATION, E TABLETS DISPERSED")) {
                for (RadioButton rb : currentTabletsOfContinuationE.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                currentTabletsOfContinuationE.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IF NEW FORMULATION, RHZ TABLETS DISPERSED")) {
                for (RadioButton rb : newTabletsOfContinuationRH.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                newTabletsOfContinuationRH.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IF NEW FORMULATION, E TABLETS DISPERSED")) {
                for (RadioButton rb : newTabletsOfContinuationE.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                newTabletsOfContinuationE.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IF ADULT FORMULATION, RH TABLETS DISPERSED")) {
                for (RadioButton rb : adultFormulationOfContinuationRH.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                adultFormulationOfContinuationRH.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IF ADULT FORMULATION OF RHE, E TABLETS DISPERSED")) {
                for (RadioButton rb : adultFormulationOfContinuationRHE_E.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                adultFormulationOfContinuationRHE_E.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IF ADULT FORMULATION OF RHE, RHE TABLETS DISPERSED")) {
                for (RadioButton rb : adultFormulationOfContinuationRHE_RHE.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                adultFormulationOfContinuationRHE_RHE.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ADDITIONAL TREATMENT TO TB PATIENT")) {
                for (CheckBox cb : moAdditionalTreatment.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_pediasure)) && obs[0][1].equals("PEDIASURE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_iron)) && obs[0][1].equals("IRON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_vitamin_B_complex)) && obs[0][1].equals("VITAMIN B COMPLEX")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_anthelminthic)) && obs[0][1].equals("ANTHELMINTHIC")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                moAdditionalTreatment.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PEDIASURE DISPERSED")) {
                for (RadioButton rb : pediasureDispersed.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                pediasureDispersed.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("VITAMIN B-COMPLEX DISPERSED")) {
                for (RadioButton rb : vitaminBComplexDispersed.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                vitaminBComplexDispersed.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IRON DISPERSED") && App.get(patientHaveTb).equalsIgnoreCase(getResources().getString(R.string.yes))) {
                    for (RadioButton rb : ironDispersed.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ironDispersed.setVisibility(View.VISIBLE);

            } else if (obs[0][0].equals("ANTHELMINTIC DISPERSED") && App.get(patientHaveTb).equalsIgnoreCase(getResources().getString(R.string.yes))) {
                    for (RadioButton rb : anthelminthicDispersed.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    anthelminthicDispersed.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IPT DOSE")) {
                for (RadioButton rb : iptDose.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_quater_per_day)) && obs[0][1].equals("1/4 TAB ONCE ADAY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_half_per_day)) && obs[0][1].equals("1/2 TAB ONCE A DAY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_per_day)) && obs[0][1].equals("1 TAB ONCE A DAY")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("IPT DRUG DISPERSED")) {
                for (RadioButton rb : iptDrugDispersed.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                iptDrugDispersed.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ADDITIONAL TREATMENT IPT PATIENT")) {
                for (CheckBox cb : moInitiateTreatmentIpt.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_iron)) && obs[0][1].equals("IRON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_multivitamins)) && obs[0][1].equals("MULTIVITAMIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_anthelmintic_albendazole)) && obs[0][1].equals("ANTHELMINTHIC")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                moInitiateTreatmentIpt.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IRON DISPERSED") && App.get(patientHaveTb).equalsIgnoreCase(getResources().getString(R.string.no))) {
                    for (RadioButton rb : ironDispersedIpt.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ironDispersedIpt.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("MULTIVITAMINS DISPERSED") && App.get(patientHaveTb).equalsIgnoreCase(getResources().getString(R.string.no))) {
                    for (RadioButton rb : multivitaminsDispersed.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    multivitaminsDispersed.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ANTHELMINTIC DISPERSED") && App.get(patientHaveTb).equalsIgnoreCase(getResources().getString(R.string.no))) {
                    for (RadioButton rb : anthelminticAlbendazoleDispersed.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    anthelminticAlbendazoleDispersed.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ANTIBIOTIC DISPERSED")) {
                for (RadioButton rb : antibioticTrialDispersed.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                antibioticTrialDispersed.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ADDITIONAL TREATMENT FOR INCONCLUSIVE PATIENT")) {
                for (CheckBox cb : moInitiatingAdditionalTreatmentAntibiotic.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_iron)) && obs[0][1].equals("IRON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_multivitamins)) && obs[0][1].equals("MULTIVITAMIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_anthelminthic)) && obs[0][1].equals("ANTHELMINTHIC")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_none)) && obs[0][1].equals("NONE")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                moInitiatingAdditionalTreatmentAntibiotic.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("IRON DISPERSED") && App.get(patientHaveTb).equalsIgnoreCase(getResources().getString(R.string.ctb_inconclusive))) {
                    for (RadioButton rb : ironDispersedAntibiotic.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ironDispersedAntibiotic.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("MULTIVITAMINS DISPERSED") && App.get(patientHaveTb).equalsIgnoreCase(getResources().getString(R.string.ctb_inconclusive))) {
                    for (RadioButton rb : multivitaminsDispersedAntibiotic.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    multivitaminsDispersedAntibiotic.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ANTHELMINTIC DISPERSED") && App.get(patientHaveTb).equalsIgnoreCase(getResources().getString(R.string.ctb_inconclusive))) {
                    for (RadioButton rb : anthelminticAlbendazoleDispersedAntibiotic.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_incomplete)) && obs[0][1].equals("INCOMPLETE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    anthelminticAlbendazoleDispersedAntibiotic.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("NEXT DATE OF DRUG DISPERSAL")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                nextDateOfDrug.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                nextDateOfDrug.setVisibility(View.VISIBLE);
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
        }
        if (view == nextDateOfDrug.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == typeFixedDosePrescribedIntensive.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_current_formulation))) {
                if (!App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rhz))) {
                    currentTabletsofE.setVisibility(View.VISIBLE);
                }
                currentTabletsofRHZ.setVisibility(View.VISIBLE);

                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
            } else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_new_formulation))) {
                if (!App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rhz))) {
                    newTabletsofE.setVisibility(View.VISIBLE);
                }
                newTabletsofRHZ.setVisibility(View.VISIBLE);

                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
            } else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_adult_formulation))) {
                if (!App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rhz))) {
                    adultFormulationofHRZE.setVisibility(View.VISIBLE);
                }
                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
            }
        } else if (spinner == typeFixedDosePrescribedContinuation.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_current_formulation_continuation))) {
                if (App.get(continuationPhaseRegimen).equals(getResources().getString(R.string.ctb_rhe)) || App.get(continuationPhaseRegimen).isEmpty()) {
                    currentTabletsOfContinuationE.setVisibility(View.VISIBLE);
                }
                currentTabletsOfContinuationRH.setVisibility(View.VISIBLE);

                newTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE_E.setVisibility(View.GONE);
            } else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_new_formulation_continuation))) {
                if (App.get(continuationPhaseRegimen).equals(getResources().getString(R.string.ctb_rhe)) || App.get(continuationPhaseRegimen).isEmpty()) {
                    newTabletsOfContinuationE.setVisibility(View.VISIBLE);
                }
                newTabletsOfContinuationRH.setVisibility(View.VISIBLE);

                currentTabletsOfContinuationE.setVisibility(View.GONE);
                currentTabletsOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE_E.setVisibility(View.GONE);
            } else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_adult_formulation_continuation_rh))) {
                adultFormulationOfContinuationRH.setVisibility(View.VISIBLE);
                adultFormulationOfContinuationRHE_E.setVisibility(View.VISIBLE);

                currentTabletsOfContinuationE.setVisibility(View.GONE);
                currentTabletsOfContinuationRH.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE_RHE.setVisibility(View.GONE);
            } else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_adult_formulation_continuation_rhe))) {
                adultFormulationOfContinuationRHE_RHE.setVisibility(View.VISIBLE);

                currentTabletsOfContinuationE.setVisibility(View.GONE);
                currentTabletsOfContinuationRH.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE_E.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(App.get(patientHaveTb).equalsIgnoreCase(getResources().getString(R.string.yes))) {
            for (CheckBox cb : moAdditionalTreatment.getCheckedBoxes()) {
                if (App.get(cb).equals(getResources().getString(R.string.ctb_pediasure))) {
                    if (cb.isChecked()) {
                        pediasureDispersed.setVisibility(View.VISIBLE);

                    } else {
                        pediasureDispersed.setVisibility(View.GONE);
                    }
                } else if (App.get(cb).equals(getResources().getString(R.string.ctb_vitamin_B_complex))) {
                    if (cb.isChecked()) {
                        vitaminBComplexDispersed.setVisibility(View.VISIBLE);

                    } else {
                        vitaminBComplexDispersed.setVisibility(View.GONE);
                    }
                } else if (App.get(cb).equals(getResources().getString(R.string.ctb_iron))) {
                    if (cb.isChecked()) {
                        ironDispersed.setVisibility(View.VISIBLE);

                    } else {
                        ironDispersed.setVisibility(View.GONE);
                    }
                } else if (App.get(cb).equals(getResources().getString(R.string.ctb_anthelminthic))) {
                    if (cb.isChecked()) {
                        anthelminthicDispersed.setVisibility(View.VISIBLE);

                    } else {
                        anthelminthicDispersed.setVisibility(View.GONE);
                    }
                }
            }
        }

        if(App.get(patientHaveTb).equalsIgnoreCase(getResources().getString(R.string.no))) {
            for (CheckBox cb : moInitiateTreatmentIpt.getCheckedBoxes()) {
                if (App.get(cb).equals(getResources().getString(R.string.ctb_iron))) {
                    if (cb.isChecked()) {
                        ironDispersedIpt.setVisibility(View.VISIBLE);

                    } else {
                        ironDispersedIpt.setVisibility(View.GONE);
                    }
                } else if (App.get(cb).equals(getResources().getString(R.string.ctb_multivitamins))) {
                    if (cb.isChecked()) {
                        multivitaminsDispersed.setVisibility(View.VISIBLE);

                    } else {
                        multivitaminsDispersed.setVisibility(View.GONE);
                    }
                } else if (App.get(cb).equals(getResources().getString(R.string.ctb_anthelmintic_albendazole))) {
                    if (cb.isChecked()) {
                        anthelminticAlbendazoleDispersed.setVisibility(View.VISIBLE);

                    } else {
                        anthelminticAlbendazoleDispersed.setVisibility(View.GONE);
                    }
                }
            }
        }
        if(App.get(patientHaveTb).equalsIgnoreCase(getResources().getString(R.string.ctb_inconclusive)))
            for (CheckBox cb : moInitiatingAdditionalTreatmentAntibiotic.getCheckedBoxes()) {
                if(App.get(cb).equals(getResources().getString(R.string.ctb_iron))) {
                    if (cb.isChecked()) {
                        ironDispersedAntibiotic.setVisibility(View.VISIBLE);

                    } else {
                        ironDispersedAntibiotic.setVisibility(View.GONE);
                    }
                }else if(App.get(cb).equals(getResources().getString(R.string.ctb_multivitamins))){
                    if (cb.isChecked()) {
                        multivitaminsDispersedAntibiotic.setVisibility(View.VISIBLE);

                    } else {
                        multivitaminsDispersedAntibiotic.setVisibility(View.GONE);
                    }
                }else if(App.get(cb).equals(getResources().getString(R.string.ctb_anthelminthic))){
                    if (cb.isChecked()) {
                        anthelminticAlbendazoleDispersedAntibiotic.setVisibility(View.VISIBLE);

                    } else {
                        anthelminticAlbendazoleDispersed.setVisibility(View.GONE);
                    }
                }
            }

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        treatmentPlan.setVisibility(View.GONE);
        intensivePhaseRegimen.setVisibility(View.GONE);
        typeFixedDosePrescribedIntensive.setVisibility(View.GONE);
        currentTabletsofRHZ.setVisibility(View.GONE);
        currentTabletsofE.setVisibility(View.GONE);
        newTabletsofRHZ.setVisibility(View.GONE);
        newTabletsofE.setVisibility(View.GONE);
        adultFormulationofHRZE.setVisibility(View.GONE);
        continuationPhaseRegimen.setVisibility(View.GONE);
        typeFixedDosePrescribedContinuation.setVisibility(View.GONE);
        currentTabletsOfContinuationRH.setVisibility(View.GONE);
        currentTabletsOfContinuationE.setVisibility(View.GONE);
        newTabletsOfContinuationRH.setVisibility(View.GONE);
        newTabletsOfContinuationE.setVisibility(View.GONE);
        adultFormulationOfContinuationRH.setVisibility(View.GONE);
        adultFormulationOfContinuationRHE_E.setVisibility(View.GONE);
        adultFormulationOfContinuationRHE_RHE.setVisibility(View.GONE);
        moAdditionalTreatment.setVisibility(View.GONE);
        pediasureDispersed.setVisibility(View.GONE);
        vitaminBComplexDispersed.setVisibility(View.GONE);
        ironDispersed.setVisibility(View.GONE);
        anthelminthicDispersed.setVisibility(View.GONE);
        iptDose.setVisibility(View.GONE);
        iptDrugDispersed.setVisibility(View.GONE);
        moInitiateTreatmentIpt.setVisibility(View.GONE);
        ironDispersedIpt.setVisibility(View.GONE);
        multivitaminsDispersed.setVisibility(View.GONE);
        anthelminticAlbendazoleDispersed.setVisibility(View.GONE);
        antibioticTrialDispersed.setVisibility(View.GONE);
        moInitiatingAdditionalTreatmentAntibiotic.setVisibility(View.GONE);
        ironDispersedAntibiotic.setVisibility(View.GONE);
        multivitaminsDispersedAntibiotic.setVisibility(View.GONE);
        anthelminticAlbendazoleDispersedAntibiotic.setVisibility(View.GONE);

        String patientHaveTbString = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "PATIENT HAVE TB");
        if (patientHaveTbString != null) {
            for (RadioButton rb : patientHaveTb.getRadioGroup().getButtons()) {
                if (rb.getText().equals(getResources().getString(R.string.yes)) && patientHaveTbString.equals("YES")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.no)) && patientHaveTbString.equals("NO")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_inconclusive)) && patientHaveTbString.equals("INCONCLUSIVE")) {
                    rb.setChecked(true);
                    break;
                }
            }
        }
        String treatmentPlanString = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "TB Treatment Followup", "TREATMENT PLAN");
        if (treatmentPlanString != null) {
            for (RadioButton rb : treatmentPlan.getRadioGroup().getButtons()) {
                if (rb.getText().equals(getResources().getString(R.string.ctb_intensive_phase)) && treatmentPlanString.equals("INTENSIVE PHASE")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_continuation_phase)) && treatmentPlanString.equals("CONTINUE REGIMEN")) {
                    rb.setChecked(true);
                    break;
                }
            }
        }
        String date1 = "";
        String date2 = "";
        String ctbRegimen1 = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.CHILDHOODTB_TREATMENT_INITIATION, "REGIMEN");
        String ctbTypeOfDose1 = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.CHILDHOODTB_TREATMENT_INITIATION, "PAEDIATRIC DOSE COMBINATION");

        if (!(ctbRegimen1 == null || ctbRegimen1.equals("")))
            date1 = serverService.getEncounterDateTime(App.getPatientId(), App.getProgram() + "-" + Forms.CHILDHOODTB_TREATMENT_INITIATION);
        String ctbRegimen2 = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.CHILDHOODTB_TB_TREATMENT_FOLLOWUP, "REGIMEN");
        String ctbTypeOfDose2 = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.CHILDHOODTB_TB_TREATMENT_FOLLOWUP, "PAEDIATRIC DOSE COMBINATION");

        if (!(ctbRegimen2 == null || ctbRegimen2.equals("")))
            date2 = serverService.getEncounterDateTime(App.getPatientId(), App.getProgram() + "-" + Forms.CHILDHOODTB_TB_TREATMENT_FOLLOWUP);
        if(!date2.equals("") || !date2.equals("")) {
            Date d1 = null;
            Date d2 = null;
            if (date1.contains("/")) {
                d1 = App.stringToDate(date1, "dd/MM/yyyy");
            } else {
                d1 = App.stringToDate(date1, "yyyy-MM-dd");
            }

            if (date2.contains("/")) {
                d2 = App.stringToDate(date2, "dd/MM/yyyy");
            } else {
                d2 = App.stringToDate(date2, "yyyy-MM-dd");
            }
            if (d2.after(d1)) {
                for (RadioButton rb : intensivePhaseRegimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_rhze)) && ctbRegimen2.equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhz)) && ctbRegimen2.equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if(d2.equals(d1)) {
                for (RadioButton rb : intensivePhaseRegimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_rhze)) && ctbRegimen2.equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhz)) && ctbRegimen2.equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else {
                for (RadioButton rb : intensivePhaseRegimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_rhze)) && ctbRegimen1.equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhz)) && ctbRegimen1.equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
        }else if(date1!=null && date2==null) {
            for (RadioButton rb : intensivePhaseRegimen.getRadioGroup().getButtons()) {
                if (rb.getText().equals(getResources().getString(R.string.ctb_rhze)) && ctbRegimen1.equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhz)) && ctbRegimen1.equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                    rb.setChecked(true);
                    break;
                }
            }
        }else if(date1==null && date2!=null) {
            for (RadioButton rb : intensivePhaseRegimen.getRadioGroup().getButtons()) {
                if (rb.getText().equals(getResources().getString(R.string.ctb_rhze)) && ctbRegimen2.equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhz)) && ctbRegimen2.equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                    rb.setChecked(true);
                    break;
                }
            }
        }
        String continuationRegimenString = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "TB Treatment Followup", "REGIMEN");
        if(continuationRegimenString!=null){
                    for (RadioButton rb : continuationPhaseRegimen.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_rh)) && continuationRegimenString.equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhe)) && continuationRegimenString.equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                if(App.get(treatmentPlan).equalsIgnoreCase(getResources().getString(R.string.ctb_continuation_phase))) {
                    continuationPhaseRegimen.setVisibility(View.VISIBLE);
                }
        }
        String typeOfDoseContinuationString = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "TB Treatment Followup", "PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE");
        if(typeOfDoseContinuationString!=null){
            String value = typeOfDoseContinuationString.equals("CURRENT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_current_formulation_continuation) :
                    (typeOfDoseContinuationString.equals("NEW FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_new_formulation_continuation) :
                            (typeOfDoseContinuationString.equals("ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_adult_formulation_continuation_rhe) :
                                    getResources().getString(R.string.ctb_adult_formulation_continuation_rh)));

            typeFixedDosePrescribedContinuation.getSpinner().selectValue(value);
        }
        String additionalTreatmentString = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "ADDITIONAL TREATMENT TO TB PATIENT");
        if(additionalTreatmentString!=null){
            for (CheckBox cb : moAdditionalTreatment.getCheckedBoxes()) {
                if (cb.getText().equals(getResources().getString(R.string.ctb_pediasure)) && additionalTreatmentString.equals("PEDIASURE")) {
                    cb.setChecked(true);
                    break;
                } else if (cb.getText().equals(getResources().getString(R.string.ctb_iron)) && additionalTreatmentString.equals("IRON")) {
                    cb.setChecked(true);
                    break;
                } else if (cb.getText().equals(getResources().getString(R.string.ctb_vitamin_B_complex)) && additionalTreatmentString.equals("VITAMIN B COMPLEX")) {
                    cb.setChecked(true);
                    break;
                } else if (cb.getText().equals(getResources().getString(R.string.ctb_anthelminthic)) && additionalTreatmentString.equals("ANTHELMINTHIC")) {
                    cb.setChecked(true);
                    break;
                }
            }


        }
        String iptDoseString = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "IPT DOSE");
        if(iptDoseString!=null){
            for (RadioButton rb : iptDose.getRadioGroup().getButtons()) {
                if (rb.getText().equals(getResources().getString(R.string.ctb_quater_per_day)) && iptDoseString.equals("1/4 TAB ONCE ADAY")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_half_per_day)) && iptDoseString.equals("1/2 TAB ONCE A DAY")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_per_day)) && iptDoseString.equals("1 TAB ONCE A DAY")) {
                    rb.setChecked(true);
                    break;
                }
            }
        }
        String additionalTreatmentIpt = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "ADDITIONAL TREATMENT IPT PATIENT");
        if(additionalTreatmentIpt!=null) {
            for (CheckBox cb : moInitiatingAdditionalTreatmentAntibiotic.getCheckedBoxes()) {
                if (cb.getText().equals(getResources().getString(R.string.ctb_iron)) && additionalTreatmentIpt.equals("IRON")) {
                    cb.setChecked(true);
                    break;
                } else if (cb.getText().equals(getResources().getString(R.string.ctb_multivitamins)) && additionalTreatmentIpt.equals("MULTIVITAMIN")) {
                    cb.setChecked(true);
                    break;
                } else if (cb.getText().equals(getResources().getString(R.string.ctb_anthelmintic_albendazole)) && additionalTreatmentIpt.equals("ANTHELMINTHIC")) {
                    cb.setChecked(true);
                    break;
                }
            }
        }
        String additionalTreatmentAntibiotic = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "ADDITIONAL TREATMENT FOR INCONCLUSIVE PATIENT");
        if(additionalTreatmentAntibiotic!=null) {
            for (CheckBox cb : moInitiatingAdditionalTreatmentAntibiotic.getCheckedBoxes()) {
                if (cb.getText().equals(getResources().getString(R.string.ctb_iron)) && additionalTreatmentAntibiotic.equals("IRON")) {
                    cb.setChecked(true);
                    break;
                } else if (cb.getText().equals(getResources().getString(R.string.ctb_multivitamins)) && additionalTreatmentAntibiotic.equals("MULTIVITAMIN")) {
                    cb.setChecked(true);
                    break;
                } else if (cb.getText().equals(getResources().getString(R.string.ctb_anthelminthic)) && additionalTreatmentAntibiotic.equals("ANTHELMINTHIC")) {
                    cb.setChecked(true);
                    break;
                } else if (cb.getText().equals(getResources().getString(R.string.ctb_other_title)) && additionalTreatmentAntibiotic.equals("OTHER")) {
                    cb.setChecked(true);
                    break;
                } else if (cb.getText().equals(getResources().getString(R.string.ctb_none)) && additionalTreatmentAntibiotic.equals("NONE")) {
                    cb.setChecked(true);
                    break;
                }
            }
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

    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar;
            if (getArguments().getInt("type") == DATE_DIALOG_ID)
                calendar = formDateCalendar;
            else if (getArguments().getInt("type") == SECOND_DATE_DIALOG_ID)
                calendar = secondDateCalendar;

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
            updateDisplay();

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == patientHaveTb.getRadioGroup()) {
            int patientAge = App.getPatient().getPerson().getAge();
            if (patientHaveTb.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                treatmentPlan.setVisibility(View.VISIBLE);
                moAdditionalTreatment.setVisibility(View.VISIBLE);

                iptDose.setVisibility(View.GONE);
                iptDrugDispersed.setVisibility(View.GONE);
                moInitiateTreatmentIpt.setVisibility(View.GONE);
                for (CheckBox cb : moInitiateTreatmentIpt.getCheckedBoxes())
                    cb.setChecked(false);
                multivitaminsDispersed.setVisibility(View.GONE);
                ironDispersedIpt.setVisibility(View.GONE);
                anthelminticAlbendazoleDispersed.setVisibility(View.GONE);
                antibioticTrialDispersed.setVisibility(View.GONE);
                moInitiatingAdditionalTreatmentAntibiotic.setVisibility(View.GONE);
                for (CheckBox cb : moInitiatingAdditionalTreatmentAntibiotic.getCheckedBoxes())
                    cb.setChecked(false);
                ironDispersedAntibiotic.setVisibility(View.GONE);
                multivitaminsDispersedAntibiotic.setVisibility(View.GONE);
                anthelminticAlbendazoleDispersedAntibiotic.setVisibility(View.GONE);


            } else if (patientHaveTb.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))) {
                if (patientAge <= 5) {
                    iptDose.setVisibility(View.VISIBLE);
                    if(!App.get(iptDose).isEmpty()){
                        iptDrugDispersed.setVisibility(View.VISIBLE);
                    }
                    moInitiateTreatmentIpt.setVisibility(View.VISIBLE);

                } else {
                    iptDose.setVisibility(View.GONE);
                    moInitiateTreatmentIpt.setVisibility(View.GONE);
                }
                treatmentPlan.setVisibility(View.GONE);
                intensivePhaseRegimen.setVisibility(View.GONE);
                typeFixedDosePrescribedIntensive.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
                continuationPhaseRegimen.setVisibility(View.GONE);
                typeFixedDosePrescribedContinuation.setVisibility(View.GONE);
                currentTabletsOfContinuationRH.setVisibility(View.GONE);
                currentTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE_E.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE_RHE.setVisibility(View.GONE);
                moAdditionalTreatment.setVisibility(View.GONE);
                for (CheckBox cb : moAdditionalTreatment.getCheckedBoxes())
                    cb.setChecked(false);
                pediasureDispersed.setVisibility(View.GONE);
                vitaminBComplexDispersed.setVisibility(View.GONE);
                ironDispersed.setVisibility(View.GONE);
                anthelminthicDispersed.setVisibility(View.GONE);
                antibioticTrialDispersed.setVisibility(View.GONE);
                antibioticTrialDispersed.setVisibility(View.GONE);
                moInitiatingAdditionalTreatmentAntibiotic.setVisibility(View.GONE);
                for (CheckBox cb : moInitiatingAdditionalTreatmentAntibiotic.getCheckedBoxes())
                    cb.setChecked(false);
                ironDispersedAntibiotic.setVisibility(View.GONE);
                multivitaminsDispersedAntibiotic.setVisibility(View.GONE);
                anthelminticAlbendazoleDispersedAntibiotic.setVisibility(View.GONE);
            } else if (patientHaveTb.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_inconclusive))) {
                antibioticTrialDispersed.setVisibility(View.VISIBLE);
                if(!App.get(antibioticTrialDispersed).isEmpty()) {
                    moInitiatingAdditionalTreatmentAntibiotic.setVisibility(View.VISIBLE);
                }
                treatmentPlan.setVisibility(View.GONE);
                intensivePhaseRegimen.setVisibility(View.GONE);
                typeFixedDosePrescribedIntensive.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
                continuationPhaseRegimen.setVisibility(View.GONE);
                typeFixedDosePrescribedContinuation.setVisibility(View.GONE);
                currentTabletsOfContinuationRH.setVisibility(View.GONE);
                currentTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE_E.setVisibility(View.GONE);
                moAdditionalTreatment.setVisibility(View.GONE);
                for (CheckBox cb : moAdditionalTreatment.getCheckedBoxes())
                    cb.setChecked(false);
                pediasureDispersed.setVisibility(View.GONE);
                vitaminBComplexDispersed.setVisibility(View.GONE);
                ironDispersed.setVisibility(View.GONE);
                anthelminthicDispersed.setVisibility(View.GONE);
                iptDose.setVisibility(View.GONE);
                iptDrugDispersed.setVisibility(View.GONE);
                moInitiateTreatmentIpt.setVisibility(View.GONE);
                for (CheckBox cb : moInitiateTreatmentIpt.getCheckedBoxes())
                    cb.setChecked(false);
                multivitaminsDispersed.setVisibility(View.GONE);
                ironDispersedIpt.setVisibility(View.GONE);
                anthelminticAlbendazoleDispersed.setVisibility(View.GONE);
            }
        } else if (group == treatmentPlan.getRadioGroup()) {
            if (treatmentPlan.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_intensive_phase))) {
                intensivePhaseRegimen.setVisibility(View.VISIBLE);
                typeFixedDosePrescribedIntensive.setVisibility(View.VISIBLE);


                continuationPhaseRegimen.setVisibility(View.GONE);
                typeFixedDosePrescribedContinuation.setVisibility(View.GONE);
                currentTabletsOfContinuationRH.setVisibility(View.GONE);
                currentTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE_E.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE_RHE.setVisibility(View.GONE);
            } else if (treatmentPlan.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_continuation_phase))) {
                continuationPhaseRegimen.setVisibility(View.VISIBLE);
                typeFixedDosePrescribedContinuation.setVisibility(View.VISIBLE);
                currentTabletsOfContinuationRH.setVisibility(View.VISIBLE);
                currentTabletsOfContinuationE.setVisibility(View.VISIBLE);

                intensivePhaseRegimen.setVisibility(View.GONE);
                typeFixedDosePrescribedIntensive.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);

            }
        } else if (group == intensivePhaseRegimen.getRadioGroup()) {
            if (intensivePhaseRegimen.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_rhz))) {
                adultFormulationofHRZE.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
            } else {
                if (App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_adult_formulation))) {
                    adultFormulationofHRZE.setVisibility(View.VISIBLE);
                } else if (App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_current_formulation))) {
                    currentTabletsofE.setVisibility(View.VISIBLE);
                } else if (App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_new_formulation))) {
                    newTabletsofE.setVisibility(View.VISIBLE);
                }
            }
        } else if (group == continuationPhaseRegimen.getRadioGroup()) {
            if (continuationPhaseRegimen.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_rh))) {
                currentTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
            } else {
                if (App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_current_formulation_continuation))) {
                    currentTabletsOfContinuationE.setVisibility(View.VISIBLE);
                } else if (App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_new_formulation_continuation))) {
                    newTabletsOfContinuationE.setVisibility(View.VISIBLE);
                } else if (App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_adult_formulation_continuation_rh))) {
                    adultFormulationOfContinuationRH.setVisibility(View.VISIBLE);
                } else if (App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_adult_formulation_continuation_rhe))) {
                    adultFormulationOfContinuationRHE_E.setVisibility(View.VISIBLE);
                }
            }
        }else if(group==iptDose.getRadioGroup()){
            if(iptDose.getRadioGroup().getSelectedValue().length()>0){
                iptDrugDispersed.setVisibility(View.VISIBLE);
            }
        }else if(group==antibioticTrialDispersed.getRadioGroup()){
            if(antibioticTrialDispersed.getRadioGroup().getSelectedValue().length()>0){
                moInitiatingAdditionalTreatmentAntibiotic.setVisibility(View.VISIBLE);
            }
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