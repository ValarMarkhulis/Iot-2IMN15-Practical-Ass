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

public class MultipleAxisJoystick extends BaseInstanceEnabler {
    // Static values for resource items
    private static final int RES_DIGITAL_INPUT_STATE = 5500;
    private static final int RES_DIGITAL_INPUT_COUNTER = 5501;
    private static final int RES_X_VALUE = 5702;
    private static final int RES_Y_VALUE = 5703;
    private static final int RES_Z_VALUE = 5704;
    private static final int RES_APPLICATION_TYPE = 5750;
    private static final List<Integer> supportedResources =
            Arrays.asList(
                    RES_DIGITAL_INPUT_STATE
                    , RES_DIGITAL_INPUT_COUNTER
                    , RES_X_VALUE
                    , RES_Y_VALUE
                    , RES_Z_VALUE
                    , RES_APPLICATION_TYPE
            );
    // Variables storing current values.
    private boolean vDigitalInputState = false;
    private int vDigitalInputCounter = 0;
    private double vXValue = 0.0d;
    private double vYValue = 0.0d;
    private double vZValue = 0.0d;
    private String vApplicationType = "";

    public MultipleAxisJoystick() {
    }

    @Override
    public synchronized ReadResponse read(ServerIdentity identity, int resourceId) {
        switch (resourceId) {
            case RES_DIGITAL_INPUT_STATE:
                return ReadResponse.success(resourceId, vDigitalInputState);
            case RES_DIGITAL_INPUT_COUNTER:
                return ReadResponse.success(resourceId, vDigitalInputCounter);
            case RES_X_VALUE:
                return ReadResponse.success(resourceId, vXValue);
            case RES_Y_VALUE:
                return ReadResponse.success(resourceId, vYValue);
            case RES_Z_VALUE:
                return ReadResponse.success(resourceId, vZValue);
            case RES_APPLICATION_TYPE:
                return ReadResponse.success(resourceId, vApplicationType);
            default:
                return super.read(identity, resourceId);
        }
    }

    @Override
    public WriteResponse write(ServerIdentity identity, int resourceId, LwM2mResource value) {
        switch (resourceId) {
            case RES_APPLICATION_TYPE:
                vApplicationType = (String) value.getValue();
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

    private synchronized void setDigitalInputState(boolean value) {
        if (vDigitalInputState != value) {
            vDigitalInputState = value;
            fireResourcesChange(RES_DIGITAL_INPUT_STATE);
        }
    }

    private synchronized void setDigitalInputCounter(int value) {
        if (vDigitalInputCounter != value) {
            vDigitalInputCounter = value;
            fireResourcesChange(RES_DIGITAL_INPUT_COUNTER);
        }
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

    private synchronized void setApplicationType(String value) {
        if (vApplicationType != value) {
            vApplicationType = value;
            fireResourcesChange(RES_APPLICATION_TYPE);
        }
    }

}
