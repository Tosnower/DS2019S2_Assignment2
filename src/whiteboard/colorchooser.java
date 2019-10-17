package whiteboard;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class colorchooser extends AbstractColorChooserPanel {
    public void buildChooser() {
        setLayout(new GridLayout(3, 3));
        makeAddButton("Red", Color.red);
        makeAddButton("Green", Color.green);
        makeAddButton("Blue", Color.blue);
        makeAddButton("yellow", Color.yellow);
        makeAddButton("cyan", Color.cyan);
        makeAddButton("pink", Color.pink);
        makeAddButton("orange", Color.orange);
        makeAddButton("cyan",Color.cyan);
        makeAddButton("magenta",Color.magenta);
        makeAddButton("black", Color.black);
    }

    public void updateChooser() {
    }

    public String getDisplayName() {
        return "boardcolor";
    }

    public Icon getSmallDisplayIcon() {
        return null;
    }
    public Icon getLargeDisplayIcon() {
        return null;
    }
    private void makeAddButton(String name, Color color) {
        JButton button = new JButton(name);
        button.setBackground(color);
        button.setAction(setColorAction);
        button.setOpaque(true);
        button.setBorderPainted(false);
        add(button);
    }

    Action setColorAction = new AbstractAction() {
        public void actionPerformed(ActionEvent evt) {
            JButton button = (JButton) evt.getSource();

            getColorSelectionModel().setSelectedColor(button.getBackground());
        }
    };
}

