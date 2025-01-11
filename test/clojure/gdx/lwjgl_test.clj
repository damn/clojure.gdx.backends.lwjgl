(ns clojure.gdx.lwjgl-test
  (:import java.nio.IntBuffer
           org.lwjgl.BufferUtils
           org.lwjgl.glfw.GLFW
           org.lwjgl.glfw.GLFWErrorCallback
           org.lwjgl.glfw.GLFWVidMode
           org.lwjgl.glfw.GLFWVidMode.Buffer
           org.lwjgl.opengl.GL
           org.lwjgl.opengl.GL11))

; TODO write it nice and see what chatgpt comes up with
; or give as example

(defn -main [argv]
  (GLFW/glfwSetErrorCallback (GLFWErrorCallback/createPrint System.err))
  (when-not (GLFW/glfwInit)
    (println "Couldn't initialize GLFW")
    (System/exit -1))
  (GLFW/glfwDefaultWindowHints)
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE  
                       GLFW/GLFW_FALSE)
  ; fullscreen, not current resolution, fails
  (let [modes (GLFW/glfwGetVideoModes (GLFW/glfwGetPrimaryMonitor))]
    ; for (int i = 0; i < modes.limit(); i++) {
    ;         System.out.println(modes.get(i).width() + "x" + modes.get(i).height());
    ; }
    (let [mode (.get modes 7)]
      ; System.out.println("Mode: " + mode.width() + "x" + mode.height());
      (let [window-handle (GLFW/glfwCreateWindow (.width mode)
                                                 (.height mode) 
                                                 "Test" 
                                                 (GLFW/glfwGetPrimaryMonitor)
                                                 0)])
      ;if (windowHandle == 0) {
      ;        throw new RuntimeException("Couldn't create window");
      ;}
      (GLFW/glfwMakeContextCurrent windowHandle)
      (GL/createCapabilities)
      (GLFW/glfwSwapInterval 1)
      (GLFW/glfwShowWindow windowHandle)
      (let [tmp (BufferUtils/createIntBuffer 1)
            tmp2 (BufferUtils/createIntBuffer 1)
            fb-width 0
            fb-height 0]
        (while (not (GLFW/glfwWindowShouldClose windowHandle)) 
          (GLFW/glfwGetFramebufferSize windowHandle tmp tmp2)
          (let [new-fb-width (.get tmp 0)
                new-fb-height (.get tmp2 0)]
            (when (or (not= @fb-width new-fb-width)
                      (not= @fb-height new-fb-height))
              (reset! fb-width new-fb-width)
              (reset! fb-height new-fb-height)
              (println "Framebuffer:" new-fb-width "x" new-fb-height)))
          ; if (fbWidth != tmp.get(0) || fbHeight != tmp2.get(0)) {
          ;                                                        fbWidth = tmp.get(0);
          ;                                                        fbHeight = tmp2.get(0);
          ;                                                        System.out.println("Framebuffer: " + tmp.get(0) + "x" + tmp2.get(0));
          ;                                                        // GL11.glViewport(0, 0, tmp.get(0) * 2, tmp2.get(0) * 2);
          ;                                                        }
          (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)
          (GL11/glBegin GL11.GL_TRIANGLES)
          (GL11/glVertex2f (float -1) (float -1))
          (GL11/glVertex2f (float  1) (float -1))
          (GL11/glVertex2f (float  0) (float  1))
          (GL11/glEnd)
          (GLFW/glfwSwapBuffers windowHandle)
          (GLFW/glfwPollEvents))

        (GLFW/glfwDestroyWindow windowHandle)
        (GLFW/glfwTerminate)))))
    
  



