package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws 
{
	private final double _gConst;
	
	public NewtonUniversalGravitation()
	{
		_gConst = 6.67E-11;	
	}
	
	public NewtonUniversalGravitation(double gConst)
	{
		_gConst = gConst;
	}
	
	public void apply(List<Body> bs) 	// Añade las fuerzas que se crean entre todos los cuerpos
	{
		Vector2D f = new Vector2D();
		for (Body body1 : bs)
		{
			for (Body body2 : bs)
			{
				if(body1 != body2) 
				{
					body1.addForce(f.plus(makeForce(body1,body2)));
				}
			}
		}
	}
	
	public Vector2D makeForce(Body uno, Body dos)	// Calcula el módulo de la fuerza según las leyes gravitacionales
	{
		 Vector2D delta = dos.getPosition().minus(uno.getPosition());
		 double dist = delta.magnitude();
		 double magnitude = dist > 0 ? (_gConst * uno.getMass() * dos.getMass()) / (dist * dist) : 0.0;
		 Vector2D resultado = delta.direction().scale(magnitude);
		 return resultado;
	}
	
	public String toString()
	{
		return "NewtonUniversalGravitation [constant=" + _gConst + "]";
	}
}
