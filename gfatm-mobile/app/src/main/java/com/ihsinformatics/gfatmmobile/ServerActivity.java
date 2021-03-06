package com.ihsinformatics.gfatmmobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.appbar.AppBarLayout;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServerActivity extends AbstractSettingActivity {

    EditText ip;
    EditText port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        int color = App.getColor(this, R.attr.colorAccent);
        LinearLayout ipLayout = new LinearLayout(this);
        ipLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        ipLayout.setOrientation(LinearLayout.VERTICAL);
        ipLayout.setPadding(0, 40, 0, 40);

        TextView ipTextView = new TextView(this);
        ipTextView.setText(getString(R.string.ip));
        ipTextView.setTextColor(color);
        ipLayout.addView(ipTextView);
        ip = new EditText(this);
        ip.setInputType(InputType.TYPE_CLASS_TEXT);
        ip.setMaxEms(50);
        ip.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        ip.setSingleLine(true);
        ip.setText(App.getIp());
        ip.setGravity(Gravity.LEFT);
        ipLayout.addView(ip);

        layout.addView(ipLayout);

        LinearLayout portLayout = new LinearLayout(this);
        portLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        portLayout.setOrientation(LinearLayout.VERTICAL);
        portLayout.setPadding(0, 40, 0, 40);

        TextView portTextView = new TextView(this);
        portTextView.setText(getString(R.string.port));
        portTextView.setTextColor(color);
        portLayout.addView(portTextView);
        port = new EditText(this);
        port.setInputType(InputType.TYPE_CLASS_PHONE);
        port.setMaxEms(4);
        port.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        port.setSingleLine(true);
        port.setText(App.getPort());
        port.setGravity(Gravity.LEFT);
        portLayout.addView(port);

        layout.addView(portLayout);

        resetButton.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {


        if (v == resetButton) {

            ip.setError(null);
            port.setError(null);

            ip.setText(getString(R.string.ip_default));
            port.setText(getString(R.string.port_default));

        } else if (v == okButton) {

            Boolean cancel = validateFields();
            if (!cancel) {

                App.setIp(App.get(ip));
                App.setPort(App.get(port));

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ServerActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preferences.IP, App.getIp());
                editor.putString(Preferences.PORT, App.getPort());
                editor.apply();

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                onBackPressed();
            }

        }

    }

    public Boolean validateFields() {

        ip.setError(null);
        port.setError(null);

        Boolean cancel = false;

       /* if (App.get(port).isEmpty()) {
            port.setError(getString(R.string.empty_field));
            port.requestFocus();
            cancel = true;
        } */

        if (App.get(ip).isEmpty()) {
            ip.setError(getString(R.string.empty_field));
            ip.requestFocus();
            cancel = true;
        }

        return cancel;
    }


}
