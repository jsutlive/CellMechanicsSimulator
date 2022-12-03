package Morphogenesis.Components.Physics.Spring;

import Morphogenesis.Components.Meshing.RingCellMesh;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Edges.LateralEdge;


public class LateralShorteningSpringForce extends SpringForce {

    @Override
    public void awake() {
        RingCellMesh mesh = parent.getComponent(RingCellMesh.class);
        int size = mesh.nodes.size();
        for(int i = 0; i < size; i++){
            if(i != mesh.lateralResolution || i != size-1){
                edges.add(mesh.edges.get(i));
            }
        }
        targetLengthRatio = 0.7f;
        constant = 3f;
    }
}
