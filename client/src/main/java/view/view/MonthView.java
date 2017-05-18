package view.view;

import com.InsertedMeeting;
import com.Meeting;
import com.MeetingService;
import model.Cache;
import view.ColorScheme;
import view.comp.JPanelFactory;
import view.dialog.ModifyMeetingDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@SuppressWarnings("all")
public class MonthView extends View {

    private JPanel calendar = new JPanel();
    private JLabel dateLabel = new JLabel();

    public MonthView(MeetingService meetingService, Cache cache) {
        super(meetingService, cache);

        cache.addCacheListener(this);
        this.onMeetingChange(cache);
        this.initMonthView();
    }

    private void initMonthView() {
        super.setBackground(ColorScheme.BACKGROUND);
        super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        super.add(this.getWeekdaysPanel());
        super.add(Box.createVerticalStrut(10));

        this.calendar.setLayout(new GridLayout(6, 7, 1, 1));
        this.calendar.setBackground(ColorScheme.GREY_LINE);

        super.add(this.calendar);
    }

    private JPanel getWeekdaysPanel() {

        JPanel weekdays = JPanelFactory.createFixedSizePanel(new GridLayout(1, 7));
        weekdays.add(new JLabel("Monday"));
        weekdays.add(new JLabel("Tuesday"));
        weekdays.add(new JLabel("Wednesday"));
        weekdays.add(new JLabel("Thursday"));
        weekdays.add(new JLabel("Friday"));
        weekdays.add(new JLabel("Saturday"));
        weekdays.add(new JLabel("Sunday"));
        return weekdays;
    }

    private void initView() {
        this.calendar.removeAll();

        LocalDate firstDayInMonth = super.getDate().withDayOfMonth(1);
        LocalDate first = firstDayInMonth.minusDays(firstDayInMonth.getDayOfWeek().getValue() - 1);

        int i = 0;

        for (LocalDate d = first; i < 42; d = d.plusDays(1), i++) {
            JPanel panel = new JPanel();
            panel.setName(d.toString());
            Color color = null;
            if (d.getMonth().equals(super.getDate().getMonth())) {
                color = ColorScheme.BACKGROUND;
                panel.setLayout(new BorderLayout());
                panel.addMouseListener(new DayController());
                JLabel dayLabel = new JLabel(String.valueOf(d.getDayOfMonth()));
                dayLabel.setForeground(ColorScheme.GREY_LINE_DARKER);
                dayLabel.setBorder(BorderFactory.createEmptyBorder(5, 2, 2, 5));
                dayLabel.setBackground(ColorScheme.BACKGROUND);
                dayLabel.setOpaque(true);
                panel.add(dayLabel, BorderLayout.PAGE_START);
                panel.add(JPanelFactory.createPanel(new FlowLayout(FlowLayout.LEFT, 2, 2)), BorderLayout.CENTER);
            } else {
                color = ColorScheme.GREY_WHITER;
            }
            panel.setBackground(color);

            this.calendar.add(panel);
        }
    }

    private JPanel getMonthDay(LocalDate date) {
        for (Component day : this.calendar.getComponents()) {
            LocalDate d = LocalDate.parse(day.getName());
            if (d.getMonth().equals(super.getDate().getMonth()) && d.getDayOfMonth() == date.getDayOfMonth()) {
                return (JPanel) ((JPanel) day).getComponent(1);
            }
        }
        return null;
    }

    @Override
    public void onMeetingChange(Cache cache) {

        Collection<InsertedMeeting> meetings = cache.produceMeetings();
        Collection<InsertedMeeting.Label> labels = cache.produceLabels();

        this.initView();
        for (InsertedMeeting m : meetings) {
            LocalDate mDate = LocalDateTime.ofInstant(m.getStart(), ZoneOffset.UTC).toLocalDate();
            if (mDate.getMonth().equals(super.getDate().getMonth())) {
                if (m.getLabels().isEmpty()) {
                    JLabel label = new JLabel(m.getTitle());
                    label.setOpaque(true);
                    label.setName(String.valueOf(m.getID()));
                    label.addMouseListener(new MeetingController());
                    label.setForeground(ColorScheme.BLACK_FONT);
                    label.setBackground(ColorScheme.GREY_WHITER_DARKER);
                    label.setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
                    this.getMonthDay(mDate).add(label);
                    continue;
                }
                for (Meeting.Label l : labels) {
                    if (l.isActive() && m.getLabels().contains(l)) {
                        JLabel label = new JLabel(m.getTitle());
                        label.setOpaque(true);
                        label.setName(String.valueOf(m.getID()));
                        label.addMouseListener(new MeetingController());
                        label.setForeground(ColorScheme.BACKGROUND);
                        label.setBackground(l.getColor());
                        label.setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
                        this.getMonthDay(mDate).add(label);
                        break;
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

    private class DayController extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            JPanel panel = (JPanel) e.getSource();
            LocalDate mDate = LocalDate.parse(panel.getName());
            LocalDateTime dateTime = LocalDateTime.of(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(), 8, 0);
            Meeting meeting = new Meeting("Untitled Meeting", dateTime.toInstant(ZoneOffset.UTC),
                dateTime.toInstant(ZoneOffset.UTC), "Enter description", "Enter location", new HashSet<>());
            getCreateDialog().setMeeting(meeting);
            getCreateDialog().setVisible(true);
        }
    }

    private class MeetingController extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            JLabel label = (JLabel) e.getSource();
            Optional<InsertedMeeting> meeting = getCache().produceMeetings()
                                                  .stream()
                                                  .filter(m -> m.getID() == Integer.valueOf(label.getName()))
                                                  .findFirst();

            if (meeting.isPresent()) {
                JLabel source = (JLabel) e.getSource();
                LocalDate date = LocalDate.parse(source.getParent().getParent().getName());
                getModifyDialog().setMeeting(meeting.get());
                getModifyDialog().setVisible(true);
            }
        }

    }

}
