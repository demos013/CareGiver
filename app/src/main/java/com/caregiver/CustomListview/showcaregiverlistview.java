package com.caregiver.CustomListview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.caregiver.Model.Caregiver;
import com.caregiver.Model.Review;
import com.caregiver.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Demos on 1/17/2018.
 */

public class showcaregiverlistview extends BaseAdapter {

    Context mContext;
    ArrayList<Caregiver> AllCaregiverDB;

    public showcaregiverlistview(Context mContext, ArrayList<Caregiver> AllCaregiverDB) {
        this.mContext = mContext;
        this.AllCaregiverDB = AllCaregiverDB;
    }

    @Override
    public int getCount() {
        return AllCaregiverDB.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = mInflater.inflate(R.layout.listview_row_caregiver, viewGroup, false);

        ImageView img = view.findViewById(R.id.row_image_caregiver);
        downloadInLocalFile(img, AllCaregiverDB.get(i));
        TextView caregiver_name = view.findViewById(R.id.row_name_caregiver);
        caregiver_name.setText("คุณ "+AllCaregiverDB.get(i).getName()+" "+AllCaregiverDB.get(i).getLastname());
        TextView caregiver_job = view.findViewById(R.id.row_caregiver_job);
        caregiver_job.setText(AllCaregiverDB.get(i).getJob());
        TextView caregiver_cost = view.findViewById(R.id.row_caregiver_cost);
        caregiver_cost.setText("ค่าบริการ "+AllCaregiverDB.get(i).getCost()+" บาท");
        final TextView score = view.findViewById(R.id.row_caregiver_score);

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mReviewRef= mRootRef.child("Review");
        Query query = mReviewRef.orderByChild("caregiver_uid").equalTo(AllCaregiverDB.get(i).getUid());
        final View finalView = view;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float rating = 0;
                int size = 0;
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    rating = rating+postSnapshot.getValue(Review.class).getScore();
                    size +=1;
                }

                rating/=size;
                RatingBar ratingBar = finalView.findViewById(R.id.row_caregiver_rating_bar);
                ratingBar.setRating(rating);
                score.setText(String.valueOf(size)+" คำวิจารณ์");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return view;
    }

    private void downloadInLocalFile(final ImageView img, Caregiver caregiverDB) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("display_caregiver/"+caregiverDB.getUid()+".png");
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
