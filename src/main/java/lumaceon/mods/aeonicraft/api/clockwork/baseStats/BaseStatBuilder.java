package lumaceon.mods.aeonicraft.api.clockwork.baseStats;

public class BaseStatBuilder {


    private static ClockworkBaseStatDescriptor progressDescriptor = new ClockworkBaseStatDescriptor("§1","Progress");
    public static ClockworkBaseStat getNewProgressStatInstance(float value){
        return new ClockworkBaseStat(value,progressDescriptor);
    }

    private static ClockworkBaseStatDescriptor efficiencyDescriptor = new ClockworkBaseStatDescriptor("§2","Efficiency");
    public static ClockworkBaseStat getNewEfficiencyStatInstance(float value){
        return new ClockworkBaseStat(value, efficiencyDescriptor);
    }

    private static ClockworkBaseStatDescriptor windupDescriptor = new ClockworkBaseStatDescriptor("§3","Windup");
    public static ClockworkBaseStat getNewWindupStatInstance(float value){
        return new ClockworkBaseStat(value,windupDescriptor);
    }

    private static ClockworkBaseStatDescriptor maxWindupDescriptor = new ClockworkBaseStatDescriptor("§4","MaxWindup");
    public static ClockworkBaseStat getNewWindupMaxStatInstance(float value){
        return new ClockworkBaseStat(value,maxWindupDescriptor);
    }



}
