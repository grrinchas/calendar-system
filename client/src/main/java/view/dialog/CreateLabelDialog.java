package view.dialog;

import com.LabelService;
import com.Meeting;
import view.comp.JLabelFactory;
import view.comp.JPanelFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.sql.SQLException;

@SuppressWarnings("all")
public class CreateLabelDialog extends LabelDialog<Meeting.Label> {
    public CreateLabelDialog(LabelService labelService) {
        super(labelService);
        JLabel createButton = JLabelFactory.createOrangeButton("Create");

        createButton.addMouseListener(new CreateController());

        JPanel layoutPanel = JPanelFactory.createFixedSizePanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        layoutPanel.add(createButton);
        this.centerPanel.add(layoutPanel);
    }

    private class CreateController extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            try {
                labelService.createLabel(getLabel());
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            setVisible(false);
        }
    }
}
