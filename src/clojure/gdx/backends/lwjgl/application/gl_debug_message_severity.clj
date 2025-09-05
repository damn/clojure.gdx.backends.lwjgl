(ns clojure.gdx.backends.lwjgl.application.gl-debug-message-severity
  (:import (com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application$GLDebugMessageSeverity)))

(defn k->value [k]
  (case k
    :high         Lwjgl3Application$GLDebugMessageSeverity/HIGH
    :medium       Lwjgl3Application$GLDebugMessageSeverity/MEDIUM
    :low          Lwjgl3Application$GLDebugMessageSeverity/LOW
    :notification Lwjgl3Application$GLDebugMessageSeverity/NOTIFICATION))
