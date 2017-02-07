package com.example.finalproj;

import java.io.Serializable;

public class GameLogic implements Serializable{
	
	private int play = 0;
	public String name="";
	
	GameLogic(String n)
	{
		name = n;
	}
	public void what(int x)
	{
	play = 1;
	}
	public String getName()
	{
		return name;
	}
	
}
