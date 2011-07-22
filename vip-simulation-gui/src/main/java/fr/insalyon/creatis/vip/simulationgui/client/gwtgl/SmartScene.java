/**  
 * Sönke Sothmann, Steffen Schäfer : http://code.google.com/p/gwtgl/
 *
 */

package fr.insalyon.creatis.vip.simulationgui.client.gwtgl;

import static com.google.gwt.core.client.GWT.log;
import com.google.gwt.core.client.GWT;
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
import fr.insalyon.creatis.vip.simulationgui.client.util.math.FloatMatrix;
import fr.insalyon.creatis.vip.simulationgui.client.util.math.MatrixUtil;

/**
 *
 * @author moulin
 */

public class SmartScene extends Canvas{

        private WebGLProgram shaderProgram;
        
        private int vertexPositionAttribute;
        private int vertexColorAttribute;
        
        private WebGLBuffer vertexBuffer;
        private WebGLBuffer indexBuffer;
        private WebGLBuffer colorBuffer;
        
        private WebGLUniformLocation projectionMatrixUniform; // matrice de projection de l'object par rapport a la caméra
        private WebGLCanvas webGLCanvas = new WebGLCanvas("500px", "500px");
        private WebGLRenderingContext glContext;
        
        private FloatMatrix perspectiveMatrix;
        private FloatMatrix translationMatrix;
        private FloatMatrix rotationMatrix;
        private FloatMatrix resultingMatrix;
        private Data3D object;
        
        private int angleX;
        private int angleY;
        private int angleZ;
        private int addx=1;
        private int addy=1;
        private int addz=0;
        private int width;
        private int height;
        private int refreshTime=18;
       
       
        // Minimun of object : camera and model
 
        private static SmartScene instance;
        
        public static SmartScene getInstance(Data3D obj,int width, int height) {
            if (instance == null) {
                instance = new SmartScene(obj,width,height);
            }
            return instance;
        } 
        private SmartScene(Data3D obj, int width, int height){   
                
               this.width=width;
               this.height=height;
                object=obj;
                
                glContext = webGLCanvas.getGlContext();
                glContext.viewport(0, 0, width, height); 
                
                this.setWidth(width);
                this.setHeight(height);
                this.addChild(webGLCanvas);      
                init();
        }   
        private void init() {
                createShaderProgram();
                initParams();
                initBuffers();
                showMatrices();
        }
        private void initParams() {
                angleX=0;
                angleY=0;
                
                glContext.viewport(0, 0, width, height);
                
                // Set the background color
                glContext.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
                // Set the clear depth (everything is cleared)
                glContext.clearDepth(1.0f);

                // Activate depth test and set the depth function
                glContext.enable(WebGLRenderingContext.DEPTH_TEST);
                glContext.depthFunc(WebGLRenderingContext.LEQUAL);
                
                checkErrors();
        }
        private void initBuffers() {
            
          
          
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
                angleX+=addx;
                angleY+=addy;
                angleZ+=addz;
                // Clear the color buffer and the depth buffer
            glContext.clear(WebGLRenderingContext.COLOR_BUFFER_BIT | WebGLRenderingContext.DEPTH_BUFFER_BIT);
            
                    glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer);
                    glContext.vertexAttribPointer(vertexPositionAttribute, object.getItemSizeVertex(), WebGLRenderingContext.FLOAT, false, 0, 0);
                    glContext.enableVertexAttribArray(vertexPositionAttribute);

                    glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer);
                    glContext.vertexAttribPointer(vertexColorAttribute, object.getItemSizeColor(), WebGLRenderingContext.FLOAT, false, 0, 0);
                    glContext.enableVertexAttribArray(vertexColorAttribute);

                    glContext.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, indexBuffer);
      
                    //proj
                    perspectiveMatrix = MatrixUtil.createPerspectiveMatrix(45, 1.0f, 0.1f, 1000);
                    
                    //object transf
                    translationMatrix = MatrixUtil.createTranslationMatrix(0,0,-4);
                    rotationMatrix = MatrixUtil.createRotationMatrix(angleX,angleY,angleZ);
                   
                    resultingMatrix = perspectiveMatrix.multiply(translationMatrix).multiply(rotationMatrix);

                    glContext.uniformMatrix4fv(projectionMatrixUniform, false, resultingMatrix.getColumnWiseFlatData());
                    glContext.drawElements(WebGLRenderingContext.TRIANGLES, object.getNumItemIndex(), WebGLRenderingContext.UNSIGNED_SHORT, 0);
                   
                    glContext.flush();
                    checkErrors();
                 
            }
        
        private void createShaderProgram() {
                // Create the Shaders
                WebGLShader fragmentShader = getShader(WebGLRenderingContext.FRAGMENT_SHADER, shaders.INSTANCE.fragmentShader().getText());
                log("Created fragment shader");
                
                WebGLShader vertexShader = getShader(WebGLRenderingContext.VERTEX_SHADER, shaders.INSTANCE.vertexShader().getText());
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
                        throw new RuntimeException("Could not initialise shaders: " + glContext.getProgramInfoLog (shaderProgram));
                }
                log("Shader program linked");
                
                // Set the ShaderProgram active
                glContext.useProgram(shaderProgram);
                vertexPositionAttribute = glContext.getAttribLocation(shaderProgram, "vertexPosition");
                vertexColorAttribute = glContext.getAttribLocation(shaderProgram, "vertexColor");
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

        public void changeObject(Data3D obj){   
            object=obj;
            initBuffers();
        }
        public void refreshBuffer(){
            initBuffers();
        }
        public void changeRefreshTime(int time)
        {
            refreshTime=time;
        }
        public void changeAngleRotation(int ax, int ay, int az)
        {
            addx=ax;
            addy=ay;
            addz=az;
        }
}