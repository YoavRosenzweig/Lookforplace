package com.project.yoavr.lookforplace;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yoavr on 28/04/2017.
 */

class MyResultAdapter  extends RecyclerView.Adapter<MyResultAdapter.SinglePlaceVH>{
    
    ArrayList<MyPlace> allResult;
    Context c;
    @Override
    public SinglePlaceVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View singeLview= LayoutInflater.from(c).inflate(R.layout.singleplace, null);
        MyResultAdapter.SinglePlaceVH singlePlaceVH= new MyResultAdapter.SinglePlaceVH(singeLview);
        return singlePlaceVH;
    }

    @Override
    public void onBindViewHolder(SinglePlaceVH singlePlaceVH, int position) {
        MyPlace currentplace=allResult.get(position);
        singlePlaceVH.bindTheMovieData(currentplace);
    }


    public MyResultAdapter(ArrayList<MyPlace> allResult, Context c) {
        this.allResult = allResult;
        this.c = c;
    }
    @Override
    public int getItemCount() {
        return allResult.size();
    }

    public class SinglePlaceVH extends  RecyclerView.ViewHolder{

        ImageView placeIV;
        TextView nameTV;
        TextView addersTV;
        TextView distanceTV;

        public SinglePlaceVH(View singeLview) {
            super(singeLview);
            placeIV= (ImageView) singeLview.findViewById(R.id.imageView);
            nameTV= (TextView) singeLview.findViewById(R.id.nameTV);
            addersTV= (TextView) singeLview.findViewById(R.id.addresTV);
            distanceTV= (TextView) singeLview.findViewById(R.id.distanceTV);
        }

        public void bindTheMovieData(MyPlace currentplace)
        {
            nameTV.setText(currentplace.name);
            addersTV.setText(currentplace.addres);
            distanceTV.setText(currentplace.distane);
           if(currentplace.icon!=null)
           {
               Bitmap Photodb=MainActivity.StringTOBItmap(currentplace.icon);
               placeIV.setImageBitmap(Photodb);
           }

        }
    }
}
