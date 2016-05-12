//our attributes
attribute vec2 a_position;
attribute vec4 a_color;

//our camera matrix
uniform mat4 u_projTrans;

uniform mat4 u_translate;

//send the color out to the fragment shader
varying vec4 vColor;
varying varCoord;

void main()
{
    vColor = a_color;
    varCoord = u_projTrans * u_translate * vec4(a_position.xy, 0.0, 1.0);
    gl_Position = varCoord;
}
