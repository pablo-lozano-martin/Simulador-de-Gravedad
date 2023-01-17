package simulator.model;

import java.util.List;
import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws{
	
	private Vector2D _point;
	private double _gConst;
	
	public MovingTowardsFixedPoint()
	{
		_point = new Vector2D();
		_gConst = 9.81;
	}
	
	public MovingTowardsFixedPoint(Vector2D point, double gConst)
	{
		_gConst = gConst;
		_point = point;
	}
	
	public void apply(List<Body> bs) 	// Añade una fuerza en una dirección determinada (hacia un punto fijo) a un objeto
	{
		for (Body body : bs)
		{
			Vector2D f = new Vector2D(_point.minus(body.getPosition()).direction().scale(_gConst * body.getMass()));
			body.addForce(f);
		}
	}
	
	public String toString()
	{
		return "MovingTowardsFixedPoint [point = " + _point + ", gConst = " + _gConst + "]";
	}
}
