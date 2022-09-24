package Model.Components.Meshing;

import Data.LogData;
import Physics.Rigidbodies.*;
import Utilities.Geometry.Vector2f;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * CellMesh retains the mesh information about a given cell, in particular its nodes, edges, and boundaries.
 * Can be used to determine collision boundaries, etc.
 */
@LogData
public class CellMesh extends Mesh{

    transient int lateralResolution = 4;
    transient int apicalResolution = 1;


    @Override
    public void start() {
        calculateArea();
        restingArea = area;
    }

    @Override
    public void earlyUpdate() {
        for(Node n: nodes) n.resetResultantForce();
    }

    @Override
    public void lateUpdate() {
        for (Node n : nodes) {
            n.Move();
        }
        calculateArea();
    }

    public boolean collidesWithNode(Node n){
        //checks whether point is inside polygon by drawing a horizontal ray from the point
        //if the num of intersections is even, then it is outside, else it is inside
        //because if a point crosses the shape a total of a even amount of times, then it
        // must have entered inside then exited again.
        Vector2f nodePos = n.getPosition().copy();
        int intersections = 0;
        for(Edge edge: edges){
            Vector2f[] positions = edge.getPositions();
            Vector2f p1 = positions[0].sub(nodePos);
            Vector2f p2 = positions[1].sub(nodePos);

            //if they are both on same side of the y-axis, it doesn't intersect
            if(Math.signum(p1.y) == Math.signum(p2.y)){continue;}
            //intersection point (not the actual point, which would contain division, but changed in a way
            // that it should still preserve sign)
            float intersectPoint = (p1.y * (p1.x - p2.x) - p1.x * (p1.y - p2.y)) * (p1.y - p2.y);

            if(intersectPoint < 0){continue;}

            intersections++;
        }
        return intersections%2 != 0;
    }

    public CellMesh build(List<Node> builderNodes){
        return build(builderNodes, apicalResolution, lateralResolution);
    }

    public CellMesh build(List<Node> builderNodes, int xRes, int yRes){
        // Start from top left, move along til end of lateral resolution
        nodes = builderNodes;
        int nodeCount = 0;
        while (nodeCount < yRes){
            nodeCount++;
            Edge e = new LateralEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
            e.setNodesReference(nodeCount-1, nodeCount);
            edges.add(e);
        }
        while (nodeCount < yRes + xRes){
            nodeCount++;
            Edge e = new ApicalEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
            e.setNodesReference(nodeCount-1, nodeCount);
            edges.add(e);
        }
        while(nodeCount < (2*yRes) + xRes){
            nodeCount++;
            Edge e = new LateralEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
            e.setNodesReference(nodeCount-1, nodeCount);
            edges.add(e);
        }
        while (nodeCount < nodes.size()){
            nodeCount++;
            if(nodeCount == nodes.size()){
                Edge e = new BasalEdge(nodes.get(nodeCount-1), nodes.get(0));
                e.setNodesReference(nodeCount-1, 0);
                edges.add(e);
            }
            else{
                Edge e = new BasalEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
                e.setNodesReference(nodeCount-1, nodeCount);
                edges.add(e);
            }
        }
        return this;
    }
}
