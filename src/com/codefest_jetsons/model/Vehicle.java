package com.codefest_jetsons.model;

/**
 * Created with IntelliJ IDEA.
 * User: nick49rt
 * Date: 2/23/13
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class Vehicle {
    public static final String LICENSE_PLATE = "vehicle.licenseplate";

    private String licensePlate;

    public Vehicle(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
