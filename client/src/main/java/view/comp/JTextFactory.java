package view.comp;

import view.ColorScheme;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

@SuppressWarnings("all")
public class JTextFactory {

    private static Color BG_COLOR = ColorScheme.GREY_WHITER;
    private static Color FG_COLOR = ColorScheme.BLACK_FONT;
    private static Color BD_COLOR = ColorScheme.GREY_WHITER_DARKER;
    private static Font FONT = new JTextField().getFont();
    private static int WIDTH = Integer.MAX_VALUE;
    private static int HEIGHT = 30;

    private JTextFactory() {}

    public static JTextField createTextField() {
        return getTextField(BG_COLOR, FG_COLOR, BD_COLOR, WIDTH, HEIGHT, WIDTH, HEIGHT, FONT);
    }

    public static JTextArea createTextArea() {
        return getTextArea(BG_COLOR, FG_COLOR, BD_COLOR, FONT);
    }

    public static JTextField createTextField(int w, int h, int fs) {
        return getTextField(BG_COLOR, FG_COLOR, BD_COLOR, WIDTH, HEIGHT, WIDTH, HEIGHT, FONT.deriveFont(Font.BOLD, fs));
    }


    public static JPasswordField createPasswordField() {
        return getPasswordField(BG_COLOR, FG_COLOR, BD_COLOR, WIDTH, HEIGHT, WIDTH, HEIGHT);
    }

    private static JTextField getTextField(Color bg, Color fg, Color bd, int pw, int ph, int mw, int mh, Font f) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(ph, ph));
        field.setMaximumSize(new Dimension(mw, mh));
        field.setBorder(new MatteBorder(1, 1, 1, 1, bd));
        field.setForeground(fg);
        field.setBackground(bg);
        field.setFont(f);
        return field;
    }

    private static JPasswordField getPasswordField(Color bg, Color fg, Color bd, int pw, int ph, int mw, int mh) {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(ph, ph));
        field.setMaximumSize(new Dimension(mw, mh));
        field.setBorder(new MatteBorder(1, 1, 1, 1, bd));
        field.setForeground(fg);
        field.setBackground(bg);
        return field;
    }

    private static JTextArea getTextArea(Color bg, Color fg, Color bd,Font f) {
        JTextArea area = new JTextArea();
        area.setBorder(new MatteBorder(1, 1, 1, 1, bd));
        area.setForeground(fg);
        area.setBackground(bg);
        area.setFont(f);
        return area;
    }
}
