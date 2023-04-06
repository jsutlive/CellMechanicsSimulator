package Renderer.UIElements.Panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import static Renderer.UIElements.Panels.MultiComponentPanel.PLACEHOLDER_FIELD;


public class StaticFieldPanel {
    JPanel panel;
    public boolean isSerializable = true;
    public String name;

    public StaticFieldPanel(Class<?> type, Object value, String name ) {
        initialize(type, value, name);
    }

    private void initialize(Class<?> type, Object value, String name){
        panel = new JPanel(new GridLayout(0, 2));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(325, 30));
        this.name = name;
        panel.add(new JLabel(name));
        if (type == int.class) {
            int val;
            JLabel field;
            if(value!=PLACEHOLDER_FIELD) {
                val = (int) value;
                field = new JLabel(String.valueOf(val));
            }else{
                field = new JLabel((String)value);
            }
            field.setBackground(Color.white);
            panel.add(field);
        } else if (type == float.class) {
            float val;
            JLabel field;
            if(value!=PLACEHOLDER_FIELD) {
                val = (float) value;
                field = new JLabel(String.valueOf(val));
            }else{
                field = new JLabel((String)value);
            }
            field.setBackground(Color.white);
            panel.add(field);
        } else if (type == String.class) {
            String val = (String) value;
            JLabel field = new JLabel(val);
            field.setBackground(Color.white);
            panel.add(field);
        } else {
            isSerializable = false;
        }
    }

    public JPanel getPanel(){
        return panel;
    }
}
