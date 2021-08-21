package com.examplmakecodeeasy.questionprep;

public class ImageMode {
    private String imageid,image;

    public ImageMode(String imageid, String image) {
        this.imageid = imageid;
        this.image = image;
    }

    public ImageMode(){

    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
