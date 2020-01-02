package org.eclipse.leshan.client.demo;

import org.eclipse.leshan.client.request.ServerIdentity;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;

import java.util.Arrays;
import java.util.List;

public class ParkingSpot extends BaseInstanceEnabler {
    // Static values for resource items
    private static final int RES_PARKING_SPOT_ID = 32800;
    private static final int RES_PARKING_SPOT_STATE = 32801;
    private static final int RES_LOT_NAME = 32802;
    private static final List<Integer> supportedResources =
            Arrays.asList(
                    RES_PARKING_SPOT_ID
                    , RES_PARKING_SPOT_STATE
                    , RES_LOT_NAME
            );
    // Variables storing current values.
    private String vParkingSpotId = "";
    // Free,Reserved,Occupied
    private String vParkingSpotState = "";
    private String vLotName = "";

    public ParkingSpot() {
    }

    @Override
    public synchronized ReadResponse read(ServerIdentity identity, int resourceId) {
        switch (resourceId) {
            case RES_PARKING_SPOT_ID:
                return ReadResponse.success(resourceId, vParkingSpotId);
            case RES_PARKING_SPOT_STATE:
                return ReadResponse.success(resourceId, vParkingSpotState);
            case RES_LOT_NAME:
                return ReadResponse.success(resourceId, vLotName);
            default:
                return super.read(identity, resourceId);
        }
    }

    @Override
    public WriteResponse write(ServerIdentity identity, int resourceId, LwM2mResource value) {
        switch (resourceId) {
            case RES_PARKING_SPOT_STATE:
                vParkingSpotState = (String) value.getValue();
                fireResourcesChange(resourceId);
                return WriteResponse.success();
            case RES_LOT_NAME:
                vLotName = (String) value.getValue();
                fireResourcesChange(resourceId);
                return WriteResponse.success();
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

    private synchronized void setParkingSpotId(String value) {
        if (vParkingSpotId != value) {
            vParkingSpotId = value;
            fireResourcesChange(RES_PARKING_SPOT_ID);
        }
    }

    private synchronized void setParkingSpotState(String value) {
        if (vParkingSpotState != value) {
            vParkingSpotState = value;
            fireResourcesChange(RES_PARKING_SPOT_STATE);
        }
    }

    private synchronized void setLotName(String value) {
        if (vLotName != value) {
            vLotName = value;
            fireResourcesChange(RES_LOT_NAME);
        }
    }

}
