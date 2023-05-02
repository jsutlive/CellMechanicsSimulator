package Component;

import Annotations.ReloadEntityOnChange;
import Renderer.Graphics.IColor;
import Renderer.Graphics.IRender;

import java.awt.*;

import static Renderer.Renderer.DEFAULT_COLOR;


public abstract class ObjectRenderer extends Component implements IRender, IColor {
    protected Color color = DEFAULT_COLOR;
    @ReloadEntityOnChange
    public Color defaultColor = DEFAULT_COLOR;

}