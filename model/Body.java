package simulator.model;

import org.json.JSONObject;
import simulator.misc.Vector2D;

public class Body 
{
	protected String _id;
	protected Vector2D _vel, _force, _pos;
	protected double _mass;
	private JSONObject js = new JSONObject();
	
	public Body(String id, Vector2D v, Vector2D p, double m)	// Cada cuerpo tiene asociado estos atributos
	{
		_id = id;
		_vel = new Vector2D(v);
		_pos = new Vector2D(p);
		_force = new Vector2D();
		_mass = m;
	}

	public String getId()
	{
		return _id;
	}
	
	public Vector2D getVelocity()
	{
		return _vel;
	}
	
	public Vector2D getForce()
	{
		return _force;
	}
	
	public Vector2D getPosition()
	{
		return _pos;
	}
	
	void addForce (Vector2D f)
	{
		_force = _force.plus(f);
	}
	
	void resetForce()
	{
		_force = new Vector2D();
	}
	
	public double getMass()
	{
		return _mass;
	}
	
	void move(double t)		//  mueve el cuerpo durante t segundos utilizando los atributos del mismo
	{
		Vector2D accel;
		if (_mass == 0)
		{
			accel = new Vector2D();
		}
		else
		{
			accel = getForce().scale(1.0 / _mass);
		}
		 _pos = _pos.plus(_vel.scale(t).plus(accel.scale(0.5 * t * t)));
		 _vel = _vel.plus(accel.scale(t));
	}
	
	public JSONObject getState()	
	{
			js.put("id", getId());
			js.put("m", getMass());
			js.put("p", getPosition().asJSONArray());
			js.put("v" ,getVelocity().asJSONArray());
			js.put("f", getForce().asJSONArray());
		
		return js;
	}
	
	public boolean equals(Object obj) // Devuelve true si dos cuerpos son iguales
	{
		Body b = (Body) obj;
		
		if (obj != null)
		{
			if (obj == this)
			{
				return true;
			}
			else
			{
				if (b._id == null && _id == null)
				{
					return true;
				}
				else if (!_id.equals(b._id))
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
	
	public String toString()
	{
		return getState().toString();
	}
	
}
