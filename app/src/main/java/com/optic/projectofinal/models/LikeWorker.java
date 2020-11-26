package com.optic.projectofinal.models;

import java.util.Date;

public class LikeWorker {
    private long timestamp;
    private String idWorker;

    public LikeWorker(String idWorker) {
        this.idWorker = idWorker;
        timestamp=new Date().getTime();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getIdWorker() {
        return idWorker;
    }

    public void setIdWorker(String idWorker) {
        this.idWorker = idWorker;
    }
}
