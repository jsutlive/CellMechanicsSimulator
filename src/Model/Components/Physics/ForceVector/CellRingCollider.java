package Model.Components.Physics.ForceVector;

import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Model.Components.Meshing.RingMesh;
import Model.Components.Physics.Force;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Geometry;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;
import com.sun.tools.internal.xjc.reader.Ring;

import java.util.ArrayList;
import java.util.List;

public class CellRingCollider extends Force {
    transient List<Cell> cells;
    transient List<Node> nodes;
    transient List<Node> bothRings;

    @Override
    public void awake() {
        forceVector.setType(ForceType.collision);
        bothRings = new ArrayList<>(getComponent(RingMesh.class).innerNodes);
        bothRings.addAll(getComponent(RingMesh.class).outerNodes);
        cells = getComponent(RingMesh.class).cellList;
        nodes = getComponent(RingMesh.class).nodes;
    }

    @Override
    public void update() {
        checkCollision();
    }
    private void checkCollision() {
        forceVector.set(Vector2f.zero);
        for (Cell cell : cells) {
            CellMesh mesh = cell.getComponent(CellMesh.class);
            for (Node node : nodes) {
                float minDist = Float.POSITIVE_INFINITY;
                Edge closestEdge = null;
                Vector2f nodePosition = node.getPosition();
                if (mesh.collidesWithNode(node) && !mesh.contains(node)) {
                    for (Edge e : mesh.edges) {
                        if (e.contains(node)) {
                            continue;
                        }
                        Vector2f closePoint = closestPointToSegmentFromPoint(node.getPosition(), e.getPositions());
                        float dist = Vector2f.sqDist(nodePosition, closePoint);
                        if (dist < minDist) {
                            minDist = dist;
                            closestEdge = e;
                        }
                    }
                    if (closestEdge != null) {
                        forceVector.set(CustomMath.normal(closestEdge));
                        if(forceVector.isNull()) forceVector.set(Vector2f.zero);
                        Node[] nodes = closestEdge.getNodes();
                        nodes[0].addForceVector(forceVector);
                        nodes[1].addForceVector(forceVector);
                        forceVector.mul(-2);
                        node.addForceVector(forceVector);
                    }
                }
            }
        }
    }

    /**
    * Find unit vector describing the angle of a given point from center given which segment it is and the distance
     * from the center
     * @param segment current segment
     * @param point total segments in the circle
     * @return an x,y vector2 (floating point) describing the unit vector.
    */

    public Vector2f closestPointToSegmentFromPoint(Vector2f point, Vector2f[] segment){
        Vector2f edgeVector = segment[1].copy().sub(segment[0]);
        Vector2f pointToEdgeP0 = point.copy().sub(segment[0]);

        float p0Dot = pointToEdgeP0.dot(edgeVector);
        float magnitudeSquared = edgeVector.dot(edgeVector);
        if (p0Dot <= 0){return segment[0].copy();}
        if (p0Dot > magnitudeSquared){return segment[1].copy();}

        Vector2f closestPoint = segment[0].copy();
        closestPoint.add(edgeVector.mul(p0Dot/magnitudeSquared));

        return closestPoint;
    }

}
