package com.example.dhruvmj.samplerssfeedreader.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Dhruv Jariwala
 */

public class RssFeedObject implements Parcelable {
    private String title;
    private String publicationDate;
    private String author;
    private String description;
    private String imageUrl;
    private String link;

    public RssFeedObject() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String obtainImageUrl(String descriptionTag) {
        String[] splitString = descriptionTag.split(" ");
        for (String s : splitString) {
            if (s.startsWith("src")) {
                return s.substring(5, s.length() - 1);
            }
        }
        if (descriptionTag.startsWith("http")) {
            return descriptionTag;
        } else {
            return "";
        }
    }

    public String getImageUrl() {
        return imageUrl = obtainImageUrl(description);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(publicationDate);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeString(link);
    }

    private RssFeedObject(Parcel in) {
        title = in.readString();
        publicationDate = in.readString();
        author = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        link = in.readString();
    }

    public static final Creator<RssFeedObject> CREATOR = new Creator<RssFeedObject>() {
        @Override
        public RssFeedObject createFromParcel(Parcel in) {
            return new RssFeedObject(in);
        }

        @Override
        public RssFeedObject[] newArray(int size) {
            return new RssFeedObject[size];
        }
    };
}
