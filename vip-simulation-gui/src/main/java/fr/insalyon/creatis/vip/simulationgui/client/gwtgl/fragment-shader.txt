#ifdef GL_ES
precision highp float;
#endif

uniform sampler2D tex;
varying vec2 texCoord;
void main() {
	gl_FragColor = texture2D(tex, texCoord);
}