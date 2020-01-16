package org.eclipse.leshan.server.demo;

import org.apache.commons.lang.StringUtils;
import org.eclipse.leshan.core.model.ResourceModel;
import org.eclipse.leshan.core.node.*;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.request.ContentFormat;
import org.eclipse.leshan.core.request.ObserveRequest;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.request.WriteRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ObserveResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.eclipse.leshan.server.californium.LeshanServer;
import org.eclipse.leshan.server.observation.ObservationListener;
import org.eclipse.leshan.server.registration.Registration;
import org.eclipse.leshan.server.registration.RegistrationListener;
import org.eclipse.leshan.server.registration.RegistrationUpdate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class LeshansServerListener {


    private ParkingLot parkingLot;
    LeshanServer lwServer;
    private final String[] whichListener2ImplementArray = new String[]{"registered", "updated", "unregistered"};
    ParkingSpot pS;
    ZmqCommunicator zmq_boy;

    /**
     * @param lwServer
     * @param zmq_boy
     *
     */
    public LeshansServerListener(LeshanServer lwServer, ZmqCommunicator zmq_boy) {


        this.zmq_boy = zmq_boy;

        this.lwServer = lwServer;
        //Setup 3 registration Listeners which makes 3 observation listeners for the 3 resources:
        //Look for "Parking Spot State" /32700/0
        makeListener(32700, 0, -1, "registered");
        //Look for "Parking Spot State" /32700/0/32801
        makeListener(32700, 0, 32801, "registered");
        //Look for "Parking Spot Lot name" /32700/0/32802
        makeListener(32700, 0, 32802, "registered");
        //Look for "Parking Spot Instance 0" /32700/0
        makeListener(32702, 0, -1, "registered");
        //TODO: Make reservation for a "Location" object to obtain a "x" and "y" value from the "RES_LATITUDE"
        // and "RES_LONGITUDE"

        lwServer.getObservationService().addListener(new ObservationListener() {
            @Override
            public void newObservation(Observation observation, Registration registration) {

            }

            @Override
            public void cancelled(Observation observation) {

            }

            @Override
            public void onResponse(Observation observation, Registration registration, ObserveResponse response) {
                if (observation.getPath().toString().matches("/32700/0/32801")) {
                    //Look for "Parking Spot State" /32700/0/32801
                    LwM2mSingleResource test = (LwM2mSingleResource) response.getContent();

                    System.out.print("[Whats in \"/32700/0/32801\"] \"" + test.getValue().toString() + "\" - ");
                    // write the new state to the parking lot so it can update the display
                    parkingLot.makeSpotStateChange(test.getValue().toString());

                    //Do the ZMQ message to update a parking spot at the ZMQ server
                    zmq_boy.SendMessage("REG " + pS.getvParkingSpotId() + " " + pS.getvParkingSpotState() +
                            " " + pS.getvLotName());


                    //System.out.println("Testing:"+observation.toString());
                } else if (observation.getPath().toString().matches("/32700/0/32802")) {
                    //Look for "Parking Spot Lot name" /32700/0/32802
                    LwM2mSingleResource test = (LwM2mSingleResource) response.getContent();

                    System.out.print("[Whats in \"/32700/0/32802\"] \"" + test.getValue().toString() + "\" - ");


                    //System.out.println("Testing:"+observation.toString());
                } else if (observation.getPath().toString().matches("/32700/0")) {
                    //Look for "Parking Spot Instance 0" /32700/0
                    Map<Integer, LwM2mResource> test = ((LwM2mObjectInstance) response.getContent()).getResources();

                    System.out.println("\nResources in /32700/0\n--------------");
                    test.forEach((k, v) -> System.out.println("Key=\"" + k + "\"\t\t Value=\"" + v.getValue().toString() + "\""));
                    System.out.println("-----------------");

                } else if (observation.getPath().toString().contains("/32702/0")) {
                    //Look for "Parking Lot Instance 0" /32702/0/
                    Map<Integer, LwM2mResource> test = ((LwM2mObjectInstance) response.getContent()).getResources();
                    if (test.size() > 0) {
                        System.out.println("\nResources in /32702/0\n--------------");
                        test.forEach((k, v) -> System.out.println("Key=\"" + k + "\"\t\t Value=\"" + v.getValue().toString()+"\""));
                        System.out.println("-----------------");
                    }

                }
                System.out.println("Path was \"" + observation.getPath().toString() +
                        "\" - [Endpoint:"+registration.getEndpoint()+"]\n");
            }

            @Override
            public void onError(Observation observation, Registration registration, Exception error) {

            }
        });
    }

    public void makeListener(int objectId, int objectInstanceId, int resourceID, String whichListener2Implement) {

        //Create a RegistrationListener
        lwServer.getRegistrationService().addListener(new RegistrationListener() {

            /*This is called when the devices tries to register at the server*/
            public void registered(Registration registration, Registration previousReg,
                                   Collection<Observation> previousObsersations) {

                //If the registration is about /32700/0/ "Parking Spot Instance 0"
                if(resourceID == -1 && objectInstanceId == 0 && objectId == 32700){
                    System.out.println("A parking spot instance is trying to register itself!");
                    ReadResponse response = null;
                    try {
                        //Do a read request on "Parking Spot Instance 0"
                        response = lwServer.send(registration, new ReadRequest(32700), 5_000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //System.out.println("DEBUG: \"/32700/\" res:" + ((LwM2mObject) response.getContent()).getInstance(0).getResources());

                    //Put the ReadResponse into a Map, so we can get the value-key pair.
                    Map<Integer, LwM2mResource> test = ((LwM2mObject) response.getContent()).getInstance(0).getResources();
                    if (test.size() > 0) {
                        System.out.println("\nResources in /32702/0\n--------------");
                        test.forEach((k, v) -> System.out.println("Key=\"" + k + "\"\t\t Value=\"" + v.getValue().toString()+"\""));
                        System.out.println("-----------------");

                        //Make a new parkingSpot and add it to a new ParkingLot
                        if(pS == null){ //TODO: There should be able to be more parkingSpots.
                            final String vParkingSpotId = test.get(32800).getValue().toString();
                            final String vParkingSpotState = test.get(32801).getValue().toString();
                            final String vLotName = test.get(32802).getValue().toString();

                            pS = new ParkingSpot(vParkingSpotId,vParkingSpotState,vLotName);
                            parkingLot = new ParkingLot(pS);

                            //Do the ZMQ message to register a parking spot at the ZMQ server
                            zmq_boy.SendMessage("REG " + vParkingSpotId + " " + vParkingSpotState +
                                    " " + parkingLot.getvLotName());

                            try {
                                //Do a WriteRequest to "Parking Spot Lot name" /32700/0/32802 with the parkingLot "vLotName" name
                                WriteRequest writeRequest = new WriteRequest(ContentFormat.TLV, 32700
                                        , 0, 32802, parkingLot.getvLotName());

                                //send the writeRequest
                                final WriteResponse writeResponse = lwServer.send(registration, writeRequest, 5000);

                                //Try to handle the writeResponse
                                if(!writeResponse.isSuccess() || !writeResponse.isValid() ){
                                    System.out.println("The writeResponse to \"Parking Spot Lot name\" with name=" + parkingLot.getvLotName() + " failed");

                                    if(!writeResponse.isSuccess()){
                                        System.out.println("DEBUG: isSuccess() returned false");
                                    }else if(!writeResponse.isValid()){
                                        System.out.println("DEBUG: isValid() returned false");
                                    }
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    //System.out.println("The parking spot is in this state right now!: " + response);
                }//TODO: Look for the registraion of a Location object path="/6" and pass it to the parkingspot

                if (whichListener2Implement.equals("registered")) {

                    //Print when a endpoint registrates itself
                    if (Integer.toString(resourceID).matches("32801")) { //Only print once
                        System.out.println("new device: " + registration.getEndpoint());
                    }

                    //Setup the path
                    String objID = Integer.toString(objectId);
                    String objInsID = "";
                    if (objectInstanceId != -1) {
                        objInsID = "/" + Integer.toString(objectInstanceId);
                    }
                    String resID = null;
                    if (resourceID != -1) {
                        resID = "/" + Integer.toString(resourceID);
                    }
                    final String stringOBJ = "/" + objID + "" + objInsID + "";//+resID;

                    //See if the path is offered by the client
                    if (Arrays.asList(registration.getObjectLinks()).stream().anyMatch(l -> stringOBJ.equals(l.getUrl()))) {
                        try {
                            String path = stringOBJ;
                            if (resID != null) {
                                path = path.concat("" + resID);
                            }
                            //Make a ObserveRequest with the given path
                            LwM2mResponse response = lwServer.send(registration, new ObserveRequest(path), 5_000L);

                            //Some of the paths we can look at
                            //LwM2mResponse response = lwServer.send(registration, new ObserveRequest("/32700/0/32801"), 5_000L);
                            //LwM2mResponse response = lwServer.send(registration, new ObserveRequest("/332801"), 5_000L);
                            //LwM2mResponse response3 = lwServer.send(registration, new ObserveRequest(32700), 5_000L);

                            //Debug
                            //System.out.println("Observe response [registered]: " + response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Did not match [registered]");
                    }
                }


            }

            public void updated(RegistrationUpdate update, Registration updatedReg, Registration previousReg) {
                if (Integer.toString(resourceID).matches("32801")) {
                    System.out.println("device is still here: " + updatedReg.getEndpoint());
                }
            }

            public void unregistered(Registration registration, Collection<Observation> observations, boolean expired,
                                     Registration newReg) {
                //Only do one print
                if (Integer.toString(resourceID).matches("32801")) {
                    System.out.println("device left: " + registration.getEndpoint());
                }

            }
        });
    }

}
