package org.eclipse.leshan.client.demo;

import org.eclipse.leshan.client.request.ServerIdentity;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AddressableTextDisplay extends BaseInstanceEnabler {
    // Static values for resource items
    private static final int RES_TEXT = 5527;
    private static final int RES_X_COORDINATE = 5528;
    private static final int RES_Y_COORDINATE = 5529;
    private static final int RES_MAX_X_COORDINATE = 5545;
    private static final int RES_MAX_Y_COORDINATE = 5546;
    private static final int RES_CLEAR_DISPLAY = 5530;
    private static final int RES_LEVEL = 5548;
    private static final int RES_CONTRAST = 5531;
    private static final int RES_APPLICATION_TYPE = 5750;
    private static final List<Integer> supportedResources =
            Arrays.asList(
                    RES_TEXT
                    , RES_X_COORDINATE
                    , RES_Y_COORDINATE
                    , RES_MAX_X_COORDINATE
                    , RES_MAX_Y_COORDINATE
                    , RES_CLEAR_DISPLAY
                    , RES_LEVEL
                    , RES_CONTRAST
                    , RES_APPLICATION_TYPE
            );
    // Variables storing current values.
    private String vText = "";
    private int vXCoordinate = 0;
    private int vYCoordinate = 0;
    private int vMaxXCoordinate = 0;
    private int vMaxYCoordinate = 0;
    // 0-100
    private double vLevel = 0.0d;
    // 0-100
    private double vContrast = 0.0d;
    private String vApplicationType = "";

    /*
    Our code
     */
    private String Command;
    private boolean isRPI = false;

    public AddressableTextDisplay() {

        try {
            if(InetAddress.getLocalHost().getHostName().contains("IoT-pi42")){
                isRPI = true;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized ReadResponse read(ServerIdentity identity, int resourceId) {
        switch (resourceId) {
            case RES_TEXT:
                return ReadResponse.success(resourceId, vText);
            case RES_X_COORDINATE:
                return ReadResponse.success(resourceId, vXCoordinate);
            case RES_Y_COORDINATE:
                return ReadResponse.success(resourceId, vYCoordinate);
            case RES_MAX_X_COORDINATE:
                return ReadResponse.success(resourceId, vMaxXCoordinate);
            case RES_MAX_Y_COORDINATE:
                return ReadResponse.success(resourceId, vMaxYCoordinate);
            case RES_LEVEL:
                return ReadResponse.success(resourceId, vLevel);
            case RES_CONTRAST:
                return ReadResponse.success(resourceId, vContrast);
            case RES_APPLICATION_TYPE:
                return ReadResponse.success(resourceId, vApplicationType);
            default:
                return super.read(identity, resourceId);
        }
    }

    @Override
    public WriteResponse write(ServerIdentity identity, int resourceId, LwM2mResource value) {
        switch (resourceId) {
            case RES_TEXT:
                Command = "Text_is_" + value.getValue().toString().replace(" ", "_");
                Command.concat(" --stdout | aplay");
                callPythonScript();
                try {
                    Process p = Runtime.getRuntime().exec("espeak " + Command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vText = (String) value.getValue();
                fireResourcesChange(resourceId);
                return WriteResponse.success();
            case RES_X_COORDINATE:
                Command = "x_is_" + value.getValue().toString();
                Command.concat(" --stdout | aplay");
                try {
                    Process p = Runtime.getRuntime().exec("espeak " + Command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vXCoordinate = Integer.parseInt(value.getValue().toString());
                fireResourcesChange(resourceId);
                return WriteResponse.success();
            case RES_Y_COORDINATE:
                Command = "y_is_" + value.getValue().toString();
                try {
                    Process p = Runtime.getRuntime().exec("espeak " + Command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vYCoordinate = Integer.parseInt(value.getValue().toString());
                fireResourcesChange(resourceId);
                return WriteResponse.success();
            case RES_LEVEL:
                Command = "res_level_is_" + Float.parseFloat(value.getValue().toString());
                try {
                    Process p = Runtime.getRuntime().exec("espeak " + Command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vLevel = Float.parseFloat(value.getValue().toString());
                fireResourcesChange(resourceId);
                return WriteResponse.success();
            case RES_CONTRAST:
                Command = "res_contrast_is_" + Float.parseFloat(value.getValue().toString());
                try {
                    Process p = Runtime.getRuntime().exec("espeak " + Command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vContrast = Float.parseFloat(value.getValue().toString());
                fireResourcesChange(resourceId);
                return WriteResponse.success();
            case RES_APPLICATION_TYPE:
                Command = "res_application_type_is_" + value.getValue().toString();
                try {
                    Process p = Runtime.getRuntime().exec("espeak " + Command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vApplicationType = (String) value.getValue();
                fireResourcesChange(resourceId);
                return WriteResponse.success();
            default:
                return super.write(identity, resourceId, value);
        }
    }

    private void callPythonScript() {
        Thread joystickReader;

        joystickReader = new Thread(new Runnable() {
            public void run() {          Process p = null;
                try {
                    p = Runtime.getRuntime().exec("python /home/chris/IdeaProjects/LegeLand/testText2Spe.py");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scanner scanner = new Scanner(p.getInputStream());
                while (scanner.hasNext()) {
                    String line = scanner.next();
                    System.out.println(line);
                    // process the content of line.
                    if(line.matches("test!")){
                        //doSomething();
                    }
                } }
        });
        joystickReader.start();
    }

    @Override
    public synchronized ExecuteResponse execute(ServerIdentity identity, int resourceId, String params) {
        switch (resourceId) {
            case RES_CLEAR_DISPLAY:
                // Perform the related function.
                Command = "Clear_Display";
                try {
                    Process p = Runtime.getRuntime().exec("espeak " + Command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return ExecuteResponse.success();
            default:
                return super.execute(identity, resourceId, params);
        }
    }

    @Override
    public List<Integer> getAvailableResourceIds(ObjectModel model) {
        return supportedResources;
    }

    private synchronized void setText(String value) {
        if (vText != value) {
            vText = value;
            fireResourcesChange(RES_TEXT);
        }
    }

    private synchronized void setXCoordinate(int value) {
        if (vXCoordinate != value) {
            vXCoordinate = value;
            fireResourcesChange(RES_X_COORDINATE);
        }
    }

    private synchronized void setYCoordinate(int value) {
        if (vYCoordinate != value) {
            vYCoordinate = value;
            fireResourcesChange(RES_Y_COORDINATE);
        }
    }

    private synchronized void setMaxXCoordinate(int value) {
        if (vMaxXCoordinate != value) {
            vMaxXCoordinate = value;
            fireResourcesChange(RES_MAX_X_COORDINATE);
        }
    }

    private synchronized void setMaxYCoordinate(int value) {
        if (vMaxYCoordinate != value) {
            vMaxYCoordinate = value;
            fireResourcesChange(RES_MAX_Y_COORDINATE);
        }
    }

    private synchronized void setLevel(double value) {
        if (vLevel != value) {
            vLevel = value;
            fireResourcesChange(RES_LEVEL);
        }
    }

    private synchronized void setContrast(double value) {
        if (vContrast != value) {
            vContrast = value;
            fireResourcesChange(RES_CONTRAST);
        }
    }

    private synchronized void setApplicationType(String value) {
        if (vApplicationType != value) {
            vApplicationType = value;
            fireResourcesChange(RES_APPLICATION_TYPE);
        }
    }



}
