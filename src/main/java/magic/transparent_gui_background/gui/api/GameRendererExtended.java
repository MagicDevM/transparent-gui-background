package magic.transparent_gui_background.gui.api;

public interface GameRendererExtended {
  /**
   * Renders the panorama on the screen
   *
   * @param radius is the total power of the blur
   * @param delta is the current delta
   */
  void TGB$renderBlur(float radius, float delta);
}