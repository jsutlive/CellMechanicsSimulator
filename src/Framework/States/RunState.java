package Framework.States;

import Framework.Object.Entity;
import Input.InputEvents;
import java.util.Collections;

import static Input.InputPanel.onTimestepSliderChanged;

public class RunState extends State
{
    protected static float dt  = 1e-3f;
    public static float deltaTime;

    public RunState(StateMachine stateMachine) {
        super(stateMachine);
    }

    /**
     * Instantiation of entities occurs here. Each behavior will have its awake and start methods called.
     */
    @Override
    public void enter() {
        Collections.shuffle(stateMachine.allObjects);
        onTimestepSliderChanged.subscribe(this::setTimeStep);
    }

    private void setTimeStep(float f){
        dt = f;
    }

    /**
     * Physics Loop. All physics objects updated here
     */
    @Override
    public void tick() {
        deltaTime = dt;

        for (Entity obj :(stateMachine.allObjects)) {
            obj.earlyUpdate();
        }
        for (Entity obj :(stateMachine.allObjects)) {
            obj.update();
        }
        for (Entity obj :(stateMachine.allObjects)) {
            obj.lateUpdate();
        }
    }

    private void goToEditorState(Boolean b){
        if(b) stateMachine.changeState(new EditorState(stateMachine));
    }

    void exit()
    {
        onTimestepSliderChanged.unSubscribe(this::setTimeStep);
    }
}
