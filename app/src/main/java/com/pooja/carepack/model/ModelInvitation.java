package com.pooja.carepack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelInvitation {

    @SerializedName("users")
    @Expose
    private ArrayList<User> users = new ArrayList<User>();
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;

    /**
     * @return The users
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * @param users The users
     */
    public void setUsers(ArrayList<User> users) {
        this.users = users;
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


    //User

    public class User {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("status")
        @Expose
        private String status;

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
         * The email
         */
        public String getEmail() {
            return email;
        }

        /**
         *
         * @param email
         * The email
         */
        public void setEmail(String email) {
            this.email = email;
        }

        /**
         *
         * @return
         * The status
         */
        public String getStatus() {
            return status;
        }

        /**
         *
         * @param status
         * The status
         */
        public void setStatus(String status) {
            this.status = status;
        }

    }

}