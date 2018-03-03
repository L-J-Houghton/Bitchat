package com.bitchat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//LOGIN ACTIVITY
//CreateS the Login Activity which houses all the pages seen while
//logged OUT. IT was built on top of the LoginActivity template.
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {


    private UserLoginTask mAuthTask = null;

    //VIEWS
    //the Views for everything editable/clickable inside the blue circle on the home screen.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;

    //ANIMATION VIEW
    //responsible for the loading animation that appears while the user is attempting to log in.
    private View mProgressView;

    //DATA COLLECTED
    //Strings for storing data collected from the database once a login attempt has been made.
    private String username;
    private String errorMessage;

    //This string is only needed temporarily while this activity still contains the string request
    // for logging in which in due time, once a OnSuccessListener class has been added to function,
    // will be moved into AccountDatabaseOperations.
    private final String url = "http://doc.gold.ac.uk/~rcorb001/bitChat/db_operations_account2.0.php";
    private AccountDatabaseOperations db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("");
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);


        db = new AccountDatabaseOperations();

        //If the sign in button is pressed attemptLogin()
        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        //If the register button is pressed attemptRegister()
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //extract the values entered into the editText Fields.
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                if (password.length() > 6)
                {
                    attemptRegister(email, password);

                }
                else Toast.makeText(LoginActivity.this, "Please enter 7+ character password", Toast.LENGTH_SHORT).show();
            }
        });


        //setOnActionListener for if the username and password is not accepted.
        //a message should appear just above the password EditText to tell the user the password or
        //username was incorrect
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        //Prepare the animations involved in logging in.
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        //Function written to ensure that only an email with the gold.ac.uk extension is entered.
        concatenateEmailExtension();


    }

    private void concatenateEmailExtension() {

        Selection.setSelection(mEmailView.getText(), mEmailView.getText().length());

        //Set the text to the extension
        mEmailView.setText("@gold.ac.uk");

        mEmailView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void afterTextChanged(Editable s) {

                //after anything is changed to the text, if the text does not contain the extension
                if (!s.toString().endsWith("@gold.ac.uk")) {

                    //set the text back to just the extension again
                    mEmailView.setText("@gold.ac.uk");

                    Selection.setSelection(mEmailView.getText(), mEmailView.getText().length());
                }

            }
        });

    }

    // function written to add registration functionality.
    private void attemptRegister(final String mEmail, final String mPassword) {

        //upon pressing the register button a dialog box will pop up asking if the user is happy
        //with their chosen email and password.
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Create New Account")
                .setMessage("Are you sure you want to create an account for " + mEmail + "?\n Are you happy with the password you have entered?")

                //if they click yes, run the registerAccount function to send the request to the server to
                //insert the account into the table.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.registerAccount(mEmail, mPassword, getApplicationContext());
                    }
                })
                //if they click no do nothing.
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    //attempt to login
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        //reset the errors
        mEmailView.setError(null);
        mPasswordView.setError(null);
        boolean cancel = false;

        //get the values from the EditText views
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();


        //check if the entered values meet the minimum requrements
        View focusView = null;
        if (!isPasswordValid(password)) {
            mPasswordView.setError("The password must be atleast 6 characters long");
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError("You must input a 'gold.ac.uk' account");
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();

            //attempt to create a new login task
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }


    //function used to verify if email is valid
    private boolean isEmailValid(String email) {

        //check that the email has the right extension and that length of the email is long enough
        return email.endsWith("@gold.ac.uk") && email.length() > 15;

    }

    //function used to verify if password is valid
    private boolean isPasswordValid(String password) {
        //check that the password is long enough.
        //check if  password.matches(".*\\d+.*"); should be added however,
        //as many of our test users have already created their passwords this will have to wait.
        return password.length() > 5;

    }

    //Display the animation (untouched function from template)
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    //(untouched function from template)
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    //(untouched function from template)
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

    }

    //(untouched function from template)
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    //(untouched function from template)
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }


    //The following class handles the login attempt once everything everything else has been verified
    //and allows the animation from the LoginActivity to happen synchronously with the login attempt.
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        //This function currently contains the request for attempting login which will be moved
        // to AccountDatabaseOperations
        @Override
        protected Boolean doInBackground(Void... params) {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            //simply used to receive the response
            final String[] response = new String[1];
            response[0] = "notset";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String Response) {
                            response[0] = Response.trim();


                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    response[0] = "response error";


                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("REQUEST_TYPE", "LOGIN");
                    params.put("username", mEmail);
                    params.put("password", mPassword);
                    return params;
                }
            };


            //sleep the thread for 3000 miliseconds to wait for a response. This will not be needed
            // once an OnSuccessfulResponse class has been created to handle responses.
            try {
                queue.add(stringRequest);
                Thread.sleep(3000);

            } catch (InterruptedException e) {

                return false;
            }

            //provide the appropriate error message based on the response and return false.
            //else store the username of the accepted user and return true.
            if (response[0].equals("incorrect")) {
                errorMessage = "wrong username or password";
                return false;
            } else if (response[0].equals("notset") || response[0].equals("response error")) {
                errorMessage = "error connecting";
                return false;
            } else if (response[0].equals("unactivated")) {
                errorMessage = "Check email " + mEmail + " for activation link";
                return false;
            } else {
                username = response[0];
                return true;
            }
        }


        //if successful start the MainActivity by creating an intent and passing the value of the username
        //through to the next activity.
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            //if login was successful
            if (success) {


                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                intent.putExtra("username", username);

                startActivity(intent);
                //sleep the thread for 1000 milliseconds as it helps with ensuring that everything
                //is loaded in the MainActivity before this Activity is finished
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Close this Activity.
                finish();

                //else display the error message created in doInBackground
            } else {
                Toast.makeText(LoginActivity.this, errorMessage,
                        Toast.LENGTH_LONG).show();
            }
        }

        //(untouched function from template)
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


}

