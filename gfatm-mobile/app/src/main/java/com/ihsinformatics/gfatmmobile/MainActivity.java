package com.ihsinformatics.gfatmmobile;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backupservice.Backup;
import com.ihsinformatics.gfatmmobile.commonlab.LabFragment;
import com.ihsinformatics.gfatmmobile.commonlab.network.CommonLabAPIClient;
import com.ihsinformatics.gfatmmobile.commonlab.network.HttpCodes;
import com.ihsinformatics.gfatmmobile.commonlab.network.RetrofitClientFactory;
import com.ihsinformatics.gfatmmobile.commonlab.network.Utils;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Attribute;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.AttributeType;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Concept;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.OpenMRSResponse;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestOrder;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestOrdersResponse;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestType;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestTypesResponse;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeTypeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.ConceptEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DrugOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntity;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.medication.MedicationDefaultDataHelper;
import com.ihsinformatics.gfatmmobile.medication.MedicationFragment;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.DrugOrder;
import com.ihsinformatics.gfatmmobile.shared.FormsObject;
import com.ihsinformatics.gfatmmobile.shared.Roles;
import com.ihsinformatics.gfatmmobile.util.DatabaseUtil;
import com.ihsinformatics.gfatmmobile.util.FusedLocationService;
import com.ihsinformatics.gfatmmobile.util.OfflineFormSyncService;
import com.ihsinformatics.gfatmmobile.util.OnlineFormSyncService;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Timer;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnTouchListener,
        AdapterView.OnItemSelectedListener {

    private static final int SELECT_PATIENT_ACTIVITY = 0;
    private static final int SAVED_FORM_ACTIVITY = 1;
    private static final int PICK_FILE_RESULT_CODE = 2;
    protected static ProgressDialog loading;
    LinearLayout buttonLayout;
    public static LinearLayout headerLayout;
    public static Button formButton;
    Button labButton;
    Button reportButton;
    Button searchButton;
    Button medicationButton;
    public static FormFragment fragmentForm = new FormFragment();
    public static LabFragment fragmentLab = new LabFragment();
    public static ReportFragment fragmentReport = new ReportFragment();
    public static SummaryFragment fragmentSummary = new SummaryFragment();
    public static MedicationFragment fragmentMedication = new MedicationFragment();
    ImageView change;
    public static ImageView update;
    public static ImageView edit;

    public static TextView patientName;
    public static TextView patientDob;
    public static TextView patientId;
    public static TextView id;

    private Timer timer;

    View editView;
    PopupWindow popupWindow;
    LinearLayout attributeContent;
    LinearLayout addressContent;
    RelativeLayout backDimLayout;

    TitledEditText patientSource;
    TitledEditText address1;
    AutoCompleteTextView address2;
    Spinner province;
    Spinner district;
    Spinner city;
    TitledEditText landmark;

    Context context = this;


    static boolean active = false;
    public static ServerService serverService;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            if (message.equals("completed")) {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(R.drawable.ic_checked);
                mBuilder.setContentTitle(getResources().getString(R.string.offline_forms_upload_completed));
                mBuilder.setContentText(getResources().getString(R.string.offline_forms_upload_completed_successfully));
                mBuilder.setPriority(Notification.PRIORITY_HIGH);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(alarmSound);

                Intent notificationIntent = new Intent();
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("buzz", "buzz");
                notificationIntent.putExtras(bundle);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(contentIntent);
                mBuilder.setDefaults(Notification.DEFAULT_ALL);

                // Add as notification
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, mBuilder.build());
            } else if (message.equals("completed_with_error")) {

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(R.drawable.error);
                mBuilder.setContentTitle("Aao TB Mitao - Error in Forms Upload");
                mBuilder.setContentText("Go to offline forms to see pending forms.");
                mBuilder.setPriority(Notification.PRIORITY_HIGH);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(alarmSound);

                Intent notificationIntent = new Intent(context, OfflineFormActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("buzz", "buzz");
                notificationIntent.putExtras(bundle);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(contentIntent);
                mBuilder.setDefaults(Notification.DEFAULT_ALL);

                // Add as notification
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, mBuilder.build());

            } else if (message.equals("completed_with_error_online")) {

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(R.drawable.error);
                mBuilder.setContentTitle("Aao TB Mitao - Error in Forms Upload");
                mBuilder.setContentText("Go to saved forms to see pending forms.");
                mBuilder.setPriority(Notification.PRIORITY_HIGH);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(alarmSound);

                Intent notificationIntent = new Intent(context, OnlineFormActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("buzz", "buzz");
                notificationIntent.putExtras(bundle);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(contentIntent);
                mBuilder.setDefaults(Notification.DEFAULT_ALL);

                // Add as notification
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, mBuilder.build());

            }
        }
    };

//    TextView nav_default;

    public static ActionBar actionBar;

    FragmentManager fm = getFragmentManager();
    private TextView message;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;

        stopService(new Intent(MainActivity.this, FusedLocationService.class));

        Date time = Calendar.getInstance().getTime();
        App.setLastActivity(time);

        String timeString = App.getSqlDateTime(time);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Preferences.LAST_ACTIVITY, timeString);
        editor.apply();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (active) {
            return;
        }

        loading = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        serverService = new ServerService(getApplicationContext());

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_place, fragmentForm, "form");
        fragmentTransaction.add(R.id.fragment_place, fragmentLab, "LAB");
        fragmentTransaction.add(R.id.fragment_place, fragmentReport, "REPORT");
        fragmentTransaction.add(R.id.fragment_place, fragmentSummary, "SEARCH");
        fragmentTransaction.add(R.id.fragment_place, fragmentMedication, "Medication");

        fragmentTransaction.hide(fragmentForm);
        fragmentTransaction.hide(fragmentMedication);
        fragmentTransaction.hide(fragmentReport);
        fragmentTransaction.hide(fragmentSummary);

        fragmentTransaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        TextView nav_appName = (TextView) hView.findViewById(R.id.appName);
        nav_appName.setText(getResources().getString(R.string.app_name) + " (" + App.getVersion() + ")");
        //nav_default = (TextView) hView.findViewById(R.id.selectedDefault);
        //nav_default.setText(getResources().getString(R.string.program) + App.getProgram() + "  |  " + getResources().getString(R.string.location) + App.getLocation());
        TextView nav_user = (TextView) hView.findViewById(R.id.menuUsername);
        nav_user.setText(App.getUserFullName());
        TextView nav_userRole = (TextView) hView.findViewById(R.id.menuUserRoles);
        nav_userRole.setText(App.getRoles());

        change = (ImageView) findViewById(R.id.change);
        int color = App.getColor(this, R.attr.colorBackground);
        DrawableCompat.setTint(change.getDrawable(), color);
        change.setOnTouchListener(this);

        update = (ImageView) findViewById(R.id.update);
        DrawableCompat.setTint(update.getDrawable(), color);
        update.setOnTouchListener(this);

        edit = (ImageView) findViewById(R.id.edit);
        DrawableCompat.setTint(edit.getDrawable(), color);
        edit.setOnTouchListener(this);
        edit.setVisibility(View.GONE);

        getSupportActionBar().setTitle(Html.fromHtml(App.getLocation() + "</small>"));
        if (App.getMode().equalsIgnoreCase("OFFLINE")) {
            getSupportActionBar().setSubtitle("Offline Mode");
            update.setVisibility(View.GONE);
            if (App.getPatient() == null)
                edit.setVisibility(View.GONE);
            else
                edit.setVisibility(View.VISIBLE);
        } else {
            getSupportActionBar().setSubtitle(null);

            if (App.getPatient() == null) {
                update.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            } else {
                update.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            }

        }

        buttonLayout = (LinearLayout) findViewById(R.id.layoutTestTabs);
        message = (TextView) findViewById(R.id.message);
        headerLayout = (LinearLayout) findViewById(R.id.header);
        formButton = (Button) findViewById(R.id.formButton);
        labButton = (Button) findViewById(R.id.labButton);
        reportButton = (Button) findViewById(R.id.reportButton);
        searchButton = (Button) findViewById(R.id.searchButton);
        medicationButton = (Button) findViewById(R.id.medicationButton);

        patientName = (TextView) findViewById(R.id.patientName);
        patientDob = (TextView) findViewById(R.id.patientDob);
        patientId = (TextView) findViewById(R.id.patientId);
        id = (TextView) findViewById(R.id.id);

        LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        editView = layoutInflater.inflate(R.layout.edit_patient, null);
        popupWindow = new PopupWindow(editView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.update();
        attributeContent = (LinearLayout) editView.findViewById(R.id.attributes);
        addressContent = (LinearLayout) editView.findViewById(R.id.address);
        backDimLayout = (RelativeLayout) findViewById(R.id.bac_dim_layout);

        loadEditPopup();

        if (App.getPatient() != null) {

            String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
            String lname = App.getPatient().getPerson().getFamilyName();
            if (!lname.equals(""))
                lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);

            patientName.setText(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")");
            String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
            if (!dob.equals("")) {
                Date date = App.stringToDate(dob, "yyyy-MM-dd");
                DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                if (App.getPatient().getPerson().getAge() == 0) {
                    Date birthDate = App.stringToDate(App.getPatient().getPerson().getBirthdate(), "yyyy-MM-dd");
                    int age = App.getDiffMonths(birthDate, new Date());
                    if (age == 0) {
                        long ageInLong = App.getDiffDays(birthDate, new Date());
                        patientDob.setText(ageInLong + " days (" + df.format(date) + ")");
                    } else patientDob.setText(age + " months (" + df.format(date) + ")");
                } else
                    patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
            } else patientDob.setText(dob);
            if (!App.getPatient().getPatientId().equals(""))
                id.setVisibility(View.VISIBLE);
            patientId.setText(App.getPatient().getPatientId());
            update.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
        }

        if (App.getLocation().equals(""))
            openLocationSelectionDialog();

        if (App.getMode().equalsIgnoreCase("ONLINE") && !OfflineFormSyncService.isRunning()) {
            int count = serverService.getPendingOfflineSavedFormsCount(App.getUsername());
            if (count > 0) {

                if (count >= App.OFFLINE_FORM_CAP) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.dialog).create();
                    String statement = getResources().getString(R.string.offline_form_alert_error);
                    alertDialog.setMessage(count + " " + statement);
                    Drawable clearIcon = getResources().getDrawable(R.drawable.ic_warning);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_offline_form_found));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                                            new IntentFilter("background-offline-sync"));
                                    startSync();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));
                } else if (count >= App.OFFLINE_FORM_WARNING) {
                    final int color1 = App.getColor(this, R.attr.colorOther);

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    String statement = getResources().getString(R.string.offline_form_alert_warning);
                    statement = statement.replace("#off#", String.valueOf(App.OFFLINE_FORM_CAP));
                    alertDialog.setMessage(count + " " + statement);
                    Drawable clearIcon = getResources().getDrawable(R.drawable.ic_warning);
                    DrawableCompat.setTint(clearIcon, color1);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_offline_form_found));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                                            new IntentFilter("background-offline-sync"));
                                    startSync();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));
                } else {
                    final int color1 = App.getColor(this, R.attr.colorAccent);

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(count + " " + getString(R.string.offline_form_alert));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.ic_submit);
                    DrawableCompat.setTint(clearIcon, color1);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_offline_form_found));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                                            new IntentFilter("background-offline-sync"));
                                    startSync();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));
                }

            }
        }
        showFormFragment();
        runCommonLabChecks();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 103);
            return;
        } else
            serverService.scheduleBackupWithDefaultValues();

         /*if(serverService.getPendingOnlineSavedFormsCount(App.getUsername()) != 0 && !OnlineFormSyncService.isRunning())
            startService(new Intent(this, OnlineFormSyncService.class));*/

    }

    private void runCommonLabChecks() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean firstRun = preferences.getBoolean("firstRun", true);
        if(firstRun) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();
            new MedicationDefaultDataHelper().insert(this);
            downloadCommonLabMetadata();
        }
    }

    private void downloadDrugsForPatient() {
        if(DataAccess.getInstance().getDrugOrdersByPatientUUID(App.getPatient().getUuid()).size() > 0) {
            // Do not automatically download for the local patient
            return;
        }

        CommonLabAPIClient apiClient = RetrofitClientFactory.createCommonLabApiClient();

        Call<OpenMRSResponse<DrugOrder>> call = apiClient.fetchDrugOrdersByPatientUUID(
                App.getPatient().getUuid(),
                "drugorder",
                "custom:(uuid,orderNumber,action,patient:(uuid,display),careSetting:(uuid,display),previousOrder,dateActivated,dateStopped,autoExpireDate,encounter:(uuid,display),orderer:(uuid,display),orderReason:(uuid,display),orderReasonNonCoded,instructions,drug:(uuid,display),dose,doseUnits:(uuid,display),frequency:(uuid,display),quantity,quantityUnits:(uuid,display),duration,durationUnits:(uuid,display),route:(uuid,display))",
                Utils.getBasicAuth());
        call.enqueue(new Callback<OpenMRSResponse<DrugOrder>>() {
            @Override
            public void onResponse(Call<OpenMRSResponse<DrugOrder>> call, Response<OpenMRSResponse<DrugOrder>> response) {
                if(response.code() == HttpCodes.OK) {
                    OpenMRSResponse<DrugOrder> drugOrders = response.body();
                    afterDrugsDownloaded(drugOrders);
                }
            }

            @Override
            public void onFailure(Call<OpenMRSResponse<DrugOrder>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void afterDrugsDownloaded(OpenMRSResponse<DrugOrder> drugOrders) {
        List<DrugOrderEntity> dbOrders = new ArrayList<>();
        List<DrugOrder> allTestOrders = drugOrders.getResults();
        for(DrugOrder drugOrder: allTestOrders) {
            if(drugOrder.getDrug() == null) continue;
            DrugOrderEntity dbEntity = new DrugOrderEntity();
            dbEntity = DrugOrder.copyProperties(dbEntity, drugOrder);
            dbOrders.add(dbEntity);
        }

        DataAccess.getInstance().insertAllDrugOrders(dbOrders);

    }

    private void downloadTestOrdersForPatient() {
        if(DataAccess.getInstance().getTestOrderByPatientUUID(App.getPatient().getUuid()).size() > 0) {
            // Do not automatically download for the local patient
            return;
        }
        CommonLabAPIClient apiClient = RetrofitClientFactory.createCommonLabApiClient();

        Call<TestOrdersResponse> call = apiClient.fetchAllTestOrders("full", App.getPatient().getUuid(), Utils.getBasicAuth());
        call.enqueue(new Callback<TestOrdersResponse>() {
            @Override
            public void onResponse(Call<TestOrdersResponse> call, Response<TestOrdersResponse> response) {
                if(response.code() == HttpCodes.OK) {
                    TestOrdersResponse testOrders = response.body();
                    afterTestsDownloaded(testOrders);
                }
            }

            @Override
            public void onFailure(Call<TestOrdersResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void afterTestsDownloaded(TestOrdersResponse response) {
        List<TestOrderEntity> dbOrders = new ArrayList<>();
        List<TestOrder> allTestOrders = response.getResults();
        for(TestOrder testOrder: allTestOrders) {
            if(testOrder.getLabTestType() == null) continue;
            TestOrderEntity dbEntity = new TestOrderEntity();
            dbEntity = TestOrder.copyProperties(dbEntity, testOrder);
            dbEntity.setLabTestType(DataAccess.getInstance().getTestTypeByUUID(testOrder.getLabTestType().getUuid()));
            dbOrders.add(dbEntity);
            downloadTestDetails(testOrder);
        }

        DataAccess.getInstance().insertAllOrders(dbOrders);
    }

    private synchronized void downloadTestDetails(final TestOrder testOrder) {
        CommonLabAPIClient apiClient = RetrofitClientFactory.createCommonLabApiClient();

        Call<TestOrder> call = apiClient.fetchTestOrderByUUID(testOrder.getUuid(), Utils.getBasicAuth());
        call.enqueue(new Callback<TestOrder>() {
            @Override
            public void onResponse(Call<TestOrder> call, Response<TestOrder> response) {
                if(response.code() == HttpCodes.OK) {
                    TestOrder testOrderDetail = response.body();
                    if(testOrderDetail.getAttributes().size() == 0) return;
                    afterOrderDetailDownloaded(testOrderDetail);
                }
            }

            @Override
            public void onFailure(Call<TestOrder> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private synchronized void afterOrderDetailDownloaded(TestOrder testOrderDetail) {
        List<AttributeEntity> attributeEntities = new ArrayList<>();
        TestOrderEntity testOrderEntity = DataAccess.getInstance().getTestOrderByUUID(testOrderDetail.getUuid());
        for(Attribute a: testOrderDetail.getAttributes()) {
            AttributeTypeEntity attributeType = DataAccess.getInstance().getAttributeTypeByUUID(a.getAttributeType().getUuid());
            AttributeEntity dbEntity = new AttributeEntity();
            attributeEntities.add(Attribute.copyProperties(dbEntity, a, testOrderEntity, attributeType));
        }

        DataAccess.getInstance().insertAllAttributes(attributeEntities);
    }

    private void downloadCommonLabMetadata() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*loading.setInverseBackgroundForced(true);*/
                loading.setIndeterminate(true);
                loading.setCancelable(false);
                loading.setMessage("Downloading metadata");
                message.setVisibility(View.VISIBLE);
                // loading.show();
            }
        });

        downloadTestTypes();
    }

    public void downloadTestTypes() {
        CommonLabAPIClient apiClient = RetrofitClientFactory.createCommonLabApiClient();

        Call<TestTypesResponse> call = apiClient.fetchAllTestTypes("full", Utils.getBasicAuth());
        call.enqueue(new Callback<TestTypesResponse>() {
            @Override
            public void onResponse(Call<TestTypesResponse> call, Response<TestTypesResponse> response) {
                if(response.code() == HttpCodes.OK) {
                    TestTypesResponse testTypesResponse = response.body();

                    onTestTypesDownloaded(testTypesResponse);
                }
            }

            @Override
            public void onFailure(Call<TestTypesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void onTestTypesDownloaded(TestTypesResponse testTypesResponse) {
        List<TestTypeEntity> dbTestTypeEntities = new ArrayList<>();
        List<TestType> testTypes = testTypesResponse.getResults();
        for(TestType t: testTypes) {
            TestTypeEntity dbTestTypeEntity = new TestTypeEntity();
            dbTestTypeEntities.add(TestType.copyProperties(dbTestTypeEntity, t));
        }
        DataAccess.getInstance().insertAllTestTypes(dbTestTypeEntities);

        downloadAttributeTypes(testTypes);
    }

    private void downloadAttributeTypes(final List<TestType> testTypes) {
        CommonLabAPIClient apiClient = RetrofitClientFactory.createCommonLabApiClient();

        for(TestType t: testTypes) {
            Call<OpenMRSResponse<AttributeType>> call = apiClient.fetchAttributeTypes("full", t.getUuid(), Utils.getBasicAuth());
            call.enqueue(new Callback<OpenMRSResponse<AttributeType>>() {
                @Override
                public void onResponse(Call<OpenMRSResponse<AttributeType>> call, Response<OpenMRSResponse<AttributeType>> response) {
                    if(response.code() == HttpCodes.OK) {
                        OpenMRSResponse<AttributeType> attributesResponse = response.body();
                        onTestAttributesDownloaded(attributesResponse);
                    }
                }
                @Override
                public void onFailure(Call<OpenMRSResponse<AttributeType>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    int conceptCount = 0;
    int downloadedConcepts = 0;
    private synchronized void onTestAttributesDownloaded(OpenMRSResponse<AttributeType> attributesResponse) {
        List<AttributeTypeEntity> dbEntities = new ArrayList<>();
        List<AttributeType> attributeTypes = attributesResponse.getResults();
        for(AttributeType a: attributeTypes) {
            AttributeTypeEntity dbEntity = new AttributeTypeEntity();
            dbEntities.add(AttributeType.copyProperties(dbEntity, a, DataAccess.getInstance().getTestTypeByUUID(a.getLabTestType().getUuid())));
            if(dbEntity.getDatatypeClassname().equalsIgnoreCase("org.openmrs.customdatatype.datatype.ConceptDatatype")) {
                conceptCount++;
                onConceptAttributeDownloaded(dbEntity.getDatatypeConfig());

            }
        }

        DataAccess.getInstance().insertAll(dbEntities);

    }

    private void onConceptAttributeDownloaded(String conceptId) {

        CommonLabAPIClient apiClient = RetrofitClientFactory.createCommonLabApiClient();
        Call<Concept> call = apiClient.fetchConcept(conceptId, Utils.getBasicAuth());
        call.enqueue(new Callback<Concept>() {
            @Override
            public void onResponse(Call<Concept> call, Response<Concept> response) {
                downloadedConcepts++;
                if(response.code() == HttpCodes.OK) {
                    Concept concept = response.body();
                    onConceptDownloaded(concept);
                }
                if(conceptCount == downloadedConcepts) {
                    message.setVisibility(View.GONE);
                    conceptCount = 0;
                    downloadedConcepts = 0;
                }
            }

            @Override
            public void onFailure(Call<Concept> call, Throwable t) {
                downloadedConcepts++;
                t.printStackTrace();
                if(conceptCount == downloadedConcepts) {
                    message.setVisibility(View.GONE);
                    conceptCount = 0;
                    downloadedConcepts = 0;
                }
            }
        });
    }

    private void onConceptDownloaded(Concept concept) {
        ConceptEntity conceptEntity = new ConceptEntity();
        Concept.copyProperties(conceptEntity, concept);
        long parent = DataAccess.getInstance().insertConcept(conceptEntity);

        List<Concept> concepts = concept.getAnswers();
        if(concepts == null || concepts.size()<=0) return;
        List<ConceptEntity> conceptEntities = new ArrayList<>();
        ConceptEntity temp;
        for(Concept c: concepts) {
            temp = new ConceptEntity();
            temp.setUuid(c.getUuid());
            temp.setDisplay(c.getDisplay());
            temp.setParentId(parent);
            conceptEntities.add(temp);
        }
        DataAccess.getInstance().insertAllConcepts(conceptEntities);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Date time = Calendar.getInstance().getTime();
        App.setLastActivity(time);

        String timeString = App.getSqlDateTime(time);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Preferences.LAST_ACTIVITY, timeString);
        editor.apply();

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if (App.getLastActivity() != null) {

            Date lastActivity = App.getLastActivity();
            Date currentTime = Calendar.getInstance().getTime();

            long diff = currentTime.getTime() - lastActivity.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;

            if (minutes >= App.TIME_OUT && !OfflineFormSyncService.isRunning()) {

                App.setAutoLogin("Disabled");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preferences.LAST_LOGIN, App.getLastLogin());
                editor.putString(Preferences.AUTO_LOGIN, App.getAutoLogin());
                editor.apply();
                startLoginIntent();

            } else {
                Date time = Calendar.getInstance().getTime();
                App.setLastActivity(time);

                String timeString = App.getSqlDateTime(time);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preferences.LAST_ACTIVITY, timeString);
                editor.apply();

            }

        }

        String d = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String v = App.getLastLogin();
        if (!(App.getAutoLogin().equals("Enabled") && App.getLastLogin().equals(d))) {
            Intent intent = new Intent(context, LoginActivity.class);
            //ServerService service = new ServerService(context);
            //service.resetScreeningCounts();
            startActivity(intent);
            finish();
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            return;
        } else {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showLocationAlert();
            } else {

                Intent intent = new Intent(MainActivity.this, FusedLocationService.class);
                startService(intent);
            }
        }

        /*if(serverService.getPendingOnlineSavedFormsCount(App.getUsername()) != 0 && OnlineFormSyncService.isRunning())
            startService(new Intent(this, OnlineFormSyncService.class));*/

        String title = getSupportActionBar().getTitle().toString();

        if (!title.contains(App.getLocation())) {
            getSupportActionBar().setTitle(App.getLocation());
            EventBus.getDefault().post(App.getLocation());

        }

        if (App.getMode().equalsIgnoreCase("OFFLINE")) {
            getSupportActionBar().setSubtitle("Offline Mode");
            update.setVisibility(View.GONE);
            if (App.getPatient() == null)
                edit.setVisibility(View.GONE);
            else
                edit.setVisibility(View.VISIBLE);
        } else {

            Boolean flag = getSupportActionBar().getSubtitle() == null ? false : true;

            getSupportActionBar().setSubtitle(null);
            if (App.getPatient() == null) {
                update.setVisibility(View.GONE);
                patientName.setText("");
                patientDob.setText("");
                patientId.setText("");
                id.setText("");
                edit.setVisibility(View.GONE);


                Fragment form = fm.findFragmentByTag("form");
                if (form != null) {
                    if (!(form != null || form.isVisible()) || flag) {
                        fragmentForm.fillScreeningFormContent();
                        fragmentForm.fillTreatmentFormContent();
                        fragmentForm.fillTestFormContent();
                        fragmentForm.fillCommonFormContent();
                        showFormFragment();
                        headerLayout.setVisibility(View.VISIBLE);
                    }
                }

                getSupportActionBar().setTitle(App.getLocation());
                fragmentReport.fillReportFragment();
                fragmentSummary.updateSummaryFragment();
            } else {

                downloadTestOrdersForPatient();
                downloadDrugsForPatient();
                String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
                String lname = App.getPatient().getPerson().getFamilyName();
                if (!lname.equals(""))
                    lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);

                if (!App.get(patientName).equals(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")")) {

                    patientName.setText(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")");
                    String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
                    if (!dob.equals("")) {
                        Date date = App.stringToDate(dob, "yyyy-MM-dd");
                        DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                        if (App.getPatient().getPerson().getAge() == 0) {
                            Date birthDate = App.stringToDate(App.getPatient().getPerson().getBirthdate(), "yyyy-MM-dd");
                            int age = App.getDiffMonths(birthDate, new Date());
                            if (age == 0) {
                                long ageInLong = App.getDiffDays(birthDate, new Date());
                                patientDob.setText(ageInLong + " days (" + df.format(date) + ")");
                            } else patientDob.setText(age + " months (" + df.format(date) + ")");
                        } else
                            patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
                    } else patientDob.setText(dob);
                    if (!App.getPatient().getPatientId().equals(""))
                        id.setVisibility(View.VISIBLE);
                    patientId.setText(App.getPatient().getPatientId());

                    getSupportActionBar().setTitle(App.getLocation());
                    fragmentForm.fillTreatmentFormContent();
                    fragmentForm.fillTestFormContent();
                    fragmentForm.fillScreeningFormContent();
                    fragmentForm.fillCommonFormContent();
                    fragmentReport.fillReportFragment();
                    fragmentSummary.updateSummaryFragment();
                    headerLayout.setVisibility(View.VISIBLE);
                    fragmentForm.showMainContent(true);
                    showFormFragment();

                }

                update.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            }

            if (!OfflineFormSyncService.isRunning()) {
                if (flag) {
                    int count = serverService.getPendingOfflineSavedFormsCount(App.getUsername());
                    if (count > 0) {

                        if (count >= App.OFFLINE_FORM_CAP) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.dialog).create();
                            String statement = getResources().getString(R.string.offline_form_alert_error);
                            alertDialog.setMessage(count + " " + statement);
                            Drawable clearIcon = getResources().getDrawable(R.drawable.ic_warning);
                            alertDialog.setIcon(clearIcon);
                            alertDialog.setTitle(getResources().getString(R.string.title_offline_form_found));
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                                                    new IntentFilter("background-offline-sync"));
                                            startSync();
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));
                        } else if (count >= App.OFFLINE_FORM_WARNING) {
                            final int color1 = App.getColor(this, R.attr.colorOther);

                            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                            String statement = getResources().getString(R.string.offline_form_alert_warning);
                            statement = statement.replace("#off#", String.valueOf(App.OFFLINE_FORM_CAP));
                            alertDialog.setMessage(count + " " + statement);
                            Drawable clearIcon = getResources().getDrawable(R.drawable.ic_warning);
                            DrawableCompat.setTint(clearIcon, color1);
                            alertDialog.setIcon(clearIcon);
                            alertDialog.setTitle(getResources().getString(R.string.title_offline_form_found));
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                                                    new IntentFilter("background-offline-sync"));
                                            startSync();
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));
                        } else {
                            final int color1 = App.getColor(this, R.attr.colorAccent);

                            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                            alertDialog.setMessage(count + " " + getString(R.string.offline_form_alert));
                            Drawable clearIcon = getResources().getDrawable(R.drawable.ic_submit);
                            DrawableCompat.setTint(clearIcon, color1);
                            alertDialog.setIcon(clearIcon);
                            alertDialog.setTitle(getResources().getString(R.string.title_offline_form_found));
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                                                    new IntentFilter("background-offline-sync"));
                                            startSync();
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));
                        }

                    }
                }
            }
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lang = preferences.getString(Preferences.LANGUAGE, "");
        if (!App.getLanguage().equals(lang)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Preferences.LANGUAGE, App.getLanguage());
            editor.apply();
            restartActivity();
        }

    }

    public static void backToMainMenu() {
        fragmentForm.setMainContentVisible(true);
        headerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        Fragment lab = fm.findFragmentByTag("LAB");
        if(lab != null && lab.isVisible() && (fragmentLab.isAddTestScreenVisible() || fragmentLab.isAddTestResultsScreenVisible())) {

            int color = App.getColor(MainActivity.this, R.attr.colorAccent);

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.warning_before_close_adding_test));
            Drawable backIcon = getResources().getDrawable(R.drawable.ic_back);
            backIcon.setAutoMirrored(true);
            DrawableCompat.setTint(backIcon, color);
            alertDialog.setIcon(backIcon);
            alertDialog.setTitle(getResources().getString(R.string.back_to_lab_tests));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            fragmentLab.toggleMainPageVisibility(true);
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.show();
            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

            return;
        }

        Fragment medication = fm.findFragmentByTag("Medication");
        if(medication != null && medication.isVisible() && (fragmentMedication.isAddMedicineScreenVisible() || fragmentMedication.isAddMultipleScreenVisible())) {

            int color = App.getColor(MainActivity.this, R.attr.colorAccent);

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.warning_before_close_adding_test));
            Drawable backIcon = getResources().getDrawable(R.drawable.ic_back);
            backIcon.setAutoMirrored(true);
            DrawableCompat.setTint(backIcon, color);
            alertDialog.setIcon(backIcon);
            alertDialog.setTitle(getResources().getString(R.string.back_to_medications));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            fragmentMedication.toggleMainPageVisibility(true);
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.show();
            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

            return;
        }

        Fragment form = fm.findFragmentByTag("form");
        if (form != null && form.isVisible() && fragmentForm.isFormVisible()) {

            int color = App.getColor(MainActivity.this, R.attr.colorAccent);

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.warning_before_close_form));
            Drawable backIcon = getResources().getDrawable(R.drawable.ic_back);
            backIcon.setAutoMirrored(true);
            DrawableCompat.setTint(backIcon, color);
            alertDialog.setIcon(backIcon);
            alertDialog.setTitle(getResources().getString(R.string.back_to_form_menu));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            fragmentForm.setMainContentVisible(true);
                            headerLayout.setVisibility(View.VISIBLE);
                            getSupportActionBar().show();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.show();
            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));


            return;
        }

        form = fm.findFragmentByTag("SEARCH");
        if (form != null && form.isVisible() && fragmentSummary.isViewVisible()) {
            fragmentSummary.setMainContentVisible(true);
            return;
        }

        int color = App.getColor(MainActivity.this, R.attr.colorAccent);

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.dialog).create();
        alertDialog.setMessage(getString(R.string.warning_before_close));
        Drawable backIcon = getResources().getDrawable(R.drawable.ic_back);
        backIcon.setAutoMirrored(true);
        DrawableCompat.setTint(backIcon, color);
        alertDialog.setIcon(backIcon);
        alertDialog.setTitle(getResources().getString(R.string.title_close));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        close();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        return;
                    }
                });

        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

    }

    public void close() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.offline_forms) {
            Intent savedFormActivityIntent = new Intent(this, OfflineFormActivity.class);
            startActivityForResult(savedFormActivityIntent, SAVED_FORM_ACTIVITY);
        } else if (id == R.id.feedback) {
            Intent feedbackActivityIntent = new Intent(this, FeedbackActivity.class);
            startActivity(feedbackActivityIntent);
        } else if (id == R.id.location_setup) {
            Intent locationSetupActivityIntent = new Intent(this, LocationSetupActivity.class);
            startActivity(locationSetupActivityIntent);
        } else if (id == R.id.search) {
            Intent selectPatientActivityIntent = new Intent(this, SearchActivity.class);
            startActivityForResult(selectPatientActivityIntent, SELECT_PATIENT_ACTIVITY);
        } else if (id == R.id.sync_metadata) {
            Intent updateDatabaseIntent = new Intent(this, UpdateDatabaseActivity.class);
            startActivity(updateDatabaseIntent);
        } else if (id == R.id.online_form) {
            Intent updateDatabaseIntent = new Intent(this, OnlineFormActivity.class);
            startActivityForResult(updateDatabaseIntent, SAVED_FORM_ACTIVITY);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_theme) {

            Intent themeActivityIntent = new Intent(this, ThemeActivity.class);
            startActivity(themeActivityIntent);

        } else if (id == R.id.nav_language) {

            Intent languageActivityIntent = new Intent(this, LanguageActivity.class);
            startActivity(languageActivityIntent);

        } else if (id == R.id.nav_operation_mode) {

            Intent operationModeActivityIntent = new Intent(this, OperationModeActivity.class);
            startActivity(operationModeActivityIntent);

        } else if (id == R.id.nav_defaults) {

            Intent defaultActivityIntent = new Intent(this, DefaultActivity.class);
            startActivity(defaultActivityIntent);

        } else if (id == R.id.nav_server) {

            Intent serverActivityIntent = new Intent(this, ServerActivity.class);
            startActivity(serverActivityIntent);

        } else if (id == R.id.nav_ssl_encryption) {

            Intent sslActivityIntent = new Intent(this, SSLEncryptionActivity.class);
            startActivity(sslActivityIntent);

        } else if (id == R.id.nav_backup) {

            Intent backupDatabaseIntent = new Intent(this, BackupDatabaseActivity.class);
            startActivity(backupDatabaseIntent);

        } else if (id == R.id.nav_schedule_backup) {

            Intent ScheduleBackupIntent = new Intent(this, ScheduleBackupActivity.class);
            startActivity(ScheduleBackupIntent);

        } else if (id == R.id.nav_restore) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
            } else {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                try {
                    startActivityForResult(intent, PICK_FILE_RESULT_CODE);
                } catch (ActivityNotFoundException e) {
                    Log.d("Pick File", e.toString());
                }
            }

        } else if (id == R.id.nav_sync_lab_metadata) {
            downloadCommonLabMetadata();
        } else if (id == R.id.nav_logout) {

            int color = App.getColor(MainActivity.this, R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.warning_before_logout));
            Drawable logoutIcon = getResources().getDrawable(R.drawable.ic_logout);
            DrawableCompat.setTint(logoutIcon, color);
            alertDialog.setIcon(logoutIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_logout));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            App.setAutoLogin("Disabled");
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(Preferences.LAST_LOGIN, App.getLastLogin());
                            editor.putString(Preferences.AUTO_LOGIN, App.getAutoLogin());
                            editor.apply();
                            startLoginIntent();

                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void selectFrag(View view) {

        if (view == formButton)
            showFormFragment();
        else if (view == labButton)
            showLabFragment();
        else if (view == reportButton)
            showReportFragment();
        else if (view == searchButton)
            showSearchFragment();
        else if (view == medicationButton)
            showMedicationFragment();

    }

    private void showFormFragment() {

        int color = App.getColor(this, R.attr.colorPrimaryDark);

        formButton.setTextColor(color);
        formButton.setBackgroundResource(R.drawable.selected_border_button);
        DrawableCompat.setTint(formButton.getCompoundDrawables()[0], color);

        labButton.setTextColor(getResources().getColor(R.color.dark_grey));
        labButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(labButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        reportButton.setTextColor(getResources().getColor(R.color.dark_grey));
        reportButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(reportButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        searchButton.setTextColor(getResources().getColor(R.color.dark_grey));
        searchButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(searchButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        medicationButton.setTextColor(getResources().getColor(R.color.dark_grey));
        medicationButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(medicationButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.show(fragmentForm);
        fragmentTransaction.hide(fragmentLab);
        fragmentTransaction.hide(fragmentReport);
        fragmentTransaction.hide(fragmentSummary);
        fragmentTransaction.hide(fragmentMedication);
        fragmentTransaction.commit();
    }

    private void showLabFragment() {
        int color = App.getColor(this, R.attr.colorPrimaryDark);

        formButton.setTextColor(getResources().getColor(R.color.dark_grey));
        formButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(formButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        labButton.setTextColor(color);
        labButton.setBackgroundResource(R.drawable.selected_border_button);
        DrawableCompat.setTint(labButton.getCompoundDrawables()[0], color);

        reportButton.setTextColor(getResources().getColor(R.color.dark_grey));
        reportButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(reportButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        searchButton.setTextColor(getResources().getColor(R.color.dark_grey));
        searchButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(searchButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        medicationButton.setTextColor(getResources().getColor(R.color.dark_grey));
        medicationButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(medicationButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.hide(fragmentForm);
        fragmentTransaction.show(fragmentLab);
        fragmentTransaction.hide(fragmentReport);
        fragmentTransaction.hide(fragmentSummary);
        fragmentTransaction.hide(fragmentMedication);
        fragmentTransaction.commit();
        fragmentLab.onBringToFront();
    }

    private void showReportFragment() {

        int color = App.getColor(this, R.attr.colorPrimaryDark);

        formButton.setTextColor(getResources().getColor(R.color.dark_grey));
        formButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(formButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        labButton.setTextColor(getResources().getColor(R.color.dark_grey));
        labButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(labButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        reportButton.setTextColor(color);
        reportButton.setBackgroundResource(R.drawable.selected_border_button);
        DrawableCompat.setTint(reportButton.getCompoundDrawables()[0], color);

        searchButton.setTextColor(getResources().getColor(R.color.dark_grey));
        searchButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(searchButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        medicationButton.setTextColor(getResources().getColor(R.color.dark_grey));
        medicationButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(medicationButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        //FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.hide(fragmentForm);
        fragmentTransaction.hide(fragmentLab);
        fragmentTransaction.show(fragmentReport);
        fragmentTransaction.hide(fragmentSummary);
        fragmentTransaction.hide(fragmentMedication);
        fragmentTransaction.commit();
    }

    private void showSearchFragment() {

        int color = App.getColor(this, R.attr.colorPrimaryDark);

        formButton.setTextColor(getResources().getColor(R.color.dark_grey));
        formButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(formButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        labButton.setTextColor(getResources().getColor(R.color.dark_grey));
        labButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(labButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        reportButton.setTextColor(getResources().getColor(R.color.dark_grey));
        reportButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(reportButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        searchButton.setTextColor(color);
        searchButton.setBackgroundResource(R.drawable.selected_border_button);
        DrawableCompat.setTint(searchButton.getCompoundDrawables()[0], color);

        medicationButton.setTextColor(getResources().getColor(R.color.dark_grey));
        medicationButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(medicationButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.hide(fragmentForm);
        fragmentTransaction.hide(fragmentLab);
        fragmentTransaction.hide(fragmentReport);
        fragmentTransaction.show(fragmentSummary);
        fragmentTransaction.hide(fragmentMedication);
        fragmentTransaction.commit();
    }

    private void showMedicationFragment() {

        int color = App.getColor(this, R.attr.colorPrimaryDark);

        formButton.setTextColor(getResources().getColor(R.color.dark_grey));
        formButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(formButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        labButton.setTextColor(getResources().getColor(R.color.dark_grey));
        labButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(labButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        reportButton.setTextColor(getResources().getColor(R.color.dark_grey));
        reportButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(reportButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        searchButton.setTextColor(getResources().getColor(R.color.dark_grey));
        searchButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(searchButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        medicationButton.setTextColor(color);
        medicationButton.setBackgroundResource(R.drawable.selected_border_button);
        DrawableCompat.setTint(medicationButton.getCompoundDrawables()[0], color);

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.hide(fragmentForm);
        fragmentTransaction.hide(fragmentLab);
        fragmentTransaction.hide(fragmentReport);
        fragmentTransaction.hide(fragmentSummary);
        fragmentTransaction.show(fragmentMedication);
        fragmentTransaction.commit();
        fragmentMedication.bringtoFront();
    }

    /**
     * Restarts the current activity...
     */
    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void startLoginIntent() {
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                /*Intent gpsCoordinate = new Intent(this, LocationService.class);
                startActivity(gpsCoordinate);*/

                ImageView view = (ImageView) v;
                view.getDrawable().setColorFilter(getResources().getColor(R.color.dark_grey), PorterDuff.Mode.SRC_ATOP);
                view.invalidate();

                if (view == change) {

                    Intent selectPatientActivityIntent = new Intent(this, SelectPatientActivity.class);
                    startActivityForResult(selectPatientActivityIntent, SELECT_PATIENT_ACTIVITY);

                    break;
                } else if (view == update) {

                    updatePatientDetails();

                    break;
                } else if (view == edit) {

                    updatePopupContent();
                    popupWindow.showAsDropDown(edit);
                    backDimLayout.setVisibility(View.VISIBLE);
                    break;
                }
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

    private void updatePatientDetails() {
        AsyncTask<String, String, String> updateTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.updating_patient));
                        loading.show();
                    }
                });

                String result = serverService.updatePatientDetails(App.getPatient().getPatientId(), true);
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

                    String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
                    String lname = App.getPatient().getPerson().getFamilyName();
                    if (!lname.equals(""))
                        lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);

                    patientName.setText(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")");
                    String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
                    if (!dob.equals("")) {
                        Date date = App.stringToDate(dob, "yyyy-MM-dd");
                        DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                        if (App.getPatient().getPerson().getAge() == 0) {
                            Date birthDate = App.stringToDate(App.getPatient().getPerson().getBirthdate(), "yyyy-MM-dd");
                            int age = App.getDiffMonths(birthDate, new Date());
                            if (age == 0) {
                                long ageInLong = App.getDiffDays(birthDate, new Date());
                                patientDob.setText(ageInLong + " days (" + df.format(date) + ")");
                            } else patientDob.setText(age + " months (" + df.format(date) + ")");
                        } else
                            patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
                    } else patientDob.setText(dob);
                    if (!App.getPatient().getPatientId().equals(""))
                        id.setVisibility(View.VISIBLE);
                    patientId.setText(App.getPatient().getPatientId());

                    fragmentReport.fillReportFragment();
                    fragmentForm.fillTestFormContent();
                    fragmentForm.fillScreeningFormContent();
                    fragmentForm.fillTreatmentFormContent();
                    fragmentForm.fillCommonFormContent();
                    fragmentSummary.updateSummaryFragment();
                    headerLayout.setVisibility(View.VISIBLE);

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.patient_updated));
                    Drawable submitIcon = getResources().getDrawable(R.drawable.ic_submit);
                    alertDialog.setIcon(submitIcon);
                    int color = App.getColor(context, R.attr.colorAccent);
                    DrawableCompat.setTint(submitIcon, color);
                    alertDialog.setTitle(getResources().getString(R.string.title_completed));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    fragmentReport.fillReportFragment();

                } else if (result.equals("CONNECTION_ERROR")) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.data_connection_error) + "\n\n (" + result + ")");
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
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
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }


            }
        };
        updateTask.execute("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showFormFragment();
        // check that it is the SELECT PATIENT with an OK result
        if (requestCode == SELECT_PATIENT_ACTIVITY) {
            if (resultCode == RESULT_OK) {

                // get String data from Intent
                String returnString = data.getStringExtra("key");
                if (returnString != null && returnString.equals("SEARCH")) {
                    showSearchFragment();
                } else if (returnString != null && returnString.equals("SELECT")) {

                    if (App.getPatient() != null) {
                        downloadTestOrdersForPatient();
                        downloadDrugsForPatient();
                        String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
                        String lname = App.getPatient().getPerson().getFamilyName();
                        if (!lname.equals(""))
                            lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);

                        patientName.setText(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")");
                        String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
                        if (!dob.equals("")) {
                            Date date = App.stringToDate(dob, "yyyy-MM-dd");
                            DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                            if (App.getPatient().getPerson().getAge() == 0) {
                                Date birthDate = App.stringToDate(App.getPatient().getPerson().getBirthdate(), "yyyy-MM-dd");
                                int age = App.getDiffMonths(birthDate, new Date());
                                if (age == 0) {
                                    long ageInLong = App.getDiffDays(birthDate, new Date());
                                    patientDob.setText(ageInLong + " days (" + df.format(date) + ")");
                                } else
                                    patientDob.setText(age + " months (" + df.format(date) + ")");
                            } else
                                patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
                        } else patientDob.setText(dob);
                        if (!App.getPatient().getPatientId().equals(""))
                            id.setVisibility(View.VISIBLE);
                        patientId.setText(App.getPatient().getPatientId());

                        headerLayout.setVisibility(View.VISIBLE);
                        fragmentReport.fillReportFragment();
                        fragmentForm.fillScreeningFormContent();
                        fragmentForm.fillTestFormContent();
                        fragmentForm.fillTreatmentFormContent();
                        fragmentForm.fillCommonFormContent();
                        fragmentSummary.updateSummaryFragment();
                        fragmentForm.showMainContent(true);

                        fragmentForm.setMainContentVisible(true);
                        getSupportActionBar().show();

                    }
                } else if (returnString != null && returnString.equals("CREATE")) {

                    if (App.getPatient() != null) {

                        String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
                        String lname = App.getPatient().getPerson().getFamilyName();
                        if (!lname.equals(""))
                            lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);

                        patientName.setText(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")");
                        String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
                        if (!dob.equals("")) {
                            Date date = App.stringToDate(dob, "yyyy-MM-dd");
                            DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                            if (App.getPatient().getPerson().getAge() == 0) {
                                Date birthDate = App.stringToDate(App.getPatient().getPerson().getBirthdate(), "yyyy-MM-dd");
                                int age = App.getDiffMonths(birthDate, new Date());
                                if (age == 0) {
                                    long ageInLong = App.getDiffDays(birthDate, new Date());
                                    patientDob.setText(ageInLong + " days (" + df.format(date) + ")");
                                } else
                                    patientDob.setText(age + " months (" + df.format(date) + ")");
                            } else
                                patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
                        } else patientDob.setText(dob);
                        if (!App.getPatient().getPatientId().equals(""))
                            id.setVisibility(View.VISIBLE);
                        patientId.setText(App.getPatient().getPatientId());

                        Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.patient_created_successfully), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        headerLayout.setVisibility(View.VISIBLE);
                        fragmentReport.fillReportFragment();
                        fragmentSummary.updateSummaryFragment();
                        fragmentForm.fillScreeningFormContent();
                        fragmentForm.fillTestFormContent();
                        fragmentForm.fillTreatmentFormContent();
                        fragmentForm.fillCommonFormContent();

                        fragmentForm.setMainContentVisible(true);
                        getSupportActionBar().show();

                    }
                }


            }
        } else if (requestCode == SAVED_FORM_ACTIVITY) {
            if (resultCode == RESULT_OK) {

                String returnString = data.getStringExtra("form_id");

                Object[][] form = serverService.getSavedForm(Integer.parseInt(returnString));

                String toastMessage = "";

                String pid = String.valueOf(form[0][3]);
                if (!(pid == null || pid.equals("null"))) {

                    if (App.getPatientId() == null || !App.getPatientId().equals(String.valueOf(form[0][3]))) {

                        App.setPatientId(String.valueOf(form[0][3]));
                        App.setPatient(serverService.getPatientBySystemIdFromLocalDB(App.getPatientId()));

                        toastMessage = getResources().getString(R.string.selected_patient_changed) + " " + App.getPatient().getPerson().getGivenName() + " " + App.getPatient().getPerson().getFamilyName() + " (" + App.getPatient().getPatientId() + ")";

                    }
                    if (!App.getLocation().equals(String.valueOf(form[0][7]))) {
                        App.setLocation(String.valueOf(form[0][7]));

                        if (!toastMessage.equals(""))
                            toastMessage = toastMessage + "\n";

                        toastMessage = toastMessage + getResources().getString(R.string.selected_location_changed) + " " + App.getLocation();

                    }
                }

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preferences.PATIENT_ID, App.getPatientId());
                editor.putString(Preferences.LOCATION, App.getLocation());
                editor.apply();

                //nav_default.setText(getResources().getString(R.string.program) + App.getProgram() + "  |  " + getResources().getString(R.string.location) + App.getLocation());
                getSupportActionBar().setTitle(App.getLocation());

                if (!(pid == null || pid.equals("null"))) {
                    String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
                    String lname = App.getPatient().getPerson().getFamilyName();
                    if (!lname.equals(""))
                        lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);

                    patientName.setText(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")");
                    String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
                    if (!dob.equals("")) {
                        Date date = App.stringToDate(dob, "yyyy-MM-dd");
                        DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                        if (App.getPatient().getPerson().getAge() == 0) {
                            Date birthDate = App.stringToDate(App.getPatient().getPerson().getBirthdate(), "yyyy-MM-dd");
                            int age = App.getDiffMonths(birthDate, new Date());
                            if (age == 0) {
                                long ageInLong = App.getDiffDays(birthDate, new Date());
                                patientDob.setText(ageInLong + " days (" + df.format(date) + ")");
                            } else patientDob.setText(age + " months (" + df.format(date) + ")");
                        } else
                            patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
                    } else patientDob.setText(dob);
                    if (!App.getPatient().getPatientId().equals(""))
                        id.setVisibility(View.VISIBLE);
                    patientId.setText(App.getPatient().getPatientId());

                    headerLayout.setVisibility(View.VISIBLE);
                } else
                    headerLayout.setVisibility(View.GONE);

                fragmentReport.fillReportFragment();

                Boolean openFlag = data.getBooleanExtra("open", false);
                if (returnString != null) {
                    showFormFragment();
                    byte[] bytes = data.getByteArrayExtra("form_object");

                    ByteArrayInputStream bais = null;
                    ObjectInputStream ins = null;
                    try {

                        bais = new ByteArrayInputStream(bytes);
                        ins = new ObjectInputStream(bais);
                        FormsObject f = (FormsObject) ins.readObject();

                        fragmentForm.openForm(f, returnString, openFlag);


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                if (!toastMessage.equals(""))
                    Toast.makeText(getApplicationContext(), toastMessage,
                            Toast.LENGTH_LONG).show();

            }
        } else if (requestCode == PICK_FILE_RESULT_CODE) {
            if (resultCode == RESULT_OK) {

                Uri URI = data.getData();
                //String fileName = getFileName(URI);
                //File sd = Environment.getExternalStorageDirectory();
                //final String path = sd.getPath() + "//DCIM//" + fileName;
                String string = URI.getAuthority();
                String path = getPath(URI);
                final Backup backup = new Backup(this);
                backup.importDB(path, DatabaseUtil.getDbName());
                //serverService.checkAndUpdateDB();
            }
        }
    }

    public void openLocationSelectionDialog() {
        Intent languageActivityIntent = new Intent(this, LocationSelectionDialog.class);
        startActivity(languageActivityIntent);
    }

    public void showLocationAlert() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.dialog).create();
        alertDialog.setMessage(getResources().getString(R.string.gps_not_enabled));
        Drawable clearIcon = getResources().getDrawable(R.drawable.ic_gps);
        int color = App.getColor(context, R.attr.colorAccent);
        DrawableCompat.setTint(clearIcon, color);
        alertDialog.setIcon(clearIcon);
        alertDialog.setTitle(getResources().getString(R.string.gps_settings));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 102:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("*/*");
                    try {
                        startActivityForResult(intent, PICK_FILE_RESULT_CODE);
                    } catch (ActivityNotFoundException e) {
                        Log.d("Pick File", e.toString());
                    }
                } else {
                    Toast.makeText(MainActivity.this, "The app was not allowed to read your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            case 103:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    serverService.scheduleBackupWithDefaultValues();
                else
                    Toast.makeText(MainActivity.this, "The app was not allowed write to  your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startSync() {
        if (!OfflineFormSyncService.isInstanceCreated()) {
            startService(new Intent(this, OfflineFormSyncService.class));
        }

    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void loadEditPopup() {

        TextView backTextView = (TextView) editView.findViewById(R.id.cancelButton);
        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDimLayout.setVisibility(View.GONE);
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editView.getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                popupWindow.dismiss();
            }
        });

        TextView saveTextView = (TextView) editView.findViewById(R.id.saveButton);
        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDimLayout.setVisibility(View.GONE);
                savePersonAttributes();
                try {
                    View view = getCurrentFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editView.getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                popupWindow.dismiss();
            }
        });

        if (App.getRoles().contains(Roles.DEVELOPER) || App.getRoles().contains(Roles.PET_PROGRAM_MANAGER) || App.getRoles().contains(Roles.PET_FIELD_SUPERVISOR) ||
                App.getRoles().contains(Roles.CHILDHOODTB_MEDICAL_OFFICER) || App.getRoles().contains(Roles.CHILDHOODTB_PROGRAM_MANAGER) || App.getRoles().contains(Roles.CHILDHOODTB_PROGRAM_ASSISTANT) ||
                App.getRoles().contains(Roles.FAST_PROGRAM_MANAGER) || App.getRoles().contains(Roles.FAST_SITE_MANAGER) || App.getRoles().contains(Roles.FAST_FIELD_SUPERVISOR))
            saveTextView.setVisibility(View.VISIBLE);
        else
            saveTextView.setVisibility(View.GONE);

        addressContent.removeAllViews();
        attributeContent.removeAllViews();

        patientSource = new TitledEditText(context, null, getResources().getString(R.string.patient_source), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        addressContent.addView(patientSource);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        int padding = (int) getResources().getDimension(R.dimen.tiny);
        linearLayout.setPadding(padding, padding, padding, padding);

        addressContent.addView(linearLayout);

        TextView address = new TextView(this);
        address.setText("Patient's Address");
        address.setTypeface(null, Typeface.BOLD);
        address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        addressContent.addView(address);

        address1 = new TitledEditText(context, null, getResources().getString(R.string.pet_address_1), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        MyLinearLayout addressLayout = new MyLinearLayout(context, null, App.VERTICAL);
        MyTextView townTextView = new MyTextView(context, getResources().getString(R.string.pet_address_2));
        address2 = new AutoCompleteTextView(context);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(20);
        address2.setFilters(fArray);
        addressLayout.addView(townTextView);
        addressLayout.addView(address2);

        addressContent.addView(address1);
        addressContent.addView(addressLayout);

        LinearLayout linearLayoutProvince = new LinearLayout(this);
        linearLayoutProvince.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutProvince.setPadding(padding, padding, padding, padding);

        TextView provinceTextView = new TextView(this);
        provinceTextView.setText(getResources().getString(R.string.province) + ": ");
        linearLayoutProvince.addView(provinceTextView);

        province = new Spinner(context, Spinner.MODE_DIALOG);
        ArrayAdapter<String> adapterr2 =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.provinces));
        adapterr2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(adapterr2);
        province.setSelection(adapterr2.getPosition(App.getProvince()));
        province.setOnItemSelectedListener(this);

        linearLayoutProvince.addView(province);

        addressContent.addView(linearLayoutProvince);

        LinearLayout linearLayoutDistrict = new LinearLayout(this);
        linearLayoutDistrict.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutDistrict.setPadding(padding, padding, padding, padding);

        TextView districtTextView = new TextView(this);
        districtTextView.setText(getResources().getString(R.string.pet_district) + ": ");
        linearLayoutDistrict.addView(districtTextView);

        district = new Spinner(context, Spinner.MODE_DIALOG);
        String[] districts = serverService.getDistrictList(App.getProvince());
        ArrayAdapter<String> adapterr3 =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, districts);
        adapterr3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(adapterr3);
        district.setOnItemSelectedListener(this);

        linearLayoutDistrict.addView(district);

        addressContent.addView(linearLayoutDistrict);

        LinearLayout linearLayoutCity = new LinearLayout(this);
        linearLayoutCity.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutCity.setPadding(padding, padding, padding, padding);

        TextView cityTextView = new TextView(this);
        cityTextView.setText(getResources().getString(R.string.pet_city) + ": ");
        linearLayoutCity.addView(cityTextView);

        city = new Spinner(context, Spinner.MODE_DIALOG);
        String[] cities = serverService.getCityList(App.get(district));
        ArrayAdapter<String> adapterr4 =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, cities);
        adapterr4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adapterr4);

        linearLayoutCity.addView(city);

        addressContent.addView(linearLayoutCity);

        landmark = new TitledEditText(context, null, getResources().getString(R.string.pet_landmark), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        addressContent.addView(landmark);

        Object[][] towns = serverService.getAllTowns();
        String[] townList = new String[towns.length];

        for (int i = 0; i < towns.length; i++) {
            townList[i] = String.valueOf(towns[i][1]);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, townList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        address2.setAdapter(spinnerArrayAdapter);

        TextView attribute = new TextView(this);
        attribute.setText("Patient's Attributes");
        attribute.setTypeface(null, Typeface.BOLD);
        attribute.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        addressContent.addView(attribute);

        Object personAttributeTypes[][] = serverService.getAllPersonAttributeTypes();
        for (Object[] personAttributeType : personAttributeTypes) {
            if (String.valueOf(personAttributeType[1]).equalsIgnoreCase("java.lang.String")) {

                TitledEditText attributeTextView = new TitledEditText(context, null, String.valueOf(personAttributeType[0]) + ": ", "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
                attributeContent.addView(attributeTextView);
            } else if (String.valueOf(personAttributeType[1]).equalsIgnoreCase("java.lang.Integer")) {

                TitledEditText attributeTextView = new TitledEditText(context, null, String.valueOf(personAttributeType[0]) + ": ", "", "", 100, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
                attributeContent.addView(attributeTextView);
            } else if (String.valueOf(personAttributeType[1]).equalsIgnoreCase("java.lang.Float")) {

                TitledEditText attributeTextView = new TitledEditText(context, null, String.valueOf(personAttributeType[0]) + ": ", "", "", 100, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
                attributeContent.addView(attributeTextView);
            } else if (String.valueOf(personAttributeType[1]).equalsIgnoreCase("java.lang.Boolean")) {

                TitledRadioGroup attributeRadioGroup = new TitledRadioGroup(context, null, String.valueOf(personAttributeType[0]) + ": ", getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.HORIZONTAL);
                attributeContent.addView(attributeRadioGroup);
            } else if (String.valueOf(personAttributeType[1]).equalsIgnoreCase("org.openmrs.Location")) {

                LinearLayout ll = new LinearLayout(this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setPadding(padding, padding, padding, padding);

                TextView tv1 = new TextView(this);
                tv1.setText(String.valueOf(personAttributeType[0]) + ": ");
                ll.addView(tv1);

                ServerService serverService = new ServerService(getApplicationContext());
                final Object[][] locations = serverService.getAllLocationsFromLocalDB();
                String[] locs = new String[locations.length + 1];
                locs[0] = "";
                for (int i = 0; i < locations.length; i++) {
                    if (String.valueOf(locations[i][16]).equals("") || String.valueOf(locations[i][16]).equals("null"))
                        locs[i + 1] = String.valueOf(locations[i][1]);
                    else
                        locs[i + 1] = String.valueOf(locations[i][16]);
                }

                Spinner spinner = new Spinner(context, Spinner.MODE_DIALOG);
                ArrayAdapter<String> adapt =
                        new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item, locs);
                adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapt);

                ll.addView(spinner);

                attributeContent.addView(ll);

            } else if (String.valueOf(personAttributeType[1]).equalsIgnoreCase("org.openmrs.Concept")) {

                LinearLayout ll = new LinearLayout(this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setPadding(padding, padding, padding, padding);

                TextView tv1 = new TextView(this);
                tv1.setText(String.valueOf(personAttributeType[0]) + ": ");
                ll.addView(tv1);

                String foreignKey = String.valueOf(personAttributeType[2]);
                String conceptUuid = serverService.getConceptMappingForConceptId(foreignKey);

                Object[][] conceptAnswers = serverService.getConceptAnswers(conceptUuid);
                String[] answers = new String[conceptAnswers.length + 1];
                answers[0] = "";
                for (int i = 0; i < conceptAnswers.length; i++)
                    answers[i + 1] = String.valueOf(conceptAnswers[i][0]);

                Spinner spinner = new Spinner(context, Spinner.MODE_DIALOG);
                ArrayAdapter<String> adap =
                        new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item, answers);
                adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adap);

                ll.addView(spinner);

                attributeContent.addView(ll);
            }
        }
    }

    public void updatePopupContent() {

        String sourceType = serverService.getLatestObsValue(App.getPatientId(), "Patient Information", "PATIENT SOURCE");
        if (sourceType == null)
            sourceType = "";
        else if (sourceType.equals("OTHER PATIENT SOURCE"))
            sourceType = serverService.getLatestObsValue(App.getPatientId(), "Patient Information", "OTHER PATIENT SOURCE");

        sourceType = App.convertToTitleCase(sourceType);
        patientSource.getEditText().setText(sourceType);
        patientSource.getEditText().setKeyListener(null);

        address1.getEditText().setText(App.getPatient().getPerson().getAddress1());
        address2.setText(App.getPatient().getPerson().getAddress2());
        landmark.getEditText().setText(App.getPatient().getPerson().getAddress3());
        String p = App.getPatient().getPerson().getStateProvince();
        String d = App.getPatient().getPerson().getCountyDistrict();
        String c = App.getPatient().getPerson().getCityVillage();

        province.setSelection(0);
        if (!(p == null || p.equals("null") || p.equals(""))) {
            for (int j = 0; j < province.getCount(); j++) {
                if (province.getItemAtPosition(j).toString().equals(App.getPatient().getPerson().getStateProvince())) {
                    province.setSelection(j);
                    break;
                }
            }

            String[] districts = serverService.getDistrictList(App.getPatient().getPerson().getStateProvince());
            ArrayAdapter<String> adapt1 =
                    new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_item, districts);
            adapt1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            district.setAdapter(adapt1);
            district.setSelection(0);
            for (int j = 0; j < district.getCount(); j++) {
                if (district.getItemAtPosition(j).toString().equals(App.getPatient().getPerson().getCountyDistrict())) {
                    district.setSelection(j);
                    break;
                }
            }
            district.setTag("selected");

            String[] cities = serverService.getCityList(App.getPatient().getPerson().getCountyDistrict());
            ArrayAdapter<String> adapt =
                    new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_item, cities);
            adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            city.setAdapter(adapt);
            city.setSelection(0);
            for (int j = 0; j < city.getCount(); j++) {
                if (city.getItemAtPosition(j).toString().equals(App.getPatient().getPerson().getCityVillage())) {
                    city.setSelection(j);
                    break;
                }
            }
            city.setTag("selected");
        } else {
            String[] provinces = serverService.getProvinceList(App.getCountry());
            ArrayAdapter<String> adapt =
                    new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_item, provinces);
            adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            province.setAdapter(adapt);
            province.setSelection(adapt.getPosition(App.getProvince()));
        }

        for (int i = 0; i < attributeContent.getChildCount(); i++) {
            View v = attributeContent.getChildAt(i);
            if (v instanceof TitledEditText) {

                String attributeType = ((TitledEditText) v).getQuestionView().getText().toString();
                String val = App.getPatient().getPerson().getPersonAttribute(attributeType.replace(": ", ""));
                if (val == null) val = "";
                ((TitledEditText) v).getEditText().setText(val);

            } else if (v instanceof TitledRadioGroup) {

                String attributeType = ((TitledRadioGroup) v).getQuestionView().getText().toString();
                String val = App.getPatient().getPerson().getPersonAttribute(attributeType.replace(": ", ""));
                if (val == null) val = "";
                else {
                    if (val.equalsIgnoreCase("false") || val.equalsIgnoreCase("No")) val = "No";
                    else if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("Yes"))
                        val = "Yes";
                    else val = "";
                }

                ((TitledRadioGroup) v).getRadioGroup().clearCheck();
                for (RadioButton rb : ((TitledRadioGroup) v).getRadioGroup().getButtons()) {
                    String str = rb.getText().toString();
                    if (str.equals(val))
                        rb.setChecked(true);
                }

            } else if (v instanceof LinearLayout) {

                View v1 = ((LinearLayout) v).getChildAt(0);
                String attributeType = ((TextView) v1).getText().toString();
                String val = App.getPatient().getPerson().getPersonAttribute(attributeType.replace(": ", ""));
                if (val == null) val = "";
                else {

                    if (val.matches("[-+]?\\d*\\.?\\d+")) {
                        Object[] locs = serverService.getLocationNameThroughLocationId(val);
                        if (locs == null) val = "";
                        else val = String.valueOf(locs[1]);
                    }

                }

                Spinner spinner = (Spinner) ((LinearLayout) v).getChildAt(1);
                spinner.setSelection(0);
                for (int j = 0; j < spinner.getCount(); j++) {
                    if (spinner.getItemAtPosition(j).toString().equals(val)) {
                        spinner.setSelection(j);
                        break;
                    }
                }

            }
        }

    }

    public void savePersonAttributes() {

        loading.setInverseBackgroundForced(true);
        loading.setIndeterminate(true);
        loading.setCancelable(false);
        loading.setMessage(getResources().getString(R.string.submitting_form));
        loading.show();

        final HashMap<String, String> personAttribute = new HashMap<String, String>();

        for (int i = 0; i < attributeContent.getChildCount(); i++) {
            View v = attributeContent.getChildAt(i);
            if (v instanceof TitledEditText) {

                String attributeType = ((TitledEditText) v).getQuestionView().getText().toString().replace(": ", "");
                String val = App.getPatient().getPerson().getPersonAttribute(attributeType);
                if (val == null) val = "";
                String newVal = ((TitledEditText) v).getEditText().getText().toString();
                if (!val.equals(newVal)) {
                    personAttribute.put(attributeType, newVal);
                }

            } else if (v instanceof TitledRadioGroup) {

                String attributeType = ((TitledRadioGroup) v).getQuestionView().getText().toString().replace(": ", "");
                String val = App.getPatient().getPerson().getPersonAttribute(attributeType);
                if (val == null) val = "";
                else {
                    if (val.equalsIgnoreCase("false")) val = "No";
                    else if (val.equalsIgnoreCase("true")) val = "Yes";
                    else val = "";
                }

                String newVal = App.get((TitledRadioGroup) v);

                if (!val.equals(newVal)) {
                    personAttribute.put(attributeType, newVal);
                }

            } else if (v instanceof LinearLayout) {

                View v1 = ((LinearLayout) v).getChildAt(0);
                String attributeType = ((TextView) v1).getText().toString().replace(": ", "");
                String val = App.getPatient().getPerson().getPersonAttribute(attributeType);
                if (val == null) val = "";
                else {

                    if (RegexUtil.isNumeric(val, false)) {
                        Object[] locs = serverService.getLocationNameThroughLocationId(val);
                        if (locs == null) val = "";
                        else val = String.valueOf(locs[1]);
                    }

                }

                Spinner spinner = (Spinner) ((LinearLayout) v).getChildAt(1);
                String newVal = spinner.getSelectedItem().toString();

                if (!val.equals(newVal)) {
                    String format = serverService.getPersonAttributeFormat(attributeType);
                    if (format != null) {
                        if (format.equals("org.openmrs.Concept")) {
                            String[][] concept = serverService.getConceptUuidAndDataType(newVal);
                            if (concept.length > 0)
                                personAttribute.put(attributeType, concept[0][0]);
                        } else if (format.equals("org.openmrs.Location")) {
                            String uuid = serverService.getLocationUuid(newVal);
                            personAttribute.put(attributeType, uuid);
                        }
                    }

                }
            }
        }


        AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.submitting_form));
                        loading.show();
                    }
                });

                String result = serverService.savePatientInformationForm(personAttribute);
                if (!result.contains("SUCCESS"))
                    return result;
                else {

                    String encounterId = "";

                    if (result.contains("_")) {
                        String[] successArray = result.split("_");
                        encounterId = successArray[1];
                    }

                    result = serverService.saveMultiplePersonAttribute(personAttribute, encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    String add1 = App.getPatient().getPerson().getAddress1();
                    String add2 = App.getPatient().getPerson().getAddress2();
                    String add3 = App.getPatient().getPerson().getAddress3();
                    String pro = App.getPatient().getPerson().getStateProvince();
                    String dist = App.getPatient().getPerson().getCountyDistrict();
                    String cit = App.getPatient().getPerson().getCityVillage();
                    String addressUuid = App.getPatient().getPerson().getAddressUuid();

                    if (addressUuid == null || addressUuid.equals("null") || addressUuid.equals("")) {
                        result = serverService.savePersonAddress(App.get(address1), App.get(address2), App.get(city), App.get(district), App.get(province), App.getCountry(), App.getLongitude(), App.getLatitude(), App.get(landmark), encounterId);
                    } else if (!(add1.equals(App.get(address1)) && add2.equals(App.get(address2)) && add3.equals(App.get(landmark)) && pro.equals(App.get(province)) && dist.equals(App.get(district)) && city.equals(App.get(city)))) {
                        if (!(App.get(address1).equals("") && App.get(address2).equals("") && App.get(district).equals("") && App.get(landmark).equals(""))) {
                            result = serverService.updatePersonAddress(App.get(address1), App.get(address2), App.get(city), App.get(district), App.get(province), App.getCountry(), App.getLongitude(), App.getLatitude(), App.get(landmark), encounterId);
                        }

                    }

                    return "SUCCESS";

                }

            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.equals("SUCCESS")) {

                    if (serverService.getPendingOnlineSavedFormsCount(App.getUsername()) != 0 && !OnlineFormSyncService.isRunning())
                        startService(new Intent(MainActivity.this, OnlineFormSyncService.class));

                    //MainActivity.backToMainMenu();
                    try {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(buttonLayout.getWindowToken(), 0);
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
                                        imm.hideSoftInputFromWindow(buttonLayout.getWindowToken(), 0);
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
                                        imm.hideSoftInputFromWindow(buttonLayout.getWindowToken(), 0);
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
                                        imm.hideSoftInputFromWindow(buttonLayout.getWindowToken(), 0);
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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;

        if (spinner == district) {

            if (city.getTag() == null) {
                String[] cities = serverService.getCityList(App.get(district));
                ArrayAdapter<String> adapt =
                        new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item, cities);
                adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                city.setAdapter(adapt);
            } else city.setTag(null);

        } else if (spinner == province) {

            if (district.getTag() == null) {
                String[] districts = serverService.getDistrictList(App.get(province));
                ArrayAdapter<String> adapt =
                        new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item, districts);
                adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                district.setAdapter(adapt);
            } else district.setTag(null);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private String getPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            // MediaStore (and general)
            return getForApi19(uri);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getForApi19(Uri uri) {
        Log.e("tag", "+++ API 19 URI :: " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            Log.e("tag", "+++ Document URI");
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                Log.e("tag", "+++ External Document URI");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    Log.e("tag", "+++ Primary External Document URI");
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                Log.e("tag", "+++ Downloads External Document URI");
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                Log.e("tag", "+++ Media Document URI");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    Log.e("tag", "+++ Image Media Document URI");
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    Log.e("tag", "+++ Video Media Document URI");
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    Log.e("tag", "+++ Audio Media Document URI");
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.e("tag", "+++ No DOCUMENT URI :: CONTENT ");

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.e("tag", "+++ No DOCUMENT URI :: FILE ");
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}