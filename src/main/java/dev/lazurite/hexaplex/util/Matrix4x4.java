package dev.lazurite.hexaplex.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

import java.nio.FloatBuffer;

// TODO: Implement vecmath.Matrix4f features
/**
 * A {@link Matrix4f} wrapper with additional features.
 * @author Patrick Hofmann
 */
public final class Matrix4x4 {

    // region attributes

    /**
     * The {@link Matrix4f} instance.
     */
    public final Matrix4f matrix4f;

    // endregion attributes

    // region constructors

    /**
     * The default constructor.
     */
    public Matrix4x4() {
        this.matrix4f = new Matrix4f();
    }

    /**
     * The {@link Matrix4x4} copy constructor.
     * @param matrix4x4 The {@link Matrix4x4} from which {@link #matrix4f} will be set to.
     */
    public Matrix4x4(Matrix4x4 matrix4x4) {
        this.matrix4f = new Matrix4f(matrix4x4.matrix4f);
    }

    /**
     * The {@code float[]} constructor.
     * @param array1d The {@link float[]} from which {@link #matrix4f} will be set to.
     */
    public Matrix4x4(float[] array1d) {
        this();
        this.set(array1d);
    }

    /**
     * The {@code float[][]} constructor.
     * @param array2d The {@link float[][]} from which {@link #matrix4f} will be set to.
     */
    public Matrix4x4(float[][] array2d) {
        this();
        this.set(array2d);
    }

    /**
     * The {@link Matrix4f} constructor.
     * @param matrix4f The {@link Matrix4f} from which {@link #matrix4f} will be set to.
     */
    public Matrix4x4(Matrix4f matrix4f) {
        this.matrix4f = new Matrix4f(matrix4f);
    }

    /**
     * The {@link Quaternion} constructor.
     * @param quaternion The {@link Quaternion} from which {@link #matrix4f} will be set to.
     */
    public Matrix4x4(Quaternion quaternion) {
        this.matrix4f = new Matrix4f(quaternion);
    }

    // endregion constructors

    // region static methods

    /**
     * The wrapper for {@link Matrix4f#pack(int, int)}.
     * @param x The {@code x} index of a {@code float[][]} representation of a {@link Matrix4f}.
     * @param y The {@code y} index of a {@code float[][]} representation of a {@link Matrix4f}.
     * @return The {@code float[]} coordinate corresponding to the {@code float[][]} coordinates {@code x} and {@code y}.
     */
    @Environment(EnvType.CLIENT)
    public static int pack(int x, int y) {
        return Matrix4f.pack(x, y);
    }

    /**
     * The wrapper for {@link Matrix4f#projectionMatrix(float, float, float, float)}.
     * @param width The width.
     * @param height The height.
     * @param nearPlane The near plane.
     * @param farPlane The far plane.
     * @return The {@link Matrix4x4} projection matrix.
     */
    @Environment(EnvType.CLIENT)
    public static Matrix4x4 projectionMatrix(float width, float height, float nearPlane, float farPlane) {
        return new Matrix4x4(Matrix4f.projectionMatrix(width, height, nearPlane, farPlane));
    }

    /**
     * The wrapper for {@link Matrix4f#scale(float, float, float)}.
     * @param x The {@code x} scalar.
     * @param y The {@code y} scalar.
     * @param z The {@code z} scalar.
     * @return The {@link Matrix4x4} scalar matrix.
     */
    @Environment(EnvType.CLIENT)
    public static Matrix4x4 scale(float x, float y, float z) {
        return new Matrix4x4(Matrix4f.scale(x, y, z));
    }

    /**
     * The wrapper for {@link Matrix4f#translate(float, float, float)}.
     * @param x The {@code x} translation.
     * @param y The {@code y} translation.
     * @param z The {@code z} translation.
     * @return The {@link Matrix4x4} translation matrix.
     */
    @Environment(EnvType.CLIENT)
    public static Matrix4x4 translate(float x, float y, float z) {
        return new Matrix4x4(Matrix4f.translate(x, y, z));
    }

    /**
     * The reverse of {@link Matrix4x4#pack(int, int)}.
     * @param index The {@code index} index of a {@code float[]} representation of a {@link Matrix4f}.
     * @return The {@code float[][]} coordinates corresponding to the {@code float[]} coordinate {@code index}.
     */
    @Environment(EnvType.CLIENT)
    public static int[] unpack(int index) {
        return new int[] { index % 4, index / 4 };
    }

    /**
     * The wrapper for {@link Matrix4f#viewboxMatrix(double, float, float, float)}.
     * @param fov The field of view.
     * @param aspectRatio The aspect ratio.
     * @param cameraDepth The camera depth.
     * @param viewDistance The view distance.
     * @return The {@link Matrix4x4} view box matrix.
     */
    @Environment(EnvType.CLIENT)
    public static Matrix4x4 viewBoxMatrix(double fov, float aspectRatio, float cameraDepth, float viewDistance) {
        return new Matrix4x4(Matrix4f.viewboxMatrix(fov, aspectRatio, cameraDepth, viewDistance));
    }

    // endregion static methods

    // region non-static methods

        // region setters

    // TODO: Error handling.
    /**
     * The setter for {@link #matrix4f} given a {@code float[]} array.
     * @param array The {@code float[]} array that {@link #matrix4f} will be set to.
     */
    @Environment(EnvType.CLIENT)
    public void set(float[] array) {
        for (int index = 0; index < array.length; ++index) {
            this.set(index, array[index]);
        }
    }

    // TODO: Error handling.
    /**
     * The setter for {@link #matrix4f} given a {@code float[][]} array.
     * @param array The {@code float[][]} array that {@link #matrix4f} will be set to.
     */
    @Environment(EnvType.CLIENT)
    public void set(float[][] array) {
        for (int rowIndex = 0; rowIndex < array.length; ++rowIndex) {
            for (int columnIndex = 0; columnIndex < array[0].length; ++columnIndex) {
                this.set(rowIndex, columnIndex, array[rowIndex][columnIndex]);
            }
        }
    }

    // TODO: Error handling.
    /**
     * The setter for {@link #matrix4f} given an {@code index} and a {@code value}.
     * @param index The {@code index} for a {@code float[]} representation of {@link #matrix4f}.
     * @param value The {@code value} that the {@code index} of {@link #matrix4f} will be set to.
     */
    @Environment(EnvType.CLIENT)
    public void set(int index, float value) {
        int[] indices = Matrix4x4.unpack(index);
        this.set(indices[0], indices[1], value);
    }

    // TODO: Error handling.
    /**
     * The setter for {@link #matrix4f} given a {@code x} and {@code y}, and a {@code value}.
     * @param x The {@code x} index for a {@code float[][]} representation of {@link #matrix4f}.
     * @param y The {@code y} index for a {@code float[][]} representation of {@link #matrix4f}.
     * @param value The {@code value} that the {@code x} and {@code y} index of {@link #matrix4f} will be set to.
     */
    @Environment(EnvType.CLIENT)
    public void set(int x, int y, float value) {
        switch (y) {
            case 0:
                switch (x) {
                    case 0:
                        this.matrix4f.a00 = value;
                        break;
                    case 1:
                        this.matrix4f.a01 = value;
                        break;
                    case 2:
                        this.matrix4f.a02 = value;
                        break;
                    case 3:
                        this.matrix4f.a03 = value;
                        break;
                    default:
                        break;
                }
            case 1:
                switch (x) {
                    case 0:
                        this.matrix4f.a10 = value;
                        break;
                    case 1:
                        this.matrix4f.a11 = value;
                        break;
                    case 2:
                        this.matrix4f.a12 = value;
                        break;
                    case 3:
                        this.matrix4f.a13 = value;
                        break;
                    default:
                        break;
                }
            case 2:
                switch (x) {
                    case 0:
                        this.matrix4f.a20 = value;
                        break;
                    case 1:
                        this.matrix4f.a21 = value;
                        break;
                    case 2:
                        this.matrix4f.a22 = value;
                        break;
                    case 3:
                        this.matrix4f.a23 = value;
                        break;
                    default:
                        break;
                }
            case 3:
                switch (x) {
                    case 0:
                        this.matrix4f.a30 = value;
                        break;
                    case 1:
                        this.matrix4f.a31 = value;
                        break;
                    case 2:
                        this.matrix4f.a32 = value;
                        break;
                    case 3:
                        this.matrix4f.a33 = value;
                        break;
                    default:
                        break;
                }
            default:
                break;
        }
    }

        // endregion setters

        // region getters

    /**
     * The wrapper for {@link Matrix4f#copy()}.
     * @return The {@link Matrix4x4} copy of {@code this}.
     */
    @Environment(EnvType.CLIENT)
    public Matrix4x4 copy() {
        return new Matrix4x4(this);
    }

    /**
     * The getter for a {@link Matrix4f} copy of {@link #matrix4f}.
     * @return The {@link Matrix4f} copy of {@link #matrix4f}.
     */
    @Environment(EnvType.CLIENT)
    public Matrix4f copyMatrix4f() {
        return this.matrix4f.copy();
    }

    /**
     * The getter for a {@code float[]} copy of {@link #matrix4f}.
     * @return The {@code float[]} copy of {@link #matrix4f}.
     */
    @Environment(EnvType.CLIENT)
    public float[] getAs1d() {
        return new float[] {
            this.matrix4f.a00, this.matrix4f.a01, this.matrix4f.a02, this.matrix4f.a03,
            this.matrix4f.a10, this.matrix4f.a11, this.matrix4f.a12, this.matrix4f.a13,
            this.matrix4f.a20, this.matrix4f.a21, this.matrix4f.a22, this.matrix4f.a23,
            this.matrix4f.a30, this.matrix4f.a31, this.matrix4f.a32, this.matrix4f.a33,
        };
    }

    /**
     * The getter for a {@code float[][]} copy of {@link #matrix4f}.
     * @return The {@code float[][]} copy of {@link #matrix4f}.
     */
    @Environment(EnvType.CLIENT)
    public float[][] getAs2d() {
        return new float[][] {
            { this.matrix4f.a00, this.matrix4f.a01, this.matrix4f.a02, this.matrix4f.a03 },
            { this.matrix4f.a10, this.matrix4f.a11, this.matrix4f.a12, this.matrix4f.a13 },
            { this.matrix4f.a20, this.matrix4f.a21, this.matrix4f.a22, this.matrix4f.a23 },
            { this.matrix4f.a30, this.matrix4f.a31, this.matrix4f.a32, this.matrix4f.a33 }
        };
    }

    /**
     * The wrapper for {@link Matrix4f#writeToBuffer(FloatBuffer)}.
     * @param floatBuffer The {@link FloatBuffer} to be written to.
     */
    @Environment(EnvType.CLIENT)
    public void writeToBuffer(FloatBuffer floatBuffer) {
        this.matrix4f.writeToBuffer(floatBuffer);
    }

        // endregion getters

        // region modifiers

    /**
     * The wrapper for {@link Matrix4f#addToLastColumn(Vector3f)}.
     * @param vector3f The {@link Vector3f} to be added to the last column of {@link #matrix4f}.
     */
    @Environment(EnvType.CLIENT)
    public void addToLastColumn(Vector3f vector3f) {
        this.matrix4f.addToLastColumn(vector3f);
    }

    // TODO: Document @return
    /**
     * The wrapper for {@link Matrix4f#determinantAndAdjugate()}.
     * @return
     */
    @Environment(EnvType.CLIENT)
    public float determinantAndAdjugate() {
        return this.matrix4f.determinantAndAdjugate();
    }

    /**
     * The wrapper for {@link Matrix4f#invert()}.
     * @return The result of if {@link #matrix4f} can and was inverted.
     */
    @Environment(EnvType.CLIENT)
    public boolean invert() {
        return this.matrix4f.invert();
    }

    /**
     * The wrapper for {@link Matrix4f#loadIdentity()}.
     */
    @Environment(EnvType.CLIENT)
    public void loadIdentity() {
        this.matrix4f.loadIdentity();
    }

    /**
     * The wrapper {@link Matrix4f#multiply(float)}.
     * @param scalar The {@code float} which {@link #matrix4f} will be multiplied by.
     */
    @Environment(EnvType.CLIENT)
    public void multiply(float scalar) {
        this.matrix4f.multiply(scalar);
    }

    /**
     * The wrapper for {@link Matrix4f#multiply(Matrix4f)}.
     * @param matrix4f The {@link Matrix4f} which {@link #matrix4f} will be multiplied by.
     */
    @Environment(EnvType.CLIENT)
    public void multiply(Matrix4f matrix4f) {
        this.matrix4f.multiply(matrix4f);
    }

    /**
     * The wrapper for {@link Matrix4f#multiply(Quaternion)}.
     * @param quaternion The {@link Quaternion} which {@link #matrix4f} will be multiplied by.
     */
    @Environment(EnvType.CLIENT)
    public void multiply(Quaternion quaternion) {
        this.matrix4f.multiply(new Matrix4f(quaternion));
    }

    /**
     * The wrapper for {@link Matrix4f#transpose()}.
     */
    @Environment(EnvType.CLIENT)
    public void transpose() {
        this.matrix4f.transpose();
    }

        // endregion modifiers

    // endregion non-static methods

    // region overrides

    /**
     * The implementation of {@link Object#equals(Object)}.
     * @param object The {@link Object} to be compared to.
     * @return The equality of {@code this} and {@code object}.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            return this.matrix4f.equals(((Matrix4x4)object).matrix4f);
        } else {
            return false;
        }
    }

    /**
     * The implementation of {@link Object#hashCode()}.
     * @return The hash value of {@code this}.
     */
    @Override
    public int hashCode() {
        return this.matrix4f.hashCode();
    }

    /**
     * The implementation of {@link Object#toString()}.
     * @return The {@link String} representation of {@code this}.
     */
    @Override
    public String toString() {
        return "Mat4:\n" +
            this.matrix4f.a00 + " " + this.matrix4f.a01 + " " + this.matrix4f.a02 + " " + this.matrix4f.a03 + "\n" +
            this.matrix4f.a10 + " " + this.matrix4f.a11 + " " + this.matrix4f.a12 + " " + this.matrix4f.a13 + "\n" +
            this.matrix4f.a20 + " " + this.matrix4f.a21 + " " + this.matrix4f.a22 + " " + this.matrix4f.a23 + "\n" +
            this.matrix4f.a30 + " " + this.matrix4f.a31 + " " + this.matrix4f.a32 + " " + this.matrix4f.a33 + "\n";
    }

    // endregion overrides
}
