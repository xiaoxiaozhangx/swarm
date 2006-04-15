package gov.usgs.swarm;

import gov.usgs.util.ConfigFile;
import gov.usgs.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Swarm configuration class. 
 * 
 * Order of configuration options:
 * 
 * 1) Swarm.config in current directory.
 * 2) Swarm.config in user's home directory.
 * 3) User specified config file.
 * 4) Individual command line config key/values.
 * 
 * $Log: not supported by cvs2svn $
 * @author Dan Cervelli
 */
public class Config
{
	private static String DEFAULT_CONFIG_FILE = "Swarm.config";
	
	public String configFilename;
	public int windowX;
	public int windowY;
	public int windowWidth;
	public int windowHeight;
	public boolean windowMaximized;
	
	public String timeZoneAbbr;
	public double timeZoneOffset;
	
	public String lastPath;
	
	public boolean useLargeCursor;
	public boolean durationEnabled;
	public double durationA;
	public double durationB;
	
	public int span;
	public int timeChunk;
	public boolean showClip;
	public boolean alertClip;
	public int alertClipTimeout;
	
	public String kiosk;
	
	public boolean saveConfig;
	public String groupConfigFile;
	
	public List<String> servers;
	
	public int chooserDividerLocation;

	public static Config createConfig(String[] args)
	{
		String configFile = DEFAULT_CONFIG_FILE;
		  
		int n = args.length - 1;
		if (n >= 0 && !args[n].startsWith("-"))
			configFile = args[n];

		ConfigFile cf = new ConfigFile(configFile);
		cf.put("configFile", configFile, false);
		   
		for (int i = 0; i <= n; i++)
		{
			if (args[i].startsWith("--"))
			{
				String key = args[i].substring(2, args[i].indexOf('='));
				String val = args[i].substring(args[i].indexOf('=') + 1);
				System.out.println(key + " = " + val);
				cf.put(key, val, false);
			}
		}
		Config config = new Config(cf);
		return config;
	}
	
	/**
	 * Sets Swarm configuration variables based on the contents of a 
	 * ConfigFile; sets default values if missing.
	 * 
	 * @param config the configuration information
	 */
	public Config(ConfigFile config)
	{
		configFilename = config.getString("configFile");
		
		windowX = Util.stringToInt(config.getString("windowX"), 50);
		windowY = Util.stringToInt(config.getString("windowY"), 50);
		windowWidth = Util.stringToInt(config.getString("windowSizeX"), 800);
		windowHeight = Util.stringToInt(config.getString("windowSizeY"), 600);
		
		chooserDividerLocation = Util.stringToInt(config.getString("chooserDividerLocation"), 200);
		
		timeZoneAbbr = Util.stringToString(config.getString("timeZoneAbbr"), "UTC");
		timeZoneOffset = Util.stringToDouble(config.getString("timeZoneOffset"), 0);
		
		windowMaximized = Util.stringToBoolean(config.getString("windowMaximized"), false);
		useLargeCursor = Util.stringToBoolean(config.getString("useLargeCursor"), false);
		
		span = Util.stringToInt(config.getString("span"), 24);
		timeChunk = Util.stringToInt(config.getString("timeChunk"), 30);
		
		lastPath = Util.stringToString(config.getString("lastPath"), "default");
		
		kiosk = Util.stringToString(config.getString("kiosk"), "false");
		
		groupConfigFile = Util.stringToString(config.getString("groupConfigFile"), "SwarmGroups.config");
		saveConfig = Util.stringToBoolean(config.getString("saveConfig"), true);
		
		durationEnabled = Util.stringToBoolean(config.getString("durationEnabled"), false);
		durationA = Util.stringToDouble(config.getString("durationA"), 1.86);
		durationB = Util.stringToDouble(config.getString("durationB"), -0.85);
		
		showClip = Util.stringToBoolean(config.getString("showClip"), true);
		alertClip = Util.stringToBoolean(config.getString("alertClip"), false);
		alertClipTimeout = Util.stringToInt(config.getString("alertClipTimeout"), 5);
		
		servers = config.getList("server");
		if (servers == null)
			servers = new ArrayList<String>();
	}
	
	public void addServer(String server)
	{
		servers.add(server);
	}
	
	public void removeServer(String server)
	{
		servers.remove(server);
	}
	
	public String getServer(String abbr)
	{
		if (servers == null)
			return null;
		
		for (String server : servers)
		{
			if (server.startsWith(abbr))
				return server;
		}
		return null;
	}
	
	public boolean serverExists(String abbr)
	{
		return getServer(abbr) != null;
	}
	
	public double getDurationMagnitude(double t)
	{
		return durationA * (Math.log(t) / Math.log(10)) + durationB;
	}
	
	public boolean isKiosk()
	{
		return !kiosk.toLowerCase().equals("false");
	}
	
	public ConfigFile toConfigFile()
	{
		ConfigFile config = new ConfigFile();
		config.put("configFile", configFilename);
		
		config.put("windowX", Integer.toString(windowX));
		config.put("windowY", Integer.toString(windowY));
		config.put("windowSizeX", Integer.toString(windowWidth));
		config.put("windowSizeY", Integer.toString(windowHeight));
		config.put("chooserDividerLocation", Integer.toString(chooserDividerLocation));
		
		config.put("timeZoneAbbr", timeZoneAbbr);
		config.put("timeZoneOffset", Double.toString(timeZoneOffset));
		
		config.put("windowMaximized", Boolean.toString(windowMaximized));
		config.put("useLargeCursor", Boolean.toString(useLargeCursor));
		
		config.put("span", Integer.toString(span));
		config.put("timeChunk", Integer.toString(timeChunk));
		
		config.put("lastPath", lastPath);
		
		config.put("kiosk", kiosk);

		config.put("groupConfigFile", groupConfigFile);
		config.put("saveConfig", Boolean.toString(saveConfig));
		
		config.put("durationEnabled", Boolean.toString(durationEnabled));
		config.put("durationA", Double.toString(durationA));
		config.put("durationB", Double.toString(durationB));

		config.put("showClip", Boolean.toString(showClip));
		config.put("alertClip", Boolean.toString(alertClip));
		config.put("alertClipTimeout", Integer.toString(alertClipTimeout));

		config.putList("server", servers);
		
		return config;
	}
	
	public String toString()
	{
		return toConfigFile().toString();
	}
}