package com.example.startup.mapodp;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Startup on 3/29/17.
 */

public class Searcher {

    public Searcher()
    {

    }


    public static int byName(ArrayList<ODP> odp, String name)
    {
        int index=0;
        for(int i=0;i<odp.size();i++)
        {
            if(odp.get(i).namaODP.contentEquals(name))
            {
                index=i;
            }
        }
        return index;
    }

    public static boolean isExist(ArrayList<ODP> odp, String name)
    {
        boolean exist=false;
        for(int i=0;i<odp.size();i++)
        {
            if(odp.get(i).namaODP.contentEquals(name))
            {
                exist=true;
            }
        }
        return exist;
    }

    public static int getNearest(ArrayList<ODP> odp, LatLng p)
    {

        Double[] distance = new Double[odp.size()];
        for(int i=0;i<odp.size();i++)
        {
            distance[i]=Searcher.getEuclideanDistance(p,new LatLng(odp.get(i).latitude,odp.get(i).longitude));
        }
        Double min = distance[0]; int index = 0;
        for (int i = 1; i < distance.length; i++) {
            if ( distance[i] < min ) {
                min = distance[i];
                index = i;
            }
        }
        return index;
    }

    public static double getEuclideanDistance(LatLng p1,LatLng p2)
    {
        return Math.sqrt(Math.pow((p2.latitude - p1.latitude), 2) + Math.pow((p2.longitude - p1.longitude), 2));
    }
}
