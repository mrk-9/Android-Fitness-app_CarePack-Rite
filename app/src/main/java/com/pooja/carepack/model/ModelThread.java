package com.pooja.carepack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yudiz on 09/02/16.
 */
public class ModelThread {

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
     * @return The updateddate
     */
    public String getUpdateddate() {
        return updateddate;
    }

    /**
     * @param updateddate The updateddate
     */
    public void setUpdateddate(String updateddate) {
        this.updateddate = updateddate;
    }

    /**
     * @return The fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * @param fullname The fullname
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * @return The profileImage
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     * @param profileImage The profile_image
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     * @return The images
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * @param images The images
     */
    public void setImages(List<Image> images) {
        this.images = images;
    }


    public class Image {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("image_150")
        @Expose
        private String image150;
        @SerializedName("image_512")
        @Expose
        private String image512;

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
         * @return The image150
         */
        public String getImage150() {
            return image150;
        }

        /**
         * @param image150 The image_150
         */
        public void setImage150(String image150) {
            this.image150 = image150;
        }

        /**
         * @return The image512
         */
        public String getImage512() {
            return image512;
        }

        /**
         * @param image512 The image_512
         */
        public void setImage512(String image512) {
            this.image512 = image512;
        }

    }


}
