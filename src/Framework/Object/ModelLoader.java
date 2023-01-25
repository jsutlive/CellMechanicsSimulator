package Framework.Object;

import Input.SelectionEvents;
import Morphogenesis.Meshing.BoxDebugMesh;
import Morphogenesis.Meshing.HexMesh;
import Morphogenesis.Meshing.RingMesh;
import Morphogenesis.MouseSelector;
import Morphogenesis.Physics.CellGroups.ApicalGradient;
import Morphogenesis.Physics.CellGroups.LateralGradient;
import Morphogenesis.Physics.Collision.*;
import Morphogenesis.Physics.Spring.ElasticForce;
import Morphogenesis.Yolk;

import static Framework.Object.Tag.MODEL;

public class ModelLoader {

    public static Entity loadDrosophilaEmbryo(){
        Entity e = new Entity("Drosophila Embryo Ring", 0, MODEL).
                with(new RingMesh()).
                with(new ApicalGradient()).
                with(new LateralGradient()).
                with(new MeshCollider()).
                with(new RigidBoundary()).
                //with(new RingStiffness2D()).
                with(new Yolk()
                );
        SelectionEvents.selectEntity(e);
        return e;
    }

    public static Entity loadHexMesh(){
        Entity e =  new Entity("Cell Sheet", 0, MODEL).
                with(new HexMesh()).
                with(new MeshCollider()
                );
        SelectionEvents.selectEntity(e);
        return e;
    }

    public static Entity loadDebugMesh(){
        Entity e = new Entity("Box", 0, MODEL).
                with(new BoxDebugMesh().build()).
                //with(new EdgeStiffness2D()).
                with(new ElasticForce()).
                with(new MeshStiffness2D());
                //with(new CornerStiffness2D());
        SelectionEvents.selectEntity(e);
        return e;
    }
}
