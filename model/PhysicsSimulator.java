package simulator.model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class PhysicsSimulator
{
	private double _realTime;
	private List <Body>bodyList;
	private ForceLaws _forceLaws;
	private double _currentTime;
	
	public PhysicsSimulator(double t, ForceLaws fl) throws IllegalArgumentException
	{
		if (t <= 0.0 || fl == null) throw new IllegalArgumentException();
		{
			_realTime = t;
			_forceLaws = fl;
			_currentTime = 0.0;
		}	
		this.bodyList = new ArrayList<Body>();
	}
	 	
 	public void advance()	// Aplica un paso de simulación
 	{
 		for (Body body : bodyList)
		{
			body.resetForce();
		}
 		_forceLaws.apply(bodyList);
 		
 		for (Body body : bodyList)
		{
 			body.move(_realTime);
		}
		_currentTime += _realTime;
 	}
 	
 	public void addBody(Body b) throws IllegalArgumentException		// Añade el cuerpo b al simulador
 	{
 		if (!bodyList.contains(b))
 		{
 	 		bodyList.add(b);
 		}
 		else
 		{
 	 		throw new IllegalArgumentException();
 		}
 	}

	public JSONObject getState() 
	{
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		
		for (Body body : bodyList)
			arr.put(body.getState());
		
		obj.put("bodies", arr);
		obj.put("time", _currentTime);
		return obj;
	}
 	
 	public String toString() { return getState().toString(); }
}
