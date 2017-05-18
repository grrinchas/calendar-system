package com;

@SuppressWarnings("all")
public class InsertedMeeting extends Meeting {

    private int id;

    public InsertedMeeting(Meeting meeting, int id) {
        super(meeting.getTitle(), meeting.getStart(), meeting.getEnd(), meeting.getDescription(), meeting.getLocation(),
            meeting.getLabels());
        this.setID(id);
    }

    private void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public Meeting getMeeting() {
        return new Meeting(getTitle(), getStart(), getEnd(), getDescription(), getLocation(), getLabels());
    }

    public void setMeeting(Meeting meeting) {
        setTitle(meeting.getTitle());
        setStart(meeting.getStart());
        setEnd(meeting.getEnd());
        setDescription(meeting.getDescription());
        setLocation(meeting.getLocation());
        getLabels().clear();
        getLabels().addAll(meeting.getLabels());
    }

    @Override
    public String toString() {
        return String.format("id: %d, %s", getID(), getMeeting());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        InsertedMeeting that = (InsertedMeeting) o;

        if (id != that.id)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        return result;
    }

    public static class Label extends Meeting.Label {

        private int id;

        public Label(Meeting.Label label, int id) {
            super(label.getName(), label.getColor(), label.isActive());
            this.id = id;
        }

        public Meeting.Label getLabel() {
            return new Meeting.Label(getName(), getColor(), isActive());
        }

        public void setLabel(Meeting.Label label) {
            setName(label.getName());
            setColor(label.getColor());
            setActive(label.isActive());
        }

        public int getID() {
            return this.id;
        }
    }
}
