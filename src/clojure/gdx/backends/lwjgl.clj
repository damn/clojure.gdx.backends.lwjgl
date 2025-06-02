(ns clojure.gdx.backends.lwjgl
  (:require [clojure.gdx.backends.lwjgl.utils :as utils])
  (:import (com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application
                                             Lwjgl3ApplicationConfiguration)))

(defn display-mode
  ([monitor]
   (utils/display-mode->map (Lwjgl3ApplicationConfiguration/getDisplayMode (utils/map->monitor monitor))))
  ([]
   (utils/display-mode->map (Lwjgl3ApplicationConfiguration/getDisplayMode))))

(defn display-modes
  "The available display-modes of the primary or the given monitor."
  ([monitor]
   (map utils/display-mode->map (Lwjgl3ApplicationConfiguration/getDisplayModes (utils/map->monitor monitor))))
  ([]
   (map utils/display-mode->map (Lwjgl3ApplicationConfiguration/getDisplayModes))))

(defn primary-monitor
  "the primary monitor."
  []
  (utils/monitor->map (Lwjgl3ApplicationConfiguration/getPrimaryMonitor)))

(defn monitors
  "The connected monitors."
  []
  (map utils/monitor->map (Lwjgl3ApplicationConfiguration/getMonitors)))

(defn application [config listener]
  (Lwjgl3Application. listener
                      (utils/create-application-config config)))

(defn window
  "Creates a new Lwjgl3Window using the provided listener and Lwjgl3WindowConfiguration. This function only just instantiates a Lwjgl3Window and returns immediately. The actual window creation is postponed with Application.postRunnable(Runnable) until after all existing windows are updated."
  [application listener config]
  (Lwjgl3Application/.newWindow application
                                listener
                                (utils/create-window-config config)))

(defn set-gl-debug-message-control
  "Enables or disables GL debug messages for the specified severity level. Returns false if the severity level could not be set (e.g. the NOTIFICATION level is not supported by the ARB and AMD extensions). See Lwjgl3ApplicationConfiguration.enableGLDebugOutput(boolean, PrintStream)"
  [severity enabled?]
  (Lwjgl3Application/setGLDebugMessageControl (utils/k->gl-debug-message-severity severity)
                                              (boolean enabled?)))
