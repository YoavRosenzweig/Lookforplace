package com.project.yoavr.lookforplace;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by yoavr on 12/04/2017.
 */

class MyplaceAdapter extends RecyclerView.Adapter <MyplaceAdapter.SinglePlaceVH> {
    ArrayList<MyPlace> allPlaces;
    Context c;
    FragmnetChanger fragmnetChanger;
    String stringphoto;
    String kilometer;
    String image;
    String uri;
    MyPlace currentPlace2;
    ContentValues cv2;
    int counter=0;
    @Override
    public SinglePlaceVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View singeLview= LayoutInflater.from(c).inflate(R.layout.singleplace, null);
        SinglePlaceVH singlePlaceVH= new SinglePlaceVH(singeLview);

        return singlePlaceVH;
    }

    @Override
    public void onBindViewHolder(SinglePlaceVH singlePlaceVH, int position) {
        MyPlace currentplace=allPlaces.get(position);
        singlePlaceVH.bindTheMovieData(currentplace);
    }

    public MyplaceAdapter(ArrayList<MyPlace> allPlaces, Context c,FragmnetChanger fragmnetChanger) {
        this.allPlaces = allPlaces;
        this.c = c;
        this.fragmnetChanger = fragmnetChanger;
    }
    @Override
    public int getItemCount() {

        return allPlaces.size();
    }

    class SinglePlaceVH extends RecyclerView.ViewHolder
    {
        ImageView placeIV;
        TextView nameTV;
        TextView addersTV;
        TextView distanceTV;
        float distancefromme;

        public SinglePlaceVH(View itemView) {
            super(itemView);
            placeIV= (ImageView) itemView.findViewById(R.id.imageView);
            nameTV= (TextView) itemView.findViewById(R.id.nameTV);
            addersTV= (TextView) itemView.findViewById(R.id.addresTV);
            distanceTV= (TextView) itemView.findViewById(R.id.distanceTV);
        }

        public  void bindTheMovieData(final MyPlace currentPlace)
        {
            // got the result and Showing them
            if(currentPlace.photos!=null) {
                image = currentPlace.photos[0].photo_reference;
                uri = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&photoreference=" + image + "&key=" + MySearchService.alisa;
                Picasso.with(c).load(uri).into(placeIV, new Callback() {
                    @Override
                    public void onSuccess() {
                       //save from bitmap after search to String and send it to table resukt
                        Bitmap bitmap=((BitmapDrawable) placeIV.getDrawable()).getBitmap();
                        if(currentPlace.photos[0]!=null) {
                            currentPlace.photos[0].photo_reference = MainActivity.BitmapToBase64(bitmap);
                            stringphoto=currentPlace.photos[0].photo_reference;
                            MySqlResults helper2 = new MySqlResults(c);
                            //make a new table for search result
                            //save only until 10 places
                            if(counter<10) {
                                cv2 = new ContentValues();
                                cv2.put("image",stringphoto);
                                cv2.put("name", currentPlace.name);
                                cv2.put("distance", distancefromme);
                                if (currentPlace.formatted_address == null) {
                                    cv2.put("adders", currentPlace.vicinity);
                                } else {
                                    cv2.put("adders", currentPlace.formatted_address);
                                }

                                helper2.getWritableDatabase().insert("myresult", null, cv2);

                                counter++;
                            }
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
            }

            else
            {
                Picasso.with(c).load(currentPlace.icon).into(placeIV);
            }
            if(!(currentPlace.formatted_address==null)) {
                addersTV.setText(currentPlace.formatted_address);
            }
            else
            {
                addersTV.setText(currentPlace.vicinity);
            }
            nameTV.setText(currentPlace.name);
            distancefromme= (float) haversine(MainActivity.lan,MainActivity.lng,currentPlace.geometry.location.lat,currentPlace.geometry.location.lng);
           if(MainActivity.KM==true)
           {
                kilometer="KM";
           }
           else
           {
               kilometer="MIELS";
           }
            distanceTV.setText(""+distancefromme+" "+kilometer);
            nameTV.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     //open a map witj curecct location
                         double lat = currentPlace.geometry.location.lat;
                         double lng = currentPlace.geometry.location.lng;
                         fragmnetChanger.changeFragments(lat, lng);
                 }
                });
            nameTV.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //open dialog for choose save or share
                    android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(c, v);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.AddFavoritePPItem:
                                    //make a table for favorie
                                    MysqlHelper helper = new MysqlHelper(c);
                                    ContentValues cv1 = new ContentValues();
                                    cv1.put("name", currentPlace.name);
                                    helper.getWritableDatabase().insert("myplace", null, cv1);
                                    Toast.makeText(c, "Save on favorite", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.SharedPlacePPItem:
                                    //share the current location on googleMaps
                                    currentPlace2=allPlaces.get(getAdapterPosition());
                                    String location="https://www.google.co.il/maps/@"+currentPlace2.geometry.location.lat+","+currentPlace2.geometry.location.lng+",18.79z?hl=en";
                                    Intent sharingIntent=new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "place Details");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,location );
                                    sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    c.startActivity(sharingIntent);
                                    break;
                            }
                            return true;
                        }
                    });

                    return true;
                }
            });
        }
    }

    //Calculates a distance between 2 points
    //only 2 digits after the point
    public static double haversine(double lat1, double lng1, double lat2, double lng2) {
        int r = 6371; // average radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c;
        DecimalFormat df = new DecimalFormat("#.##");
        String dx = df.format(d);
        d = Double.valueOf(dx);
        double dm = (d / 1.6);
        DecimalFormat df1 = new DecimalFormat("#.##");
        String dx1 = df1.format(dm);
        dm = Double.valueOf(dx1);
        if (MainActivity.KM) {

            return d;
        } else {
            return dm;
        }
    }


    }

