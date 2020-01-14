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
        //makeListener(6,0,0,"registered");
        //makeListener(6,0,1,"registered");
        makeListener(32700,0,32801,"registered");
        makeListener(32702,0,1,"registered");

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
                    Map<Integer, LwM2mResource> test = ((LwM2mObject)response.getContent()).getInstance(0).getResources();
                    //System.out.println("[Whast in /32700/0/32800] "+test.get(32800).getValue().toString());
                    System.out.println("[Whast in /32700/0/32801] "+test.get(32801).getValue().toString());
                    //System.out.println("[Whast in /32700/0/32802] "+test.get(32801).getValue().toString());
                    LwM2mNode content = response.getContent();
                    // gson.toJson(response.getContent())
                    //content[]
                    //lwM2mNodeVisitor.visit((LwM2mObjectInstance) content);
                    //content.accept();
                    System.out.println(content.toString());

                    System.out.println("Testing:"+observation.toString());
                }else if(observation.getPath().toString().contains("/32702")){
                    Map<Integer, LwM2mResource> test = ((LwM2mObjectInstance)response.getContent()).getResources();
                    System.out.println("[Whast in /32702/0/32802] "+test.get(32802).getValue().toString());

                }
                System.out.println("Path is\""+observation.getPath().toString()+"\"");
            }

            @Override
            public void onError(Observation observation, Registration registration, Exception error) {

            }
        });
    }

    public void makeListener(int objectId, int objectInstanceId, int resourceID, String whichListener2Implement){

        lwServer.getRegistrationService().addListener(new RegistrationListener() {

            public void registered(Registration registration, Registration previousReg,
                                   Collection< Observation > previousObsersations) {
                /*try {
                    ReadResponse response = lwServer.send(registration, new ReadRequest(objectId,objectInstanceId,resourceID));

                    if (response.isSuccess()) {
                        LwM2mSingleResource content = (LwM2mSingleResource) response.getContent();
                        System.out.println(content.getValue().toString());

                    }else {
                        System.out.println("Failed to read:" + response.getCode() + " " + response.getErrorMessage());
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                if(whichListener2Implement.equals("registered")){
                    System.out.println("new device: " + registration.getEndpoint());
                    String objID = Integer.toString(objectId);
                    String objInsID = "";
                    if(objectInstanceId != -1){
                        objInsID = "/"+Integer.toString(objectInstanceId);
                    }
                    String resID = "";
                    if(resourceID != -1){
                        resID = "/"+Integer.toString(resourceID);
                    }

                    String stringOBJ = "/"+objID+""+objInsID+"";//+resID;
                    if(Arrays.asList(registration.getObjectLinks()).stream().anyMatch(l -> stringOBJ.equals(l.getUrl()))){
                        try {
                            LwM2mResponse response = lwServer.send(registration, new ObserveRequest("/332801"), 5_000L);
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
                if(whichListener2Implement.equals("updated") ){
                    System.out.println("device is still here: " + updatedReg.getEndpoint());
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

                }

            }

            public void unregistered(Registration registration, Collection<Observation> observations, boolean expired,
                                     Registration newReg) {
                System.out.println("device left: " + registration.getEndpoint());
                //TODO: Implement!
            }
        });
    }

}
