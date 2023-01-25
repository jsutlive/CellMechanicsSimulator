package Morphogenesis.Physics.Collision;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Entity;
import Morphogenesis.Meshing.Mesh;
import Framework.Rigidbodies.Edge;
import Framework.Rigidbodies.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Physics.Collision2D;

import java.util.Collections;
import java.util.List;

@DoNotDestroyInGUI
public class MeshCollider extends Collider{
    transient List<Entity> cells;
    transient List<Node2D> nodes;

    @Override
    public void awake() {
        cells = getChildren();
        nodes = getComponent(Mesh.class).nodes;
    }

    @Override
    public void lateUpdate() {
        Collections.shuffle(cells);
        checkCollision();
    }

    private void checkCollision() {
        for(Entity cell: cells){
            Mesh mesh = cell.getComponent(Mesh.class);
            for(Node2D node: nodes){
                if(!mesh.contains(node) && mesh.collidesWithNode(node)) {
                    for (Edge e : mesh.edges) {
                        setNodePositionToClosestEdge(node, e);
                    }
                }
            }
        }
    }

    private void setNodePositionToClosestEdge(Node2D node, Edge e) {
        Vector2f closePoint = Collision2D.closestPointToSegmentFromPoint(node.getPosition(), e.getPositions());
        if(closePoint.isNull() || (closePoint.x == 0 && closePoint.y == 0)) return;
        if(node.getPosition().distanceTo(closePoint) > 5f) return;
        Vector2f force = closePoint.sub(node.getPosition());
        node.addForceVector("Collision", force);
    }
}
