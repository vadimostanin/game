package com.mygdx.game_asteroid_3d;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.mygdx.game_asteroid_3d.level.*;

public class XMLparse
{	
	public HashMap<String, String> XMLparseLangs(String lang)
	{
		HashMap<String, String> langs = new HashMap<String, String>();
		try {
			Element root = new XmlReader().parse(Gdx.files.internal("xml/langs.xml"));
			Array<Element> xml_langs = root.getChildrenByName("lang");
			
			for (Element el : xml_langs) {
				if (el.getAttribute("key").equals(lang)) {
					Array<Element> xml_strings = el.getChildrenByName("string");
					for (Element e : xml_strings) {
						langs.put(e.getAttribute("key"), e.getText());
					}
				} else if (el.getAttribute("key").equals("en")) {
					Array<Element> xml_strings = el.getChildrenByName("string");
					for (Element e : xml_strings) {
						langs.put(e.getAttribute("key"), e.getText());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return langs;
	}
	
	public Array<String> XMLparseLevels( String dir )
	{
		Array<String> levels = new Array<String>();
		
		FileHandle dirHandle;
		if( Gdx.app.getType() == ApplicationType.Android )
		{
			dirHandle = Gdx.files.internal( dir + "xml/levels" );
		}
		else
		{
			dirHandle = Gdx.files.internal( dir + "" );
		}
		for (FileHandle entry : dirHandle.list())
		{
			String ext = "";
			try
			{
				ext = entry.name().substring( entry.name().length() - 4, entry.name().length() );
			}
			catch( Exception e )
			{
				ext = "";
			}
			if( false == ext.equals( ".xml" ) )
			{
				continue;
			}
			
			levels.add( entry.name() );
		}

		return levels;
	}
	
	private Barriers loadBarriers( Barriers barriers, String levelFile )
	{
		try {
			Element root = new XmlReader().parse( Gdx.files.absolute( levelFile ) );
			Array<Element> xml_barriers = root.getChildrenByName("barriers");
			
			for( Element el_barriers : xml_barriers )
			{
				Array<Element> xml_barrier = el_barriers.getChildrenByName("barrier");
				for( Element el_barrier : xml_barrier )
				{
					Barrier barrier = new Barrier();
					
					Array<Element> xml_positions = el_barrier.getChildrenByName("positions");
					for( Element el_positions : xml_positions )
					{
						Array<Element> xml_position = el_positions.getChildrenByName("position");
						for( Element el_position : xml_position )
						{
							Vector2 position = new Vector2();
							position.set( Integer.parseInt( el_position.getAttribute("x") ), Integer.parseInt( el_position.getAttribute("y") ) );
							barrier.BarrierShape.add( position ) ;
						}
					}
					barriers.mBarriers.add( barrier );
				}
			}
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		return barriers;
	}
	
	private LevelActiveObject loadActiveObject( LevelActiveObject activeObject, String levelFile )
	{
		try {
			Element root = new XmlReader().parse( Gdx.files.absolute( levelFile ) );
			Array<Element> xml_activeobject = root.getChildrenByName("activeobject");
			
			for( Element el_activeobject : xml_activeobject )
			{
				Array<Element> xml_position = el_activeobject.getChildrenByName("initposition");
				for( Element el_position : xml_position )
				{
					Vector2 position = new Vector2();
					position.set( Integer.parseInt( el_position.getAttribute("x") ), Integer.parseInt( el_position.getAttribute("y") ) );
					activeObject.Position.set( position ) ;
				}
			}
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		return activeObject;
	}
	
	private GameGoal loadGoalObject( GameGoal goalObject, String levelFile )
	{
		try {
			Element root = new XmlReader().parse( Gdx.files.absolute( levelFile ) );
			Array<Element> xml_activeobject = root.getChildrenByName("goalobject");
			
			for( Element el_activeobject : xml_activeobject )
			{
				Array<Element> xml_position = el_activeobject.getChildrenByName("initposition");
				for( Element el_position : xml_position )
				{
					Vector2 position = new Vector2();
					position.set( Integer.parseInt( el_position.getAttribute("x") ), Integer.parseInt( el_position.getAttribute("y") ) );
					goalObject.Position.set( position ) ;
				}
			}
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		return goalObject;
	}
	
	private LevelGravityObjects loadGravityObjects( LevelGravityObjects gravityObjects, String levelFile )
	{
		try {
			Element root = new XmlReader().parse( Gdx.files.absolute( levelFile ) );
			Array<Element> xml_gravityobjects = root.getChildrenByName("gravityobjects");
			
			for( Element el_gravityobjects : xml_gravityobjects )
			{
				Array<Element> xml_gravityobject = el_gravityobjects.getChildrenByName("gravityobject");
				
				for( Element el_gravityobject : xml_gravityobject )
				{
					LevelGravityObject gravityObject  = new LevelGravityObject();
					Array<Element> xml_position = el_gravityobject.getChildrenByName("initposition");
					for( Element el_position : xml_position )
					{
						Vector2 position = new Vector2();
						position.set( Integer.parseInt( el_position.getAttribute("x") ), Integer.parseInt( el_position.getAttribute("y") ) );
						gravityObject.mPosition.set( position ) ;
					}
					gravityObjects.mGravityObjects.add( gravityObject );
				}
			}
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		return gravityObjects;
	}
	
	private LevelWorld loadWorld( LevelWorld world, String levelFile )
	{
		try {
			Element root = new XmlReader().parse( Gdx.files.absolute( levelFile ) );
			Array<Element> xml_world = root.getChildrenByName("world");
			
			Element el_world = xml_world.first();
			Vector2 position = new Vector2();
			position.set( Integer.parseInt( el_world.getAttribute("width") ), Integer.parseInt( el_world.getAttribute("height") ) );
			world.mWh.set( position ) ;
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		return world;
	}

	public Level loadLevel( String levelFile )
	{
		Level level = new Level();
		loadBarriers( level.mBariers, levelFile );
		loadActiveObject( level.mActiveObject, levelFile );
		loadGoalObject( level.mGoal, levelFile );
		loadGravityObjects(level.mGravityObjects, levelFile );
		loadWorld(level.mLevelWorld, levelFile );
		return level;
	}
}
