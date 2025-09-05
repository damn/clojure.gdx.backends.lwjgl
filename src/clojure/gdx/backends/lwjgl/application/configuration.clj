(ns clojure.gdx.backends.lwjgl.application.configuration
  (:require [clojure.gdx.backends.lwjgl.application.configuration.gl-emulation :as gl-emulation]
            [clojure.gdx.backends.lwjgl.graphics.display-mode :as display-mode]
            [clojure.gdx.backends.lwjgl.graphics.monitor :as monitor]
            [clojure.gdx.backends.lwjgl.window.configuration :as window-config])
  (:import (com.badlogic.gdx.backends.lwjgl3 Lwjgl3ApplicationConfiguration)))

(defn- set-key! [^Lwjgl3ApplicationConfiguration object k v]
  (case k
    :audio (.setAudioConfig object
                            (int (:simultaneous-sources v))
                            (int (:buffer-size         v))
                            (int (:buffer-count        v)))
    :disable-audio? (.disableAudio object (boolean v))
    :max-net-threads (.setMaxNetThreads object (int v))
    :opengl-emulation (.setOpenGLEmulation object
                                           (gl-emulation/k->value (:gl-version v))
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
    (window-config/set-key! object k v)))

(defn create [config]
  (let [obj (Lwjgl3ApplicationConfiguration.)]
    (doseq [[k v] config]
      (set-key! obj k v))
    obj))

(defn display-mode!
  ([monitor]
   (display-mode/clojurize (Lwjgl3ApplicationConfiguration/getDisplayMode (monitor/javaize monitor))))
  ([]
   (display-mode/clojurize (Lwjgl3ApplicationConfiguration/getDisplayMode))))

(defn display-modes!
  ([monitor]
   (map display-mode/clojurize (Lwjgl3ApplicationConfiguration/getDisplayModes (monitor/javaize monitor))))
  ([]
   (map display-mode/clojurize (Lwjgl3ApplicationConfiguration/getDisplayModes))))

(defn primary-monitor!
  []
  (monitor/clojurize (Lwjgl3ApplicationConfiguration/getPrimaryMonitor)))

(defn monitors!
  []
  (map monitor/clojurize (Lwjgl3ApplicationConfiguration/getMonitors)))
