package com.example.android.project.login;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.project.R;
import com.example.android.project.data.contract;
import com.example.android.project.viewHome;
import androidx.appcompat.app.AppCompatActivity;
//Login page
public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
private static final int LOGIN_LOADER=1;
EditText username,password;
   int checkusername=0;
public static  String USERNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    username=findViewById(R.id.username);
    password=findViewById(R.id.password);

    }
    //Check the database for the user
    public void submitLogin(View view)
    { if(TextUtils.isEmpty(username.getText()))
    {
        username.setError("Enter username");
    }
    else if(TextUtils.isEmpty(password.getText()))
    {
        password.setError("Enter password");
    }
    else
    {
        getLoaderManager().restartLoader(LOGIN_LOADER,null,this);}
    }
    //If new user
    public void Register(View view)
    { Intent intent=new Intent(this,Register.class);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection= {
                contract.loginEntry.rowid,
                contract.loginEntry.c1username,
                contract.loginEntry.c2password
        };
        return new CursorLoader(this,contract.loginEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
         if (data.getCount()>0) {
             int usernameIndex = data.getColumnIndex(contract.loginEntry.c1username);
             int passwordIndex = data.getColumnIndex(contract.loginEntry.c2password);
             checkusername=0;
             while (data.moveToNext()) {

                 if (data.getString(usernameIndex)!=null&&data.getString(usernameIndex).equals(username.getText().toString()))
             { checkusername++;
                 if (data.getString(passwordIndex)!=null&&data.getString(passwordIndex).equals(password.getText().toString())) {
                     USERNAME = username.getText().toString();

                     Intent intent2 = new Intent(MainActivity.this, viewHome.class);
                     startActivity(intent2);
                     break;

                 }
                 else
                 {
                     Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                     password.setText("");
                     break;
                 }
             }}
          if(checkusername==0)
             Toast.makeText(this, "Username does not exist", Toast.LENGTH_SHORT).show();
       }
       else
        {Toast.makeText(this, "Username does not exist", Toast.LENGTH_SHORT).show(); }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
