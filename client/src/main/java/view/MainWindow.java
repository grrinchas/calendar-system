package view;

import com.LabelService;
import com.MeetingService;
import model.Cache;
import view.comp.JLabelFactory;
import view.comp.JPanelFactory;
import view.view.MonthView;
import view.view.YearView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("all")
public class MainWindow extends JFrame {

    private static final Dimension SIZE = new Dimension(1000, 800);
    private static final Dimension MIN_SIZE = new Dimension(1000, 800);

    private static final String MONTH = "month";
    private static final String YEAR = "year";

    private MonthView monthView;
    private YearView yearView;

    private CardLayout layout = new CardLayout();
    private JPanel headerPanel = JPanelFactory.createPanel();
    private JPanel sidebar;
    private JPanel viewDeck = JPanelFactory.createPanel(layout);
    private LocalDate date = LocalDate.now();
    private JLabel dateLabel = new JLabel();
    private JPanel currentView;

    public MainWindow(MeetingService meetingService, Cache cache, LabelService labelService) {
        this.monthView = new MonthView(meetingService, cache);
        this.yearView = new YearView(meetingService, cache);
        this.sidebar = new Sidebar(labelService, cache);

        this.initHeaderPanel();
        this.initViewDeck();
        this.initJFrame();
    }

    private void initJFrame() {
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.getContentPane().setLayout(new BorderLayout());
        super.getContentPane().add(this.headerPanel, BorderLayout.PAGE_START);
        super.getContentPane().add(this.viewDeck, BorderLayout.CENTER);
        super.getContentPane().add(this.sidebar, BorderLayout.LINE_START);
        super.getContentPane().setPreferredSize(SIZE);
        super.setMinimumSize(MIN_SIZE);
        super.pack();
    }

    private void initViewDeck() {
        viewDeck.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        monthView.setName(MONTH);
        yearView.setName(YEAR);

        viewDeck.add(monthView, MONTH);
        viewDeck.add(yearView, YEAR);

        currentView = monthView;
        layout.show(viewDeck, currentView.getName());
    }

    private void initHeaderPanel() {
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20,20));
        headerPanel.setLayout(new GridLayout(1, 2));
        JPanel buttonsPanel = JPanelFactory.createFixedSizePanel();
        JLabel today = JLabelFactory.createButton("Today");
        today.addMouseListener(new TemporalController(0));

        JLabel previous = JLabelFactory.createButton("<", 40);
        previous.addMouseListener(new TemporalController(1));

        JLabel next = JLabelFactory.createButton(">", 40);
        next.addMouseListener(new TemporalController(2));

        JLabel month = JLabelFactory.createButton("Month");
        month.addMouseListener(new ViewController(monthView));

        JLabel year = JLabelFactory.createButton("Year");
        year.addMouseListener(new ViewController(yearView));

        dateLabel = new JLabel(date.format(DateTimeFormatter.ofPattern("MMM uuuu")));
        dateLabel.setForeground(ColorScheme.BLACK_FONT);

        JPanel tempPanel = JPanelFactory.createPanel();
        tempPanel.add(today);
        tempPanel.add(Box.createHorizontalStrut(40));
        tempPanel.add(previous);
        tempPanel.add(next);
        tempPanel.add(Box.createHorizontalStrut(40));
        tempPanel.add(dateLabel);
        JPanel viewPanel = JPanelFactory.createPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        viewPanel.add(month);
        viewPanel.add(year);

        this.headerPanel.add(tempPanel);
        this.headerPanel.add(viewPanel);
    }

    private class ViewController extends MouseAdapter {

        private JPanel view;

        public ViewController(JPanel view) {
            this.view = view;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            currentView = view;
            layout.show(viewDeck, currentView.getName());
            if (currentView == monthView)
                dateLabel.setText(date.format(DateTimeFormatter.ofPattern("MMM uuuu")));
            else if (currentView == yearView)
                dateLabel.setText(date.format(DateTimeFormatter.ofPattern("uuuu")));

        }
    }



    private class TemporalController extends MouseAdapter {

        private int action;

        TemporalController(int action) {
            this.action = action;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (action == 0)
                nowDate();
            else if (action == 1)
                minusDate();
            else
                plusDate();
        }

        private void nowDate() {
            date = LocalDate.now();
            if (currentView == monthView) {
                monthView.setDate(date);
                dateLabel.setText(date.format(DateTimeFormatter.ofPattern("MMM uuuu")));
            } else if (currentView == yearView) {
                yearView.setDate(date);
                dateLabel.setText(date.format(DateTimeFormatter.ofPattern("uuuu")));
            }
        }

        private void minusDate() {
            if (currentView == monthView) {
                date = date.minusMonths(1);
                monthView.setDate(date);
                dateLabel.setText(date.format(DateTimeFormatter.ofPattern("MMM uuuu")));
            } else if (currentView == yearView) {
                date = date.minusYears(1);
                yearView.setDate(date);
                dateLabel.setText(date.format(DateTimeFormatter.ofPattern("uuuu")));
            }

        }

        private void plusDate() {
            if (currentView == monthView) {
                date = date.plusMonths(1);
                monthView.setDate(date);
                dateLabel.setText(date.format(DateTimeFormatter.ofPattern("MMM uuuu")));
            } else if (currentView == yearView) {
                date = date.plusYears(1);
                yearView.setDate(date);
                dateLabel.setText(date.format(DateTimeFormatter.ofPattern("uuuu")));
            }
        }
    }

}
