package org.FitnessApp1.calendarfxfallback;

public class EntryEvent {

    // Konstantvärden som simulerar CalendarFX:s EntryEvent-typer
    public static final String ENTRY_CALENDAR_CHANGED = "calendar_changed";
    public static final String ENTRY_INTERVAL_CHANGED = "interval_changed";

    private final String eventType;

    public EntryEvent(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }
}
