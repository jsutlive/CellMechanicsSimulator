package Framework.States;

import Component.BatchManager;
import Component.SaveSystem;
import Framework.Utilities.Debug;
import Framework.Events.EventHandler;
import Framework.Object.Entity;
import Framework.Object.Tag;
import Framework.Object.ModelLoader;
import Framework.Utilities.Time;
import Input.InputEvents;
import Input.SelectionEvents;
import Component.MouseSelector;
import Component.Camera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static Framework.Object.Tag.PHYSICS;
import static Framework.Object.Tag.CAMERA;

public final class StateMachine {

    public State currentState;
    public List<Entity> allObjects = new ArrayList<>();
    public static Time timer;
    public static EventHandler<String> onSaveStateInfo = new EventHandler<>();
    public static EventHandler<Boolean> onStateMachineStateChange = new EventHandler<>();

    private Entity physics;

    public StateMachine(Time referenceTime, boolean launchOnStart){
        timer = referenceTime;
        Entity.onAddEntity.subscribe(this::addEntityToList);
        Entity.onRemoveEntity.subscribe(this::removeEntityFromList);
        SelectionEvents.onTagSelected.subscribe(this::selectEntityWithTag);
        InputEvents.onToggleSimulation.subscribe(this::handleSimulationToggle);
        InputEvents.onClear.subscribe(this::clearStateMachine);
        InputEvents.onLoadModel.subscribe(this::loadModel);
        new Entity("Camera", -1, CAMERA).with(new Camera());
        physics = new Entity("Physics", -2, PHYSICS).
                with(new MouseSelector()).
                with(new BatchManager()).
                with(new SaveSystem());
        Objects.requireNonNull(physics.getComponent(MouseSelector.class)).stateMachine = this;
        Objects.requireNonNull(physics.getComponent(BatchManager.class)).stateMachine = this;
        Objects.requireNonNull(physics.getComponent(SaveSystem.class)).stateMachine = this;

        if(launchOnStart){
            changeState(new RunState(this));
        }else {
            changeState(new EditorState(this));
        }
        Debug.Log("System Loaded");
    }

    /**
     * Add entity to state and have it run its initial state
     * @param e entity added
     */
    private void addEntityToList(Entity e) {
        if(allObjects.contains(e))return;
        allObjects.add(e);
        e.awake();
        e.onValidate();
    }

    public void cycle(){
        Collections.reverse(allObjects);
    }

    private void selectEntityWithTag(Tag t){
        Entity e = currentState.findObjectWithTag(t);
        if(e!=null) SelectionEvents.selectEntity(e);
        else SelectionEvents.clearSelection();
    }

    /**
     * Remove entity to state
     * @param e entity removed
     */
    private void removeEntityFromList(Entity e){
        allObjects.remove(e);
    }

    /**
     * Call exit events in current state, move to and call enter events in new state
     * @param newState state to change to
     */
    public void changeState(State newState){
        if(currentState!=null)currentState.exit();
        currentState = newState;
        timer.reset();
        currentState.enter();
    }

    /**
     * Manage GUI events for switching between editor and simulator     *
     * @param isPlaying is true if need to go to RunState.
     */
    private void handleSimulationToggle(boolean isPlaying){
        if(Objects.requireNonNull(physics.getComponent(BatchManager.class)).useBatchTesting){
            Objects.requireNonNull(physics.getComponent(BatchManager.class)).runNextBatch(isPlaying);
            return;
        }
        if(isPlaying) {
            Debug.Log("Starting simulation");
            changeState(new RunState(this));
            onStateMachineStateChange.invoke(true);
        } else{
            Debug.Log("Simulation complete");
            changeState(new EditorState(this));
            onStateMachineStateChange.invoke(false);
        }

    }



    public void clearStateMachine(boolean keepCamera){
        SelectionEvents.clearGroups();
        SelectionEvents.clearSelection();
        for(int i = allObjects.size()-1; i>= 0; i-- ){
                if(i >= allObjects.size()) continue;
                Tag tag = allObjects.get(i).getTag();
                if(!( tag== CAMERA || tag == PHYSICS)) {
                    allObjects.get(i).destroy();
                }
        }
    }

    public void loadModel(String modelName){
        clearStateMachine(true);
        changeState(new EditorState(this));
        if(modelName.equals("Embryo")) ModelLoader.loadDrosophilaEmbryo();
        if(modelName.equals("Hexagons")) ModelLoader.loadHexMesh();
        if(modelName.equals("Debug")) ModelLoader.loadDebugMesh();
    }

    /**
     *  unsubscribe from events when application is exited.
     */
    public void deactivate(){
        Entity.onAddEntity.close();
        Entity.onRemoveEntity.close();
        InputEvents.onToggleSimulation.close();
        InputEvents.onClear.close();
        SelectionEvents.onTagSelected.close();
        for(Entity e: allObjects) e.destroy();
    }
}
