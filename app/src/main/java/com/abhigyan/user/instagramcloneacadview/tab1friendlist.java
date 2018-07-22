package com.abhigyan.user.instagramcloneacadview;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by USER on 16-07-2018.
 */

public class tab1friendlist extends Fragment {

    ListView friendlist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab1friendlist, container, false);
        friendlist = rootView.findViewById(R.id.friendList);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Parse.initialize(getContext());
        ParseInstallation.getCurrentInstallation().saveInBackground();

        final ArrayList<String> friendArrayList = new ArrayList<>();
        final ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, friendArrayList);


         ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    Log.i("content", String.valueOf(objects.size()));
                    if (objects.size() > 0) {
                        for (ParseUser user : objects) {
                            Log.i("content", user.getUsername());
                            friendArrayList.add(user.getUsername());
                        }
                        friendlist.setAdapter(arrayAdapter);
                    }
                }
            }
        });
    }

}

