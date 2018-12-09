package gui;
import java.awt.image.BufferedImage;

public class AnimatedSprite {
	private BufferedImage[] sprites;
	private int frame, num_frames, tick;
	private static final int FRAME_DELAY = 10;

	public AnimatedSprite(BufferedImage[] sprites, int num_frames) {
		this.sprites = sprites;
		this.num_frames = num_frames;
		frame = 0;
		tick = 0;
	}
	
	public BufferedImage getCurrentImage() {
		tick++;
		if(tick > FRAME_DELAY) {
			if(frame < (num_frames - 1)) {
				frame++;
			}
			else {
				frame = 0;
			}
			tick = 0;
		}
		
		return sprites[frame];
	}
}
