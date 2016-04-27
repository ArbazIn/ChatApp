package chatapp.com.chatapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

/**
 * Created by vivacious on 4/5/2016.
 */
public class Delete_Chat extends AppCompatActivity {
    Button btn_delete;
    EditText ed_delete, ed_admin_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ed_delete = (EditText) findViewById(R.id.ed_delete);
        ed_admin_name = (EditText) findViewById(R.id.ed_admin_name);
        btn_delete = (Button) findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    final Firebase myFirebaseRef = new Firebase("https://masandy.firebaseio.com/" + ed_delete.getText().toString());
                    myFirebaseRef.removeValue();
                }
            }

        });
    }

    public boolean validate() {
        boolean valid = false;

        String name = ed_admin_name.getText().toString();

        if (name.equals("masandy")) {
            ed_admin_name.setError(null);
            valid = true;
        } else if (name.trim().isEmpty()) {
            ed_admin_name.setError("Wrong Admin name");
            valid = false;
        } else {
            ed_admin_name.setError("Wrong Admin name");
        }


        return valid;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            Intent intentHome = new Intent(this, MainActivity.class);
            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentHome);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
