package view.view;

import com.InsertedMeeting;
import com.Meeting;
import com.MeetingService;
import model.Cache;
import view.ColorScheme;
import view.comp.JLabelFactory;
import view.comp.JPanelFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.*;

@SuppressWarnings("all")
public class YearView extends View {
    private static int BUTTON_WIDTH = 40;
    private JLabel dateLabel = new JLabel();
    private JPanel calendar = new JPanel();

    public YearView(MeetingService meetingService, Cache cache) {
        super(meetingService, cache);
        cache.addCacheListener(this);
        this.initYearView();
        onMeetingChange(cache);
    }

    @Override
    public void onMeetingChange(Cache cache) {

        Collection<InsertedMeeting> meetings = cache.produceMeetings();
        Collection<InsertedMeeting.Label> labels = cache.produceLabels();
        this.initMonths();
        for (InsertedMeeting m : meetings) {
            LocalDate mDate = LocalDateTime.ofInstant(m.getStart(), ZoneOffset.UTC).toLocalDate();

            if (mDate.getYear() == super.getDate().getYear()) {
                for (Component month : this.calendar.getComponents()) {
                    if (LocalDate.parse(month.getName()).getMonth().equals(mDate.getMonth())) {
                        for (Component day : ((JPanel) ((JPanel) month).getComponent(2)).getComponents()) {
                            if (LocalDate.parse(day.getName())
                                         .getDayOfMonth() == mDate.getDayOfMonth() && LocalDate.parse(day.getName())
                                                                                               .getMonth()
                                                                                               .equals(LocalDate.parse(
                                                                                                   month.getName()).
                                                                                                                    getMonth())) {

                                if(m.getLabels().isEmpty()) {
                                    day.setForeground(ColorScheme.BLACK_FONT);
                                    day.setBackground(ColorScheme.GREY_WHITER_DARKER);
                                    for (MouseListener l : day.getMouseListeners())
                                        day.removeMouseListener(l);
                                    day.addMouseListener(new MeetingController(m.getID()));
                                    continue;
                                }
                                for (Meeting.Label l : labels) {
                                    if (l.isActive() && m.getLabels().contains(l)) {
                                        day.setForeground(ColorScheme.BACKGROUND);
                                        day.setBackground(l.getColor());
                                        for (MouseListener li : day.getMouseListeners())
                                            day.removeMouseListener(li);
                                        day.addMouseListener(new MeetingController(m.getID()));
                                        break;
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        this.calendar.validate();
        this.calendar.repaint();
    }

    @Override
    public void onLabelChange(Cache cache) {
        this.onMeetingChange(cache);
    }

    private void initYearView() {
        super.setBackground(ColorScheme.BACKGROUND);
        super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        super.add(Box.createVerticalStrut(20));
        super.add(calendar);
    }

    private void initMonths() {
        calendar.removeAll();
        calendar.setLayout(new GridLayout(3, 4, 30, 30));
        calendar.setBackground(ColorScheme.BACKGROUND);

        LocalDate d = LocalDate.of(super.getDate().getYear(), Month.JANUARY, 1);
        for (int i = 0; i < 12; i++) {
            JPanel month = getMonth(d);
            d = d.plusMonths(1);
            calendar.add(month);
        }
    }

    private JPanel getMonth(LocalDate date) {


        JPanel monthName = JPanelFactory.createFixedSizePanel(ColorScheme.ORANGE_LIGHT, ColorScheme.BACKGROUND,
            new FlowLayout(FlowLayout.CENTER), 25);

        JLabel monthLabel = new JLabel(date.getMonth().toString());
        monthLabel.setFont(monthLabel.getFont().deriveFont(Font.BOLD, 12));
        monthLabel.setForeground(ColorScheme.BACKGROUND);
        monthName.add(monthLabel);

        JPanel weekdays = JPanelFactory.createFixedSizePanel(30);
        weekdays.setLayout(new GridLayout(1, 7));
        weekdays.add(JLabelFactory.createLabel("Mon", SwingConstants.CENTER));
        weekdays.add(JLabelFactory.createLabel("Tue", SwingConstants.CENTER));
        weekdays.add(JLabelFactory.createLabel("Wed", SwingConstants.CENTER));
        weekdays.add(JLabelFactory.createLabel("Thu", SwingConstants.CENTER));
        weekdays.add(JLabelFactory.createLabel("Fri", SwingConstants.CENTER));
        weekdays.add(JLabelFactory.createLabel("Sat", SwingConstants.CENTER));
        weekdays.add(JLabelFactory.createLabel("Sun", SwingConstants.CENTER));

        JPanel days = new JPanel(new GridLayout(6, 7, 10, 6));
        days.setBackground(ColorScheme.BACKGROUND);

        LocalDate firstDayInMonth = date.withDayOfMonth(1);
        LocalDate first = firstDayInMonth.minusDays(firstDayInMonth.getDayOfWeek().getValue() - 1);

        int i = 0;

        for (LocalDate d = first; i < 42; d = d.plusDays(1), i++) {
            Color color = null;
            JLabel dayLabel = new JLabel(String.valueOf(d.getDayOfMonth()), SwingConstants.CENTER);
            dayLabel.setOpaque(true);
            dayLabel.setBackground(ColorScheme.BACKGROUND);
            dayLabel.setName(d.toString());
            dayLabel.addMouseListener(new DayController());
            if (d.getMonth().equals(date.getMonth())) {
                color = ColorScheme.BLACK_FONT;
            } else {
                color = ColorScheme.GREY_LINE;
            }
            dayLabel.setForeground(color);
            days.add(dayLabel);
        }

        JPanel month = JPanelFactory.createPanel();
        month.setName(date.toString());
        month.setLayout(new BoxLayout(month, BoxLayout.Y_AXIS));
        month.add(monthName);
        month.add(weekdays);
        month.add(days);
        return month;
    }


    private class DayController extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            JLabel panel = (JLabel) e.getSource();
            LocalDate mDate = LocalDate.parse(panel.getName());
            Meeting meeting = new Meeting("Untitled Meeting", mDate.atStartOfDay().toInstant(ZoneOffset.UTC),
                mDate.atStartOfDay().toInstant(ZoneOffset.UTC), "Enter description",
                "Enter location", new HashSet<>());
            getCreateDialog().setMeeting(meeting);
            getCreateDialog().setVisible(true);
        }
    }

    private class MeetingController extends MouseAdapter {

        int ID;

        MeetingController(int id) {
            this.ID = id;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            JLabel label = (JLabel) e.getSource();
            Optional<InsertedMeeting> meeting = getCache().produceMeetings()
                                                  .stream()
                                                  .filter(m -> m.getID() == ID)
                                                  .findFirst();

            if (meeting.isPresent()) {
                JLabel source = (JLabel) e.getSource();
                LocalDate date = LocalDate.parse(source.getName());
                getCreateDialog().setMeeting(meeting.get());
                getCreateDialog().setVisible(true);
            }
        }

    }

}
