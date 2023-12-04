package com.favorites.favorites;

public class Web {

    private Integer id;

    private Integer userId;

    private String url;

    private String webUsername;

    private String webPassword;

    private String eventReminder;

    @Override
    public String toString() {
        return "Web{" +
                "id=" + id +
                ", userId=" + userId +
                ", url='" + url + '\'' +
                ", webUsername='" + webUsername + '\'' +
                ", webPassword='" + webPassword + '\'' +
                ", eventReminder='" + eventReminder + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWebUsername() {
        return webUsername;
    }

    public void setWebUsername(String webUsername) {
        this.webUsername = webUsername;
    }

    public String getWebPassword() {
        return webPassword;
    }

    public void setWebPassword(String webPassword) {
        this.webPassword = webPassword;
    }

    public String getEventReminder() {
        return eventReminder;
    }

    public void setEventReminder(String eventReminder) {
        this.eventReminder = eventReminder;
    }
}
