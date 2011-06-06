package fr.insalyon.creatis.vip.simulationgui.client.util.math;

/**
 * Interface of vector with float components
 * @author Sönke Sothmann
 *
 */
public interface Vectorf {

        /**
         * Creates a new Vector that is this vector multiplied with the given
         * scalar.
         *
         * @param scalar
         * @return the multiplied Vector
         */
        public abstract Vectorf multiply(float scalar);

        /**
         * Returns the length of the vector.
         *
         * @return the length of the vector
         */
        public abstract float length();

        /**
         * Creates a new Vector that is the unit vector of this vector.
         *
         * @return the unit vector
         */
        public abstract Vectorf toUnitVector();

        /**
         * Returns the data of this vector as array.
         *
         * @return the data as array
         */
        public abstract float[] toArray();

}

