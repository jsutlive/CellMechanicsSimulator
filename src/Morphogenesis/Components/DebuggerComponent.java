package Morphogenesis.Components;

import Framework.Object.Component;

public class DebuggerComponent extends Component {
    int storedValue = 5;
    transient int notStored = 2;
    public int displayedValue = 3;
}
