(ns clojure.gdx.backends.lwjgl.application
  (:require [clojure.gdx.application.listener :as listener]
            [clojure.gdx.backends.lwjgl.application.configuration :as config]
            [clojure.gdx.backends.lwjgl.application.gl-debug-message-severity :as gl-debug-message-severity]
            [clojure.gdx.backends.lwjgl.window.configuration :as window-config])
  (:import (com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application)))

(defn start! [listener config]
  (Lwjgl3Application. (listener/create listener)
                      (config/create config)))

(defn new-window! [application listener config]
  (Lwjgl3Application/.newWindow application
                                listener
                                (window-config/create config)))

(defn set-gl-debug-message-control!
  "Enables or disables GL debug messages for the specified severity level. Returns false if the severity level could not be set (e.g. the NOTIFICATION level is not supported by the ARB and AMD extensions). See `Lwjgl3ApplicationConfiguration.enableGLDebugOutput(boolean, PrintStream)`."
  [severity enabled?]
  (Lwjgl3Application/setGLDebugMessageControl (gl-debug-message-severity/k->value severity)
                                              (boolean enabled?)))
