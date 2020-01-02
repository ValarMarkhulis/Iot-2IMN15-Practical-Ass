package org.eclipse.leshan.client.demo;

import org.eclipse.leshan.client.request.ServerIdentity;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Location extends BaseInstanceEnabler {
    // Static values for resource items
    private static final int RES_LATITUDE = 0;
    private static final int RES_LONGITUDE = 1;
    private static final int RES_ALTITUDE = 2;
    private static final int RES_RADIUS = 3;
    private static final int RES_VELOCITY = 4;
    private static final int RES_TIMESTAMP = 5;
    private static final int RES_SPEED = 6;
    private static final List<Integer> supportedResources =
            Arrays.asList(
                    RES_LATITUDE
                    , RES_LONGITUDE
                    , RES_ALTITUDE
                    , RES_RADIUS
                    , RES_VELOCITY
                    , RES_TIMESTAMP
                    , RES_SPEED
            );
    // Variables storing current values.
    private double vLatitude = 0.0d;
    private double vLongitude = 0.0d;
    private double vAltitude = 0.0d;
    private double vRadius = 0.0d;
    private double vVelocity = 0;
    private Date vTimestamp = new Date();
    private double vSpeed = 0.0d;

    public Location() {
    }

    @Override
    public synchronized ReadResponse read(ServerIdentity identity, int resourceId) {
        switch (resourceId) {
            case RES_LATITUDE:
                return ReadResponse.success(resourceId, vLatitude);
            case RES_LONGITUDE:
                return ReadResponse.success(resourceId, vLongitude);
            case RES_ALTITUDE:
                return ReadResponse.success(resourceId, vAltitude);
            case RES_RADIUS:
                return ReadResponse.success(resourceId, vRadius);
            case RES_VELOCITY:
                return ReadResponse.success(resourceId, vVelocity); // Added .toString
            case RES_TIMESTAMP:
                return ReadResponse.success(resourceId, vTimestamp);
            case RES_SPEED:
                return ReadResponse.success(resourceId, vSpeed);
            default:
                return super.read(identity, resourceId);
        }
    }

    @Override
    public WriteResponse write(ServerIdentity identity, int resourceId, LwM2mResource value) {
        switch (resourceId) {
            default:
                return super.write(identity, resourceId, value);
        }
    }

    @Override
    public synchronized ExecuteResponse execute(ServerIdentity identity, int resourceId, String params) {
        switch (resourceId) {
            default:
                return super.execute(identity, resourceId, params);
        }
    }

    @Override
    public List<Integer> getAvailableResourceIds(ObjectModel model) {
        return supportedResources;
    }

    private synchronized void setLatitude(double value) {
        if (vLatitude != value) {
            vLatitude = value;
            fireResourcesChange(RES_LATITUDE);
        }
    }

    private synchronized void setLongitude(double value) {
        if (vLongitude != value) {
            vLongitude = value;
            fireResourcesChange(RES_LONGITUDE);
        }
    }

    private synchronized void setAltitude(double value) {
        if (vAltitude != value) {
            vAltitude = value;
            fireResourcesChange(RES_ALTITUDE);
        }
    }

    private synchronized void setRadius(double value) {
        if (vRadius != value) {
            vRadius = value;
            fireResourcesChange(RES_RADIUS);
        }
    }

    private synchronized void setVelocity(double value) {
        if (vVelocity != value) {
            vVelocity = value;
            fireResourcesChange(RES_VELOCITY);
        }
    }

    private synchronized void setTimestamp(Date value) {
        if (vTimestamp != value) {
            vTimestamp = value;
            fireResourcesChange(RES_TIMESTAMP);
        }
    }

    private synchronized void setSpeed(double value) {
        if (vSpeed != value) {
            vSpeed = value;
            fireResourcesChange(RES_SPEED);
        }
    }

}
