package com.abhigyan.user.instagramcloneacadview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by USER on 16-07-2018.
 */

public class tab2feed extends Fragment {

    FloatingActionButton camFab, galFab;
    int flag = 0;

    LinearLayout flowLayout;

    public void getPhotos() {
        if (flag == 1) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 886);
        } else {
            Toast.makeText(getContext(), "Sorry! Permission to use camera denied.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 112) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                flag = 1;
            }
        } else {
            Toast.makeText(getContext(), "Sorry! Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap NUFI;
        if (requestCode == 886 && data != null && resultCode == RESULT_OK) {
            NUFI = (Bitmap) data.getExtras().get("data");
            layoutView(NUFI);
        } else if (requestCode == 625 && data != null && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                NUFI = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                layoutView(NUFI);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2feed, container, false);
        camFab = rootView.findViewById(R.id.camFab);
        galFab = rootView.findViewById(R.id.galFab);
        flowLayout = rootView.findViewById(R.id.flowLayout);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Parse.initialize(getContext());
        ParseInstallation.getCurrentInstallation().saveInBackground();

        getLayoutView();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 112);
        } else {
            flag = 1;
        }
        camFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPhotos();

            }
        });
        galFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 625);
            }
        });

    }

    public void layoutView(Bitmap img) {
        CardView cardView = new CardView(getContext());
        cardView.setLayoutParams(new ViewGroup.LayoutParams(1027, 700));

        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new ViewGroup.LayoutParams(1027, 15));

        LinearLayout cardLayout = new LinearLayout(getContext());
        cardLayout.setLayoutParams(new ViewGroup.LayoutParams(1027, 697));
        ImageView nativeUserFeedImage = new ImageView(getContext());
        nativeUserFeedImage.setLayoutParams(new ViewGroup.LayoutParams(1027, 695));
        RoundedBitmapDrawable RDB = RoundedBitmapDrawableFactory.create(getResources(), img);
        RDB.setCornerRadius(5);
        RDB.setAntiAlias(true);
        nativeUserFeedImage.setImageDrawable(RDB);

        flowLayout.addView(cardView);
        flowLayout.addView(tv);
        cardView.addView(cardLayout);
        cardLayout.addView(nativeUserFeedImage);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();//helps in transferring bitmap to parse server as objects
        img.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] byteArray = stream.toByteArray();//store in byte array after converting to byte

        ParseFile file = new ParseFile("imageFile.png", byteArray);

        ParseObject parseObject = new ParseObject("Image");

        parseObject.put("image", file);

        parseObject.put("username", ParseUser.getCurrentUser().getUsername());

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    Toast.makeText(getContext(), "Photo shared!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Something went wrong. Try again later!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void getLayoutView() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            ParseFile file1 = (ParseFile) object.get("image");
                            file1.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {//return byte array

                                    if (e == null && data != null) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                        CardView cardView = new CardView(getContext());
                                        cardView.setLayoutParams(new ViewGroup.LayoutParams(1027, 700));

                                        TextView tv = new TextView(getContext());
                                        tv.setLayoutParams(new ViewGroup.LayoutParams(1027, 15));

                                        LinearLayout cardLayout = new LinearLayout(getContext());
                                        cardLayout.setLayoutParams(new ViewGroup.LayoutParams(1027, 697));
                                        ImageView nativeUserFeedImage = new ImageView(getContext());
                                        nativeUserFeedImage.setLayoutParams(new ViewGroup.LayoutParams(1027, 695));
                                        RoundedBitmapDrawable RDB = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                                        RDB.setCornerRadius(5);
                                        RDB.setAntiAlias(true);
                                        nativeUserFeedImage.setImageDrawable(RDB);

                                        flowLayout.addView(cardView);
                                        flowLayout.addView(tv);
                                        cardView.addView(cardLayout);
                                        cardLayout.addView(nativeUserFeedImage);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}