package com.project.yoavr.lookforplace;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentA extends Fragment {
    RecyclerView recyclerView;
    EditText editTextsearch;
    View view;
    CheckBox checkBox;
    FragmnetChanger fragmnetChanger;
    ArrayList<MyPlace> allresult;
    SharedPreferences preferences;
    String wordtochak;
   public static int index=0;
    public FragmentA() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_a, container, false);
preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!isNetworkAvailable(getActivity()))
        {
            //NO internet on divice
            //take information from table result put it on value "Place"(image,name,addres,distance)  and send it to another adpter
            Toast.makeText(getActivity(), "NO INTERNET show last result", Toast.LENGTH_SHORT).show();
            MySqlResults mySqlResults=new MySqlResults(getActivity());
            allresult= new ArrayList<>();
            allresult.clear();
            Cursor cursor= mySqlResults.getReadableDatabase().query("myresult" , null, null, null, null,null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                String addres = cursor.getString(cursor.getColumnIndex("adders"));
                String distane = cursor.getString(cursor.getColumnIndex("distance"));
                MyPlace place = new MyPlace(image, name, addres, distane, id);
                allresult.add(place);
            }
            recyclerView=(RecyclerView)view.findViewById(R.id.ListRecycle);
            GridLayoutManager gridLayoutManager=(new GridLayoutManager(getActivity(),1));
           recyclerView.setLayoutManager(gridLayoutManager);
            MyResultAdapter adapter= new MyResultAdapter(allresult,getActivity());
            recyclerView.setAdapter(adapter);


        }
        else
        {
            //have internt

            Toast.makeText(getActivity(), "start auto search", Toast.LENGTH_SHORT).show();
            //on Larage divice map  will show
            if (MainActivity.isLarge) {
                Toast.makeText(getActivity(), "The map will show in few second", Toast.LENGTH_SHORT).show();
            }
            //wordtochak=last search defult value is null
            wordtochak=preferences.getString("place",wordtochak);
            //if enter and have value on wordtochak start auto search for this word
            if(wordtochak!=null) {
                Intent intent = new Intent(getActivity(), MySearchService.class);
                intent.putExtra("search", wordtochak);
                intent.setAction("globalsearch");
                intent.putExtra("index",1);

                getActivity().startService(intent);
            }

        }

        checkBox=(CheckBox)view.findViewById(R.id.checkBox);
        recyclerView=(RecyclerView)view.findViewById(R.id.ListRecycle);
        editTextsearch= (EditText) view.findViewById(R.id.Etsearch);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new MyPlaceRecieer(), new IntentFilter("FINISHED_DETAILS"));
        ((Button)view.findViewById(R.id.btnlocalsearch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(), "NO INTERNT", Toast.LENGTH_SHORT).show();
                } else {

                    if (checkBox.isChecked()) {
                        String placetolook = editTextsearch.getText().toString();
                        preferences.edit().putString("place",placetolook).commit();
                        Intent intent = new Intent(getActivity(), MySearchService.class);
                        intent.putExtra("searchnear", placetolook);
                        intent.setAction("location");
                        getActivity().startService(intent);
                        MySqlResults helper2 = new MySqlResults(getActivity());
                        helper2.getWritableDatabase().delete("myresult", null, null);
                    } else {
                        String tempstring = editTextsearch.getText().toString();
                        String placetolook = tempstring.replaceAll(" ", "%20");
                        preferences.edit().putString("place",placetolook).commit();
                        Intent intent = new Intent(getActivity(), MySearchService.class);
                        intent.putExtra("search", placetolook);
                        intent.setAction("globalsearch");
                        getActivity().startService(intent);
                        MySqlResults helper2 = new MySqlResults(getActivity());
                        helper2.getWritableDatabase().delete("myresult", null, null);
                    }

                }
            }
        });
        fragmnetChanger= (FragmnetChanger) getActivity();
        return  view;
    }



    class MyPlaceRecieer extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Recie the Broadcast and send the results to the adpter
            ArrayList<MyPlace> allplaces= intent.getParcelableArrayListExtra("Places");
            recyclerView.setLayoutManager(new GridLayoutManager(context,1));
            MyplaceAdapter adapter= new MyplaceAdapter(allplaces, context,fragmnetChanger );
            recyclerView.setAdapter(adapter);

        }

    }

    //Fanction is cheaking if Internet Working or not
    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
