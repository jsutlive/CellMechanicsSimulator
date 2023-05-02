package Component;

import Framework.Rigidbodies.Edge;
import Framework.Utilities.Time;
import Utilities.Geometry.Vector.Vector;

import static Framework.States.StateMachine.timer;
import static Framework.Utilities.Time.asNanoseconds;


public class LateralShorteningSpringForce extends SpringForce {

    public float onsetTime;
    public float rampTime;

    @Override
    public void awake() {

        RingCellMesh mesh = parent.getComponent(RingCellMesh.class);
        assert mesh != null;
        int size = mesh.nodes.size();
        for(int i = 0; i < size; i++){
            if(i != mesh.lateralResolution || i != size-1){
                edges.add(mesh.edges.get(i));
            }
        }
        targetLengthRatio = 0.7f;
        constant = 3f;
    }

    @Override
    public void update() {
        if(timer.elapsedTime < asNanoseconds(onsetTime)) return;
        Vector force;
        for(Edge edge: edges) {
            if (timer.elapsedTime < Time.asNanoseconds(rampTime)) {
                    force = calculateSpringForce(edge, constant * timer.elapsedTime / Time.asNanoseconds(rampTime));
            } else {
                force = calculateSpringForce(edge, constant);
            }
            edge.addConstrictionForceVector(getClass().getSimpleName(), force);
        }
    }
}
