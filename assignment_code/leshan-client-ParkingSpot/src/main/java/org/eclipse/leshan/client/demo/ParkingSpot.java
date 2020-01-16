package org.eclipse.leshan.client.demo;

import org.eclipse.leshan.client.request.ServerIdentity;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
    private String vParkingSpotId = "Default_vParkingSpotId";
    // Free,Reserved,Occupied
    private final String[] SpotStates = new String[]{"free","reserved","occupied"};
    private String vParkingSpotState = "free";
    private String vLotName = "Default_vLotName";

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
                String vParkingSpotState_temp = ((String) value.getValue()).toLowerCase();
                for (String s: SpotStates)
                {
                    if(s.equals(vParkingSpotState_temp)){
                        vParkingSpotState = vParkingSpotState_temp;
                        fireResourcesChange(resourceId);
                        try {
                            Process p = Runtime.getRuntime().exec("espeak State_changed_to_" + vParkingSpotState+"");
                            String changeScreenScript = System.getProperty("user.dir") + "/python_code/LEDmatrixStatusChange.py ";
                            System.out.println("Trying to run LEDMatrixStatusChange from "+changeScreenScript);
                            switch (vParkingSpotState){
                                case "free":
                                    Process p2 = Runtime.getRuntime().exec(changeScreenScript + "G");
                                    Scanner Errorscanner = new Scanner(p2.getErrorStream());

                                    while (Errorscanner.hasNext()) {
                                        String line2 = Errorscanner.next();
                                        System.err.println(line2);
                                    }
                                    break;
                                case "reserved":
                                    p2 = Runtime.getRuntime().exec(changeScreenScript + "O");
                                    Errorscanner = new Scanner(p2.getErrorStream());

                                    while (Errorscanner.hasNext()) {
                                        String line2 = Errorscanner.next();
                                        System.err.println(line2);
                                    }
                                    break;
                                case "occupied":
                                    p2 = Runtime.getRuntime().exec(changeScreenScript + "R");
                                    Errorscanner = new Scanner(p2.getErrorStream());

                                    while (Errorscanner.hasNext()) {
                                        String line2 = Errorscanner.next();
                                        System.err.println(line2);
                                    }
                                    break;
                                default:
                                    throw new Exception("Unknown SpotState String!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return WriteResponse.success();
                    }
                }
                return WriteResponse.badRequest("The String=\""+vParkingSpotState_temp+"\" was not a valid parameter." +
                        "The following strings can be used as parameters: \""+vParkingSpotState_temp+"\"");

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
