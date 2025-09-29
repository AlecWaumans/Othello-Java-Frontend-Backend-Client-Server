package othello.view.javaFx;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 *
 * @author g58399
 */
public class OptionReset extends Menu {
    private final MenuItem resetItem;
    
    public OptionReset(){
        super("Reset");
        this.resetItem = new MenuItem("Reset");
        this.getItems().add(resetItem);
    }
}
