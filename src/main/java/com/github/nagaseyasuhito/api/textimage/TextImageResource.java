package com.github.nagaseyasuhito.api.textimage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.github.nagaseyasuhito.BouvardiaApplication;

@Path("textimage")
public class TextImageResource {
	@GET
	@Path("{value}")
	@Produces(BouvardiaApplication.IMAGE_PNG)
	public BufferedImage build(@PathParam("value") String value) {
		Graphics calculator = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
		// calculator.setFont(new Font(null, 0, 24));
		int width = calculator.getFontMetrics().stringWidth(value);
		int height = calculator.getFontMetrics().getHeight();
		int ascent = calculator.getFontMetrics().getAscent();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		// graphics.setFont(new Font(null, 0, 24));
		graphics.setColor(Color.BLACK);
		graphics.drawString(value, 0, ascent);

		return image;
	}
}
