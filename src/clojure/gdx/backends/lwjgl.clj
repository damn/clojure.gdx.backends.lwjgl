(ns clojure.gdx.backends.lwjgl
  "Clojure API for `com.badlogic.gdx.backends.lwjgl3`.

  # Application options

  | Key                | Description | Default value |
  | --------           | -------     | -------       |
  | `:audio`    | Sets the audio device configuration. <br> Parameters: <br> `:simultaneous-sources` - the maximum number of sources that can be played simultaniously <br> `:buffer-size` - the audio device buffer size in samples <br> `:buffer-count` - the audio device buffer count | `{:simultaneous-sources 16, :buffer-size 512, :buffer-count 9}` |
   | `:disable-audio?`  | Whether to disable audio or not. If set to true, the returned audio class instances like Audio or Music will be mock implementations.    | `false` |
   | `:max-net-threads` | Sets the maximum number of threads to use for network requests.     | `Integer/MAX_VALUE` |
  | `:opengl-emulation`    |  Sets which OpenGL version to use to emulate OpenGL ES. If the given major/minor version is not supported, the backend falls back to OpenGL ES 2.0 emulation through OpenGL 2.0. The default parameters for major and minor should be 3 and 2 respectively to be compatible with Mac OS X. Specifying major version 4 and minor version 2 will ensure that all OpenGL ES 3.0 features are supported. Note however that Mac OS X does only support 3.2. <br> <br> Parameters: <br> `:gl-version` - which OpenGL ES emulation version to use (Options are: `:angle-gles20`, `:gl20`, `:gl31` and `:gl32`. <br> `:gles-3-major-version` - OpenGL ES major version <br> `:gles-3-minor-version` - OpenGL ES minor version <br> See Also: <br> https://legacy.lwjgl.org/javadoc/org/lwjgl/opengl/ContextAttribs.html   | `{:gl-version :gl20, :gles-3-major-version 3, :gles-3-minor-version 2}` |
  | `:backbuffer`    | Sets the bit depth of the color, depth and stencil buffer as well as multi-sampling. <br> <br> Parameters: <br> `:r` - red bits <br> `:g` - green bits <br> `:b` - blue bits <br> `:a` - alpha bits <br> `:depth` - depth bits <br> `:stencil` - stencil bits <br> `:samples` - MSAA samples | `{:r 8, :g 8, :b 8, :a 8, :depth 16, :stencil 0, :samples 0}`|
  | `:transparent-framebuffer`    | Set transparent window hint. Results may vary on different OS and GPUs. Usage with the ANGLE backend is less consistent.    | `false` |
  | `:idle-fps`    | Sets the polling rate during idle time in non-continuous rendering mode. Must be positive.     | `60` |
  | `:foreground-fps`    | Sets the target framerate for the application. The CPU sleeps as needed. Must be positive. Use 0 to never sleep.   | `0` |
  | `:pause-when-minimized?`    | Sets whether to [[clojure.gdx.application/pause]] the application and fire LifecycleListener.pause()/LifecycleListener.resume() events on when window is minimized/restored.    | `true` |
  | `:pause-when-lost-focus?`    | Sets whether to [[clojure.gdx.application/pause]] the applicationand fire LifecycleListener.pause()/LifecycleListener.resume() events on when window loses/gains focus.    | `false` |
  | `:preferences`    | Sets the directory where Preferences will be stored, as well as the file type to be used to store them. Defaults to \"$USER_HOME/.prefs/\" and Files.FileType.External.    |
  | `:hdpi-mode`    | Defines how HDPI monitors are handled. Operating systems may have a per-monitor HDPI scale setting. The operating system may report window width/height and mouse coordinates in a logical coordinate system at a lower resolution than the actual physical resolution. This setting allows you to specify whether you want to work in logical or raw pixel units. See HdpiMode for more information. Note that some OpenGL functions like GL20.glViewport(int, int, int, int) and GL20.glScissor(int, int, int, int) require raw pixel units. Use HdpiUtils to help with the conversion if HdpiMode is set to HdpiMode.Logical. Defaults to HdpiMode.Logical.    |
  | `:gl-debug-output?`    | Enables use of OpenGL debug message callbacks. If not supported by the core GL driver (since GL 4.3), this uses the KHR_debug, ARB_debug_output or AMD_debug_output extension if available. By default, debug messages with NOTIFICATION severity are disabled to avoid log spam. You can call with System.err to output to the \"standard\" error output stream. Use Lwjgl3Application.setGLDebugMessageControl(Lwjgl3Application.GLDebugMessageSeverity, boolean) to enable or disable other severity debug levels.    |

  # Window options

  | Key                         | Description | Default value |
  | ----------------------------| ----------  | ------------  |
  | `:initial-visible?`         | whether the window will be visible on creation | `true` |
  | `:windowed-mode`            | Sets the app to use windowed mode. |  `{:width 640 :height 480}` |
  | `:resizable?`               | whether the windowed mode window is resizable | `true` |
  | `:decorated?`               |  whether the windowed mode window is decorated, i.e. displaying the title bars | `true` |
  | `:maximized?`               | whether the window starts maximized. Ignored if the window is full screen.  | `false` |
  | `maximized-monitor`         | what monitor the window should maximize to | - |
  | `:auto-iconify?`            | whether the window should automatically iconify and restore previous video mode on input focus loss. Does nothing in windowed mode. | `true` |
  | `:window-position`          |  Sets the position of the window in windowed mode. Default -1 for both coordinates for centered on primary monitor. | `` |
  | `:window-size-limits`       | Sets minimum and maximum size limits for the window. If the window is full screen or not resizable, these limits are ignored. The default for all four parameters is -1, which means unrestricted. | `` |
  | `:window-icon`              | Sets the icon that will be used in the window's title bar. Has no effect in macOS, which doesn't use window icons. | `` |
  | `:windowed-listener`        | Sets the {@link Lwjgl3WindowListener} which will be informed about iconficiation, focus loss and window close events. | `` |
  | `:fullscreen-mode`          |  Sets the app to use fullscreen mode. Use the static methods like {@link Lwjgl3ApplicationConfiguration#getDisplayMode()} on this class to enumerate connected monitors and their fullscreen display modes. |
  | `:title`                    | Sets the window title. If null, the application listener's class name is used.  | `` |
  | `:initial-background-color` | Sets the initial background color. Defaults to black.  | `` |
  | `:vsync?`                   | Sets whether to use vsync. This setting can be changed anytime at runtime via {@link Graphics#setVSync(boolean)}. For multi-window applications, only one (the main) window should enable vsync. Otherwise, every window will wait for the vertical blank on swap individually, effectively cutting the frame rate to (refreshRate / numberOfWindows). | `` |"
  (:require [clojure.gdx.application :as app])
  (:import (com.badlogic.gdx ApplicationListener)
           (com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application
                                             Lwjgl3ApplicationConfiguration
                                             Lwjgl3ApplicationConfiguration$GLEmulation
                                             Lwjgl3Graphics$Lwjgl3DisplayMode
                                             Lwjgl3Graphics$Lwjgl3Monitor
                                             Lwjgl3WindowConfiguration)
           (java.lang.reflect Constructor)))

(defn- display-mode->map [^Lwjgl3Graphics$Lwjgl3DisplayMode display-mode]
  {:width          (.width        display-mode)
   :height         (.height       display-mode)
   :refresh-rate   (.refreshRate  display-mode)
   :bits-per-pixel (.bitsPerPixel display-mode)
   :monitor-handle (.getMonitor   display-mode)})

(defn- monitor->map [^Lwjgl3Graphics$Lwjgl3Monitor monitor]
  {:virtual-x      (.virtualX         monitor)
   :virtual-y      (.virtualY         monitor)
   :name           (.name             monitor)
   :monitor-handle (.getMonitorHandle monitor)})

(defn- map->display-mode [{:keys [width height refresh-rate bits-per-pixel monitor-handle]}]
  (let [constructor (.getDeclaredConstructor Lwjgl3Graphics$Lwjgl3DisplayMode
                                             (into-array Class [Long/TYPE Integer/TYPE Integer/TYPE Integer/TYPE Integer/TYPE]))
        _ (.setAccessible constructor true)]
    (.newInstance constructor
                  (into-array Object [monitor-handle width height refresh-rate bits-per-pixel]))))

(defn- map->monitor [{:keys [virtual-x virtual-y name monitor-handle]}]
  (let [constructor (.getDeclaredConstructor Lwjgl3Graphics$Lwjgl3Monitor
                                             (into-array Class [Long/TYPE Integer/TYPE Integer/TYPE String]))
        _ (.setAccessible constructor true)]
    (.newInstance constructor
                  (into-array Object [monitor-handle virtual-x virtual-y name]))))

(defn display-mode
  "The currently active display-mode of the primary or the given monitor."
  ([monitor]
   (display-mode->map (Lwjgl3ApplicationConfiguration/getDisplayMode (map->monitor monitor))))
  ([]
   (display-mode->map (Lwjgl3ApplicationConfiguration/getDisplayMode))))

(defn display-modes
  "The available display-modes of the primary or the given monitor."
  ([monitor]
   (map display-mode->map (Lwjgl3ApplicationConfiguration/getDisplayModes (map->monitor monitor))))
  ([]
   (map display-mode->map (Lwjgl3ApplicationConfiguration/getDisplayModes))))

(defn primary-monitor
  "the primary monitor."
  []
  (monitor->map (Lwjgl3ApplicationConfiguration/getPrimaryMonitor)))

(defn monitors
  "The connected monitors."
  []
  (map monitor->map (Lwjgl3ApplicationConfiguration/getMonitors)))

(defn- gdx-listener [listener]
  (proxy [ApplicationListener] []
    (create []
      (app/create listener))
    (dispose []
      (app/dispose listener))
    (pause []
      (app/pause listener))
    (render []
      (app/render listener))
    (resize [width height]
      (app/resize listener width height))
    (resume []
      (app/resume listener))))

(defn- k->glversion [gl-version]
  (case gl-version
    :angle-gles20 Lwjgl3ApplicationConfiguration$GLEmulation/ANGLE_GLES20
    :gl20         Lwjgl3ApplicationConfiguration$GLEmulation/GL20
    :gl30         Lwjgl3ApplicationConfiguration$GLEmulation/GL30
    :gl31         Lwjgl3ApplicationConfiguration$GLEmulation/GL31
    :gl32         Lwjgl3ApplicationConfiguration$GLEmulation/GL32))

(defn- set-window-config-key! [^Lwjgl3WindowConfiguration object k v]
  (case k
    :initial-visible? (.setInitialVisible object (boolean v))
    :windowed-mode   (.setWindowedMode object
                                       (int (:width v))
                                       (int (:height v)))
    :resizable? (.setResizable object (boolean v))
    :decorated? (.setDecorated object (boolean v))
    :maximized? (.setMaximized object (boolean v))
    :maximized-monitor (.setMaximizedMonitor object (map->monitor v))
    :auto-iconify? (.setAutoIconify object (boolean v))

    ;:window-position (.setWindowPosition object ())

    ;(int minWidth, int minHeight, int maxWidth, int maxHeight)
    ;:window-size-limits (.setWindowSizeLimits object ())

    ; (String... filePaths)
    ; (FileType fileType, String... filePaths)
    ;:window-icon (.setWindowIcon object ())

    ;:window-listener (.setWindowListener object ())

    ;:initial-background-color (.setInitialBackgroundColorer object color #_(->munge-color v))

    :fullscreen-mode (.setFullscreenMode object (map->display-mode v))
    :title (.setTitle object (str v))
    :vsync? (.useVsync object (boolean v))))

(defn- set-application-config-key! [^Lwjgl3ApplicationConfiguration object k v]
  (case k
    :audio (.setAudioConfig object
                            (int (:simultaneous-sources v))
                            (int (:buffer-size         v))
                            (int (:buffer-count        v)))
    :disable-audio? (.disableAudio object (boolean v))
    :max-net-threads (.setMaxNetThreads object (int v))
    :opengl-emulation (.setOpenGLEmulation object
                                           (k->glversion (:gl-version v))
                                           (int (:gles-3-major-version v))
                                           (int (:gles-3-minor-version v)))
    :backbuffer (.setBackBufferConfig object
                                      (int (:r       v))
                                      (int (:g       v))
                                      (int (:b       v))
                                      (int (:a       v))
                                      (int (:depth   v))
                                      (int (:stencil v))
                                      (int (:samples v)))
    :transparent-framebuffer (.setTransparentFramebuffer object (boolean v))
    :idle-fps (.setIdleFPS object (int v))
    :foreground-fps (.setForegroundFPS object (int v))
    :pause-when-minimized? (.setPauseWhenMinimized object (boolean v))
    :pause-when-lost-focus? (.setPauseWhenLostFocus object (boolean v))

    #_(defmethod set-option! :preferences [object _ v]
        (.setPreferencesConfig object
                               (str (:directory v))
                               ; com.badlogic.gdx.Files.FileType
                               (k->filetype (:filetype v))))

    #_(defmethod set-option! :hdpi-mode [object _ v]
        ; com.badlogic.gdx.graphics.glutils.HdpiMode
        (.setHdpiMode object (k->hdpi-mode v)))

    #_(defmethod set-option! :gl-debug-output? [object _ v]
        (.enableGLDebugOutput object
                              (boolean (:enable? v))
                              (->PrintStream (:debug-output-stream v))))

    (set-window-config-key! object k v)))

(defn- configure-object [object config set-key-fn]
  (doseq [[k v] config]
    (set-key-fn object k v))
  object)

(defn- window-configuration [config]
  (configure-object (Lwjgl3WindowConfiguration.) config set-window-config-key!))

(defn- application-configuration [config]
  (configure-object (Lwjgl3ApplicationConfiguration.) config set-application-config-key!))

(defn application
  "See [[clojure.gdx.application/Listener]]."
  ([listener]
   (application listener nil))
  ([listener config]
   (Lwjgl3Application. (gdx-listener listener)
                       (application-configuration config))))

(defn window
  "Creates a new Lwjgl3Window using the provided listener and Lwjgl3WindowConfiguration. This function only just instantiates a Lwjgl3Window and returns immediately. The actual window creation is postponed with Application.postRunnable(Runnable) until after all existing windows are updated."
  [application listener config]
  (Lwjgl3Application/.newWindow application
                                (gdx-listener listener)
                                (window-configuration config)))

(defn set-gl-debug-message-control
  "Enables or disables GL debug messages for the specified severity level. Returns false if the severity level could not be set (e.g. the NOTIFICATION level is not supported by the ARB and AMD extensions). See Lwjgl3ApplicationConfiguration.enableGLDebugOutput(boolean, PrintStream)"
  [severity enabled?]
  (Lwjgl3Application/setGLDebugMessageControl severity #_(k->severity severity)
                                              (boolean enabled?)))
