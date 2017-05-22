package com.project.yoavr.lookforplace;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MySearchService extends IntentService {
    public static String alisa="AIzaSyBbf6jU_14dCTNjzekXf2X6alCeQW-8AOg";
    //AIzaSyBbf6jU_14dCTNjzekXf2X6alCeQW-8AOg
    //AIzaSyASzIJP9N8-7Gw6g2QEJwwK-mEtXuxS-gQ
    SharedPreferences preferences,preferences1;
    double latitude,longitude;
    public MySearchService() {
        super("MySearchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String action = intent.getAction();
        String url = "";

        if (action.equals("globalsearch")) {
            String search = intent.getStringExtra("search");
            url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + search + "&key="+alisa;

        } else {

                latitude =MainActivity.lan;
                longitude = MainActivity.lng;

            String place= intent.getStringExtra("searchnear");//AIzaSyBwrlgsWDcO3C-1Zz9tSIQl1tpdbx96M6U //AIzaSyAW2XNPZ2DtU8yq-L-xsto4G3gGIgVCSEk
          //  https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&keyword=sushi&key=AIzaSyAV3E-XQ-Zdze08MwnxBG3gNejgXlNO4MY";
            url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=500&keyword="+place+"&key="+alisa;
        }

        OkHttpClient client = new OkHttpClient();
        // GET request
        Request request = new Request.Builder()
                .url(url)
                .build();
        String jsronRespo = "";
        try {
            client.newCall(request).execute();
            Response response = client.newCall(request).execute();
            jsronRespo = response.body().string();

        } catch (IOException interntEX) {
        }
        //after got result put them on gson and send broadcast to fragment
        Gson gson = new Gson();
        if (action.equals("globalsearch")){
            MySearch currentplace = gson.fromJson(jsronRespo, MySearch.class);
            Intent finishedDetailsIntent = new Intent("FINISHED_DETAILS");
            finishedDetailsIntent.putParcelableArrayListExtra("Places", currentplace.results);
            LocalBroadcastManager.getInstance(this).sendBroadcast(finishedDetailsIntent);

        }
        else
        {

            MySearch currentplace = gson.fromJson(jsronRespo, MySearch.class);
            Intent finishedDetailsIntent = new Intent("FINISHED_DETAILS");
            finishedDetailsIntent.putParcelableArrayListExtra("Places", currentplace.results);
            LocalBroadcastManager.getInstance(this).sendBroadcast(finishedDetailsIntent);

        }

    }
     //https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&CoQBdwAAAE8bhV-KeyRQXwryomMhAV_YiCQD_oxqAxCWCTzhZs30i34jWcEPHUGeBTIvJBU1wNRG9JUmRzhI8oAk04lb6LT3Y8yu3UF6vv-O0lU4mGBoVKtw79Kx6IV7m6aqnbS5uSHn6belAcZJU4ZNgD3Iv5UNWEGUf49U2aGj2kMB6yj2EhBPJ7S-aOeFXl8E9-sQ0cgUGhT9sRrlzx9kC8j5iVUVsTqh-F_2t=photoreference&key=AIzaSyAV3E-XQ-Zdze08MwnxBG3gNejgXlNO4MY
    /////https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyAV3E-XQ-Zdze08MwnxBG3gNejgXlNO4MY

}
