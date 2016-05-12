/*

uniform vec3 resolution; // screen resolution
#define T texture2D(u_texture,.5+(p.xy*=.992))
void main() 
{
  vec3 p = gl_FragCoord.xyz / resolution-.5;
  vec3 o = T.rbb;
  for (float i=0.;i<100.;i++) 
    p.z += pow(max(0.,.5-length(T.rg)),2.)*exp(-i*.08);
  gl_FragColor=vec4(o*o+p.z,1);
}
*/
#ifdef GL_ES
precision mediump float;
#endif

//input from vertex shader
varying vec4 vColor;
varying varCoord;
uniform sampler2D u_texture;

void main()
{
    gl_FragColor = vec4( 1.0, 1.0, 0.0, 1.0 );

   vec2 aux = varCoord;
       
//    aux.x = aux.x + (sin(aux.y*fTime0_X*.5)*0.05);

//    gl_FragColor = texture2D( Texture0, aux );
}
