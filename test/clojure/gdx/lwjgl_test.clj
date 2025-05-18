(ns clojure.gdx.lwjgl-test
  (:require [clojure.gdx.backends.lwjgl :as lwjgl]))

; TODO FIXME can't call display-mode without setting GLFW async first

(defn -main []
  (let [display-mode (lwjgl/display-mode)]
    (println "display-mode: " display-mode)
    (println "primary monitor: " (lwjgl/primary-monitor))
    (lwjgl/application {:fullscreen-mode display-mode
                        :mac-os {:glfw-async? true}})))
