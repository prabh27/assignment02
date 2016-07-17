package com.cnu2016.assignment02;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;




public class SmartHome {
    List list = new ArrayList<Appliance>();

    public static void main(String... args) {
        

    }   

}
class Appliance {
    static enum State {
        ON,
        OFF;
    }
    static enum  Type {
        AirConditioner,
        CookingOven,
        WaterHeater;
    }

    State state;
    Type type;
    public Appliance(Type type, State state){
        this.state = state;
        this.type = type;
    }

    public Appliance() {

    }

    public void ScheduleEvent(Appliance modifiedAppliance, long delayTime, State currentState) {
        TimerTask tasknew = new MyTimerTask(modifiedAppliance, currentState);
        Timer timer = new Timer();
        timer.schedule(tasknew, delayTime); 
    }
    
}
class MyTimerTask extends TimerTask {
    Appliance appliance = new Appliance();
    private Appliance.State currentState;
    private Appliance.State previousState;
    public MyTimerTask(Appliance modifiedAppliance, Appliance.State currentState) {
        this.appliance = modifiedAppliance;
        this.currentState = currentState;
        this.previousState = modifiedAppliance.state;
    }
    
    public void run() {
        if(this.currentState == Appliance.State.OFF)
            appliance.state = Appliance.State.ON;
        else
            appliance.state = Appliance.State.OFF;
    }
}
