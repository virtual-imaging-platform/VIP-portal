/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulationgui.client.gwtgl;


import static com.google.gwt.core.client.GWT.log;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.gwtgl.array.Float32Array;
import com.googlecode.gwtgl.binding.WebGLBuffer;
import com.googlecode.gwtgl.binding.WebGLCanvas;
import com.googlecode.gwtgl.binding.WebGLProgram;
import com.googlecode.gwtgl.binding.WebGLRenderingContext;
import com.googlecode.gwtgl.binding.WebGLShader;
import com.googlecode.gwtgl.binding.WebGLTexture;
import com.googlecode.gwtgl.binding.WebGLUniformLocation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.MouseOutEvent;


import fr.insalyon.creatis.vip.simulationgui.client.util.MatrixWidget;
import fr.insalyon.creatis.vip.simulationgui.client.util.math.FloatMatrix;
import fr.insalyon.creatis.vip.simulationgui.client.util.math.MatrixUtil;
import fr.insalyon.creatis.vip.simulationgui.client.util.mesh.CubeFactory;
import fr.insalyon.creatis.vip.simulationgui.client.util.mesh.Mesh;

/**
 *
 * @author moulin
 */
public class Scene extends Canvas{
    
        private Mesh cube = CubeFactory.createNewInstance(1.0f);
        private int angleX = 0;
        private int angleY = 0;
        private int angleZ = 0;
        private float translateZ = -2;
        private float translateX=0;
        private float translateY=0;
        //private float translateMouseX=0;
        //private float translateMouseY=0;
        private float xMouse=0;
        private float yMouse=0;
        boolean checkMouse=false;
        private FloatMatrix perspectiveMatrix;
        private FloatMatrix translationMatrix;
        private FloatMatrix rotationMatrix;
        private FloatMatrix resultingMatrix;
        
        private WebGLProgram shaderProgram;
        private int vertexPositionAttribute;
        private int textureCoordAttribute;
        private WebGLBuffer vertexBuffer;
        private WebGLBuffer vertexTextureCoordBuffer;
        private WebGLUniformLocation projectionMatrixUniform;
        private WebGLUniformLocation textureUniform;
        private WebGLTexture texture;
        private WebGLRenderingContext glContext;
       private WebGLCanvas webGLCanvas = new WebGLCanvas("500px", "500px");
       
        private static Scene instance;
       
        public static Scene getInstance() {
            if (instance == null) {
                instance = new Scene();
            }
            return instance;
        }
    
        private Scene()
        {   
               
                glContext = webGLCanvas.getGlContext();
                glContext.viewport(0, 0, 500, 500); 
                this.setWidth(500);
                this.setHeight(500);
                this.addChild(webGLCanvas);
                init();
        }
       
        
        public void AddObject ()
        {
          
        }
         
        private void init() {
                initParams();
                initShaders();
                initBuffers();
                initTexture();
                initControls(); 
                //draw_object();
                showMatrices();
        }

        /**
         * Updates the Matrix widgets every 500ms
         */
        private void showMatrices() {
                Timer timer = new Timer() {
                       
                        public void run() {
                                draw_object();
                        }
                };
                timer.scheduleRepeating(5);
        }

        /**
         * Initializes the controls of the example.
         */
       private void initControls() {
                // Handle keyboard input
                webGLCanvas.addKeyUpHandler(new KeyUpHandler() {
                        @Override
                        public void onKeyUp(KeyUpEvent event) {
                                if (event.getNativeKeyCode() == KeyCodes.KEY_UP) {
                                        translateY += 0.1f;
                                        event.stopPropagation();
                                        event.preventDefault();
                                        
                                }
                                if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                                        translateY -= 0.1f;
                                        event.stopPropagation();
                                        event.preventDefault();
                                }
                                if (event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
                                        translateX += 0.1f;
                                        event.stopPropagation();
                                        event.preventDefault();
                                        
                                }
                                if (event.getNativeKeyCode() == KeyCodes.KEY_LEFT) {
                                        translateX -= 0.1f;
                                        event.stopPropagation();
                                        event.preventDefault();
                                }
                                if (event.getNativeKeyCode() == KeyCodes.KEY_PAGEUP) {
                                        translateZ += 0.1f;
                                        event.stopPropagation();
                                        event.preventDefault();
                                        
                                }
                                if (event.getNativeKeyCode() == KeyCodes.KEY_PAGEDOWN) {
                                        translateZ -= 0.1f;
                                        event.stopPropagation();
                                        event.preventDefault();
                                }
                                
                        }
                });
                
                 webGLCanvas.addMouseUpHandler(new MouseUpHandler(){

                  public void onMouseUp(MouseUpEvent event) {
         
                  checkMouse=false;
                  
                 //SC.say(" x de la souris " + xMouse + "y de la souris" + yMouse);
                  //throw new UnsupportedOperationException("Not supported yet.");
                  }
                     
                 });
                  webGLCanvas.addMouseMoveHandler(new MouseMoveHandler(){

                  public void onMouseMove(MouseMoveEvent event) {
                  if(checkMouse==true)
                  {
                    translateX+=(event.getClientX()-xMouse)/250;
                    translateY+=(yMouse-event.getClientY())/250;
                    xMouse=event.getClientX();
                    yMouse=event.getClientY();
                  //throw new UnsupportedOperationException("Not supported yet.");
                   }
                  }
                      
                  });
              
                  webGLCanvas.addMouseDownHandler(new MouseDownHandler(){

                  public void onMouseDown(MouseDownEvent event) {
                  xMouse=event.getClientX();
                  yMouse=event.getClientY();
                  checkMouse=true;
                  //throw new UnsupportedOperationException("Not supported yet.");
                  }
                     
                 }); 
                  webGLCanvas.addMouseOutHandler(new MouseOutHandler(){
                  public void onMouseOut(com.google.gwt.event.dom.client.MouseOutEvent event)
                  {
                    checkMouse=false;
                   }
                              
                   });
                  
                  webGLCanvas.addMouseUpHandler(new MouseUpHandler(){
                  public void onMouseUp(MouseUpEvent event) {
                  checkMouse=false;
                  
                 //SC.say(" x de la souris " + xMouse + "y de la souris" + yMouse);
                  //throw new UnsupportedOperationException("Not supported yet.");
                  }
                     
                 });
        }

        /**
         * Initialized the params of WebGL.
         */
        private void initParams() {
                glContext.viewport(0, 0, this.getOffsetWidth(), this.getOffsetHeight());
                
                // clear with background color
                glContext.clearColor(1.0f, 1.0f, 1.0f, 1.0f);

                // clear the whole image
                glContext.clearDepth(1.0f);

                // enable the depth test
                glContext.enable(WebGLRenderingContext.DEPTH_TEST);
                glContext.depthFunc(WebGLRenderingContext.LEQUAL);
                
                checkErrors();
        }

        /**
         * Checks the WebGL Errors and throws an exception if there is an error.
         */
        private void checkErrors() {
                int error = glContext.getError();
                if (error != WebGLRenderingContext.NO_ERROR) {
                        String message = "WebGL Error: " + error;
                        GWT.log(message, null);
                        throw new RuntimeException(message);
                }
        }

        /**
         * Creates the ShaderProgram used by the example to render.
         */
        private void initShaders() {
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

                // Bind vertexPosition to attribute 0
                glContext.bindAttribLocation(shaderProgram, 0, "vertexPosition");
                // Bind texPosition to attribute 1
                glContext.bindAttribLocation(shaderProgram, 1, "texPosition");
                
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
                glContext.enableVertexAttribArray(vertexPositionAttribute);
                
                textureCoordAttribute = glContext.getAttribLocation(shaderProgram, "texPosition");
            glContext.enableVertexAttribArray(textureCoordAttribute);

                // get the position of the projectionMatrix uniform.
                projectionMatrixUniform = glContext.getUniformLocation(shaderProgram,
                                "projectionMatrix");
                
                // get the position of the tex uniform.
                textureUniform = glContext.getUniformLocation(shaderProgram, "tex");
                
                checkErrors();
        }
        
        /**
         * Creates an Shader instance defined by the ShaderType and the source.
         * 
         * @param type
         *            the type of the shader to create
         * @param source
         *            the source of the shader
         * @return the created Shader instance.
         */
        private WebGLShader getShader(int type, String source) {
                WebGLShader shader = glContext.createShader(type);
                glContext.shaderSource(shader, source);
                glContext.compileShader(shader);
                checkErrors();

                // check if the Shader is successfully compiled
                if (!glContext.getShaderParameterb(shader, WebGLRenderingContext.COMPILE_STATUS)) {
                        throw new RuntimeException(glContext.getShaderInfoLog(shader));
                }

                return shader;

        }

        /**
         * Initializes the buffers for vertex coordinates, normals and texture
         * coordinates.
         */
        private void initBuffers() {
                vertexBuffer = glContext.createBuffer();
                glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer);
                glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER,
                                Float32Array.create(cube.getVertices()),
                                WebGLRenderingContext.STATIC_DRAW);
                vertexTextureCoordBuffer = glContext.createBuffer();
                glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexTextureCoordBuffer);
                glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array.create(cube.getTexCoords()), WebGLRenderingContext.STATIC_DRAW);
                checkErrors();
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.googlecode.gwtgl.example.client.AbstractGwtGLExample#draw()
         */
     
        // @Override
        private void draw_object() {
                //angleX = (angleX + 1) % 360;
                //angleY = (angleY + 1) % 360;
                // angleZ=(angleZ+2)%360;

                glContext.clear(WebGLRenderingContext.COLOR_BUFFER_BIT | WebGLRenderingContext.DEPTH_BUFFER_BIT);

                glContext.vertexAttribPointer(vertexPositionAttribute, 3,
                                WebGLRenderingContext.FLOAT, false, 0, 0);

                // Load the vertex data
                glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer);
                glContext.vertexAttribPointer(vertexPositionAttribute, 3, WebGLRenderingContext.FLOAT, false, 0, 0);
                
                // Load the texture coordinates data
                glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexTextureCoordBuffer);
                glContext.vertexAttribPointer(textureCoordAttribute, 2, WebGLRenderingContext.FLOAT, false, 0, 0);

                perspectiveMatrix = MatrixUtil.createPerspectiveMatrix(45, 1.0f, 0.1f, 100);
                translationMatrix = MatrixUtil.createTranslationMatrix(translateX, translateY, translateZ);
                rotationMatrix = MatrixUtil.createRotationMatrix(angleX, angleY, angleZ);
                resultingMatrix = perspectiveMatrix.multiply(translationMatrix).multiply(rotationMatrix);

                glContext.uniformMatrix4fv(projectionMatrixUniform, false, resultingMatrix.getColumnWiseFlatData());
                
                // Bind the texture to texture unit 0
                glContext.activeTexture(WebGLRenderingContext.TEXTURE0);
                glContext.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture);

                // Point the uniform sampler to texture unit 0
                glContext.uniform1i(textureUniform, 0);
                glContext.drawArrays(WebGLRenderingContext.TRIANGLES, 0, 36);
                glContext.flush();
                checkErrors();
        }
        /**
         * Initializes the texture of this example.
         */
        private void initTexture() {
                texture = glContext.createTexture();
                glContext.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture);
                final Image img = getImage(shaders.INSTANCE.texture());
                img.addLoadHandler(new LoadHandler() {
                        @Override
                        public void onLoad(LoadEvent event) {
                                RootPanel.get().remove(img);
                                GWT.log("texture image loaded", null);
                                glContext.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture);
                                glContext.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.RGBA, WebGLRenderingContext.UNSIGNED_BYTE, img.getElement());
                               
                        }
                });
                checkErrors();
                glContext.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.LINEAR);
                glContext.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.LINEAR);
                glContext.bindTexture(WebGLRenderingContext.TEXTURE_2D, null);
                checkErrors();
                
                

        }
        
        /**
         * Converts ImageResource to Image.
         * @param imageResource
         * @return {@link Image} to be used as a texture
         */
        public Image getImage(final ImageResource imageResource) {
                final Image img = new Image();
                img.setVisible(false);
                RootPanel.get().add(img);

                img.setUrl(imageResource.getURL());
        
                return img;
        }
        public void setValueTranslation(float x,float y,float z)
        {
            translateX=x/250;
            translateY=y/250;
            translateZ=z;
        }
        public void setValueRotation(int x, int y, int z)
        {
            angleX =x;
            angleY =y;
            angleZ =z;
        }
        public void reDraw()
        {
            draw_object();
        }

 
}


