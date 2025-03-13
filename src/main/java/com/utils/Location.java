package com.utils;

public class Location {
    private double lat;
    private double lng;

    // Phương thức kiểm tra giá trị lat và lng hợp lệ
    public boolean isValid() {
        return lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180;
    }

    // Phương thức tính khoảng cách giữa hai địa điểm trên bề mặt trái đất(sử dụng công thức Haversine)
    public double distanceTo(Location other) {
        if (!this.isValid() || !other.isValid()) {
            throw new IllegalArgumentException("Invalid location");
        }
        double earthRadius = 6371; // R Earth (km)
        double dLat = Math.toRadians(other.lat - this.lat);
        double dLng = Math.toRadians(other.lng - this.lng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(this.lat)) * Math.cos(Math.toRadians(other.lat)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
