package simulator.control;

import org.json.JSONObject;
import org.json.JSONArray;
import simulator.misc.Vector2D;
public class EpsilonEqualStates implements StateComparator
{
	private final double _eps;
	
	public EpsilonEqualStates(double eps)
	{
		_eps = eps;
	}
	
	public boolean equal(JSONObject s1,JSONObject s2)  // Comparador de cuerpos. Según el valor de epsilon admite ligeras discrepancias
	{		
		if (Math.abs(s1.getDouble("time") - s2.getDouble("time")) > _eps) return false;
		
		JSONArray jsArray1 = s1.getJSONArray("bodies");
		JSONArray jsArray2 = s2.getJSONArray("bodies");
		
		if (jsArray1.length() == jsArray2.length())	
		{
			for (int i = 0; i < jsArray2.length(); i++)
			{
				JSONObject jso1 = jsArray1.getJSONObject(i);
				JSONObject jso2 = jsArray2.getJSONObject(i);
				
				Vector2D pos1, pos2, vel1, vel2, force1, force2;
				
				if (jso1.getString("id").equals(jso2.getString("id")))	
				{
					pos1 = new Vector2D(jso1.getJSONArray("p").getDouble(0),jso1.getJSONArray("p").getDouble(1));
					pos2 = new Vector2D(jso2.getJSONArray("p").getDouble(0),jso2.getJSONArray("p").getDouble(1));
					vel1 = new Vector2D(jso1.getJSONArray("v").getDouble(0),jso1.getJSONArray("v").getDouble(1));
					vel2 = new Vector2D(jso2.getJSONArray("v").getDouble(0),jso2.getJSONArray("v").getDouble(1));
					force1 = new Vector2D(jso1.getJSONArray("f").getDouble(0),jso1.getJSONArray("f").getDouble(1));
					force2 = new Vector2D(jso2.getJSONArray("f").getDouble(0),jso2.getJSONArray("f").getDouble(1));
					
					if (pos1.distanceTo(pos2) > _eps || vel1.distanceTo(vel2) > _eps || force1.distanceTo(force2) > _eps) return false;
				}
				else
				{
					return false;
				}
			}
		}
		else
		{
			return false;
		}
		return true;

	}
}
