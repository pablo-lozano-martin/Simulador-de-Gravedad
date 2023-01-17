package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws>
{
	static String _type = "mtfp";
	static String _desc = "moving towards fixed point";
	
	public  MovingTowardsFixedPointBuilder()
	{
		super(_type, _desc);
	}
	
	protected ForceLaws createTheInstance(JSONObject js)	// Comprueba que los datos son válidos. Si lo son crea un nuevo Body, de lo contrario devolverá null/producirá una excepción
	{
		if(!js.getString("type").equals(_type)) {return null;}
		js = js.getJSONObject("data");  // Para acceder a los valores de "data" fácilmente
		
		try
		{
			Vector2D p = null;
			JSONArray vector;
			double g = js.has("g") ? js.getDouble("g") : 9.81;

			if(!js.isEmpty())
			{
				vector = js.getJSONArray("c");
				double x = vector.getDouble(0);
				double y = vector.getDouble(1);
				p = new Vector2D(x, y);
			}
			else
			{
				p = new Vector2D(0.0, 0.0);
			}
			return new MovingTowardsFixedPoint(p,g);
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("Illegal argument");
		}
	}
	
	protected JSONObject createData()  	// Añade la información
	{
		JSONObject js = new JSONObject();
		
		js.put("g", "gravitation constant");
		js.put("c", "fixed point");
		
		return js;
	}
}
