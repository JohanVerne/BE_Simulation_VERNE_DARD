package enstabretagne.engine.simple.framework.impl;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.simulation.basics.IScenarioIdProvider;
import enstabretagne.simulation.basics.ScenarioId;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;

public class ScenarioInitData extends InitData implements IScenarioIdProvider {
    double graine;


    private LogicalDateTime debut;
    private LogicalDateTime fin;

    @JsonbCreator
    public ScenarioInitData(
            @JsonbProperty(value = "name") String name,
            @JsonbProperty(value = "replique") int replique,
            @JsonbProperty(value = "graine") double graine,
            @JsonbProperty(value = "debutS") String debutS,
            @JsonbProperty(value = "finS")String finS)
    {
        super(name);
        this.debut= new LogicalDateTime(debutS);
        this.fin=new LogicalDateTime(finS);
        this.graine = graine;
        this.replique=replique;

        this.id = new ScenarioId(name,replique,graine);
    }
    public final int replique;

    public double getGraine() {
        return graine;
    }
    @JsonbTransient
    public LogicalDateTime getDebut() {
        return debut;
    }
    @JsonbTransient
    public LogicalDateTime getFin() {
        return fin;
    }


    public String getDebutS() {
        return debut.toString();
    }

    public String getFinS() {
        return fin.toString();
    }

    private final ScenarioId id;
    @Override
    public ScenarioId getScenarioId() {
        return id;
    }
}
