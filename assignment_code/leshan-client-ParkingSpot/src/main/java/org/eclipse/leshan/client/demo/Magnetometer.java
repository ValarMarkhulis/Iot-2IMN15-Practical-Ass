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

public class Magnetometer extends BaseInstanceEnabler {
    // Static values for resource items
    private static final int RES_X_VALUE = 5702;
    private static final int RES_Y_VALUE = 5703;
    private static final int RES_Z_VALUE = 5704;
    private static final int RES_COMPASS_DIRECTION = 5705;
    private static final int RES_SENSOR_UNITS = 5701;
    private static final List<Integer> supportedResources =
            Arrays.asList(
                    RES_X_VALUE
                    , RES_Y_VALUE
                    , RES_Z_VALUE
                    , RES_COMPASS_DIRECTION
                    , RES_SENSOR_UNITS
            );
    // Variables storing current values.
    private double vXValue = 0.0d;
    private double vYValue = 0.0d;
    private double vZValue = 0.0d;
    // 0-360
    private double vCompassDirection = 0.0d;
    private String vSensorUnits = "";

    public Magnetometer() {
    }

    @Override
    public synchronized ReadResponse read(ServerIdentity identity, int resourceId) {
        switch (resourceId) {
            case RES_X_VALUE:
                return ReadResponse.success(resourceId, vXValue);
            case RES_Y_VALUE:
                return ReadResponse.success(resourceId, vYValue);
            case RES_Z_VALUE:
                return ReadResponse.success(resourceId, vZValue);
            case RES_COMPASS_DIRECTION:
                return ReadResponse.success(resourceId, vCompassDirection);
            case RES_SENSOR_UNITS:
                return ReadResponse.success(resourceId, vSensorUnits);
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

    private synchronized void setXValue(double value) {
        if (vXValue != value) {
            vXValue = value;
            fireResourcesChange(RES_X_VALUE);
        }
    }

    private synchronized void setYValue(double value) {
        if (vYValue != value) {
            vYValue = value;
            fireResourcesChange(RES_Y_VALUE);
        }
    }

    private synchronized void setZValue(double value) {
        if (vZValue != value) {
            vZValue = value;
            fireResourcesChange(RES_Z_VALUE);
        }
    }

    private synchronized void setCompassDirection(double value) {
        if (vCompassDirection != value) {
            vCompassDirection = value;
            fireResourcesChange(RES_COMPASS_DIRECTION);
        }
    }

    private synchronized void setSensorUnits(String value) {
        if (vSensorUnits != value) {
            vSensorUnits = value;
            fireResourcesChange(RES_SENSOR_UNITS);
        }
    }

}
