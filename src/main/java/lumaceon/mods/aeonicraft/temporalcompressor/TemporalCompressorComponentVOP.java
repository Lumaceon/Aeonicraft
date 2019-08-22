package lumaceon.mods.aeonicraft.temporalcompressor;

//VOP stands for ValueOperatorPair
public class TemporalCompressorComponentVOP {


    private float Value;
    private final TemporalCompressorComponentOperator operator;
    public boolean isGlobal = false;
    public ModifyLevel modifyLevel = ModifyLevel.BASE;


    public TemporalCompressorComponentVOP(float value, TemporalCompressorComponentOperator operator) {
        this.Value = value;
        this.operator = operator;
    }

    public float getValue() {
        return Value;
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
