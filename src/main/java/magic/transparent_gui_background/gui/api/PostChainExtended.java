package magic.transparent_gui_background.gui.api;

import net.minecraft.client.gui.GuiGraphics;
import java.lang.String;

public interface PostChainExtended {
  /**
   * Renders the panorama on the screen
   *
   * @param type is the type of pass
   * @param value is the values for that pass
   */
  void TGB$setUniform(String type, float value);
}