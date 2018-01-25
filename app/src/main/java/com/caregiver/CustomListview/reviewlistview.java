package com.caregiver.CustomListview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caregiver.Model.Elder;
import com.caregiver.Model.Review;
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
 * Created by Demos on 1/25/2018.
 */

public class reviewlistview extends BaseAdapter {

    Elder elderDB;
    Context mContext;
    ArrayList<Review> review;

    public reviewlistview(Context mContext, ArrayList<Review> review) {
        this.mContext = mContext;
        this.review = review;
        elderDB = new Elder();
    }

    @Override
    public int getCount() {
        return review.size();
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
            view = mInflater.inflate(R.layout.listview_row_review, viewGroup, false);
        TextView review_detail = view.findViewById(R.id.row_review_detail);
        final TextView review_name = view.findViewById(R.id.row_review_name);
        final ImageView review_display = view.findViewById(R.id.row_review_display);
        Log.d(review.get(i).getReview_detail(), "getView: ");
        review_detail.setText(review.get(i).getReview_detail());

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mElderRef = mRootRef.child("Elder").child(review.get(i).getElder_uid());
        mElderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                review_name.setText(dataSnapshot.getValue(Elder.class).getName()+" "+dataSnapshot.getValue(Elder.class).getLastname());

                downloadInLocalFile(review_display,dataSnapshot.getValue(Elder.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        TextView review_date = view.findViewById(R.id.row_review_date);
        review_date.setText(review.get(i).getDate_of_view());




        return view;
    }

    public static Bitmap GetBitmapClippedCircle(Bitmap bitmap) {

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle(
                (float)(width / 2)
                , (float)(height / 2)
                , (float) Math.min(width, (height / 2))
                , Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
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
                img.setImageBitmap( GetBitmapClippedCircle(bmpPic));
                file.delete();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }
}
