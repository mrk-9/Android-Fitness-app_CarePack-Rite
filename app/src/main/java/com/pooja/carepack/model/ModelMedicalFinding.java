package com.pooja.carepack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ModelMedicalFinding {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("medical_findings")
    @Expose
    private List<MedicalFinding> medicalFindings = new ArrayList<MedicalFinding>();

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
     * @return The medicalFindings
     */
    public List<MedicalFinding> getMedicalFindings() {
        return medicalFindings;
    }

    /**
     * @param medicalFindings The medical_findings
     */
    public void setMedicalFindings(List<MedicalFinding> medicalFindings) {
        this.medicalFindings = medicalFindings;
    }

    public class MedicalFinding {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("finding_date")
        @Expose
        private String findingDate;
        @SerializedName("email_status")
        @Expose
        private String emailStatus;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("image_512")
        @Expose
        private String image512;

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
         * The findingDate
         */
        public String getFindingDate() {
            return findingDate;
        }

        /**
         *
         * @param findingDate
         * The finding_date
         */
        public void setFindingDate(String findingDate) {
            this.findingDate = findingDate;
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

        /**
         *
         * @return
         * The image
         */
        public String getImage() {
            return image;
        }

        /**
         *
         * @param image
         * The image
         */
        public void setImage(String image) {
            this.image = image;
        }

        /**
         *
         * @return
         * The image512
         */
        public String getImage512() {
            return image512;
        }

        /**
         *
         * @param image512
         * The image_512
         */
        public void setImage512(String image512) {
            this.image512 = image512;
        }


    }

}

