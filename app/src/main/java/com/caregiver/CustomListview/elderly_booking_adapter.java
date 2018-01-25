package com.caregiver.CustomListview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caregiver.Model.Care_Activity;
import com.caregiver.Model.Elder;
import com.caregiver.Model.Request_Care_Activity;
import com.caregiver.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Demos on 1/24/2018.
 */

public class elderly_booking_adapter extends BaseAdapter {

    Context mContext;
    ArrayList<Care_Activity> AllActivityDB;
    Elder elderDB;

    public elderly_booking_adapter(Context mContext, ArrayList<Care_Activity> allActivityDB) {
        this.mContext = mContext;
        AllActivityDB = allActivityDB;
    }

    @Override
    public int getCount() {
        return AllActivityDB.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = mInflater.inflate(R.layout.listview_row_elderly, viewGroup, false);

        final ImageView img = view.findViewById(R.id.row_image_elderly);
        final TextView caregiver_name = view.findViewById(R.id.row_name_elderly);
        final TextView caregiver_date = view.findViewById(R.id.row_date_elderly);
        final TextView caregiver_time = view.findViewById(R.id.row_time_elderly);

        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mElder = mRootRef.child("Elder");
        mElder.child(AllActivityDB.get(i).getElder_uid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                elderDB = dataSnapshot.getValue(Elder.class);
                downloadInLocalFile(img, elderDB);
                caregiver_name.setText(elderDB.getName()+" "+elderDB.getLastname());
                caregiver_date.setText(AllActivityDB.get(i).getStart_date());
                caregiver_time.setText(AllActivityDB.get(i).getStart_time());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void downloadInLocalFile(final ImageView img, Elder elderDB) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("display_elder/"+elderDB.getUid()+".png");
        File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
        final File file = new File(dir, UUID.randomUUID().toString() + ".png");
        try {
            if (!dir.exists()) {
                dir.mkdir();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final FileDownloadTask fileDownloadTask = imageRef.getFile(file);
        fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bmpPic = BitmapFactory.decodeFile(file.getPath());
                bmpPic = Bitmap.createScaledBitmap(bmpPic, 200, 150, true);
                img.setImageBitmap(bmpPic);
                file.delete();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }
}
