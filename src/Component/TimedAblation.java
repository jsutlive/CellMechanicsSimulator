package Component;

import Framework.Events.EventHandler;
import Framework.Object.Annotations.DoNotDestroyInGUI;
import Utilities.Geometry.Vector.Vector2f;

import java.util.ArrayList;
import java.util.List;


public class TimedAblation extends Component {
    public float timeUntilAblation = 5f;
    private boolean hasActed = false;
    private List<Component> targets = new ArrayList<>();

    @Override
    public void awake() {
        for(Component c: parent.getComponents()){
            if(Force.class.isAssignableFrom(c.getClass())){
                targets.add(c);
            }
        }
    }

    @Override
    public void update() {
        if(hasActed) return;


    }


}
