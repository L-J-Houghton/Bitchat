package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.bitchat.MainActivity;
import com.bitchat.R;

//SettingsFragment
//currently only used to change username
public class settingsFragment extends Fragment implements View.OnClickListener {

    EditText usernameEditText;
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Settings");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //create all the views
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        button = (Button) rootView.findViewById(R.id.update_username_button);
        button.setOnClickListener(this);
        usernameEditText = (EditText) rootView.findViewById(R.id.update_username_edittext);
        usernameEditText.setText(MainActivity.username);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        //when the button is clicked change the username if the name is atleast 6 characters long
        Log.d("Button", "button clicked");
        switch (view.getId()) {
            case R.id.update_username_button: {
                String newname = usernameEditText.getText().toString();
                if (newname.length() > 2) {
                    //call the DatabaseOperations function updateUsername and pass in the new and old username.
                    ((MainActivity) getActivity()).db.updateUsername(newname, MainActivity.username, this.getContext());
                    //hide the keyboard.
                    View keybView = getActivity().getCurrentFocus();
                    if (keybView != null) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(keybView.getWindowToken(), 0);
                    }
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}



