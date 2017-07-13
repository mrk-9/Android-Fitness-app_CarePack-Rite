package com.pooja.carepack.model;

import com.google.gson.JsonObject;

import java.util.List;

public class ModelTreatmentAppointment {
    public Integer status;
    public String message;


    public TreatmentPlans treatment_plans;

    public class TreatmentPlans {
        public String id;
        public String userid;
        public String title;
        public String notes;
        public String location;
        public String starts;
        public String ends;
        public String repeats;
        public String startdate;
        public String enddate;
        public List<String> invitees;
        public String reminder;
        public String isactive;
        public String updateddate;
        public String reminder_title;
    }
}
