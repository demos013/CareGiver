package com.caregiver.CustomListview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caregiver.Model.Care_Activity;
import com.caregiver.Model.Caregiver;
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
import com.google.zxing.WriterException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.ContentValues.TAG;

/**
 * Created by Demos on 1/23/2018.
 *
 */

public class showbookingcaregiverlistview extends BaseAdapter {

    Context mContext;
    ArrayList<Care_Activity> activity;
    private Bitmap bitmap;

    public showbookingcaregiverlistview(Context mContext, ArrayList<Care_Activity> activity) {
        this.mContext = mContext;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return activity.size();
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
            view = mInflater.inflate(R.layout.listview_row_booking_caregiver, viewGroup, false);

        ImageView img = view.findViewById(R.id.row_booking_image_caregiver);
        downloadInLocalFile(img, activity.get(i));
        final TextView caregiver_name = view.findViewById(R.id.row_booking_name);
        TextView start_date = view.findViewById(R.id.row_booking_start_date);
        TextView start_time = view.findViewById(R.id.row_booking_start_time);
        ImageView qrCode = view.findViewById(R.id.row_booking_qrcode);
        start_date.setText(activity.get(i).getStart_date());
        start_time.setText(activity.get(i).getStart_time());


        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mCaregiverRef = mRootRef.child("Caregiver/"+activity.get(i).getCaregiver_uid());
        mCaregiverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Caregiver caregiverDB  = dataSnapshot.getValue(Caregiver.class);
                caregiver_name.setText(caregiverDB.getName()+" "+caregiverDB.getLastname());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        QRGEncoder qrgEncoder = new QRGEncoder(activity.get(i).getStart_key(), null, QRGContents.Type.TEXT,400 );
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogConfirmActivity();
            }
        });

        return view;

    }

    private void downloadInLocalFile(final ImageView img, Care_Activity activity) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("display_caregiver/"+activity.getCaregiver_uid()+".png");
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
    public void showDialogConfirmActivity(){

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_qrcode);

        ImageView qrimage = dialog.findViewById(R.id.bigqrcode);
        qrimage.setImageBitmap(bitmap);

        dialog.show();

    }
}

