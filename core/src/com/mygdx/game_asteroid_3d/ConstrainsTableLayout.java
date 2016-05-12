package com.mygdx.game_asteroid_3d;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class ConstrainsTableLayout
{
	private int mX;
	private int mY;
	private int mWidth;
	private int mHeight;
	private int mCount;
	private int mCellPaddingInitial;
	private int mCellPaddingComputed;
	
	public ConstrainsTableLayout( int x, int y, int layoutWidth, int layoutHeight, int cellsCount, int cellPadding )
	{
		mX = x;
		mY = y;
		mWidth = layoutWidth;
		mHeight = layoutHeight;
		mCount = cellsCount;
		mCellPaddingInitial = cellPadding;
		
		int left = ( mWidth / mCount - ( mCount - 1 ) );
		int right = mCellPaddingInitial * ( mCount - 1 );
		if(  left >= right )// cells are not overlapping
		{
			mCellPaddingComputed = mCellPaddingInitial;
		}
		else
		{
			mCellPaddingComputed = 0;
		}		
	}
	
//	public List<Vector2[]> getHorizontalCellRects()
//	{
//		ArrayList<Vector2[]> result = new ArrayList<Vector2[]>();
//		
//		int cellWidth = mWidth / mCount - mCellPaddingComputed * ( mCount - 1 );
//		int cellHeight = mHeight;
//		
//		int currLayoutX = mX;
//		
//		for( int cell_i = 0 ; cell_i < mCount ; ++cell_i )
//		{
//			Vector2[] cellLayout = new Vector2[] { 
//					GameScreen.mScreenProportionalConvertor.convert( new Vector2( currLayoutX, mY ) ),
//					GameScreen.mScreenProportionalConvertor.convert( new Vector2( currLayoutX + cellWidth, mY ) ),
//					GameScreen.mScreenProportionalConvertor.convert( new Vector2( currLayoutX + cellWidth, mY + cellHeight ) ),
//					GameScreen.mScreenProportionalConvertor.convert( new Vector2( currLayoutX, mY + cellHeight ) )
//												 };
//			result.add( cellLayout );
//			currLayoutX += cellWidth + mCellPaddingComputed;
//		}
//		
//		return result;
//	}
//	
//	public List<Vector2[]> getHorizontalPaddingRects()
//	{
//		ArrayList<Vector2[]> result = new ArrayList<Vector2[]>();
//		
//		int cellWidth = mWidth / mCount - mCellPaddingComputed * ( mCount - 1 );
//		int cellHeight = mHeight;
//		
//		int currLayoutX = mX;
//		
//		for( int cell_i = 0 ; cell_i < mCount - 1 ; ++cell_i )
//		{
//			Vector2[] cellLayout = new Vector2[] { new Vector2( currLayoutX + cellWidth, mY ),
//												   new Vector2( currLayoutX + cellWidth + mCellPaddingComputed, mY ),
//												   new Vector2( currLayoutX + cellWidth + mCellPaddingComputed, mY + cellHeight ),
//												   new Vector2( currLayoutX + cellWidth, mY + cellHeight )
//												 };
//			result.add( cellLayout );
//			currLayoutX += cellWidth + mCellPaddingComputed;
//		}
//		
//		return result;
//	}
}
