package chatapp.com.chatapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import Global.Global;

public class MainActivity extends AppCompatActivity {

    // TODO: change this to your own Firebase URL
    private static final String FIREBASE_URL = "https://masandy.firebaseio.com/";

    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;
    Dialog delete_chat;
    private SimpleDateFormat dateFormatter;
    TextView tvtime;
    private PendingIntent pendingIntent1;
    ListView listView;
    ImageView send_emoji;
    //shake
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeEventListener mShakeDetector;
    int count = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        // Make sure we have a mUsername
        setupUsername();

        tvtime = (TextView) findViewById(R.id.tvtime);

        setTitle("Chat As " + mUsername);

        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child(Global.getPreference("ChatGroup", ""));

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   sendMessage();

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //shake logic


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeEventListener(new ShakeEventListener.OnShakeListener() {

            @Override
            public void onShake() {
                count++;
                Log.e("shake", "" + count);
                if (count == 1) {
                    sendMessage();
                    count = 0;
                }
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete:

                startActivity(new Intent(MainActivity.this, Delete_Chat.class));
                break;
            case R.id.action_logout: {
                Global.clearPrefernces();
                if (Global.getPreference("IsLogin", true)) {
                    startActivity(new Intent(getApplicationContext(), UserName.class));
                }

            }

        }

        //Return false to allow normal menu processing to proceed,
        //true to consume it here.
        return false;
    }


    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        listView = (ListView) findViewById(R.id.list);
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this, R.layout.receive_chat, mUsername);
        listView.setAdapter(mChatListAdapter);
        final Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down_bounce);
        listView.setAnimation(animation);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        //List View Chages


        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                dataSnapshot.getKey();
                if (connected) {
                    mFirebaseRef.orderByKey().startAt("time_stamp");
                    Toast.makeText(MainActivity.this, "Connected to Mas Server", Toast.LENGTH_SHORT).show();

                } else {
                    //Toast.makeText(MainActivity.this, "Disconnected from Mas Server", Toast.LENGTH_SHORT).show();
                    Global.showAlertDialog(MainActivity.this, "Error", "Disconnected from Mas Server");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://chatapp.com.chatapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://chatapp.com.chatapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    private void setupUsername() {
//        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
//        mUsername = prefs.getString("username", null);
        mUsername = Global.getPreference("ChatPrefs", "");

        if (mUsername == null) {
            Random r = new Random();
            // Assign a random user name if we don't have one saved.
            mUsername = "I am sender:" + r.nextInt(100000);
            //prefs.edit().putString("username", mUsername).commit();


        }
    }


    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        //date Time
        String input = inputText.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        String strDate = sdf.format(c.getTime());
        //Time Stamp
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, mUsername, strDate, ts);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            inputText.setText("");

        }
    }

}