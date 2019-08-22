package lumaceon.mods.aeonicraft.temporalcompressor;

import javax.vecmath.Point2i;

public class TemporalCompressorMatrix
{
    public TemporalCompressorComponent[][] matrix; // Array of column arrays

    public TemporalCompressorMatrix(int size) {
        matrix = new TemporalCompressorComponent[size][size];
    }

    public Point2i getCenter() {
        return new Point2i(matrix.length/2, matrix[matrix.length/2].length/2);
    }

    public TemporalCompressorComponent getComponentForCoordinates(int x, int y) {
        return matrix[x][y];
    }

    public boolean isPartOfMatrix(int x, int y) {
        return x < matrix.length && y < matrix[x].length;
    }
}
