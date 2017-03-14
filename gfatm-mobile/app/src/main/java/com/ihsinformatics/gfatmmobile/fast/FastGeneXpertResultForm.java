package com.ihsinformatics.gfatmmobile.fast;

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
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
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
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 1/24/2017.
 */

public class FastGeneXpertResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText cartridgeId;
    TitledButton dateTestResult;
    TitledSpinner gxpResult;
    TitledRadioGroup mtbBurden;
    TitledRadioGroup rifResult;
    TitledEditText errorCode;


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
        FORM_NAME = Forms.FAST_GENEXPERT_RESULT_FORM;
        FORM = Forms.fastGeneXpertResultForm;

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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        cartridgeId = new TitledEditText(context, null, getResources().getString(R.string.fast_cartridge_id), "", "", 10, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        dateTestResult = new TitledButton(context, null, getResources().getString(R.string.fast_date_of_result_recieved), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);
        gxpResult = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_genexpert_mtb_result), getResources().getStringArray(R.array.fast_genexpert_mtb_result_list), getResources().getString(R.string.fast_mtb_not_detected), App.VERTICAL);
        mtbBurden = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_mtb_burden), getResources().getStringArray(R.array.fast_mtb_burden_list), getResources().getString(R.string.fast_very_low), App.VERTICAL, App.VERTICAL);
        rifResult = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_if_mtb_then_rif_result), getResources().getStringArray(R.array.fast_if_mtb_then_rif_list), getResources().getString(R.string.fast_not_detected), App.VERTICAL, App.VERTICAL);
        errorCode = new TitledEditText(context, null, getResources().getString(R.string.fast_error_code), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), cartridgeId.getEditText(), dateTestResult.getButton(), gxpResult.getSpinner(),
                mtbBurden.getRadioGroup(), rifResult.getRadioGroup(), errorCode.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, cartridgeId, dateTestResult, gxpResult, mtbBurden, rifResult, errorCode}};

        formDate.getButton().setOnClickListener(this);
        dateTestResult.getButton().setOnClickListener(this);
        gxpResult.getSpinner().setOnItemSelectedListener(this);
        mtbBurden.getRadioGroup().setOnCheckedChangeListener(this);
        rifResult.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            }else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            }


            else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        }

        if (!(dateTestResult.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString()))) {

            String formDa = dateTestResult.getButton().getText().toString();
            String sampleSubmissionDateString = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "GXP Specimen Collection", "SPECIMEN SUBMISSION DATE");
            Date sampleSubmissionDate = (sampleSubmissionDateString != null) ? App.stringToDate(sampleSubmissionDateString, "dd MMM yyyy HH:mm:ss") : null;
            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                dateTestResult.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

            } else if (sampleSubmissionDate != null && secondDateCalendar.before(App.getCalendar(sampleSubmissionDate))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(sampleSubmissionDateString, "dd MMM yyyy HH:mm:ss"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_date_of_result_recieved_cannot_be_less_than_submission_date), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();

                dateTestResult.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

            } else
                dateTestResult.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        }


    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (cartridgeId.getVisibility() == View.VISIBLE && App.get(cartridgeId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cartridgeId.getEditText().setError(getString(R.string.empty_field));
            cartridgeId.getEditText().requestFocus();
            error = true;
        }

        if (cartridgeId.getVisibility() == View.VISIBLE && App.get(cartridgeId).length() != 10) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cartridgeId.getEditText().setError(getString(R.string.invalid_value));
            cartridgeId.getEditText().requestFocus();
            error = true;
        }

        if (errorCode.getVisibility() == View.VISIBLE && App.get(errorCode).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            errorCode.getEditText().setError(getString(R.string.empty_field));
            errorCode.getEditText().requestFocus();
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
        //  observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        //observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});
        observations.add(new String[]{"CARTRIDGE ID", App.get(cartridgeId)});
        observations.add(new String[]{"DATE OF  TEST RESULT RECEIVED", App.getSqlDateTime(secondDateCalendar)});

        if (gxpResult.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"GENEXPERT MTB/RIF RESULT", App.get(gxpResult).equals(getResources().getString(R.string.fast_mtb_detected)) ? "MYCOBACTERIUM TUBERCULOSIS DETECTED WITH RIFAMPIN RESISTANCE" :
                    (App.get(gxpResult).equals(getResources().getString(R.string.fast_mtb_not_detected)) ? "MYCOBACTERIUM TUBERCULOSIS DETECTED WITHOUT RIFAMPIN RESISTANCE" :
                            (App.get(gxpResult).equals(getResources().getString(R.string.fast_error)) ? "NEGATIVE" :
                                    (App.get(gxpResult).equals(getResources().getString(R.string.fast_invalid)) ? "INVALID" : "NO RESULT")))});

        if (mtbBurden.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"MTB BURDEN", App.get(mtbBurden).equals(getResources().getString(R.string.fast_very_low)) ? "VERY LOW" :
                    (App.get(mtbBurden).equals(getResources().getString(R.string.fast_low)) ? "LOW" :
                            (App.get(mtbBurden).equals(getResources().getString(R.string.fast_medium)) ? "MEDIUM" : "HIGH"))});

        if (rifResult.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"RIF RESISTANCE RESULT", App.get(rifResult).equals(getResources().getString(R.string.fast_not_detected)) ? "NOT DETECTED" :
                    (App.get(rifResult).equals(getResources().getString(R.string.fast_detected)) ? "DETECTED" : "INTERMEDIATE")});

        if (errorCode.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ERROR CODE", App.get(errorCode)});

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

                String result = serverService.saveEncounterAndObservation("GXP Test", FORM, formDateCalendar, observations.toArray(new String[][]{}));
                if (result.contains("SUCCESS"))
                    return "SUCCESS";

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

        serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);*/

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
            if (obs[0][0].equals("FORM START TIME")) {
                startTime = App.stringToDate(obs[0][1], "yyyy-MM-dd hh:mm:ss");
            } else if (obs[0][0].equals("CARTRIDGE ID")) {
                cartridgeId.getEditText().setText(obs[0][1]);
                cartridgeId.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("GENEXPERT MTB/RIF RESULT")) {
                String value = obs[0][1].equals("MYCOBACTERIUM TUBERCULOSIS DETECTED WITH RIFAMPIN RESISTANCE") ? getResources().getString(R.string.fast_mtb_detected) :
                        (obs[0][1].equals("MYCOBACTERIUM TUBERCULOSIS DETECTED WITHOUT RIFAMPIN RESISTANCE") ? getResources().getString(R.string.fast_mtb_not_detected) :
                                (obs[0][1].equals("NEGATIVE") ? getResources().getString(R.string.fast_error) :
                                        (obs[0][1].equals("INVALID") ? getResources().getString(R.string.fast_invalid) : getResources().getString(R.string.fast_no_result))));

                gxpResult.getSpinner().selectValue(value);
                gxpResult.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DATE OF TEST RESULT RECEIVED")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                dateTestResult.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                dateTestResult.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("MTB BURDEN")) {

                for (RadioButton rb : mtbBurden.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_very_low)) && obs[0][1].equals("VERY LOW")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_low)) && obs[0][1].equals("LOW")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_medium)) && obs[0][1].equals("MEDIUM")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_high)) && obs[0][1].equals("HIGH")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                mtbBurden.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("RIF RESULT")) {

                for (RadioButton rb : rifResult.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_not_detected)) && obs[0][1].equals("NOT DETECTED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_detected)) && obs[0][1].equals("DETECTED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INTERMEDIATE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                rifResult.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ERROR CODE")) {
                errorCode.getEditText().setText(obs[0][1]);
                errorCode.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }

        if (view == dateTestResult.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        dateTestResult.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        mtbBurden.setVisibility(View.GONE);
        errorCode.setVisibility(View.GONE);
        rifResult.setVisibility(View.GONE);

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == gxpResult.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_mtb_detected))) {
                mtbBurden.setVisibility(View.VISIBLE);
                rifResult.setVisibility(View.VISIBLE);
            } else {
                mtbBurden.setVisibility(View.GONE);
                rifResult.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_error))) {
                errorCode.setVisibility(View.VISIBLE);
            } else {
                errorCode.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
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
