package server;

import com.*;

import java.util.Collection;
import java.util.HashSet;

@SuppressWarnings("all")
public class User extends Person {

  private MeetingService meetingService;
  private NotificationService notificationService;
  private PersonService personService;


  private LabelService labelService;

  private int ID;
  private Key key;

  public User(String name, int ID, Key key) {
    super(name);
    this.ID = ID;
    this.key = key;
  }

  public int getID() {
    return ID;
  }

  public Key getKey() {
    return key;
  }

  public LabelService getLabelService() {
    return labelService;
  }

  public void setLabelService(LabelService labelService) {
    this.labelService = labelService;
  }

  public MeetingService getMeetingService() {
    return meetingService;
  }

  public void setMeetingService(MeetingService meetingService) {
    this.meetingService = meetingService;
  }

  public PersonService getPersonService() {
    return this.personService;
  }

  public void setPersonService(PersonService service) {
    this.personService = service;
  }
  public NotificationService getNotificationService() {
    return notificationService;
  }

  public void setNotificationService(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

}
