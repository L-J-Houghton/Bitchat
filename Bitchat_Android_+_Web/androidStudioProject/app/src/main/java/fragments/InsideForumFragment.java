package fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bitchat.MainActivity;
import com.bitchat.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

//InsideForumFragment
//contains a list of messages and an edit text field for entering a message and a button to send it
//One Fragment used for all Forums.
public class InsideForumFragment extends Fragment {


    //used to determine where the messages are posted and which messages are displayed.
    String forumName;
    ArrayList<String> chatArrayList = new ArrayList();
    ArrayAdapter adapter;
    ListView listView;
    EditText editText;
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //clear the chatArray
        MainActivity.chatArray = new JSONArray();
        //get the chosen forum
        forumName = MainActivity.chosenForum;
        adapter = new ArrayAdapter<>(this.getActivity(), R.layout.list_item, chatArrayList);
        //set the title of the page to the name of the forum
        getActivity().setTitle(forumName);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_inside_forum, container, false);
        listView = (ListView) rootView.findViewById(R.id.chat_list);
        editText = (EditText) rootView.findViewById(R.id.textField);
        listView.setAdapter(adapter);
        //scroll to the end of the list.
        listView.setSelection(adapter.getCount() - 1);

        //every 500 milliseconds if the user is inside the forum, update the chat adapter.
        final Handler ha = new Handler();

        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (MainActivity.inForum) {
                    updateChatAdapter(MainActivity.chatArray);
                }

                ha.postDelayed(this, 500);

            }
        }, 500);

        //Send button
        button = (Button) rootView.findViewById(R.id.btn_send);

        //If clicked and the message field isnt blank, run the DatabaseOperations function postMessage
        //then clear the message field
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editText.getText().toString().equals("")) {
                    ((MainActivity) getActivity()).db.postMessage(MainActivity.chosenForum, editText.getText().toString().trim(), MainActivity.username, getActivity().getApplicationContext());
                    editText.setText("");

                }
            }
        });


        return rootView;

    }

//on start ensure that inForum is set to true
    public void onStart() {
        super.onStart();
        MainActivity.inForum = true;


    }
    //add the items from the JSONArray received from the request inside pullChatList in DatabaseOperations to the Arraylist used by the adapter
    //call notifyDataSetChanged() on the adapter to update the values displayed in the list.
    private void updateChatAdapter(JSONArray jsonArray) {
        chatArrayList.clear();
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                try {
                    chatArrayList.add(jsonArray.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        adapter.notifyDataSetChanged();
    }


}
