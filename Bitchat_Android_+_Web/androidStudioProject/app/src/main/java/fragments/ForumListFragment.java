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

//ForumListFragment
//contains a simple list of clickable items used to navigate to forumName forums
public class ForumListFragment extends Fragment {
    ArrayList<String> forumArrayList = new ArrayList();
    ArrayAdapter adapter;
    ListView listView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_forum_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.forum_list);
        listView.setAdapter(adapter);


        //when an item is clicked from the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //get the string at the index of the chosen item.
                String chosenForum = (String) listView.getAdapter().getItem(position);
                //call the MainActivity function enterForum to create an InsideForumFragment.
                ((MainActivity) getActivity()).enterForum(chosenForum, "From ForumList Forum");


            }
        });
        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        MainActivity.forumArray = new JSONArray();
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Forums");


        ((MainActivity) getActivity()).db.pullForums(this.getContext());

        adapter = new ArrayAdapter<>(this.getActivity(), R.layout.list_item, forumArrayList);


        final Handler ha = new Handler();
        //update the forum adapter every 1000 milliseconds
        //unnecessary at this point as the forums in the list wont change while on this page
        //but will be useful when forums are customisable and can be created.
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
        forumArrayList.clear();
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                try {

                    forumArrayList.add(jsonArray.get(i).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        adapter.notifyDataSetChanged();
    }


}
