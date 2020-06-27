package frc.robot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLConnection;

import frc.robot.classes.ArcadeInput;

public class Camera {

  private static int camState;
  private static boolean camSwitchPressed = false;

  public static void init() {

    camState = 0;

  }

  public static void controlCam(ArcadeInput inputs) {

    if(inputs.getSwitchCam()) {
      if(!camSwitchPressed) {
        switchCam();
        camSwitchPressed = true;
      }
    }
    else {
      camSwitchPressed = false;
    }

  }

  public static int getCamState() {

    return camState;

  }

  public static void switchCam() {

    //DriveTrain.reverse();

    System.out.println("Camera switch activated with state: " + camState);

    if(camState == 0) {
      connect("http://10.38.12.6:1181/api/switchCam?camId=B");
      camState = 1;
    }
    else {
      connect("http://10.38.12.6:1181/api/switchCam?camId=A");
      camState = 0;
    }

  }

  private static void connect(String url) {
    URL destination;
    try {
      destination = new URL(url);
      URLConnection conn = destination.openConnection();
      conn.connect();
      System.out.println(conn.getContent());
    } 
    catch (MalformedURLException e) {
      System.out.println("Invalid URL");
      System.out.println(e);
    }
    catch (IOException e) {
      System.out.println("Connection Failed");
      System.out.println(e);
    }
  }

}
