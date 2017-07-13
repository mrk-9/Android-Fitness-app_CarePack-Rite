package com.pooja.carepack.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelLogin {


    public Integer status;
    public General general;
    public String message;

    public class General {
        public String id;
        public String firstname;
        public String lastname;
        public String gender;
        public String email;
        public String profile_image;
        public String birthdate;
        public String bloodgroup;
        public String street1;
        public String street2;
        public String countryid;
        public String city;
        public String postalcode;
        public String homephone;
        public String mobile;
        public String language;
        public String device_lat;
        public String device_lng;
        public String fbid;
        public String country;
    }
}
