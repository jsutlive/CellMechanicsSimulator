package Renderer.UIElements.Panels;

import Framework.Object.Entity;
import Framework.States.State;
import Input.SelectionEvents;

import javax.swing.*;
import java.awt.*;

public class HierarchyPanel {

    private JPanel panel;

    public JPanel getPanel(){
        return panel;
    }

    public HierarchyPanel(){
        panel = new JPanel(new GridLayout(0, 1, 5, 5));
        Entity.onAddEntity.subscribe(this::addEntityLabel);

    }

    public void addEntityLabel(Entity entity) {
        JButton button = new JButton(entity.name);
        button.addActionListener(e -> SelectionEvents.selectEntity(entity));
        panel.add(button);
    }
}
