package Physics.Rigidbodies;

public class BasalEdge extends Edge{
    public BasalEdge(Node a, Node b)
    {
        MakeNewEdge(a,b);
        elasticConstant = .08f;
        isNull = false;

    }

    @Override
    public Edge clone(){
        return new BasalEdge(this.getNodes()[0].clone(), this.getNodes()[1].clone() );
    }
}
