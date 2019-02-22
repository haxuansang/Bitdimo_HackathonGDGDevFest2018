package com.appproteam.sangha.bitdimo.Presenter.Objects;

public class CommentsObject {
    String urlImageUserComment;
    String nameOfUserComment;
    String contentOfComment;

    public String getUrlImageUserComment() {
        return urlImageUserComment;
    }

    public void setUrlImageUserComment(String urlImageUserComment) {
        this.urlImageUserComment = urlImageUserComment;
    }

    public String getNameOfUserComment() {
        return nameOfUserComment;
    }

    public void setNameOfUserComment(String nameOfUserComment) {
        this.nameOfUserComment = nameOfUserComment;
    }

    public String getContentOfComment() {
        return contentOfComment;
    }

    public void setContentOfComment(String contentOfComment) {
        this.contentOfComment = contentOfComment;
    }

    public CommentsObject(String urlImageUserComment, String nameOfUserComment, String contentOfComment) {

        this.urlImageUserComment = urlImageUserComment;
        this.nameOfUserComment = nameOfUserComment;
        this.contentOfComment = contentOfComment;
    }
}
