package com.sms.data.model.dto;

/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

import com.sms.data.model.Route;
import java.util.List;

public class UpdateRouteDto {

    private long syncId;
    private List<Route> routes;

    public long getSyncId() {
        return syncId;
    }

    public void setSyncId(long syncId) {
        this.syncId = syncId;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "UpdateRouteDto{" +
                "syncId=" + syncId +
                ", routes=" + routes +
                '}';
    }
}
