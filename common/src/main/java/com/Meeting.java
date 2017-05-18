package com;

import java.awt.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;


@SuppressWarnings("all")
public class Meeting implements Serializable {

    private Instant start;
    private Instant end;
    private String title;
    private String description;
    private String location;

    private Collection<InsertedMeeting.Label> labels;

    public Meeting(String title, Instant start, Instant end, String description, String location, Collection<InsertedMeeting.Label> labels) {
        this.setStart(start);
        this.setEnd(end);
        this.setTitle(title);
        this.setDescription(description);
        this.setLocation(location);
        this.labels = new HashSet<>(labels);
    }

    public Collection<InsertedMeeting.Label> getLabels() {
        return this.labels;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return String.format("title: %s, start: %s, end: %s, description: %s, location: %s", getTitle(), getStart(),
            getEnd(), getDescription(), getLocation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Meeting meeting = (Meeting) o;

        if (!start.equals(meeting.start))
            return false;
        if (!end.equals(meeting.end))
            return false;
        if (!title.equals(meeting.title))
            return false;
        if (!description.equals(meeting.description))
            return false;
        if (!location.equals(meeting.location))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + location.hashCode();
        return result;
    }

    public static class Label implements Serializable {

        private Color color;
        private String name;
        private boolean active;

        public Label(String name, Color color, boolean active) {
            this.color = color;
            this.name = name;
            this.active = active;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return String.format("name: %s, color: %s", getName(), getColor());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Label label = (Label) o;

            if (active != label.active)
                return false;
            if (!color.equals(label.color))
                return false;
            if (!name.equals(label.name))
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = color.hashCode();
            result = 31 * result + name.hashCode();
            result = 31 * result + (active ? 1 : 0);
            return result;
        }
    }
}
