package com.ihsinformatics.gfatmmobile.pmdt;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;

/**
 * Created by Tahira on 3/20/2017.
 */

public class PmdtNutritionalSupportReceivingForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledButton visitDate;
    TitledEditText externalId;
    LinearLayout facilityLinearLayout;
    TextView treatmentFacilityText;
    AutoCompleteTextView treatmentFacilityAutoCompleteList;
    TitledEditText nationalDrTbRegistrationNumber;
    TitledEditText treatmentMonth;

    TitledEditText nutritionalSupportVoucherNumber;     // title: Nutritional support eligibility
    TitledRadioGroup showingSameVoucher;
    TitledSpinner nutritionalSupportTypeEligible;
    TitledRadioGroup glucernaGiven;
    TitledRadioGroup ensureGiven;
    TitledRadioGroup energidGiven;
    TitledRadioGroup pediasureGiven;
    TitledRadioGroup otherNutritionalSupportGiven;
    TitledEditText reasonNutritionSupportNotGiven;

    ScrollView scrollView;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PAGE_COUNT = 2;
        FORM_NAME = Forms.PMDT_NUTRITIONAL_SUPPORT_RECEIVING;
        FORM = Forms.pmdtNutritionalSupportReceiving;

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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }
        return mainContent;
    }

    @Override
    public void initViews() {
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        visitDate = new TitledButton(context, null, getResources().getString(R.string.pmdt_visit_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        externalId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_external_id), "", "", 11, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        // Fetching PMDT Locations
        String program = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            program = "pet_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            program = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            program = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            program = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            program = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocations(program);
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }

        treatmentFacilityText = new TextView(context);
        treatmentFacilityText.setText(getResources().getString(R.string.pmdt_treatment_facility));
        LinearLayout requiredTreatmentFacilityLayout = new LinearLayout(context);
        MyTextView treatmentFacilityQuestionRequired = new MyTextView(context, "*");
        int color1 = App.getColor(context, R.attr.colorAccent);
        treatmentFacilityQuestionRequired.setTextColor(color1);
        requiredTreatmentFacilityLayout.setOrientation(LinearLayout.HORIZONTAL);
        requiredTreatmentFacilityLayout.addView(treatmentFacilityQuestionRequired);
        requiredTreatmentFacilityLayout.addView(treatmentFacilityText);
        treatmentFacilityAutoCompleteList = new AutoCompleteTextView(context);
        final ArrayAdapter<String> autoCompleteFacilityAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, locationArray);
        treatmentFacilityAutoCompleteList.setAdapter(autoCompleteFacilityAdapter);
        treatmentFacilityAutoCompleteList.setHint("Enter facility");
        facilityLinearLayout = new LinearLayout(context);
        facilityLinearLayout.setOrientation(LinearLayout.VERTICAL);
        facilityLinearLayout.addView(requiredTreatmentFacilityLayout);
        facilityLinearLayout.addView(treatmentFacilityAutoCompleteList);

        nationalDrTbRegistrationNumber = new TitledEditText(context, null, getResources().getString(R.string.pmdt_national_dr_tb_registration_number), "", "", 25, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        treatmentMonth = new TitledEditText(context, null, getResources().getString(R.string.pmdt_treatment_month), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        nutritionalSupportVoucherNumber = new TitledEditText(context, getResources().getString(R.string.pmdt_title_nutrition_eligibility), getResources().getString(R.string.pmdt_nutritional_support_voucher_number), "", "", 20, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        showingSameVoucher = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_showing_same_voucher), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        nutritionalSupportTypeEligible = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_nutrition_support_type), getResources().getStringArray(R.array.pmdt_nutrition_support_types), null, App.VERTICAL, true);
        glucernaGiven = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_glucerna_given), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.HORIZONTAL, true);
        ensureGiven = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_ensure_given), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.HORIZONTAL, true);
        energidGiven = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_energid_given), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.HORIZONTAL, true);
        pediasureGiven = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_pediasure_given), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.HORIZONTAL, true);
        otherNutritionalSupportGiven = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_other_nutritioal_support_given), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        reasonNutritionSupportNotGiven = new TitledEditText(context, null, getResources().getString(R.string.pmdt_nutritional_support_not_given), "", "", 100, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), visitDate.getButton(), externalId.getEditText(), treatmentFacilityAutoCompleteList, nationalDrTbRegistrationNumber.getEditText(), treatmentMonth.getEditText(),
                nutritionalSupportVoucherNumber.getEditText(), showingSameVoucher.getRadioGroup(), nutritionalSupportTypeEligible.getSpinner(), glucernaGiven.getRadioGroup(), ensureGiven.getRadioGroup(),
                energidGiven.getRadioGroup(), pediasureGiven.getRadioGroup(), otherNutritionalSupportGiven.getRadioGroup(), reasonNutritionSupportNotGiven.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, visitDate, externalId, facilityLinearLayout, nationalDrTbRegistrationNumber, treatmentMonth},
                        {nutritionalSupportVoucherNumber, showingSameVoucher, nutritionalSupportTypeEligible, glucernaGiven, ensureGiven,
                                energidGiven, pediasureGiven, otherNutritionalSupportGiven, reasonNutritionSupportNotGiven}};

        formDate.getButton().setOnClickListener(this);
        visitDate.getButton().setOnClickListener(this);
        showingSameVoucher.getRadioGroup().setOnCheckedChangeListener(this);
        nutritionalSupportTypeEligible.getSpinner().setOnItemSelectedListener(this);
        glucernaGiven.getRadioGroup().setOnCheckedChangeListener(this);
        ensureGiven.getRadioGroup().setOnCheckedChangeListener(this);
        energidGiven.getRadioGroup().setOnCheckedChangeListener(this);
        pediasureGiven.getRadioGroup().setOnCheckedChangeListener(this);
        otherNutritionalSupportGiven.getRadioGroup().setOnCheckedChangeListener(this);

        // TODO: check if patient is eligible else show pop-up and redirect to main menu. Check concept#165536: ELIGIBLE FOR NUTRITIONAL SUPPORT

    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        visitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public boolean submit() {
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
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == visitDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", false);
            args.putBoolean("allowPastDate", true);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public void refill(int encounterId) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        visitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

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