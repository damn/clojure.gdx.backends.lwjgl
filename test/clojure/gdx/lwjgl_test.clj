(ns clojure.gdx.lwjgl-test
  (:require [clojure.gdx.application :as app]
            [clojure.gdx.backends.lwjgl :as lwjgl]))

(defn -main []
  (.set org.lwjgl.system.Configuration/GLFW_LIBRARY_NAME "glfw_async")
  (let [display-mode (lwjgl/display-mode)]
    (println "display-mode: " display-mode)
    (println "primary monitor: " (lwjgl/primary-monitor))
    (lwjgl/application (reify app/Listener
                         (create [_])
                         (dispose [_])
                         (pause [_])
                         (render [_])
                         (resize [_ width height])
                         (resume [_]))
                       {:fullscreen-mode display-mode})))
