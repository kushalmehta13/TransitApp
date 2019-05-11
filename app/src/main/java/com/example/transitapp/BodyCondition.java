package com.example.transitapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BodyCondition extends Fragment {
    private Button cameraTrigger;
    private static  final int CAMERA_REQUEST = 200;
    private static final int MY_CAMERA_PERMISSION_CODE = 200;
    static final int REQUEST_TAKE_PHOTO = 1;
    private LinearLayout root;
    private ViewGroup.LayoutParams params;
    private PostInspectionActivty parentActivity;
    private ArrayList<Uri> imagePaths;
    private int bus_number;
    String mCurrentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_body_condition, container, false);
        bus_number = getArguments().getInt("bus_number");
        parentActivity = (PostInspectionActivty) getActivity();
        imagePaths = new ArrayList<>();
        params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        ((LinearLayout.LayoutParams) params).setMargins(100, 50, 100, 0);
        root = view.findViewById(R.id.root);
        cameraTrigger = (Button) view.findViewById(R.id.cameraTrigger);

        cameraTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else {
                    dispatchTakePictureIntent();
                }
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_CAMERA_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getContext(), "Camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){

            final CardView c = new CardView(getContext());
            final Uri file = Uri.fromFile(new File(mCurrentPhotoPath));
            LinearLayout l = new LinearLayout(getContext());
            l.setOrientation(LinearLayout.VERTICAL);
            Button b = new Button(getContext());
            ImageView i = new ImageView(getContext());
            c.setLayoutParams(params);
            c.setRadius(9);
            c.setMaxCardElevation(2);
            c.setElevation(2);
            c.setContentPadding(16,16,16,16);
            b.setText("Delete");
            l.addView(i);
            l.addView(b);
            c.addView(l);
            imagePaths.add(file);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    root.removeView(c);
                    imagePaths.remove(file);
                }
            });
            Glide.with(this).load(mCurrentPhotoPath).apply(new RequestOptions().override(300, 500)).into(i);
            parentActivity.defects = imagePaths;
            root.addView(c,0);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = bus_number+"_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.transitapp.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}
