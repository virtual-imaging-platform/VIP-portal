/**  
 * Sönke Sothmann, Steffen Schäfer : http://code.google.com/p/gwtgl/
 *
 */
package fr.insalyon.creatis.vip.simulationgui.client.gwtgl;


import static com.google.gwt.core.client.GWT.log;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.googlecode.gwtgl.array.Float32Array;
import com.googlecode.gwtgl.array.Uint16Array;
import com.googlecode.gwtgl.binding.WebGLBuffer;
import com.googlecode.gwtgl.binding.WebGLCanvas;
import com.googlecode.gwtgl.binding.WebGLProgram;
import com.googlecode.gwtgl.binding.WebGLRenderingContext;
import com.googlecode.gwtgl.binding.WebGLShader;
import com.googlecode.gwtgl.binding.WebGLUniformLocation;
import com.smartgwt.client.widgets.Canvas;

import fr.insalyon.creatis.vip.simulationgui.client.util.math.FloatMatrix;
import fr.insalyon.creatis.vip.simulationgui.client.util.math.FloatMatrix4x4;
import fr.insalyon.creatis.vip.simulationgui.client.util.math.MatrixUtil;
import fr.insalyon.creatis.vip.simulationgui.client.util.math.Vector3f;



/**
 *
 * @author moulin
 */

public class Scene extends Canvas {

        private WebGLProgram shaderProgram;
        
        private int vertexPositionAttribute[]=new int[6];
        private int vertexColorAttribute[]=new int[6];
         private int vertexNormalAttribute[]=new int[6];
        
        private WebGLBuffer vertexBuffer[]= new WebGLBuffer[6];
        private WebGLBuffer indexBuffer[]= new WebGLBuffer[6];
        private WebGLBuffer colorBuffer[]= new WebGLBuffer[6];
        private WebGLBuffer normalBuffer[]= new WebGLBuffer[6];
        
        private WebGLUniformLocation projectionNormalUniform;
        private WebGLUniformLocation projectionMatrixUniform; //projection matrix
        
        private WebGLCanvas webGLCanvas = new WebGLCanvas("500px", "500px");
        private WebGLRenderingContext glContext;
        
        private FloatMatrix4x4 perspectiveMatrix;
        private FloatMatrix4x4 translationCameraMatrix;
        private FloatMatrix4x4 rotationCameraMatrix;
        private FloatMatrix4x4 translationMatrix[]= new FloatMatrix4x4[6];
        private FloatMatrix4x4 normalMatrix[]=new FloatMatrix4x4[6];
        private FloatMatrix4x4 rotationMatrix[]= new FloatMatrix4x4[6];
        private FloatMatrix4x4 resultingMatrix[]= new FloatMatrix4x4[6];
        private Object3D object[]= new Object3D[6];
        

        // For mouse control
        private int xMouseRot=0;
        private int yMouseRot=0;
        private float xMouseDrag=0;
        private float yMouseDrag=0;
        boolean checkMouse=false;
        
        // Minimun of object : camera and models
        private ObjectModel mod;
        private Camera cam;
        private int NUM_OBJECT=5;
       
        private static Scene instance;
        
        public static Scene getInstance() {
            if (instance == null) {
                instance = new Scene();
            }
            return instance;
        } 
        private Scene(){   
                cam=Camera.getInstance();
                mod=ObjectModel.getInstance();
                
                object[4]=mod;
                object[1]=object[2]=object[3]=object[0]=null;
                
                glContext = webGLCanvas.getGlContext();
                glContext.viewport(0, 0, 500, 500); 
                
                this.setWidth(500);
                this.setHeight(500);
                this.addChild(webGLCanvas);
                init();
        }   
        private void init() {
                createShaderProgram();
                initParams();
                initBuffers();
                initControls();
                drawObject();
        }
        private void initParams() {
                glContext.viewport(0, 0, this.getOffsetWidth(), this.getOffsetHeight());
                
                // Set the background color
                glContext.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
                // Set the clear depth (everything is cleared)
                glContext.clearDepth(1.0f);

    
               /////////////////Light and transparency///////////////////// 
                    
                glContext.enable(WebGLRenderingContext.BLEND);
                glContext.blendFunc(WebGLRenderingContext.SRC_ALPHA,WebGLRenderingContext.ONE_MINUS_SRC_ALPHA); 
              //SC.say("blabla + one "+ WebGLRenderingContext.ONE+" blabla + src "+WebGLRenderingContext.SRC_ALPHA);
                //glContext.disable(WebGLRenderingContext.DEPTH_TEST);
               // gl.uniform1f(shaderProgram.alphaUniform, parseFloat(document.getElementById("alpha").value));
                ///////////////////////////////////////////:
                
                // Activate depth test and set the depth function
                glContext.enable(WebGLRenderingContext.DEPTH_TEST);
                glContext.depthFunc(WebGLRenderingContext.LEQUAL);
                glContext.viewport(0, 0, this.getOffsetWidth(), this.getOffsetHeight());
                checkErrors();
        }
        private void initBuffers() {
             
            // refresh the height context to the simulator child !
            changeHeighContext(ObjectModel.getInstance().getBoundingBox());
            for(int i=0;i<=NUM_OBJECT;i++)
            {
               if(object[i]!=null)
                {
                // create the vertexBuffer
                vertexBuffer[i] = glContext.createBuffer();
                glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer[i]);
             
                 
                glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER,
                                Float32Array.create(object[i].getVertices()),
                                WebGLRenderingContext.STATIC_DRAW);
                
                // create the colorBuffer
                colorBuffer[i] = glContext.createBuffer();
                glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer[i]);
              
                glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER,
                                Float32Array.create(object[i].getColors()),
                                WebGLRenderingContext.STATIC_DRAW);
                
                // create the indexBuffer
                indexBuffer[i] = glContext.createBuffer();
                glContext.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, indexBuffer[i]);
                
                glContext.bufferData(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER,
                                Uint16Array.create(object[i].getIndices()),
                                WebGLRenderingContext.STATIC_DRAW);
                 normalBuffer[i] = glContext.createBuffer();
                glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, normalBuffer[i]);
                
                glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER,
                                Float32Array.create(object[i].getNormals()),
                                WebGLRenderingContext.STATIC_DRAW);

                checkErrors();
                }
            }
            
        }
        private void initControls() {
                // Handle keyboard input
                webGLCanvas.addKeyUpHandler(new KeyUpHandler() {
                        @Override
                        public void onKeyUp(KeyUpEvent event) {
                                if (event.getNativeKeyCode() == KeyCodes.KEY_UP) {
                                        
                                        event.stopPropagation();
                                        event.preventDefault();
                                        
                                }
                                if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                                       
                                        event.stopPropagation();
                                        event.preventDefault();
                                }
                                if (event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
                                      
                                        event.stopPropagation();
                                        event.preventDefault();
                                        
                                }
                                if (event.getNativeKeyCode() == KeyCodes.KEY_LEFT) {
                                        
                                        event.stopPropagation();
                                        event.preventDefault();
                                }
                                if (event.getNativeKeyCode() == KeyCodes.KEY_PAGEUP) {
                                       
                                        event.stopPropagation();
                                        event.preventDefault();
                                        
                                }
                                if (event.getNativeKeyCode() == KeyCodes.KEY_PAGEDOWN) {
                                       
                                        event.stopPropagation();
                                        event.preventDefault();
                                }
                                
                        }
                });
                
                
                  webGLCanvas.addMouseUpHandler(new MouseUpHandler(){
                  public void onMouseUp(MouseUpEvent event) {        
                  checkMouse=false;
                  }
                     });
                 
                  webGLCanvas.addMouseMoveHandler(new MouseMoveHandler(){
                  public void onMouseMove(MouseMoveEvent event) {
                  if(checkMouse==true )
                  {
                    if(event.isControlKeyDown())
                    {                   
                      cam.setTranslateX(event.getClientX()-xMouseDrag);
                      cam.setTranslateY(yMouseDrag-event.getClientY());  
                      xMouseDrag=event.getClientX();
                      yMouseDrag=event.getClientY(); 
                      drawObject();
                   }
                   else
                   {    
                      cam.setAngleX(cam.getAngleX()-event.getClientY()+yMouseRot);
                      cam.setAngleY(cam.getAngleY()-event.getClientX()+xMouseRot);
                      xMouseRot=event.getClientX();
                      yMouseRot=event.getClientY();
                      drawObject();
                   }
                  }
                 }
                });
                               
                  webGLCanvas.addMouseDownHandler(new MouseDownHandler(){
                  public void onMouseDown(MouseDownEvent event) {
                  xMouseDrag=event.getClientX();
                  yMouseDrag=event.getClientY();
                  xMouseRot=event.getClientX();
                  yMouseRot=event.getClientY();
                  checkMouse=true;
                  }    
                 }); 
                  
                  webGLCanvas.addMouseOutHandler(new MouseOutHandler(){
                  public void onMouseOut(com.google.gwt.event.dom.client.MouseOutEvent event){
                    checkMouse=false;
                   }                
                  });
                  
                  webGLCanvas.addMouseUpHandler(new MouseUpHandler(){
                  public void onMouseUp(MouseUpEvent event) {
                  checkMouse=false;
                  }     
                 });
                  
               webGLCanvas.addMouseWheelHandler(new MouseWheelHandler() {
                public void onMouseWheel(MouseWheelEvent event) {
                 Camera.getInstance().setTranslateZ(Float.valueOf(event.getDeltaY()));
                 drawObject(); 
                }
                });       
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
                // Clear the color buffer and the depth buffer
            glContext.clear(WebGLRenderingContext.COLOR_BUFFER_BIT | WebGLRenderingContext.DEPTH_BUFFER_BIT);
            for(int i=0;i<=NUM_OBJECT;i++)
            {
                if(object[i]!=null)
                {
                    
                    glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer[i]);
                    glContext.vertexAttribPointer(vertexPositionAttribute[i], object[i].getItemSizeVertex(), WebGLRenderingContext.FLOAT, false, 0, 0);
                    glContext.enableVertexAttribArray(vertexPositionAttribute[i]);

                    glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer[i]);
                    glContext.vertexAttribPointer(vertexColorAttribute[i], object[i].getItemSizeColor(), WebGLRenderingContext.FLOAT, false, 0, 0);
                    glContext.enableVertexAttribArray(vertexColorAttribute[i]);

                    glContext.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, indexBuffer[i]);
                    
                    
                    glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, normalBuffer[i]);
                    glContext.vertexAttribPointer(vertexNormalAttribute[i], 3, WebGLRenderingContext.FLOAT, false, 0, 0);
                    glContext.enableVertexAttribArray(vertexNormalAttribute[i]);
                    
                    
                    //camera
                    translationCameraMatrix=MatrixUtil.createTranslationMatrix(cam.getTranslateX(),cam.getTranslateY(),cam.getTranslateZ());
                    rotationCameraMatrix=MatrixUtil.createRotationMatrix(cam.getAngleX(),cam.getAngleY(),0);
     
                    
                    //proj
                    perspectiveMatrix = MatrixUtil.createPerspectiveMatrix(45, 1.0f, 0.1f, 10000);
                    
                    //object transf
                    translationMatrix[i] = MatrixUtil.createTranslationMatrix(object[i].getTranslateX(),object[i].getTranslateY(),object[i].getTranslateZ());
                    rotationMatrix[i] = MatrixUtil.createRotationMatrix(object[i].getAngleX(),object[i].getAngleY(),object[i].getAngleZ());
                   
                    resultingMatrix[i] = perspectiveMatrix.multiply(translationCameraMatrix).multiply(rotationCameraMatrix).multiply(translationMatrix[i]).multiply(rotationMatrix[i]);
                    
                    
                    normalMatrix[i]=(rotationCameraMatrix.multiply(rotationMatrix[i])).inverse();
                    normalMatrix[i]=normalMatrix[i].transpose();
          
                     glContext.uniform3f(glContext.getUniformLocation(shaderProgram, "lightDir"),0f,0f,1f); // on envoie au shader la valeur de cette variable..
                     glContext.uniform1f(glContext.getUniformLocation(shaderProgram, "uAlpha"), 1f);
                     glContext.uniformMatrix4fv(projectionNormalUniform, false, normalMatrix[i].getColumnWiseFlatData());
                    glContext.uniformMatrix4fv(projectionMatrixUniform, false, resultingMatrix[i].getColumnWiseFlatData());
                    glContext.drawElements(WebGLRenderingContext.TRIANGLES, object[i].getNumItemIndex(), WebGLRenderingContext.UNSIGNED_SHORT, 0);
                   
                    glContext.flush();
                    checkErrors();
                 }
            }
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

                for(int i=0;i<=NUM_OBJECT;i++)
                {
                    vertexPositionAttribute[i] = glContext.getAttribLocation(shaderProgram, "vertexPosition");
                    vertexColorAttribute[i] = glContext.getAttribLocation(shaderProgram, "vertexColor");
                    vertexNormalAttribute[i] = glContext.getAttribLocation(shaderProgram, "vNormal");
                    projectionMatrixUniform = glContext.getUniformLocation(shaderProgram, "projectionMatrix");
                    projectionNormalUniform= glContext.getUniformLocation(shaderProgram, "normalMatrix");
            
                }
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
         
        public void addObject(Object3D obj, int num){   
            object[num]=obj;
            initBuffers();
            drawObject();
        }
        public void refreshBuffer(){
            initBuffers();
        }
        public void refreshScreen(){
            drawObject();
        }
        public void changeCameraView(double[] boundBox)
        {
            cam.setNormalZ(-((float)(boundBox[5]-boundBox[4])));          
        }
        public void changeHeighContext(double[] boundBox){
               for(int i=0;i<4;i++){    
                if(object[i]!=null)object[i].setBoundingBox(boundBox);
               }
        }
}
