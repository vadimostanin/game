package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class Utils
{
	static public Texture screenShoot()
	{
		byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);
		
		

		Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
		BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
		
		return new Texture( pixmap );
//		
//		PixmapIO.writePNG( Gdx.files.external("mypixmap.png"), pixmap );
//		pixmap.dispose();
	}
	
	static public Texture screenShoot( int x, int y, int width, int height )
	{
		byte[] pixels = ScreenUtils.getFrameBufferPixels( x, y, width, height, true);
		
		

		Pixmap pixmap = new Pixmap( width, height, Pixmap.Format.RGBA8888 );
		BufferUtils.copy( pixels, 0, pixmap.getPixels(), pixels.length );
		
		return new Texture( pixmap );
//		
//		PixmapIO.writePNG( Gdx.files.external("mypixmap.png"), pixmap );
//		pixmap.dispose();
	}
	
	static public void saveTexture( Texture texture, String filename )
	{
		PixmapIO.writePNG( Gdx.files.absolute( Gdx.files.getLocalStoragePath() + filename ), texture.getTextureData().consumePixmap() );
	}
	
	static public Pixmap screenShootPixmap()
	{
		byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);
		
		

		Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
		BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
		
		return pixmap;
//		
//		PixmapIO.writePNG( Gdx.files.external("mypixmap.png"), pixmap );
//		pixmap.dispose();
	}
	
	static public float angle( Vector2 vector )
	{
		int tempLinkVertical_vector_x = 0;
		int tempLinkVertical_vector_y = 10;

		int tempLinkHorizantal_vector_x = 10;
		int tempLinkHorizantal_vector_y = 0;
		
		float vectorCosDivPart = (float) Math.sqrt( vector.x * vector.x + vector.y * vector.y );
		
		if( vectorCosDivPart == 0.0f )
		{
			return 0.0f;
		}
		
		float cosRelativeVerticalVector = (float) (( vector.x * tempLinkVertical_vector_x + vector.y * tempLinkVertical_vector_y )
				/
			 ( vectorCosDivPart *
			   Math.sqrt( tempLinkVertical_vector_x * tempLinkVertical_vector_x + tempLinkVertical_vector_y * tempLinkVertical_vector_y )
			 ));
	
		float cosRelativeHorizontalVector = (float) (( vector.x * tempLinkHorizantal_vector_x + vector.y * tempLinkHorizantal_vector_y )
				/
			 ( vectorCosDivPart *
			   Math.sqrt( tempLinkHorizantal_vector_x * tempLinkHorizantal_vector_x + tempLinkHorizantal_vector_y * tempLinkHorizantal_vector_y )
			 ));
	
		float angleCosRelativeVerticalVector = (float)Math.acos( cosRelativeVerticalVector );
		float angleCosRelativeHorizontalVector = (float)Math.acos( cosRelativeHorizontalVector );
	
		float angleCosRelativeVerticalVectorInt = (float) (angleCosRelativeVerticalVector / Math.PI * 180);
		float angleCosRelativeHorizontalVectorInt = (float) (angleCosRelativeHorizontalVector / Math.PI * 180);
	
		float angleResultInt = 0;
		if( angleCosRelativeVerticalVectorInt <= 90 )//first and second quarter
		{
			angleResultInt = angleCosRelativeHorizontalVectorInt;
		}
		else//third and fourth quarter
		{
			angleResultInt = 360 - angleCosRelativeHorizontalVectorInt;
		}
	
		return angleResultInt;
	}
}
