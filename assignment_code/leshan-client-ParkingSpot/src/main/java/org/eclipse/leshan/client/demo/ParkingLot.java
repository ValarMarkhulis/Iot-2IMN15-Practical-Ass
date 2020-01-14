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

public class ParkingLot extends BaseInstanceEnabler {
    // Static values for resource items
    private static final int RES_PARKING_LOT_ID = 32800;
    private static final int RES_LOT_NAME = 32802;
    private static final int RES_CAPACITY = 32806;
    private static final int RES_RESERVATIONS = 32807;
    private static final int RES_VEHICLES = 32808;
    private static final int RES_FREE = 32809;
    private static final List<Integer> supportedResources =
            Arrays.asList(
                    RES_PARKING_LOT_ID
                    , RES_LOT_NAME
                    , RES_CAPACITY
                    , RES_RESERVATIONS
                    , RES_VEHICLES
                    , RES_FREE
            );
    // Variables storing current values.
    private String vParkingLotId = "TestId";
    private String vLotName = "TestLotName";
    private int vCapacity = 1337;
    private int vReservations = 2;
    private int vVehicles = 1;
    private int vFree = 4;

    public ParkingLot() {
    }

    @Override
    public synchronized ReadResponse read(ServerIdentity identity, int resourceId) {
        switch (resourceId) {
            case RES_PARKING_LOT_ID:
                return ReadResponse.success(resourceId, vParkingLotId);
            case RES_LOT_NAME:
                return ReadResponse.success(resourceId, vLotName);
            case RES_CAPACITY:
                return ReadResponse.success(resourceId, vCapacity);
            case RES_RESERVATIONS:
                return ReadResponse.success(resourceId, vReservations);
            case RES_VEHICLES:
                return ReadResponse.success(resourceId, vVehicles);
            case RES_FREE:
                return ReadResponse.success(resourceId, vFree);
            default:
                return super.read(identity, resourceId);
        }
    }

    @Override
    public WriteResponse write(ServerIdentity identity, int resourceId, LwM2mResource value) {
        switch (resourceId) {
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

    private synchronized void setParkingLotId(String value) {
        if (vParkingLotId != value) {
            vParkingLotId = value;
            fireResourcesChange(RES_PARKING_LOT_ID);
        }
    }

    private synchronized void setLotName(String value) {
        if (vLotName != value) {
            vLotName = value;
            fireResourcesChange(RES_LOT_NAME);
        }
    }

    private synchronized void setCapacity(int value) {
        if (vCapacity != value) {
            vCapacity = value;
            fireResourcesChange(RES_CAPACITY);
        }
    }

    private synchronized void setReservations(int value) {
        if (vReservations != value) {
            vReservations = value;
            fireResourcesChange(RES_RESERVATIONS);
        }
    }

    private synchronized void setVehicles(int value) {
        if (vVehicles != value) {
            vVehicles = value;
            fireResourcesChange(RES_VEHICLES);
        }
    }

    private synchronized void setFree(int value) {
        if (vFree != value) {
            vFree = value;
            fireResourcesChange(RES_FREE);
        }
    }

}
