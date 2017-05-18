package view.dialog;

import com.LabelService;
import com.Meeting;
import view.comp.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("all")
public class LabelDialog<T extends Meeting.Label> extends JDialog {

    protected static final int WIDTH = 270;
    protected static final int HEIGHT = 150;
    protected static final int PADDING = 20;

    protected JTextField nameField;
    protected JLabel colorButton;
    protected JPanel centerPanel;
    protected ColorChooser colorChooser = new ColorChooser();
    protected LabelService labelService;

    private T meetingLabel;

    public LabelDialog(LabelService labelService) {
        this.labelService = labelService;
        this.createUI();
        this.createCenterPanel();
    }

    private void createUI() {
        this.centerPanel = JPanelFactory.createPanel();
        this.colorButton = JLabelFactory.createLabel("", 40, 30);
        this.colorButton.addMouseListener(new ColorChooserController());
        this.nameField = JTextFactory.createTextField();
        this.nameField.setBorder(BorderFactory.createMatteBorder(0, 10, 0, 0, this.centerPanel.getBackground()));
    }


    private void createCenterPanel() {
        this.centerPanel = JPanelFactory.createFixedSizePanel(WIDTH, HEIGHT, PADDING);
        this.centerPanel.setLayout(new BoxLayout(this.centerPanel, BoxLayout.Y_AXIS));

        JPanel panel = JPanelFactory.createFixedSizePanel(new BorderLayout());

        panel.add(this.colorButton, BorderLayout.LINE_START);
        panel.add(this.nameField, BorderLayout.CENTER);
        this.centerPanel.add(panel);
        this.centerPanel.add(Box.createVerticalStrut(20));

        super.add(this.centerPanel);
        super.setTitle("Modify label");
        super.setModalityType(ModalityType.APPLICATION_MODAL);
        super.setSize(new Dimension(WIDTH, HEIGHT));
        super.setResizable(false);
    }

    public T getLabel() {
        meetingLabel.setName(nameField.getText());
        meetingLabel.setColor(colorButton.getBackground());
        return meetingLabel;
    }

    public void setLabel(T label) {
        this.meetingLabel = label;
        this.nameField.setText(label.getName());
        this.colorButton.setBackground(label.getColor());
    }

    private class ColorController extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            meetingLabel.setColor(((JLabel) e.getSource()).getBackground());
        }
    }

    private class ColorChooserController extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            colorChooser.setVisible(true);
            colorButton.setBackground(colorChooser.getColor());
        }
    }

}
