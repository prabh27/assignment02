package com.cnu2016.assignment02;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class SmartHome {
    ArrayList<Appliance> list = new ArrayList<Appliance>();
    public static void main(String... args) {
        

    }   

}
class Appliance {
    boolean state = false;
    public void ScheduleEvent(Appliance modifiedAppliance, long delayTime, int value) {
        TimerTask tasknew = new MyTimerTask(modifiedAppliance, value);
        Timer timer = new Timer();
        timer.schedule(tasknew, delayTime); 
    }
    
}
class MyTimerTask extends TimerTask {
    Appliance appliance = new Appliance();
    boolean previousState;
    int value;
    public MyTimerTask(Appliance modifiedAppliance, int value) {
        this.appliance = modifiedAppliance;
        this.previousState = modifiedAppliance.state;
        this.value = value;
    }
    
    public void run() {
        if(value == 0 && previousState == true)
            appliance.state = false;
        else if(value == 0 && previousState == false)
            System.out.println("Appliance already switched off");
        if(value == 1)
            appliance.state = true;
    }
}
class AirConditioner extends Appliance {
    public AirConditioner() {
        super();
    }
}
class CookingOven extends Appliance {
    public CookingOven() {
        super();
    }
}
class WaterHeater extends Appliance {
    public WaterHeater() {
        super();
    }
}
