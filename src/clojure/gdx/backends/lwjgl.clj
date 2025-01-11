(ns clojure.gdx.backends.lwjgl
  (:require [clojure.java.io :as io])
  (:import (com.badlogic.gdx Application
                             ApplicationAdapter
                             Audio
                             Files
                             Gdx
                             Graphics
                             Input
                             LifecycleListener
                             Net
                             Preferences)
           (com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application
                                             Lwjgl3ApplicationConfiguration
                                             Lwjgl3ApplicationConfiguration$GLEmulation
                                             Lwjgl3ApplicationLogger
                                             Lwjgl3Clipboard
                                             Lwjgl3NativesLoader
                                             Lwjgl3Net
                                             Sync)
           (com.badlogic.gdx.backends.lwjgl3.audio Lwjgl3Audio
                                                   OpenALLwjgl3Audio)
           (com.badlogic.gdx.backends.lwjgl3.audio.mock MockAudio)
           (com.badlogic.gdx.graphics.glutils GLVersion)
           (com.badlogic.gdx.math GridPoint2)
           (com.badlogic.gdx.utils Array
                                   Clipboard
                                   GdxRuntimeException
                                   ObjectMap
                                   Os
                                   SharedLibraryLoader)

           (java.awt Taskbar Toolkit)
           (java.io File PrintStream)
           (java.nio IntBuffer)
           (org.lwjgl BufferUtils)
           (org.lwjgl.glfw GLFW
                           GLFWErrorCallback)
           (org.lwjgl.opengl KHRDebug
                             GLUtil
                             GLCapabilities
                             GL43
                             GL11
                             GL
                             ARBDebugOutput
                             AMDDebugOutput)
           (org.lwjgl.system Callback
                             Configuration)))

(defn create-window [listener lifecycleListeners config application]
  {:application application
   :listener listener
   :lifecycleListeners lifecycleListeners
   :windowListener (:windowListener config)
   :config config
   :tmpBuffer  (BufferUtils/createIntBuffer 1)
   :tmpBuffer2 (BufferUtils/createIntBuffer 1)})

(defn- initialize-glfw [error-callback]
  (Lwjgl3NativesLoader/load)
  (GLFW/glfwSetErrorCallback error-callback)
  (when (= SharedLibraryLoader/os Os/MacOsX)
    (GLFW/glfwInitHint GLFW/GLFW_ANGLE_PLATFORM_TYPE,
                       GLFW/GLFW_ANGLE_PLATFORM_TYPE_METAL))
  (GLFW/glfwInitHint GLFW/GLFW_JOYSTICK_HAT_BUTTONS,
                     GLFW/GLFW_FALSE)
  (when-not (GLFW/glfwInit)
    (throw (GdxRuntimeException. "Unable to initialize GLFW"))))

(defn- load-angle []
  (try
    (let [angle-loader (Class/forName "com.badlogic.gdx.backends.lwjgl3.angle.ANGLELoader")]
      (.invoke (.getMethod angle-loader "load") angle-loader))
    ; or clojure does reflection by itself??
    ; can do like this  then ?
    #_(com.badlogic.gdx.backends.lwjgl3.angle.ANGLELoader/load)
    (catch ClassNotFoundException e
      nil); ?? why
    (catch Throwable t
      (throw (GdxRuntimeException. "Couldn't load ANGLE." t)))))

(defn post-load-angle []
  (try
   (let [angle-loader (Class/forName "com.badlogic.gdx.backends.lwjgl3.angle.ANGLELoader")
         load-method (.getMethod angle-loader "postGlfwInit")]
     (.invoke load-method angle-loader))
   (catch ClassNotFoundException _
     nil)
   (catch Throwable t
     (throw (GdxRuntimeException. "Couldn't load ANGLE." t)))))

(defn create-glfw-window [config shared-context-window]
  (GLFW/glfwDefaultWindowHints)
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE (if (:window-resizable config) GLFW/GLFW_TRUE GLFW/GLFW_FALSE))
  (GLFW/glfwWindowHint GLFW/GLFW_MAXIMIZED (if (:window-maximized config) GLFW/GLFW_TRUE GLFW/GLFW_FALSE))
  (GLFW/glfwWindowHint GLFW/GLFW_AUTO_ICONIFY (if (:auto-iconify config) GLFW/GLFW_TRUE GLFW/GLFW_FALSE))
  (GLFW/glfwWindowHint GLFW/GLFW_RED_BITS (:r config))
  (GLFW/glfwWindowHint GLFW/GLFW_GREEN_BITS (:g config))
  (GLFW/glfwWindowHint GLFW/GLFW_BLUE_BITS (:b config))
  (GLFW/glfwWindowHint GLFW/GLFW_ALPHA_BITS (:a config))
  (GLFW/glfwWindowHint GLFW/GLFW_STENCIL_BITS (:stencil config))
  (GLFW/glfwWindowHint GLFW/GLFW_DEPTH_BITS (:depth config))
  (GLFW/glfwWindowHint GLFW/GLFW_SAMPLES (:samples config))

  ;; OpenGL context and API configuration
  (cond
    (#{:GL30 :GL31 :GL32} (:gl-emulation config))
    (do
      (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MAJOR (:gles30-context-major-version config))
      (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR (:gles30-context-minor-version config))
      (when (= SharedLibraryLoader/os Os/MacOsX)
        (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_FORWARD_COMPAT GLFW/GLFW_TRUE)
        (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_PROFILE GLFW/GLFW_OPENGL_CORE_PROFILE)))

    (= :ANGLE_GLES20 (:gl-emulation config))
    (do
      (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_CREATION_API GLFW/GLFW_EGL_CONTEXT_API)
      (GLFW/glfwWindowHint GLFW/GLFW_CLIENT_API GLFW/GLFW_OPENGL_ES_API)
      (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MAJOR 2)
      (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR 0)))

  ;; Transparent framebuffer
  (when (:transparent-framebuffer config)
    (GLFW/glfwWindowHint GLFW/GLFW_TRANSPARENT_FRAMEBUFFER GLFW/GLFW_TRUE))

  ;; Debugging
  (when (:debug config)
    (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_DEBUG_CONTEXT GLFW/GLFW_TRUE))

  ;; Create window
  (let [window-handle
        (if (:fullscreen-mode config)
          (do
            (GLFW/glfwWindowHint GLFW/GLFW_REFRESH_RATE (:refresh-rate (:fullscreen-mode config)))
            (GLFW/glfwCreateWindow (:width (:fullscreen-mode config))
                                    (:height (:fullscreen-mode config))
                                    (:title config)
                                    (:monitor (:fullscreen-mode config))
                                    shared-context-window))
          (do
            (GLFW/glfwWindowHint GLFW/GLFW_DECORATED (if (:window-decorated config) GLFW/GLFW_TRUE GLFW/GLFW_FALSE))
            (GLFW/glfwCreateWindow (:window-width config)
                                    (:window-height config)
                                    (:title config)
                                    0
                                    shared-context-window)))]
    (when (zero? window-handle)
      (throw (GdxRuntimeException. "Couldn't create window")))

    ;; Set window size limits
    (Lwjgl3Window/setSizeLimits window-handle
                                (:window-min-width config)
                                (:window-min-height config)
                                (:window-max-width config)
                                (:window-max-height config))

    ;; Configure window position
    (when (and (nil? (:fullscreen-mode config)) (= GLFW/glfwGetPlatform GLFW/GLFW_PLATFORM_WAYLAND))
      (if (and (= -1 (:window-x config)) (= -1 (:window-y config)))
        (let [new-pos (Lwjgl3ApplicationConfiguration/calculateCenteredWindowPosition
                        (Lwjgl3ApplicationConfiguration/toLwjgl3Monitor GLFW/glfwGetPrimaryMonitor)
                        (:window-width config)
                        (:window-height config))]
          (GLFW/glfwSetWindowPos window-handle (:x new-pos) (:y new-pos)))
        (GLFW/glfwSetWindowPos window-handle (:window-x config) (:window-y config))))

    ;; Maximize window if configured
    (when (:window-maximized config)
      (GLFW/glfwMaximizeWindow window-handle))

    ;; Set window icon
    (when (:window-icon-paths config)
      (Lwjgl3Window/setIcon window-handle (:window-icon-paths config) (:window-icon-file-type config)))

    ;; Make context current and configure VSync
    (GLFW/glfwMakeContextCurrent window-handle)
    (GLFW/glfwSwapInterval (if (:v-sync-enabled config) 1 0))

    ;; Initialize OpenGL
    (if (= :ANGLE_GLES20 (:gl-emulation config))
      (try
        (let [gles (Class/forName "org.lwjgl.opengles.GLES")
              create-capabilities (.getMethod gles "createCapabilities")]
          (.invoke create-capabilities gles))
        (catch Throwable e
          (throw (GdxRuntimeException. "Couldn't initialize GLES" e))))
      (GL/createCapabilities))

    ;; Ensure OpenGL version compatibility
    (initiateGL (= :ANGLE_GLES20 (:gl-emulation config)))
    (when-not (glVersion/isVersionEqualToOrHigher 2 0)
      (throw (GdxRuntimeException. (str "OpenGL 2.0 or higher with the FBO extension is required. OpenGL version: "
                                        (glVersion/getVersionString) "\n" (glVersion/getDebugVersionString)))))

    ;; Ensure FBO support
    (when (and (not= :ANGLE_GLES20 (:gl-emulation config)) (not (supportsFBO)))
      (throw (GdxRuntimeException. (str "OpenGL 2.0 or higher with the FBO extension is required. OpenGL version: "
                                        (glVersion/getVersionString) ", FBO extension: false\n"
                                        (glVersion/getDebugVersionString)))))

    ;; Set up debug output
    (when (:debug config)
      (when (= :ANGLE_GLES20 (:gl-emulation config))
        (throw (IllegalStateException. "ANGLE cannot be used with GL debug output enabled")))
      (let [callback (GLUtil/setupDebugMessageCallback (:debug-stream config))]
        (set! glDebugCallback callback)
        (setGLDebugMessageControl GLDebugMessageSeverity/NOTIFICATION false)))

    window-handle))

(defn createWindow2 [window, config, sharedContext]
  (let [window-handle (create-glfw-window config sharedContext)]
    (.create window windowHandle)
    (.setVisible window (:initial-visible? config))
    (let [gl (.. window getGraphics gl20)]
      (doseq [_ (range 2)]
        (.glClearColor gl
                       (:r config.initial-background-color)
                       (:g config.initial-background-color)
                       (:b config.initial-background-color)
                       (:a config.initial-background-color))
        (.glClear gl GL11/GL_COLOR_BUFFER_BIT)
        (GLFW/glfwSwapBuffers window-handle)))
    (when currentWindow
      ;// the call above to createGlfwWindow switches the OpenGL context to the newly created window,
      ;// ensure that the invariant "currentWindow is the window with the current active OpenGL context" holds
      (.makeCurrent currentWindow)) ))

(defn createWindow1 [this config listener sharedContext]
  (let [window (create-window listener lifecycleListeners config this)]
    (if (zero? sharedContext)
			; the main window is created immediately
			(createWindow2 window, config, sharedContext)
			; creation of additional windows is deferred to avoid GL context trouble
      (post-runnable this (fn []
                            (createWindow2 window config sharedContext)
                            (.add windows window))))
    window))

(defn main-loop []
  (let [closed-windows (atom [])
        running (atom true)]
    (while (and @running (pos? (.size windows)))
      ;; FIXME put it on a separate thread
      (.update audio)

      (let [have-windows-rendered (atom false)
            target-framerate (atom -2)]
        (reset! closed-windows [])
        (doseq [window windows]
          (when (not= current-window window)
            (.makeCurrent window)
            (set! current-window window))
          (when (= @target-framerate -2)
            (reset! target-framerate (.foregroundFPS (.getConfig window))))
          (locking lifecycle-listeners
            (swap! have-windows-rendered #(or % (.update window))))
          (when (.shouldClose window)
            (swap! closed-windows conj window)))

        (GLFW/glfwPollEvents)

        (let [should-request-rendering (atom false)]
          (locking runnables
            (reset! should-request-rendering (pos? (.size runnables)))
            (.clear executed-runnables)
            (.addAll executed-runnables runnables)
            (.clear runnables))
          (doseq [runnable executed-runnables]
            (.run runnable))
          (when @should-request-rendering
            ; Must follow Runnables execution so changes done by Runnables are reflected
            ; in the following render.
            (doseq [window windows]
              (when-not (.isContinuousRendering (.getGraphics window))
                (.requestRendering window)))))

        (doseq [closed-window @closed-windows]
          (when (= (.size windows) 1)
            ;; Lifecycle listener methods
            (dotimes [i (dec (.size lifecycle-listeners)) -1]
              (let [listener (.get lifecycle-listeners i)]
                (.pause listener)
                (.dispose listener)))
            (.clear lifecycle-listeners))
          (.dispose closed-window)
          (.removeValue windows closed-window false))

        (if-not @have-windows-rendered
          ;; Sleep when no rendering was requested
          (try
            (Thread/sleep (/ 1000 (.idleFPS config)))
            (catch InterruptedException e
              ;; ignore
              nil))
          (when (pos? @target-framerate)
            (.sync sync @target-framerate)))))))

(defn cleanup-windows []
  (locking lifecycle-listeners
    (doseq [lifecycle-listener lifecycle-listeners]
      (.pause lifecycle-listener)
      (.dispose lifecycle-listener)))
  (doseq [window windows]
    (.dispose window))
  (.clear windows))

(defn cleanup []
  (Lwjgl3Cursor/disposeSystemCursors)
  (.dispose audio)
  (.free error-callback)
  (set! error-callback nil)
  (when gl-debug-callback
    (.free gl-debug-callback)
    (set! gl-debug-callback nil))
  (GLFW/glfwTerminate))

(defn create [listener config]
  (when (= SharedLibraryLoader/os Os/MacOsX)
    (.set Configuration/GLFW_LIBRARY_NAME "glfw_async"))
  (when (= (:gl-emulation config) :gl-emulation/angle-gles20)
    (load-angle))
  (let [application {:errorCallback (GLFWErrorCallback/createPrint System/err)
                     :glVersion ; is create at add window - ( initiateGL)
                     :glDebugCallback ; in createGlfw window
                     :config config
                     :windows (atom [])
                     :currentWindow (atom nil)
                     :audio (if (:disable-audio? config)
                              (MockAudio.)
                              (try (OpenALLwjgl3Audio. (:audioDeviceSimultaneousSources config)
                                                       (:audioDeviceBufferCount         config)
                                                       (:audioDeviceBufferSize          config))
                                   (catch Throwable t
                                     (.log this "Lwjgl3Application", "Couldn't initialize audio, disabling audio", t)
                                     (MockAudio.))))
                     :files (Lwjgl3Files.)
                     :net (Lwjgl3Net. config)
                     :preferences ; new ObjectMap<String, Preferences>();
                     :clipboard (Lwjgl3Clipboard.)
                     :logLevel Application/LOG_INFO
                     :applicationLogger (Lwjgl3ApplicationLogger.)
                     :running? true
                     :runnables new Array<Runnable>()
                     :executedRunnables new Array<Runnable>()
                     :lifecycleListeners new Array<LifecycleListener>()
                     :sync (Sync.)}]
    (println "initialize glfw")
    (initialize-glfw (:errorCallback application))
    ; !!!!

    ; (set! (.app Gdx) this)
    ; (set! (.audio Gdx) (:audio this))
    ; (set! (.files Gdx) (:files this))
    ; (set! (.net Gdx)  (:net this))

    ; !!!!
    )
  (let [window (createWindow1 this config listener 0)]
    (when (= (:gl-emulation config) :gl-emulation/angle-gles20)
      (post-load-angle))
    (.add windows window))
  (try
   (main-loop this)
   (cleanup-windows this)
   (catch Throwable t
     (if (instance? RuntimeException t) ; TODO check arg order right
       (throw (RunTimeException. t))
       (throw (GdxRuntimeException. t))))
   (finally
    (cleanup this))))

(defn -main []
  (create (proxy [ApplicationAdapter] [])
          {:config true}
          )
  )
