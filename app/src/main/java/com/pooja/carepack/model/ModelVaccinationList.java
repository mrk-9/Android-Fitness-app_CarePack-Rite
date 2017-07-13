package com.pooja.carepack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ModelVaccinationList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("vaccination_list")
    @Expose
    private List<VaccinationList> vaccinationList = new ArrayList<VaccinationList>();

    /**
     * @return The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The vaccinationList
     */
    public List<VaccinationList> getVaccinationList() {
        return vaccinationList;
    }

    /**
     * @param vaccinationList The vaccination_list
     */
    public void setVaccinationList(List<VaccinationList> vaccinationList) {
        this.vaccinationList = vaccinationList;
    }

    public class VaccinationList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;

        /**
         * @return The id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title The title
         */
        public void setTitle(String title) {
            this.title = title;
        }

    }


}