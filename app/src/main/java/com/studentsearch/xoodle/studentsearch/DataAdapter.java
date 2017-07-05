package com.studentsearch.xoodle.studentsearch;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.studentsearch.xoodle.studentsearch.utils.MappingUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

  private ArrayList<StudentData> mStudentData;
  private Context context;
  private String packageName;
  private Comparator<StudentData> comparator = new Comparator<StudentData>() {
    @Override
    public int compare(StudentData s1, StudentData s2) {
      MappingUtils mappingUtils = new MappingUtils();
      Map<String, String> yearMap = mappingUtils.getYearMap();
      if (yearMap.get(s1.getYear()) == null && yearMap.get(s2.getYear()) == null) {
        return 0;
      } else if (yearMap.get(s1.getYear()) == null && yearMap.get(s2.getYear()) != null) {
        return 1;
      } else if(yearMap.get(s1.getYear()) != null && yearMap.get(s2.getYear()) == null) {
        return -1;
      } else {
        return (yearMap.get(s2.getYear()))
                .compareTo(yearMap.get(s1.getYear()));
      }
    }
  };

  public DataAdapter(Context context, ArrayList<StudentData> studentData) {
    this.context = context;
    this.packageName = context.getPackageName();
    Collections.sort(studentData, comparator);
    this.mStudentData = studentData;
    Collections.sort(studentData, comparator);
  }

  @Override
  public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_entries, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.bind(context, packageName, mStudentData.get(position));
  }

  @Override
  public int getItemCount() {
    return mStudentData.size();
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mNameView, mRollView, mDeptView, mHallView, mAddressView, mUserBloodView;
    private ImageView mImageView;

    public ViewHolder(View v) {
      super(v);
      mNameView = (TextView) v.findViewById(R.id.tv_name);
      mRollView = (TextView) v.findViewById(R.id.tv_roll);
      mDeptView = (TextView) v.findViewById(R.id.tv_dept);
      mHallView = (TextView) v.findViewById(R.id.tv_hall);
//      mAddressView = (TextView) v.findViewById(R.id.tv_address);
//      mUserBloodView = (TextView) v.findViewById(R.id.tv_user_blood);
      mImageView = (ImageView) v.findViewById(R.id.user_image);
    }

    @Override
    public void onClick(View v) {
      //clicked
    }

    public void bind(Context context, String packageName, StudentData studentData) {
      mNameView.setText(studentData.getName());

      mRollView.setText("Roll Number: "+studentData.getRollNo());
      mDeptView.setText("Dept: "+studentData.getDept()+" - "+studentData.getProgramme());
      mHallView.setText("IITK Address:"+studentData.getRoomNo()+ ", "+studentData.getHall());

//      mAddressView.setText("Home Address:"+studentData.getAddress());
//      mUserBloodView.setText("Email: "+studentData.getUserName()+"@iitk.ac.in"+"\n"+"Blood Group: "+studentData.getBloodGroup());

      int errID;
      Resources res = context.getResources();
      if (studentData.getGender().equals("M")) {
        errID = res.getIdentifier("boy", "drawable", packageName);
      } else {
        errID = res.getIdentifier("girl", "drawable", packageName);
      }

      File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "studentPics");
      File image = new File(directory, studentData.getRollNo() + "_0");
//      File image = new File(directory, "160757_0.jpeg");
      if (image.exists()) {
        Log.i("ad", studentData.getRollNo());
        Picasso.with(context)
                .load(image)
                .placeholder(errID)
                .error(errID)
                .into(this.mImageView);
      } else {
        Picasso.with(context)
                .load("http://oa.cc.iitk.ac.in/Oa/Jsp/Photo/" + studentData.getRollNo() + "_0.jpg")
                .placeholder(errID)
                .error(errID)
                .into(this.mImageView);
      }

    }
  }
}