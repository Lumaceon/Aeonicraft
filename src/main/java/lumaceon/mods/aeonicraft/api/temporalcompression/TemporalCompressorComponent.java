package lumaceon.mods.aeonicraft.api.temporalcompression;

import lumaceon.mods.aeonicraft.temporalcompressor.TemporalCompressorComponentVOP;
import lumaceon.mods.aeonicraft.temporalcompressor.TemporalCompressorMatrix;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import lumaceon.mods.aeonicraft.temporalcompressor.TemporalCompressorComponentVOP.ModifyLevel;

import javax.vecmath.Point2i;
import java.util.ArrayList;

/**
 * Component for temporal compressor system, which can be attached to a world chunk.
 */
public class TemporalCompressorComponent extends IForgeRegistryEntry.Impl<TemporalCompressorComponent>
{

    public ArrayList<TemporalCompressorComponentVOP> TCModifiers = new ArrayList<TemporalCompressorComponentVOP>();
    public float oTCValue = 0;
    public float bTCValue = 0;
    public float mTCValue = 0;
    public float fTCValue = 0;
    public float storage = 0;
    public boolean isModifiable = false;
    public boolean isDisabled = false;

    TemporalCompressorComponentNeighbours neighbours;

    //Create modifiers and add them to specific components via this.
    public TemporalCompressorComponent addTCModifier(TemporalCompressorComponentVOP modifier){
        TCModifiers.add(modifier);
        return this;
    }

    public TemporalCompressorComponent(ResourceLocation registryName) {
        this.setRegistryName(registryName);
    }

    public void doStuff(ModifyLevel modifyLevel){
        modify(modifyLevel);
        //Todo: Properly setting the basic, modified, and final values
    }



    //Modify the temporalCompressorsComponentsStats based on which modifyLevel is given
    private void modify(ModifyLevel modifyLevel){

        ArrayList<TemporalCompressorComponentVOP> TCModifiers;
        ArrayList<TemporalCompressorComponentVOP> tempTCModifiers = new ArrayList<>();

        //Skipped if component is not modifable
        if(!isModifiable){
            return;
        }

        //Looks into each neighbour and gets relevant modifiers
        for (TemporalCompressorComponent neighbour : neighbours.NEIGHBOURS) {
            if(neighbour == null){
                continue;
            }

            switch (modifyLevel) {
                case BASE:
                    tempTCModifiers.addAll(neighbour.getNeighbourTCModifiers(ModifyLevel.BASE));
                    break;
                case MODIFIED:
                    tempTCModifiers.addAll(neighbour.getNeighbourTCModifiers(ModifyLevel.MODIFIED));
                    break;
                case FINAL:
                    tempTCModifiers.addAll(neighbour.getNeighbourTCModifiers(ModifyLevel.FINAL));
                    break;
            }
        }

        //If no modifiers have been found, return
        if(tempTCModifiers == null){
            return;
        }

        //Sorts list
        TCModifiers = getSortedList(tempTCModifiers);


        //Actual modification. Subtraction, Addition, Multiplication and Division happen inside the VOP.
        for (int i = 0; i < TCModifiers.size(); i++) {
            switch (modifyLevel) {
                case BASE:
                    bTCValue = TCModifiers.get(i).execute(bTCValue);
                    break;
                case MODIFIED:
                    mTCValue = TCModifiers.get(i).execute(mTCValue);
                    break;
                case FINAL:
                    fTCValue = TCModifiers.get(i).execute(fTCValue);
                    break;
            }
        }
    }

    //Sorts the given TemporalcompressorComponentVOP by Multiplication/Division first and then Addition/Subtraction
    private ArrayList<TemporalCompressorComponentVOP> getSortedList(ArrayList<TemporalCompressorComponentVOP> tempMod){
        ArrayList<TemporalCompressorComponentVOP> returnValue = new ArrayList<>();

        for (int i = tempMod.size()-1 ; i >0; i--){
            TemporalCompressorComponentVOP modifier = tempMod.get(i);
            if(modifier.multiOrDiv()){
                returnValue.add(modifier);
                tempMod.remove(modifier);
            }
        }

        returnValue.addAll(tempMod);

        return returnValue;
    }

    //Finds the neighbours in a given matrix that this component belongs to and populates the neighbour list
    public TemporalCompressorComponent findAndSetNeighbours(TemporalCompressorMatrix matrix){
        //Find own position
        Point2i ownPos = new Point2i();

        boolean flag = false;
        for (int x = 0; !flag && x < matrix.matrix.length ; x++) {
            for (int y = 0; !flag &&y < matrix.matrix[x].length; y++) {
                if(this == matrix.matrix[x][y]){
                    ownPos.set(x,y);
                    flag = true;
                }
            }
        }

        neighbours.setNeighbours(matrix,ownPos);
        return this;
    }

    public TemporalCompressorComponent findAndSetNeighbours(TemporalCompressorMatrix matrix, int x, int y){
        Point2i ownPos = new Point2i(x, y);
        neighbours.setNeighbours(matrix,ownPos);
        return this;
    }

    public TemporalCompressorComponent findAndSetNeighbours(TemporalCompressorMatrix matrix, Point2i ownPos){
        neighbours.setNeighbours(matrix,ownPos);
        return this;
    }




    //returns arrayList that modify the neighbours equal the modifyLevel.
    public ArrayList<TemporalCompressorComponentVOP> getNeighbourTCModifiers(ModifyLevel modifyLevel){

        //ArrayList that will be returned
        ArrayList<TemporalCompressorComponentVOP> returnValue = new ArrayList<>();


        for (TemporalCompressorComponentVOP modifier : TCModifiers) {
            if(!modifier.isGlobal && modifier.modifyLevel == modifyLevel){
                    returnValue.add(modifier);
            }
        }
        return returnValue;
    }


    public TemporalCompressorComponent setStorage(float storage){
        this.storage = storage;
        return this;
    }



    public TemporalCompressorComponent setTCValue(TemporalCompressorComponentVOP value){
        //this.oTCValue = value;
        return this;
    }

    public class TemporalCompressorComponentNeighbours{
        public TemporalCompressorComponent EAST;
        public TemporalCompressorComponent SOUTH;
        public TemporalCompressorComponent WEST;
        public TemporalCompressorComponent NORTH;
        public final TemporalCompressorComponent[] NEIGHBOURS = new TemporalCompressorComponent[]{EAST,SOUTH,WEST,NORTH};

        public void setNeighbours(TemporalCompressorMatrix matrix , Point2i pos){
            if(pos.x > 0){
                //south
                SOUTH = matrix.getComponentForCoordinates(pos.x-1,pos.y);
            }

            if(pos.x < matrix.matrix.length){
                //north
                NORTH = matrix.getComponentForCoordinates(pos.x+1,pos.y);
            }

            if(pos.y > 0){
                //west
                WEST = matrix.getComponentForCoordinates(pos.x,pos.y-1);
            }

            if(pos.y < matrix.matrix[matrix.matrix.length].length){
                //east
                EAST = matrix.getComponentForCoordinates(pos.x,pos.y+1);
            }



        }

    }
}
