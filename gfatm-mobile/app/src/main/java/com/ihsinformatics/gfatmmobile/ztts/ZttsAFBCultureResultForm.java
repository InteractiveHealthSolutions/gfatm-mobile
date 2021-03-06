package com.ihsinformatics.gfatmmobile.ztts;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
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
 * Created by Haris on 1/5/2017.
 */

public class ZttsAFBCultureResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    Boolean emptyError = false;

    public static final int THIRD_DATE_DIALOG_ID = 3;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;
    Boolean dateChoose = false;

    // Views...
    TitledButton formDate;
    TitledSpinner orderIds;
    TitledRadioGroup sample_afb_culture;
    TitledSpinner test_ordered;
    TitledRadioGroup sample_reject;
    TitledEditText sample_id;
    TitledRadioGroup culture_done;
    TitledButton culture_test_date;
    TitledEditText culture_test_lab_id;
    TitledRadioGroup typeof_culture_med;
    TitledEditText culture_med_other;
    TitledRadioGroup culture_result;
    TitledRadioGroup culture_colony;
    TitledButton culture_result_date;


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 1;
        formName = Forms.ZTTS_AFB_CULTURE_RESULT;
        form = Forms.ztts_afbCultureResultForm;

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
        thirdDateFragment = new ZttsAFBCultureResultForm.SelectDateFragment();
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        orderIds = new TitledSpinner(context, "", getResources().getString(R.string.order_id), getResources().getStringArray(R.array.ztts_empty_array), "", App.HORIZONTAL, true);
        sample_afb_culture = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_sample_afb_culture), getResources().getStringArray(R.array.ztts_sample_g_x_options), "", App.VERTICAL, App.VERTICAL, true);
        test_ordered = new TitledSpinner(context, "", getResources().getString(R.string.ztts_test_order), getResources().getStringArray(R.array.ztts_test_order_options), "", App.HORIZONTAL);
        sample_reject = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_sample_reject), getResources().getStringArray(R.array.ztts_yes_no), getString(R.string.no), App.VERTICAL, App.VERTICAL, true);
        sample_id = new TitledEditText(context, null, getResources().getString(R.string.ztts_sample_id), "", "", 25, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        culture_done = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_culture_done), getResources().getStringArray(R.array.ztts_yes_no), getString(R.string.yes), App.VERTICAL, App.VERTICAL, false);
        culture_test_date = new TitledButton(context, null, getResources().getString(R.string.ztts_date_culture_test), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        culture_test_lab_id = new TitledEditText(context, null, getResources().getString(R.string.ztts_culture_test_lab_id), "", "", 25, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        typeof_culture_med = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_typeof_culture_med), getResources().getStringArray(R.array.ztts_typeof_culture_med_options), getString(R.string.ztts_typeof_culture_med_lowenstein), App.VERTICAL, App.VERTICAL, false);
        culture_med_other = new TitledEditText(context, null, getResources().getString(R.string.ztts_typeof_culture_med_if_other), "", "", 100, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        culture_result = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_culture_result), getResources().getStringArray(R.array.ztts_culture_result_options), getString(R.string.ztts_culture_result_positive), App.VERTICAL, App.VERTICAL, false);
        culture_colony = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_culture_colony), getResources().getStringArray(R.array.ztts_culture_colony_options), getString(R.string.ztts_culture_colony_not_done), App.VERTICAL, App.VERTICAL, true);
        culture_result_date = new TitledButton(context, null, getResources().getString(R.string.ztts_culture_result_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), orderIds.getSpinner(), sample_afb_culture.getRadioGroup(), test_ordered.getSpinner(), sample_reject.getRadioGroup()
                , sample_id.getEditText(), culture_done.getRadioGroup(), culture_test_date.getButton(), culture_test_lab_id.getEditText(), typeof_culture_med.getRadioGroup(), culture_med_other.getEditText(),
                culture_result.getRadioGroup(), culture_colony.getRadioGroup(), culture_result_date.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, orderIds, sample_afb_culture, test_ordered, sample_reject, sample_id, culture_done, culture_test_date, culture_test_lab_id, typeof_culture_med, culture_med_other,
                        culture_result, culture_colony, culture_result_date}};


        formDate.getButton().setOnClickListener(this);
        culture_test_date.getButton().setOnClickListener(this);
        culture_result_date.getButton().setOnClickListener(this);
        orderIds.getSpinner().setOnItemSelectedListener(this);
        sample_reject.getRadioGroup().setOnCheckedChangeListener(this);
        culture_done.getRadioGroup().setOnCheckedChangeListener(this);
        typeof_culture_med.getRadioGroup().setOnCheckedChangeListener(this);
        culture_result.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {
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
                if (!App.get(orderIds).equals("")) {
                    String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), Forms.ZTTS_SAMPLE_COLLECTION, "AFB CULTURE ORDER ID", App.get(orderIds));

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

        if (!(culture_test_date.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = culture_test_date.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                culture_test_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                culture_test_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else {
                culture_test_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                if (!App.get(orderIds).equals("")) {
                    String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), Forms.ZTTS_SAMPLE_COLLECTION, "AFB CULTURE ORDER ID", App.get(orderIds));

                    String format = "";
                    if (encounterDateTime.contains("/")) {
                        format = "dd/MM/yyyy";
                    } else {
                        format = "yyyy-MM-dd";
                    }

                    Date orderDate = App.stringToDate(encounterDateTime, format);

                    if (secondDateCalendar.before(App.getCalendar(orderDate))) {

                        Date dDate = App.stringToDate(formDa, "EEEE, MMM dd,yyyy");
                        if (dDate.before(orderDate)) {
                            secondDateCalendar = Calendar.getInstance();
                            culture_test_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                        } else {
                            secondDateCalendar = App.getCalendar(dDate);
                            culture_test_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                        }

                        snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_result_date_cannot_be_before_order_date), Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();

                    }
                }

            }
        }

        if (!(culture_result_date.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString()))) {

            String formDa = culture_result_date.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            if (thirdDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                culture_result_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            } else {
                culture_result_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
                if (!App.get(orderIds).equals("")) {
                    String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), Forms.ZTTS_SAMPLE_COLLECTION, "AFB CULTURE ORDER ID", App.get(orderIds));

                    String format = "";
                    if (encounterDateTime.contains("/")) {
                        format = "dd/MM/yyyy";
                    } else {
                        format = "yyyy-MM-dd";
                    }

                    Date orderDate = App.stringToDate(encounterDateTime, format);

                    if (thirdDateCalendar.before(App.getCalendar(orderDate))) {

                        Date dDate = App.stringToDate(formDa, "EEEE, MMM dd,yyyy");
                        if (dDate.before(orderDate)) {
                            thirdDateCalendar = Calendar.getInstance();
                            culture_result_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
                        } else {
                            thirdDateCalendar = App.getCalendar(dDate);
                            culture_result_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
                        }

                        snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_result_date_cannot_be_before_order_date), Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();

                    }
                }
            }
        }
        dateChoose = false;
        formDate.getButton().setEnabled(true);
        culture_test_date.getButton().setEnabled(true);
        culture_result_date.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (sample_afb_culture.getVisibility() == View.VISIBLE && App.get(sample_afb_culture).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            sample_afb_culture.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            sample_afb_culture.getQuestionView().setError(null);
        }

        if (sample_reject.getVisibility() == View.VISIBLE && App.get(sample_reject).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            sample_reject.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            sample_reject.getQuestionView().setError(null);
        }

        if (culture_colony.getVisibility() == View.VISIBLE && App.get(culture_colony).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            culture_colony.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            culture_colony.getQuestionView().setError(null);
        }
       /* if (sample_id.getVisibility() == View.VISIBLE && sample_id.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            sample_id.getEditText().setError(getString(R.string.empty_field));
            sample_id.getEditText().requestFocus();
            error = true;
        }*/
        if (culture_med_other.getVisibility() == View.VISIBLE && culture_med_other.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            culture_med_other.getEditText().setError(getString(R.string.empty_field));
            culture_med_other.getEditText().requestFocus();
            error = true;
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

            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), Forms.ZTTS_AFB_CULTURE_RESULT, "AFB CULTURE ORDER ID");

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
        if (orderIds.getSpinner().getSelectedItem().toString().contains("EM"))
            if (!(orderIds.getSpinner().getSelectedItem().toString().contains("EM") && sample_afb_culture.getRadioGroup().getSelectedValue().equals(getString(R.string.ztts_sputum_specimen_type_early_morning)))) {
                error = true;
                sample_afb_culture.getQuestionView().setError("sample type should match with order id");

            } else {
                sample_afb_culture.getQuestionView().setError(null);

            }
        if (orderIds.getSpinner().getSelectedItem().toString().contains("OS"))
            if (!(orderIds.getSpinner().getSelectedItem().toString().contains("OS") && sample_afb_culture.getRadioGroup().getSelectedValue().equals(getString(R.string.ztts_sputum_specimen_type_on_spot)))) {
                error = true;
                sample_afb_culture.getQuestionView().setError("sample type should match with order id");
            } else {
                sample_afb_culture.getQuestionView().setError(null);

            }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
            if (!emptyError)
                alertDialog.setMessage(getString(R.string.form_error));
            else
                alertDialog.setMessage(getString(R.string.fast_required_field_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            //   DrawableCompat.setTint(clearIcon, color);
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

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if(!flag){

                    final androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.form_does_not_exist));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    bundle.putBoolean("save", false);
                                    submit();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
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

        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});

        if (orderIds.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"AFB CULTURE ORDER ID", App.get(orderIds)});
        }

        if (sample_afb_culture.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SAMPLE COLLECTION FOR AFB CULTURE", App.get(sample_afb_culture).equals(getResources().getString(R.string.ztts_sample_afb_secon_spot)) ? "ON SPOT SAMPLE" : "EARLY MORNING SAMPLE"});

        if (test_ordered.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"TESTS ORDERED", "CULTURE FOR MYCOBACTERIA"});
        }

        if (sample_reject.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SAMPLE REJECTED", App.get(sample_reject).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (sample_id.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SPECIMEN ID", App.get(sample_id)});

        if (culture_done.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CULTURE FOR MYCOBACTERIA", App.get(culture_done).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (culture_test_date.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"AFB CULTURE TEST DATE", App.getSqlDateTime(secondDateCalendar)});

        if (culture_test_lab_id.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"AFB CULTURE TEST ID", App.get(culture_test_lab_id)});

        if (typeof_culture_med.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CULTURE MEDIUM TYPE", App.get(typeof_culture_med).equals(getResources().getString(R.string.ztts_typeof_culture_med_lowenstein)) ? "LOWENSTEIN-JENSEN MYCOBACTERIA CULTURE METHOD" :
                    App.get(typeof_culture_med).equals(getResources().getString(R.string.ztts_typeof_culture_med_mycobacteria)) ? "MYCOBACTERIA GROWTH INDICATOR TUBE" :
                            App.get(typeof_culture_med).equals(getResources().getString(R.string.ztts_typeof_culture_med_middlebrook)) ? "MIDDLEBROOK 7H11S" :
                                    App.get(typeof_culture_med).equals(getResources().getString(R.string.ztts_typeof_culture_med_total)) ? "TOTAL LABORATORY AUTOMATION" : "OTHER DST MEDIUM"});

        if (culture_med_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER DST MEDIUM", App.get(culture_med_other)});


        if (culture_result.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"AFB CULTURE RESULTS", App.get(culture_result).equals(getResources().getString(R.string.ztts_culture_result_positive)) ? "POSITIVE FOR MYCOBACTERIUM TUBERCULOSIS COMPLEX" :
                    App.get(culture_result).equals(getResources().getString(R.string.ztts_culture_result_negitive)) ? "NEGATIVE FOR MYCOBACTERIUM TUBERCULOSIS COMPLEX" :
                            App.get(culture_result).equals(getResources().getString(R.string.ztts_culture_result_only_positive)) ? "ONLY POSITIVE FOR NON-TUBERCULOSIS MYCOBACTERIA" :
                                    App.get(culture_result).equals(getResources().getString(R.string.ztts_culture_result_contaminated)) ? "CONTAMINATED SPECIMEN" : "OTHER"});

        if (culture_colony.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COLONY COUNT", App.get(culture_colony).equals(getResources().getString(R.string.ztts_culture_colony_lessthan10)) ? "<10 COLONIES" :
                    App.get(culture_colony).equals(getResources().getString(R.string.ztts_culture_colony_10to100)) ? "1+ (10-100)" :
                            App.get(culture_colony).equals(getResources().getString(R.string.ztts_culture_colony_greaterthan100)) ? "2+ (>100)" :
                                    App.get(culture_colony).equals(getResources().getString(R.string.ztts_culture_colony_greaterthan200)) ? "3+ (>200)" : "NOT DONE"});


        if (culture_result_date.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"AFB CULTURE RESULT DATE", App.getSqlDateTime(thirdDateCalendar)});

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
                if(App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocally(Forms.ZTTS_AFB_CULTURE_RESULT, form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveEncounterAndObservationTesting(Forms.ZTTS_AFB_CULTURE_RESULT, form, formDateCalendar, observations.toArray(new String[][]{}),id);
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
        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));


        return true;
    }

    @Override
    public void refill(int formId) {

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("AFB CULTURE ORDER ID")) {
                orderIds.getSpinner().selectValue(obs[0][1]);
                orderIds.getSpinner().setEnabled(false);

            } else if (obs[0][0].equals("SAMPLE COLLECTION FOR AFB CULTURE")) {
                for (RadioButton rb : sample_afb_culture.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_sample_g_x_early)) && obs[0][1].equals("EARLY MORNING SAMPLE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_sample_afb_secon_spot)) && obs[0][1].equals("ON SPOT SAMPLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("SAMPLE REJECTED")) {
                for (RadioButton rb : sample_reject.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("SPECIMEN ID")) {
                sample_id.getEditText().setText(obs[0][1]);

            } else if (obs[0][0].equals("CULTURE FOR MYCOBACTERIA")) {
                for (RadioButton rb : culture_done.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("AFB CULTURE TEST DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                culture_test_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                culture_test_date.setVisibility(View.VISIBLE);

            } else if (obs[0][0].equals("AFB CULTURE TEST ID")) {
                culture_test_lab_id.getEditText().setText(obs[0][1]);

            } else if (obs[0][0].equals("CULTURE MEDIUM TYPE")) {
                for (RadioButton rb : typeof_culture_med.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_typeof_culture_med_lowenstein)) && obs[0][1].equals("LOWENSTEIN-JENSEN MYCOBACTERIA CULTURE METHOD")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_typeof_culture_med_mycobacteria)) && obs[0][1].equals("MYCOBACTERIA GROWTH INDICATOR TUBE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_typeof_culture_med_middlebrook)) && obs[0][1].equals("MIDDLEBROOK 7H11S")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_typeof_culture_med_total)) && obs[0][1].equals("TOTAL LABORATORY AUTOMATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_typeof_culture_med_other)) && obs[0][1].equals("OTHER DST MEDIUM")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER DST MEDIUM")) {
                culture_med_other.getEditText().setText(obs[0][1]);

            } else if (obs[0][0].equals("AFB CULTURE RESULTS")) {
                for (RadioButton rb : typeof_culture_med.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_typeof_culture_med_lowenstein)) && obs[0][1].equals("POSITIVE FOR MYCOBACTERIUM TUBERCULOSIS COMPLEX")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_typeof_culture_med_mycobacteria)) && obs[0][1].equals("NEGATIVE FOR MYCOBACTERIUM TUBERCULOSIS COMPLEX")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_typeof_culture_med_middlebrook)) && obs[0][1].equals("ONLY POSITIVE FOR NON-TUBERCULOSIS MYCOBACTERIA")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_typeof_culture_med_total)) && obs[0][1].equals("CONTAMINATED SPECIMEN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_typeof_culture_med_other)) && obs[0][1].equals("OTHER")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COLONY COUNT")) {
                for (RadioButton rb : culture_colony.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_culture_colony_lessthan10)) && obs[0][1].equals("<10 COLONIES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_culture_colony_10to100)) && obs[0][1].equals("1+ (10-100)")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_culture_colony_greaterthan100)) && obs[0][1].equals("2+ (>100")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_culture_colony_greaterthan200)) && obs[0][1].equals("3+ (>200)")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_culture_colony_not_done)) && obs[0][1].equals("NOT DONE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("AFB CULTURE RESULT DATE")) {
                String secondDate = obs[0][1];
               /* thirdDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                culture_result_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
                culture_result_date.setVisibility(View.VISIBLE);*/
            }

        }

    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar,false,true, false);
            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);*/
        }

        if (view == culture_test_date.getButton()) {
            culture_test_date.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar,false,true, true);

            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);*/
        }

        if (view == culture_result_date.getButton()) {
            culture_result_date.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar,true,true, true);

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
        if (spinner == orderIds.getSpinner()) {
            formDateCalendar = Calendar.getInstance();
            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == sample_reject.getRadioGroup()) {
            if (sample_reject.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                sample_id.setVisibility(View.GONE);
                culture_done.setVisibility(View.GONE);
                culture_test_date.setVisibility(View.GONE);
                culture_test_lab_id.setVisibility(View.GONE);
                typeof_culture_med.setVisibility(View.GONE);
                culture_med_other.setVisibility(View.GONE);
                culture_result.setVisibility(View.GONE);
                culture_colony.setVisibility(View.GONE);
                culture_result_date.setVisibility(View.GONE);
            } else {
                sample_id.setVisibility(View.VISIBLE);
                culture_done.setVisibility(View.VISIBLE);
                if (culture_done.getRadioGroup().getSelectedValue().equals(getString(R.string.no))) {
                    culture_test_date.setVisibility(View.GONE);
                    culture_test_lab_id.setVisibility(View.GONE);
                    typeof_culture_med.setVisibility(View.GONE);
                    culture_med_other.setVisibility(View.GONE);
                    culture_result.setVisibility(View.GONE);
                    culture_colony.setVisibility(View.GONE);
                } else {
                    sample_id.setVisibility(View.VISIBLE);
                    culture_test_date.setVisibility(View.VISIBLE);
                    culture_test_lab_id.setVisibility(View.VISIBLE);
                    typeof_culture_med.setVisibility(View.VISIBLE);
                    if (App.get(typeof_culture_med).equals(getString(R.string.ztts_typeof_culture_med_other))) {
                        culture_med_other.setVisibility(View.VISIBLE);
                    } else {
                        culture_med_other.setVisibility(View.GONE);
                    }
                    culture_result.setVisibility(View.VISIBLE);
                    if (App.get(culture_result).equals(getString(R.string.ztts_culture_result_positive))) {
                        culture_colony.setVisibility(View.VISIBLE);
                    } else {
                        culture_colony.setVisibility(View.GONE);
                    }
                    culture_result_date.setVisibility(View.VISIBLE);
                }
            }
        } else if (radioGroup == culture_done.getRadioGroup()) {
            if (App.get(culture_done).equals(getString(R.string.yes))) {
                culture_test_date.setVisibility(View.VISIBLE);
                culture_test_lab_id.setVisibility(View.VISIBLE);
                typeof_culture_med.setVisibility(View.VISIBLE);
                if (App.get(typeof_culture_med).equals(getString(R.string.ztts_typeof_culture_med_other))) {
                    culture_med_other.setVisibility(View.VISIBLE);
                } else {
                    culture_med_other.setVisibility(View.GONE);
                }
                culture_result.setVisibility(View.VISIBLE);
                if (App.get(culture_result).equals(getString(R.string.ztts_culture_result_positive))) {
                    culture_colony.setVisibility(View.VISIBLE);
                } else {
                    culture_colony.setVisibility(View.GONE);
                }

            } else {
                culture_test_date.setVisibility(View.GONE);
                culture_test_lab_id.setVisibility(View.GONE);
                typeof_culture_med.setVisibility(View.GONE);
                culture_med_other.setVisibility(View.GONE);
                culture_result.setVisibility(View.GONE);
                culture_colony.setVisibility(View.GONE);
            }
        } else if (radioGroup == typeof_culture_med.getRadioGroup()) {
            if (App.get(typeof_culture_med).equals(getString(R.string.ztts_typeof_culture_med_other))) {
                culture_med_other.setVisibility(View.VISIBLE);
            } else {
                culture_med_other.setVisibility(View.GONE);

            }
        } else if (radioGroup == culture_result.getRadioGroup()) {
            if (App.get(culture_result).equals(getString(R.string.ztts_culture_result_positive))) {
                culture_colony.setVisibility(View.VISIBLE);
            } else {
                culture_colony.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    @Override
    public void resetViews() {
        super.resetViews();
        culture_med_other.setVisibility(View.GONE);
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        ArrayList<String> modifiedTestIds;
        String[] testIds = serverService.getAllObsValues(App.getPatientId(), Forms.ZTTS_SAMPLE_COLLECTION, "AFB CULTURE ORDER ID");

        if (testIds == null || testIds.length == 0) {
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
            /*modifiedTestIds = new ArrayList<>();
            for (int i = 0; i < testIds.length; i++) {
                if (testIds[i].contains("OS") && testIds[i].contains("EM")) {
                    modifiedTestIds.add("AFB_OS_" + testIds[i].split("_")[3]);
                    modifiedTestIds.add("AFB_EM_" + testIds[i].split("_")[3]);
                } else {
                    modifiedTestIds.add(testIds[i]);
                }
            }*/
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
}
