package com.codefest_jetsons.model;

/**
 * Created with IntelliJ IDEA.
 * User: nick49rt
 * Date: 2/23/13
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class Vehicle {
    public static final String VEHICLE_ID = "vehicle.vid";
    public static final String LICENSE_PLATE = "vehicle.licenseplate";

    private long vehicleId;
    private String licensePlate;

    public Vehicle(long vehicleId, String licensePlate) {
        this.vehicleId = vehicleId;
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }
}
