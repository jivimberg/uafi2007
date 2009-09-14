package palettes;

import java.io.InputStream;
import java.io.InputStreamReader;

import palettes.effects.EffectsManager;
import palettes.env.EnvironmentManager;

public class Application {

	private static Application app = new Application();
	
	private CommandsManager cmd;
	private EnvironmentManager env;
	
	private Application(){
		
		cmd = new CommandsManager();
		
		env = new EnvironmentManager();
		
		EffectsManager effects = new EffectsManager();
		InputStream s = getClass().getResourceAsStream("GifEffects.lst");
		if(s != null) {
			effects.loadEffects(new InputStreamReader(s));
			env.setEffects(effects.getEffects());
		}
	}
	
	public CommandsManager getCmd() {
		return cmd;
	}

	public EnvironmentManager getEnv() {
		return env;
	}

	public static Application getApplication(){
		return app;
	}

	public static void start() {}
}
