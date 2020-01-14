package org.eclipse.leshan.server.demo;

import org.eclipse.leshan.core.node.*;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.request.ObserveRequest;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ObserveResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.server.californium.LeshanServer;
import org.eclipse.leshan.server.observation.ObservationListener;
import org.eclipse.leshan.server.registration.Registration;
import org.eclipse.leshan.server.registration.RegistrationListener;
import org.eclipse.leshan.server.registration.RegistrationUpdate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class LeshansServerListener {


    LeshanServer lwServer;
    private final String[] whichListener2ImplementArray = new String[]{"registered","updated","unregistered"};


    public LeshansServerListener(LeshanServer lwServer){
        this.lwServer = lwServer;
        //Setup 3 registration Listeners which makes 3 observation listeners for the 3 resources:
        makeListener(32700,0,32801,"registered"); //"/32700/0/32801"
        makeListener(32700,0,32802,"registered"); //"/32700/0/32802"
        makeListener(32702,0,-1,"registered"); //"/32700/0"

        lwServer.getObservationService().addListener(new ObservationListener() {
            @Override
            public void newObservation(Observation observation, Registration registration) {

            }

            @Override
            public void cancelled(Observation observation) {

            }

            @Override
            public void onResponse(Observation observation, Registration registration, ObserveResponse response) {
                if(observation.getPath().toString().matches("/32700/0/32801")){
                    //Look for "Parking Spot State" /32700/0/32801
                    LwM2mSingleResource test = (LwM2mSingleResource)response.getContent();

                    System.out.println("[Whats in /32700/0/32801] "+test.getValue().toString());


                    //System.out.println("Testing:"+observation.toString());
                }
                else if(observation.getPath().toString().matches("/32700/0/32802")){
                    //Look for "Parking Spot Lot name" /32700/0/32802
                    LwM2mSingleResource test = (LwM2mSingleResource)response.getContent();

                    System.out.println("[Whats in /32700/0/32802] "+test.getValue().toString());


                    //System.out.println("Testing:"+observation.toString());
                }else if(observation.getPath().toString().matches("/32700/0")){
                    //Look for "Parking Spot Instance 0" /32700/0
                    Map<Integer, LwM2mResource> test = ((LwM2mObjectInstance)response.getContent()).getResources();
                    test.forEach((k,v)-> System.out.println("Key=\""+k+"\" Value=\""+v.getValue().toString()+"\""));

                }else if(observation.getPath().toString().contains("/32702/0")){
                    //Look for "Parking Lot Instance 0" /32702/0/
                    Map<Integer, LwM2mResource> test = ((LwM2mObjectInstance)response.getContent()).getResources();
                    if(test.size() > 0){
                        System.out.println("Resources in /32702/0\n--------------");
                        test.forEach((k,v)-> System.out.println("Key="+k+" Value="+v.getValue().toString()));
                        System.out.println("-----------------");
                    }

                }
                System.out.println("Path is\""+observation.getPath().toString()+"\"");
            }

            @Override
            public void onError(Observation observation, Registration registration, Exception error) {

            }
        });
    }

    public void makeListener(int objectId, int objectInstanceId, int resourceID, String whichListener2Implement){

        //Create a RegistrationListener
        lwServer.getRegistrationService().addListener(new RegistrationListener() {

            /*This is called when the devices tries to register at the server*/
            public void registered(Registration registration, Registration previousReg,
                                   Collection< Observation > previousObsersations) {

                if(whichListener2Implement.equals("registered")){

                    //Print when a endpoint registrates itself
                    System.out.println("new device: " + registration.getEndpoint());

                    //Setup the path
                    String objID = Integer.toString(objectId);
                    String objInsID = "";
                    if(objectInstanceId != -1){
                        objInsID = "/"+Integer.toString(objectInstanceId);
                    }
                    String resID = null;
                    if(resourceID != -1){
                        resID = "/"+Integer.toString(resourceID);
                    }
                    final String stringOBJ = "/"+objID+""+objInsID+"";//+resID;

                    //See if the path is offered by the client
                    if(Arrays.asList(registration.getObjectLinks()).stream().anyMatch(l -> stringOBJ.equals(l.getUrl()))){
                        try {
                            String path = stringOBJ;
                            if(resID != null){
                                path = path.concat(""+resID);
                            }
                            //Make a ObserveRequest with the given path
                            LwM2mResponse response = lwServer.send(registration, new ObserveRequest(path), 5_000L);
                            //LwM2mResponse response = lwServer.send(registration, new ObserveRequest("/32700/0/32801"), 5_000L);
                            //LwM2mResponse response = lwServer.send(registration, new ObserveRequest("/332801"), 5_000L);
                            //LwM2mResponse response3 = lwServer.send(registration, new ObserveRequest(32700), 5_000L);
                            System.out.println("Observe response [registered]: " + response);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }else{
                        System.out.println("Did not match [registered]");
                    }
                }



            }

            public void updated(RegistrationUpdate update, Registration updatedReg, Registration previousReg) {
                System.out.println("device is still here: " + updatedReg.getEndpoint());

                //TODO: Remove this if not necessary
                /*
                if(whichListener2Implement.equals("updated") ){

                    String objID = Integer.toString(objectId);
                    String objInsID = Integer.toString(objectInstanceId);
                    String stringOBJ = "/"+objID+"/"+objInsID;


                    if(Arrays.stream(updatedReg.getObjectLinks()).anyMatch(l -> stringOBJ.equals(l.getUrl()))){
                        try {
                            LwM2mResponse response = lwServer.send(updatedReg, new ObserveRequest(objID), 5_000L);
                            //LwM2mResponse response = lwServer.send(updatedReg, new ObserveRequest(32700), 5_000L);
                            //System.out.println("Observe response [updated]: " + response);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }else{
                        System.out.println("Did not match [updated]");
                    }

                }*/

            }

            public void unregistered(Registration registration, Collection<Observation> observations, boolean expired,
                                     Registration newReg) {
                System.out.println("device left: " + registration.getEndpoint());
                //TODO: Implement!
            }
        });
    }

}
