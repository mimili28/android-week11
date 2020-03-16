package com.mariailieva.mapdemo.repo;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mariailieva.mapdemo.MapsActivity;
import com.mariailieva.mapdemo.model.MyLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class FireBaseRepo {

    public static List<MyLocation> locations = new ArrayList<>();
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static MapsActivity mapsActivity;
    private static String path = "mylocations";

    public static void setMapsActivity(MapsActivity activity){
        mapsActivity = activity;
        startListener();
    }


    public static void addMarker(String lat, String lon){
        DocumentReference ref = db.collection(path).document();
        Map<String, String> map = new HashMap<>();
        map.put("lat", lat);
        map.put("lon", lon);
        ref.set(map);
    }

    private static void startListener(){
        db.collection(path).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot data, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.d(TAG,"Error:"+e.getMessage());
                }
                else {
                    locations.clear();
                    for (DocumentSnapshot snap : data.getDocuments()) {
                        Log.i("all", " snapshot " + snap.getId());
                        MyLocation location = new MyLocation(snap.get("lat").toString(), snap.get("lon").toString());
                        locations.add(location);
                    }
                    mapsActivity.updateMarkers();
                }
            }

        });

    }


}
