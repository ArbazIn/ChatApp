package Global;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import chatapp.com.chatapp.R;

/**
 * Created by vivacious on 4/4/2016.
 */
public class Global {

    private final Context context;
    private static Dialog m_dialog;
    private static LinearLayout alert_linear;
    public Global(Context context){
        this.context=context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static SharedPreferences sharedPref;
        public static void storePreference(HashMap<String, String> parameters) {
            SharedPreferences.Editor editor = sharedPref.edit();
            Iterator<Map.Entry<String, String>> it = parameters.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pairs = it.next();
                editor.putString((String) pairs.getKey(), (String) pairs.getValue());
            }
            editor.commit();
        }

        public static void storePreference(String key, String value) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(key, value);
            editor.commit();
        }

        public static void storePreference(String key, int value) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(key, value);
            editor.commit();
        }

        public static void storePreference(String key, long value) {
            SharedPreferences.Editor editor =sharedPref.edit();
            editor.putLong(key, value);
            editor.commit();
        }
        public static boolean storePreference(String key, Boolean value) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(key, value);
            editor.commit();
            return false;
        }



        public static HashMap<String, String> getPreference(String[] keys) {
            HashMap<String, String> parameters = new HashMap<String, String>();
            for (String key : keys) {
                parameters.put(key, sharedPref.getString(key, null));
            }
            return parameters;
        }


        public static String getPreference(String key, String defValue) {
            return sharedPref.getString(key, defValue);
        }

        public static long getPreference(String key, long defValue) {
            return sharedPref.getLong(key, defValue);
        }

        public static int getPreference(String key, int defValue) {
            return sharedPref.getInt(key, defValue);
        }

        public static Boolean getPreference(String key, Boolean defValue) {
            return sharedPref.getBoolean(key, defValue);
        }
        public static void clearPrefernces() {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.commit();
        }


    public static void showAlertDialog(final Context context, String headerTitle ,String message) {
        m_dialog = new Dialog(context, R.style.Dialog_No_Border);
        m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        m_dialog.getWindow().getAttributes().windowAnimations = R.style.custom_delete_dialog_animation;

        LayoutInflater m_inflater = LayoutInflater.from(context);
        View m_view = m_inflater.inflate(R.layout.custom_alert_dialog, null);
        alert_linear = (LinearLayout) m_view.findViewById(R.id.alert_linear);




        TextView logout_tv_title = (TextView) m_view.findViewById(R.id.alert_tv_title);
        TextView tv_custom_logout_msg = (TextView) m_view.findViewById(R.id.tv_custom_alert_msg);
        Button btn_ok_alert_dialog = (Button) m_view.findViewById(R.id.btn_ok_alert_dialog);


        logout_tv_title.setText(headerTitle);

        tv_custom_logout_msg.setText(message);


        alert_linear.setBackgroundResource(R.drawable.btn_style_roundcorner);




        View.OnClickListener m_clickListener = new View.OnClickListener(){

            @Override
            public void onClick(View p_v)
            {


                switch (p_v.getId())
                {
                    case R.id.btn_ok_alert_dialog:
                        m_dialog.dismiss();

                        break;



                    default:
                        break;
                }
            }
        };

        btn_ok_alert_dialog.setOnClickListener(m_clickListener);


        m_dialog.setContentView(m_view);
        m_dialog.show();

    }

    }
