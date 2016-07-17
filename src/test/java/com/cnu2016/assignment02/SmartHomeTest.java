package com.cnu2016.assignment02;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SmartHomeTest {
    @Test
    public void checkTestCase() {
        SmartHome s = new SmartHome();
        int acDelayTime = 10;
        int whDelayTime = 8;
        int coDelayTime = 3;
        Appliance airConditioner = new Appliance(Appliance.Type.AirConditioner, Appliance.State.OFF);
        Appliance waterHeater = new Appliance(Appliance.Type.WaterHeater, Appliance.State.OFF);
        Appliance cookingOven = new Appliance(Appliance.Type.CookingOven, Appliance.State.OFF);
        airConditioner.ScheduleEvent(airConditioner, acDelayTime, Appliance.State.OFF);
        waterHeater.ScheduleEvent(waterHeater, whDelayTime, Appliance.State.OFF);
        cookingOven.ScheduleEvent(cookingOven, coDelayTime, Appliance.State.OFF);
        s.list.add(airConditioner);
        s.list.add(waterHeater);
        s.list.add(cookingOven);
        try {

            Thread.sleep(40);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        assertEquals(Appliance.State.ON, airConditioner.state);
        assertEquals(Appliance.State.ON, waterHeater.state);
        assertEquals(Appliance.State.ON, cookingOven.state);
    }
}


