package view.comp;

import view.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

@SuppressWarnings("all")
public class ColorChooser extends JDialog {

    private JPanel centerPanel;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;
    static final int ROWS = 5;
    static final int COLUMNS = 5;

    private Color color = ColorScheme.ORANGE_LIGHT;

    public ColorChooser() {
        this.initCenterPanel();
        this.initColors();
        this.initColorChooser();
    }

    private void initCenterPanel() {
        this.centerPanel = JPanelFactory.createFixedSizePanel(WIDTH, HEIGHT);
        this.centerPanel.setLayout(new GridLayout(ROWS, COLUMNS, 1, 1));
    }

    private void initColorChooser() {
        super.add(this.centerPanel);
        super.setModalityType(ModalityType.APPLICATION_MODAL);
        super.setSize(new Dimension(WIDTH, HEIGHT));
        super.setResizable(false);
    }

    private void initColors() {
        Color mColor = null;

        for (int i = 0; i < ROWS; i++) {
            if (i == 0)
                mColor = ColorScheme.ORANGE_LIGHT;
            else if (i == 1)
                mColor = ColorScheme.VIOLET_LIGHT;
            else if (i == 2)
                mColor = ColorScheme.GREEN_LIGHT;
            else if (i == 3)
                mColor = ColorScheme.YELLOW_LIGHT;
            else if (i == 4)
                mColor = ColorScheme.PINK_LIGHT;

            for (int j = 0; j < COLUMNS; j++) {
                JLabel label = new JLabel();
                label.setSize(new Dimension(WIDTH / COLUMNS, HEIGHT / ROWS));
                label.setOpaque(true);
                label.addMouseListener(new ChooseController());
                int red = mColor.getRed() - 9 * j;
                int green = mColor.getGreen() - 9 * j;
                int blue = mColor.getBlue() - 9 * j;
                Color color = new Color(red, green, blue);
                label.setBackground(color);
                this.centerPanel.add(label);
            }
        }
    }

    public static void main(String[] args) {
        new ColorChooser().setVisible(true);
    }

    public Color getColor() {
        return this.color;
    }

    private class ChooseController extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            color = ((JLabel)e.getSource()).getBackground();
            setVisible(false);
        }
    }
}
