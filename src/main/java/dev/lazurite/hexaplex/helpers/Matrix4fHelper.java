package dev.lazurite.hexaplex.helpers;

import net.minecraft.util.math.Matrix4f;

public class Matrix4fHelper {
    public interface Matrix4fInject {
        int ROW_SIZE = 4;
        int COLUMN_SIZE = 4;
        int MATRIX_SIZE = ROW_SIZE * COLUMN_SIZE;

        void stuff(float[] fa) throws IllegalArgumentException;
        void stuff(float[][] fa) throws IllegalArgumentException;
    }

    public static void stuff(Matrix4f mat, float[] fa) {
        ((Matrix4fInject) (Object) mat).stuff(fa);
    }

    public static void stuff(Matrix4f mat, float[][] fa) {
        ((Matrix4fInject) (Object) mat).stuff(fa);
    }
}
