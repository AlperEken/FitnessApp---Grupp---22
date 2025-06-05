package org.FitnessApp1.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Activity {
    private String note;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public Activity(String note, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.note = note;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
