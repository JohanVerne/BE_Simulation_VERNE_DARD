package enstabretagne.base.loggerv2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import enstabretagne.base.Settings;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.simulation.basics.IScenarioIdProvider;
import enstabretagne.simulation.basics.ISimulationDateProvider;
import enstabretagne.simulation.basics.ScenarioId;

public class LoggerV2 {
	public static interface ILoggerRequire extends ISimulationDateProvider,IScenarioIdProvider{}
	Instant startdate;
	
	public final static LoggerV2 instance;
	
	private static String defaultWorkinghDir;
	static{
		defaultWorkinghDir = System.getProperty("user.dir")+File.separator+ Settings.resultDir;
		instance = new LoggerV2();
	}
	
	
	Hashtable<String, List<List<Object>>> journaux;
	boolean initState = false;
    private String name;
    private String workingDirectory;
    private String separator = ";";
    private ILoggerRequire defaultRequired = new ILoggerRequire() {
		
		ScenarioId s = new ScenarioId("defaultID");
		LogicalDateTime d = LogicalDateTime.Zero;
				
		@Override
		public ScenarioId getScenarioId() {
			return s;
		}

		@Override
		public LogicalDateTime SimulationDate() {
			return d;
		}

	};

	private LoggerV2() {
		journaux = new Hashtable<>();
	}

    public void init(String name,String workingDirectory)
    {
    	init(name,workingDirectory,null);
    }
    public void init(String name,String workingDirectory,ILoggerRequire l)
    {
    	if(initState) return;
    	
    	startdate = Instant.now();
    	
    	if(l!=null)
    		defaultRequired = l;
    	
        this.name = name;
        if(workingDirectory!=null)
        	this.workingDirectory = workingDirectory;
        else
        	this.workingDirectory=defaultWorkinghDir;
        
        if (!Files.exists(Paths.get(this.workingDirectory)))
            try {
				Files.createDirectory(Paths.get(this.workingDirectory));
			} catch (IOException e) {
				e.printStackTrace();
			}
        initState = true;
        addRecordingDomain(Settings.journalGlobal,entete);
    }
    
    
    
    public boolean addRecordingDomain(String domaineName,Object... entete)
    {
        if (!initState) return false;
        if (!journaux.containsKey(domaineName))
        {
            List<List<Object>> domaine = new ArrayList<List<Object>>();
    		if(Settings.displayLog && domaineName==Settings.journalGlobal) {
    			String format = "";
    			for(int i =0 ;i<entete.length;i++) format = format + "%s |"; 
    			System.out.printf(format+" \n",entete);
    		}
    		List<Object> l = new ArrayList<Object>();
    		if(domaineName == Settings.journalGlobal) {
            	l.add("Fonction");
            	l.add("Objet");
            	l.add("Level");
            	l.add("Date logique");
            	l.add("Temps r?el");
            	l.add("Num?ro de r?plique");
            	l.add("Identifiant de sc?nario");
            	l.add("Nom de m?thode");
            	l.add("Num?ro de ligne");
            	l.add("Nom de fichier");
    		}
    		else
    		{
            	l.add("Date logique");
            	l.add("Temps r?el");
            	l.add("Num?ro de r?plique");
            	l.add("Identifiant de sc?nario");    			
    		}
            l.addAll(Arrays.asList(entete));
            
            domaine.add(l);
            journaux.put(domaineName, domaine);
            return true ;
        }
        return false;
    }


    private void _addDataTo(StackTraceElement el,ScenarioId scenarioId,Instant t, LogicalDateTime d, LogLevels level, Object obj,String function, String domaineName,Object... data)
    {
        if(journaux.containsKey(domaineName))
        {
        	
        	List<Object> l = new ArrayList<Object>();
        	if(domaineName == Settings.journalGlobal) {
            	l.add(function);
            	l.add(obj);
            	l.add(level);
            	l.add(d);
            	l.add(t);
            	l.add(scenarioId.getRepliqueNumber());
            	l.add(scenarioId.getScenarioId());
            	l.add(el.getMethodName());
            	l.add(el.getLineNumber());
            	l.add(el.getFileName());
        		
        	}
        	else {
            	l.add(d);
            	l.add(t);
            	l.add(scenarioId.getRepliqueNumber());
            	l.add(scenarioId.getScenarioId());        		
        	}
        	l.addAll(Arrays.asList(data));
            journaux.get(domaineName).add(l);
        }
    }

    private void persist()
    {

    	String fileName = workingDirectory + "\\" + name;

        for(var domain :journaux.entrySet())
        {
            String domainefileName = fileName + "-" + domain.getKey() + ".csv";

            try {
            
                // Check if file already exists. If yes, delete it.     
                if (Files.exists(Paths.get(domainefileName)))
                {
						Files.delete(Paths.get(domainefileName));
                }

                // Create a new file
                Files.createFile(Paths.get(domainefileName));
                FileWriter fw = new FileWriter(domainefileName,false);
                BufferedWriter bw = new BufferedWriter(fw);
                                
                    for(var record : domain.getValue())
                    {
                        String recordLine = "";
                        for(var data :record)
                        {
                            recordLine += data + separator;
                        }
                        bw.write(recordLine);
                        bw.newLine();
                    }
                 bw.close();
            }
			catch (IOException e) {
				

				e.printStackTrace();
			}

        }
    }
    

    private void addDataTo(String domaine, Object... params) {
    	_addDataTo(null, defaultRequired.getScenarioId(),Instant.now(),defaultRequired.SimulationDate(), null, params, domaine, domaine, params);
    }
    
    static private Object[] entete = {"importance","entite","date reelle","datelogique","activite","message"}; 
	private void journalise(LogLevels importance,Object obj,String activite,String message) {
		StackTraceElement[] els = Thread.currentThread().getStackTrace();
		StackTraceElement el = els[3];
		
		if(!initState)
			init(Settings.defaultLogName,null);
		
		String objetToRecord = "";
		if(obj!=null) objetToRecord=obj.toString();
		
		String date = "";
		if(defaultRequired!=null) date = defaultRequired.SimulationDate().toString();
      //addDataTo(StackTraceElement el,ScenarioId scenarioId,Temporal t, LogicalDateTime d, LogLevels level, Object obj,String function, String domaineName,Object... data)

		_addDataTo(el,defaultRequired.getScenarioId(),Instant.now(),defaultRequired.SimulationDate(), importance, obj,activite,Settings.journalGlobal,message);
		 
		if(Settings.displayLog) {
			String elTxt = "(" + el.getFileName() + ":" + el.getLineNumber() + ")>" + el.getMethodName();

			System.out.printf(elTxt + "[LT=" + defaultRequired.SimulationDate() + " / DR"+ startdate.until(Instant.now(),ChronoUnit.SECONDS) + "] " + message + "\n");
		}
		
	}
	public void Fatal(Object obj, String act, String message) {
		journalise(LogLevels.fatal, obj, act, message);
	}
	
	public void Detail(Object obj, String act, String message) {
		journalise(LogLevels.detail, obj, act, message);
	}

	public void Information(Object obj, String act, String message) {
		journalise(LogLevels.information, obj, act, message);

	}
	

	public void Terminate() {
		persist();
	}

	public static void main(String[] args) {
		LoggerV2.instance.init("SimulationTest", null);
		LoggerV2.instance.Information(null, "main", "on est dans le main");
		LoggerV2.instance.addRecordingDomain("Test", "nom","prenom");
		LoggerV2.instance.addDataTo("Test", "Verron","Olivier");
		LoggerV2.instance.addRecordingDomain("Test2", "nom","prenom");
		LoggerV2.instance.addDataTo("Test2", "Durand","Claire");
		LoggerV2.instance.persist();
	}
}
