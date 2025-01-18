(ns clojure.gdx.backends.lwjgl
  "# Application options

  | Key                | Description | Default value |
  | --------           | -------     | -------       |
  | `:audio`    | Sets the audio device configuration. <br> Parameters: <br> `:simultaneous-sources` - the maximum number of sources that can be played simultaniously (default 16) <br> `:buffer-size` - the audio device buffer size in samples (default 512) <br> `:buffer-count` - the audio device buffer count (default 9)     | |
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
  | `:hdpi-mode`    | Defines how HDPI monitors are handled. Operating systems may have a per-monitor HDPI scale setting. The operating system may report window width/height and mouse coordinates in a logical coordinate system at a lower resolution than the actual physical resolution. This setting allows you to specify whether you want to work in logical or raw pixel units. See {@link HdpiMode} for more information. Note that some OpenGL functions like {@link GL20#glViewport(int, int, int, int)} and {@link GL20#glScissor(int, int, int, int)} require raw pixel units. Use {@link HdpiUtils} to help with the conversion if HdpiMode is set to {@link HdpiMode#Logical}. Defaults to {@link HdpiMode#Logical}. |

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
  | `:fullscreen-mode`          |  Sets the app to use fullscreen mode. Use the static methods like {@link Lwjgl3ApplicationConfiguration#getDisplayMode()} on
  | `:title`                    | Sets the window title. If null, the application listener's class name is used.  | `` |
  | `:initial-background-color` | Sets the initial background color. Defaults to black.  | `` |
  | `:vsync?`                   | Sets whether to use vsync. This setting can be changed anytime at runtime via {@link Graphics#setVSync(boolean)}. For multi-window applications, only one (the main) window should enable vsync. Otherwise, every window will wait for the vertical blank on swap individually, effectively cutting the frame rate to (refreshRate / numberOfWindows). | `` |"
  (:require [clojure.app :as app])
  (:import (com.badlogic.gdx ApplicationListener)
           (com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application
                                             Lwjgl3ApplicationConfiguration
                                             Lwjgl3ApplicationConfiguration$GLEmulation
                                             Lwjgl3WindowConfiguration)))

(defn gdx-listener [listener]
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

(defmulti ^:private set-option!
  (fn [_object k _v]
    k))

(defn- build-config [object options]
  (doseq [[k v] options]
    (set-option! object k v))
  object)

(defn application
  "See [[clojure.app/Listener]]."
  ([listener]
   (application listener nil))
  ([listener config]
   (Lwjgl3Application. (gdx-listener listener)
                       (build-config (Lwjgl3ApplicationConfiguration.) config))))

(defn window
  "Creates a new Lwjgl3Window using the provided listener and Lwjgl3WindowConfiguration. This function only just instantiates a Lwjgl3Window and returns immediately. The actual window creation is postponed with Application.postRunnable(Runnable) until after all existing windows are updated."
  [application listener config]
  (Lwjgl3Application/.newWindow application
                                (gdx-listener listener)
                                (build-config (Lwjgl3WindowConfiguration.) config)))

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

(defmethod set-option! :initial-visible? [config _ v]
  (.setInitialVisible config (boolean v)))

(defmethod set-option! :disable-audio? [config _ v]
  (.disableAudio config (boolean v)))

(defmethod set-option! :max-net-threads [config _ v]
  (.setMaxNetThreads config (int v)))

(defmethod set-option! :audio [config _ v]
  (.setAudioConfig config
                   (int (:simultaneous-sources v))
                   (int (:buffer-size         v))
                   (int (:buffer-count        v))))

(defmethod set-option! :opengl-emulation [config _ v]
  (.setOpenGLEmulation config
                       (k->glversion (:gl-version v))
                       (int (:gles-3-major-version v))
                       (int (:gles-3-minor-version v))))

(defmethod set-option! :backbuffer [config _ v]
  (.setBackBufferConfig config
                        (int (:r       v))
                        (int (:g       v))
                        (int (:b       v))
                        (int (:a       v))
                        (int (:depth   v))
                        (int (:stencil v))
                        (int (:samples v))))

(defmethod set-option! :transparent-framebuffer [config _ v]
  (.setTransparentFramebuffer config (boolean v)))

(defmethod set-option! :idle-fps [config _ v]
  (.setIdleFPS config (int v)))

(defmethod set-option! :foreground-fps [config _ v]
  (.setForegroundFPS config (int v)))

(defmethod set-option! :pause-when-minimized? [config _ v]
  (.setPauseWhenMinimized config (boolean v)))

(defmethod set-option! :pause-when-lost-focus? [config _ v]
  (.setPauseWhenLostFocus config (boolean v)))

#_(defmethod set-option! :preferences [config _ v]
  (.setPreferencesConfig config
                         (str (:directory v))
                         ; com.badlogic.gdx.Files.FileType
                         (k->filetype (:filetype v))))

#_(defmethod set-option! :hdpi-mode [config _ v]
  ; com.badlogic.gdx.graphics.glutils.HdpiMode
  (.setHdpiMode config (k->hdpi-mode v)))

#_(defmethod set-option! :gl-debug-output? [config _ v]
  (.enableGLDebugOutput config
                        (boolean (:enable? v))
                        (->PrintStream (:debug-output-stream v))))

(defmethod set-option! :windowed-mode [config _ v]
  (.setWindowedMode config
                    (int (:width v))
                    (int (:height v))))

(defmethod set-option! :resizable? [config _ v]
  (.setResizable config (boolean v)))

(defmethod set-option! :decorated? [config _ v]
  (.setDecorated config (boolean v)))

(defmethod set-option! :maximized? [config _ v]
  (.setMaximized config (boolean v)))

; Graphics.Monitor
#_(defmethod set-option! :maximized-monitor [config _ v]
  (.setMaximizedMonitor config ()))

(defmethod set-option! :auto-iconify? [config _ v]
  (.setAutoIconify config (boolean v)))

; (int x, int y)
#_(defmethod set-option! :window-position [config _ v]
  (.setWindowPosition config ()))

;(int minWidth, int minHeight, int maxWidth, int maxHeight)
#_(defmethod set-option! :window-size-limits [config _ v]
  (.setWindowSizeLimits config ()))

; (String... filePaths)
; (FileType fileType, String... filePaths)
#_(defmethod set-option! :window-icon [config _ v]
  (.setWindowIcon config ())) ; TODO multiple options

; Lwjgl3WindowListener
#_(defmethod set-option! :window-listener [config _ v]
  (.setWindowListener config ()))

; com.badlogic.gdx.Graphics$DisplayMode (the one you got ...)
; or convert ?!
(defmethod set-option! :fullscreen-mode [config _ display-mode]
  (.setFullscreenMode config display-mode))

(defmethod set-option! :title [config _ v]
  (.setTitle config (str v)))

(defmethod set-option! :initial-background-color [config _ color]
  (.setInitialBackgroundColorer config color
                                #_(->munge-color v)
                                ))

(defmethod set-option! :vsync? [config _ v]
  (.useVsync config (boolean v)))
