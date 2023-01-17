// Práctica 1 TP - Pablo Lozano Martín y Pablo Magno Pezo Ortiz

package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.control.StateComparator;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.EpsilonEqualStateBuilder;
import simulator.factories.Factory;
import simulator.factories.MassEqualStateBuilder;
import simulator.factories.MassLosingBodyBuilder;
import simulator.factories.MovingTowardsFixedPointBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoForceBuilder;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;

public class Main
{
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static Integer _defaultStepsValue = 150;
	private final static String _forceLawsDefaultValue = "nlug";
	private final static String _stateComparatorDefaultValue = "epseq";
	
	private static Double _dtime = null;
	private static Integer _steps = null;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _expFile = null; 
	private static JSONObject _forceLawsInfo = null;
	private static JSONObject _stateComparatorInfo = null;

	private static Factory<Body> _bodyFactory;
	private static Factory<ForceLaws> _forceLawsFactory;
	private static Factory<StateComparator> _stateComparatorFactory;

	private static void init() 	// Inicializa los arrays y los builders
	{
		ArrayList <Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLosingBodyBuilder());
		_bodyFactory = new BuilderBasedFactory <Body>(bodyBuilders);

		ArrayList<Builder<ForceLaws>>forceLawsBuilders= new ArrayList<>();
		forceLawsBuilders.add(new NewtonUniversalGravitationBuilder());
		forceLawsBuilders.add(new MovingTowardsFixedPointBuilder());
		forceLawsBuilders.add(new NoForceBuilder());
		_forceLawsFactory = new BuilderBasedFactory <ForceLaws>(forceLawsBuilders);
		
		ArrayList<Builder<StateComparator>> stateCmpBuilders = new ArrayList<>();
		stateCmpBuilders.add(new EpsilonEqualStateBuilder());
		stateCmpBuilders.add(new MassEqualStateBuilder());
		_stateComparatorFactory = new BuilderBasedFactory <StateComparator>(stateCmpBuilders);
	}

	private static void parseArgs(String[] args) 	// Parsea los diferentes comandos
	{
		Options cmdLineOptions = buildOptions();

		CommandLineParser parser = new DefaultParser();
		try
		{
			CommandLine line = parser.parse(cmdLineOptions, args);

			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			
			parseExpOutFileOption(line);
			parseOutFileOption(line);

			parseStepsOption(line);
			
			parseDeltaTimeOption(line);
			parseForceLawsOption(line);
			parseStateComparatorOption(line);

			String[] remaining = line.getArgs();
			if (remaining.length > 0) 	// Lanza la excepción cuando el comando es erróneo
			{
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}
		}
		catch (ParseException e)
		{
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}
	}

	private static Options buildOptions() 	 // Añade la información de las opciones
	{
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where output is written. Default value:"
						+ "the standard output.")
				.build());
		
		cmdLineOptions.addOption(Option.builder("eo").longOpt("expected-output").hasArg().desc("The expected output file. If not provided"
						+ "no comparison is applied")
				.build());
		
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc("An integer representing the number of simulation steps."
						+ " Default value: 150.")
				.build());

		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());

		cmdLineOptions.addOption(Option.builder("fl").longOpt("force-laws").hasArg()
				.desc("Force laws to be used in the simulator. Possible values: "
						+ factoryPossibleValues(_forceLawsFactory) + ". Default value: '" + _forceLawsDefaultValue
						+ "'.")
				.build());
		
		cmdLineOptions.addOption(Option.builder("cmp").longOpt("comparator").hasArg()
				.desc("State comparator to be used when comparing states. Possible values: "
						+ factoryPossibleValues(_stateComparatorFactory) + ". Default value: '"
						+ _stateComparatorDefaultValue + "'.")
				.build());

		return cmdLineOptions;
	}

	public static String factoryPossibleValues(Factory<?> factory) 
	{
		if (factory == null)
			return "No values found (the factory is null)";

		String s = "";

		for (JSONObject fe : factory.getInfo())
		{
			if (s.length() > 0)
			{
				s = s + ", ";
			}
			s = s + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
		}

		s = s + ". You can provide the 'data' json attaching :{...} to the tag, but without spaces.";
		return s;
	}

	// Parses de cada comando:
	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) 
	{
		if (line.hasOption("h")) 
		{
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException 
	{
		_inFile = line.getOptionValue("i");
		if (_inFile == null) 
		{
			throw new ParseException("In batch mode an input file of bodies is required");
		}
	}
	
	private static void parseOutFileOption(CommandLine line) throws ParseException 
	{
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseExpOutFileOption(CommandLine line) throws ParseException 
	{
		_expFile = line.getOptionValue("eo");
	}
	
	private static void parseStepsOption(CommandLine line) throws ParseException 
	{
		String steps = line.getOptionValue("s", _defaultStepsValue.toString());
		try
		{
			_steps = Integer.parseInt(steps);
			assert (_steps > 0);
		}
		catch (Exception e)
		{
			throw new ParseException("Invalid steps value: " + steps);
		}
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException
	{
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try
		{
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	private static JSONObject parseWRTFactory(String v, Factory<?> factory) 
	{
		int i = v.indexOf(":");
		String type = null;
		String data = null;
		if (i != -1) 
		{
			type = v.substring(0, i);
			data = v.substring(i + 1);
		} 
		else
		{
			type = v;
			data = "{}";
		}

		boolean found = false;
		for (JSONObject fe : factory.getInfo()) 
		{
			if (type.equals(fe.getString("type")))
			{
				found = true;
				break;
			}
		}

		JSONObject jo = null;
		if (found) 
		{
			jo = new JSONObject();
			jo.put("type", type);
			jo.put("data", new JSONObject(data));
		}
		return jo;

	}

	private static void parseForceLawsOption(CommandLine line) throws ParseException
	{
		String fl = line.getOptionValue("fl", _forceLawsDefaultValue); 
		_forceLawsInfo = parseWRTFactory(fl, _forceLawsFactory);
		if (_forceLawsInfo == null) 
		{
			throw new ParseException("Invalid force laws: " + fl);
		}
	}

	private static void parseStateComparatorOption(CommandLine line) throws ParseException
	{
		String scmp = line.getOptionValue("cmp", _stateComparatorDefaultValue);
		_stateComparatorInfo = parseWRTFactory(scmp, _stateComparatorFactory);
		if (_stateComparatorInfo == null) 
		{
			throw new ParseException("Invalid state comparator: " + scmp);
		}
	}

	private static void startBatchMode() throws Exception // Inicia la simulación
	{
		InputStream in = new FileInputStream(new File(_inFile));
		OutputStream out = _outFile == null ? System.out : new FileOutputStream(new File(_outFile));
		ForceLaws fuerzas = _forceLawsFactory.createInstance(_forceLawsInfo);
		PhysicsSimulator simulador = new PhysicsSimulator(_dtime, fuerzas);
		Controller control = new Controller(simulador, _bodyFactory);
		
		InputStream expOut = null;
		StateComparator cmp = null;
		
		if(_expFile != null)
		{
			expOut = new FileInputStream(new File(_expFile));
			cmp = _stateComparatorFactory.createInstance(_stateComparatorInfo);
		}
		
		control.loadBodies(in);
		control.run(_steps, out, expOut, cmp);
	}

	private static void start(String[] args) throws Exception // Parsea todos los datos necesarios e inicia la simulación
	{
		parseArgs(args);
		startBatchMode();
	}

	public static void main(String[] args) 
	{
		try
		{
			init();
			start(args);
		}
		catch (Exception e) 
		{
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
