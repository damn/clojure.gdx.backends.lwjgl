(ns clojure.gdx.lwjgl-test
  (:require [clojure.gdx.backends.lwjgl :as lwjgl]))

(defn -main []
  (.set org.lwjgl.system.Configuration/GLFW_LIBRARY_NAME "glfw_async")

  (let [foo ^{:monitor 1234} {:bar 1}]
    (meta foo)
    )
  (lwjgl/display-mode)
  {:width 1440, :height 900, :refresh-rate 60, :bits-per-pixel 24, :monitor-handle 140207961116912}

  (lwjgl/primary-monitor)
  {:virtual-x 0, :virtual-y 0, :name "Built-in Retina Display", :monitor-handle 140655926337712}

  #_(let [display-mode (lwjgl/display-mode)]
      (println "display-mode: " display-mode)
      (println "primary monitor: " (lwjgl/primary-monitor))
      (lwjgl/application {:fullscreen-mode display-mode
                          :mac-os {:glfw-async? true}}))

  (lwjgl/application {}
                     (proxy [com.badlogic.gdx.ApplicationAdapter] []))
  )
