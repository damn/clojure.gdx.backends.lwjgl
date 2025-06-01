(ns clojure.gdx.backends.lwjgl
  (:require [clojure.java.io :as io])
  (:import (com.badlogic.gdx Gdx)
           (com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application
                                             Lwjgl3ApplicationConfiguration
                                             Lwjgl3ApplicationConfiguration$GLEmulation
                                             Lwjgl3ApplicationLogger
                                             Lwjgl3Clipboard
                                             Lwjgl3Graphics$Lwjgl3DisplayMode
                                             Lwjgl3Graphics$Lwjgl3Monitor
                                             Lwjgl3WindowConfiguration
                                             Lwjgl3Net
                                             Sync)
           (com.badlogic.gdx.backends.lwjgl3.audio.mock MockAudio)
           (com.badlogic.gdx.utils SharedLibraryLoader
                                   Os)
           (java.awt Taskbar
                     Toolkit)
           (org.lwjgl.system Configuration)))

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

(defn- k->glversion [gl-version]
  (case gl-version
    :angle-gles20 Lwjgl3ApplicationConfiguration$GLEmulation/ANGLE_GLES20
    :gl20         Lwjgl3ApplicationConfiguration$GLEmulation/GL20
    :gl30         Lwjgl3ApplicationConfiguration$GLEmulation/GL30
    :gl31         Lwjgl3ApplicationConfiguration$GLEmulation/GL31
    :gl32         Lwjgl3ApplicationConfiguration$GLEmulation/GL32))

; TODO tests for all keys / gl emulation / etc ?

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
    :window-position (.setWindowPosition object
                                         (int (:x v))
                                         (int (:y v)))
    :window-size-limits (.setWindowSizeLimits object
                                                (int (:min-width  v))
                                                (int (:min-height v))
                                                (int (:max-width  v))
                                                (int (:max-height v)))
    :window-icons (.setWindowIcon object
                                  ; filetype
                                  ; array of string of file icons
                                  )

    :window-listener (.setWindowListener object
                                         ; Lwjgl3WindowListener v
                                         )

    :initial-background-color (.setInitialBackgroundColorer object
                                                            #_(->munge-color v))

    :fullscreen-mode (.setFullscreenMode object (map->display-mode v))
    :title (.setTitle object (str v))
    :vsync? (.useVsync object (boolean v))))

(defn- set-application-config-key! [^Lwjgl3ApplicationConfiguration object k v]
  (case k
    :mac-os (when (= SharedLibraryLoader/os Os/MacOsX)
              (println "Setting mac-os options")
              (let [{:keys [glfw-async?
                            dock-icon] :as options} v]
                (println "options:" options)
                (when glfw-async?
                  (println "Setting glfw-async")
                  (.set Configuration/GLFW_LIBRARY_NAME "glfw_async"))
                (when dock-icon
                  (println "Setting dock-icon")
                  (.setIconImage (Taskbar/getTaskbar)
                                 (.getImage (Toolkit/getDefaultToolkit)
                                            (io/resource dock-icon))))))
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

    ; String preferencesDirectory, Files.FileType preferencesFileType
    #_(defmethod set-option! :preferences [object _ v]
        (.setPreferencesConfig object
                               (str (:directory v))
                               ; com.badlogic.gdx.Files.FileType
                               (k->filetype (:filetype v))))

    ; com.badlogic.gdx.graphics.glutils.HdpiMode/ 'Logical' / 'Pixels'
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

(defn application [config listener]
  (let [config (configure-object (Lwjgl3ApplicationConfiguration.)
                                 config
                                 set-application-config-key!)]
    (when (= (.glEmulation config)
             Lwjgl3ApplicationConfiguration$GLEmulation/ANGLE_GLES20)
      (Lwjgl3Application/loadANGLE))
    (Lwjgl3Application/initializeGlfw)
    (let [application (Lwjgl3Application.)]
      (.setApplicationLogger application (Lwjgl3ApplicationLogger.))
      (set! (.config application) config)
      (if (nil? (.title config))
        (set! (.title config) (.getSimpleName (class listener))))
      (set! Gdx/app application)
      (if (.disableAudio config)
        (set! (.audio application) (MockAudio.))
        (try
         (set! (.audio application) (.createAudio application config))
         (catch Throwable t
           (.log application "Lwjgl3Application" "Couldn't initialize audio, disabling audio" t)
           (set! (.audio application) (MockAudio.)))))
      (set! Gdx/audio (.audio application))
      (set! (.files application) (.createFiles application))
      (set! Gdx/files (.files application))
      (set! (.net application) (Lwjgl3Net. config))
      (set! Gdx/net (.net application))
      (set! (.clipboard application) (Lwjgl3Clipboard.))
      (set! (.sync application) (Sync.))
      (.setup application listener config))))

(defn window
  "Creates a new Lwjgl3Window using the provided listener and Lwjgl3WindowConfiguration. This function only just instantiates a Lwjgl3Window and returns immediately. The actual window creation is postponed with Application.postRunnable(Runnable) until after all existing windows are updated."
  [application listener config]
  (Lwjgl3Application/.newWindow application
                                listener ; TODO window-listener
                                (configure-object (Lwjgl3WindowConfiguration.)
                                                  config
                                                  set-window-config-key!)))

(defn set-gl-debug-message-control
  "Enables or disables GL debug messages for the specified severity level. Returns false if the severity level could not be set (e.g. the NOTIFICATION level is not supported by the ARB and AMD extensions). See Lwjgl3ApplicationConfiguration.enableGLDebugOutput(boolean, PrintStream)"
  [severity enabled?]
  (Lwjgl3Application/setGLDebugMessageControl severity #_(k->severity severity)
                                              (boolean enabled?)))
