package view.dialog;

import com.Meeting;
import com.MeetingService;
import model.Cache;
import view.comp.JLabelFactory;
import view.comp.JPanelFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.sql.SQLException;

@SuppressWarnings("all")
public class CreateMeetingDialog extends MeetingDialog<Meeting> {

    private Meeting meeting;

    public CreateMeetingDialog(MeetingService meetingService, Cache cache) {
        super(meetingService, cache);
        super.setTitle("Create");

        // lets create button and add controller to it
        JLabel createButton = JLabelFactory.createOrangeButton("Create");
        createButton.addMouseListener(new CreateController());

        // add button to the main panel
        JPanel layoutPanel = JPanelFactory.createFixedSizePanel(new FlowLayout(FlowLayout.RIGHT), 35);
        layoutPanel.add(createButton);
        this.centerPanel.add(layoutPanel);
        this.centerPanel.add(Box.createVerticalStrut(PADDING));
    }

    private class CreateController extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            try {
                meetingService.createMeeting(getMeeting());
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            setVisible(false);
        }
    }

}
