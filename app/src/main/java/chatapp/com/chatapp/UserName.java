package chatapp.com.chatapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

import Global.Global;

/**
 * Created by vivacious on 4/4/2016.
 */
public class UserName extends Activity {
    EditText ed_name;
    EditText ed_database;
    Button btn_set;
    Global global;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.user_name);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Wedgie Regular.ttf");
        global = new Global(UserName.this);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_database = (EditText) findViewById(R.id.ed_database);


        ed_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ed_name.setTypeface(null, Typeface.BOLD);
                ed_name.setHint("Set You Chat Name");
            }
        });
        ed_database.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ed_database.setTypeface(null, Typeface.BOLD);
                ed_database.setHint("Set Group Name");
            }
        });
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                String[] str = possibleEmail.split("@");
                String var = "" + str[0].charAt(0);
                var = var.toUpperCase();
                var += str[0].substring(1, str[0].length());
                ed_name.setText(var);
            }
        }

        btn_set = (Button) findViewById(R.id.btn_set);
        btn_set.setTypeface(font);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    Global.storePreference("ChatPrefs", ed_name.getText().toString());
                    Global.storePreference("ChatGroup", ed_database.getText().toString());
                    Global.storePreference("IsLogin", true);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });
        if (Global.getPreference("IsLogin", false)) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

    }


    public boolean validate() {
        boolean valid = true;

        String name = ed_name.getText().toString();
        String dbname = ed_database.getText().toString();

        if (name.trim().isEmpty()) {
            ed_name.setError("Set Your Chat Name");

            valid = false;
        } else {
            ed_name.setError(null);
        }
        if (dbname.trim().isEmpty()) {
            ed_database.setError("Set chat Group Name");

            valid = false;
        } else {
            ed_database.setError(null);
        }


        return valid;
    }
}
