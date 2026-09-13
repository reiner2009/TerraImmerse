package net.terraimmerse.client.render.sky;

import org.lwjgl.opengl.GL11;

public class SkyRenderer{
	private static SkyBoxRenderer skyBoxRenderer;
	private static SunRenderer sunRenderer;
	private static float bright;
    private static float dark;
    private static float t;
    private static float brightness;
	public static void init(){
		skyBoxRenderer=new SkyBoxRenderer();
        skyBoxRenderer.init();
        sunRenderer=new SunRenderer();
        sunRenderer.init();
        brightness=1.0F;
	}
	public static void render(){
		brightness=calculateLight(SunRenderer.sunAngle);
        GL11.glClearColor(0.2F*brightness, 0.4F*brightness, 0.8F*brightness, 1.0F);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        SkyBoxRenderer.skyR=brightness;
        SkyBoxRenderer.skyG=brightness;
        SkyBoxRenderer.skyB=brightness;
        skyBoxRenderer.render();
        sunRenderer.render();
	}
	public static void tickLight(){
        if (SunRenderer.sunAngle>=360.0F){
            SunRenderer.sunAngle=0.0F;
        }
        SunRenderer.sunAngle+=0.01F;
        SunRenderer.lightAngle=(float) Math.toRadians(SunRenderer.sunAngle);
        SunRenderer.calculateSunColor(SunRenderer.sunAngle);
    } 
	public static float calculateLight(float w) {
		dark = 0.1F;
        bright = 1.0F;
        if (w >= 370.0F){
            t = (w - 370.0F) / (390.0F - 370.0F);
            return dark + t * (bright - dark);
        }
        if (0.0F <= w && w <= 10.0F) {
            t = w / 10.0F;
            return dark + t * (bright - dark);
        }
        if (10.0F < w && w < 150.0F) {
            return bright;
        }
        if (150.0F <= w && w <= 170.0F) {
            t = (w - 150.0F) / (170.0F - 150.0F);
            return bright - t * (bright - dark);
        }
        return dark;
    }
}
