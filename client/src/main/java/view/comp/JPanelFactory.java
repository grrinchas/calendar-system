package view.comp;

import view.ColorScheme;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

@SuppressWarnings("all")
public class JPanelFactory {


    private static Color BG_COLOR = ColorScheme.BACKGROUND;
    private static Color FG_COLOR = ColorScheme.BLACK_FONT;
    private static LayoutManager FLOW_LAYOUT = new FlowLayout(FlowLayout.LEFT, 0, 0);
    private static Border EMPTY_BORDER = BorderFactory.createEmptyBorder();
    private static int HEIGHT = 30;
    private static int MAX_WIDTH = Integer.MAX_VALUE;
    private static int MAX_HEIGHT = Integer.MAX_VALUE;

    private JPanelFactory() {}

    public static JPanel createPanel(LayoutManager layout) {
        return getPanel(BG_COLOR, FG_COLOR, layout, EMPTY_BORDER);
    }

    public static JPanel createPanel() {
        return getPanel(BG_COLOR, FG_COLOR, FLOW_LAYOUT, EMPTY_BORDER);
    }

    public static JPanel createPanel(int border) {
        return getFixedSizePanel(BG_COLOR, FG_COLOR, FLOW_LAYOUT, MAX_WIDTH, HEIGHT, MAX_WIDTH, HEIGHT,
            BorderFactory.createEmptyBorder(border, border, border, border));
    }


    public static JPanel createPanel(LayoutManager layout, int border) {
        return getFixedSizePanel(BG_COLOR, FG_COLOR, layout, MAX_WIDTH, HEIGHT, MAX_WIDTH, HEIGHT,
            BorderFactory.createEmptyBorder(border, border, border, border));
    }

    public static JPanel createFixedSizePanel() {
        return getFixedSizePanel(BG_COLOR, FG_COLOR, FLOW_LAYOUT, MAX_WIDTH, HEIGHT, MAX_WIDTH, HEIGHT, EMPTY_BORDER);
    }

    public static JPanel createFixedSizePanel(int h) {
        return getFixedSizePanel(BG_COLOR, FG_COLOR, FLOW_LAYOUT, MAX_WIDTH, h, MAX_WIDTH, h, EMPTY_BORDER);
    }

    public static JPanel createFixedSizePanel(LayoutManager layout) {
        return getFixedSizePanel(BG_COLOR, FG_COLOR, layout, MAX_WIDTH, HEIGHT, MAX_WIDTH, HEIGHT, EMPTY_BORDER);
    }

    public static JPanel createFixedSizePanel(LayoutManager layout, int h) {
        return getFixedSizePanel(BG_COLOR, FG_COLOR, layout, MAX_WIDTH, h, MAX_WIDTH, h, EMPTY_BORDER);
    }

    public static JPanel createFixedSizePanel(int w, int h, int b) {
        return getFixedSizePanel(BG_COLOR, FG_COLOR, FLOW_LAYOUT, w, h, w, h,
            BorderFactory.createEmptyBorder(b, b, b, b));
    }

    public static JPanel createFixedSizePanel(Color bg, int h) {
        return getFixedSizePanel(bg, FG_COLOR, FLOW_LAYOUT, MAX_WIDTH, h, MAX_WIDTH, h, EMPTY_BORDER);
    }

    public static JPanel createFixedSizePanel(Color bg,Color fg, int h) {
        return getFixedSizePanel(bg, fg, FLOW_LAYOUT, MAX_WIDTH, h, MAX_WIDTH, h, EMPTY_BORDER);
    }

    public static JPanel createFixedSizePanel(Color bg,Color fg, LayoutManager l, int h) {
        return getFixedSizePanel(bg, fg, l, MAX_WIDTH, h, MAX_WIDTH, h, EMPTY_BORDER);
    }

    public static JPanel createFixedSizePanel(int w, int h) {
        return getFixedSizePanel(BG_COLOR, FG_COLOR, FLOW_LAYOUT, w, h, w, h, EMPTY_BORDER);
    }

    public static JPanel createFixedSizePanel(int pw, int ph, int mw, int mh) {
        return getFixedSizePanel(BG_COLOR, FG_COLOR, FLOW_LAYOUT, pw, ph, mw, mh, EMPTY_BORDER);
    }

    private static JPanel getFixedSizePanel(Color bg, Color fg, LayoutManager layout, int pw, int ph, int mw, int mh,
                                            Border border) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(bg);
        panel.setForeground(bg);
        panel.setBorder(border);
        panel.setPreferredSize(new Dimension(pw, ph));
        panel.setMaximumSize(new Dimension(mw, mh));
        return panel;
    }

    private static JPanel getPanel(Color bg, Color fg, LayoutManager layout, Border border) {

        JPanel panel = new JPanel(layout);
        panel.setBackground(bg);
        panel.setForeground(bg);
        panel.setBorder(border);
        return panel;
    }
}
