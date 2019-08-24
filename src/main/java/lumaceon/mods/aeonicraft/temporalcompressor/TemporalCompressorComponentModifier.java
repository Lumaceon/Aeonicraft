package lumaceon.mods.aeonicraft.temporalcompressor;

//VOP stands for ValueOperatorPair
public class TemporalCompressorComponentModifier implements Comparable<TemporalCompressorComponentModifier> {


    private float Value;
    private TemporalCompressorComponentOperator operator;

    public boolean isGlobal = false;
    //Only used for isGlobal. -value executes the priority queue before neighbour modifiers. Otherwise after.
    //Higher number has priority. so -100  will be executed before -50. 100 will be executed before 50.
    public int priority = 0;

    public ModifyLevel modifyLevel = ModifyLevel.BASE;


    public TemporalCompressorComponentModifier(float value, TemporalCompressorComponentOperator operator) {
        this.Value = value;
        this.operator = operator;
    }

    public TemporalCompressorComponentModifier(float value, TemporalCompressorComponentOperator operator, ModifyLevel modifyLevel) {
        this.Value = value;
        this.operator = operator;
        this.modifyLevel = modifyLevel;
    }

    public TemporalCompressorComponentModifier(float value, TemporalCompressorComponentOperator operator, ModifyLevel modifyLevel, int globalPriority) {
        this.Value = value;
        this.operator = operator;
        this.modifyLevel = modifyLevel;
        isGlobal = true;
        priority = globalPriority;
    }


    public float getValue() {
        return Value;
    }

    public TemporalCompressorComponentModifier setGlobal(int priority){
        isGlobal = true;
        this.priority = priority;
        return this;
    }

    public TemporalCompressorComponentModifier setValue(int value){
        this.Value = value;
        return this;
    }

    public TemporalCompressorComponentModifier setModifyLevel(ModifyLevel modifyLevel){
        this.modifyLevel = modifyLevel;
        return this;
    }

    public TemporalCompressorComponentModifier setOperator(TemporalCompressorComponentOperator operator){
        this.operator = operator;
        return this;
    }

    public boolean  multiOrDiv(){
        if(operator == TemporalCompressorComponentOperator.MULTIPLY || operator == TemporalCompressorComponentOperator.DIVIDE){
            return true;
        }
        return false;
    }
    public TemporalCompressorComponentOperator getOperator(){
        return operator;
    }
    public float execute(float v1){
        return operator.execute(v1, Value);
    }

    @Override
    public int compareTo(TemporalCompressorComponentModifier o) {
        return Integer.compare(this.priority,o.priority);
    }

    public enum ModifyLevel{
        BASE,
        MODIFIED,
        FINAL
    }


    public enum TemporalCompressorComponentOperator {
        ADD{
            @Override
            float execute(float v1, float v2){
                return v1 + v2;
            }
        },
        SUBTRACT{
            @Override
            float execute(float v1, float v2){
                return v1 - v2;
            }
        },
        MULTIPLY{
            @Override
            float execute(float v1, float v2){
                return v1 * v2;
            }
        },
        DIVIDE{
            @Override
            float execute(float v1, float v2){
                if(v2 != 0){
                return v1  / v2;
                }else{
                    System.out.println("Can't divide by zero, silly fool!");
                }
                return 0;
            }
        };

        abstract float execute(float value1, float value2);
    }





}
