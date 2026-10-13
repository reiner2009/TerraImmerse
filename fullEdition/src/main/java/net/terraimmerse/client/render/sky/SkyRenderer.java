 package net.terraimmerse.client.render.sky;

import org.lwjgl.opengl.GL11;

public class SkyRenderer{
	private static SkyBoxRenderer skyBoxRenderer;
	public static SunRenderer sunRenderer;
	private static SunEffectRenderer sunEffectRenderer;
	private static float bright;
	private static float dark;
	private static float t;
	private static float brightness;
	private static float sunStep;
	public static void init(){
		skyBoxRenderer=new SkyBoxRenderer();
		skyBoxRenderer.init();
		sunRenderer=new SunRenderer();
		sunEffectRenderer=new SunEffectRenderer();
		brightness=1.0F;
		sunStep=0.5F;
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
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        sunEffectRenderer.setAlpha(calculateSunEffectAlpha(SunRenderer.sunAngle));
        sunEffectRenderer.render();
        sunRenderer.render();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
	}
	public static void tickLight(){
		SunRenderer.sunAngle+=sunStep;
		if (SunRenderer.sunAngle>=360.0F){
			SunRenderer.sunAngle=0.0F;
		}
		SunRenderer.lightAngle=(float) Math.toRadians(SunRenderer.sunAngle);
		sunRenderer.calculateSunColor(SunRenderer.sunAngle);
	} 
	public static float calculateLight(float w) {
		dark = 0.05F;
		bright = 1.0F;
		if (350.0F <= w && w <= 361.0F) {
		    t = (360.0F - w) / 20.0F;
		    return 0.5F - t * (bright - dark * 2);
		}
		if (0.0F <= w && w <= 10.0F) {
		    t = w / 20.0F;
		    return t + 0.5F;
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
	public static float calculateSunEffectAlpha(float w){
		if (357.0F <= w && w <= 359.0F){
			return (w - 357) / 2;
		}
		if ((359.0F < w && w <= 360.0F) || (0.0F <= w && w < 1.0F)){
			return 1.0F;
		}
		if (1.0F <= w && w <= 3.0F) {
		    return 1 - ((w - 1) / 2);
		}
		else {
			return 0;
		}
	}	
}
