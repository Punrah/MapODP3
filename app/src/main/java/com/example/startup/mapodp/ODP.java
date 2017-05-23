package com.example.startup.mapodp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Startup on 3/29/17.
 */

public class ODP implements Parcelable {

    static final int AVAILABLE=1;
    static final int FULL=2;

    String idOdp;
    String namaODP;
    String clusterCode;
    String neqptReasue;
    String tenossFracid;
    String isiskaName;
    String keterangan;
    String alamat;
    Double latitude;
    Double longitude;
    String kapasitas;
    String idAdmin;

    public ODP()
    {
         idOdp="";
         namaODP="";
         clusterCode="";
         neqptReasue="";
         tenossFracid="";
         isiskaName="";
         keterangan="";
         alamat="";
         latitude=0.0;
         longitude=0.0;
         kapasitas="";
         idAdmin="";
    }

    public ODP(JSONObject json) throws JSONException {

        idOdp=json.getString("id_odp");
        namaODP=json.getString("nama_odp");
        clusterCode=json.getString("cluster_code");
        neqptReasue=json.getString("neqpt_reasue");
        tenossFracid=json.getString("tenoss_fracid");
        isiskaName=json.getString("isiska_name");
        keterangan=json.getString("keterangan");
        alamat=json.getString("alamat");
        latitude=json.getDouble("lat");
        longitude=json.getDouble("long");
        kapasitas=json.getString("kapasitas");
        idAdmin=json.getString("id_admin");
    }

    public static final Parcelable.Creator<ODP> CREATOR = new Parcelable.Creator<ODP>() {
        public ODP createFromParcel(Parcel source) {
            ODP odp = new ODP();
            odp.idOdp = source.readString();
            odp.namaODP = source.readString();
            odp.clusterCode = source.readString();
            odp.neqptReasue=source.readString();
            odp.tenossFracid=source.readString();
            odp.isiskaName=source.readString();
            odp.keterangan=source.readString();
            odp.alamat=source.readString();
            odp.latitude=source.readDouble();
            odp.longitude=source.readDouble();
            odp.kapasitas=source.readString();
            odp.idAdmin=source.readString();

            return odp;
        }
        public ODP[] newArray(int size) {
            return new ODP[size];
        }
    };

    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(idOdp);
        parcel.writeString(namaODP);
        parcel.writeString(clusterCode);
        parcel.writeString(neqptReasue);
        parcel.writeString(tenossFracid);
        parcel.writeString(isiskaName);
        parcel.writeString(keterangan);
        parcel.writeString(alamat);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(kapasitas);
        parcel.writeString(idAdmin);
    }
}

