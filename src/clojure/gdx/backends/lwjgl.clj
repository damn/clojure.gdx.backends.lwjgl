; TODO tests
(ns clojure.gdx.backends.lwjgl
  ; * `Audio` / `Music` Have to become links !
  ; * TODO what about 'type' of the required input?, boolean,e tc.
  "

  | Key                | Description | Default value |
  | --------           | -------     | -------       |
  | `:audio`    | Sets the audio device configuration.  <br> Parameters: <br> simultaneousSources - the maximum number of sources that can be played simultaniously (default 16) <br> bufferSize - the audio device buffer size in samples (default 512) <br> bufferCount - the audio device buffer count (default 9)     | |
  | `:disable-audio?`  | Whether to disable audio or not. If set to true, the returned audio class instances like Audio or Music will be mock implementations.    | `false` |
  | `:max-net-threads` | Sets the maximum number of threads to use for network requests.     | `Integer/MAX_VALUE` |
  | `:opengl-emulation`    |  Sets which OpenGL version to use to emulate OpenGL ES. If the given major/minor version is not supported, the backend falls back to OpenGL ES 2.0 emulation through OpenGL 2.0. The default parameters for major and minor should be 3 and 2 respectively to be compatible with Mac OS X. Specifying major version 4 and minor version 2 will ensure that all OpenGL ES 3.0 features are supported. Note however that Mac OS X does only support 3.2. <br> <br> Parameters: <br> glVersion - which OpenGL ES emulation version to use <br> gles3MajorVersion - OpenGL ES major version, use 3 as default <br> gles3MinorVersion - OpenGL ES minor version, use 2 as default <br> See Also: <br> LWJGL OSX ContextAttribs note   |
  | `:backbuffer`    | Sets the bit depth of the color, depth and stencil buffer as well as multi-sampling. <br> <br> Parameters: <br> r - red bits (default 8) <br> g - green bits (default 8) <br> b - blue bits (default 8) <br> a - alpha bits (default 8) <br> depth - depth bits (default 16) <br> stencil - stencil bits (default 0) <br> samples - MSAA samples (default 0)   |
  | `:transparent-framebuffer`    | Set transparent window hint. Results may vary on different OS and GPUs. Usage with the ANGLE backend is less consistent.    |
  | `:idle-fps`    | Sets the polling rate during idle time in non-continuous rendering mode. Must be positive. Default is 60.    |
  | `:foreground-fps`    | Sets the target framerate for the application. The CPU sleeps as needed. Must be positive. Use 0 to never sleep. Default is 0.    |
  | `:pause-when-minimized?`    | Sets whether to pause the application ApplicationListener.pause() and fire LifecycleListener.pause()/LifecycleListener.resume() events on when window is minimized/restored.    |
  | `:pause-when-lost-focus?`    | Sets whether to pause the application ApplicationListener.pause() and fire LifecycleListener.pause()/LifecycleListener.resume() events on when window loses/gains focus.    |
  | `:preferences`    | Sets the directory where Preferences will be stored, as well as the file type to be used to store them. Defaults to \"$USER_HOME/.prefs/\" and Files.FileType.External.    |
  | `:hdpi-mode`    | Defines how HDPI monitors are handled. Operating systems may have a per-monitor HDPI scale setting. The operating system may report window width/height and mouse coordinates in a logical coordinate system at a lower resolution than the actual physical resolution. This setting allows you to specify whether you want to work in logical or raw pixel units. See HdpiMode for more information. Note that some OpenGL functions like GL20.glViewport(int, int, int, int) and GL20.glScissor(int, int, int, int) require raw pixel units. Use HdpiUtils to help with the conversion if HdpiMode is set to HdpiMode.Logical. Defaults to HdpiMode.Logical.    |
  | `:gl-debug-output?`    | Enables use of OpenGL debug message callbacks. If not supported by the core GL driver (since GL 4.3), this uses the KHR_debug, ARB_debug_output or AMD_debug_output extension if available. By default, debug messages with NOTIFICATION severity are disabled to avoid log spam. You can call with System.err to output to the \"standard\" error output stream. Use Lwjgl3Application.setGLDebugMessageControl(Lwjgl3Application.GLDebugMessageSeverity, boolean) to enable or disable other severity debug levels.    |
  | `:hdpi-mode`    | $420    |
  | window config | | |
  | `:vsync?` | Sets whether to use vsync. This setting can be changed anytime at runtime via {@link Graphics#setVSync(boolean)}. <br> For multi-window applications, only one (the main) window should enable vsync. Otherwise, every window will wait for the vertical blank on swap individually, effectively cutting the frame rate to (refreshRate / numberOfWindows) | `true`

  # Window Configuration

  * `:initial-visible?` - whether the window will be visible on creation. (default true)

  * `:title`

  * `:windowed-mode`

  * `:resizable?`

  * `:decorated?`

  * `:maximized?`
  "
  (:import (com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application
                                             Lwjgl3ApplicationConfiguration
                                             Lwjgl3ApplicationConfiguration$GLEmulation)))

(defmulti ^:private set-option!
  (fn [k _v _config]
    k))

; TODO option w/o config also possible -> taking default then
(defn application
  "`com.badlogic.gdx.ApplicationListener`"
  ([listener]
   (Lwjgl3Application. listener))
  ([listener config]
   (Lwjgl3Application. listener
                       (let [options config
                             config (Lwjgl3ApplicationConfiguration.)]
                         (doseq [[k v] options]
                           (set-option! k v config))
                         config))))

(defn window
  "Creates a new Lwjgl3Window using the provided listener and Lwjgl3WindowConfiguration. This function only just instantiates a Lwjgl3Window and returns immediately. The actual window creation is postponed with Application.postRunnable(Runnable) until after all existing windows are updated."
  [application listener window-config]
  (Lwjgl3Application/.newWindow application
                                listener

                                ; TODO build window config!
                                ; => no application config settings !
                                ; so 2 calls?
                                window-config))

(defn set-gl-debug-message-control
  "Enables or disables GL debug messages for the specified severity level. Returns false if the severity level could not be set (e.g. the NOTIFICATION level is not supported by the ARB and AMD extensions). See Lwjgl3ApplicationConfiguration.enableGLDebugOutput(boolean, PrintStream)"
  [severity enabled?]
  (Lwjgl3Application/setGLDebugMessageControl severity #_(k->severity severity)
                                              (boolean enabled?)))

(defn display-mode
  "the currently active Graphics.DisplayMode of the primary or the given monitor"
  ([monitor]
   (Lwjgl3ApplicationConfiguration/getDisplayMode monitor))
  ([]
   (Lwjgl3ApplicationConfiguration/getDisplayMode)))

(defn display-modes
  "the available Graphics.DisplayModes of the primary or the given monitor"
  ([monitor]
   (Lwjgl3ApplicationConfiguration/getDisplayModes monitor))
  ([]
   (Lwjgl3ApplicationConfiguration/getDisplayModes)))

(comment
 (first (display-modes))
 )

(defn primary-monitor
  "the primary Graphics.Monitor"
  []
  (Lwjgl3ApplicationConfiguration/getPrimaryMonitor))

(defn monitors
  "the connected Graphics.Monitors"
  []
  (Lwjgl3ApplicationConfiguration/getMonitors))

(defn- k->glversion [gl-version]
  (case gl-version
    ; TODO in the first case need to draw in the library (error cant find lib)
    :angle-gles20 Lwjgl3ApplicationConfiguration$GLEmulation/ANGLE_GLES20
    :gl20         Lwjgl3ApplicationConfiguration$GLEmulation/GL20
    :gl30         Lwjgl3ApplicationConfiguration$GLEmulation/GL30

    ; java.lang.IllegalArgumentException: Error compiling shader: Vertex shader
    :gl31         Lwjgl3ApplicationConfiguration$GLEmulation/GL31

    ; shader compile error ...
    :gl32         Lwjgl3ApplicationConfiguration$GLEmulation/GL32))

(defmethod set-option! :initial-visible? [_ v config]
  (.setInitialVisible config (boolean v)))

(defmethod set-option! :disable-audio? [_ v config]
  (.disableAudio      config (boolean v)))

(defmethod set-option! :max-net-threads [_ v config]
  (.setMaxNetThreads  config (int v)))

(defmethod set-option! :audio [_ v config]
  (.setAudioConfig    config
                   (int (:simultaneousSources v))
                   (int (:buffer-size         v))
                   (int (:buffer-count        v))))

(defmethod set-option! :opengl-emulation [_ v config]
  (.setOpenGLEmulation config
                       (k->glversion (:gl-version v))
                       (int (:gles-3-major-version v))
                       (int (:gles-3-minor-version v))))

(defmethod set-option! :backbuffer [_ v config]
  (.setBackBufferConfig config
                        (int (:r       v))
                        (int (:g       v))
                        (int (:b       v))
                        (int (:a       v))
                        (int (:depth   v))
                        (int (:stencil v))
                        (int (:samples v))))

(defmethod set-option! :transparent-framebuffer [_ v config]
  (.setTransparentFramebuffer config (boolean v)))

(defmethod set-option! :idle-fps [_ v config]
  (.setIdleFPS config (int v)))

(defmethod set-option! :foreground-fps [_ v config]
  (.setForegroundFPS config (int v)))

(defmethod set-option! :pause-when-minimized? [_ v config]
  (.setPauseWhenMinimized config (boolean v)))

(defmethod set-option! :pause-when-lost-focus? [_ v config]
  (.setPauseWhenLostFocus config (boolean v)))

#_(defmethod set-option! :preferences [_ v config]
  (.setPreferencesConfig config
                         (str (:directory v))
                         ; com.badlogic.gdx.Files.FileType
                         (k->filetype (:filetype v))))

#_(defmethod set-option! :hdpi-mode [_ v config]
  ; com.badlogic.gdx.graphics.glutils.HdpiMode
  (.setHdpiMode config (k->hdpi-mode v)))

#_(defmethod set-option! :gl-debug-output? [_ v config]
  (.enableGLDebugOutput config
                        (boolean (:enable? v))
                        (->PrintStream (:debug-output-stream v))))

; TODO duplicated below
(defmethod set-option! :title [_ v config]
  (.setTitle config (str v)))

(defmethod set-option! :windowed-mode [_ v config]
  (.setWindowedMode config
                    (int (:width v))
                    (int (:height v))))

(defmethod set-option! :resizable? [_ v config]
  (.setResizable config (boolean v)))

(defmethod set-option! :decorated? [_ v config]
  (.setDecorated config (boolean v)))

(defmethod set-option! :maximized? [_ v config]
  (.setMaximized config (boolean v)))

; Graphics.Monitor
#_(defmethod set-option! :maximized-monitor [_ v config]
  (.setMaximizedMonitor config ()))

(defmethod set-option! :auto-iconify? [_ v config]
  (.setAutoIconify config (boolean v)))

; (int x, int y)
#_(defmethod set-option! :window-position [_ v config]
  (.setWindowPosition config ()))

;(int minWidth, int minHeight, int maxWidth, int maxHeight)
#_(defmethod set-option! :window-size-limits? [_ v config]
  (.setWindowSizeLimits config ()))

; (String... filePaths)
; (FileType fileType, String... filePaths)
#_(defmethod set-option! :window-icon [_ v config]
  (.setWindowIcon config ())) ; TODO multiple options

; Lwjgl3WindowListener
#_(defmethod set-option! :window-listener [_ v config]
  (.setWindowListener config ()))

; com.badlogic.gdx.Graphics$DisplayMode (the one you got ...)
; or convert ?!
(defmethod set-option! :fullscreen-mode [_ display-mode config]
  (.setFullscreenMode config display-mode))

(defmethod set-option! :title [_ v config]
  (.setTitle config (str v)))

(defmethod set-option! :initial-background-color [_ color config]
  (.setInitialBackgroundColorer config color
                                #_(->munge-color v)
                                ))

(defmethod set-option! :vsync? [_ v config]
  (.useVsync config (boolean v)))
