; FIXME call GLFW fns yourself then no need to convert
(ns clojure.gdx.backends.lwjgl.graphics.display-mode
  (:import (com.badlogic.gdx.backends.lwjgl3 Lwjgl3Graphics$Lwjgl3DisplayMode)))

(defn clojurize [^Lwjgl3Graphics$Lwjgl3DisplayMode display-mode]
  {:width          (.width        display-mode)
   :height         (.height       display-mode)
   :refresh-rate   (.refreshRate  display-mode)
   :bits-per-pixel (.bitsPerPixel display-mode)
   :monitor-handle (.getMonitor   display-mode)})

(defn javaize [{:keys [width height refresh-rate bits-per-pixel monitor-handle]}]
  (let [constructor (.getDeclaredConstructor Lwjgl3Graphics$Lwjgl3DisplayMode
                                             (into-array Class [Long/TYPE Integer/TYPE Integer/TYPE Integer/TYPE Integer/TYPE]))
        _ (.setAccessible constructor true)]
    (.newInstance constructor
                  (into-array Object [monitor-handle width height refresh-rate bits-per-pixel]))))
