package fr.insalyon.creatis.vip.simulationgui.client.util;

import java.util.List;

/**
 * Utility class for conversions (e.g. convert collections to arrays)
 * @author ssothman
 *
 */
public class ConversionUtils {
        /**
         * Coverts Float-List to float array
         * @param list List to convert
         * @return float array
         */
        public static float[] floatListToFloatArray(List<Float> list) {
                float[] array = new float[list.size()];
                for (int i = 0; i < list.size(); i++) {
                        array[i] = list.get(i);
                }
                return array;
        }
        
        /**
         * Converts Integer-List to int array
         * @param list List to convert
         * @return int array
         */
        public static int[] integerListToIntegerArray(List<Integer> list) {
                int[] array = new int[list.size()];
                for (int i = 0; i < list.size(); i++) {
                        array[i] = list.get(i);
                }
                return array;
        }
}
