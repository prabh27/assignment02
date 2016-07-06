package com.cnu2016.assignment02;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class SmartHome {
    ArrayList<Appliance> list = new ArrayList<Appliance>();
    public static void main(String... args) {
        SmartHome s = new SmartHome();
        AirConditioner airConditioner = new AirConditioner();
        WaterHeater waterHeater = new WaterHeater();
        CookingOven cookingOven = new CookingOven();
        s.list.add(airConditioner);
        s.list.add(waterHeater);
        s.list.add(cookingOven);
        s.list.get(0).ScheduleEvent(s.list.get(0),10);
        s.list.get(1).ScheduleEvent(s.list.get(1),10);
        s.list.get(2).ScheduleEvent(s.list.get(2),10);
        s.list.get(0).ScheduleEvent(s.list.get(0), 15);
        s.list.get(1).ScheduleEvent(s.list.get(1), 15);
        s.list.get(2).ScheduleEvent(s.list.get(2), 15);
    }   

}
class Appliance {
    boolean state = false;
    public void ScheduleEvent(Appliance modifiedAppliance, long delayTime) {
        TimerTask tasknew = new MyTimerTask(modifiedAppliance);
        Timer timer = new Timer();
        timer.schedule(tasknew, delayTime); 
    }
    
}
class MyTimerTask extends TimerTask {
    Appliance appliance = new Appliance();
    public MyTimerTask(Appliance modifiedAppliance) {
        appliance = modifiedAppliance;
    }
    
    public void run() {
        if(appliance.state == true)
            appliance.state = false;
        else
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
