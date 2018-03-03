package fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bitchat.MainActivity;
import com.bitchat.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

//FriendsFragment
//contains a simple list of clickable items used to navigate to users forums. Currently displays all
//users forums and not just friends.
public class FriendsFragment extends Fragment {

    ArrayList<String> friendsArrayList = new ArrayList();
    ArrayAdapter adapter;
    ListView listView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        listView = (ListView) rootView.findViewById(R.id.friends_list);
        listView.setAdapter(adapter);

        //when an item is clicked from the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //get the string at the index of the chosen item.
                String chosenForum = (String) listView.getAdapter().getItem(position);
                //call the MainActivity function enterForum to create an InsideForumFragment.
                ((MainActivity) getActivity()).enterForum(chosenForum, "Other Users Forum");
            }
        });
        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        MainActivity.forumArray = new JSONArray();

        super.onCreate(savedInstanceState);
        getActivity().setTitle("User's Forums");
        ((MainActivity) getActivity()).db.pullUsers(this.getContext());

        adapter = new ArrayAdapter<>(this.getActivity(), R.layout.list_item, friendsArrayList);

        final Handler ha = new Handler();
        //update the forum adapter every 1000 milliseconds to ensure that if another user changes their name,
        //other users on this screen will see their name change too.
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {

                updateForumAdapter(MainActivity.forumArray);


                ha.postDelayed(this, 1000);

            }
        }, 500);


    }

    //add the items from the JSONArray received from the request inside pullForums in DatabaseOperations to the Arraylist used by the adapter
    //call notifyDataSetChanged() on the adapter to update the values displayed in the list.
    private void updateForumAdapter(JSONArray jsonArray) {
        friendsArrayList.clear();
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                try {
                    friendsArrayList.add(jsonArray.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        adapter.notifyDataSetChanged();
    }


}
