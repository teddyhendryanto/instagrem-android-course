package com.teddyhendryanto.instagrem;

/**
 * Created by teddyhendryanto on 2017-09-13.
 */

class Feed {
    private String username;
    private String thumbnail;

    public Feed(String username, String thumbnail) {
        this.username = username;
        this.thumbnail = thumbnail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
