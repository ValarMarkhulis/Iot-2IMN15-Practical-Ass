package org.eclipse.leshan.server.demo;

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

    public String getvLotName() {
        return vLotName;
    }

    private String vLotName = "TestLotName_-_IoT-pi42";
    private int vCapacity = 1337;
    private int vReservations = 2;
    private int vVehicles = 1;
    private int vFree = 4;

    //Holds one parkingSpot
    private ParkingSpot parkingSpot;
    String changeScreenScript = System.getProperty("user.dir") + "/python_code/set_parking_spot_status.py ";

    public ParkingLot(ParkingSpot pS) {
        if(parkingSpot == null){
            System.out.println("A parkingSpot was added: "+pS.toString());
            parkingSpot = pS;
            String spotStatus = parkingSpot.getvParkingSpotState();
            updateLotStatus(spotStatus);
        }

    }

    private void updateLotStatus(String spotStatus) {

        try {
            //Process p = Runtime.getRuntime().exec("espeak State_changed_to_" + spotStatus+"");

            //TODO: Get the x,y from the "Location" object
            String x = "0";
            String y = "0";

            System.out.println("Trying to run set_parking_spot_status.py from \n"
                    +changeScreenScript+" with parm x="+x+" y="+y+" spotStatus="+spotStatus);

            switch (spotStatus){
                case "free":
                    setParkingSpotStatus(x, y, "G");
                    break;
                case "reserved":
                    setParkingSpotStatus(x, y, "O");
                    break;
                case "occupied":
                    setParkingSpotStatus(x, y, "R");
                    break;
                default:
                    throw new Exception("Unknown SpotState String!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setParkingSpotStatus(String x, String y, String color)  {
        try {
            Process p2 = Runtime.getRuntime().exec(changeScreenScript + x +" " + y + " "+color);
            Scanner Errorscanner = new Scanner(p2.getErrorStream());

            while (Errorscanner.hasNext()) {
                String line2 = Errorscanner.next();
                System.err.println(line2);
            }
        }catch (IOException ex ){
            ex.printStackTrace();
        }

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

    public void makeSpotStateChange(String updateLotStatusString) {
        updateLotStatus(updateLotStatusString);
    }
}
