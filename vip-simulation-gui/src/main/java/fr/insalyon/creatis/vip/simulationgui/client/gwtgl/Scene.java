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
        
        private WebGLBuffer vertexBuffer[]= new WebGLBuffer[6];
        private WebGLBuffer indexBuffer[]= new WebGLBuffer[6];;
        private WebGLBuffer colorBuffer[]= new WebGLBuffer[6];;
        
        private WebGLUniformLocation projectionMatrixUniform; //projection matrix
        private WebGLCanvas webGLCanvas = new WebGLCanvas("500px", "500px");
        private WebGLRenderingContext glContext;
        
        private FloatMatrix perspectiveMatrix;
        private FloatMatrix translationCameraMatrix;
        private FloatMatrix rotationCameraMatrix;
        private FloatMatrix translationMatrix[]= new FloatMatrix[6];
        private FloatMatrix rotationMatrix[]= new FloatMatrix[6];
        private FloatMatrix resultingMatrix[]= new FloatMatrix[6];
        private Object3D Object[]= new Object3D[6];
        
        // lighting
        private Vector3f lightingDirection = new Vector3f(0, -1, -1);
        private float directionalColorRed = 1.0f;
        private float directionalColorGreen = 0.1f;
        private float directionalColorBlue = 0.1f;
        private float ambientColorRed = 0.5f;
        private float ambientColorGreen = 0.5f;
        private float ambientColorBlue = 0.5f;
        
        // For mouse control
        private int xMouseRot=0;
        private int yMouseRot=0;
        private float xMouseDrag=0;
        private float yMouseDrag=0;
        boolean checkMouse=false;
        
        // Minimun of object : camera and model
        private ObjectModel mod;
        private Camera cam;
        private int NUM_OBJECT=5;
        private float stepOfView=2.0f;
       
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
                
                Object[0]=mod;
                Object[1]=Object[2]=Object[3]=Object[4]=null;
                
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

                // Activate depth test and set the depth function
                glContext.enable(WebGLRenderingContext.DEPTH_TEST);
                glContext.depthFunc(WebGLRenderingContext.LEQUAL);
              
                checkErrors();
        }
        private void initBuffers() {
             
            // refresh the height context to the simulator child !
            changeHeighContext(ObjectModel.getInstance().getBoundingBox());
            for(int i=0;i<=NUM_OBJECT;i++)
            {
               if(Object[i]!=null)
                {
                // create the vertexBuffer
                vertexBuffer[i] = glContext.createBuffer();
                glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer[i]);
             
                 
                glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER,
                                Float32Array.create(Object[i].getVertices()),
                                WebGLRenderingContext.STATIC_DRAW);
                
                // create the colorBuffer
                colorBuffer[i] = glContext.createBuffer();
                glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer[i]);
              
                glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER,
                                Float32Array.create(Object[i].getColors()),
                                WebGLRenderingContext.STATIC_DRAW);
                
                // create the indexBuffer
                indexBuffer[i] = glContext.createBuffer();
                glContext.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, indexBuffer[i]);
                
                glContext.bufferData(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER,
                                Uint16Array.create(Object[i].getIndices()),
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
                      float add;
                      add=cam.getTranslateX()+((event.getClientX()-xMouseDrag)/250)*stepOfView;
                      cam.setTranslateX(add);
                      add=cam.getTranslateY()+((yMouseDrag-event.getClientY())/250)*stepOfView;
                      cam.setTranslateY(add);  
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
                 Camera.getInstance().setTranslateZ((Camera.getInstance().getTranslateZ()-Float.valueOf(event.getDeltaY())));
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
                if(Object[i]!=null)
                {
                    
                    glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer[i]);
                    glContext.vertexAttribPointer(vertexPositionAttribute[i], Object[i].getItemSizeVertex(), WebGLRenderingContext.FLOAT, false, 0, 0);
                    glContext.enableVertexAttribArray(vertexPositionAttribute[i]);

                    glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer[i]);
                    glContext.vertexAttribPointer(vertexColorAttribute[i], Object[i].getItemSizeColor(), WebGLRenderingContext.FLOAT, false, 0, 0);
                    glContext.enableVertexAttribArray(vertexColorAttribute[i]);

                    glContext.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, indexBuffer[i]);
                    
                    //camera
                    translationCameraMatrix=MatrixUtil.createTranslationMatrix(cam.getTranslateX(),cam.getTranslateY(),cam.getTranslateZ());
                    rotationCameraMatrix=MatrixUtil.createRotationMatrix(cam.getAngleX(),cam.getAngleY(),0);
     
                    
                    //proj
                    perspectiveMatrix = MatrixUtil.createPerspectiveMatrix(45, 1.0f, 0.1f, 10000);
                    
                    //object transf
                    translationMatrix[i] = MatrixUtil.createTranslationMatrix(Object[i].getTranslateX(),Object[i].getTranslateY(),Object[i].getTranslateZ());
                    rotationMatrix[i] = MatrixUtil.createRotationMatrix(Object[i].getAngleX(),Object[i].getAngleY(),Object[i].getAngleZ());
                   
                    resultingMatrix[i] = perspectiveMatrix.multiply(translationCameraMatrix).multiply(rotationCameraMatrix).multiply(translationMatrix[i]).multiply(rotationMatrix[i]);

                    glContext.uniformMatrix4fv(projectionMatrixUniform, false, resultingMatrix[i].getColumnWiseFlatData());
                    glContext.drawElements(WebGLRenderingContext.TRIANGLES, Object[i].getNumItemIndex(), WebGLRenderingContext.UNSIGNED_SHORT, 0);
                   
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
                    projectionMatrixUniform = glContext.getUniformLocation(shaderProgram, "projectionMatrix");
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
            Object[num]=obj;
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
            cam.setNormalZ(-((float)(boundBox[5]-boundBox[4])*2));          
            stepOfView=((float)(boundBox[5]-boundBox[4]))/20;
        }
        public void changeHeighContext(double[] boundBox){
               for(int i=1;i<5;i++){    
                if(Object[i]!=null)Object[i].setBoundingBox(boundBox);
               }
        }
}
