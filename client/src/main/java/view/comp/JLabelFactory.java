package view.comp;

import view.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("all")
public class JLabelFactory {

    private static Color BTN_BG_COLOR = ColorScheme.GREY_WHITER;
    private static Color LBL_BG_COLOR = ColorScheme.BACKGROUND;
    private static Color FG_COLOR = ColorScheme.BLACK_FONT;
    private static Color BD_COLOR = ColorScheme.GREY_LINE;
    private static Color HV_COLOR = ColorScheme.GREY_WHITER_DARKER;
    private static Font FONT = new JTextField().getFont();
    private static int WIDTH = 100;
    private static int HEIGHT = 30;

    public JLabelFactory() {}

    public static JLabel createLabel(String label, int c) {
        return getLabel(label, LBL_BG_COLOR, FG_COLOR, c, FONT);
    }

    public static JLabel createLabel(String label, int width, int height) {
        JLabel l = getLabel(label, LBL_BG_COLOR, FG_COLOR, SwingConstants.LEFT, FONT);
        l.setPreferredSize(new Dimension(width, height));
        return l;
    }

    public static JLabel createLabel(String label) {
        return getLabel(label, LBL_BG_COLOR, FG_COLOR, SwingConstants.LEFT, FONT);
    }

    public static JLabel createLabel(String label, Color bg, Color fg, int c, int fs) {
        return getLabel(label, bg, fg, c, FONT.deriveFont(Font.BOLD, fs));
    }

    public static JLabel createLabel(String label, Color fg, int c, int fs) {
        return getLabel(label, LBL_BG_COLOR, fg, c, FONT.deriveFont(Font.BOLD, fs));
    }

    public static JLabel createButton(String label) {
        return getButton(label, BTN_BG_COLOR, FG_COLOR, HV_COLOR, BD_COLOR, WIDTH, HEIGHT);
    }

    public static JLabel createButton(String label, int width) {
        return getButton(label, BTN_BG_COLOR, FG_COLOR, HV_COLOR, BD_COLOR, width, HEIGHT);
    }

    public static JLabel createButton(String label, int width, int height) {
        return getButton(label, BTN_BG_COLOR, FG_COLOR, HV_COLOR, BD_COLOR, width, height);
    }

    public static JLabel createButton(String lbl, Color bg, Color fg, Color hv, Color bd) {
        return getButton(lbl, bg, fg, hv, bd, WIDTH, HEIGHT);
    }

    public static JLabel createButton(String lbl, Color bg, Color fg, Color hv, Color bd, int w, int h) {
        return getButton(lbl, bg, fg, hv, bd, w, h);
    }

    public static JLabel createOrangeButton(String lbl) {
        return JLabelFactory.createButton(lbl, ColorScheme.ORANGE_LIGHT, ColorScheme.BACKGROUND,
            ColorScheme.ORANGE_DARK, ColorScheme.ORANGE_DARK);
    }

    public static JLabel createPinkButton(String lbl) {
        return JLabelFactory.createButton(lbl, ColorScheme.PINK_LIGHT, ColorScheme.BACKGROUND, ColorScheme.PINK_DARK,
            ColorScheme.PINK_DARK);
    }

    private static JLabel getLabel(String lbl, Color bg, Color fg, int c, Font font) {
        JLabel label = new JLabel(lbl, c);
        label.setOpaque(true);
        label.setForeground(fg);
        label.setBackground(bg);
        label.setFont(font);
        return label;
    }

    private static JLabel getButton(String t, Color bg, Color fg, Color hv, Color bd, int w, int h) {
        JLabel label = new JLabel(t, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(w, h));
        label.setForeground(fg);
        label.setBackground(bg);
        label.setBorder(BorderFactory.createLineBorder(bd, 1));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                label.setBackground(hv);
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                label.setBackground(bg);
                label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        return label;
    }
}
