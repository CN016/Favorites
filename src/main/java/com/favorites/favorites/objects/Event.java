package com.favorites.favorites.objects;


import java.util.Date;

public class Event {

    private Integer id;
    private Integer userId;
    private String url;
    private Long eventTime;
    private String event;
    private String remark;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", userId=" + userId +
                ", url='" + url + '\'' +
                ", eventTime=" + eventTime +
                ", event='" + event + '\'' +
                ", remark='" + remark + '\'' +
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

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
