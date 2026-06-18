package com.python4d.hitcurl.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextActor extends Actor {
	private BitmapFont font;
	CharSequence str;
	SpriteBatch batch;

	public TextActor(BitmapFont font, CharSequence str) {
		this.font = font;
		setText(str.toString());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		font.setColor(getColor().r, getColor().g, getColor().b, getColor().a);
		font.draw(batch, str, this.getX(), this.getY() + this.getHeight());
	}

	@Override
	public void setScale(float scale) {
		super.setScale(1);
		font.getData().setScale(scale);
		GlyphLayout layout = new GlyphLayout(font, str);
		this.setBounds(getX(), getY(), layout.width, layout.height);
	}

	@Override
	public void setScale(float scalex, float scaley) {
		super.setScale(1);
		font.getData().setScale(scalex, scaley);
		GlyphLayout layout = new GlyphLayout(font, str);
		this.setBounds(getX(), getY(), layout.width, layout.height);
	}

	@Override
	public void scaleBy(float scaleX, float scaleY) {
		super.scaleBy(scaleX, scaleY);
		GlyphLayout oldLayout = new GlyphLayout(font, str);
		float w = oldLayout.width;
		float h = oldLayout.height;
		font.getData().scale(scaleX);
		GlyphLayout newLayout = new GlyphLayout(font, str);
		float W = newLayout.width;
		float H = newLayout.height;
		this.setBounds(getX() - (W - w) / 2f, getY() - (H - h) / 2f, W, H);
	}

	protected void setText(String string) {
		this.str = string;
		GlyphLayout layout = new GlyphLayout(font, str);
		this.setBounds(getX(), getY(), layout.width, layout.height);
	}

	public BitmapFont getFont() {
		return font;
	}

	public int getStrWidth() {
		return (int) new GlyphLayout(font, str).width;
	}

	public float getStrHeight() {
		return new GlyphLayout(font, str).height;
	}

}
