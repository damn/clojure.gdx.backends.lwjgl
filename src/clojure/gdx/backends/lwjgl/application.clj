(ns clojure.gdx.backends.lwjgl.application
  (:require [clojure.gdx.backends.lwjgl.application.gl-debug-message-severity :as gl-debug-message-severity])
  (:import (com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application)))

(defn start! [listener configuration]
  (Lwjgl3Application. listener configuration))

(defn new-window! [application listener configuration]
  (Lwjgl3Application/.newWindow application listener configuration))

(defn set-gl-debug-message-control!
  "Enables or disables GL debug messages for the specified severity level. Returns false if the severity level could not be set (e.g. the NOTIFICATION level is not supported by the ARB and AMD extensions). See `Lwjgl3ApplicationConfiguration.enableGLDebugOutput(boolean, PrintStream)`."
  [severity enabled?]
  (Lwjgl3Application/setGLDebugMessageControl (gl-debug-message-severity/k->value severity)
                                              (boolean enabled?)))
