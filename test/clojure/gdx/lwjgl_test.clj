(ns clojure.gdx.lwjgl-test
  (:require [clojure.gdx.backends.lwjgl :as lwjgl]))

(defn -main []
  (.set org.lwjgl.system.Configuration/GLFW_LIBRARY_NAME "glfw_async")

  #_(let [display-mode (lwjgl/display-mode)]
      (println "display-mode: " display-mode)
      (println "primary monitor: " (lwjgl/primary-monitor))
      (lwjgl/application {:fullscreen-mode display-mode
                          :mac-os {:glfw-async? true}}))

  (lwjgl/application! {}
                      (proxy [com.badlogic.gdx.ApplicationAdapter] []))
  )
