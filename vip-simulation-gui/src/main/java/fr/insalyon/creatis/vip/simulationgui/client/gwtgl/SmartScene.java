/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.simulationgui.client.gwtgl;

import static com.google.gwt.core.client.GWT.log;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.Timer;
import com.googlecode.gwtgl.array.Float32Array;
import com.googlecode.gwtgl.array.Uint16Array;
import com.googlecode.gwtgl.binding.WebGLBuffer;
import com.googlecode.gwtgl.binding.WebGLCanvas;
import com.googlecode.gwtgl.binding.WebGLProgram;
import com.googlecode.gwtgl.binding.WebGLRenderingContext;
import com.googlecode.gwtgl.binding.WebGLShader;
import com.googlecode.gwtgl.binding.WebGLUniformLocation;
import com.smartgwt.client.widgets.Canvas;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import fr.insalyon.creatis.vip.simulationgui.client.util.math.FloatMatrix4x4;
import fr.insalyon.creatis.vip.simulationgui.client.util.math.MatrixUtil;

/**
 * Example that shows a colored triangle created with the GwtGL binding.
 * 
 * @author Sanke Sothmann
 * 
 */
public class SmartScene extends Canvas {

    private WebGLProgram shaderProgram;
    private int vertexPositionAttribute;
    private int vertexColorAttribute;
    private int vertexNormalAttribute;
    private int vertexNormalInvertAttribute;
    private WebGLBuffer vertexBuffer;
    private WebGLBuffer indexBuffer;
    private WebGLBuffer colorBuffer;
    private WebGLBuffer normalBuffer;
    private WebGLBuffer normalInvertBuffer;
    private WebGLUniformLocation projectionMatrixUniform; // matrice de projection de l'object par rapport a la caméra
    private WebGLUniformLocation projectionNormalUniform;
    private WebGLCanvas webGLCanvas = new WebGLCanvas("500px", "500px");
    private WebGLRenderingContext glContext;
    private FloatMatrix4x4 perspectiveMatrix;
    private FloatMatrix4x4 translationMatrix;
    private FloatMatrix4x4 rotationMatrix;
    private FloatMatrix4x4 resultingMatrix;
    private FloatMatrix4x4 normalMatrix;
    private Data3D object;
    // lightings
    private int angleX;
    private int angleY;
    private int angleZ;
    private int addx = 0;
    private int addy = 1;
    private int addz = 0;
    private int refreshTime = 10;
    private float translateZ = -4;
    private int xMouseRot = 0;
    private int yMouseRot = 0;
    boolean checkMouse = false;

    // Minimun of object : camera and model
    // vDot = max(dot(transNormal.xyz, lightDir),0.0);
    //
        /*
     * (non-Javadoc)
     * 
     * @see com.googlecode.gwtgl.example.client.AbstractGwtGLExample#init()
     */
    //@Override
    /* private static SmartScene instance;
    
    public static SmartScene getInstance(Data3D obj,int width, int height) {
    if (instance == null) {
    instance = new SmartScene(obj,width,height);
    }
    return instance;
    } */
    public SmartScene(Data3D obj, int width, int height) {


        object = obj;

        glContext = webGLCanvas.getGlContext();
        glContext.viewport(0, 0, width, height);

        this.setWidth(width);
        this.setHeight(height);
        this.addChild(webGLCanvas);
        //RootPanel.get("gwtgl").add(webGLCanvas);
        init();
    }

    private void init() {
        createShaderProgram();
        initParams();
        initBuffers();
        initControl();
        showMatrices();
    }

    private void initParams() {
        angleX = -20;
        angleY = 0;
        angleZ = 0;
        translateZ = -((float) (object.getBoundingBox()[5] - object.getBoundingBox()[4]) * 2);
        //
        glContext.viewport(0, 0, this.getOffsetWidth(), this.getOffsetHeight());

        // Set the background color
        glContext.clearColor(0.5f, 1.0f, 1.0f, 1.0f);
        // Set the clear depth (everything is cleared)
        glContext.clearDepth(1.0f);
        ////////////////////////////////////////////// 

        glContext.enable(WebGLRenderingContext.BLEND);
        glContext.blendFunc(WebGLRenderingContext.SRC_ALPHA, WebGLRenderingContext.ONE_MINUS_SRC_ALPHA);
        //SC.say("blabla + one "+ WebGLRenderingContext.ONE+" blabla + src "+WebGLRenderingContext.SRC_ALPHA);
        //glContext.disable(WebGLRenderingContext.DEPTH_TEST);
        // gl.uniform1f(shaderProgram.alphaUniform, parseFloat(document.getElementById("alpha").value));
        ///////////////////////////////////////////:

        // Activate depth test and set the depth function
        glContext.enable(WebGLRenderingContext.DEPTH_TEST);
        glContext.depthFunc(WebGLRenderingContext.LEQUAL);

        checkErrors();
    }

    private void initBuffers() {
        // create the vertexBuffer
        // One Triangle with 3 Points à 3 coordinates
        // refresh the height context to the simulator child !


        // create the vertexBuffer
        vertexBuffer = glContext.createBuffer();
        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer);


        glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER,
                Float32Array.create(object.getVertices()),
                WebGLRenderingContext.STATIC_DRAW);

        // create the colorBuffer
        colorBuffer = glContext.createBuffer();
        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer);

        glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER,
                Float32Array.create(object.getColors()),
                WebGLRenderingContext.STATIC_DRAW);

        // create the indexBuffer
        indexBuffer = glContext.createBuffer();
        glContext.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, indexBuffer);

        glContext.bufferData(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER,
                Uint16Array.create(object.getIndices()),
                WebGLRenderingContext.STATIC_DRAW);

        normalBuffer = glContext.createBuffer();
        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, normalBuffer);

        glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER,
                Float32Array.create(object.getNormals()),
                WebGLRenderingContext.STATIC_DRAW);

        checkErrors();
    }

    private void checkErrors() {
        int error = glContext.getError();
        if (error != WebGLRenderingContext.NO_ERROR) {
            String message = "WebGL Error: " + error;
            GWT.log(message, null);
            throw new RuntimeException(message);
        }
    }

    private void drawObject() {
        angleX += addx;
        angleY += addy;
        angleZ += addz;
        // Clear the color buffer and the depth buffer
        glContext.clear(WebGLRenderingContext.COLOR_BUFFER_BIT | WebGLRenderingContext.DEPTH_BUFFER_BIT);

        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer);
        glContext.vertexAttribPointer(vertexPositionAttribute, object.getItemSizeVertex(), WebGLRenderingContext.FLOAT, false, 0, 0);
        glContext.enableVertexAttribArray(vertexPositionAttribute);

        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer);
        glContext.vertexAttribPointer(vertexColorAttribute, object.getItemSizeColor(), WebGLRenderingContext.FLOAT, false, 0, 0);
        glContext.enableVertexAttribArray(vertexColorAttribute);

        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, normalBuffer);
        glContext.vertexAttribPointer(vertexNormalAttribute, 3, WebGLRenderingContext.FLOAT, false, 0, 0);
        glContext.enableVertexAttribArray(vertexNormalAttribute);


        glContext.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, indexBuffer);

        //proj
        perspectiveMatrix = MatrixUtil.createPerspectiveMatrix(45, 1.0f, 0.1f, 1000);

        //object transf
        translationMatrix = MatrixUtil.createTranslationMatrix(0, 0, translateZ);
        rotationMatrix = MatrixUtil.createRotationMatrix(angleX, angleY, angleZ);

        resultingMatrix = perspectiveMatrix.multiply(translationMatrix).multiply(rotationMatrix);

        normalMatrix = rotationMatrix.inverse();
        normalMatrix = normalMatrix.transpose();

        glContext.uniform3f(glContext.getUniformLocation(shaderProgram, "lightDir"), 0f, 0f, 1f); // on envoie au shader la valeur de cette variable..
        glContext.uniform1f(glContext.getUniformLocation(shaderProgram, "uAlpha"), 1f);
        glContext.uniformMatrix4fv(projectionNormalUniform, false, normalMatrix.getColumnWiseFlatData());

        glContext.uniformMatrix4fv(projectionMatrixUniform, false, resultingMatrix.getColumnWiseFlatData());



        glContext.drawElements(WebGLRenderingContext.TRIANGLES, object.getNumItemIndex(), WebGLRenderingContext.UNSIGNED_SHORT, 0);

        glContext.flush();
        checkErrors();

    }

    private void createShaderProgram() {
        // Create the Shaders 
        WebGLShader fragmentShader = getShader(WebGLRenderingContext.FRAGMENT_SHADER, Shaders.INSTANCE.fragmentShader().getText());
        log("Created fragment shader");

        WebGLShader vertexShader = getShader(WebGLRenderingContext.VERTEX_SHADER, Shaders.INSTANCE.vertexShader().getText());
        log("Created vertex shader");
        if (vertexShader == null || fragmentShader == null) {
            log("Shader error");
            throw new RuntimeException("shader error");
        }

        // create the ShaderProgram and attach the Shaders
        shaderProgram = glContext.createProgram();
        if (shaderProgram == null || glContext.getError() != WebGLRenderingContext.NO_ERROR) {
            log("Program errror");
            throw new RuntimeException("program error");
        }

        log("Shader program created");
        glContext.attachShader(shaderProgram, vertexShader);
        log("vertex shader attached to shader program");
        glContext.attachShader(shaderProgram, fragmentShader);
        log("fragment shader attached to shader program");

        // Link the Shader Program
        glContext.linkProgram(shaderProgram);
        if (!glContext.getProgramParameterb(shaderProgram,
                WebGLRenderingContext.LINK_STATUS)) {
            throw new RuntimeException("Could not initialise shaders: " + glContext.getProgramInfoLog(shaderProgram));
        }
        log("Shader program linked");

        // Set the ShaderProgram active 
        glContext.useProgram(shaderProgram);
        vertexPositionAttribute = glContext.getAttribLocation(shaderProgram, "vertexPosition");
        vertexColorAttribute = glContext.getAttribLocation(shaderProgram, "vertexColor");
        vertexNormalAttribute = glContext.getAttribLocation(shaderProgram, "vNormal");
        projectionNormalUniform = glContext.getUniformLocation(shaderProgram, "normalMatrix");
        projectionMatrixUniform = glContext.getUniformLocation(shaderProgram, "projectionMatrix");

        checkErrors();
    }

    private WebGLShader getShader(int type, String source) {
        WebGLShader shader = glContext.createShader(type);

        glContext.shaderSource(shader, source);
        glContext.compileShader(shader);

        // check if the Shader is successfully compiled
        if (!glContext.getShaderParameterb(shader,
                WebGLRenderingContext.COMPILE_STATUS)) {
            throw new RuntimeException(glContext.getShaderInfoLog(shader));
        }

        return shader;
    }

    private void showMatrices() {
        Timer timer = new Timer() {

            public void run() {
                drawObject();
            }
        };
        timer.scheduleRepeating(refreshTime);
    }

    public void changeObject(Data3D obj) {
        object = obj;
        initBuffers();
    }

    public void refreshBuffer() {
        initBuffers();
    }

    public void changeRefreshTime(int time) {
        refreshTime = time;
    }

    public void changeAngleRotation(int ax, int ay, int az) {
        addx = ax;
        addy = ay;
        addz = az;
    }

    private void initControl() {
        webGLCanvas.addMouseUpHandler(new MouseUpHandler() {

            public void onMouseUp(com.google.gwt.event.dom.client.MouseUpEvent event) {
                checkMouse = false;
            }
        });

        webGLCanvas.addMouseMoveHandler(new MouseMoveHandler() {

            public void onMouseMove(MouseMoveEvent event) {
                if (checkMouse == true) {

                    angleX = angleX - event.getClientY() + yMouseRot;
                    angleY = angleY - event.getClientX() + xMouseRot;
                    xMouseRot = event.getClientX();
                    yMouseRot = event.getClientY();
                    drawObject();
                }
            }
        });

        webGLCanvas.addMouseDownHandler(new MouseDownHandler() {

            public void onMouseDown(MouseDownEvent event) {
                xMouseRot = event.getClientX();
                yMouseRot = event.getClientY();
                checkMouse = true;
            }
        });

        webGLCanvas.addMouseOutHandler(new MouseOutHandler() {

            public void onMouseOut(com.google.gwt.event.dom.client.MouseOutEvent event) {
                checkMouse = false;
            }
        });
        webGLCanvas.addMouseWheelHandler(new MouseWheelHandler() {

            public void onMouseWheel(MouseWheelEvent event) {
                translateZ = translateZ - Float.valueOf(event.getDeltaY());
            }
        });
    }
}
