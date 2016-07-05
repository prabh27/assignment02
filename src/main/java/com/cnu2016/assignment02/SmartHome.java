package com.cnu2016.assignment02;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;



public class SmartHome {
    static int testAC = 0;
    
    ArrayList<Appliance> list = new ArrayList<Appliance>();
    public static void main(String... args) {
        SmartHome s = new SmartHome();
        
        AirConditioner airConditioner = new AirConditioner();
        WaterHeater waterHeater = new WaterHeater();
        CookingOven cookingOven = new CookingOven();
        s.list.add(airConditioner);
        s.list.add(waterHeater);
        s.list.add(cookingOven);
        airConditioner.ScheduleEvent(airConditioner, s ,10);
    }
			    

}

class Appliance {
    public boolean state = false;

    public void ScheduleEvent(Appliance modifiedAppliance, SmartHome s, long delayTime) {
        TimerTask tasknew = new MyTimerTask(modifiedAppliance,s);
        Timer timer = new Timer();
        timer.schedule(tasknew, delayTime); 
        return;
    }
    
}

class MyTimerTask extends TimerTask {
    Appliance appliance = new Appliance();
        SmartHome s = new SmartHome();
    public MyTimerTask(Appliance modifiedAppliance, SmartHome s) {
        this.appliance = modifiedAppliance;
        this.s = s;

    }
    
    public void run() {
        appliance.state = true;
        if(appliance.state == true)
        {
            s.list.get(0).state = true;
        }
        return;
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
