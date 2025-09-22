(ns clojure.gdx.backends.lwjgl.window.configuration
  (:require [clojure.gdx.backends.lwjgl.graphics.display-mode :as display-mode]
            [clojure.gdx.backends.lwjgl.graphics.monitor :as monitor])
  (:import (com.badlogic.gdx.backends.lwjgl3 Lwjgl3WindowConfiguration)))

(defn set-key! [^Lwjgl3WindowConfiguration object k v]
  (case k
    :initial-visible? (.setInitialVisible object (boolean v))
    :windowed-mode   (.setWindowedMode object
                                       (int (:width v))
                                       (int (:height v)))
    :resizable? (.setResizable object (boolean v))
    :decorated? (.setDecorated object (boolean v))
    :maximized? (.setMaximized object (boolean v))
    :maximized-monitor (.setMaximizedMonitor object (monitor/javaize v))
    :auto-iconify? (.setAutoIconify object (boolean v))
    :window-position (.setWindowPosition object
                                         (int (:x v))
                                         (int (:y v)))
    :window-size-limits (.setWindowSizeLimits object
                                              (int (:min-width  v))
                                              (int (:min-height v))
                                              (int (:max-width  v))
                                              (int (:max-height v)))
    #_:window-icons #_(.setWindowIcon object ; TODO
                                  ; filetype
                                  ; array of string of file icons
                                  )
    #_:window-listener #_(.setWindowListener object
                                         ; Lwjgl3WindowListener v
                                         )
    :initial-background-color (.setInitialBackgroundColor object v)
    :fullscreen-mode (.setFullscreenMode object (display-mode/javaize v))
    :title (.setTitle object (str v))
    :vsync? (.useVsync object (boolean v))))

(defn create [config]
  (let [obj (Lwjgl3WindowConfiguration.)]
    (doseq [[k v] config]
      (set-key! obj k v))
    obj))
