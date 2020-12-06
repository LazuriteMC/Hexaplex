package dev.lazurite.hexaplex.mixin;

import dev.lazurite.hexaplex.helpers.Matrix4fHelper.Matrix4fInject;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Matrix4f.class)
public class Matrix4fMixin implements Matrix4fInject {
    @Shadow protected float a00;
    @Shadow protected float a01;
    @Shadow protected float a02;
    @Shadow protected float a03;
    @Shadow protected float a10;
    @Shadow protected float a11;
    @Shadow protected float a12;
    @Shadow protected float a13;
    @Shadow protected float a20;
    @Shadow protected float a21;
    @Shadow protected float a22;
    @Shadow protected float a23;
    @Shadow protected float a30;
    @Shadow protected float a31;
    @Shadow protected float a32;
    @Shadow protected float a33;

    @Override
    public void stuff(float[] fa) throws IllegalArgumentException {
        if (fa.length == MATRIX_SIZE) {
            for (int index = 0; index < fa.length; ++index) {
                set(index, fa[index]);
            }
        } else {
            throw new IllegalArgumentException(String.format("Array of size %d is invalid for a matrix of size %d.", fa.length, MATRIX_SIZE));
        }
    }

    @Override
    public void stuff(float[][] fa) throws IllegalArgumentException {
        if (fa.length == ROW_SIZE && fa[0].length == COLUMN_SIZE) {
            for (int row = 0; row < ROW_SIZE; ++row) {
                for (int column = 0; column < COLUMN_SIZE; ++column) {
                    set(row, column, fa[row][column]);
                }
            }
        } else {
            throw new IllegalArgumentException(String.format("Array of size %d, %d is invalid for a matrix of size %d, %d.", fa.length, fa[0].length, ROW_SIZE, COLUMN_SIZE));
        }
    }

    private void set(int index, float value) {
        switch (index) {
            case 0:
                this.a00 = value;
                break;
            case 1:
                this.a01 = value;
                break;
            case 2:
                this.a02 = value;
                break;
            case 3:
                this.a03 = value;
                break;
            case 4:
                this.a10 = value;
                break;
            case 5:
                this.a11 = value;
                break;
            case 6:
                this.a12 = value;
                break;
            case 7:
                this.a13 = value;
                break;
            case 8:
                this.a20 = value;
                break;
            case 9:
                this.a21 = value;
                break;
            case 10:
                this.a22 = value;
                break;
            case 11:
                this.a23 = value;
                break;
            case 12:
                this.a30 = value;
                break;
            case 13:
                this.a31 = value;
                break;
            case 14:
                this.a32 = value;
                break;
            case 15:
                this.a33 = value;
                break;
            default:
                break;
        }
    }

    private void set(int rowIndex, int columnIndex, float value) {
        switch (rowIndex) {
            case 0:
                set(columnIndex, value);
                break;
            case 1:
                set(columnIndex + ROW_SIZE, value);
                break;
            case 2:
                set(columnIndex + ROW_SIZE * 2, value);
                break;
            case 3:
                set(columnIndex + ROW_SIZE * 3, value);
                break;
            default:
                break;
        }
    }
}
