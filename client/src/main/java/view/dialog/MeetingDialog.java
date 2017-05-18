package view.dialog;

import com.InsertedMeeting;
import com.Meeting;
import com.MeetingService;
import model.Cache;
import view.comp.JLabelFactory;
import view.comp.JPanelFactory;
import view.comp.JTextFactory;
import view.comp.TimeChooser;
import view.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;

@SuppressWarnings("all")
public abstract class MeetingDialog<T extends Meeting> extends JDialog {

    protected static final int WIDTH = 400;
    protected static final int HEIGHT = 600;
    protected static final int PADDING = 20;
    protected static final int TITLE_SIZE = 40;
    protected static final int TITLE_FONT_SIZE = 20;

    protected static final DateTimeFormatter LABEL_DATE = DateTimeFormatter.ofPattern("dd MMM uuuu");

    protected JTextField titleField;
    protected JTextField locationField;
    protected JTextArea descriptionArea;
    protected TimeChooser startTimeChooser;
    protected TimeChooser finishTimeChooser;
    protected JPanel centerPanel;
    protected JLabel dateLabel;
    protected JLabel labelButton;
    protected Cache cache;
    protected MeetingService meetingService;
    protected LabelListDialog labelListDialog;

    private T meeting;

    public MeetingDialog(MeetingService meetingService, Cache cache) {
        this.meetingService = meetingService;
        this.cache = cache;
        this.createUI();
        this.createCenterPanel();

    }

    private void createUI() {
        this.titleField = JTextFactory.createTextField(TITLE_SIZE, TITLE_SIZE, TITLE_FONT_SIZE);
        this.locationField = JTextFactory.createTextField();
        this.descriptionArea = JTextFactory.createTextArea();
        this.startTimeChooser = new TimeChooser();
        this.finishTimeChooser = new TimeChooser();
        this.centerPanel = new JPanel();
        this.dateLabel = JLabelFactory.createLabel("", ColorScheme.PINK_DARK, SwingConstants.CENTER, TITLE_FONT_SIZE);
        this.labelListDialog = new LabelListDialog();
    }

    private void createCenterPanel() {
        this.centerPanel = JPanelFactory.createFixedSizePanel(WIDTH, HEIGHT, PADDING);
        this.centerPanel.setLayout(new BoxLayout(this.centerPanel, BoxLayout.PAGE_AXIS));

        JPanel p = JPanelFactory.createPanel(new FlowLayout(FlowLayout.CENTER));
        p.add(dateLabel);
        this.centerPanel.add(p);
        this.centerPanel.add(Box.createVerticalStrut(PADDING));
        this.centerPanel.add(this.titleField);
        this.centerPanel.add(Box.createVerticalStrut(30));
        this.centerPanel.add(this.getTimeChooser());
        this.centerPanel.add(Box.createVerticalStrut(10));

        JPanel labelPanel = JPanelFactory.createFixedSizePanel(new GridLayout(1, 2));
        labelPanel.add(new JLabel("Assign labels: "));
        labelButton = JLabelFactory.createButton("Labels");
        labelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                labelListDialog.display();
            }
        });

        labelPanel.add(labelButton);
        this.centerPanel.add(labelPanel);

        this.centerPanel.add(Box.createVerticalStrut(PADDING));
        this.centerPanel.add(this.locationField);
        this.centerPanel.add(Box.createVerticalStrut(10));
        this.centerPanel.add(this.descriptionArea);
        this.centerPanel.add(Box.createVerticalStrut(PADDING));

        // add main panel to the dialog
        super.setLayout(new BorderLayout());
        super.add(this.centerPanel, BorderLayout.CENTER);
        super.setModalityType(ModalityType.APPLICATION_MODAL);
        super.setSize(new Dimension(WIDTH, HEIGHT));
        super.setResizable(false);
    }

    private JPanel getTimeChooser() {
        JPanel panel = JPanelFactory.createPanel(new GridLayout(2, 2, 0, 10));
        panel.add(new JLabel("Start time:"));
        panel.add(this.startTimeChooser);
        panel.add(new JLabel("Finish time:"));
        panel.add(this.finishTimeChooser);
        return panel;
    }

    public void setMeeting(T meeting) {
        this.meeting = meeting;
        dateLabel.setText(toLocalDate(meeting.getStart()).format(LABEL_DATE));
        titleField.setText(meeting.getTitle());
        locationField.setText(meeting.getLocation());
        descriptionArea.setText(meeting.getDescription());
        startTimeChooser.setTime(toLocalTime(meeting.getStart()));
        finishTimeChooser.setTime(toLocalTime(meeting.getEnd()));
    }

    public T getMeeting() {
        meeting.setTitle(titleField.getText());
        meeting.setStart(adjustTime(meeting.getStart(), startTimeChooser.getTime()));
        meeting.setEnd(adjustTime(meeting.getEnd(), finishTimeChooser.getTime()));
        meeting.setLocation(locationField.getText());
        meeting.setDescription(descriptionArea.getText());
        return meeting;
    }

    private LocalDate toLocalDate(Instant instant) {
        return LocalDate.from(LocalDateTime.ofInstant(instant, ZoneOffset.UTC));
    }

    private LocalTime toLocalTime(Instant instant) {
        return LocalTime.from(LocalDateTime.ofInstant(instant, ZoneOffset.UTC));
    }

    private Instant adjustTime(Instant instant, LocalTime time) {
        LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getHour(), time.getMinute())
                            .toInstant(ZoneOffset.UTC);
    }

    private class LabelListDialog extends JDialog {
        JPanel centerPanel;

        private LabelListDialog() {
            this.centerPanel = JPanelFactory.createPanel();
            this.centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
            this.centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            super.add(this.centerPanel);
            super.setModalityType(ModalityType.APPLICATION_MODAL);
            super.setTitle("Labels");

        }

        void display() {
            this.centerPanel.removeAll();
            Collection<InsertedMeeting.Label> labels = cache.produceLabels();
            for (InsertedMeeting.Label label : labels) {
                JPanel mPanel = JPanelFactory.createFixedSizePanel(new BorderLayout());
                mPanel.setName(meeting.getLabels().contains(label) ? "selected" : "");

                JLabel name = JLabelFactory.createLabel("  " + label.getName());
                name.setName(String.valueOf(label.getID()));
                name.setOpaque(true);
                name.setBackground(meeting.getLabels().contains(label) ? label.getColor() : ColorScheme.BACKGROUND);
                name.setForeground(
                    meeting.getLabels().contains(label) ? ColorScheme.BACKGROUND : ColorScheme.BLACK_FONT);
                name.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorScheme.GREY_LINE));

                mPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        Component l = ((Component) e.getSource());
                        if (l.getName().equals("selected")) {
                            l.setName("");
                            name.setBackground(ColorScheme.BACKGROUND);
                            name.setForeground(ColorScheme.BLACK_FONT);
                        } else {
                            l.setName("selected");
                            name.setBackground(label.getColor());
                            name.setForeground(ColorScheme.BACKGROUND);
                        }
                    }
                });

                mPanel.add(name, BorderLayout.CENTER);

                this.centerPanel.add(mPanel);
                this.centerPanel.add(Box.createVerticalStrut(5));
            }

            super.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    meeting.getLabels().clear();

                    for (Component c : centerPanel.getComponents()) {
                        if (c instanceof JPanel && c.getName().equals("selected")) {
                            int id = Integer.valueOf(((JPanel) c).getComponent(0).getName());
                            Optional<InsertedMeeting.Label> label = cache.produceLabels()
                                                                 .stream()
                                                                 .filter(l -> l.getID() == id)
                                                                 .findFirst();
                            if (label.isPresent())
                                meeting.getLabels().add(label.get());
                        }
                    }

                }
            });

            super.setMinimumSize(new Dimension(200, (int) this.centerPanel.getPreferredSize().getHeight() + 40));
            super.setVisible(true);
        }
    }
}
