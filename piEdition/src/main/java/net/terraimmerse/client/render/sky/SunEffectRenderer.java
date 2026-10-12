package net.terraimmerse.client.render.sky;

public class SunEffectRenderer extends AbstractSunEffectRenderer{
	public SunEffectRenderer(){
		super("/assets/textures/sky/sun.png", vertices);
	}
	private static float[] vertices={
		-7.0F,0.2F,0.0F,0.0F,1.0F,
		-7.0F,-0.2F,0.0F,0.0F,0.0F,
		7.0F,-0.2F,0.0F,1.0F,0.0F,
		-7.0F,0.2F,0.0F,0.0F,1.0F,
		7.0F,-0.2F,0.0F,1.0F,0.0F,
		7.0F,0.2F,0.0F,1.0F,1.0F
	};
	public void render(){
		this.setColor(1.0F, 0.55F, 0.12F);
		super.render();
	}
}
