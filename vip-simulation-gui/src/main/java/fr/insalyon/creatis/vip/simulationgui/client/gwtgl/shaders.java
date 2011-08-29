/**  
 * Sönke Sothmann, Steffen Schäfer : http://code.google.com/p/gwtgl/
 *
 */
package fr.insalyon.creatis.vip.simulationgui.client.gwtgl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

/**
 *
 * @author moulin
 */
public interface shaders extends ClientBundle {

        /** The instance of the Shaders ClientBundle. */
        public static shaders INSTANCE = GWT.create(shaders.class);

        /**
         * The fragment shader to use in the example.
         *
         * @return the source of the fragment shader.
         */
        @Source(value = { "fragment-shader.txt" })
        TextResource fragmentShader();

        /**
         * The vertex shader to use in the example.
         *
         * @return the source of the vertex shader.
         */
        @Source(value = { "vertex-shader.txt" })
        TextResource vertexShader();

}

