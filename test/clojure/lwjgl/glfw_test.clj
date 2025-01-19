(ns clojure.lwjgl.glfw-test
  (:import (org.lwjgl Version)
           (org.lwjgl.glfw GLFW
                           GLFWErrorCallback
                           GLFWKeyCallbackI)))

(defn init []
  ; this is required on mac-os  (see XThreadonfirststart comment in GLFW docs)
  (.set org.lwjgl.system.Configuration/GLFW_LIBRARY_NAME "glfw_async")

  ; Setup an error callback. The default implementation
	; will print the error message in System.err.
  (.set (GLFWErrorCallback/createPrint System/err))

  ; Initialize GLFW. Most GLFW functions will not work before doing this.
  (when-not (GLFW/glfwInit)
    (throw (IllegalStateException. "Unable to initialize GLFW")))

  ; Configure GLFW
  (GLFW/glfwDefaultWindowHints) ; optional, the current window hints are already the default
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE ; the window will stay hidden after creation
                       GLFW/GLFW_FALSE)
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE ; the window will be resizable
                       GLFW/GLFW_TRUE)

  ; Create the window
  (let [window (GLFW/glfwCreateWindow 300 300 "Hello World!" nil nil)]
    (when (nil? window)
      (throw (RuntimeException. "Failed to create the GLFW window")))

    ; Setup a key callback. It will be called every time a key is pressed, repeated or released.
    (GLFW/glfwSetKeyCallback window (reify GLFWKeyCallbackI
                                      (invoke [_ window key scancode action mods]
                                        (when (and (= key GLFW/GLFW_KEY_ESCAPE)
                                                   (= action GLFW/GLFW_RELEASE))
                                          (GLFW/glfwSetWindowShouldClose window true)) ; We will detect this in the rendering loop
                                        )))

    ))

(defn -main []


  (println "Hello LWJGL " (Version/getVersion) "!")
  (init)


;  for (int i = 0; i < 2; i++) {
;			window.getGraphics().gl20.glClearColor(config.initialBackgroundColor.r, config.initialBackgroundColor.g,
;				config.initialBackgroundColor.b, config.initialBackgroundColor.a);
;			window.getGraphics().gl20.glClear(GL11.GL_COLOR_BUFFER_BIT);
;			GLFW.glfwSwapBuffers(windowHandle);
;		}
  )
