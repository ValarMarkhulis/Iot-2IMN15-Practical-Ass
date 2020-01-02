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

public class VehicleCounter extends BaseInstanceEnabler {
    // Static values for resource items
    private static final int RES_VEHICLE_COUNTER_ID = 32800;
    private static final int RES_LOT_NAME = 32802;
    private static final int RES_VEHICLE_COUNTER = 32803;
    private static final int RES_LAST_PLATE = 32804;
    private static final int RES_DIRECTION = 32805;
    private static final List<Integer> supportedResources =
            Arrays.asList(
                    RES_VEHICLE_COUNTER_ID
                    , RES_LOT_NAME
                    , RES_VEHICLE_COUNTER
                    , RES_LAST_PLATE
                    , RES_DIRECTION
            );
    // Variables storing current values.
    private String vVehicleCounterId = "";
    private String vLotName = "";
    private int vVehicleCounter = 0;
    private String vLastPlate = "";
    // 1: Enter, 0: Exit
    private int vDirection = 0;

    public VehicleCounter() {
    }

    @Override
    public synchronized ReadResponse read(ServerIdentity identity, int resourceId) {
        switch (resourceId) {
            case RES_VEHICLE_COUNTER_ID:
                return ReadResponse.success(resourceId, vVehicleCounterId);
            case RES_LOT_NAME:
                return ReadResponse.success(resourceId, vLotName);
            case RES_VEHICLE_COUNTER:
                return ReadResponse.success(resourceId, vVehicleCounter);
            case RES_LAST_PLATE:
                return ReadResponse.success(resourceId, vLastPlate);
            case RES_DIRECTION:
                return ReadResponse.success(resourceId, vDirection);
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

    private synchronized void setVehicleCounterId(String value) {
        if (vVehicleCounterId != value) {
            vVehicleCounterId = value;
            fireResourcesChange(RES_VEHICLE_COUNTER_ID);
        }
    }

    private synchronized void setLotName(String value) {
        if (vLotName != value) {
            vLotName = value;
            fireResourcesChange(RES_LOT_NAME);
        }
    }

    private synchronized void setVehicleCounter(int value) {
        if (vVehicleCounter != value) {
            vVehicleCounter = value;
            fireResourcesChange(RES_VEHICLE_COUNTER);
        }
    }

    private synchronized void setLastPlate(String value) {
        if (vLastPlate != value) {
            vLastPlate = value;
            fireResourcesChange(RES_LAST_PLATE);
        }
    }

    private synchronized void setDirection(int value) {
        if (vDirection != value) {
            vDirection = value;
            fireResourcesChange(RES_DIRECTION);
        }
    }

}
