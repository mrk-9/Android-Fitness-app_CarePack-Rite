package com.pooja.carepack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelConversion {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("thread")
    @Expose
    private ArrayList<Thread> thread = new ArrayList<Thread>();

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
     * @return The thread
     */
    public ArrayList<Thread> getThread() {
        return thread;
    }

    /**
     * @param thread The thread
     */
    public void setThread(ArrayList<Thread> thread) {
        this.thread = thread;
    }


    public class Thread {

        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("updateddate")
        @Expose
        private String updateddate;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("fullname")
        @Expose
        private String fullname;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The userid
         */
        public String getUserid() {
            return this.userid;
        }

        /**
         * @param userid The userid
         */
        public void setUserid(String userid) {
            this.userid = userid;
        }

        /**
         * @return The message
         */
        public String getMessage() {
            return this.message;
        }

        /**
         * @param message The message
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         * @return The updateddate
         */
        public String getUpdateddate() {
            return this.updateddate;
        }

        /**
         * @param updateddate The updateddate
         */
        public void setUpdateddate(String updateddate) {
            this.updateddate = updateddate;
        }

        /**
         * @return The profileImage
         */
        public String getProfileImage() {
            return this.profileImage;
        }

        /**
         * @param profileImage The profile_image
         */
        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        /**
         * @return The fullname
         */
        public String getFullname() {
            return this.fullname;
        }

        /**
         * @param fullname The fullname
         */
        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

    }

}