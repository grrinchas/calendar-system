package view.comp;

import view.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.time.LocalTime;

@SuppressWarnings("all")
public class TimeChooser extends JPanel {

  public static void main(String[] args) {
    JFrame f = new JFrame();

    f.add(new TimeChooser());
    f.getContentPane().setBackground(ColorScheme.BACKGROUND);
    f.setSize(100, 100);
    f.setResizable(false);
    f.setVisible(true);
  }

  private JLabel hours = JLabelFactory.createButton("8");
  private JLabel min = JLabelFactory.createButton("0");

  public TimeChooser() {
    super.setPreferredSize(new Dimension(100, 30));
    super.setLayout(new GridLayout(1, 3));
    super.setBackground(ColorScheme.BACKGROUND);
    hours.addMouseWheelListener(new HoursButton());
    min.addMouseWheelListener(new MinButton());
    super.add(this.hours);
    super.add(this.min);
  }

  public void setTime(LocalTime time) {
    hours.setText(String.valueOf(time.getHour()));
    min.setText(String.valueOf(time.getMinute()));
  }

  public LocalTime getTime() {
    int h = Integer.valueOf(hours.getText());
    int m = Integer.valueOf(min.getText());

    return LocalTime.of(h, m);
  }

  private class MinButton implements MouseWheelListener {

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
      int notch = e.getWheelRotation();
      String text = min.getText();
      if (notch > 0) {
        int h = notch + Integer.valueOf(text);
        text = h > 59 ? String.valueOf(59) : String.valueOf(Math.abs(h));
      } else if (notch < 0) {
        int h = notch + Integer.valueOf(text);
        text = h < 0 ? String.valueOf(0) : String.valueOf(Math.abs(h));
      }
      min.setText(text);
    }
  }

  private class HoursButton implements MouseWheelListener {

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
      int notch = e.getWheelRotation();
      String text = hours.getText();
      if (notch > 0) {
        int h = notch + Integer.valueOf(text);
        text = h > 23 ? String.valueOf(23) : String.valueOf(Math.abs(h));
      } else if (notch < 0) {
        int h = notch + Integer.valueOf(text);
        text = h < 0 ? String.valueOf(0) : String.valueOf(Math.abs(h));
      }
      hours.setText(text);
    }
  }

}
