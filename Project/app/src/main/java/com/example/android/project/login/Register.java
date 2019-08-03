package com.example.android.project.login;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.example.android.project.R;
import com.example.android.project.data.contract;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//To register for a new user
public class Register extends AppCompatActivity {
    EditText register_username,register_password,confirmpass;
    boolean check;
    int cnt=0;
    private ProgressBar loading;
private static final int REGISTER_LOADER=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_register);
        loading=findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        register_username=findViewById(R.id.register_username);
        register_password=findViewById(R.id.register_password);
        confirmpass=findViewById(R.id.confirmpassword);
        register_password.setError("The password must be atleast 8 characters long");
    }
    public void submitRegister(View view)
    {  check=true;
        if(TextUtils.isEmpty(register_username.getText()))
        {
            register_username.setError("Please enter the username");
            check=false;
        }
        if(TextUtils.isEmpty(register_password.getText()))
        {    check=false;
            register_password.setError("Please enter a password");
        }
        if(register_password.length()<8)
        { check=false;
            register_password.setError("The password must be atleast 8 characters long");}
        if(TextUtils.isEmpty(confirmpass.getText())|| !register_password.getText().toString().equals(confirmpass.getText().toString()))
        { check=false;
         confirmpass.setError("Please re-enter the correct password");
        }
        if(check) {
          registerUser();
        }
    }
    private void registerUser()
    {
        loading.setVisibility(View.VISIBLE);
                 getLoaderManager().initLoader(REGISTER_LOADER,null,toRegister);

           loading.setVisibility(View.GONE);

    }
    public LoaderCallbacks<Cursor> toRegister=new LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String[] projection= {
                    contract.loginEntry.rowid,
                    contract.loginEntry.c1username,
                    contract.loginEntry.c2password
            };
            return new CursorLoader(Register.this,contract.loginEntry.CONTENT_URI,projection,null,null,null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if(data!=null)
            {  try{int usernameIndex=data.getColumnIndex(contract.loginEntry.c1username);
                while(data.moveToNext())
                { if(data.getString(usernameIndex)!=null&&data.getString(usernameIndex).equals(register_username.getText().toString()))
                {       cnt++;
                }}}
            finally {
                data.close();
            }}
            if(cnt==0)
            {
                ContentValues contentValues=new ContentValues();
                contentValues.put(contract.loginEntry.c1username,register_username.getText().toString());
                contentValues.put(contract.loginEntry.c2password,register_password.getText().toString());
                Uri newuri=getContentResolver().insert(contract.loginEntry.CONTENT_URI,contentValues);
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                register_username.setError("Username already exists");
            }
            cnt=0;
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };
    //If the user wants to login using an already existing account
    public void backtologin(View view)
    { Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }
}
