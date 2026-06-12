package magic.transparent_gui_background.gui.api;

import net.minecraft.client.gui.GuiGraphics;

public interface PanoramaRendererExtended {
  /**
   * Renders the panorama on the screen
   *
   * @param delta is the current delta
   * @param alpha is the alpha of the panorama
   */
  
  void TGB$renderPanorama(GuiGraphics graphics, int width, int height, float delta, float alpha);
}