package model;

import java.time.LocalTime;

public class OpeningHours {

    private LocalTime openTime;

    private LocalTime closeTime;

    private boolean isOpen;

    public OpeningHours(LocalTime openTime, LocalTime closeTime, boolean isOpen) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isOpen = isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }
}
