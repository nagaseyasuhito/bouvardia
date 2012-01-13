package com.docuverse.identicon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.math.BigInteger;

/**
 * 9-block Identicon renderer.
 * 
 * <p>
 * Current implementation uses only the lower 32 bits of identicon code.
 * </p>
 * 
 * @author don
 * 
 */
public class NineBlockIdenticonRenderer2 implements IdenticonRenderer {

	/*
	 * Each patch is a polygon created from a list of vertices on a 5 by 5 grid. Vertices are numbered from 0 to 24, starting from top-left corner of the grid, moving left to right and top to bottom.
	 */

	private static final int PATCH_GRIDS = 5;

	private static final float DEFAULT_PATCH_SIZE = 20.0f;

	private static final byte PATCH_SYMMETRIC = 1;

	private static final byte PATCH_INVERTED = 2;

	private static final int PATCH_MOVETO = -1;

	private static final byte[] patch0 = { 0, 4, 24, 20 };

	private static final byte[] patch1 = { 0, 4, 20 };

	private static final byte[] patch2 = { 2, 24, 20 };

	private static final byte[] patch3 = { 0, 2, 20, 22 };

	private static final byte[] patch4 = { 2, 14, 22, 10 };

	private static final byte[] patch5 = { 0, 14, 24, 22 };

	private static final byte[] patch6 = { 2, 24, 22, 13, 11, 22, 20 };

	private static final byte[] patch7 = { 0, 14, 22 };

	private static final byte[] patch8 = { 6, 8, 18, 16 };

	private static final byte[] patch9 = { 4, 20, 10, 12, 2 };

	private static final byte[] patch10 = { 0, 2, 12, 10 };

	private static final byte[] patch11 = { 10, 14, 22 };

	private static final byte[] patch12 = { 20, 12, 24 };

	private static final byte[] patch13 = { 10, 2, 12 };

	private static final byte[] patch14 = { 0, 2, 10 };

	private static final byte[] patchTypes[] = { NineBlockIdenticonRenderer2.patch0, NineBlockIdenticonRenderer2.patch1, NineBlockIdenticonRenderer2.patch2, NineBlockIdenticonRenderer2.patch3, NineBlockIdenticonRenderer2.patch4,
			NineBlockIdenticonRenderer2.patch5, NineBlockIdenticonRenderer2.patch6, NineBlockIdenticonRenderer2.patch7, NineBlockIdenticonRenderer2.patch8, NineBlockIdenticonRenderer2.patch9, NineBlockIdenticonRenderer2.patch10,
			NineBlockIdenticonRenderer2.patch11, NineBlockIdenticonRenderer2.patch12, NineBlockIdenticonRenderer2.patch13, NineBlockIdenticonRenderer2.patch14, NineBlockIdenticonRenderer2.patch0 };

	private static final byte patchFlags[] = { NineBlockIdenticonRenderer2.PATCH_SYMMETRIC, 0, 0, 0, NineBlockIdenticonRenderer2.PATCH_SYMMETRIC, 0, 0, 0, NineBlockIdenticonRenderer2.PATCH_SYMMETRIC, 0, 0, 0, 0, 0, 0,
			NineBlockIdenticonRenderer2.PATCH_SYMMETRIC + NineBlockIdenticonRenderer2.PATCH_INVERTED };

	private static int centerPatchTypes[] = { 0, 4, 8, 15 };

	private float patchSize;

	private GeneralPath[] patchShapes;

	// used to center patch shape at origin because shape rotation works
	// correctly.
	private float patchOffset;

	private Color backgroundColor = Color.WHITE;

	/**
	 * Constructor.
	 * 
	 */
	public NineBlockIdenticonRenderer2() {
		this.setPatchSize(NineBlockIdenticonRenderer2.DEFAULT_PATCH_SIZE);
	}

	/**
	 * Returns the size in pixels at which each patch will be rendered before they are scaled down to requested identicon size.
	 * 
	 * @return
	 */
	public float getPatchSize() {
		return this.patchSize;
	}

	/**
	 * Set the size in pixels at which each patch will be rendered before they are scaled down to requested identicon size. Default size is 20 pixels which means, for 9-block identicon, a 60x60 image will be rendered and scaled down.
	 * 
	 * @param size
	 *            patch size in pixels
	 */
	public void setPatchSize(float size) {
		this.patchSize = size;
		this.patchOffset = this.patchSize / 2.0f; // used to center patch shape at
		float patchScale = this.patchSize / 4.0f;
		// origin.
		this.patchShapes = new GeneralPath[NineBlockIdenticonRenderer2.patchTypes.length];
		for (int i = 0; i < NineBlockIdenticonRenderer2.patchTypes.length; i++) {
			GeneralPath patch = new GeneralPath(Path2D.WIND_NON_ZERO);
			boolean moveTo = true;
			byte[] patchVertices = NineBlockIdenticonRenderer2.patchTypes[i];
			for (byte v : patchVertices) {
				if (v == NineBlockIdenticonRenderer2.PATCH_MOVETO) {
					moveTo = true;
				}
				float vx = ((v % NineBlockIdenticonRenderer2.PATCH_GRIDS) * patchScale) - this.patchOffset;
				float vy = ((float) Math.floor(((float) v) / NineBlockIdenticonRenderer2.PATCH_GRIDS)) * patchScale - this.patchOffset;
				if (!moveTo) {
					patch.lineTo(vx, vy);
				} else {
					moveTo = false;
					patch.moveTo(vx, vy);
				}
			}
			patch.closePath();
			this.patchShapes[i] = patch;
		}
	}

	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	@Override
	public BufferedImage render(BigInteger code, int size) {
		return this.renderQuilt(code.intValue(), size);
	}

	/**
	 * Returns rendered identicon image for given identicon code.
	 * 
	 * <p>
	 * Size of the returned identicon image is determined by patchSize set using {@link setPatchSize}. Since a 9-block identicon consists of 3x3 patches, width and height will be 3 times the patch size.
	 * </p>
	 * 
	 * @param code
	 *            identicon code
	 * @param size
	 *            image size
	 * @return identicon image
	 */
	@Override
	public BufferedImage render(int code, int size) {
		return this.renderQuilt(code, size);
	}

	protected BufferedImage renderQuilt(int code, int size) {
		// -------------------------------------------------
		// PREPARE
		//

		// decode the code into parts
		// bit 0-1: middle patch type
		// bit 2: middle invert
		// bit 3-6: corner patch type
		// bit 7: corner invert
		// bit 8-9: corner turns
		// bit 10-13: side patch type
		// bit 14: side invert
		// bit 15: corner turns
		// bit 16-20: blue color component
		// bit 21-26: green color component
		// bit 27-31: red color component
		int middleType = NineBlockIdenticonRenderer2.centerPatchTypes[code & 0x3];
		boolean middleInvert = ((code >> 2) & 0x1) != 0;
		int cornerType = (code >> 3) & 0x0f;
		boolean cornerInvert = ((code >> 7) & 0x1) != 0;
		int cornerTurn = (code >> 8) & 0x3;
		int sideType = (code >> 10) & 0x0f;
		boolean sideInvert = ((code >> 14) & 0x1) != 0;
		int sideTurn = (code >> 15) & 0x3;
		int blue = (code >> 16) & 0x01f;
		int green = (code >> 21) & 0x01f;
		int red = (code >> 27) & 0x01f;

		// color components are used at top of the range for color difference
		// use white background for now.
		// TODO: support transparency.
		Color fillColor = new Color(red << 3, green << 3, blue << 3);

		// outline shapes with a noticeable color (complementary will do) if
		// shape color and background color are too similar (measured by color
		// distance).
		Color strokeColor = null;
		if (this.getColorDistance(fillColor, this.backgroundColor) < 32.0f) {
			strokeColor = this.getComplementaryColor(fillColor);
		}

		// -------------------------------------------------
		// RENDER
		//

		BufferedImage targetImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = targetImage.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setBackground(this.backgroundColor);
		g.clearRect(0, 0, size, size);

		float blockSize = size / 3.0f;
		float blockSize2 = blockSize * 2.0f;

		// middle patch
		this.drawPatch(g, blockSize, blockSize, blockSize, middleType, 0, middleInvert, fillColor, strokeColor);

		// side patchs, starting from top and moving clock-wise
		this.drawPatch(g, blockSize, 0, blockSize, sideType, sideTurn++, sideInvert, fillColor, strokeColor);
		this.drawPatch(g, blockSize2, blockSize, blockSize, sideType, sideTurn++, sideInvert, fillColor, strokeColor);
		this.drawPatch(g, blockSize, blockSize2, blockSize, sideType, sideTurn++, sideInvert, fillColor, strokeColor);
		this.drawPatch(g, 0, blockSize, blockSize, sideType, sideTurn++, sideInvert, fillColor, strokeColor);

		// corner patchs, starting from top left and moving clock-wise
		this.drawPatch(g, 0, 0, blockSize, cornerType, cornerTurn++, cornerInvert, fillColor, strokeColor);
		this.drawPatch(g, blockSize2, 0, blockSize, cornerType, cornerTurn++, cornerInvert, fillColor, strokeColor);
		this.drawPatch(g, blockSize2, blockSize2, blockSize, cornerType, cornerTurn++, cornerInvert, fillColor, strokeColor);
		this.drawPatch(g, 0, blockSize2, blockSize, cornerType, cornerTurn++, cornerInvert, fillColor, strokeColor);

		g.dispose();

		return targetImage;
	}

	private void drawPatch(Graphics2D g, float x, float y, float size, int patch, int turn, boolean invert, Color fillColor, Color strokeColor) {
		assert patch >= 0;
		assert turn >= 0;
		patch %= NineBlockIdenticonRenderer2.patchTypes.length;
		turn %= 4;
		if ((NineBlockIdenticonRenderer2.patchFlags[patch] & NineBlockIdenticonRenderer2.PATCH_INVERTED) != 0) {
			invert = !invert;
		}

		Shape shape = this.patchShapes[patch];
		double scale = ((double) size) / ((double) this.patchSize);
		float offset = size / 2.0f;

		// paint background
		g.setColor(invert ? fillColor : this.backgroundColor);
		g.fill(new Rectangle2D.Float(x, y, size, size));

		AffineTransform savet = g.getTransform();
		g.translate(x + offset, y + offset);
		g.scale(scale, scale);
		g.rotate(Math.toRadians(turn * 90));

		// if stroke color was specified, apply stroke
		// stroke color should be specified if fore color is too close to the
		// back color.
		if (strokeColor != null) {
			g.setColor(strokeColor);
			g.draw(shape);
		}

		// render rotated patch using fore color (back color if inverted)
		g.setColor(invert ? this.backgroundColor : fillColor);
		g.fill(shape);

		g.setTransform(savet);
	}

	/**
	 * Returns distance between two colors.
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	private float getColorDistance(Color c1, Color c2) {
		float dx = c1.getRed() - c2.getRed();
		float dy = c1.getGreen() - c2.getGreen();
		float dz = c1.getBlue() - c2.getBlue();
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	/**
	 * Returns complementary color.
	 * 
	 * @param color
	 * @return
	 */
	private Color getComplementaryColor(Color color) {
		return new Color(color.getRGB() ^ 0x00FFFFFF);
	}
}
