package com.pooja.carepack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ModelThreadList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("thread")
    @Expose
    private ArrayList<ModelThread> thread = new ArrayList<ModelThread>();

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
    public ArrayList<ModelThread> getThread() {

        return thread;
    }

    /**
     * @param thread The thread
     */
    public void setThread(ArrayList<ModelThread> thread) {
        this.thread = thread;
    }

    public class Thread {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("updateddate")
        @Expose
        private String updateddate;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("images")
        @Expose
        private List<Image> images = new ArrayList<Image>();

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
         * The message
         */
        public String getMessage() {
            return message;
        }

        /**
         *
         * @param message
         * The message
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         *
         * @return
         * The updateddate
         */
        public String getUpdateddate() {
            return updateddate;
        }

        /**
         *
         * @param updateddate
         * The updateddate
         */
        public void setUpdateddate(String updateddate) {
            this.updateddate = updateddate;
        }

        /**
         *
         * @return
         * The fullname
         */
        public String getFullname() {
            return fullname;
        }

        /**
         *
         * @param fullname
         * The fullname
         */
        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        /**
         *
         * @return
         * The profileImage
         */
        public String getProfileImage() {
            return profileImage;
        }

        /**
         *
         * @param profileImage
         * The profile_image
         */
        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        /**
         *
         * @return
         * The images
         */
        public List<Image> getImages() {
            return images;
        }

        /**
         *
         * @param images
         * The images
         */
        public void setImages(List<Image> images) {
            this.images = images;
        }

    }

    public class Image {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("filename")
        @Expose
        private String filename;

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
         * The filename
         */
        public String getFilename() {
            return filename;
        }

        /**
         *
         * @param filename
         * The filename
         */
        public void setFilename(String filename) {
            this.filename = filename;
        }

    }

}
