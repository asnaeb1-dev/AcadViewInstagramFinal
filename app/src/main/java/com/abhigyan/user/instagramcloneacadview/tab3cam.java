package com.abhigyan.user.instagramcloneacadview;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by USER on 16-07-2018.
 */

public class tab3cam extends Fragment{

    ImageView profilePicture;
    Button ppButton, upButton;
    int flag=0;
    Bitmap profilePicBitmap;
    SharedPreferences sharedPreferences;

    int picTaken =0;//if 0 then pic has not been taken

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==987)
        {
            if(grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED)
            {
                flag=1;
            }
            else
            {
                Toast.makeText(getContext(), "Permissions denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==234 && resultCode==RESULT_OK && data!=null)
        {
            profilePicBitmap = (Bitmap) data.getExtras().get("data");
            profilePicture.setImageBitmap(profilePicBitmap);

            parseUSER(profilePicBitmap,"Photo shared!", "Sorry! Something is wrong");
        }
        else
            if(requestCode == 675 && resultCode == RESULT_OK &&data != null)
            {
                Uri selectedImage = data.getData();
                try
                {
                    profilePicBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    profilePicture.setImageBitmap(profilePicBitmap);
                    parseUSER(profilePicBitmap,"Photo shared!", "Sorry! Something is wrong");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
        picTaken=1;//pictaken 1 means a picture has been taken
        //save the value of pictaken
        SharedPreferences.Editor editor = getContext().getSharedPreferences("checkIfPicTaken", MODE_PRIVATE).edit();
        editor.putInt("picTaken", 1);
        editor.apply();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3camera, container, false);
        profilePicture =  rootView.findViewById(R.id.profilePicture);

        ppButton = rootView.findViewById(R.id.ppButton);
        upButton = rootView.findViewById(R.id.upButton);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        permission();
        sharedPreferences = getContext().getSharedPreferences("checkIfPicTaken", MODE_PRIVATE);
        int check = sharedPreferences.getInt("picTaken",0);
        if(check!=1) {
            profilePicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
            profilePicBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.appback);
            parseUSER(profilePicBitmap, "Default profile pic set!", "Failed to upload pic!");
        }

        ppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 234);
            }
        });
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag==1)
                {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent,675);
                }
            }
        });

    }

    public void permission()
    {
        if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},987);
        }
         else
        {
            flag=1;
        }
    }

    public void parseUSER(Bitmap propic, final String successMsg, final String failureMsg)
    {
        ByteArrayOutputStream stream= new ByteArrayOutputStream();//helps in transferring bitmap to parse server as objects
        propic.compress(Bitmap.CompressFormat.PNG,100,stream);

        byte[] byteArray=stream.toByteArray();//store in byte array after converting to byte

        ParseFile file= new ParseFile("imageFile.png",byteArray);

        ParseUser user = ParseUser.getCurrentUser();

        user.put("image", file);

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null)
                {
                    Toast.makeText(getContext(), successMsg, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), failureMsg, Toast.LENGTH_SHORT).show();
                    Log.i("error",e.getMessage());
                }
            }
        });
    }
}




