package com.pooja.carepack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ModelUserVaccinationList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_vaccination")
    @Expose
    private List<UserVaccination> userVaccination = new ArrayList<UserVaccination>();

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
     * @return The userVaccination
     */
    public List<UserVaccination> getUserVaccination() {
        return userVaccination;
    }

    /**
     * @param userVaccination The user_vaccination
     */
    public void setUserVaccination(List<UserVaccination> userVaccination) {
        this.userVaccination = userVaccination;
    }


    public class UserVaccination {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("reminder")
        @Expose
        private String reminder;
        @SerializedName("email_status")
        @Expose
        private String emailStatus;

        /**
         *
         * @return
         * The id
         */
        public String getId() {
            return id;
        }

        /**
         *
         * @param id
         * The id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         *
         * @return
         * The title
         */
        public String getTitle() {
            return title;
        }

        /**
         *
         * @param title
         * The title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         *
         * @return
         * The reminder
         */
        public String getReminder() {
            return reminder;
        }

        /**
         *
         * @param reminder
         * The reminder
         */
        public void setReminder(String reminder) {
            this.reminder = reminder;
        }

        /**
         *
         * @return
         * The emailStatus
         */
        public String getEmailStatus() {
            return emailStatus;
        }

        /**
         *
         * @param emailStatus
         * The email_status
         */
        public void setEmailStatus(String emailStatus) {
            this.emailStatus = emailStatus;
        }

    }

}